<template>
  <div>
    <v-toolbar dark prominent class="navigation-bar">
      <v-toolbar-title>OUR POS</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>mdi-export</v-icon>
      </v-btn>
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

### Script

```javascript
<script setup>
import { ref } from 'vue';

const categories = ref(['Menu Category 1', 'Menu Category 2', 'Menu Category 3']);
const selectedCategory = ref(categories.value[0]);

const menus = ref([
  { id: 1, name: 'Burger A', price: '5,000원', description: 'Delicious Burger A', image: 'https://via.placeholder.com/200', category: 'Menu Category 1' },
  { id: 2, name: 'Burger B', price: '6,000원', description: 'Delicious Burger B', image: 'https://via.placeholder.com/200', category: 'Menu Category 1' },
  { id: 3, name: 'Burger C', price: '7,000원', description: 'Delicious Burger C', image: 'https://via.placeholder.com/200', category: 'Menu Category 1' },
  { id: 4, name: 'Burger D', price: '8,000원', description: 'Delicious Burger D', image: 'https://via.placeholder.com/200', category: 'Menu Category 2' },
  { id: 5, name: 'Burger E', price: '9,000원', description: 'Delicious Burger E', image: 'https://via.placeholder.com/200', category: 'Menu Category 2' },
  { id: 6, name: 'Burger F', price: '10,000원', description: 'Delicious Burger F', image: 'https://via.placeholder.com/200', category: 'Menu Category 2' },
  { id: 7, name: 'Burger G', price: '11,000원', description: 'Delicious Burger G', image: 'https://via.placeholder.com/200', category: 'Menu Category 3' },
  { id: 8, name: 'Burger H', price: '12,000원', description: 'Delicious Burger H', image: 'https://via.placeholder.com/200', category: 'Menu Category 3' },
  { id: 9, name: 'Burger I', price: '13,000원', description: 'Delicious Burger I', image: 'https://via.placeholder.com/200', category: 'Menu Category 3' }
]);

const filteredMenus = ref(menus.value.filter(menu => menu.category === selectedCategory.value));

const filterMenus = (category) => {
  selectedCategory.value = category;
  filteredMenus.value = menus.value.filter(menu => menu.category === category);
};
</script>

### Styles

```css
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
