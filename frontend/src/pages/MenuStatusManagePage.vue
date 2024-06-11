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
                <v-img :src="menu.image" height="200px"></v-img>
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

const menus = ref([
  { id: 1, name: 'Burger A', price: '5,000원', description: 'Delicious Burger A', image: 'https://via.placeholder.com/200', category: 'BURGERS' },
  { id: 2, name: 'Burger B', price: '6,000원', description: 'Delicious Burger B', image: 'https://via.placeholder.com/200', category: 'BURGERS' },
  { id: 3, name: 'Burger C', price: '7,000원', description: 'Delicious Burger C', image: 'https://via.placeholder.com/200', category: 'BURGERS' },
  { id: 4, name: 'Burger D', price: '8,000원', description: 'Delicious Burger D', image: 'https://via.placeholder.com/200', category: 'FRIES' },
  { id: 5, name: 'Burger E', price: '9,000원', description: 'Delicious Burger E', image: 'https://via.placeholder.com/200', category: 'FRIES' },
  { id: 6, name: 'Burger F', price: '10,000원', description: 'Delicious Burger F', image: 'https://via.placeholder.com/200', category: 'FRIES' },
  { id: 7, name: 'Burger G', price: '11,000원', description: 'Delicious Burger G', image: 'https://via.placeholder.com/200', category: 'MILKSHAKES' },
  { id: 8, name: 'Burger H', price: '12,000원', description: 'Delicious Burger H', image: 'https://via.placeholder.com/200', category: 'MILKSHAKES' },
  { id: 9, name: 'Burger I', price: '13,000원', description: 'Delicious Burger I', image: 'https://via.placeholder.com/200', category: 'MILKSHAKES' },
  { id: 10, name: 'Burger J', price: '14,000원', description: 'Delicious Burger J', image: 'https://via.placeholder.com/200', category: 'DRINKS' },
  { id: 11, name: 'Burger K', price: '15,000원', description: 'Delicious Burger K', image: 'https://via.placeholder.com/200', category: 'DRINKS' },
]);

const filteredMenus = ref([]);

const filterMenus = (category) => {
  selectedCategory.value = category;
  filteredMenus.value = menus.value.filter(menu => menu.category === category);
};

onMounted(() => {
  fetchCategories();
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
