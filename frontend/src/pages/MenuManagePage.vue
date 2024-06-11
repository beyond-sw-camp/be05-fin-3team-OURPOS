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
            <v-btn @click="openDialog('addMenu')" class="action-button">메뉴 추가</v-btn>
          </div>
        </v-col>
        <v-col cols="9">
          <v-row>
            <v-col cols="4" v-for="menu in filteredMenus" :key="menu.id" class="mb-4">
              <v-card @click="openUpdateMenuDialog(menu)">
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

    <!-- Add Menu Dialog -->
    <v-dialog v-model="dialog.addMenu" max-width="800px">
      <v-card>
        <v-card-title>메뉴 추가</v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="6">
                <v-img :src="newMenu.image" height="200px"></v-img>
                <v-text-field v-model="newMenu.name" label="제품명"></v-text-field>
                <v-select v-model="newMenu.category" :items="categories" label="카테고리"></v-select>
                <v-text-field v-model="newMenu.price" label="가격"></v-text-field>
                <v-text-field v-model="newMenu.image" label="사진 URL"></v-text-field>
                <input type="file" ref="fileInput" style="display: none" @change="handleFileChange">
                <v-btn @click="triggerFileInput">Browse</v-btn>
                <v-textarea v-model="newMenu.description" label="메뉴 설명"></v-textarea>
              </v-col>
              <v-col cols="6">
                <v-btn color="blue darken-1" block @click="addMenu">메뉴 추가</v-btn>
                <v-btn color="grey" block @click="closeDialog">취소</v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- Update Menu Dialog -->
    <v-dialog v-model="dialog.updateMenu" max-width="800px">
      <v-card>
        <v-card-title>메뉴 수정</v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="6">
                <v-img :src="selectedMenu.image" height="200px"></v-img>
                <v-text-field v-model="selectedMenu.name" label="제품명"></v-text-field>
                <v-select v-model="selectedMenu.category" :items="categories" label="카테고리"></v-select>
                <v-text-field v-model="selectedMenu.price" label="가격"></v-text-field>
                <v-text-field v-model="selectedMenu.image" label="사진 URL"></v-text-field>
                <input type="file" ref="fileInputUpdate" style="display: none" @change="handleFileChangeUpdate">
                <v-btn @click="triggerFileInputUpdate">Browse</v-btn>
                <v-textarea v-model="selectedMenu.description" label="메뉴 설명"></v-textarea>
              </v-col>
              <v-col cols="6">
                <v-btn color="blue darken-1" block @click="updateMenu">수정 반영</v-btn>
                <v-btn color="red darken-1" block @click="deleteMenu">메뉴 삭제</v-btn>
                <v-btn color="grey" block @click="closeDialog">뒤로가기</v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
      </v-card>
    </v-dialog>

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
  addMenu: false,
  updateMenu: false,
});

const newCategory = ref('');
const selectedEditCategory = ref(null);
const updatedCategoryName = ref('');
const selectedDeleteCategory = ref(null);
const newMenu = ref({
  name: '',
  category: '',
  price: '',
  image: '',
  description: ''
});

const selectedMenu = ref({
  id: null,
  name: '',
  category: '',
  price: '',
  image: '',
  description: ''
});

const openDialog = (type) => {
  dialog.value[type] = true;
};

const closeDialog = () => {
  dialog.value.addCategory = false;
  dialog.value.editCategory = false;
  dialog.value.deleteCategory = false;
  dialog.value.addMenu = false;
  dialog.value.updateMenu = false;
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

const addMenu = () => {
  if (newMenu.value.name && newMenu.value.category && newMenu.value.price) {
    menus.value.push({
      id: menus.value.length + 1,
      name: newMenu.value.name,
      category: newMenu.value.category,
      price: newMenu.value.price,
      image: newMenu.value.image,
      description: newMenu.value.description
    });
    newMenu.value = {
      name: '',
      category: '',
      price: '',
      image: '',
      description: ''
    };
    filterMenus(selectedCategory.value);
    closeDialog();
  }
};

const openUpdateMenuDialog = (menu) => {
  selectedMenu.value = { ...menu };
  dialog.value.updateMenu = true;
};

const updateMenu = () => {
  const index = menus.value.findIndex(m => m.id === selectedMenu.value.id);
  if (index !== -1) {
    menus.value[index] = { ...selectedMenu.value };
    filterMenus(selectedCategory.value);
    closeDialog();
  }
};

const deleteMenu = () => {
  const index = menus.value.findIndex(m => m.id === selectedMenu.value.id);
  if (index !== -1) {
    menus.value.splice(index, 1);
    filterMenus(selectedCategory.value);
    closeDialog();
  }
};

const triggerFileInput = () => {
  document.querySelector('input[type="file"]').click();
};

const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      newMenu.value.image = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const triggerFileInputUpdate = () => {
  document.querySelector('input[type="file"]').click();
  // document.querySelector('input[ref="fileInputUpdate"]').click();
};

const handleFileChangeUpdate = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      selectedMenu.value.image = e.target.result;
    };
    reader.readAsDataURL(file);
  }
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
