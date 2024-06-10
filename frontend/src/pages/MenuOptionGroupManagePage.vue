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
              <v-card @click="openDialog(item, index)">
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

    <v-btn fab bottom right @click="openDialog" class="add-button">
      <v-icon>mdi-plus</v-icon>
    </v-btn>

    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">추가하기</span>
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
          <v-btn color="blue darken-1" text @click="closeDialog">취소</v-btn>
          <v-btn v-if="isEditing" color="red darken-1" text @click="deleteItem">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const dialog = ref(false);
const isEditing = ref(false);
const selectedCategory = ref('group');
const selectedItems = ref([]);
const currentItemIndex = ref(null);

const menuOptionGroups = ref([
  { name: 'Hamburger Size', description: 'Size of the hamburger', unit: '', price: 0 },
  { name: 'Topping', description: 'Extra toppings for the hamburger', unit: '', price: 0 },
]);

const menuOptions = ref([
  { name: 'Little', group: 'Hamburger Size', description: 'Small size', unit: 'pcs', price: 0 },
  { name: 'Normal', group: 'Hamburger Size', description: 'Regular size', unit: 'pcs', price: 0 },
  { name: 'All the way', group: 'Topping', description: 'All toppings', unit: 'pcs', price: 1 },
  { name: 'Tomato', group: 'Topping', description: 'Tomato topping', unit: 'pcs', price: 0.5 },
  { name: 'Lettuce', group: 'Topping', description: 'Lettuce topping', unit: 'pcs', price: 0.5 },
  { name: 'Cheese', group: 'Topping', description: 'Cheese topping', unit: 'pcs', price: 1 },
]);

const currentItem = ref({
  name: '',
  group: '',
  price: '',
  unit: '',
  description: '',
});

const selectCategory = (category) => {
  selectedCategory.value = category;
  selectedItems.value = category === 'group' ? menuOptionGroups.value : menuOptions.value;
};

const openDialog = (item = null, index = null) => {
  if (item) {
    currentItem.value = { ...item };
    currentItemIndex.value = index;
    isEditing.value = true;
  } else {
    resetNewItem();
    isEditing.value = false;
  }
  dialog.value = true;
};

const closeDialog = () => {
  dialog.value = false;
  resetNewItem();
};

const resetNewItem = () => {
  currentItem.value = {
    name: '',
    group: '',
    price: '',
    unit: '',
    description: '',
  };
  currentItemIndex.value = null;
};

const saveItem = () => {
  if (isEditing.value) {
    if (selectedCategory.value === 'group') {
      menuOptionGroups.value[currentItemIndex.value] = { ...currentItem.value, unit: '', price: 0 };
    } else {
      menuOptions.value[currentItemIndex.value] = { ...currentItem.value };
    }
  } else {
    if (selectedCategory.value === 'group') {
      menuOptionGroups.value.push({ ...currentItem.value, unit: '', price: 0 });
    } else {
      menuOptions.value.push({ ...currentItem.value });
    }
  }
  selectCategory(selectedCategory.value);
  closeDialog();
};

const deleteItem = () => {
  if (selectedCategory.value === 'group') {
    menuOptionGroups.value.splice(currentItemIndex.value, 1);
  } else {
    menuOptions.value.splice(currentItemIndex.value, 1);
  }
  selectCategory(selectedCategory.value);
  closeDialog();
};

selectCategory('group');
</script>

<style scoped>
.navigation-bar {
  background-color: #6200ea;
}

.add-button {
  position: fixed;
  bottom: 16px;
  right: 16px;
}
</style>
