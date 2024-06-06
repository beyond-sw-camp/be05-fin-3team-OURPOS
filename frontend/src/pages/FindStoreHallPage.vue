<template>
  <v-container>
    <v-row>
      <v-col>
        <v-row>
          <v-col v-for="store in stores" :key="store.storeId" cols="12" md="6">
            <v-card class="mx-auto" max-width="400">
<!--              <v-img :src="store.pictureUrl" aspect-ratio="16/9" class="white&#45;&#45;text" height="200px">-->
<!--              </v-img>-->
              <v-card-title>{{ store.storeName }}</v-card-title>

              <v-card-subtitle>
                {{ store.addressResponseDto.addressBase }}
              </v-card-subtitle>

              <v-card-text>
                <p style="color: red">{{ store.distance }}</p>
              </v-card-text>

              <v-card-actions>
                <v-btn color="primary" text @click="viewStore(store.storeId)">View Store</v-btn>
              </v-card-actions>
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

const stores = ref([]);
const router = useRouter();

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
    console.log("items:", stores.value);
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const viewStore = (storeId) => {
  router.push({name: 'StoreDetail', params: {storeId}});
};

navigator.geolocation.getCurrentPosition((position) => {
  console.log(position.coords.latitude, position.coords.longitude);
  findItems(position.coords.latitude, position.coords.longitude);
});
</script>

<style scoped>
</style>
