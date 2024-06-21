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
      <!-- 사진 슬라이더 추가 부분 -->
      <v-row justify="center">
          <v-col cols="12">
            <v-carousel cycle :interval="3000" height="300" hide-delimiters>
              <v-carousel-item v-for="(image, i) in images" :key="i">
                <v-img :src="image" class="fill-width"></v-img>
              </v-carousel-item>
            </v-carousel>
          </v-col>
        </v-row>
        <!-- 사진 슬라이더 추가 부분 끝 -->
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
  <BottomNav />
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from "axios";
import { onMounted, ref } from "vue";
import BottomNav from "@/components/BottomNav.vue";

const router = useRouter();
const customer = ref({});
const images = ref([
  'https://res.klook.com/images/fl_lossy.progressive,q_65/c_fill,w_1200,h_630/w_80,x_15,y_15,g_south_west,l_Klook_water_br_trans_yhcmh3/activities/yvboar8gqwcjmdvnuwug/Shake%20Shack%20in%20Singapore.jpg',
  'https://images.fastcompany.com/image/upload/f_auto,c_fit,w_3840,q_auto/wp-cms-2/2024/05/p-1-91128528-shake-shack-summer-menu-mania-bbq.jpg',
  'https://shakeshack.com/sites/default/files/styles/blog_teaser/public/2024-01/2024-01_SHA_Korean-Style_Blog_1920x1080.jpg?h=d1cb525d&itok=xMWmUpTW'
]);

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

.fill-width {
  object-fit: cover; /* 이미지를 컨테이너에 맞추어 채우기 */
  width: 100%; /* 가로로 꽉 차게 하기 */
}


.v-carousel .v-btn--icon::before {
  background-color: rgba(255, 255, 255, 0.5); /* 흰색 배경에 50% 불투명도 */
}
</style>
