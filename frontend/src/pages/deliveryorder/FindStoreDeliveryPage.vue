<template>
  <v-container>
    <v-app-bar app dark>
      <v-btn icon @click="goBack">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      <v-toolbar-title>배달 주문</v-toolbar-title>
    </v-app-bar>
    <v-row>
      <v-col>
        <v-row>
          <v-col v-if="loading" cols="12" class="text-center">
            <v-progress-circular
              indeterminate
              color="primary"
            ></v-progress-circular>
            <p>배달 가능 가게 가져오는 중...</p>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" class="text-center" v-show="!loading">
            <v-card
              link
              @click="router.push('/stores/delivery/address')"
            >
              <v-card-text>
                <FontAwesomeIcon :icon="faMapSigns"/>
                {{ address.addressBase }}
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        <v-row>
          <v-col v-for="store in stores" :key="store.storeId" cols="12" md="6">
            <v-card
              link
              class="mx-auto"
              max-width="400"
              @click="viewStore(store.storeId, store.storeName, store.minimumOrderPrice)"
              :disabled="calculateTime(store.duration, store.distance) === '배달 불가'"
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
                    <p style="color: red">
                      <FontAwesomeIcon :icon="faCar"/>
                      {{ calculateTime(store.duration, store.distance) }}
                    </p>
                  </v-card-text>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
  <BottomNav />
</template>

<script setup>
import BottomNav from "@/components/BottomNav.vue";
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faCar, faMapSigns } from "@fortawesome/free-solid-svg-icons";

const stores = ref([]);
const router = useRouter();
const loading = ref(true);
const addresses = ref([]);
const address = ref({});

const setLocalStorageWithExpiry = (key, value, expiryInMinutes) => {
  const now = new Date();
  const expiry = now.getTime() + expiryInMinutes * 60000;

  const item = {
    value: value,
    expiry: expiry
  };

  localStorage.setItem(key, JSON.stringify(item));
};

const getLocalStorageWithExpiry = (key) => {
  const itemStr = localStorage.getItem(key);

  if (!itemStr) {
    return null;
  }

  const item = JSON.parse(itemStr);
  const now = new Date();

  if (now.getTime() > item.expiry) {
    localStorage.removeItem(key);
    return null;
  }

  return item.value;
};

const findStores = async () => {
  const cachedStores = getLocalStorageWithExpiry('stores');
  if (cachedStores) {
    stores.value = cachedStores;
    loading.value = false;
    findAddresses();
    return;
  }

  try {
    const response = await axios.get('http://localhost:8080/api/v1/stores/delivery', {
      withCredentials: true
    });
    if (response.data.code === 400) {
      alert(response.data.message);
      router.push('/stores/delivery/address');
      return;
    }
    stores.value = response.data.data;
    setLocalStorageWithExpiry('stores', stores.value, 60 * 12); // 12시간 동안 캐싱
    findAddresses();
  } catch (error) {
    console.error('Error fetching items:', error);
  } finally {
    loading.value = false;
  }
};

const viewStore = (storeId, storeName, minOrderAmount) => {
  const existingOrder = JSON.parse(localStorage.getItem('deliveryOrder'));

  if (existingOrder) {
    existingOrder.storeId = storeId;
    existingOrder.storeName = storeName;
    existingOrder.orderAddressRequestDto = address.value;
  } else {
    const newOrder = {
      storeId: storeId,
      storeName: storeName,
      orderAddressRequestDto: address.value,
      minOrderAmount: minOrderAmount,
      ownerMessage: '',
      riderMessage: '',
      disposableYn: false,
      orderDetailDtos: []
    };
    localStorage.setItem('deliveryOrder', JSON.stringify(newOrder));
  }

  router.push('/stores/' + storeId + '/delivery/menus');
};

function calculateTime(duration, distance) {
  let minutes = duration / 60;
  if (distance > 10000) {
    return '배달 불가';
  } else if (minutes >= 1) {
    let duration = Math.ceil(minutes) + 15;
    return duration + '~' + (duration + 10) + '분';
  } else {
    return 15 + '~' + 25 + '분';
  }
}

const findAddresses = () => {
  axios.get('http://localhost:8080/api/v1/customers/my/addresses', {
    withCredentials: true
  }).then(response => {
    addresses.value = response.data.data;
    if (addresses.value.length === 0) {
      alert('배달 주소를 먼저 등록해주세요.');
      router.push('/stores/delivery/address');
    }
    for (let i = 0; i < addresses.value.length; i++) {
      if (addresses.value[i].defaultYn) {
        address.value = addresses.value[i];
        break;
      }
    }
  }).catch(error => {
    if (error.response.status === 401 || error.response.status === 403) {
      alert('로그인이 필요합니다.');
      router.push('/login');
    }
    console.error('Error fetching items:', error);
  });
};

findStores();

const goBack = () => {
  router.push('/');
};
</script>

<style scoped>
</style>
