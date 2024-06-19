<template>
  <div>
    <nav class="navbar navbar-dark bg-dark navigation-bar">
      <span class="navbar-brand">OUR POS</span>
      <router-link to="/owner" class="ml-auto">
        <button class="btn btn-outline-light">
          <i class="mdi mdi-export"></i> 뒤로 가기
        </button>
      </router-link>
    </nav>
    <div class="container-fluid">
      <div class="row">
        <div class="col-2">
          <div class="category-list">
            <material-button
              v-for="category in categories"
              :key="category.id"
              @click="selectCategory(category)"
              :class="['category-material-button', { primary: selectedCategory === category, default: selectedCategory !== category }]"
            >
              {{ category.name }}
            </material-button>
            <material-button  @click="openDialog('addCategory')" class="action-material-button">카테고리 추가</material-button>
            <material-button  @click="openDialog('editCategory')" class="action-material-button">카테고리 수정</material-button>
            <material-button  @click="openDialog('deleteCategory')" class="action-material-button">카테고리 삭제</material-button>
            <material-button  @click="openDialog('addMenu')" class="action-material-button">메뉴 추가</material-button>
          </div>
        </div>
        <div class="col-10">
          <div class="row">
            <div class="col-3 mb-4" v-for="menu in filteredMenus" :key="menu.id">
              <div class="card" @click="openUpdateMenuDialog(menu)">
                <img :src="getMenuImageUrl(menu.pictureUrl)" style="height: 200px; object-fit: cover;" />
                <h2>{{ menu.name }}</h2>
                <h3>{{ menu.price }}</h3>
                <p>{{ menu.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- Add Category MenuManageModal -->
    <MenuManageModal
      v-if="dialog.addCategory"
      :isOpen="dialog.addCategory"
      title="카테고리 추가"
      @close="closeDialog"
      @confirm="addCategory"
    >
      <input v-model="newCategory" placeholder="카테고리 이름" />
    </MenuManageModal>

    <!-- Edit Category Modal -->
    <MenuManageModal
      v-if="dialog.editCategory"
      :isOpen="dialog.editCategory"
      title="카테고리 수정"
      @close="closeDialog"
      @confirm="editCategory"
    >
      <select v-model="selectedEditCategory">
        <option v-for="name in categoryNames" :key="name">{{ name }}</option>
      </select>
      <input v-model="updatedCategoryName" placeholder="변경 후 카테고리 이름" />
    </MenuManageModal>

    <!-- Delete Category Modal -->
    <MenuManageModal
      v-if="dialog.deleteCategory"
      :isOpen="dialog.deleteCategory"
      title="카테고리 삭제"
      @close="closeDialog"
      @confirm="deleteCategory"
    >
      <select v-model="selectedDeleteCategory">
        <option v-for="name in categoryNames" :key="name">{{ name }}</option>
      </select>
    </MenuManageModal>

    <!-- Add Menu Modal -->
    <MenuManageModal
      v-if="dialog.addMenu"
      :isOpen="dialog.addMenu"
      title="메뉴 추가"
      @close="closeDialog"
      @confirm="addMenu"
    >
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
          <material-button @click="triggerFileInput">Browse</material-button>
          <textarea v-model="newMenu.description" placeholder="메뉴 설명"></textarea>
        </div>
      </div>
    </MenuManageModal>

    <!-- Update Menu Modal -->
    <MenuManageModal
      v-if="dialog.updateMenu"
      :isOpen="dialog.updateMenu"
      title="메뉴 수정"
      @close="closeDialog"
      @confirm="updateMenu"
    >
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
          <material-button @click="triggerFileInputUpdate">Browse</material-button>
          <textarea v-model="selectedMenu.description" placeholder="메뉴 설명"></textarea>
        </div>
        <div class="col-6">
          <material-button @click="updateMenu">수정 반영</material-button>
          <material-button @click="deleteMenu">메뉴 삭제</material-button>
          <material-button @click="closeDialog">뒤로가기</material-button>
        </div>
      </div>
    </MenuManageModal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MenuManageModal from '../views/MenuManageModal.vue';
import MaterialButton from '../components/MaterialButton.vue';

const categories = ref([]);
const categoryNames = ref([]);

const menus = ref([]);
const selectedCategory = ref(null);

const filteredMenus = ref([]); // Make sure this is correctly initialized

// Dialogs state
const dialog = ref({
  addCategory: false,
  editCategory: false,
  deleteCategory: false,
  addMenu: false,
  updateMenu: false,
});

const selectCategory = (category) => {
  selectedCategory.value = category;
  filterMenus(category);
};

const filterMenus = (category) => {
  selectedCategory.value = category;
  filteredMenus.value = menus.value.filter(menu => menu.category === category.name);
};

// Helper method to get full URL of menu image
const getMenuImageUrl = (imagePath) => {
  return `http://localhost:8080/images/${imagePath}`;
};

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

const selectedMenuFile = ref(null); // Add this line

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
    const response = await axios.post(
      'http://localhost:8080/api/v1/categories',
      {
        name: newCategory.value,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      }
    );
    if (response.status === 200) {
      // Update categories after successfully adding
      fetchCategoriesAndMenus();
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
    },
    {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      }
    );
    if (response.status === 200) {
      // Update categories after successfully updating
      fetchCategoriesAndMenus();
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
    const response = await axios.put(`http://localhost:8080/api/v1/categories/${categoryId}/delete`,{},{
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      }
    );
    if (response.status === 200) {
      // Update categories after successfully deleting
      fetchCategoriesAndMenus();
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
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.status === 200) {
      fetchCategoriesAndMenus();
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

const updateMenu = async () => {
  const formData = new FormData();
  const menuUpdateDto = {
    categoryId: categories.value.find(category => category.name === selectedMenu.value.category)?.id,
    name: selectedMenu.value.name,
    price: selectedMenu.value.price,
    description: selectedMenu.value.description,
    pictureUrl: selectedMenu.value.pictureUrl // This will be updated by the file upload
  };
  formData.append('menuUpdateDto', new Blob([JSON.stringify(menuUpdateDto)], { type: 'application/json' }));
  formData.append('multipartFile', selectedMenuFile.value);

  try {
    const response = await axios.post(`http://localhost:8080/api/v1/menus/${selectedMenu.value.id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.status === 200) {
      fetchCategoriesAndMenus();
      closeDialog();
    } else {
      console.error('Error updating menu:', response);
    }
  } catch (error) {
    console.error('Error updating menu:', error);
  }
};

const deleteMenu = async () => {
  try {
    const response = await axios.put(`http://localhost:8080/api/v1/menus/${selectedMenu.value.id}/delete`,{},{
      headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        }
    });
    if (response.status === 200) {
      fetchCategoriesAndMenus();
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
};

const handleFileChangeUpdate = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      selectedMenu.value.pictureUrl = e.target.result;
    };
    reader.readAsDataURL(file);
    selectedMenuFile.value = file; // Use selectedMenuFile
  }
};

