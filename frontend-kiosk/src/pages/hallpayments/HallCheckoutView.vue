<template>
  <div class="box_section">
    <p class="title payment-widget-cache-bd75t9">{{ orderData.storeName }} - {{ orderData.orderTakeoutYn ? '테이크아웃' : '매장 식사' }}</p>
    <!-- 결제 UI -->
    <div id="payment-method"></div>
    <!-- 이용약관 UI -->
    <div id="agreement"></div>
    <!-- 결제하기 버튼 -->
    <button :disabled="!inputEnabled" @click="requestPayment" class="button" id="payment-button"
            style="margin-top: 30px">
      {{ Number(totalOrderPrice).toLocaleString() }}원 결제하기
    </button>

    <button @click="goBack" class="button" id="payment-button"
            style="background-color: pink">
      결제 취소
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { loadPaymentWidget, ANONYMOUS } from "@tosspayments/payment-widget-sdk";
import { nanoid } from "nanoid";
import router from "@/router";
import { saveTempOrder } from "@/services/apiService";

// 주문 데이터 초기화
const orderData = ref({});

// 데이터 및 초기값 설정
const paymentWidget = ref(null);
const paymentMethodWidget = ref(null);
const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const orderId = nanoid();
const inputEnabled = ref(false);

// 로컬 스토리지에서 주문 데이터 가져오기
const loadOrderData = () => {
  const data = localStorage.getItem('orders');
  if (data) {
    orderData.value = JSON.parse(data);
  }
};

const requestPayment = async () => {
  try {
    const response = await saveTempOrder(orderId, totalOrderPrice.value);

    if (response.code === 200) {
      console.log('Temp order submitted successfully');
    } else {
      router.push(`/hall/fail?message=${response.message}&code=${response.code}`);
      return;
    }

    if (paymentWidget.value) {
      await paymentWidget.value.requestPayment({
        orderId: orderId,
        orderName: `${orderData.value.orderDetailDtos[0].menuName} 외 ${orderData.value.orderDetailDtos.length}건`,
        successUrl: `${window.location.origin}/hall/success`,
        failUrl: `${window.location.origin}/hall/fail`,
      });
    }
  } catch (error) {
    console.error('Error submitting order:', error);
    router.push(`/hall/fail?message=${error.message}&code=500`);
  }
};

const goBack = () => {
  router.push('/');
};

const totalOrderPrice = computed(() => {
  if (orderData.value.orderDetailDtos) {
    return orderData.value.orderDetailDtos.reduce((total, orderDetail) => {
      return total + orderDetail.totalPrice;
    }, 0);
  }
  return 0;
});

const checkForChanges = () => {
  const initialData = localStorage.getItem('orders');
  window.addEventListener('storage', () => {
    const currentData = localStorage.getItem('orders');
    if (initialData !== currentData) {
      alert('주문 정보가 변경되었습니다.');
      router.push('/');
    }
  });
};

onMounted(async () => {
  loadOrderData();

  if (!orderData.value.orderDetailDtos || orderData.value.orderDetailDtos.length === 0) {
    alert('주문할 상품이 없습니다.');
    router.push('/');
    return;
  }


  const amount = totalOrderPrice.value;

  paymentWidget.value = await loadPaymentWidget(clientKey, ANONYMOUS);
  paymentMethodWidget.value = paymentWidget.value.renderPaymentMethods("#payment-method", {value: amount}, {variantKey: "DEFAULT"});
  paymentWidget.value.renderAgreement("#agreement", {variantKey: "AGREEMENT"});

  paymentMethodWidget.value.on("ready", () => {
    inputEnabled.value = true;
  });

  checkForChanges(); // 데이터 변경 감지
});
</script>

<style scoped>
@import '@/public/style.css';
</style>
