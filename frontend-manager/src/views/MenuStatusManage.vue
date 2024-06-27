<template>
  <div>
    <Navbar2/>

    <div class="container-fluid">
      <div class="row">
        <div class="col-2">
          <div class="category-list">
            <material-button
              v-for="category in categories"
              :key="category"
              @click="filterMenus(category)"
              :class="['btn', selectedCategory === category ? 'btn-primary' : 'btn-light', 'category-button']"
            >
              {{ category }}
            </material-button>
          </div>
        </div>
        <div class="col-10">
          <div class="row">
            <div class="col-3 mb-4" v-for="menu in filteredMenus" :key="menu.id">
              <div class="card" :class="{ 'deactivated': !menu.available }"
                @click="menu.available ? confirmDeactivateMenu(menu.id) : confirmActivateMenu(menu.id)">
                <img :src="menu.pictureUrl" class="card-img-top" style="height: 200px; object-fit: cover;">
                  <h2>{{ menu.name }}</h2>
                  <h3>{{ menu.price }}</h3>
                  <p>{{ menu.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <Modal
      :isOpen="showConfirmModal"
      :title="modalTitle"
      @close="closeModal"
      @confirm="handleConfirm"
    >
      {{ modalMessage }}
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MaterialButton from '../components/MaterialButton.vue';
import Modal from '../views/Modal.vue';
import Navbar2 from "@/examples/Navbars/Navbar2.vue";

const categories = ref([]);
const selectedCategory = ref(null);
const menus = ref([]);
const filteredMenus = ref([]);
const showConfirmModal = ref(false);
const modalTitle = ref('');
const modalMessage = ref('');
let confirmAction = null;

const fetchCategories = async () => {
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/categories', {
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

const fetchMenus = async () => {
  try {
    const response = await axios.get('https://api.ourpos.org/api/v1/menus/store', {
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
    const response = await axios.post('https://api.ourpos.org/api/v1/menus/deactivate', {
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
    const response = await axios.post('https://api.ourpos.org/api/v1/menus/activate', {
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
  modalTitle.value = 'Deactivate Menu';
  modalMessage.value = '이 메뉴를 비활성화 시키시겠습니까?';
  confirmAction = () => deactivateMenu(menuId);
  showConfirmModal.value = true;
};

const confirmActivateMenu = (menuId) => {
  modalTitle.value = 'Activate Menu';
  modalMessage.value = '이 메뉴를 활성화 시키시겠습니까?';
  confirmAction = () => activateMenu(menuId);
  showConfirmModal.value = true;
};

const handleConfirm = () => {
  if (confirmAction) {
    confirmAction();
  }
  showConfirmModal.value = false;
};

const closeModal = () => {
  showConfirmModal.value = false;
};

onMounted(() => {
  fetchCategories();
  fetchMenus();
});
</script>

<style scoped>

.category-list {
  display: flex;
  flex-direction: column;
}

.category-button {
  font-size: 16px;
  padding: 10px 20px;
  margin: 10px 0;
}

.deactivated {
  background-color: gray;
  opacity: 0.6;
}

.btn {
  font-size: 1.25rem; /* Increased font size */
}

</style>
