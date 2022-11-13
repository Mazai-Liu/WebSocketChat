<template>
    <NavbarComponent />
    <ChatComponent />
</template>

<script>
    import ChatComponent from '../components/ChatComponent.vue'
    import NavbarComponent from '../components/NavbarComponent.vue'

    export default {
        name: 'ChatView',
        components: {
            ChatComponent,
            NavbarComponent,
        }
    }
</script>
<style scoped>
</style>