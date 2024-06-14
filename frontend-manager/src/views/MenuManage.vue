<template>
  <div>
    <div class="navigation-bar">
      <h1>OUR POS</h1>
      <router-link to="/">
        <button class="icon-button">
          <i class="mdi mdi-export"></i>
        </button>
      </router-link>
    </div>
    <div class="container-fluid">
      <div class="row">
        <div class="col-3">
          <div class="category-list">
            <button
              v-for="category in categories"
              :key="category.id"
              @click="filterMenus(category)"
              :class="selectedCategory === category.name ? 'category-button primary' : 'category-button default'"
            >
              {{ category.name }}
            </button>
            <button @click="openDialog('addCategory')" class="action-button">카테고리 추가</button>
            <button @click="openDialog('editCategory')" class="action-button">카테고리 수정</button>
            <button @click="openDialog('deleteCategory')" class="action-button">카테고리 삭제</button>
            <button @click="openDialog('addMenu')" class="action-button">메뉴 추가</button>
          </div>
        </div>
        <div class="col-9">
          <div class="row">
            <div class="col-4 mb-4" v-for="menu in filteredMenus" :key="menu.id">
              <div class="card" @click="openUpdateMenuDialog(menu)">
                <img :src="getMenuImageUrl(menu.pictureUrl)" height="200px" />
                <h2>{{ menu.name }}</h2>
                <h3>{{ menu.price }}</h3>
                <p>{{ menu.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Category Modal -->
    <Modal v-if="dialog.addCategory" @close="closeDialog">
      <h2>추가할 카테고리</h2>
      <input v-model="newCategory" placeholder="카테고리 이름" />
      <div class="actions">
        <button @click="addCategory">확인</button>
        <button @click="closeDialog">취소</button>
      </div>
    </Modal>

    <!-- Edit Category Modal -->
    <Modal v-if="dialog.editCategory" @close="closeDialog">
      <h2>카테고리 수정</h2>
      <select v-model="selectedEditCategory">
        <option v-for="name in categoryNames" :key="name">{{ name }}</option>
      </select>
      <input v-model="updatedCategoryName" placeholder="변경 후 카테고리 이름" />
      <div class="actions">
        <button @click="editCategory">확인</button>
        <button @click="closeDialog">취소</button>
      </div>
    </Modal>

    <!-- Delete Category Modal -->
    <Modal v-if="dialog.deleteCategory" @close="closeDialog">
      <h2>카테고리 삭제</h2>
      <select v-model="selectedDeleteCategory">
        <option v-for="name in categoryNames" :key="name">{{ name }}</option>
      </select>
      <div class="actions">
        <button @click="deleteCategory">확인</button>
        <button @click="closeDialog">취소</button>
      </div>
    </Modal>

    <!-- Add Menu Modal -->
    <Modal v-if="dialog.addMenu" @close="closeDialog">
      <h2>메뉴 추가</h2>
      <div class="row">
        <div class="col-6">
          <img :src="newMenu.image" height="200px" />
          <input v-model="newMenu.name" placeholder="제품명" />
          <select v-model="newMenu.category">
            <option v-for="name in categoryNames" :key="name">{{ name }}</option>
          </select>
          <input v-model="newMenu.price" placeholder="가격" />
          <input v-model="newMenu.image" placeholder="사진 URL" />
          <input type="file" ref="fileInput" @change="handleFileChange" style="display: none" />
          <button @click="triggerFileInput">Browse</button>
          <textarea v-model="newMenu.description" placeholder="메뉴 설명"></textarea>
        </div>
        <div class="col-6">
          <button @click="addMenu">메뉴 추가</button>
          <button @click="closeDialog">취소</button>
        </div>
      </div>
    </Modal>

    <!-- Update Menu Modal -->
    <Modal v-if="dialog.updateMenu" @close="closeDialog">
      <h2>메뉴 수정</h2>
      <div class="row">
        <div class="col-6">
          <img :src="getMenuImageUrl(selectedMenu.pictureUrl)" height="200px" />
          <input v-model="selectedMenu.name" placeholder="제품명" />
          <select v-model="selectedMenu.category">
            <option v-for="name in categoryNames" :key="name">{{ name }}</option>
          </select>
          <input v-model="selectedMenu.price" placeholder="가격" />
          <input v-model="selectedMenu.pictureUrl" placeholder="사진 URL" />
          <input type="file" ref="fileInputUpdate" @change="handleFileChangeUpdate" style="display: none" />
          <button @click="triggerFileInputUpdate">Browse</button>
          <textarea v-model="selectedMenu.description" placeholder="메뉴 설명"></textarea>
        </div>
        <div class="col-6">
          <button @click="updateMenu">수정 반영</button>
          <button @click="deleteMenu">메뉴 삭제</button>
          <button @click="closeDialog">뒤로가기</button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Modal from './Modal.vue';

const categories = ref([]);
const categoryNames = ref([]);

const menus = ref([]);
const selectedCategory = ref(null);

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/categories',{
      headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        }
    });
    categories.value = response.data.data.map(category => ({
      id: category.id,
      name: category.name
    }));
    categoryNames.value = categories.value.map(category => category.name);
    selectedCategory.value = categories.value[0].name; // Set default selected category
    filterMenus(selectedCategory.value);
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const fetchMenus = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/menus/all',{
      headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        }
    });
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
  filteredMenus.value = menus.value.filter(menu => menu.category === category.name);
};

