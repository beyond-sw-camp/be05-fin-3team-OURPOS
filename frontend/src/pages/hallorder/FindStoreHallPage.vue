<template>
  <v-container>
    <v-app-bar app dark>
      <v-btn icon @click="goBack">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      <v-toolbar-title>주변 가게 찾기</v-toolbar-title>
    </v-app-bar>
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
              @click="openOrderTypeDialog(store)"
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

    <!-- Order Type Dialog -->
    <v-dialog v-model="orderTypeDialog" max-width="800">
      <v-card>
        <v-card-title>{{ selectedStore.storeName }}</v-card-title>
        <v-card-subtitle>{{ selectedStore.addressResponseDto.addressBase }}</v-card-subtitle>
        <v-img :src="'http://localhost:8080/images/' + selectedStore.pictureUrl"></v-img>
        <v-card-text>
          <p><FontAwesomeIcon :icon="faPhone"/> 전화번호: {{ selectedStore.storePhone }}</p>
          <p><FontAwesomeIcon :icon="faClock"/> 영업 시간: {{ selectedStore.openTime }} - {{ selectedStore.closeTime }}</p>
          <p><FontAwesomeIcon :icon="faMapLocation"/> 거리: {{ formatDistance(selectedStore.distance) }}</p>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary"
                 prepend-icon="mdi-silverware-fork-knife"
                 @click="setOrderType(false)" stacked>
            매장 식사
          </v-btn>
          <v-btn color="primary"
                 prepend-icon="mdi-shopping"
                 @click="setOrderType(true)" stacked>
            테이크아웃
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
  <BottomNav />
</template>

<script setup>
import BottomNav from "@/components/BottomNav.vue";
import {ref} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faClock, faMapLocation, faPhone, faShoppingBag} from "@fortawesome/free-solid-svg-icons";

const stores = ref([]);
const router = useRouter();
const loading = ref(true);

const orderTypeDialog = ref(false);
const selectedStore = ref({});

const openOrderTypeDialog = (store) => {
  selectedStore.value = store;
  orderTypeDialog.value = true;
};

const setOrderType = (orderTakeoutYn) => {
  viewStore(selectedStore.value.storeId, selectedStore.value.storeName, orderTakeoutYn);
  orderTypeDialog.value = false;
};

const EXPIRATION_TIME = 10 * 60 * 1000; // 10분

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

const findItems = async (latitude, longitude) => {
  const cachedStores = getItemWithExpiration('hallStores');
  if (cachedStores) {
    stores.value = cachedStores;
    loading.value = false;
    return;
  }

  try {
    const response = await axios.get('http://localhost:8080/api/v1/stores/hall', {
      withCredentials: true,
      params: {
        latitude: latitude,
        longitude: longitude
      }
    });
    if (response.data.code !== 200) {
      console.error('Error fetching items:', response.data);
      return;
    }
    stores.value = response.data.data;
    setItemWithExpiration('hallStores', stores.value);
  } catch (error) {
    console.error('Error fetching items:', error);
  } finally {
    loading.value = false;
  }
};

const viewStore = (storeId, storeName, orderTakeoutYn) => {
  let storageFullOrder = localStorage.getItem('fullOrder');
  if (storageFullOrder !== null) {
    storageFullOrder = JSON.parse(storageFullOrder);
    storageFullOrder.storeId = storeId;
    storageFullOrder.storeName = storeName;
    storageFullOrder.orderTakeoutYn = orderTakeoutYn;
    console.log('storageFullOrder:', storageFullOrder);
    localStorage.setItem('fullOrder', JSON.stringify(storageFullOrder));
    router.push('/stores/' + storeId + '/menus');
    return;
  }

  const fullOrder = {
    storeId: storeId,
    storeName: storeName,
    orderTakeoutYn: orderTakeoutYn,
    orderDetailDtos: []
  };

  localStorage.setItem('fullOrder', JSON.stringify(fullOrder));
  router.push('/stores/' + storeId + '/menus');
};

const formatDistance = (distance) => {
  if (distance < 1000) {
    return `${Math.round(distance)}m`;
  } else {
    return `${(distance / 1000).toFixed(2)}km`;
  }
};

const latitude = getItemWithExpiration('latitude');
const longitude = getItemWithExpiration('longitude');

const initMap = () => {
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
      console.error('Error getting location:', error);
      loading.value = false;
    });
  }
}

initMap();

const goBack = () => {
  router.push('/');
};
</script>

<style scoped>
</style>
