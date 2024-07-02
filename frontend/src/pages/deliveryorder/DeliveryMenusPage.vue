<template>
  <v-container>
    <AppDeliveryHeader title="메뉴"/>
    <v-row>
      <v-col>
        <v-tabs v-model="activeTab" background-color="grey lighten-4" grow show-arrows>
          <v-tab v-for="category in categories" :key="category.name" @click="findMenus(category.name)">
            {{ category.name }}
          </v-tab>
        </v-tabs>
        <v-card class="my-5">
          <v-row>
            <v-col v-for="menu in menus" :key="menu.id" cols="12" md="6">
              <v-card
                @click="viewMenu(menu.id)"
                :disabled="menu.available === false"
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
                      <v-img :src="'https://api.ourpos.org/images/' + menu.pictureUrl" cover></v-img>
                    </v-avatar>
                  </v-col>
                  <v-col>
                    <v-card-title>{{ menu.name }}</v-card-title>
                    <v-card-subtitle>
                      {{ Number(menu.price).toLocaleString() }}원
                    </v-card-subtitle>
                    <v-card-text>
                      <p style="color: gray">{{ menu.description }}</p>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <BottomNav />
  </v-container>

<!--  <BottomButton/>-->
<BottomNav />

</template>

<script setup>
import BottomNav from "@/components/BottomNav.vue";
import axios from "axios";
import {ref} from "vue";
import router from "@/router";
import {useRoute} from "vue-router";
import AppDeliveryHeader from "@/components/AppDeliveryHeader.vue";

const route = useRoute();
const id = route.params.id;
const menus = ref([]);
const activeTab = ref(0); // Active tab index
const categories = ref([]);

const findMenus = async (category) => {
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/menus', {
      withCredentials: true,
      params: {
        category: category,
        storeId: id
      }
    });
    menus.value = response.data.data;
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const findCategories = async () => {
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/categories', {
      withCredentials: true
    });
    categories.value = response.data.data;
    await findMenus(categories.value[0].name);
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const viewMenu = (menuId) => {
  router.push('/stores/' +  + id + '/delivery/menus/' + menuId);
};

// Initialize with the first category
findCategories();
</script>

<style scoped>
</style>

