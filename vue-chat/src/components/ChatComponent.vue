<template>
    <div class="container">
        <div class="row">
            <div class="col-md-2">
                <img v-bind:src="this.$github_url + user.avatar" class="rounded-circle mx-auto" style="width: 25%;" />
                <b v-bind:style="{'font-size': '30px', 'color': isOnline ? 'green' : 'red',}"> · </b> &nbsp;
                {{ user.username }}
            <br>在线人数：{{ headcount }}
            <ul class="list-group">
                <li v-for="(item, index) in onlines" 
                v-bind:key="index" 
                v-on:click="showChat(item[0])" 
                class="list-group-item"
                style="cursor:pointer">
                    <span>
                        <img v-bind:src="this.$github_url + avatarMap.get(item[0])" style="width: 15%">
                        {{ item[0] }}
                        <span v-if="item[1] != 0">
                            <b style="font-size: 20px; color: red">&nbsp; {{ item[1] }}</b>
                        </span>
                    </span>
                </li>
            </ul>
            </div>
            <div class="col-md-10">
                <div v-if="to != ''" class="card"> 
                    <div class="card-header">
                        <h3 class="text-center">{{ to }} </h3>
                    </div>
                    <div class="card-body" style="overflow: auto;height: 500px" name="display">
                        <ul class="list-group" style="list-style:none">
                            <li v-bind:style="{'text-align': item.fromName != user.username ? 'left' : 'right'}"
                                v-for="(item, index) of records" v-bind:key="index"
                            >
                                <img class="rounded-circle" v-if="item.fromName != user.username" v-bind:src="this.$github_url + item.fromAvatar" style="height: 30px;">
                                    {{ item.time }}#{{ item.fromName }} &nbsp;{{ item.content }}
                                <img class="rounded-circle" v-if="item.fromName == user.username" v-bind:src="this.$github_url + item.fromAvatar" style="height: 30px;">
                            </li>
                        </ul>
                    </div>

                    <div class="card-header" name="inputbox">
                        <input class="form-control-plaintext" type="text" v-model.trim="content"/>
                        
                        <div v-if="showEmojiBox">
                            <Picker style="position: absolute;left: 700px;top:0" :data="emojiIndex" set="twitter" @select="showEmoji" />
                        </div>
                        <div class="btn-group float-end" role="group">
                            <button class="btn btn-primary" type="button"
                                    style="border-right-color: white"
                            v-on:click="showEmojiBox = !showEmojiBox">Emoji</button>
                            <button class="btn btn-primary" type="button" v-on:click="sendMessage">发送</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
    </div>
</template>
<script>

    // Emoji
    import data from "emoji-mart-vue-fast/data/all.json";
    import "emoji-mart-vue-fast/css/emoji-mart.css";
    import { Picker, EmojiIndex } from "emoji-mart-vue-fast/src";
    let emojiIndex = new EmojiIndex(data);

    export default {
        name: 'ChatComponent',
        components: {
            Picker,
        },
        data(){
            return {
                user: JSON.parse(window.localStorage.getItem("user_info") || "{}"),
                token: window.localStorage.getItem("token") || "",
                isOnline: false,
                onlines: new Map(),
                avatarMap: new Map(),
                to: '',
                content: '',
                records: [],
                headcount: 0,
                showEmojiBox: false,
                

                // Emoji
                emojiIndex: emojiIndex,
                emojisOutput: ""
            }
        },
        computed: {
            ws: function() {
                    return new WebSocket("ws://192.168.123.155:8070/websocket/chat" + "?token=" + this.token);
				},
        },
        methods:{
            sendMessage(){
                if(this.content.trim() != ''){
                    console.log("sendMessage:" + this.content)
                    const json = {"toName": this.to, "fromAvatar": this.user.avatar,"content": this.content};
                    this.content = '';
                    this.ws.send(JSON.stringify(json));
                }
            },
            showChat(toName){
                this.to = toName;
                this.records = JSON.parse(window.sessionStorage.getItem(toName));
                if(this.records == null){
                    this.$axios.post("http://192.168.123.155:8070/getRecords", {"fromName": this.user.username, "toName": this.to},{
                            headers: {'Authorization': this.token}
                        }).then(
                        res => {
                            const data = res.data;
                            if(data.code == "999"){
                                this.records = data.data;
                                window.sessionStorage.setItem(toName, JSON.stringify(data.data));
                            }
                        }
                    );
                }
                this.onlines.set(toName, 0);
            },

            // Emoji
            showEmoji(emoji) {
                this.content += emoji.native;
            }
        },
        mounted(){
            this.ws.onopen = () => {
                console.log("Connection Created");
                this.isOnline = true;
            };
            this.ws.onclose = () => {
                console.log("Connection Closed");
                this.isOnline = false;
                
            };


            this.ws.onmessage = (event) => {
              console.log("Receive Message");
              
              var data = event.data;
              data = JSON.parse(data);

              if(data.isListUsers){
                data.message = data.message.filter((item) => item.username != this.user.username);
                this.onlines.clear();

                const map = new Map();
                this.avatarMap.clear();
                data.message.forEach(nameAndAvatar => {
                    const newUser = nameAndAvatar.username;
                    const newAvatar = nameAndAvatar.avatar;
                    this.avatarMap.set(newUser, newAvatar);
                    if(this.onlines.has(newUser)){
                        map.set(newUser, this.onlines.get(newUser));
                    }else{
                        map.set(newUser, 0);
                    }
                });
                
                this.onlines = map;
            
                this.headcount = data.headcount;
                console.log(data);
              }else if(data.isSingle){
                const newrecord = data.message;
                const username = this.user.username;
                var oldrecords;
                console.log(newrecord)
                if(newrecord.toName == "Lec"){
                    oldrecords = JSON.parse(window.sessionStorage.getItem("Lec"));
                    if(oldrecords == null)
                        oldrecords = [];
                    oldrecords.push(newrecord)
                    window.sessionStorage.setItem(newrecord.toName, JSON.stringify(oldrecords));

                    if(this.to == "Lec")
                        this.records = oldrecords;

                    if(this.to != "Lec"){
                        const oldValue = this.onlines.get("Lec");
                        this.onlines.set("Lec", oldValue + 1);
                    }
                    
                }else{
                    if(newrecord.toName == username){
                        oldrecords = JSON.parse(window.sessionStorage.getItem(newrecord.fromName));
                    if(oldrecords == null)
                        oldrecords = [];
                        oldrecords.push(newrecord)
                        window.sessionStorage.setItem(newrecord.fromName, JSON.stringify(oldrecords));

                        if(this.to != newrecord.fromName){
                            const oldValue = this.onlines.get(newrecord.fromName);
                            this.onlines.set(newrecord.fromName, oldValue + 1);
                        }
                    }else if(newrecord.fromName == username){
                        oldrecords = JSON.parse(window.sessionStorage.getItem(newrecord.toName));
                        if(oldrecords == null)
                            oldrecords = [];
                        oldrecords.push(newrecord)
                        window.sessionStorage.setItem(newrecord.toName, JSON.stringify(oldrecords));
                    }

                    if(this.to == newrecord.toName || this.to == newrecord.fromName)
                        this.records = oldrecords;
                    } 
              }else if(data.isSystem){

              }
            };
        }
    }
</script>
<style scoped>
</style>