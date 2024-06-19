<template>
  <v-main>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" class="text-center">
          <h1>방문 해주셔서 감사합니다!</h1>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="auto">
          <v-btn
            stacked
            width="250"
            height="250"
            class="ma-6"
            variant="outlined"
            rounded="xl"
            color="primary"
            @click="goToMenus(false)"
          >
            <v-icon size="90" class="mb-2">mdi-food</v-icon>
            <span>먹고가요</span>
          </v-btn>
        </v-col>
        <v-col cols="auto">
          <v-btn
            stacked
            width="250"
            height="250"
            class="ma-6"
            variant="outlined"
            rounded="xl"
            color="primary"
            @click="goToMenus(true)"
          >
            <v-icon size="90" class="mb-2">mdi-shopping</v-icon>
            <span>포장해요</span>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToMenus = (isTakeOut) => {
  const orders = {
    orderTakeoutYn: isTakeOut,
    orderDetailDtos: [],
    expiry: new Date().getTime() + 5 * 60 * 1000 // 5분 후 만료 시간 설정
  };
  localStorage.setItem('orders', JSON.stringify(orders));
  router.push('/menus');
};

</script>

<style scoped>
span {
  font-weight: 700;
  font-size: 30px;
}

h1 {
  font-size: 40px;
  font-weight: 700;
  margin-top: 50px;
  margin-bottom: 50px;
}

h2 {
  font-size: 30px;
  font-weight: 700;
  margin-bottom: 20px;
}
</style>
