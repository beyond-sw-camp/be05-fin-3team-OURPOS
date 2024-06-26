<template>
  <div>
  <Navbar2/>
  <div>
    <div class="container mt-4">
      <h2>주문 목록</h2>
      <div class="row mb-3">
        <div class="col">
          <ul class="nav nav-tabs">
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'WAITING' }" 
                @click.prevent="fetchOrders('WAITING', 1, pageSize)"
                href="#"
              >
                대기중
              </a>
            </li>
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'COOKING' }" 
                @click.prevent="fetchOrders('COOKING', 1, pageSize)"
                href="#"
              >
                조리중
              </a>
            </li>
            <li class="nav-item">
              <a 
                class="nav-link" 
                :class="{ active: selectedTab === 'COMPLETED' }" 
                @click.prevent="fetchOrders('COMPLETED', 1, pageSize)"
                href="#"
              >
                조리완료
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
              <div v-if="orders.length === 0" class="d-flex justify-content-center align-items-center" style="height: 200px;">
                <p>"주문이 없습니다"</p>
              </div>
              <ul class="list-group">
                <li v-for="order in orders" :key="order.orderId" class="list-group-item">
                  <div class="row">
                    <div class="col"><strong>주문 번호:</strong> {{ order.orderId }}</div>
                    <div class="col"><strong>주문 일시:</strong> {{ order.orderCreatedDateTime }}</div>
                    <div class="col"><strong>경과 시간:</strong> {{ order.formattedCookingTime }}</div>
                    <div class="col"><strong>주문 금액:</strong> {{ order.price }} 원</div>
                    <div class="col">
                      <a href="#" @click.prevent="showOrderDetail(order)">
                        {{ order.hallOrderStatus }}
                      </a>
                    </div>
                  </div>
                </li>
              </ul>
              <div class="mt-3 d-flex justify-content-center">
                <button @click="fetchOrders(selectedTab, pageNumber - 1, pageSize)" :disabled="pageNumber === 1" class="btn btn-outline-secondary btn-sm">prev</button>
                <button 
                  v-for="page in totalPages" 
                  :key="page" 
                  @click="fetchOrders(selectedTab, page, pageSize)" 
                  class="btn btn-link text-secondary btn-sm"
                  :class="{ 'text-dark': page === pageNumber }"
                >
                  {{ page }}
                </button>
                <button @click="fetchOrders(selectedTab, pageNumber + 1, pageSize)" :disabled="pageNumber === totalPages" class="btn btn-outline-secondary btn-sm">next</button>
              </div>
            </div>
          </div>
        </div>
        <!-- 세부 정보 표시 -->
        <div class="col-3" v-if="selectedOrderDetail">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">주문 세부 정보</h5>
              <div v-if="Array.isArray(selectedOrderDetail.orderDetailResponseDtos)">
                <div v-for="(detail, index) in selectedOrderDetail.orderDetailResponseDtos" :key="index">
                  <div><strong>상품명:</strong> {{ detail.menuName }}</div>
                  <div><strong>가격:</strong> {{ detail.menuPrice }} 원</div>
                  <div><strong>수량:</strong> {{ detail.quantity }}</div>
                  <!-- 추가적인 세부 정보 표시 -->
                  <div v-if="Array.isArray(detail.orderOptionGroupResponseDtos)">
                    <div v-for="(optionGroup, idx) in detail.orderOptionGroupResponseDtos" :key="idx">
                      <div><strong>{{ optionGroup.optionGroupName }}:</strong></div>
                      <ul>
                        <li v-for="(option, i) in optionGroup.orderOptionResponseDtos" :key="i">
                          {{ option.optionName }}
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else>
                <div><strong>상품명:</strong> {{ selectedOrderDetail.menuName }}</div>
                <div><strong>가격:</strong> {{ selectedOrderDetail.price }} 원</div>
                <!-- 추가적인 세부 정보 표시 -->
              </div>
              <!-- 주문 상태 변경 버튼 추가 -->
              <div class="mt-3 text-center" v-if="selectedOrderDetail.hallOrderStatus === 'WAITING'">
                <button @click="changeOrderStatus(selectedOrderDetail.orderId, 'accept')" class="btn btn-success btn-sm">조리시작</button>
                <button @click="changeOrderStatus(selectedOrderDetail.orderId, 'cancel')" class="btn btn-warning btn-sm">주문취소</button>
              </div>
              <div class="mt-3 text-center" v-if="selectedOrderDetail.hallOrderStatus === 'COOKING'">
                <button @click="changeOrderStatus(selectedOrderDetail.orderId, 'complete')" class="btn btn-primary btn-sm">조리완료</button>
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
      orders: [],
      loading: false,
      error: null,
      selectedOrderDetail: null, // 선택된 주문의 세부 정보를 저장할 상태
      selectedTab: 'WAITING',
      pageNumber: 1,
      pageSize: 5,
      totalPages: 1
    };
  },
  created() {
    this.fetchOrders(this.selectedTab, this.pageNumber, this.pageSize);
  },
  methods: {
    async fetchOrders(status, page, size) {
      this.loading = true;
      this.error = null;
      this.selectedTab = status;
      const token = localStorage.getItem('token');

      if (!token) {
        this.error = 'No token found in local storage';
        this.loading = false;
        return;
      }

      try {
        const response = await axios.get(`https://api.ourpos.org/api/v1/orders/hall/my`, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${token}` 
          },
          withCredentials: true,
          params: {
            status: status,
            page: page - 1,
            size: size
          }
        });
        this.orders = response.data.data.content;
        this.pageNumber = response.data.data.pageable.pageNumber + 1;
        this.pageSize = response.data.data.pageable.pageSize;
        this.totalPages = response.data.data.totalPages;
        console.log(response.data.data.content);
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
      // 선택된 주문의 세부 정보를 저장하고 표시
      this.selectedOrderDetail = order;
    },

    async changeOrderStatus(orderId, action) {
      const token = localStorage.getItem('token');

      if (!token) {
        this.error = 'No token found in local storage';
        return;
      }

      let url = `https://api.ourpos.org/api/v1/orders/hall/${orderId}`;
      if (action === 'accept') {
        url += '/accept';
      } else if (action === 'cancel') {
        url += '/cancel';
      } else if (action === 'complete') {
        url += '/complete';
      }

      try {
        await axios.put(url, {}, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${token}` 
          },
          withCredentials: true
        });
        this.selectedOrderDetail = null; // 상태 변경 후 세부 정보 탭 숨기기
        this.fetchOrders(this.selectedTab, this.pageNumber, this.pageSize); // 상태 변경 후 주문 목록 새로고침
      } catch (error) {
        this.error = `Error changing order status: ${error.message}`;
        console.error('Error changing order status:', error);
      }
    },
    goBack() {
     
      this.$router.go(-1); 
    }

  }
};
</script>

<style scoped>
.nav-link.active {
  font-weight: bold;
}
</style>