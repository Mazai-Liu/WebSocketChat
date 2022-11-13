<template>
  <div class="container">
    <div class="columns is-mobile is-centered">
      <div class="column is-one-third">
        <LoginComponent />
      </div>
    </div>
  </div>
</template>

<script>
import LoginComponent from '@/components/LoginComponent.vue'

export default {
  name: 'LoginView',
  components: {
    LoginComponent
  }
}
</script>
