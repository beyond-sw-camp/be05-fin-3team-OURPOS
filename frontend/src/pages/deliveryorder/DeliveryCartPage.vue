<template>
  <v-app>
    <v-main>
      <v-container>
        <AppDeliveryHeader title="장바구니" />
        <v-row>
          <v-col cols="12">
            <h2 v-show="orderDetailDtos.length !== 0">{{ deliveryOrder.storeName }}</h2>
            <v-card v-for="(orderDetail, index) in orderDetailDtos" :key="orderDetail.menuId" class="my-1">
              <v-card-title class="d-flex justify-space-between align-center">
                {{ orderDetail.menuName }}
                <div class="d-flex align-center">
                  <v-btn variant="plain" icon small @click="decrementQuantity(index)" :disabled="orderDetail.quantity <= 1">
                    <v-icon>mdi-minus</v-icon>
                  </v-btn>
                  <span class="mx-2">{{ orderDetail.quantity }}</span>
                  <v-btn variant="plain" icon small @click="incrementQuantity(index)">
                    <v-icon>mdi-plus</v-icon>
                  </v-btn>
                  <v-btn variant="plain" icon small @click="deleteOrderDetail(index)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </div>
              </v-card-title>
              <v-row no-gutters>
                <v-col cols="auto">
                  <v-avatar size="64" class="mx-2 my-2">
                    <v-img :src="'http://localhost:8080/images/' + orderDetail.menuPictureUrl" cover></v-img>
                  </v-avatar>
                </v-col>
                <v-col>
                  <v-card-text v-for="orderOptionGroup in orderDetail.orderOptionGroups" v-show="orderOptionGroup.orderOptions.length !== 0" :key="orderOptionGroup.optionGroupName">
                    {{ orderOptionGroup.optionGroupName }}:
                    <v-chip v-for="orderOption in orderOptionGroup.orderOptions" :key="orderOption.optionName">
                      {{ orderOption.optionName }}
                    </v-chip>
                  </v-card-text>
                </v-col>
              </v-row>
              <v-card-text class="text-right">
                <strong>금액: {{ Number(orderDetail.totalPrice).toLocaleString()  }}원</strong>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

        <v-container class="d-flex flex-column justify-end">
          <v-row v-if="orderDetailDtos.length > 0">
            <v-col cols="12" md="4" sm="6">
              <v-progress-linear :value="orderCompletionPercentage" class="mb-2" color="primary"></v-progress-linear>
              <v-btn :disabled="!canOrder" @click="goToPayment" rounded="lg" size="x-large" block class="mb-4">
                {{ Number(totalOrderPrice).toLocaleString() }}원 주문하기
              </v-btn>
              <div v-if="!canOrder" class="text-right">
                최소 주문 금액: {{ Number(minOrderPrice).toLocaleString() }}원
              </div>
            </v-col>
          </v-row>
          <v-row v-else class="text-center">
            <v-col cols="12">
              <v-icon color="primary" size="48">mdi-cart-outline</v-icon>
              <div class="mt-2">장바구니가 비었습니다!</div>
            </v-col>
          </v-row>
        </v-container>
        <v-alert v-model="orderErrorAlert" type="error" dismissible>
          {{ orderErrorMessage }}
        </v-alert>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AppDeliveryHeader from "@/components/AppDeliveryHeader.vue";

let deliveryOrder = ref(JSON.parse(localStorage.getItem('deliveryOrder')) || {});
let orderDetailDtos = ref(deliveryOrder.value.orderDetailDtos || []);
const orderErrorAlert = ref(false);
const orderErrorMessage = ref('');
const router = useRouter();
const minOrderPrice = ref(0);

// Fetch store information using storeId
minOrderPrice.value = deliveryOrder.value.minOrderAmount;

// Compute the total price of the order
const totalOrderPrice = computed(() => {
  return orderDetailDtos.value.reduce((total, orderDetail) => {
    return total + orderDetail.totalPrice;
  }, 0);
});

// Check if the order can be placed
const canOrder = computed(() => {
  return totalOrderPrice.value >= minOrderPrice.value;
});

// Calculate the percentage of the minimum order amount achieved
const orderCompletionPercentage = computed(() => {
  return Math.min((totalOrderPrice.value / minOrderPrice.value) * 100, 100);
});

const deleteOrderDetail = (index) => {
  orderDetailDtos.value.splice(index, 1);
  localStorage.setItem('deliveryOrder', JSON.stringify({ ...deliveryOrder.value, orderDetailDtos: orderDetailDtos.value }));
};

const incrementQuantity = (index) => {
  let orderDetail = orderDetailDtos.value[index];
  orderDetail.quantity += 1;
  orderDetail.totalPrice = orderDetail.menuPrice * orderDetail.quantity;
  localStorage.setItem('deliveryOrder', JSON.stringify({ ...deliveryOrder.value, orderDetailDtos: orderDetailDtos.value }));
};

const decrementQuantity = (index) => {
  if (orderDetailDtos.value[index].quantity > 1) {
    let orderDetail = orderDetailDtos.value[index];
    orderDetail.quantity -= 1;
    orderDetail.totalPrice = orderDetail.menuPrice * orderDetail.quantity;
    localStorage.setItem('deliveryOrder', JSON.stringify({ ...deliveryOrder.value, orderDetailDtos: orderDetailDtos.value }));
  }
};

const goToPayment = () => {
  router.push('/pay');
};

// Watch for changes in orderDetailDtos and update localStorage accordingly
watch(orderDetailDtos, (newOrderDetailDtos) => {
  localStorage.setItem('deliveryOrder', JSON.stringify({ ...deliveryOrder.value, orderDetailDtos: newOrderDetailDtos }));
});

console.log(orderDetailDtos.value);
</script>

<style scoped>
</style>
