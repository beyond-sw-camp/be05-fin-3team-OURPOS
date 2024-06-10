<template>
  <div>
    <v-toolbar dark prominent class="navigation-bar">
      <v-toolbar-title>OUR POS</v-toolbar-title>
      <v-spacer></v-spacer>
      <router-link to="/super-admin/headOfficeLanding">
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
            <v-btn @click="openDialog('addCategory')" class="action-button">카테고리 추가</v-btn>
            <v-btn @click="openDialog('editCategory')" class="action-button">카테고리 수정</v-btn>
            <v-btn @click="openDialog('deleteCategory')" class="action-button">카테고리 삭제</v-btn>
            <v-btn class="action-button">메뉴 추가</v-btn>
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

    <!-- Add Category Dialog -->
    <v-dialog v-model="dialog.addCategory" max-width="500px">
      <v-card>
        <v-card-title>추가할 카테고리</v-card-title>
        <v-card-text>
          <v-text-field v-model="newCategory" label="카테고리 이름"></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addCategory">확인</v-btn>
          <v-btn color="grey" text @click="closeDialog">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Edit Category Dialog -->
    <v-dialog v-model="dialog.editCategory" max-width="500px">
      <v-card>
        <v-card-title>카테고리 수정</v-card-title>
        <v-card-text>
          <v-select v-model="selectedEditCategory" :items="categories" label="수정할 카테고리"></v-select>
          <v-text-field v-model="updatedCategoryName" label="변경 후 카테고리 이름"></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="editCategory">확인</v-btn>
          <v-btn color="grey" text @click="closeDialog">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Delete Category Dialog -->
    <v-dialog v-model="dialog.deleteCategory" max-width="500px">
      <v-card>
        <v-card-title>카테고리 삭제</v-card-title>
        <v-card-text>
          <v-select v-model="selectedDeleteCategory" :items="categories" label="삭제할 카테고리"></v-select>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="deleteCategory">확인</v-btn>
          <v-btn color="grey" text @click="closeDialog">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

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

// Dialogs state
const dialog = ref({
  addCategory: false,
  editCategory: false,
  deleteCategory: false,
});

const newCategory = ref('');
const selectedEditCategory = ref(null);
const updatedCategoryName = ref('');
const selectedDeleteCategory = ref(null);

const openDialog = (type) => {
  dialog.value[type] = true;
};

const closeDialog = () => {
  dialog.value.addCategory = false;
  dialog.value.editCategory = false;
  dialog.value.deleteCategory = false;
};

const addCategory = () => {
  if (newCategory.value) {
    categories.value.push(newCategory.value);
    newCategory.value = '';
    closeDialog();
  }
};

const editCategory = () => {
  const index = categories.value.indexOf(selectedEditCategory.value);
  if (index !== -1 && updatedCategoryName.value) {
    categories.value[index] = updatedCategoryName.value;
    selectedEditCategory.value = null;
    updatedCategoryName.value = '';
    closeDialog();
  }
};

const deleteCategory = () => {
  const index = categories.value.indexOf(selectedDeleteCategory.value);
  if (index !== -1) {
    categories.value.splice(index, 1);
    selectedDeleteCategory.value = null;
    closeDialog();
  }
};
</script>

<style scoped>
.navigation-bar {
  background-color: #3f51b5;
}

.category-list {
  display: flex;
  flex-direction: column;
}

.category-button, .action-button {
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
