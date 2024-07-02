<template>
  <v-container>
    <v-card-text class="mx-auto pl-12 pr-12" max-width="400" rounded="lg">
      <v-img class="mx-auto " max-width="228" src="/public/img/ourpos_logo.png"></v-img>

      <v-img
        class="mx-auto kakao-login-button"
        width="228"
        height="60"
        src="/public/img/login_kakao.png"
        @click="onKakaoLogin"
        @mousedown="scaleUp"
        @mouseup="scaleDown"
        @touchstart="scaleUp"
        @touchend="scaleDown"
      ></v-img>
      <v-img
        class="mx-auto my-5 naver-login-button"
        width="228"
        height="60"
        src="/public/img/login_naver_2.png"
        @click="onNaverLogin"
        @mousedown="scaleUp"
        @mouseup="scaleDown"
        @touchstart="scaleUp"
        @touchend="scaleDown"
      ></v-img>
      <v-row justify="center">
        <v-btn
          class="mx-auto my-5"
          width="228"
          height="60"
          rounded="xs"
          variant="tonal"
          color="primary"
          @click="testLogin"
        >
          <v-icon>mdi-account</v-icon>
          <span
            class="text-body-1 font-weight-bold"
          >테스트 계정으로 로그인</span>
        </v-btn>
      </v-row>

      <v-card class="my-5" color="surface-variant" variant="tonal">
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
import axios from "axios";

const router = useRouter();
const route = useRoute();

const onNaverLogin = () => {
  console.log('onNaverLogin');
  localStorage.clear();
  window.location.href = 'https://api.ourpos.org/oauth2/authorization/naver';
}

const onKakaoLogin = () => {
  console.log('onKakaoLogin');
  localStorage.clear();
  window.location.href = 'https://api.ourpos.org/oauth2/authorization/kakao';
}

const testLogin = async () => {
  console.log('testLogin');
  localStorage.clear();
  let response = await axios.post('https://api.ourpos.org/test/login');
  console.log(response);

  if (response.data.code === 200) {
    router.push('/');
  } else {
    alert('로그인에 실패했습니다.');
  }
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
