<template>
  <div>
    <div class="navigation-bar">
      <h1>OUR POS</h1>
      <router-link to="/head-office-landing">
        <MaterialButton class="icon-MaterialButton">
          <i class="mdi mdi-export"></i>
        </MaterialButton>
      </router-link>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-3">
          <MaterialButton
            :class="selectedCategory.value === 'group' ? 'btn primary' : 'btn'"
            @click="selectCategory('group')"
          >
            메뉴 옵션 그룹
          </MaterialButton>
          <MaterialButton
            :class="selectedCategory.value === 'option' ? 'btn primary' : 'btn'"
            @click="selectCategory('option')"
          >
            메뉴 옵션
          </MaterialButton>
        </div>
        <div class="col-9">
          <div class="row">
            <div class="col-12 col-sm-4" v-for="(item, index) in selectedItems" :key="index">
              <div class="card" @click="openEditDialog(item, index)">
                <h2>{{ item.name }}</h2>
                <h3>{{ item.description }}</h3>
                <p v-if="selectedCategory.value === 'option'">단위: {{ item.unit }}</p>
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

    <MenuOptionGroupManageModal v-if="addDialogGroup" @update:modelValue="addDialogGroup = false">
      <template #header>
        <h2>메뉴 옵션 그룹 추가하기</h2>
      </template>
      <template #body>
        <div class="form-group">
          <label>메뉴 옵션 그룹 이름</label>
          <input type="text" v-model="newItem.name" class="form-control">
        </div>
        <div class="form-group">
          <label>메뉴 옵션 그룹 설명</label>
          <input type="text" v-model="newItem.description" class="form-control">
        </div>
        <div class="form-group">
          <label>카테고리 이름</label>
          <select v-model="selectedCategoryName" class="form-control">
            <option v-for="category in categories" :key="category.id">{{ category.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>
            <input type="checkbox" v-model="newItem.exclusiveYn"> 배타선택 여부
          </label>
        </div>
      </template>
      <template #footer>
        <MaterialButton @click="addMenuOptionGroup" class="btn btn-primary">저장</MaterialButton>
        <MaterialButton @click="closeAddDialogGroup" class="btn btn-secondary">취소</MaterialButton>
      </template>
    </MenuOptionGroupManageModal>

    <MenuOptionGroupManageModal v-if="addDialogOption" @update:modelValue="addDialogOption = false">
      <template #header>
        <h2>메뉴 옵션 추가하기</h2>
      </template>
      <template #body>
        <div class="form-group">
          <label>메뉴 옵션 이름</label>
          <input type="text" v-model="newItem.name" class="form-control">
        </div>
        <div class="form-group">
          <label>메뉴 옵션 그룹</label>
          <select v-model="newItem.group" class="form-control">
            <option v-for="group in menuOptionGroups" :key="group.id">{{ group.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>추가금액</label>
          <input type="number" v-model="newItem.price" class="form-control">
        </div>
      </template>
      <template #footer>
        <MaterialButton @click="addMenuOption" class="btn btn-primary">저장</MaterialButton>
        <MaterialButton @click="closeAddDialogOption" class="btn btn-secondary">취소</MaterialButton>
      </template>
    </MenuOptionGroupManageModal>

    <!-- Edit Dialog for 그룹 -->
    <MenuOptionGroupManageModal v-if="editDialogGroup" @update:modelValue="editDialogGroup = false">
      <template #header>
        <h2>메뉴 옵션 그룹 수정하기</h2>
      </template>
      <template #body>
        <div class="form-group">
          <label>메뉴 옵션 그룹 이름</label>
          <input type="text" v-model="currentItem.name" class="form-control">
        </div>
        <div class="form-group">
          <label>메뉴 옵션 그룹 설명</label>
          <input type="text" v-model="currentItem.description" class="form-control">
        </div>
        <div class="form-group">
          <label>카테고리 이름</label>
          <select v-model="currentCategoryName" class="form-control">
            <option v-for="category in categories" :key="category.id">{{ category.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>
            <input type="checkbox" v-model="currentItem.exclusiveYn"> 배타선택 여부
          </label>
        </div>
      </template>
      <template #footer>
        <MaterialButton @click="saveMenuOptionGroup" class="btn btn-primary">저장</MaterialButton>
        <MaterialButton @click="closeEditDialogGroup" class="btn btn-secondary">취소</MaterialButton>
        <MaterialButton @click="deleteMenuOptionGroup" class="btn btn-danger">삭제</MaterialButton>
      </template>
    </MenuOptionGroupManageModal>

    <!-- Edit Dialog for 옵션 -->
    <MenuOptionGroupManageModal v-if="editDialogOption" @update:modelValue="editDialogOption = false">
      <template #header>
        <h2>메뉴 옵션 수정하기</h2>
      </template>
      <template #body>
        <div class="form-group">
          <label>메뉴 옵션 이름</label>
          <input type="text" v-model="currentItem.name" class="form-control">
        </div>
        <div class="form-group">
          <label>메뉴 옵션 그룹</label>
          <select v-model="currentItem.group" class="form-control">
            <option v-for="group in menuOptionGroups" :key="group.id">{{ group.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>추가금액</label>
          <input type="number" v-model="currentItem.price" class="form-control">
        </div>
      </template>
      <template #footer>
        <MaterialButton @click="saveMenuOption" class="btn btn-primary">저장</MaterialButton>
        <MaterialButton @click="closeEditDialogOption" class="btn btn-secondary">취소</MaterialButton>
        <MaterialButton @click="deleteMenuOption" class="btn btn-danger">삭제</MaterialButton>
      </template>
    </MenuOptionGroupManageModal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MenuOptionGroupManageModal from '../views/MenuOptionGroupManageModal';


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

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/categories', {
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
      await axios.post('http://localhost:8080/api/v1/menuOptionGroups', {
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
      await axios.post('http://localhost:8080/api/v1/menuOptions', {
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
        await axios.put(`http://localhost:8080/api/v1/menuOptionGroups/${currentItem.value.id}/update`, {
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
        await axios.put(`http://localhost:8080/api/v1/menuOptions/${currentItem.value.menuOptionId}/update`, {
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
      await axios.put(`http://localhost:8080/api/v1/menuOptionGroups/${currentItem.value.id}/delete`,{},{
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
      await axios.put(`http://localhost:8080/api/v1/menuOptions/${currentItem.value.menuOptionId}/delete`,{},{
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
  padding: 10px;
  text-align: center;
  cursor: pointer;
}

.btn.primary {
  background-color: #3f51b5;
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
  background-color: #3f51b5;
  color: white;
  border: none;
  border-radius: 50%;
  width: 56px;
  height: 56px;
  cursor: pointer;
}

.card {
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 16px;
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
</style>