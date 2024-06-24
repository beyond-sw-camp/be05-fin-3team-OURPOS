<template>
  <div>
    <div class="container mt-5">
      <h2>식자재/비품 주문</h2>
      <div class="row mb-3">
        <div class="col">
          <ul class="nav nav-tabs">
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'WAITING' }" 
                @click="fetchOrders('WAITING', 0)"
                href="#"
              >
                대기중
              </a>
            </li>
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'ACCEPTED' }" 
                @click="fetchOrders('ACCEPTED', 0)"
                href="#"
              >
                주문승인
              </a>
            </li>
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'DELIVERING' }" 
                @click="fetchOrders('DELIVERING', 0)"
                href="#"
              >
                배송중
              </a>
            </li>
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'COMPLETED' }" 
                @click="fetchOrders('COMPLETED', 0)"
                href="#"
              >
                배송 완료
              </a>
            </li>
          </ul>
        </div>
      </div>
      <div class="row">
        <div class="col-9">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">주문 리스트</h5>
              <ul class="list-group">
                <li v-for="order in filteredOrders" :key="order.storeOrderId" class="list-group-item">
                  <div class="row">
                    <div class="col"><strong>주문 번호:</strong> {{ order.storeOrderId }}</div>
                    <div class="col"><strong>주문 일시:</strong> {{ order.storeOrderDate }}</div>
                    <div class="col"><strong>주문 금액:</strong> {{ order.storeCommPrice }}</div>
                    <div class="col"><strong>지점명:</strong> {{ order.storeName }}</div>
                    <div class="col">
                      <a href="#" @click="showOrderDetail(order)">
                        {{ order.storeOrderStatus }}
                      </a>
                    </div>
                  </div>
                </li>
              </ul>

              <p v-if="filteredOrders.length === 0 && !loading" class="text-center mt-3">주문이 없습니다.</p>

              <!-- 페이지 네이션 -->
              <div class="mt-3 d-flex justify-content-center">
                <button @click="fetchOrders(selectedTab, currentPage - 1)" :disabled="currentPage === 1" class="btn btn-outline-secondary btn-sm">prev</button>
                <button 
                  v-for="page in totalPages" 
                  :key="page" 
                  @click="fetchOrders(selectedTab, page-1)" 
                  class="btn btn-link text-secondary btn-sm"
                  :class="{ 'text-dark': page === currentPage }"
                >
                  {{ page }}
                </button>
                <button @click="fetchOrders(selectedTab, currentPage + 1)" :disabled="currentPage === totalPages" class="btn btn-outline-secondary btn-sm">next</button>
              </div>
            </div>
          </div>
        </div>
        <!-- 주문 세부 정보 표시 -->
        <div class="col-3" v-if="selectedOrderDetail">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">주문 세부 정보</h5>
              <div>
                <div><strong>상품명:</strong> {{ selectedOrderDetail.storeCommName }}</div>
                <div><strong>상품 수량:</strong> {{ selectedOrderDetail.storeOrderDetailQuantity }}</div>
                <div><strong>상품 단위:</strong> {{ selectedOrderDetail.storeCommArticleUnit }}</div>
                <div><strong>주문 날짜:</strong> {{ selectedOrderDetail.storeOrderDate }}</div>
                <div><strong>주문 가격:</strong> {{ selectedOrderDetail.storeCommPrice }}</div>
                <div><strong>지점명:</strong> {{ selectedOrderDetail.storeName }}</div>
                <div><strong>지점 주소:</strong> {{ selectedOrderDetail.addressDetail }}</div>
                <div><strong>전화번호:</strong> {{ selectedOrderDetail.storePhone }}</div>
              </div>
              <!-- 주문 상태 변경 버튼 -->
              <div class="mt-3">
                <button v-if="selectedOrderDetail.storeOrderStatus === 'WAITING'"
                        @click="changeOrderStatus(selectedOrderDetail.storeOrderId, 'ACCEPTED')"
                        class="btn btn-success btn-sm">
                  ACCEPTED
                </button>
                <button v-else-if="selectedOrderDetail.storeOrderStatus === 'ACCEPTED'"
                        @click="changeOrderStatus(selectedOrderDetail.storeOrderId, 'DELIVERING')"
                        class="btn btn-primary btn-sm">
                  DELIVERING
                </button>
                <button v-else-if="selectedOrderDetail.storeOrderStatus === 'DELIVERING'"
                        @click="changeOrderStatus(selectedOrderDetail.storeOrderId, 'COMPLETED')"
                        class="btn btn-primary btn-sm">
                  COMPLETED
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 로딩 및 에러 메시지 표시 -->
      <div v-if="loading" class="text-center mt-3">
        <div class="spinner-border text-primary" role="status">
          <span class="sr-only">Loading...</span>
        </div>
      </div>
      <div v-if="error" class="alert alert-danger mt-3">
        {{ error }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      orders: [],
      loading: false,
      error: null,
      selectedOrderDetail: null, // 선택된 주문의 세부 정보를 저장할 상태
      selectedTab: 'WAITING', // 현재 선택된 탭의 상태
      currentPage: {
        WAITING: 1,
        ACCEPTED: 1,
        DELIVERING: 1,
        COMPLETED: 1
      }, // 각 탭 별 현재 페이지 (1부터 시작)
      totalPages: {
        WAITING: 0,
        ACCEPTED: 0,
        DELIVERING: 0,
        COMPLETED: 0
      }, // 각 탭 별 총 페이지 수
      pageSize: 5, // 페이지 당 항목 수
    };
  },
  computed: {
    // 현재 선택된 탭에 해당하는 주문 필터링
    filteredOrders() {
      return this.orders.filter(order => order.storeOrderStatus === this.selectedTab);
    }
  },
  created() {
    this.fetchOrders('WAITING', 0); // 초기화 시 대기중 주문 조회
    this.fetchOrders('ACCEPTED', 0); // 초기화 시 주문승인 조회
    this.fetchOrders('DELIVERING', 0); // 초기화 시 배송중 조회
    this.fetchOrders('COMPLETED', 0); // 초기화 시 배송 완료 조회
  },
  methods: {
    async fetchOrders(status, page) {
      
      this.loading = true;
      this.error = null;
      this.selectedTab = status;
      this.currentPage = page;
      

      const token = localStorage.getItem('token');
      if (!token) {
        this.error = 'No token found in local storage';
        this.loading = false;
        return;
      }

      try {
        const response = await axios.get(`http://localhost:8080/api/v1/storeorder/1/check`, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': token
          },
          withCredentials: true,
          params: {
            status: status,
            page: page,
            size: this.pageSize
          }
        });

        this.orders = response.data.data.content;
        this.currentPage = response.data.data.number + 1;
        this.totalPages = response.data.data.totalPages;

      } catch (error) {
        this.error = 'Error fetching orders';
        console.error('Error fetching orders:', error);

        if (error.response && error.response.status === 401) {
          this.error = 'Unauthorized: Invalid or expired token';
          console.error('Unauthorized: Invalid or expired token');
        }
      } finally {
        this.loading = false;
      }
    },

    showOrderDetail(order) {
      // 선택된 주문의 세부 정보 표시
      this.selectedOrderDetail = order;
    },

    async changeOrderStatus(storeOrderId, action) {
      const token = localStorage.getItem('token');
      if (!token) {
        this.error = 'No token found in local storage';
        return;
      }

      let url = `http://localhost:8080/api/v1/storeorder/${storeOrderId}`;
      if (action === 'ACCEPTED') {
        url += '/accepted';
      } else if (action === 'DELIVERING') {
        url += '/delivering';
      } else if (action === 'COMPLETED') {
        url += '/complete';
      }

      try {
        await axios.put(url, {}, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':'${token}'
          },
          withCredentials:true
        });
        this.selectedOrderDetail = null; //상태 변경 후 세부 정보 탭 숨기기
        this.fetchOrders(this.selectedTab,
        this.currentPage);
      }catch(error){
        this.error=`Error changing order status: ${error.message}`;
        console.error('Error changing order status:', error);
      }
      },
    },
  };
  </script>

  <style scoped>
  .text-center {
    text-align: center;
  }
  </style>

