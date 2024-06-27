<template>
  <!-- 결제 성공 시 -->
  <section>
    <div class="box_section">
      <img style="width: 100px" src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" />
      <h2>결제를 완료했어요</h2>
      <p v-if="jsonData">주문번호: {{ jsonData.orderId }}</p>
      <p v-if="jsonData">결제승인번호: {{ jsonData.paymentKey }}</p>
      <p v-if="jsonData && jsonData.balanceAmount !== null">결제금액: {{ Number(jsonData.balanceAmount).toLocaleString() }}원</p>
      <p v-if="jsonData">결제 일시: {{ new Date(jsonData.approvedAt).toLocaleString() }}</p>
      <button class="button" @click="goToMain">주문 페이지로 돌아가기</button>
    </div>
  </section>
  <v-container v-if="jsonData">
    <v-row>
      <v-col cols="12">
        <v-card class="my-4">
          <v-card-title>주문서</v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12">
                <h3>{{ orders.orderTakeoutYn ? '테이크아웃' : '매장 식사' }}</h3>
              </v-col>
            </v-row>
            <v-divider class="my-4"></v-divider>
            <v-row>
              <v-col cols="12">
                <h4>주문 내용</h4>
                <v-card v-for="(detail, index) in orders.orderDetailDtos" :key="index" class="my-2">
                  <v-card-title>
                    <div class="d-flex justify-space-between align-center">
                      {{ detail.menuName }} (x{{ detail.quantity }})
                    </div>
                  </v-card-title>
                  <v-card-subtitle>
                    <v-avatar size="64" class="mx-2 my-2">
                      <v-img :src="'https://api.ourpos.org/images/' + detail.menuPictureUrl" cover></v-img>
                    </v-avatar>
                  </v-card-subtitle>
                  <v-card-text>
                    <p><strong>가격:</strong> {{ Number(detail.totalPrice).toLocaleString() }}원</p>
                    <div v-for="(optionGroup, i) in detail.orderOptionGroups" :key="i">
                      <p><strong>{{ optionGroup.optionGroupName }}:</strong></p>
                      <v-chip v-for="(option, j) in optionGroup.orderOptions" :key="j">
                        {{ option.optionName }}
                      </v-chip>
                    </div>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
            <v-divider class="my-4"></v-divider>
            <v-row>
              <v-col cols="12" class="text-right">
                <h4>총 금액: {{ Number(jsonData.balanceAmount).toLocaleString() }}원</h4>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { confirmPayment } from "@/confirmPayment";
import axios from "axios";
import { deleteTempOrder } from "@/services/apiService";

export default {
  setup() {
    const orders = ref(JSON.parse(localStorage.getItem('orders')) || {});
    const orderDetailDtos = ref(orders.value.orderDetailDtos || []);
    const route = useRoute();
    const router = useRouter();
    const confirmed = ref(false);
    const jsonData = ref(null);
    const totalPrice = computed(() => orderDetailDtos.value.reduce((total, item) => total + item.totalPrice, 0));

    const submitOrder = async () => {

      try {
        const response = await axios.post(
          'https://api.ourpos.org/api/v1/orders/kiosk',
          {
            storeId: orders.value.storeId,
            orderTakeoutYn: orders.value.orderTakeoutYn,
            orderDetailDtos: orders.value.orderDetailDtos
          },
          {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            },
          }
        );
        console.log('Order response:', response.data);

        if (response.data.code === 200) {
          // Clear the cart after successful order
          orderDetailDtos.value = [];
          localStorage.setItem('orders', JSON.stringify({ ...orders.value, orderDetailDtos: [] }));

          // Show success message
        } else if (response.data.code === 401 || response.data.code === 403) {
          router.push('/login');
        } else {
          // Show error message
        }
      } catch (error) {
        console.error('Error submitting order:', error);
      }
    };


    const goToMain = () => {
      router.push('/');
    };

    onMounted(async () => {
      const requestData = {
        orderId: route.query.orderId,
        amount: route.query.amount,
        paymentKey: route.query.paymentKey,
      };

      const confirm = async () => {
        try {
          orders.value.tempOrderId = requestData.orderId;
          localStorage.setItem('orders', JSON.stringify(orders.value));

          await submitOrder();

          const { response, json } = await confirmPayment(requestData);
          console.log('Payment confirmation:', json)
          jsonData.value = json;
          await deleteTempOrder(json.orderId);

          if (!response.ok) {
            router.push(`/hall/fail?message=${json.message}&code=${json.code}`);
          }
        } catch (error) {
          console.error(error);
          router.push(`/hall/fail?message=${error.message}`);
        }
      };

      confirm();
    });

    return {
      confirmed,
      jsonData,
      orders,
      totalPrice,
      placeOrder: submitOrder,
      goToMain
    };
  },
};
</script>

<style scoped>
@import '@/public/style.css';
</style>
