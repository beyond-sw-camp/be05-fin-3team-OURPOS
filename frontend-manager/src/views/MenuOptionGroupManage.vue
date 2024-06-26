<template>
  <div>
    <div class="container mt-5">
      <h2>메뉴 옵션 관리</h2>
      <hr class="styled-line">
    <div class="container-fluid">
      <div class="row">
        <div class="col-2">
          <div class="category-list">
            <material-button
              :class="['category-material-button', { primary: selectedCategory === 'group', default: selectedCategory !== 'group' }]"
              @click="selectCategory('group')"
            >
              메뉴 옵션 그룹
            </material-button>
            <material-button
              :class="['category-material-button', { primary: selectedCategory === 'option', default: selectedCategory !== 'option' }]"
              @click="selectCategory('option')"
            >
              메뉴 옵션
            </material-button>
          </div>
        </div>
        <div class="col-10">
          <div class="row">
            <div class="col-12 col-sm-3" v-for="(item, index) in selectedItems" :key="index">
              <div class="card" @click="openEditDialog(item, index)">
                <h2>{{ item.name }}</h2>
                <h3>{{ item.description }}</h3>
                <p v-if="selectedCategory === 'option'">단위: {{ item.unit }}</p>
                <p>가격: {{ item.price }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <MaterialButton class="fab" @click="openAddDialog">
      추가하기
    </MaterialButton>

    <MenuManageModal v-if="addDialogGroup" :isOpen="addDialogGroup" title="메뉴 옵션 그룹 추가하기" @close="closeAddDialogGroup" @confirm="addMenuOptionGroup">
      <div class="form-group">
        <label>메뉴 옵션 그룹 이름</label>
        <input v-model="newItem.name" @input="validateAddGroupName" placeholder="메뉴 옵션 그룹 이름" class="form-control">
        <span v-if="AddGroupNameErrorMessage" class="error-message">{{ AddGroupNameErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>메뉴 옵션 그룹 설명</label>
        <input type="text" v-model="newItem.description" @input="validateAddGroupDescription" placeholder="메뉴 옵션 그룹 설명" class="form-control">
        <span v-if="AddGroupDescriptionErrorMessage" class="error-message">{{ AddGroupDescriptionErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>카테고리 선택</label>
        <select v-model="selectedCategoryName" class="form-control">
          <option v-for="category in categories" :key="category.id">{{ category.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>
          <input type="checkbox" v-model="newItem.exclusiveYn"> 배타선택 여부
        </label>
      </div>
    </MenuManageModal>

    <MenuManageModal v-if="addDialogOption" :isOpen="addDialogOption" title="메뉴 옵션 추가하기" @close="closeAddDialogOption" @confirm="addMenuOption">
      <div class="form-group">
        <label>메뉴 옵션 이름</label>
        <input type="text" v-model="newItem.name" @input="validateAddOptionName" placeholder="메뉴 옵션 이름" class="form-control">
        <span v-if="AddOptionNameErrorMessage" class="error-message">{{ AddOptionNameErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>메뉴 옵션 그룹</label>
        <select v-model="newItem.group" class="form-control">
          <option v-for="group in menuOptionGroups" :key="group.id">{{ group.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>추가금액</label>
        <input  v-model="newItem.price" @input="validateAddOptionPrice" placeholder="메뉴 옵션 가격" class="form-control">
        <span v-if="AddOptionPriceErrorMessage" class="error-message">{{ AddOptionPriceErrorMessage }}</span>
      </div>
    </MenuManageModal>

    <MenuManageModal v-if="editDialogGroup" :isOpen="editDialogGroup" title="메뉴 옵션 그룹 수정하기" @close="closeEditDialogGroup" @confirm="saveMenuOptionGroup">
      <div class="form-group">
        <label>메뉴 옵션 그룹 이름</label>
        <input type="text" v-model="currentItem.name" @input="validateUpdateGroupName" placeholder="메뉴 옵션 그룹 이름" class="form-control">
        <span v-if="UpdateGroupNameErrorMessage" class="error-message">{{ UpdateGroupNameErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>메뉴 옵션 그룹 설명</label>
        <input type="text" v-model="currentItem.description" @input="validateUpdateGroupDescription" placeholder="메뉴 옵션 그룹 설명" class="form-control">
        <span v-if="UpdateGroupDescriptionErrorMessage" class="error-message">{{ UpdateGroupDescriptionErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>카테고리 선택</label>
        <select v-model="currentCategoryName" class="form-control">
          <option v-for="category in categories" :key="category.id">{{ category.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>
          <input type="checkbox" v-model="currentItem.exclusiveYn"> 배타선택 여부
        </label>
      </div>
      <MaterialButton @click="deleteMenuOptionGroup" class="btn btn-danger">삭제</MaterialButton>
    </MenuManageModal>

    <MenuManageModal v-if="editDialogOption" :isOpen="editDialogOption" title="메뉴 옵션 수정하기" @close="closeEditDialogOption" @confirm="saveMenuOption">
      <div class="form-group">
        <label>메뉴 옵션 이름</label>
        <input type="text" v-model="currentItem.name" @input="validateUpdateOptionName" placeholder="메뉴 옵션 이름" class="form-control">
        <span v-if="UpdateOptionNameErrorMessage" class="error-message">{{ UpdateOptionNameErrorMessage }}</span>
      </div>
      <div class="form-group">
        <label>메뉴 옵션 그룹</label>
        <select v-model="currentItem.group" class="form-control">
          <option v-for="group in menuOptionGroups" :key="group.id">{{ group.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>추가금액</label>
        <input  v-model="currentItem.price" @input="validateUpdateOptionPrice" placeholder="메뉴 옵션 가격" class="form-control">
        <span v-if="UpdateOptionPriceErrorMessage" class="error-message">{{ UpdateOptionPriceErrorMessage }}</span>
      </div>
      <MaterialButton @click="deleteMenuOption" class="btn btn-danger">삭제</MaterialButton>
    </MenuManageModal>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MenuManageModal from '../views/MenuManageModal.vue';
import MaterialButton from '../components/MaterialButton.vue';

const addDialogGroup = ref(false);
const addDialogOption = ref(false);
const editDialogGroup = ref(false);
const editDialogOption = ref(false);
const selectedCategory = ref('group');
const selectedItems = ref([]);
const currentItemIndex = ref(null);

const menuOptionGroups = ref([]);
const menuOptions = ref([]);
const categories = ref([]);

const currentItem = ref({
  name: '',
  group: '',
  price: '',
  unit: '',
  description: '',
});

const newItem = ref({
  name: '',
  categoryId: null,
  exclusiveYn: false,
  description: '',
  group: '',
  price: 0
});

const selectedCategoryName = ref('');
const currentCategoryName = ref('');

const AddGroupNameErrorMessage = ref('');
const AddGroupDescriptionErrorMessage = ref('');
const UpdateGroupNameErrorMessage = ref('');
const UpdateGroupDescriptionErrorMessage = ref('');
const AddOptionNameErrorMessage = ref('');
const AddOptionPriceErrorMessage = ref('');
const UpdateOptionNameErrorMessage = ref('');
const UpdateOptionPriceErrorMessage = ref('');

const validateAddGroupName = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(newItem.value.name)) {
  AddGroupNameErrorMessage.value = '한글만 입력 가능합니다';
} else {
  AddGroupNameErrorMessage.value = '';
}
};

const validateAddGroupDescription = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(newItem.value.description)) {
  AddGroupDescriptionErrorMessage.value = '한글만 입력 가능합니다';
} else {
  AddGroupDescriptionErrorMessage.value = '';
}
};

const validateUpdateGroupName = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(currentItem.value.name)) {
  UpdateGroupNameErrorMessage.value = '한글만 입력 가능합니다';
} else {
  UpdateGroupNameErrorMessage.value = '';
}
};

const validateUpdateGroupDescription = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(currentItem.value.description)) {
  UpdateGroupDescriptionErrorMessage.value = '한글만 입력 가능합니다';
} else {
  UpdateGroupDescriptionErrorMessage.value = '';
}
};

const validateAddOptionName = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(newItem.value.name)) {
  AddOptionNameErrorMessage.value = '한글만 입력 가능합니다';
} else {
  AddOptionNameErrorMessage.value = '';
}
};

const validateAddOptionPrice = () => {
const regex = /^[0-9]*$/; // This regex allows only numbers
if (!regex.test(newItem.value.price)) {
  AddOptionPriceErrorMessage.value = '숫자만 입력 가능합니다';
} else {
  AddOptionPriceErrorMessage.value = '';
}
};

const validateUpdateOptionName = () => {
const regex = /^[ㄱ-ㅎ|가-힣]+$/; // Regex to allow only Korean characters
if (!regex.test(currentItem.value.name)) {
  UpdateOptionNameErrorMessage.value = '한글만 입력 가능합니다';
} else {
  UpdateOptionNameErrorMessage.value = '';
}
};

const validateUpdateOptionPrice = () => {
  const regex = /^[0-9]*$/; // This regex allows only numbers
if (!regex.test(currentItem.value.price)) {
  UpdateOptionPriceErrorMessage.value = '숫자만 입력 가능합니다';
} else {
  UpdateOptionPriceErrorMessage.value = '';
}
};


const fetchCategories = async () => {
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/categories', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.data.code === 200) {
      const fetchedCategories = response.data.data;
      categories.value = fetchedCategories;
      menuOptionGroups.value = [];
      menuOptions.value = [];
      fetchedCategories.forEach(category => {
        category.menuOptionGroupResponseDtos.forEach(group => {
          menuOptionGroups.value.push({
            name: group.name,
            exclusiveYn: group.exclusiveYn,
            description: group.description,
            categoryId: group.categoryId,
            id: group.id
          });
          group.menuOptionResponseDtos.forEach(option => {
            menuOptions.value.push({
              name: option.menuOptionName,
              group: group.name,
              price: option.price,
              menuOptionGroupId: option.menuOptionGroupId,
              menuOptionId: option.menuOptionId
            });
          });
        });
      });
      selectCategory('group');
    }
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const selectCategory = (category) => {
  selectedCategory.value = category;
  selectedItems.value = category === 'group' ? menuOptionGroups.value : menuOptions.value;
};

const openAddDialog = () => {
  resetNewItem();
  if (selectedCategory.value === 'group') {
    addDialogGroup.value = true;
  } else {
    addDialogOption.value = true;
  }
};

const openEditDialog = (item, index) => {
  currentItem.value = { ...item };
  currentItemIndex.value = index;
  if (selectedCategory.value === 'group') {
    currentCategoryName.value = categories.value.find(category => category.id === item.categoryId).name;
    editDialogGroup.value = true;
  } else {
    editDialogOption.value = true;
  }
};

const closeAddDialogGroup = () => {
  addDialogGroup.value = false;
  resetNewItem();
};

const closeAddDialogOption = () => {
  addDialogOption.value = false;
  resetNewItem();
};

const closeEditDialogGroup = () => {
  editDialogGroup.value = false;
  resetCurrentItem();
};

const closeEditDialogOption = () => {
  editDialogOption.value = false;
  resetCurrentItem();
};

const resetNewItem = () => {
  newItem.value = {
    name: '',
    categoryId: null,
    exclusiveYn: false,
    description: '',
    group: '',
    price: 0
  };
  selectedCategoryName.value = '';
};

const resetCurrentItem = () => {
  currentItem.value = {
    name: '',
    group: '',
    price: '',
    unit: '',
    description: '',
  };
  currentCategoryName.value = '';
  currentItemIndex.value = null;
};

const addMenuOptionGroup = async () => {
  try {
    const category = categories.value.find(cat => cat.name === selectedCategoryName.value);
    if (category) {
      await axios.post('https://api.ourpos.org/api/v1/menuOptionGroups', {
        name: newItem.value.name,
        categoryId: category.id,
        exclusiveYn: newItem.value.exclusiveYn,
        description: newItem.value.description
      },{
        headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
      });
      fetchCategories();
      closeAddDialogGroup();
    }
  } catch (error) {
    console.error('Error adding menu option group:', error);
  }
};

const addMenuOption = async () => {
  try {
    const group = menuOptionGroups.value.find(g => g.name === newItem.value.group);
    if (group) {
      await axios.post('https://api.ourpos.org/api/v1/menuOptions', {
        menuOptionGroupId: group.id,
        name: newItem.value.name,
        price: newItem.value.price
      },{
        headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
      });
      await fetchCategories();
      selectCategory('option');
      closeAddDialogOption();
    }
  } catch (error) {
    console.error('Error adding menu option:', error);
  }
};

const saveMenuOptionGroup = async () => {
  if (currentItemIndex.value !== null) {
    try {
      const category = categories.value.find(cat => cat.name === currentCategoryName.value);
      if (category) {
        await axios.put(`https://api.ourpos.org/api/v1/menuOptionGroups/${currentItem.value.id}/update`, {
          name: currentItem.value.name,
          categoryId: category.id,
          exclusiveYn: currentItem.value.exclusiveYn,
          description: currentItem.value.description
        },{
          headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
        });
        menuOptionGroups.value[currentItemIndex.value] = { ...currentItem.value };
        closeEditDialogGroup();
      }
    } catch (error) {
      console.error('Error updating menu option group:', error);
    }
  }
};

const saveMenuOption = async () => {
  if (currentItemIndex.value !== null) {
    try {
      const group = menuOptionGroups.value.find(g => g.name === currentItem.value.group);
      if (group) {
        await axios.put(`https://api.ourpos.org/api/v1/menuOptions/${currentItem.value.menuOptionId}/update`, {
          menuOptionGroupId: group.id,
          name: currentItem.value.name,
          price: currentItem.value.price
        },{
          headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
        });
        menuOptions.value[currentItemIndex.value] = { ...currentItem.value };
        closeEditDialogOption();
      }
    } catch (error) {
      console.error('Error updating menu option:', error);
    }
  }
};

const deleteMenuOptionGroup = async () => {
  if (currentItemIndex.value !== null) {
    try {
      await axios.put(`https://api.ourpos.org/api/v1/menuOptionGroups/${currentItem.value.id}/delete`,{},{
        headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
      });
      menuOptionGroups.value.splice(currentItemIndex.value, 1);
      closeEditDialogGroup();
    } catch (error) {
      console.error('Error deleting menu option group:', error);
    }
    selectCategory('group');
  }
};

const deleteMenuOption = async () => {
  if (currentItemIndex.value !== null) {
    try {
      await axios.put(`https://api.ourpos.org/api/v1/menuOptions/${currentItem.value.menuOptionId}/delete`,{},{
        headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
      });
      menuOptions.value.splice(currentItemIndex.value, 1);
      closeEditDialogOption();
    } catch (error) {
      console.error('Error deleting menu option:', error);
    }
    selectCategory('option');
  }
};

onMounted(() => {
  fetchCategories();
});
</script>

<style scoped>
.navigation-bar {
  background-color: #3f51b5;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.container-fluid {
  padding: 16px;
}

.btn {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 20px; /* Increased padding for height */
  font-size: 1.25rem; /* Increased font size */
  text-align: center;
  cursor: pointer;
}

.primary {
  background-color: #28282B; /* Selected state color */
  color: white;
}

.btn.secondary {
  background-color: grey;
  color: white;
}

.fab {
  position: fixed;
  bottom: 16px;
  right: 16px;
  background-color: #28282B;
  color: white;
  border: none;
  border-radius: 50%;
  width: 70px; /* Increased width */
  height: 70px; /* Increased height */
  font-size: 1.5rem; /* Increased font size */
  cursor: pointer;
}

.card {
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 16px;
}

.card h2 {
  font-size: 1.75rem; /* Increased font size */
}

.card h3 {
  font-size: 1.5rem; /* Increased font size */
}

.card p {
  font-size: 1.25rem; /* Increased font size */
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-control {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.headline {
  font-size: 1.5rem;
  font-weight: 500;
}

.icon-MaterialButton {
  background: none;
  border: none;
  cursor: pointer;
}

.category-material-button, .action-material-button {
  font-size: 1.25rem; /* Increased font size */
  padding: 10px 20px;
  margin: 10px 0;
  width: 100%;
}

.primary {
  background-color: #28282B;
  color: white;
}

.default {
  background-color: white;
  color: black;
}

.error-message {
  color: red;
  font-size: 0.875em;
  margin-top: 5px; /* Add some margin to space it from the input field */
}
</style>
