<template>
  <v-container class="order-summary" fluid>
    <v-row>
      <v-col cols="12">
        <h2>장바구니</h2>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-row v-if="orders.length === 0">
      <v-col cols="12" class="text-center">
        <p>장바구니에 담긴 메뉴가 없습니다.</p>
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="12">
        <div class="order-list">
          <v-card v-for="(order, index) in orders" :key="index" class="mb-3">
            <v-card-title>{{ order.menuName }}</v-card-title>
            <v-card-subtitle>
              수량:
              <v-btn variant="plain" small @click="updateQuantity(order, order.quantity - 1)">
                <v-icon>mdi-minus</v-icon>
              </v-btn>
              {{ order.quantity }}
              <v-btn variant="plain" small @click="updateQuantity(order, order.quantity + 1)">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
              <p>가격: {{ Number(order.totalPrice).toLocaleString() }}원</p>
            </v-card-subtitle>
            <v-card-text>
              <v-avatar
                color="grey"
                size="60"
                class="my-2"
              >
                <v-img :src="'https://api.ourpos.org/images/' + order.menuPictureUrl" cover></v-img>
              </v-avatar>
              <div v-for="optionGroup in order.orderOptionGroups" :key="optionGroup.optionGroupName">
                <h3>{{ optionGroup.optionGroupName }}</h3>
                <ul>
                  <li v-for="option in optionGroup.orderOptions" :key="option.optionName">
                    {{ option.optionName }} (+{{ option.price }}원)
                  </li>
                </ul>
              </div>
              <v-btn color="red" small @click="removeOrder(index)">삭제</v-btn>
            </v-card-text>
          </v-card>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" class="text-right">
        <v-btn @click="emitGoToPayment" rounded="lg" size="x-large"
               block
               class="mb-4"
               color="primary"
               :disabled="totalOrderPrice <= 0"
        >
          {{ Number(totalOrderPrice).toLocaleString() }}원 주문하기
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, watchEffect, onMounted } from 'vue';

const orders = ref([]);
const totalOrderPrice = ref(0);

const calculateOrderTotalPrice = (order) => {
  const optionPrice = order.orderOptionGroups.reduce((sum, group) => {
    return sum + group.orderOptions.reduce((groupSum, option) => groupSum + option.price, 0);
  }, 0);
  return (order.menuPrice + optionPrice) * order.quantity;
};

const loadOrders = () => {
  const storedOrders = JSON.parse(localStorage.getItem('orders'));
  if (storedOrders && storedOrders.orderDetailDtos) {
    orders.value = storedOrders.orderDetailDtos.map(order => {
      order.totalPrice = calculateOrderTotalPrice(order);
      return order;
    });
    // calculateTotalOrderPrice();
  }
};

const calculateTotalOrderPrice = () => {
  totalOrderPrice.value = orders.value.reduce((sum, order) => sum + order.totalPrice, 0);
};

const removeOrder = (index) => {
  orders.value.splice(index, 1);
  updateLocalStorage();
  calculateTotalOrderPrice();
};

const updateQuantity = (order, newQuantity) => {
  if (newQuantity > 0) {
    order.quantity = newQuantity;
    order.totalPrice = calculateOrderTotalPrice(order);
    updateLocalStorage();
    calculateTotalOrderPrice();
  }
};

const updateLocalStorage = () => {
  const storedOrders = JSON.parse(localStorage.getItem('orders'));
  storedOrders.orderDetailDtos = orders.value;
  localStorage.setItem('orders', JSON.stringify(storedOrders));
};

const emit = defineEmits(['goToPayment']);

const emitGoToPayment = () => {
  if (totalOrderPrice.value > 0) {
    emit('goToPayment', orders.value);
  }
};

onMounted(() => {
  window.addEventListener('cartUpdated', loadOrders);
  loadOrders();
});

watchEffect(() => {
  calculateTotalOrderPrice();
});
</script>

<style scoped>
.order-summary {
  position: fixed;
  bottom: 16px;
  right: 16px;
  width: 300px;
  border: 1px solid #ccc;
  background: white;
  padding: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-height: 80vh; /* Set a maximum height */
  overflow-y: auto; /* Enable vertical scrolling */
}

.order-list {
  max-height: 60vh; /* Set a maximum height for the list */
  overflow-y: auto; /* Enable vertical scrolling */
}
</style>
