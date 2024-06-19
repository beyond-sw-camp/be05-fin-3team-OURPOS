<template>
  <div>
    <nav class="navbar navbar-dark bg-dark navigation-bar">
      <div class="mr-auto">
        <div class="POS-name">OUR POS</div>
      </div>
      <router-link to="/owner" class="ml-auto">
        <button class="btn btn-outline-light">
          <i class="mdi mdi-export">뒤로 가기</i> 
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
            <material-button @click="openDialog('addCategory')" class="action-material-button">카테고리 추가</material-button>
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

    <!-- Add Category Modal -->
    <MenuManageModal
          v-if="dialog.addCategory"
          :isOpen="dialog.addCategory"
          title="카테고리 추가"
          @close="closeDialog"
          @confirm="addCategory"
        >
          <div class="form-group">
            <input v-model="newCategoryName" @input="validateAddCategoryName" placeholder="카테고리 이름을 입력하세요" class="form-control" />
            <span v-if="AddCategoryNameErrorMessage" class="error-message">{{ AddCategoryNameErrorMessage }}</span>
          </div>
    </MenuManageModal>

    <!-- Edit Category Modal -->
    <MenuManageModal
        v-if="dialog.editCategory"
        :isOpen="dialog.editCategory"
        title="카테고리 수정"
        @close="closeDialog"
        @confirm="editCategory"
      >
        <div class="form-group">
          <label for="selectEditCategory">카테고리 선택</label>
          <select id="selectEditCategory" v-model="selectedEditCategory" class="form-control border-visible">
            <option v-for="name in categoryNames" :key="name">{{ name }}</option>
          </select>
          <input v-model="updatedCategoryName" @input="validateUpdateCategoryName" placeholder="변경 후 카테고리 이름" class="form-control" />
          <span v-if="UpdateCategoryNameErrorMessage" class="error-message">{{ UpdateCategoryNameErrorMessage }}</span>
        </div>
    </MenuManageModal>

    <!-- Delete Category Modal -->
    <MenuManageModal
      v-if="dialog.deleteCategory"
      :isOpen="dialog.deleteCategory"
      title="카테고리 삭제"
      @close="closeDialog"
      @confirm="deleteCategory"> 
      <div class="form-group">
        <label for="deleteCategoryName">카테고리 선택</label>
        <select id="deleteCategoryName" v-model="selectedDeleteCategory">
          <option v-for="name in categoryNames" :key="name">{{ name }}</option>
        </select>
      </div>
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
        
        <div class="form-group">
          <label for="menuName">제품명</label>
          <input id="menuName" v-model="newMenu.name" @input="validateAddMenuName" placeholder="제품명" />
          <!-- Ensure error message is displayed only if it exists -->
          <span v-if="AddMenuNameErrorMessage" class="error-message">{{ AddMenuNameErrorMessage }}</span>
        </div>
        <div class="form-group">
          <label for="AddMenuCategory">카테고리 선택</label>
          <select id="AddMenuCategory" v-model="newMenu.category">
            <option v-for="name in categoryNames" :key="name">{{ name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label for="AddMenuPrice">가격 입력</label>
          <input id="AddMenuPrice" v-model="newMenu.price" @input="validateAddMenuPrice" placeholder="가격" />
          <span v-if="AddMenuPriceErrorMessage" class="error-message">{{ AddMenuPriceErrorMessage }}</span>
        </div>
        <div class="form-group">
          <label for="AddMenuImage">사진 URL</label>
          <input id="AddMenuImage" v-model="newMenu.image" placeholder="사진 URL" />
        </div>
        <div class="form-group">
          <input id="fileInput" type="file" ref="fileInput" @change="handleFileChange" style="display: none" />
          <material-button @click="triggerFileInput">Browse</material-button>
        </div>
        <div class="form-group">
          <label for="AddMenuDescription">설명 입력</label>
          <textarea id="AddMenuDescription" v-model="newMenu.description" @input="validateAddMenuDescription" placeholder="메뉴 설명"></textarea>
          <span v-if="AddMenuDescriptionErrorMessage" class="error-message">{{ AddMenuDescriptionErrorMessage }}</span>
        </div>
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
      <img :src="newMenu.image" height="200px" />

      <div class="form-group">
        <label for="updateMenuName">제품명 입력</label>
        <input id="updateMenuName" v-model="selectedMenu.name" @input="validateUpdateMenuName" placeholder="제품명"/>
        <span v-if="UpdateMenuNameErrorMessage" class="error-message">{{ UpdateMenuNameErrorMessage }}</span>
      </div>
      
      <select v-model="selectedMenu.category">
        <option v-for="name in categoryNames" :key="name">{{ name }}</option>
      </select>
      <div class="form-group">
        <label for="updateMenuPrice">가격 입력</label>
        <input id="updateMenuPrice" v-model="selectedMenu.price" @input="validateUpdateMenuPrice" placeholder="가격"/>
        <span v-if="UpdateMenuPriceErrorMessage" class="error-message">{{ UpdateMenuPriceErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label for="updateMenuDescription">설명 입력</label>
        <textarea id="updateMenuDescription" v-model="selectedMenu.description" @input="validateUpdateMenuDescription" placeholder="메뉴 설명"></textarea>
        <span v-if="UpdateMenuDescriptionErrorMessage" class="error-message">{{ UpdateMenuDescriptionErrorMessage }}</span>
      </div>
        
    </div>
    <div class="col-6">
      <material-button @click="deleteMenu">메뉴 삭제</material-button>
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
// import MaterialInput from '../components/MaterialInput.vue';

// Dialogs state
const dialog = ref({
  addCategory: false,
  editCategory: false,
  deleteCategory: false,
  addMenu: false,
  updateMenu: false,
});

const categories = ref([]);
const categoryNames = ref([]);

const menus = ref([]);
const selectedCategory = ref(null);

const filteredMenus = ref([]); // Make sure this is correctly initialized

const errorMessage = ref('');

const AddCategoryNameErrorMessage = ref('');
const UpdateCategoryNameErrorMessage = ref('');
const AddMenuNameErrorMessage = ref('');
const AddMenuPriceErrorMessage = ref('');
const AddMenuDescriptionErrorMessage = ref('');
const UpdateMenuNameErrorMessage = ref('');
const UpdateMenuPriceErrorMessage = ref('');
const UpdateMenuDescriptionErrorMessage = ref('');

const newCategoryName = ref('');

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
  description: ''
});

const validateAddCategoryName = () => {
  const regex = /^[a-zA-Z]*$/;
  if (!regex.test(newCategoryName.value)) {
    AddCategoryNameErrorMessage.value = '영어만 입력 가능합니다';
  } else {
    AddCategoryNameErrorMessage.value = '';
  }
};

const validateUpdateCategoryName = () => {
  const regex = /^[a-zA-Z]*$/;
  if (!regex.test(updatedCategoryName.value)) {
    UpdateCategoryNameErrorMessage.value = '영어만 입력 가능합니다';
  } else {
    UpdateCategoryNameErrorMessage.value = '';
  }
};

const validateAddMenuName = () => {

  const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
  if (!regex.test(newMenu.value.name)) {
    AddMenuNameErrorMessage.value = '한글만 입력 가능합니다';
  } else {
    AddMenuNameErrorMessage.value = '';
  }
};

const validateAddMenuPrice = () => {
  const regex = /^[0-9]*$/; // This regex allows only numbers
  if (!regex.test(newMenu.value.price)) {
    AddMenuPriceErrorMessage.value = '숫자만 입력 가능합니다';
  } else {
    AddMenuPriceErrorMessage.value = '';
  }
};

const validateAddMenuDescription = () => {
  const regex = /^[가-힣\s]*$/; // This regex allows only Korean characters and spaces
  if (!regex.test(newMenu.value.description)) {
    AddMenuDescriptionErrorMessage.value = '한글만 입력 가능합니다';
  } else {
    AddMenuDescriptionErrorMessage.value = '';
  }
};

const validateUpdateMenuName = () => {
  const regex = /^[ㄱ-ㅎ|가-힣]+$/; // This regex allows only Korean characters and spaces
  if (!regex.test(selectedMenu.value.name)) {
    UpdateMenuNameErrorMessage.value = '한글만 입력 가능합니다';
  } else {
    UpdateMenuNameErrorMessage.value = '';
  }
};

const validateUpdateMenuDescription = () => {
  const regex = /^[가-힣\s]*$/; // This regex allows only Korean characters and spaces
  if (!regex.test(selectedMenu.value.description)) {
    UpdateMenuDescriptionErrorMessage.value = '한글만 입력 가능합니다';
  } else {
    UpdateMenuDescriptionErrorMessage.value = '';
  }
};

const validateUpdateMenuPrice = () => {
  const regex = /^[0-9]*$/; // This regex allows only numbers
  if (!regex.test(selectedMenu.value.price)) {
    UpdateMenuPriceErrorMessage.value = '숫자만 입력 가능합니다';
  } else {
    UpdateMenuPriceErrorMessage.value = '';
  }
};




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
  if (errorMessage.value)  return;
  try {
    const response = await axios.post(
      'http://localhost:8080/api/v1/categories',
      {
        name: newCategoryName.value,
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
      await fetchCategoriesAndMenus();
      newCategory.value = '';
      closeDialog();
    } else {
      console.error('Error adding category:', response);
    }
  } catch (error) {
    console.error('Error adding category:', error);
  }
    closeDialog();
};


const editCategory = async () => {
  if (errorMessage.value) return;
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
  const menuUpdateDto = {
    categoryId: categories.value.find(category => category.name === selectedMenu.value.category)?.id,
    name: selectedMenu.value.name,
    price: parseInt(selectedMenu.value.price),
    description: selectedMenu.value.description
  };

  try {
    const response = await axios.put(`http://localhost:8080/api/v1/menus/${selectedMenu.value.id}/update`, menuUpdateDto, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.status === 200) {
      await fetchCategoriesAndMenus();
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
.form-control {
  margin-bottom: 10px;
  height: 40px;
  width: 100%;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.border-visible {
  border: 2px solid #000; /* Making the border more visible */
}



.navigation-bar {
  background-color: #3f51b5;
}

.category-list {
  display: flex;
  flex-direction: column;
}

.category-material-button, .action-material-button {
  font-size: 1.25rem; /* Increased font size */
  padding: 10px 20px;
  margin: 10px 0;
  width: 100%;
  
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

.POS-name {
  font-size: 3rem; /* Increased font size */
  color: aliceblue;
}

.mdi {
  font-size: 1.25rem; /* Increased font size */
}


.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.error-message {
  color: red;
  font-size: 0.875em;
  margin-top: 5px; /* Add some margin to space it from the input field */
}
</style>
