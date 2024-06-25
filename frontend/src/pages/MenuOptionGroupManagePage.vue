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
          <v-btn
            block
            :color="selectedCategory.value === 'group' ? 'primary' : ''"
            @click="selectCategory('group')"
          >
            메뉴 옵션 그룹
          </v-btn>
          <v-btn
            block
            :color="selectedCategory.value === 'option' ? 'primary' : ''"
            @click="selectCategory('option')"
          >
            메뉴 옵션
          </v-btn>
        </v-col>
        <v-col cols="9">
          <v-row>
            <v-col cols="12" sm="4" v-for="(item, index) in selectedItems" :key="index">
              <v-card @click="openEditDialog(item, index)">
                <v-card-title>{{ item.name }}</v-card-title>
                <v-card-subtitle>{{ item.description }}</v-card-subtitle>
                <v-card-text>
                  <div v-if="selectedCategory.value === 'option'">단위: {{ item.unit }}</div>
                  <div>가격: {{ item.price }}</div>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>

    <v-btn fab bottom right @click="openAddDialog" class="add-button">
      추가하기
    </v-btn>

    <!-- Add Dialog for 그룹 -->
    <v-dialog v-model="addDialogGroup" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">메뉴 옵션 그룹 추가하기</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 이름" v-model="newItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 설명" v-model="newItem.description"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  label="카테고리 이름"
                  :items="categories.map(category => category.name)"
                  v-model="selectedCategoryName"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-checkbox label="배타선택 여부" v-model="newItem.exclusiveYn"></v-checkbox>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addMenuOptionGroup">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeAddDialogGroup">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Add Dialog for 옵션 -->
    <v-dialog v-model="addDialogOption" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">메뉴 옵션 추가하기</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 이름" v-model="newItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  label="메뉴 옵션 그룹"
                  :items="menuOptionGroups.map(group => group.name)"
                  v-model="newItem.group"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field label="추가금액" v-model="newItem.price" type="number"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addMenuOption">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeAddDialogOption">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Edit Dialog for 그룹 -->
    <v-dialog v-model="editDialogGroup" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">메뉴 옵션 그룹 수정하기</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 이름" v-model="currentItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 설명" v-model="currentItem.description"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  label="카테고리 이름"
                  :items="categories.map(category => category.name)"
                  v-model="currentCategoryName"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-checkbox label="배타선택 여부" v-model="currentItem.exclusiveYn"></v-checkbox>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="saveMenuOptionGroup">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeEditDialogGroup">취소</v-btn>
          <v-btn color="red darken-1" text @click="deleteMenuOptionGroup">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Edit Dialog for 옵션 -->
    <v-dialog v-model="editDialogOption" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">메뉴 옵션 수정하기</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 이름" v-model="currentItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  label="메뉴 옵션 그룹"
                  :items="menuOptionGroups.map(group => group.name)"
                  v-model="currentItem.group"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field label="추가금액" v-model="currentItem.price" type="number"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="saveMenuOption">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeEditDialogOption">취소</v-btn>
          <v-btn color="red darken-1" text @click="deleteMenuOption">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

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
    const response = await axios.get('https://ourpos.org/api/v1/categories');
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
      await axios.post('https://ourpos.org/api/v1/menuOptionGroups', {
        name: newItem.value.name,
        categoryId: category.id,
        exclusiveYn: newItem.value.exclusiveYn,
        description: newItem.value.description
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
      await axios.post('https://ourpos.org/api/v1/menuOptions', {
        menuOptionGroupId: group.id,
        name: newItem.value.name,
        price: newItem.value.price
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
        await axios.put(`https://ourpos.org/api/v1/menuOptionGroups/${currentItem.value.id}/update`, {
          name: currentItem.value.name,
          categoryId: category.id,
          exclusiveYn: currentItem.value.exclusiveYn,
          description: currentItem.value.description
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
        await axios.put(`https://ourpos.org/api/v1/menuOptions/${currentItem.value.menuOptionId}/update`, {
          menuOptionGroupId: group.id,
          name: currentItem.value.name,
          price: currentItem.value.price
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
      await axios.put(`https://ourpos.org/api/v1/menuOptionGroups/${currentItem.value.id}/delete`);
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
      await axios.put(`https://ourpos.org/api/v1/menuOptions/${currentItem.value.menuOptionId}/delete`);
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
}

.add-button {
  position: fixed;
  bottom: 16px;
  right: 16px;
}

.headline {
  font-size: 1.5rem;
  font-weight: 500;
}
</style>
