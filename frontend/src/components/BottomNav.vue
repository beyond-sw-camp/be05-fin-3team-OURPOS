<template>
  <v-footer>
    <v-bottom-navigation>

      <v-spacer></v-spacer>

      <v-btn :class="getButtonClass('/')" @click="goToHome">
        <v-icon :class="getIconClass('/')">mdi-home</v-icon>
        <span>홈</span>
      </v-btn>

      <v-spacer></v-spacer>

      <v-btn :class="getButtonClass('/order-check')" @click="goToOrderHistory">
        <v-icon :class="getIconClass('/order-check')">mdi-food</v-icon>
        <span>주문내역</span>
      </v-btn>

      <v-spacer></v-spacer>

      <v-btn :class="getButtonClass('/change-address')" @click="goToMyAddress">
        <v-icon :class="getIconClass('/change-address')">mdi-earth</v-icon>
        <span>주소 관리</span>
      </v-btn>

      <v-spacer></v-spacer>

      <v-btn :class="getButtonClass('/mypage')" @click="goToMyPage">
        <v-icon :class="getIconClass('/mypage')">mdi-account-outline</v-icon>
        <span>내 정보</span>
      </v-btn>

      <v-spacer></v-spacer>
    </v-bottom-navigation>
  </v-footer>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const goToMyPage = () => {
  router.push("/mypage");
};

const goToHome = () => {
  router.push("/");
};

const goToOrderHistory = () => {
  router.push("/order-check");
};

const goToMyAddress = () => {
  router.push("/change-address");
};

const getButtonClass = (path) => {
  return route.path === path ? 'btn-active' : '';
};

const getIconClass = (path) => {
  return route.path === path ? 'icon-active' : '';
};
</script>

<style scoped>
.btn-active {
  color: rgb(246, 162, 28);
}

.icon-active {
  color: rgb(246, 162, 28);
}
</style>
