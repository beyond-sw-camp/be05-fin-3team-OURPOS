<template>
  <!-- 결제 성공 시 -->
  <section>
    <div class="box_section">
      <img style="width: 100px" src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" />
      <h2>결제를 완료했어요</h2>
      <p v-if="jsonData">주문번호: {{ jsonData.orderId }}</p>
      <p v-if="jsonData">결제승인번호: {{ jsonData.paymentKey }}</p>
      <p v-if="jsonData">결제금액: {{ Number(jsonData.balanceAmount).toLocaleString() }}원</p>

      <button class="button" @click="goToMain">주문 페이지로 돌아가기</button>
    </div>
  </section>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card class="my-4">
          <v-card-title>주문서</v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12">
                <h3>{{ fullOrder.storeName }} - {{ fullOrder.orderTakeoutYn ? '테이크아웃' : '매장 식사' }}</h3>
              </v-col>
            </v-row>
            <v-divider class="my-4"></v-divider>
            <v-row>
              <v-col cols="12">
                <h4>주문 내용</h4>
                <v-card v-for="(detail, index) in fullOrder.orderDetailDtos" :key="index" class="my-2">
                  <v-card-title>
                    <div class="d-flex justify-space-between align-center">
                      {{ detail.menuName }} (x{{ detail.quantity }})
                    </div>
                  </v-card-title>
                  <v-card-subtitle>
                    <v-avatar size="64" class="mx-2 my-2">
                      <v-img :src="'http://localhost:8080/images/' + detail.menuPictureUrl" cover></v-img>
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
    const fullOrder = ref(JSON.parse(localStorage.getItem('fullOrder')) || {});
    const orderDetailDtos = ref(fullOrder.value.orderDetailDtos || []);
    const route = useRoute();
    const router = useRouter();
    const confirmed = ref(false);
    const jsonData = ref(null);
    const totalPrice = computed(() => orderDetailDtos.value.reduce((total, item) => total + item.totalPrice, 0));

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
          fullOrder.value.tempOrderId = requestData.orderId;
          localStorage.setItem('fullOrder', JSON.stringify(fullOrder.value));

          await submitOrder();

          const { response, json } = await confirmPayment(requestData);
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
      fullOrder,
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
