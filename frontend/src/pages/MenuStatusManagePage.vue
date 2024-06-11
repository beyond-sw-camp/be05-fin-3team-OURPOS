<template>
  <div>
    <v-toolbar dark prominent class="navigation-bar">
      <v-toolbar-title>OUR POS</v-toolbar-title>
      <v-spacer></v-spacer>
      <router-link to="/admin/storeLanding">
        <v-btn icon>
          <v-icon>mdi-export</v-icon>
        </v-btn>
      </router-link>
    </v-toolbar>

    <v-container fluid>
      <v-row>
        <v-col cols="3">
          <div class="category-list">
            <v-btn
              v-for="category in categories"
              :key="category"
              @click="filterMenus(category)"
              :color="selectedCategory === category ? 'primary' : 'default'"
              class="category-button"
            >
              {{ category }}
            </v-btn>
          </div>
        </v-col>
        <v-col cols="9">
          <v-row>
            <v-col cols="4" v-for="menu in filteredMenus" :key="menu.id" class="mb-4">
              <v-card>
                <v-img :src="menu.pictureUrl" height="200px"></v-img>
                <v-card-title>{{ menu.name }}</v-card-title>
                <v-card-subtitle>{{ menu.price }}</v-card-subtitle>
                <v-card-text>{{ menu.description }}</v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const categories = ref([]);
const selectedCategory = ref(null);

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/categories');
    categories.value = response.data.data.map(category => category.name);
    selectedCategory.value = categories.value[0]; // Set default selected category
    filterMenus(selectedCategory.value);
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const menus = ref([]);
const filteredMenus = ref([]);

const fetchMenus = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/menus');
    menus.value = response.data.data.map(menu => ({
      id: menu.id,
      name: menu.name,
      price: `${menu.price}원`,  // Converting price to string with '원' suffix
      description: menu.description,
      pictureUrl: menu.pictureUrl,
      category: menu.categoryName,
    }));
    filterMenus(selectedCategory.value);
  } catch (error) {
    console.error('Error fetching menus:', error);
  }
};

const filterMenus = (category) => {
  selectedCategory.value = category;
  filteredMenus.value = menus.value.filter(menu => menu.category === category);
};

onMounted(() => {
  fetchCategories();
  fetchMenus();
});
</script>

<style scoped>
.navigation-bar {
  background-color: #3f51b5;
}

.category-list {
  display: flex;
  flex-direction: column;
}

.category-button {
  margin-bottom: 10px;
}

.v-btn--default {
  background-color: white;
  color: black;
}

.v-btn--primary {
  background-color: #3f51b5;
  color: white;
}
</style>
