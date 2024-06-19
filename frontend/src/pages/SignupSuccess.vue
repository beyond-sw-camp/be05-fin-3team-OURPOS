<template>
  <v-sheet
    class="pa-4 text-center mx-auto my-16"
    rounded="lg"
    width="100%"
  >
    <v-icon
      class="mb-5"
      color="success"
      icon="mdi-check-circle"
      size="112"
    ></v-icon>

    <h2 class="text-h5 mb-3">회원가입이 완료되었습니다!</h2>

    <p class="mb-4 text-medium-emphasis text-body-2">
      바로 서비스를 이용하실 수 있습니다.
    </p>

    <!--    <v-divider class="mb-4"></v-divider>-->
    <canvas ref="confettiCanvas" class="confetti-canvas"></canvas>

    <div class="end">
      <v-btn
        class="text-none my-5"
        color="success"
        variant="flat"
        width="120"
        @click="navigateHome"
      >
        서비스 이용하기
      </v-btn>
    </div>
  </v-sheet>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import confetti from 'canvas-confetti';

const confettiCanvas = ref(null);

const navigateHome = () => {
  window.location.href = 'http://localhost:3000';
};

const launchConfetti = () => {
  const canvas = confettiCanvas.value;
  const myConfetti = confetti.create(canvas, {
    resize: true,
    useWorker: true
  });

  myConfetti({
    particleCount: 200,
    spread: 160,
    origin: { y: 0.6 }
  });
};

onMounted(() => {
  launchConfetti();
});
</script>

<style scoped>
.confetti-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}
</style>
