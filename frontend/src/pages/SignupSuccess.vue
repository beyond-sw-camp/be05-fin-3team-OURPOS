<template>
  <v-app>
    <v-main>
      <v-container class="text-center">
        <v-row class="justify-center">
          <v-col cols="12" md="8">
            <v-card class="pa-4">
              <v-card-title class="headline">
                FIVEGUYS 회원가입을 축하합니다!
              </v-card-title>
              <v-card-text>
                <div ref="confettiCanvas" class="confetti-canvas"></div>
                <div class="mt-4">
                  <v-btn @click="navigateHome" color="primary" rounded large>
                    서비스 이용하기
                  </v-btn>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import confetti from 'canvas-confetti';

const router = useRouter();
const confettiCanvas = ref(null);

const navigateHome = () => {
  window.location.href = 'http://localhost:3000';
};

const launchConfetti = () => {
  confetti.create(confettiCanvas.value, {
    resize: true,
    useWorker: true
  })({
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
