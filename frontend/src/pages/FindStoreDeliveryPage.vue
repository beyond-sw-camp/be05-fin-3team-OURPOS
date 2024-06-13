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
          <v-col cols="12" class="text-center">
              <v-card
                link
                @click="router.push('/stores/delivery/address')"
              >
                <v-card-text>{{ address.addressBase }}</v-card-text>
              </v-card>
          </v-col>
        </v-row>
        <v-row>
          <v-col v-for="store in stores" :key="store.storeId" cols="12" md="6">
            <v-card
              link
              class="mx-auto"
              max-width="400"
              @click="viewStore(store.storeId, store.storeName)"
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
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const stores = ref([]);
const router = useRouter();
const loading = ref(true);

// const selectedStore = ref({});
const addresses = ref([]);
const address = ref({});

const findStores = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/stores/delivery', {
      withCredentials: true
    });
    if (response.data.code !== 200) {
      console.error('Error fetching items:', response.data);
      return;
    }
    stores.value = response.data.data;
    console.log('stores:', stores.value);
    findAddresses();
  } catch (error) {
    console.error('Error fetching items:', error);
  } finally {
    loading.value = false;
  }
};

const viewStore = (storeId, storeName) => {
  const deliveryOrder = {
    storeId: storeId,
    storeName: storeName,
    orderAddressRequestDto: address.value,
    ownerMessage: '',
    riderMessage: '',
    disposableYn: false,
    orderDetailDtos: []
  };

  localStorage.setItem('deliveryOrder', JSON.stringify(deliveryOrder));
  router.push('/stores/' + storeId + '/menus/delivery');
};

const formatDistance = (distance) => {
  if (distance < 1000) {
    return `${Math.round(distance)}m`;
  } else {
    return `${(distance / 1000).toFixed(2)}km`;
  }
};

const findAddresses = () => {
  axios.get('http://localhost:8080/api/v1/customers/my/addresses', {
    withCredentials: true
  }).then(response => {
    addresses.value = response.data.data;
    for (let i = 0; i < addresses.value.length; i++) {
      if (addresses.value[i].defaultYn) {
        address.value = addresses.value[i];
        break;
      }
    }
  }).catch(error => {
    console.error('Error fetching items:', error);
  });
};

findStores();

const goBack = () => {
  router.back();
};
</script>

<style scoped>
</style>
