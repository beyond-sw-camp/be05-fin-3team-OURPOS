<template>
  <div>
    <nav class="navbar navbar-dark bg-dark navigation-bar">
      <span class="navbar-brand">OUR POS</span>
      <router-link to="/store-landing" class="ml-auto">
        <button class="btn btn-outline-light">
          <i class="mdi mdi-export"></i>
        </button>
      </router-link>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-3">
          <div class="category-list">
            <button
              v-for="category in categories"
              :key="category"
              @click="filterMenus(category)"
              :class="['btn', selectedCategory === category ? 'btn-primary' : 'btn-light', 'category-button']"
            >
              {{ category }}
            </button>
          </div>
        </div>
        <div class="col-9">
          <div class="row">
            <div class="col-4 mb-4" v-for="menu in filteredMenus" :key="menu.id">
              <div
                class="card"
                :class="{ 'deactivated': !menu.available }"
                @click="menu.available ? confirmDeactivateMenu(menu.id) : confirmActivateMenu(menu.id)"
              >
                <img :src="menu.pictureUrl" class="card-img-top" style="height: 200px; object-fit: cover;">
                <div class="card-body">
                  <h5 class="card-title">{{ menu.name }}</h5>
                  <h6 class="card-subtitle mb-2 text-muted">{{ menu.price }}</h6>
                  <p class="card-text">{{ menu.description }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const categories = ref([]);
const selectedCategory = ref(null);

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/categories', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    categories.value = response.data.data.map(category => category.name);
    selectedCategory.value = categories.value[0]; // Set default selected category
    filterMenus(selectedCategory.value);
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const menus = ref([]);
const filteredMenus = ref([]);

const fetchMenus = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/menus/store', {
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
      available: menu.available // Use available property
    }));
    filterMenus(selectedCategory.value);
  } catch (error) {
    console.error('Error fetching menus:', error);
  }
};

const filterMenus = (category) => {
  selectedCategory.value = category;
  filteredMenus.value = menus.value.filter(menu => menu.category === category);
};

const deactivateMenu = async (menuId) => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/menus/deactivate', {
      menuId: menuId
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.status === 200) {
      const menu = menus.value.find(menu => menu.id === menuId);
      if (menu) menu.available = false;
      filterMenus(selectedCategory.value);
    } else {
      console.error('Error deactivating menu:', response);
    }
  } catch (error) {
    console.error('Error deactivating menu:', error);
  }
};

const activateMenu = async (menuId) => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/menus/activate', {
      menuId: menuId
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
      }
    });
    if (response.status === 200) {
      const menu = menus.value.find(menu => menu.id === menuId);
      if (menu) menu.available = true;
      filterMenus(selectedCategory.value);
    } else {
      console.error('Error activating menu:', response);
    }
  } catch (error) {
    console.error('Error activating menu:', error);
  }
};

const confirmDeactivateMenu = (menuId) => {
  if (confirm("이 메뉴를 비활성화 시키시겠습니까?")) {
    deactivateMenu(menuId);
  }
};

const confirmActivateMenu = (menuId) => {
  if (confirm("이 메뉴를 활성화 시키시겠습니까?")) {
    activateMenu(menuId);
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

.category-button {
  margin-bottom: 10px;
}

.btn-light {
  background-color: white;
  color: black;
}

.btn-primary {
  background-color: #3f51b5;
  color: white;
}

.deactivated {
  background-color: gray;
  opacity: 0.6;
}
</style>
