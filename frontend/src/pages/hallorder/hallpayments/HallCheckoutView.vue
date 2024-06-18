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
import { findLoginCustomer, saveTempOrder } from "@/services/apiService";

// 주문 데이터 초기화
const orderData = ref({});

// 데이터 및 초기값 설정
const paymentWidget = ref(null);
const paymentMethodWidget = ref(null);
const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const orderId = nanoid();
const inputEnabled = ref(false);
const customer = ref({});

// 로컬 스토리지에서 주문 데이터 가져오기
const loadOrderData = () => {
  const data = localStorage.getItem('fullOrder');
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
        customerName: customer.value.name,
        customerEmail: customer.value.email,
        customerMobilePhone: customer.value.phone,
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
  router.push('/stores');
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
  const initialData = localStorage.getItem('fullOrder');
  window.addEventListener('storage', () => {
    const currentData = localStorage.getItem('fullOrder');
    if (initialData !== currentData) {
      alert('주문 정보가 변경되었습니다.');
      router.push('/stores');
    }
  });
};

onMounted(async () => {
  loadOrderData(); // 주문 데이터 로드

  if (!orderData.value.orderDetailDtos || orderData.value.orderDetailDtos.length === 0) {
    alert('주문할 상품이 없습니다.');
    router.push('/stores');
    return;
  }

  try {
    customer.value = await findLoginCustomer();
  } catch (error) {
    console.error('Error finding login customer:', error);
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
