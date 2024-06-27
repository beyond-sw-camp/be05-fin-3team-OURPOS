<template>
  <div class="bg-white">
    <main class="mt-0 main-content">
      <section>
        <div class="page-header min-vh-100">
          <div class="container">
            <div
                class="col-6 d-lg-flex d-none h-100 my-auto pe-0 ps-0 position-absolute top-0 start-0 text-center justify-content-center flex-column"
            >
              <div
                  class="position-relative h-100 m-3 px-7 border-radius-lg d-flex flex-column justify-content-center"
                  :style="{
                  backgroundImage: `url(${illustrationImage})`,
                }"
              ></div>
            </div>
            <div
                class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column ms-auto me-auto ms-lg-auto me-lg-5"
            >
              <div class="card card-plain">
                <div class="pb-0 card-header bg-transparent mb-4">
                  <h3 class="font-weight-bolder">로그인</h3>
                  <p class="mb-0">아이디와 패스워드를 입력해주세요!</p>
                </div>
                <form class="card-body" @submit.prevent="login">
                  <div class="mb-3">
                    <material-input
                        id="username"
                        :value="username"
                        @input="username = $event.target.value"
                        type="text"
                        label="아이디"
                        size="lg"
                    />
                  </div>
                  <div class="mb-3">
                    <material-input
                        id="password"
                        :value="password"
                        @input="password = $event.target.value"
                        type="password"
                        label="비밀번호"
                        size="lg"
                    />
                  </div>
                  <material-checkbox
                      id="flexCheckDefault"
                      class="font-weight-light"
                      checked
                  >
                    I agree the
                    <a
                        href="../../../pages/privacy.html"
                        class="text-dark font-weight-bolder"
                    >Terms and Conditions</a
                    >
                  </material-checkbox>
                  <div class="text-center">
                    <material-button
                        class="mt-4"
                        variant="gradient"
                        color="success"
                        fullWidth
                        size="lg"
                    ><span>로그인</span></material-button
                    >
                  </div>
                </form>
                <div class="px-1 pt-0 text-center card-footer px-lg-2">
                  <p class="mx-auto mb-4 text-sm">
                    Don't have an account?
                    <router-link
                        :to="{ name: 'SignUp' }"
                        class="text-success text-gradient font-weight-bold"
                    >로그인</router-link
                    >
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';
import { useRouter } from 'vue-router';

import MaterialInput from "@/components/MaterialInput.vue";
import MaterialCheckbox from "@/components/MaterialCheckbox.vue";
import MaterialButton from "@/components/MaterialButton.vue";

const username = ref('');
const password = ref('');
const illustrationImage = require('@/assets/img/illustrations/illustration-signin.jpg');
const store = useStore();
const router = useRouter();
import {parseJwt} from "@/utils/auth";
const body = document.getElementsByTagName("body")[0];

const updateBodyClass = (revert = false) => {
  if (revert) {
    body.classList.add("bg-gray-100");
  } else {
    body.classList.remove("bg-gray-100");
  }
  store.commit("toggleEveryDisplay");
  store.commit("toggleHideConfig");
};

onMounted(() => {
  updateBodyClass();
});

onBeforeUnmount(() => {
  updateBodyClass(true);
});

const login = async () => {
  try {
    const formData = new FormData();
    formData.append('username', username.value);
    formData.append('password', password.value);
    const response = await axios.post('https://api.ourpos.org/managers/login', formData);
    if (response.status === 200) {
      const token = response.headers.authorization;
      console.log(token);
      const role = parseJwt(token).role;
      console.log(role);
      localStorage.setItem('token', token);

      if (role === 'ROLE_SUPER_ADMIN') {
        await router.push('/owner');
      } else if (role === 'ROLE_ADMIN') {
        await router.push('/manager');
      } else {
        await router.push('/sign-up');
      }
    } else {
      alert('아이디와 비밀번호를 확인해주세요.');
    }
  } catch (e) {
    if (e.response && e.response.status === 401) {
      alert('아이디와 비밀번호를 확인해주세요.');
    } else {
      console.log("Login error: ", e);
      alert('로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
    }
  }
};
</script>

<style scoped>
</style>
