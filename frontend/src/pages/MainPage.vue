<template>
    <v-app-bar color="#f5f5f5" app dark>
    <v-toolbar-title>
      <img src="/public/img/ourpos_logo.png" height="80" alt="OurPOS" class="mt-3"/>
    </v-toolbar-title>
    <v-toolbar-items>
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
              <v-list-item-title>
                <v-icon>mdi-logout</v-icon>
                로그아웃
              </v-list-item-title>
            </v-list-item>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-toolbar-items>
  </v-app-bar>
  <v-main class="d-flex flex-column align-center fill-height main-background">
    <div class="text-center hero" style="margin-top: 10vh;">
      <h1 class="display-1">Welcome to OurPOS!</h1>
      <p class="subtitle-1">Explore our menu and place your orders.</p>
    </div>
    <v-container class="d-flex justify-center align-center fill-height hero">
      <v-row justify="center">
        <v-col cols="12" class="text-center">
          <h2>{{ customer.name }}님 안녕하세요!</h2>
          <v-card-text>
            <v-btn
              class="ma-3"
              width="120"
              height="120"
              rounded="xl"
              stacked
              @click="hallOrder"
            >
              <v-icon size="50" class="mb-2">mdi-food</v-icon>
              홀/포장 주문
            </v-btn>
            <v-btn
              class="ma-3"
              width="120"
              height="120"
              rounded="xl"
              stacked
              @click="deliveryOrder">
              <v-icon size="60" class="mb-2">mdi-moped</v-icon>
              배달 주문
            </v-btn>
          </v-card-text>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
  <BottomNav/>
</template>

<script setup>
import {useRouter} from 'vue-router';
import axios from "axios";
import {onMounted, ref} from "vue";
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
  .catch(() => {
    router.push('/login')
  });
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
.fill-height {
  height: 100vh;
}

.main-background {
  background-color: #f5f5f5;
}

.hero {
  text-align: center;
}

</style>
