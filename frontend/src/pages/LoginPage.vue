<template>
  <v-container>
    <!--    <HeaderPage title="로그인"/>-->
    <v-card-text class="mx-auto pl-12 pr-12" max-width="400" rounded="lg">
      <v-img class="mx-auto my-8" max-width="228" src="../img/Five-Guys-logo.png"></v-img>

      <v-img
        class="mx-auto my-8 kakao-login-button"
        width="228"
        src="../img/login_kakao.png"
        @click="onKakaoLogin"
        @mousedown="scaleUp"
        @mouseup="scaleDown"
        @touchstart="scaleUp"
        @touchend="scaleDown"
      ></v-img>
      <v-img
        class="mx-auto my-8 naver-login-button"
        width="228"
        src="../img/login_naver_2.png"
        @click="onNaverLogin"
        @mousedown="scaleUp"
        @mouseup="scaleDown"
        @touchstart="scaleUp"
        @touchend="scaleDown"
      ></v-img>

      <v-card class="my-12" color="surface-variant" variant="tonal">
        <v-card-text class="text-medium-emphasis text-caption">
          <FontAwesomeIcon :icon="faCircleExclamation" class="me-1"/>
          본 페이지는 실제 서비스가 아닙니다. 프로젝트를 위해 만들어진 테스트 서비스 입니다!
        </v-card-text>
      </v-card>
    </v-card-text>
  </v-container>
</template>

<script setup>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faCircleExclamation } from "@fortawesome/free-solid-svg-icons";
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

const onNaverLogin = () => {
  console.log('onNaverLogin');
  localStorage.clear();
  window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
}

const onKakaoLogin = () => {
  console.log('onKakaoLogin');
  localStorage.clear();
  window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
}

const scaleUp = (event) => {
  event.target.style.transform = 'scale(1.1)';
}

const scaleDown = (event) => {
  event.target.style.transform = 'scale(1)';
}

// 회원가입 성공 페이지를 위한 경로 처리
if (route.path === '/signup-success') {
  alert('회원가입이 완료되었습니다!');
  router.push('/');
}
</script>

<style scoped>
.kakao-login-button, .naver-login-button {
  cursor: pointer;
  transition: transform 0.2s ease;
}
</style>
