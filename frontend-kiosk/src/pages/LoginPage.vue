<template>
  <v-container>
    <v-card
      class="mx-auto pa-16 my-16"
      elevation="8"
      max-width="600"
      height="1000"
      rounded="lg"
    >
      <v-img
        class="mx-auto pa-6"
        max-width="456"
        src="https://cdn.vuetifyjs.com/docs/images/logos/vuetify-logo-v3-slim-text-light.svg"
      ></v-img>

      <div class="text-subtitle-1 text-medium-emphasis">ID</div>

      <v-text-field
        placeholder="아이디를 입력하세요"
        prepend-inner-icon="mdi-account-outline"
        variant="outlined"
        v-model="username"
      ></v-text-field>

      <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
        PASSWORD
      </div>

      <v-text-field
        :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
        :type="visible ? 'text' : 'password'"

        placeholder="비밀번호를 입력하세요"
        prepend-inner-icon="mdi-lock-outline"
        variant="outlined"
        v-model="password"
        @click:append-inner="visible = !visible"
      ></v-text-field>

      <v-btn
        class="mb-8"
        color="primary"
        size="x-large"
        variant="outlined"
        block
        @click="login"
      >
        <v-icon size="30" class="mr-2">mdi-login</v-icon>
        <span>로그인</span>
      </v-btn>
      <v-card
        class="mb-12"
        color="surface-variant"
        variant="tonal"
      >
        <v-card-text class="text-medium-emphasis text-caption">
          점주용 키오스크 로그인
        </v-card-text>
      </v-card>

      <h2>테스트 로그인</h2>
      <v-expansion-panels>
        <v-expansion-panel
          title="강남점 로그인"
          text="id: admin1, pw: 1234"
        >
        </v-expansion-panel>
        <v-expansion-panel
          title="고속터미널점 로그인"
          text="id: admin2, pw: 1234"
        >
        </v-expansion-panel>
        <v-expansion-panel
          title="서울역점 로그인"
          text="id: admin3, pw: 1234"
        >
        </v-expansion-panel>
        <v-expansion-panel
          title="여의도점 로그인"
          text="id: admin4, pw: 1234"
        >
        </v-expansion-panel>
        <v-expansion-panel
          title="신대방삼거리점 로그인"
          text="id: admin5, pw: 1234"
        >
        </v-expansion-panel>
      </v-expansion-panels>
    </v-card>
  </v-container>
</template>

<script setup>
import router from "@/router";
import {ref} from "vue";
import axios from "axios";
import {parseJwt} from "@/utils/auth";

const visible = ref(false);

const username = ref('');
const password = ref('');

const login = async () => {
  try {
    const formData = new FormData();
    formData.append('username', username.value);
    formData.append('password', password.value);
    console.log(username.value);
    console.log(password.value);
    const response = await axios.post('https://api.ourpos.org/managers/login', formData);
    if (response.status === 200) {
      const token = response.headers.authorization;
      console.log(token);
      const role = parseJwt(token).role;
      console.log(role);
      localStorage.setItem('token', token);

      if (role === 'ROLE_SUPER_ADMIN') {
        alert('아이디와 비밀번호를 확인해주세요.');
      }

      router.push('/');
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
span {
  font-weight: 700;
  font-size: 30px;
}

h2 {
  font-size: 30px;
  font-weight: 700;
  margin-bottom: 20px;
}
</style>
