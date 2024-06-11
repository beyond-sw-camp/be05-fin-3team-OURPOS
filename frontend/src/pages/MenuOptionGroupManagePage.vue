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
            :color="selectedCategory === 'group' ? 'primary' : ''"
            @click="selectCategory('group')"
          >
            메뉴 옵션 그룹
          </v-btn>
          <v-btn
            block
            :color="selectedCategory === 'option' ? 'primary' : ''"
            @click="selectCategory('option')"
          >
            메뉴 옵션
          </v-btn>
        </v-col>
        <v-col cols="9">
          <v-row>
            <v-col cols="12" sm="4" v-for="(item, index) in selectedItems" :key="item.name">
              <v-card @click="openEditDialog(item, index)">
                <v-card-title>{{ item.name }}</v-card-title>
                <v-card-subtitle>{{ item.description }}</v-card-subtitle>
                <v-card-text>
                  <div v-if="selectedCategory === 'option'">단위: {{ item.unit }}</div>
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

    <!-- Add Dialog -->
    <v-dialog v-model="addDialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">추가하기</span>
        </v-card-title>
        <v-card-text>
          <v-container v-if="selectedCategory === 'group'">
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 이름" v-model="newItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 설명" v-model="newItem.description"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <v-container v-else>
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
                <v-text-field label="추가금액" v-model="newItem.price"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="단위" v-model="newItem.unit"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="제품 설명" v-model="newItem.description"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="addItem">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeAddDialog">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Edit Dialog -->
    <v-dialog v-model="editDialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">수정하기</span>
        </v-card-title>
        <v-card-text>
          <v-container v-if="selectedCategory === 'group'">
            <v-row>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 이름" v-model="currentItem.name"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="메뉴 옵션 그룹 설명" v-model="currentItem.description"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <v-container v-else>
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
                <v-text-field label="추가금액" v-model="currentItem.price"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="단위" v-model="currentItem.unit"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field label="제품 설명" v-model="currentItem.description"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="saveItem">저장</v-btn>
          <v-btn color="blue darken-1" text @click="closeEditDialog">취소</v-btn>
          <v-btn color="red darken-1" text @click="deleteItem">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const addDialog = ref(false);
const editDialog = ref(false);
const selectedCategory = ref('group');
const selectedItems = ref([]);
const currentItemIndex = ref(null);

const menuOptionGroups = ref([]);
const menuOptions = ref([]);

const currentItem = ref({
  name: '',
  group: '',
  price: '',
  unit: '',
  description: '',
});

const newItem = ref({
  name: '',
  group: '',
  price: '',
  unit: '',
  description: '',
});

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/categories');
    if (response.data.code === 200) {
      const categories = response.data.data;
      categories.forEach(category => {
        category.menuOptionGroupResponseDtos.forEach(group => {
          menuOptionGroups.value.push({
            name: group.name,
            description: group.exclusiveYn ? 'Exclusive' : 'Non-exclusive',
            categoryId: group.categoryId,
            id: group.id
          });
          group.menuOptionResponseDtos.forEach(option => {
            menuOptions.value.push({
              name: option.menuOptionName,
              group: group.name,
              price: option.price,
              unit: 'pcs', // Assuming unit is pcs for all options
              description: '',
              menuOptionGroupId: option.menuOptionGroupId,
              menuOptionId: option.menuOptionId
            });
          });
        });
      });
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
  addDialog.value = true;
};

const openEditDialog = (item, index) => {
  currentItem.value = { ...item };
  currentItemIndex.value = index;
  editDialog.value = true;
};

const closeAddDialog = () => {
  addDialog.value = false;
  resetNewItem();
};

const closeEditDialog = () => {
  editDialog.value = false;
  resetCurrentItem();
};

const resetNewItem = () => {
  newItem.value = {
    name: '',
    group: '',
    price: '',
    unit: '',
    description: '',
  };
};

const resetCurrentItem = () => {
  currentItem.value = {
    name: '',
    group: '',
    price: '',
    unit: '',
    description: '',
  };
  currentItemIndex.value = null;
};

const addItem = () => {
  if (selectedCategory.value === 'group') {
    menuOptionGroups.value.push({ ...newItem.value, unit: '', price: 0 });
  } else {
    menuOptions.value.push({ ...newItem.value });
  }
  selectCategory(selectedCategory.value);
  closeAddDialog();
};

const saveItem = () => {
  if (currentItemIndex.value !== null) {
    if (selectedCategory.value === 'group') {
      menuOptionGroups.value[currentItemIndex.value] = { ...currentItem.value };
    } else {
      menuOptions.value[currentItemIndex.value] = { ...currentItem.value };
    }
    selectCategory(selectedCategory.value);
  }
  closeEditDialog();
};

const deleteItem = () => {
  if (currentItemIndex.value !== null) {
    if (selectedCategory.value === 'group') {
      menuOptionGroups.value.splice(currentItemIndex.value, 1);
    } else {
      menuOptions.value.splice(currentItemIndex.value, 1);
    }
    selectCategory(selectedCategory.value);
  }
  closeEditDialog();
};

onMounted(() => {
  fetchCategories();
  selectCategory(selectedCategory.value);
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
