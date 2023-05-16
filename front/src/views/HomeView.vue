<template>
    <div class="home">
        <img alt="Vue logo" src="../assets/logo.png">
        <HelloWorld msg="환영 합니다."/>
    </div>
</template>

<script setup lang="ts">
import HelloWorld from '@/components/HelloWorld.vue';

</script>

<style scoped>
.home {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30px;
  height: 100vh;
}
</style>