const fetchCategoriesAndMenus = async () => {
  try {
    // Fetch categories
    const categoriesResponse = await axios.get('http://localhost:8080/api/v1/categories', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    categories.value = categoriesResponse.data.data.map(category => ({
      id: category.id,
      name: category.name
    }));
    categoryNames.value = categories.value.map(category => category.name);

    // Fetch menus
    const menusResponse = await axios.get('http://localhost:8080/api/v1/menus/all', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    menus.value = menusResponse.data.data.map(menu => ({
      id: menu.id,
      name: menu.name,
      price: `${menu.price}원`,  // Converting price to string with '원' suffix
      description: menu.description,
      pictureUrl: menu.pictureUrl,
      category: menu.categoryName,
    }));

    // Set default selected category and filter menus
    selectedCategory.value = categories.value[0].name; 
    filterMenus(categories.value[0]);

  } catch (error) {
    console.error('Error fetching categories or menus:', error);
  }
};

onMounted(() => {
  fetchCategoriesAndMenus();
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

.category-material-button, .action-material-button {
  font-size: 16px;
  padding: 10px 20px;
  margin: 10px 0;
  width: 100%;
  height: 60px; /* Adjust the height as needed */
}

.primary {
  background-color: #3f51b5;
  color: white;
}

.default {
  background-color: white;
  color: black;
}

.MenuManageModal {
  background: white;
  padding: 20px;
  border-radius: 5px;
  position: relative;
  max-width: 500px;
  width: 100%;
  z-index: 1000;
}

.MenuManageModal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.icon-material-button {
  font-size: 16px;
  padding: 10px 20px;
  margin: 10px 0;
}
</style>
