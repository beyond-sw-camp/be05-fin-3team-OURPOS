<template>
  <v-container>
    <HeaderPage title="주변 가게 찾기"/>
    <v-row>
      <v-col>
        <v-row>
          <v-col v-if="loading" cols="12" class="text-center">
            <v-progress-circular
              indeterminate
              color="primary"
            ></v-progress-circular>
            <p>현재 위치를 가져오는 중...</p>
          </v-col>
          <v-col v-else v-for="store in stores" :key="store.storeId" cols="12" md="6">
            <v-card
              @click="viewStore(store.storeId)"
              link
              class="mx-auto"
              max-width="400"
            >
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-avatar
                    color="grey"
                    size="110"
                    class="my-2 ml-2"
                  >
                    <v-img :src="'http://localhost:8080/images/' + store.pictureUrl"></v-img>
                  </v-avatar>
                </v-col>
                <v-col>
                  <v-card-title>{{ store.storeName }}</v-card-title>
                  <v-card-subtitle>
                    {{ store.addressResponseDto.addressBase }}
                  </v-card-subtitle>
                  <v-card-text>
                    <p style="color: red">{{ formatDistance(store.distance) }}</p>
                  </v-card-text>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>

  <BottomNav/>
</template>

<script setup>
import BottomNav from "@/components/BottomNav.vue";
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import HeaderPage from "@/components/AppHeader.vue";

const stores = ref([]);
const router = useRouter();
const loading = ref(true); // 초기 로딩 상태를 true로 설정합니다.

const findItems = async (latitude, longitude) => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/stores/hall', {
      withCredentials: true,
      params: {
        latitude: latitude,
        longitude: longitude
      }
    });
    stores.value = response.data.data;
  } catch (error) {
    console.error("Error fetching items:", error);
  } finally {
    loading.value = false; // 데이터를 모두 불러온 후 로딩 상태를 false로 변경합니다.
  }
};

const viewStore = (storeId) => {
  router.push('/stores/' + storeId +'/menus');
};

const formatDistance = (distance) => {
  if (distance < 1000) {
    return `${Math.round(distance)}m`;
  } else {
    return `${(distance / 1000).toFixed(2)}km`;
  }
};
const EXPIRATION_TIME = 10 * 60 * 1000; // 10 minutes in milliseconds

function setItemWithExpiration(key, value) {
  const now = new Date().getTime();
  const item = {
    value: value,
    timestamp: now
  };
  localStorage.setItem(key, JSON.stringify(item));
}

function getItemWithExpiration(key) {
  const itemStr = localStorage.getItem(key);

  if (!itemStr) {
    return null;
  }

  const item = JSON.parse(itemStr);
  const now = new Date().getTime();

  if (now - item.timestamp > EXPIRATION_TIME) {
    localStorage.removeItem(key);
    return null;
  }

  return item.value;
}

const latitude = getItemWithExpiration('latitude');
const longitude = getItemWithExpiration('longitude');

if (latitude && longitude) {
  findItems(latitude, longitude);
} else {
  navigator.geolocation.getCurrentPosition((position) => {
    const latitude = position.coords.latitude;
    const longitude = position.coords.longitude;
    findItems(latitude, longitude);

    setItemWithExpiration('latitude', latitude.toString());
    setItemWithExpiration('longitude', longitude.toString());
  }, (error) => {
    console.error("Error getting location:", error);
    loading.value = false; // 위치 정보를 가져오는 데 실패하면 로딩 상태를 false로 변경합니다.
  });
}
</script>

<style scoped>
</style>
