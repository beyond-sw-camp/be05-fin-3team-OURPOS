<template>
  <v-app>
    <v-container fluid>
      <v-row>
        <v-col cols="9">
          <v-tabs v-model="selectedTab">
            <v-tab>식자재</v-tab>
            <v-tab>비품</v-tab>
          </v-tabs>
          <div v-if="selectedTab === 0">
            <v-row>
              <v-col
                v-for="(item, index) in foodItems"
                :key="index"
                cols="4"
              >
                <v-card>
                  <v-img :src="item.storeCommPictureUrl" height="200px"></v-img>
                  <v-card-title>{{ item.storeCommName }}</v-card-title>
                  <v-card-subtitle>{{ item.storeCommPrice }} 원</v-card-subtitle>
                  <v-card-actions>
                    <v-btn @click="addToCart(item)">담기</v-btn>
                  </v-card-actions>
                </v-card>
              </v-col>
            </v-row>
          </div>
          <div v-if="selectedTab === 1">
            <v-row>
              <v-col
                v-for="(item, index) in supplyItems"
                :key="index"
                cols="4"
              >
                <v-card>
                  <v-img :src="item.storeCommPictureUrl" height="200px"></v-img>
                  <v-card-title>{{ item.storeCommName }}</v-card-title>
                  <v-card-subtitle>{{ item.storeCommPrice }} 원</v-card-subtitle>
                  <v-card-actions>
                    <v-btn @click="addToCart(item)">담기</v-btn>
                  </v-card-actions>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </v-col>
        <v-col cols="3">
          <v-card>
            <v-card-title>장바구니</v-card-title>
            <v-divider></v-divider>
            <v-list>
              <v-list-item
                v-for="(item, index) in cart"
                :key="index"
              >
                <v-list-item-content>
                  <v-list-item-title>{{ item.storeCommName }}</v-list-item-title>
                  <v-list-item-subtitle>{{ item.storeCommPrice }} 원</v-list-item-subtitle>
                  <v-text-field
                    v-model.number="item.quantity"
                    type="number"
                    min="1"
                    label="수량"
                    @change="calculateTotal"
                  ></v-text-field>
                </v-list-item-content>
                <v-list-item-action>
                  <v-btn @click="removeFromCart(index)">삭제</v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>
          </v-card>
          <v-card>
            <v-card-title>총액</v-card-title>
            <v-divider></v-divider>
            <v-card-text>{{ cartTotal }} 원</v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn color="primary" @click="submitOrder">주문 요청</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>



<script>
import axios from 'axios';

export default {
  data() {
    return {
      selectedTab: 0,
      cart: [],
      foodItems: [],
      supplyItems: [],
      cartTotal: 0
    };
  },
  methods: {
    addToCart(item) {
      const cartItem = this.cart.find(cartItem => cartItem.storeCommId === item.storeCommId);
      if (cartItem) {
        cartItem.quantity += 1;
      } else {
        this.cart.push({ ...item, quantity: 1 });
      }
      this.calculateTotal();
    },
    removeFromCart(index) {
      this.cart.splice(index, 1);
      this.calculateTotal();
    },
    calculateTotal() {
      this.cartTotal = this.cart.reduce((total, item) => total + item.storeCommPrice * item.quantity, 0);
    },
    async submitOrder() {
      if (this.cart.length === 0) {
        alert('장바구니가 비어있습니다.');
        return;
      }

      try {
        const promises = this.cart.map(item => {
          const orderData = {
            storeCommId: item.storeCommId,
            storeId: 1, // 예시로 1로 설정합니다.
            storeOrderDetailQuantity: item.quantity
          };
          return axios.post('http://localhost:8080/api/v1/storecomms/order', orderData);
        });

        await Promise.all(promises);

        // 주문이 성공적으로 완료되었다는 알림 표시
        alert('주문이 완료되었습니다.');

        // 장바구니 비우기
        this.cart = [];
        this.cartTotal = 0;
      } catch (error) {
        console.error('주문 요청 실패:', error);
        // 주문 실패 시 사용자에게 알림 표시 등의 처리를 할 수 있습니다.
        alert('주문 요청에 실패하였습니다.');
      }
    },
    async fetchStoreComms() {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/storecomms');
        const items = response.data.data;

        this.foodItems = items.filter(item => item.storeCommCategory === 'INGREDIENT');
        this.supplyItems = items.filter(item => item.storeCommCategory === 'SUPPLIES');
      } catch (error) {
        console.error('Failed to fetch store comms:', error);
      }
    }
  },
  created() {
    this.fetchStoreComms();
  }
};


</script>

<style>
/* 필요한 경우 스타일 추가 */
</style>
