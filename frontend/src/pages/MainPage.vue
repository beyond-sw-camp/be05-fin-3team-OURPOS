<template>
  <v-app>
    <v-main>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-card>
              <v-card-title>메인 페이지</v-card-title>
              <v-card-text>
                <p>메인 페이지입니다.</p>
                <v-btn @click="hallOrder">홀/포장 주문</v-btn>
                <v-btn @click="deliveryOrder">배달 주문</v-btn>
                <v-btn @click="logout">로그아웃</v-btn>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from "axios";
const router = useRouter();

const hallOrder = () => {
  router.push('/stores');
};

const deliveryOrder = () => {
  router.push('/stores/delivery');
};

const logout = () => {
  axios.post('http://localhost:8080/logout', {}, {
    withCredentials: true
  }).then(() => {
    router.push('/login');
  });
};

</script>

<style scoped>

</style>
