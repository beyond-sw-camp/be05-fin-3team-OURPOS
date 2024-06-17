<template>
  <div id="info" class="box_section">
    <img style="width: 100px" src="https://static.toss.im/lotties/error-spot-no-loop-space-apng.png" />
    <h2>결제를 실패했어요</h2>
    <!-- 실패 메시지 출력 -->
    <p>{{ $route.query.message }}</p>
    <button class="button" @click="goToMain">주문 페이지로 돌아가기</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const deleteTempOrders = async () => {
  try {
  } catch (error) {
    console.error('Error deleting temp order:', error);
  }
};

const goToMain = () => {
  router.push('/pay');
}
deleteTempOrders();
</script>

<style scoped>
@import '@/public/style.css';

</style>
