<template>
  <section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5"></div>
        <h1 class="text-center">Lec-Chat</h1>
        <div class="row d-flex justify-content-center">
            <div class="col-md-6 col-xl-4">
                <div class="card mb-5">
                    <div class="card-body d-flex flex-column align-items-center">
                        <div class="bs-icon-xl bs-icon-circle bs-icon-primary bs-icon my-4"><svg class="bi bi-person" xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16">
                                <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"></path>
                            </svg></div>
                        <form class="text-center">
                            <div class="mb-3">
                              <input v-model="loginForm.username" placeholder="Username" 
                              class="form-control" type="text" name="username"/>
                            </div>
                            <div class="mb-3">
                              <input v-model="loginForm.password"
                              class="form-control" type="password" name="password" placeholder="Password" />
                            </div>
                            <div class="mb-3">
                              <button v-on:click="login" type="button" class="btn btn-primary d-block w-100">Login</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
      </div>
  </section>

<!-- 

  <div class="card">
    <label class="label is-large is-centered">Lec-Chat</label>
    <div class="field">
      <p class="control has-icons-left has-icons-right">
        <input class="input is-small" type="text" v-model="loginForm.username" placeholder="username">
        <span class="icon is-small is-left">
          <i class="fas fa-envelope"></i>
        </span>
        <span class="icon is-small is-right">
          <i class="fas fa-check"></i>
        </span>
      </p>
    </div>
    <div class="field">
      <p class="control has-icons-left">
        <input class="input is-small" type="password" v-model="loginForm.password" placeholder="password">
        <span class="icon is-small is-left">
          <i class="fas fa-lock"></i>
        </span>
      </p>
    </div>
    <div class="field is-grouped">
      <div class="control">
        <button class="button is-success" type="button" v-on:click="login">登录</button>
      </div>
      <div class="control">
        <button class="button is-info is-right" type="button" v-on:click="">注册</button>
      </div>
    </div>
  </div>   -->
</template>

<script>


export default {
  name: 'LoginComponent',

  data(){
    return {
      loginForm: {
        username: '',
        password: '',
      },
    }
  },
  methods: {
    login(){
      if(this.loginForm.username.trim() == "Lec"){
        alert("用户名不能为Lec！");
        return;
      }
      this.$axios.post("http://192.168.123.155:8070/login", this.loginForm).then(
        res => {
          console.log(res);
          if(res.data.code == "1001"){
            window.localStorage.setItem("token",res.data.data.token);
            const user_info = JSON.stringify(res.data.data);
            window.localStorage.setItem("user_info", user_info);
            this.$router.push('/chat');
          }else{
            alert(res.data.msg)
          }
        }
      )
    },
  },
}
</script>
