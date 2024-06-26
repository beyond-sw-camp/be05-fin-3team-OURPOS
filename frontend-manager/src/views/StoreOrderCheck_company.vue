storeordercheck_company

<template>
  <div class="container mt-5">
    <h2>비품, 식자재 관리</h2>
    
    <!-- 탭 네비게이션 -->
    <ul class="nav nav-tabs">
      <li class="nav-item">
        <a class="nav-link" :class="{ active: activeTab === 'WAITING' }" @click="changeTab('WAITING')">대기중</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ active: activeTab === 'ACCEPTED' }" @click="changeTab('ACCEPTED')">주문승인</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ active: activeTab === 'DELIVERING' }" @click="changeTab('DELIVERING')">배달중</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ active: activeTab === 'COMPLETED' }" @click="changeTab('COMPLETED')">배달완료</a>
      </li>
    </ul>
    
    <!-- 주문 목록 -->
    <div class="mt-4">
      <div v-if="loading">불러오는 중...</div>
      <div v-else>
        <div v-for="order in filteredOrders" :key="order.storeOrderId" class="card mb-3 shadow-sm order-card">
          <div class="card-body">
            <div class="row">
              <div class="col-md-6">
                <h5 class="card-title mb-1">주문 번호: {{ order.storeOrderId }}</h5>
                <div class="d-flex justify-content-between">
                  <p class="card-text text-muted mb-1">주문 일시: {{ order.storeOrderDate }}</p>
                  <p class="card-text text-muted mb-1">주문 금액: {{ order.storeCommPrice }}</p>
                  <p class="card-text text-muted mb-1">지점명: {{ order.storeName }}</p>
                  <p class="card-text text-muted mb-1"> {{ order.storeOrderStatus }}</p>
                  
                </div>
              </div>
              <div class="col-md-6 d-flex align-items-center justify-content-end">
                <button class="btn btn-outline-primary" @click="openModal(order)">상세 보기</button>
              </div>
            </div>
          </div>
        </div>
        <!-- 페이징 -->
        <nav v-if="totalPages > 1" aria-label="Page navigation">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ 'disabled': currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="prevPage">&laquo;</a>
            </li>
            <li class="page-item" v-for="page in totalPages" :key="page" :class="{ 'active': currentPage === page }">
              <a class="page-link" href="#" @click.prevent="goToPage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="nextPage">&raquo;</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
    
    <!-- 주문 상태 변경 모달 -->
    <div class="modal fade" tabindex="-1" role="dialog" :class="{ 'show d-block': showModal }">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">주문 상세 정보</h5>
            <button type="button" class="close" @click="closeModal">
            <i class="fas fa-times"></i>
          </button>
          </div>
          <div class="modal-body" v-if="selectedOrder">
            <p><strong>상품명:</strong> {{ selectedOrder.storeCommName }}</p>
            <p><strong>상품 수량:</strong> {{ selectedOrder.storeOrderDetailQuantity }}</p>
            <p><strong>상품 단위:</strong> {{ selectedOrder.storeCommArticleUnit }}</p>
            <p><strong>주문 날짜:</strong> {{ selectedOrder.storeOrderDate }}</p>
            <p><strong>주문 가격:</strong> {{ selectedOrder.storeOrderPrice }}</p>
            <p><strong>지점명:</strong> {{ selectedOrder.storeName }}</p>
            <p><strong>지점 주소:</strong> {{ selectedOrder.addressBase }} {{ selectedOrder.addressDetail }}</p>
            <p><strong>전화번호:</strong> {{ selectedOrder.storePhone }}</p>
            
            <!-- 주문 상태 변경 버튼 -->
            <div class="text-center" v-if="selectedOrder.storeOrderStatus === 'WAITING'">
              <button class="btn btn-primary" @click="updateOrderStatus(selectedOrder.storeOrderId, 'accepted')">주문 승인</button>
            </div>
            <div class="text-center" v-else-if="selectedOrder.storeOrderStatus === 'ACCEPTED'">
              <button class="btn btn-primary" @click="updateOrderStatus(selectedOrder.storeOrderId, 'delivering')">배달 중</button>
            </div>
            <div class="text-center" v-else-if="selectedOrder.storeOrderStatus === 'DELIVERING'">
              <button class="btn btn-primary" @click="updateOrderStatus(selectedOrder.storeOrderId, 'complete')">배달 완료</button>
            </div>
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
      activeTab: 'WAITING',
      orders: [],
      loading: false,
      currentPage: 1,
      pageSize: 5,
      totalPages: 0,
      showModal: false,
      selectedOrder: null
    };
  },
  computed: {
    filteredOrders() {
      return this.orders.filter(order => order.storeOrderStatus === this.activeTab);
    }
  },
  methods: {
    fetchOrders() {
      this.loading = true;
      axios.get(`https://api.ourpos.org/api/v1/storeorder/${this.activeTab.toLowerCase()}?page=${this.currentPage - 1}&size=${this.pageSize}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      })
      .then(response => {
        this.orders = response.data.data.content;
        this.totalPages = response.data.data.totalPages;
        this.loading = false;
      })
      .catch(error => {
        console.error('Error fetching orders:', error);
        this.loading = false;
      });
    },
    changeTab(tab) {
      this.activeTab = tab;
      this.currentPage = 1; 
      this.fetchOrders();
    },
    goToPage(page) {
      this.currentPage = page;
      this.fetchOrders();
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchOrders();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchOrders();
      }
    },
    openModal(order) {
      this.selectedOrder = order;
      this.showModal = true;
    },
    closeModal() {
      this.selectedOrder = null; 
      this.showModal = false;
    },
    updateOrderStatus(orderId, newStatus) {
      axios.put(`https://api.ourpos.org/api/v1/storeorder/${orderId}/${newStatus}`, null, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        }
      })
      .then(response => {
        console.log(`Order ${orderId} status updated to ${newStatus}`);
        // Update 
        console.log('Response:', response.data);
        this.fetchOrders();
        this.closeModal();
      })
      .catch(error => {
        console.error(`Error updating order ${orderId} status:`, error);
        
      });
    }
  },
  mounted() {
    this.fetchOrders();
  }
};
</script>

<style>
.order-card {
  border-radius: 0.5rem;
  transition: transform 0.2s;
}

.order-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.modal.show.d-block {
  display: block;
  background-color: rgba(0, 0, 0, 0.5);
}

.card-title {
  font-weight: bold;
}

.pagination .page-item.active .page-link {
  background-color: #333333;
  border-color: #333333;
}

.pagination .page-link {
  color: #333333;
}

.pagination .page-link:hover {
  color: #0056b3;
}

.modal-dialog {
  max-width: 600px;
}

.modal-body {
  max-height: 70vh;
  overflow-y: auto;
}
</style>