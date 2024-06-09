<template>
  <v-container>
    <HeaderPage title="메뉴" />
    <v-row>
      <v-col>
        <v-tabs v-model="activeTab" background-color="grey lighten-4" grow show-arrows>
          <v-tab v-for="category in categories" :key="category" @click="findMenus(category)">
            {{ category }}
          </v-tab>
        </v-tabs>
        <v-card class="my-5">
          <v-row>
            <v-col v-for="menu in menus" :key="menu.id" cols="12" md="6">
              <v-card
                @click="viewMenu(menu.id)"
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
                      <v-img :src="'http://localhost:8080/images/' + menu.pictureUrl" cover></v-img>
                    </v-avatar>
                  </v-col>
                  <v-col>
                    <v-card-title>{{ menu.name }}</v-card-title>
                    <v-card-subtitle>
                      {{ menu.price }}
                    </v-card-subtitle>
                    <v-card-text>
                      <p style="color: red">{{ menu.description }}</p>
                    </v-card-text>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

<!--  <BottomButton/>-->

</template>

<script setup>
import axios from "axios";
import {ref} from "vue";
import router from "@/router";
import {useRoute} from "vue-router";
import HeaderPage from "@/components/AppHeader.vue";

const route = useRoute();
const id = route.params.id;
const menus = ref([]);
const activeTab = ref(0); // Active tab index
const categories = ref(['BURGERS', 'FRIES', 'MILKSHAKES', 'DRINKS']);

const findMenus = async (category) => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/menus', {
      withCredentials: true,
      params: {
        category: category,
      }
    });
    menus.value = response.data.data;
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const viewMenu = (menuId) => {
  router.push('/stores/' + id + '/menus/' + menuId);
};

// Initialize with the first category
findMenus(categories.value[0]);
</script>

<style scoped>
</style>

