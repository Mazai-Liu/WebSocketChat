<template>
    <div>
        <nav class="navbar navbar-light navbar-expand-md bg-secondary py-3" style="height: 80px;margin-bottom: 10px">
            <div class="container">
                <a class="navbar-brand d-flex align-items-center" href="#">
                    <span class="d-flex justify-content-center align-items-center bs-icon-sm bs-icon-rounded bs-icon-primary me-2 bs-icon"><svg class="bi bi-bezier" xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M0 10.5A1.5 1.5 0 0 1 1.5 9h1A1.5 1.5 0 0 1 4 10.5v1A1.5 1.5 0 0 1 2.5 13h-1A1.5 1.5 0 0 1 0 11.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zm10.5.5A1.5 1.5 0 0 1 13.5 9h1a1.5 1.5 0 0 1 1.5 1.5v1a1.5 1.5 0 0 1-1.5 1.5h-1a1.5 1.5 0 0 1-1.5-1.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zM6 4.5A1.5 1.5 0 0 1 7.5 3h1A1.5 1.5 0 0 1 10 4.5v1A1.5 1.5 0 0 1 8.5 7h-1A1.5 1.5 0 0 1 6 5.5v-1zM7.5 4a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1z"></path>
                        <path d="M6 4.5H1.866a1 1 0 1 0 0 1h2.668A6.517 6.517 0 0 0 1.814 9H2.5c.123 0 .244.015.358.043a5.517 5.517 0 0 1 3.185-3.185A1.503 1.503 0 0 1 6 5.5v-1zm3.957 1.358A1.5 1.5 0 0 0 10 5.5v-1h4.134a1 1 0 1 1 0 1h-2.668a6.517 6.517 0 0 1 2.72 3.5H13.5c-.123 0-.243.015-.358.043a5.517 5.517 0 0 0-3.185-3.185z"></path>
                        </svg>
                    </span>
                    <span>
                        Brand
                    </span>
                </a>
                <button class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navcol-3">
                    <span class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span>
                </button>
                <div id="navcol-3" class="collapse navbar-collapse">
                    <button class="btn btn-light d-lg-flex align-items-lg-end" type="button" style="margin: 10px;"
                        v-on:click="showFileInput = !showFileInput">
                        编辑头像
                    </button>
                    <button class="btn btn-primary" type="button" v-on:click="logout">退出</button>
                </div>
            </div>
        </nav>
    </div>
    <FileInputComponentVue v-if="showFileInput" />
</template>
<script>
    import FileInputComponentVue from "./FileInputComponent.vue";
    export default {
        name: 'NavbarComponent',
        components:{
            FileInputComponentVue,
        },
        data() {
            return {
                showFileInput: false,
            }
        },
        methods: {
            logout(){
                // const user = JSON.parse(window.localStorage.getItem("user_info") || "{}");
                // const logoutForm = {
                //     userid: user.id,
                //     username: user.username,
                //     token: window.localStorage.getItem("token"),
                // };
                // this.$axios.post("http://localhost:8070/logout", logoutForm).then(
                //     res => {
                //         console.log(res);
                //     }
                // );

                window.localStorage.clear();
                window.sessionStorage.clear();
                this.$router.push('/');
            },
        },
    }
</script>
<style scoped>
</style>