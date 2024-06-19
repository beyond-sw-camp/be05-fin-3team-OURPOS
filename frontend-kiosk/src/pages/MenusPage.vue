<template>
  <v-container>
    <v-row>
      <v-col>
        <v-btn
          stacked
          @click="goToMain"
        >
          <v-icon>mdi-home</v-icon>
          Home
        </v-btn>
      </v-col>
      <v-col class="text-center">
        <h2>남은 시간: {{ remainingTime }}초</h2>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-tabs v-model="activeTab" background-color="grey lighten-4" grow show-arrows>
          <v-tab v-for="category in categories" :key="category.name" @click="getMenus(category.name)">
            {{ category.name }}
          </v-tab>
        </v-tabs>
        <v-row>
          <v-col v-for="menu in menus" :key="menu.id" cols="12" md="6">
            <v-card
              :disabled="menu.available === false"
              link
              class="mx-auto"
              max-width="400"
              @click="viewMenu(menu, menu.categoryName)"
            >
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-avatar
                    color="grey"
                    size="60"
                    class="my-2 ml-2"
                  >
                    <v-img :src="'http://localhost:8080/images/' + menu.pictureUrl" cover></v-img>
                  </v-avatar>
                </v-col>
                <v-col>
                  <v-card-title>{{ menu.name }}</v-card-title>
                  <v-card-subtitle>
                    {{ Number(menu.price).toLocaleString() }}원
                  </v-card-subtitle>
                  <v-card-text>
                    <p style="color: red">{{ menu.description }}</p>
                  </v-card-text>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <MenuDetailModal
      v-if="selectedMenu"
      :menu="selectedMenu"
      v-model:isVisible="isModalVisible"
      :category="selectedCategory"
      @cartUpdated="handleCartUpdate"
    />

    <v-row justify="end">
      <v-col>
        <OrderSummary @goToPayment="handleGoToPayment" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue';
import {findCategories, findMenus} from '@/services/menusApi';
import router from '@/router';
import MenuDetailModal from '@/components/MenuDetailModal.vue';
import OrderSummary from '@/components/OrderSummary.vue';

const menus = ref([]);
const activeTab = ref(0);
const categories = ref([]);
const selectedMenu = ref(null);
const selectedCategory = ref('');
const isModalVisible = ref(false);

const remainingTime = ref(0);
let timer;

const getCategories = async () => {
  try {
    let response = await findCategories();
    if (response.code !== 200) {
      alert(response.message);
      return;
    }
    categories.value = response.data;
    await getMenus(categories.value[0].name);
  } catch (e) {
    alert(e.message);
  }
}

const getMenus = async (category) => {
  try {
    let response = await findMenus(category);
    if (response.code !== 200) {
      alert(response.message);
      return;
    }
    menus.value = response.data;
  } catch (e) {
    alert(e.message);
  }
}

const viewMenu = (menu, category) => {
  selectedMenu.value = menu;
  selectedCategory.value = category;
  isModalVisible.value = true;
}

const startTimer = () => {
  clearInterval(timer);
  const orders = JSON.parse(localStorage.getItem('orders'));
  if (orders) {
    const currentTime = new Date().getTime();
    const expiryTime = orders.expiry;
    remainingTime.value = Math.floor((expiryTime - currentTime) / 1000);
    timer = setInterval(() => {
      const currentTime = new Date().getTime();
      remainingTime.value = Math.floor((expiryTime - currentTime) / 1000);
      if (remainingTime.value <= 0 || isNaN(remainingTime.value) || orders.expiry < currentTime) {
        clearInterval(timer);
        localStorage.removeItem('orders');
        router.push('/');
      }
    }, 1000);
  }
};

const checkAndRemoveExpiredOrders = () => {
  const orders = JSON.parse(localStorage.getItem('orders'));
  if (orders) {
    const currentTime = new Date().getTime();
    if (orders.expiry < currentTime) {
      localStorage.removeItem('orders');
      router.push('/');
    }
  }
};

const handleCartUpdate = () => {
  const event = new Event('cartUpdated');
  window.dispatchEvent(event);
}

const goToMain = () => {
  router.push('/');
}

const handleGoToPayment = () => {
  router.push('/hall/pay');
}

getCategories();
onMounted(() => {
  checkAndRemoveExpiredOrders();
  startTimer();
});

onBeforeUnmount(() => {
  clearInterval(timer);
});
</script>

<style scoped>
</style>
