<template>
  <div id="info" class="box_section">
    <img style="width: 100px" src="https://static.toss.im/lotties/error-spot-no-loop-space-apng.png" />
    <h2>결제를 실패했어요</h2>
    <p>{{ $route.query.message }}</p>
    <button class="button" @click="goToMain">다시 주문하기</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToMain = () => {
  router.push('/');
}

</script>

<style scoped>
@import '@/public/style.css';

</style>
