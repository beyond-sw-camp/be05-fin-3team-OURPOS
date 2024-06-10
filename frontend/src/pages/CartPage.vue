<template>
  <v-app>
    <v-main>
      <v-container>
        <AppHeader title="장바구니" />
        <v-row>
          <v-col cols="12">
            <h2 v-show="orderDetailDtos.length !== 0">{{ fullOrder.storeName }}</h2>
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
            </v-card>
          </v-col>
        </v-row>

        <v-container class="d-flex flex-column justify-end">
          <v-row v-if="orderDetailDtos.length > 0">
            <v-col cols="12" md="4" sm="6">
              <v-btn @click="submitOrder" rounded="lg" size="x-large" block class="mb-4">주문하기</v-btn>
            </v-col>
          </v-row>
          <v-row v-else class="text-center">
            <v-col cols="12">
              <v-icon color="primary" size="48">mdi-cart-outline</v-icon>
              <div class="mt-2">장바구니가 비었습니다!</div>
            </v-col>
          </v-row>
        </v-container>

        <!-- Success message bottom sheet -->
        <v-bottom-sheet v-model="orderSuccessSheet" inset>
          <v-card class="text-center py-4">
            <v-card-text>
              <div>
                <v-icon color="green" size="36">mdi-check-circle</v-icon>
                <div>주문이 완료되었습니다!</div>
              </div>
              <v-btn variant="text" @click="orderSuccessSheet = false">
                닫기
              </v-btn>
            </v-card-text>
          </v-card>
        </v-bottom-sheet>

        <!-- Error alert -->
        <v-alert v-model="orderErrorAlert" type="error" dismissible>
          {{ orderErrorMessage }}
        </v-alert>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import AppHeader from "@/components/AppHeader.vue";

let fullOrder = ref(JSON.parse(localStorage.getItem('fullOrder')) || {});
let orderDetailDtos = ref(fullOrder.value.orderDetailDtos || []);
const orderSuccessSheet = ref(false);
const orderErrorAlert = ref(false);
const orderErrorMessage = ref('');
const router = useRouter();

const deleteOrderDetail = (index) => {
  orderDetailDtos.value.splice(index, 1);
  localStorage.setItem('fullOrder', JSON.stringify({ ...fullOrder.value, orderDetailDtos: orderDetailDtos.value }));
};

const incrementQuantity = (index) => {
  orderDetailDtos.value[index].quantity += 1;
  localStorage.setItem('fullOrder', JSON.stringify({ ...fullOrder.value, orderDetailDtos: orderDetailDtos.value }));
};

const decrementQuantity = (index) => {
  if (orderDetailDtos.value[index].quantity > 1) {
    orderDetailDtos.value[index].quantity -= 1;
    localStorage.setItem('fullOrder', JSON.stringify({ ...fullOrder.value, orderDetailDtos: orderDetailDtos.value }));
  }
};

const submitOrder = async () => {
  try {
    const response = await axios.post(
      'http://localhost:8080/api/v1/orders/hall',
      {
        storeId: fullOrder.value.storeId,
        orderTakeoutYn: true,
        orderDetailDtos: orderDetailDtos.value
      },
      {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      }
    );
    console.log('Order response:', response.data);

    if (response.data.code === 200) {
      // Clear the cart after successful order
      orderDetailDtos.value = [];
      localStorage.setItem('fullOrder', JSON.stringify({ ...fullOrder.value, orderDetailDtos: [] }));

      // Show success message
      orderSuccessSheet.value = true;
    } else if (response.data.code === 401 || response.data.code === 403) {
      router.push('/login');
    } else {
      // Show error message
      orderErrorMessage.value = response.data.message;
      orderErrorAlert.value = true;
    }
  } catch (error) {
    console.error('Error submitting order:', error);
    orderErrorMessage.value = '서버와 통신 중 오류가 발생했습니다. 다시 시도해주세요.';
    orderErrorAlert.value = true;
  }
};

console.log(orderDetailDtos.value);
</script>

<style scoped>
</style>
