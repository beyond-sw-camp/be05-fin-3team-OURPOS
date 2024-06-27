<template>
  <v-app>
    <v-app-bar app class="transparent-app-bar">
      <v-spacer></v-spacer>
      <v-menu offset-y>
        <template v-slot:activator="{ props }">
          <v-btn icon v-bind="props">
            <v-avatar size="40">
              <v-img :src="customer.profileImage"></v-img>
            </v-avatar>
          </v-btn>
        </template>
        <v-list>
          <v-list-item @click="logout">
            <v-list-item>
              <v-list-item-title><v-icon>mdi-logout</v-icon>로그아웃</v-list-item-title>
            </v-list-item>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <v-main>
       <!-- 배경 이미지가 적용된 v-main -->
      <div class="hero">
        <h1>Welcome to our pos!</h1>
        <p>Explore our menu and place your orders.</p>
      </div>

      <v-container class="main-container">
        <v-row justify="center">
          <v-col cols="12" class="text-center">
            <v-card class="pa-5">
              <v-card-title>{{ customer.name }}님 안녕하세요!</v-card-title>
              <v-card-text>
                <v-btn class="ma-2" large @click="hallOrder" stacked>
                  <v-icon size="36" class="mb-2">mdi-food</v-icon>
                  홀/포장 주문
                </v-btn>
                <v-btn class="ma-2" large @click="deliveryOrder" stacked>
                  <v-icon size="36" class="mb-2">mdi-truck-delivery</v-icon>
                  배달 주문
                </v-btn>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
  <BottomNav />
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from "axios";
import { onMounted, ref } from "vue";
import BottomNav from "@/components/BottomNav.vue";

const router = useRouter();
const customer = ref({});

const hallOrder = () => {
  router.push('/stores');
};

const deliveryOrder = () => {
  router.push('/stores/delivery');
};

const getMyInfo = async () => {
  await axios.get('https://api.ourpos.org/api/v1/customers/my', {
    withCredentials: true
  }).then((response) => {
    if (response.data.code === 200) {
      customer.value = response.data.data;
    } else {
      router.push('/login')
    }
  })

};

const logout = () => {
  axios.post('https://api.ourpos.org/logout', {}, {
    withCredentials: true
  }).then(() => {
    localStorage.clear();
    router.push('/login');
  });
};

onMounted(() => {
  getMyInfo();
});
</script>

<style scoped>
.v-avatar img {
  border-radius: 50%;
}

.fill-width {
  object-fit: cover;
  width: 100%;
}

.v-carousel .v-btn--icon::before {
  background-color: rgba(255, 255, 255, 0.5); /* 흰색 배경에 50% 불투명도 */
}

.v-main {
  background: url('@/assets/shake.jpg') no-repeat center center fixed;
  background-size: cover;
}

.main-container {
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  padding: 20px;
  margin-top: 235px;
}

.hero {
  margin-top: 250px;
  text-align: center;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.mt-5 {
  margin-top: 50px; /* 50px 상단 마진 */
}

.transparent-app-bar {
  background-color: transparent !important;
  box-shadow: none;
}


.bottom-nav{
  height: 65px;
  flex-shrink: 0;
}
</style>
