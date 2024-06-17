<template>
    <div class="container-fluid">
      <div class="row">
        <div class="col-9">
          <ul class="nav nav-tabs">
            <li class="nav-item" @click="selectedTab = 0">
              <a class="nav-link" :class="{ active: selectedTab === 0 }">식자재</a>
            </li>
            <li class="nav-item" @click="selectedTab = 1">
              <a class="nav-link" :class="{ active: selectedTab === 1 }">비품</a>
            </li>
          </ul>
          <div v-if="selectedTab === 0" class="row mt-3">
            <div v-for="(item, index) in foodItems" :key="index" class="col-4 mb-3">
              <div class="card">
                <img :src="item.storeCommPictureUrl" class="card-img-top" alt="...">
                <div class="card-body">
                  <h5 class="card-title">{{ item.storeCommName }}</h5>
                  <p class="card-text">{{ item.storeCommPrice }} 원</p>
                  <button class="btn btn-primary" @click="addToCart(item)">담기</button>
                </div>
              </div>
            </div>
          </div>
          <div v-if="selectedTab === 1" class="row mt-3">
            <div v-for="(item, index) in supplyItems" :key="index" class="col-4 mb-3">
              <div class="card">
                <img :src="item.storeCommPictureUrl" class="card-img-top" alt="...">
                <div class="card-body">
                  <h5 class="card-title">{{ item.storeCommName }}</h5>
                  <p class="card-text">{{ item.storeCommPrice }} 원</p>
                  <button class="btn btn-primary" @click="addToCart(item)">담기</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-3">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">장바구니</h5>
              <hr>
              <ul class="list-group">
                <li class="list-group-item" v-for="(item, index) in cart" :key="index">
                  <div>
                    <strong>{{ item.storeCommName }}</strong>
                  </div>
                  <div>
                    <strong>{{ item.storeCommPrice }} 원</strong>
                  </div>
                  <div class="input-group mt-2">
                    <div>
                        <span class="input-group-text">수량</span>
                        <input type="number" class="form-control" v-model.number="item.quantity" min="1" @change="calculateTotal">
                    </div>
                    <div>
                        <button class="btn btn-outline-danger" @click="removeFromCart(index)">삭제</button>
                    </div>
                  </div>
                </li>
              </ul>
              <hr>
              <div>
                <strong>총액:</strong> {{ cartTotal }} 원
              </div>
              <button class="btn btn-primary mt-3" @click="submitOrder">주문 요청</button>
            </div>
          </div>
        </div>
      </div>
    </div>
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
        // Promise 배열을 사용, 각 아이템에 대한 주문 요청을 모아서 처리
        const promises = this.cart.map(item => {
            const orderData = {
                storeCommId: item.storeCommId,
                storeId: 1, // 예시로 1로 설정합니다.
                storeOrderDetailQuantity: item.quantity
            };
            return axios.post('http://localhost:8080/api/v1/storecomms/order', orderData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem('token')
                }
            });
        });

        // 모든 주문 요청이 완료될 때까지 기다림
        await Promise.all(promises);

        // 주문이 성공적으로 완료되었다는 알림 표시
        alert('주문이 완료되었습니다.');

        // 장바구니 비우기
        this.cart = [];
        this.cartTotal = 0;
    } catch (error) {
        console.error('주문 요청 실패:', error);
        // 주문 실패 시 사용자에게 알림 표시 등의 처리 가능
        alert('주문 요청에 실패하였습니다.');
    }
},

      async fetchStoreComms() {
        try {
          const response = await axios.get('http://localhost:8080/api/v1/storecomms',{
          headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
          }
      });
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
  
  <style scoped>
 
  </style>
  