// Helper method to get full URL of menu image
const getMenuImageUrl = (imagePath) => {
  return `http://localhost:8080/images/${imagePath}`;
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
const newMenuFile = ref(null);

const selectedMenu = ref({
  id: null,
  name: '',
  category: '',
  price: '',
  pictureUrl: '',
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

const addCategory = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/categories', {
      name: newCategory.value,
      headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        }
    });
    if (response.status === 200) {
      // Update categories after successfully adding
      fetchCategories();
      newCategory.value = '';
      closeDialog();
    } else {
      console.error('Error adding category:', response);
    }
  } catch (error) {
    console.error('Error adding category:', error);
  }
};

const editCategory = async () => {
  try {
    const categoryId = categories.value.find(cat => cat.name === selectedEditCategory.value)?.id;
    if (!categoryId) {
      console.error('Category ID not found');
      return;
    }
    const response = await axios.put(`http://localhost:8080/api/v1/categories/${categoryId}/update`, {
      name: updatedCategoryName.value
    });
    if (response.status === 200) {
      // Update categories after successfully updating
      fetchCategories();
      selectedEditCategory.value = null;
      updatedCategoryName.value = '';
      closeDialog();
    } else {
      console.error('Error updating category:', response);
    }
  } catch (error) {
    console.error('Error updating category:', error);
  }
};

const deleteCategory = async () => {
  try {
    const categoryId = categories.value.find(cat => cat.name === selectedDeleteCategory.value)?.id;
    if (!categoryId) {
      console.error('Category ID not found');
      return;
    }
    const response = await axios.put(`http://localhost:8080/api/v1/categories/${categoryId}/delete`);
    if (response.status === 200) {
      // Update categories after successfully deleting
      fetchCategories();
      selectedDeleteCategory.value = null;
      closeDialog();
    } else {
      console.error('Error deleting category:', response);
    }
  } catch (error) {
    console.error('Error deleting category:', error);
  }
};

const addMenu = async () => {
  const formData = new FormData();
  const menuRequestDto = {
    categoryId: categories.value.find(category => category.name === newMenu.value.category)?.id,
    name: newMenu.value.name,
    price: newMenu.value.price,
    description: newMenu.value.description,
    pictureUrl: newMenu.value.image // This will be updated by the file upload
  };
  formData.append('menuRequestDto', new Blob([JSON.stringify(menuRequestDto)], { type: 'application/json' }));
  formData.append('multipartFile', newMenuFile.value);

  try {
    const response = await axios.post('http://localhost:8080/api/v1/menus', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    if (response.status === 200) {
      fetchMenus();
      newMenu.value = {
        name: '',
        category: '',
        price: '',
        image: '',
        description: ''
      };
      newMenuFile.value = null;
      closeDialog();
    } else {
      console.error('Error adding menu:', response);
    }
  } catch (error) {
    console.error('Error adding menu:', error);
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

const deleteMenu = async () => {
  try {
    const response = await axios.put(`http://localhost:8080/api/v1/menus/${selectedMenu.value.id}/delete`);
    if (response.status === 200) {
      fetchMenus();
      closeDialog();
    } else {
      console.error('Error deleting menu:', response);
    }
  } catch (error) {
    console.error('Error deleting menu:', error);
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
    newMenuFile.value = file;
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

.primary {
  background-color: #3f51b5;
  color: white;
}

.default {
  background-color: white;
  color: black;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
}
</style>
