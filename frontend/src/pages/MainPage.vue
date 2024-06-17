<template>
  <v-app>
    <v-app-bar app>
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
      <v-container>
        <v-row justify="center">
          <v-col cols="12" class="text-center">
            <v-card class="pa-5">
              <v-card-title>{{ customer.name }}님 안녕하세요!</v-card-title>
              <v-card-text>
                <v-btn class="ma-4" large @click="hallOrder" stacked>
                  <v-icon size="36" class="mb-2">mdi-food</v-icon>
                  홀/포장 주문
                </v-btn>
                <v-btn class="ma-4" large @click="deliveryOrder" stacked>
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
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from "axios";
import { onMounted, ref } from "vue";

const router = useRouter();
const customer = ref({});

const hallOrder = () => {
  router.push('/stores');
};

const deliveryOrder = () => {
  router.push('/stores/delivery');
};

const getMyInfo = async () => {
  await axios.get('http://localhost:8080/api/v1/customers/my', {
    withCredentials: true
  }).then((response) => {
    if (response.data.code === 200) {
      customer.value = response.data.data;
    } else {
      router.push('/login')
    }
  });
};

const logout = () => {
  axios.post('http://localhost:8080/logout', {}, {
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
</style>
