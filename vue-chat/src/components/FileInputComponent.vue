<template>
    <div class="card" 
    style="position: absolute;left: 41%;top: 35%;z-index: 1">
        <div class="card-header">
            <h5 class="mb-0">上传头像</h5>
        </div>
        <div class="card-body">
            <div style="width: 200px;">
                <input v-on:change="changeFile" class="form-control custom-file-input" type="file" />
                <div class="text-center"><label id="user_group_label" class="form-label" for="user_group_logo"><i class="fas fa-upload"></i> Choose an image...</label></div>
                <div class="text-center mt-2">
                    <button v-bind:disabled="!couldUpload" class="btn btn-primary" type="button" v-on:click="upload">Upload</button>
                </div>
            </div>
            <p class="card-text"></p>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'FileInputComponent',
        data(){
            return {
                file: '',
                user: JSON.parse(window.localStorage.getItem("user_info") || "{}"),
                token: localStorage.getItem("token") || "",
                couldUpload: true,
            }
        },
        methods:{
            changeFile(event){
                console.log(event.target.files[0])
                this.file = event.target.files[0];
            },
            upload(){
                if(this.couldUpload == false){
                    alert('正在上传修改。。请稍后');
                    return;
                }
                if(this.file == ''){
                    alert("请上传文件");
                }else{
                    this.couldUpload = false;
                    const setAvatarForm = {
                        'id': this.user.id,
                        'file': this.file,
                    };
                    this.$axios.post("http://localhost:8070/setAvatar", setAvatarForm, 
                        {
                            headers: {'Content-Type': 'multipart/form-data', 'Authorization': this.token}
                        }
                    ).then(
                        res => {
                            const data = res.data;
                            if(data.code == "999"){
                                console.log(data)
                                localStorage.setItem("user_info", JSON.stringify(data.data));
                                sessionStorage.clear();
                                window.location.reload();
                            }
                        }, err => {
                            alert('出错！');
                            this.couldUpload = true;
                        }
                    );
                }
            },
        }
    }
</script>
<style scoped>

</style>