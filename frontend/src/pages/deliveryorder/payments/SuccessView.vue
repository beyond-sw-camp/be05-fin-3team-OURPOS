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
                <h3>{{ deliveryOrder.storeName }} - 배달</h3>
              </v-col>
              <v-col cols="12">
                <h4>고객 정보</h4>
                <p>받는 사람: {{ deliveryOrder.orderAddressRequestDto.receiverName }}</p>
                <p>전화번호: {{ deliveryOrder.orderAddressRequestDto.telNo }}</p>
                <p>주소: {{ deliveryOrder.orderAddressRequestDto.addressBase }} {{ deliveryOrder.orderAddressRequestDto.addressDetail }}</p>
                <p>우편번호: {{ deliveryOrder.orderAddressRequestDto.zipcode }}</p>
              </v-col>
            </v-row>
            <v-divider class="my-4"></v-divider>
            <v-row>
              <v-col cols="12">
                <h4>주문 내용</h4>
                <v-card v-for="(detail, index) in deliveryOrder.orderDetailDtos" :key="index" class="my-2">
                  <v-card-title>
                    <div class="d-flex justify-space-between align-center">
                      {{ detail.menuName }} (x{{ detail.quantity }})
                    </div>
                  </v-card-title>
                  <v-card-subtitle>
                    <v-avatar size="64" class="mx-2 my-2">
                      <v-img :src="'https://ourpos.org/images/' + detail.menuPictureUrl" cover></v-img>
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
                <p v-if="jsonData">결제금액: {{ Number(jsonData.balanceAmount).toLocaleString() }}원</p>
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
    const deliveryOrder = ref(JSON.parse(localStorage.getItem('deliveryOrder')) || {});
    const orderDetailDtos = ref(deliveryOrder.value.orderDetailDtos || []);
    const route = useRoute();
    const router = useRouter();
    const confirmed = ref(false);
    const jsonData = ref(null);
    const totalPrice = computed(() => orderDetailDtos.value.reduce((total, item) => total + item.totalPrice, 0));

    const submitOrder = async () => {
      try {
        const response = await axios.post(
          'https://ourpos.org/api/v1/orders/delivery',
          deliveryOrder.value,
          {
            headers: {
              'Content-Type': 'application/json'
            },
            withCredentials: true
          }
        );

        if (response.data.code === 200) {
          orderDetailDtos.value = [];
          localStorage.setItem('deliveryOrder', JSON.stringify({ ...deliveryOrder.value, orderDetailDtos: [] }));
          confirmed.value = true;
        } else {
          router.push(`/fail?message=${response.data.message}&code=${response.data.code}`);
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
          deliveryOrder.value.tempOrderId = requestData.orderId;
          localStorage.setItem('deliveryOrder', JSON.stringify(deliveryOrder.value));

          await submitOrder();

          const { response, json } = await confirmPayment(requestData);
          jsonData.value = json;
          await deleteTempOrder(json.orderId);

          if (!response.ok) {
            router.push(`/fail?message=${json.message}&code=${json.code}`);
          }
        } catch (error) {
          console.error(error);
          router.push(`/fail?message=${error.message}`);
        }
      };

      confirm();
    });

    return {
      confirmed,
      jsonData,
      deliveryOrder,
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
