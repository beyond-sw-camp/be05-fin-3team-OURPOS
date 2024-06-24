<template>
  <div>
    <Navbar2/>
    <div class="container mt-5">
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
  
            <!-- Pagination Controls -->
            <div class="mt-3 d-flex justify-content-center">
              <button @click="fetchStoreComms(selectedTab, pageNumber - 1, pageSize)" :disabled="pageNumber === 1" class="btn btn-outline-secondary btn-sm">prev</button>
              <button 
                v-for="page in totalPages" 
                :key="page" 
                @click="fetchStoreComms(selectedTab, page, pageSize)" 
                class="btn btn-link text-secondary btn-sm"
                :class="{ 'text-dark': page === pageNumber }"
              >
                {{ page }}
              </button>
              <button @click="fetchStoreComms(selectedTab, pageNumber + 1, pageSize)" :disabled="pageNumber === totalPages" class="btn btn-outline-secondary btn-sm">next</button>
            </div>
          </div>
          <div class="col-3">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">장바구니</h5>
                <hr>
                <ul class="list-group">
                  <li v-if="cart.length === 0" class="list-group-item text-center">
                    장바구니가 비었습니다
                  </li>
                  <li v-else class="list-group-item" v-for="(item, index) in cart" :key="index">
                    <div class="d-flex justify-content-between">
                      <strong>{{ item.storeCommName }}</strong>
                      <strong>{{ item.storeCommPrice }} 원</strong>
                    </div>
                    <div class="input-group mt-2">
                      <span class="input-group-text">수량</span>
                      <input type="number" class="form-control me-2" v-model.number="item.quantity" min="1" @change="calculateTotal">
                    </div>
                    <div class="d-flex justify-content-end">
                      <button class="btn btn-outline-danger" @click="removeFromCart(index)"><i class="fas fa-trash-alt"></i></button>
                    </div>
                  </li>
                </ul>
                <hr>
                <div v-if="cart.length > 0">
                  <div class="d-flex justify-content-end">
                    <strong>총액:</strong> {{ cartTotal }} 원
                  </div>
                  <div class="d-flex justify-content-end">
                    <button class="btn btn-primary mt-3" @click="submitOrder">주문 요청</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script>
  import axios from 'axios';
  import Navbar2 from "@/examples/Navbars/Navbar2.vue";
  
  export default {
    components: {Navbar2},
    data() {
      return {
        selectedTab: 0,
        cart: [],
        foodItems: [],
        supplyItems: [],
        cartTotal: 0,
        pageNumber: 1,
        totalPages: 1,
        pageSize: 5  // 페이지 크기를 정의합니다.
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
              storeOrderDetailQuantity: item.quantity,
              storeCommPrice: item.storeCommPrice * item.quantity
            };
            return axios.post('http://localhost:8080/api/v1/storecomms/order', orderData, {
              headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
              }
            });
          });
  
          await Promise.all(promises);
  
          alert('주문이 완료되었습니다.');
  
          this.cart = [];
          this.cartTotal = 0;
        } catch (error) {
          console.error('주문 요청 실패:', error);
          alert('주문 요청에 실패하였습니다.');
        }
      },
      async fetchStoreComms(selectedTab = 0, page = 1, size = 5) {
        try {
          const response = await axios.get(`http://localhost:8080/api/v1/storecomms?page=${page - 1}&size=${size}`, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            }
          });
  
          console.log(response.data);  // 응답 데이터 구조 확인
          const items = response.data.data.content; // 실제 데이터가 있는 위치로 접근
  
          if (Array.isArray(items)) {  // 배열인지 확인
            this.foodItems = selectedTab === 0 ? items.filter(item => item.storeCommCategory === 'INGREDIENT') : [];
            this.supplyItems = selectedTab === 1 ? items.filter(item => item.storeCommCategory === 'SUPPLIES') : [];
            this.pageNumber = page;
            this.totalPages = response.data.data.totalPages;
          } else {
            console.error('응답 데이터가 배열이 아닙니다:', items);
          }
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
  