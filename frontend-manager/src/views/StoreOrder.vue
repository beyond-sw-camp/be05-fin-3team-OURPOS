<template>
  <div>
    <Navbar2 />
    <div class="container mt-5">
      <div class="row">
        <div class="col-lg-8">
          <ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
              <button
                class="nav-link active"
                id="ingredients-tab"
                data-bs-toggle="tab"
                data-bs-target="#ingredients"
                type="button"
                role="tab"
                aria-controls="ingredients"
                aria-selected="true"
                @click="fetchData('ingredients')"
              >
                식자재
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                class="nav-link"
                id="supplies-tab"
                data-bs-toggle="tab"
                data-bs-target="#supplies"
                type="button"
                role="tab"
                aria-controls="supplies"
                aria-selected="false"
                @click="fetchData('supplies')"
              >
                비품
              </button>
            </li>
          </ul>
          <div class="tab-content" id="myTabContent">
            <div
              class="tab-pane fade show active"
              id="ingredients"
              role="tabpanel"
              aria-labelledby="ingredients-tab"
            >
              <div v-if="loading">Loading...</div>
              <div v-else class="card-container">
                <div
                  v-for="item in items"
                  :key="item.storeCommId"
                  class="card"
                >
                  <img :src="item.storeCommPictureUrl" class="card-img-top" alt="..." />
                  <div class="card-body">
                    <h5 class="card-title">{{ item.storeCommName }}</h5>
                    <p class="card-text">{{ item.storeCommPrice }} 원</p>
                    
                    <button class="btn btn-outline-secondary" @click="addToCart(item)">담기</button>
                  </div>
                </div>
              </div>
            </div>
            <div
              class="tab-pane fade"
              id="supplies"
              role="tabpanel"
              aria-labelledby="supplies-tab"
            >
              <div v-if="loading">Loading...</div>
              <div v-else class="card-container">
                <div
                  v-for="item in items"
                  :key="item.storeCommId"
                  class="card"
                >
                  <img :src="item.storeCommPictureUrl" class="card-img-top" alt="..." />
                  <div class="card-body">
                    <h5 class="card-title">{{ item.storeCommName }}</h5>
                    <p class="card-text">{{ item.storeCommPrice }} 원</p>
                    
                    <button class="btn btn-outline-secondary " @click="addToCart(item)">담기</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination Controls -->
          <div class="mt-3 d-flex justify-content-center">
            <button
              @click="fetchData(currentCategory, pageNumber - 1)"
              :disabled="pageNumber === 1"
              class="btn btn-outline-secondary btn-sm"
            >
              prev
            </button>
            <button
              v-for="page in totalPages"
              :key="page"
              @click="fetchData(currentCategory, page)"
              class="btn btn-link text-secondary btn-sm"
              :class="{ 'text-dark': page === pageNumber }"
            >
              {{ page }}
            </button>
            <button
              @click="fetchData(currentCategory, pageNumber + 1)"
              :disabled="pageNumber === totalPages"
              class="btn btn-outline-secondary btn-sm"
            >
              next
            </button>
          </div>
        </div>

        <!-- Cart Section -->
        <div class="col-lg-4">
          <div class="cart sticky-top mt-3">
            <h5>장바구니</h5>
            <ul class="list-group">
              <li v-if="cart.length === 0" class="list-group-item">장바구니가 비어 있습니다.</li>
              <li v-for="(item, index) in cart" :key="index" class="list-group-item">
                <div class="d-flex justify-content-between">
                  <span>{{ item.storeCommName }}</span>
                  <span>{{ item.storeCommPrice }} 원 x {{ item.quantity }}</span>
                  <button class="btn btn-danger btn-sm" @click="removeFromCart(index)">삭제</button>
                </div>
              </li>
            </ul>
            <div class="mt-3" v-if="cart.length > 0">
              <p class="text-end"><strong>총 금액: {{ totalCartPrice }} 원</strong></p>
              <button class="btn btn-primary w-100" @click="submitOrder">주문 요청</button>
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
  components: { Navbar2 },
  data() {
    return {
      items: [],
      cart: [],
      loading: false,
      currentCategory: 'ingredients',
      pageNumber: 1,
      totalPages: 1,
      pageSize: 5, // 페이지 크기를 정의합니다.
    };
  },
  computed: {
    totalCartPrice() {
      return this.cart.reduce((total, item) => total + item.storeCommPrice * item.quantity, 0);
    }
  },
  methods: {
    async fetchData(category, page = 1) {
      this.loading = true;
      this.currentCategory = category;
      let url = '';
      if (category === 'ingredients') {
        url = `http://localhost:8080/api/v1/storecomms/ingredients?page=${page - 1}&size=${this.pageSize}`;
      } else if (category === 'supplies') {
        url = `http://localhost:8080/api/v1/storecomms/supplies?page=${page - 1}&size=${this.pageSize}`;
      }
      try {
        const response = await axios.get(url, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
          }
        });
        this.items = response.data.data.content;
        this.pageNumber = page;
        this.totalPages = response.data.data.totalPages;
      } catch (error) {
        console.error('Error fetching data', error);
      } finally {
        this.loading = false;
      }
    },
    addToCart(item) {
      const cartItem = this.cart.find(i => i.storeCommId === item.storeCommId);
      if (cartItem) {
        cartItem.quantity += 1;
      } else {
        this.cart.push({ ...item, quantity: 1 });
      }
    },
    removeFromCart(index) {
      this.cart.splice(index, 1);
    },
    async submitOrder() {
      if (this.cart.length === 0) {
        alert('장바구니가 비어있습니다.');
        return;
      }
      try {

        // 관리자 로그인 ID를 사용하여 storeId 조회 API 호출
        const storeIdResponse = await axios.get(`http://localhost:8080/api/v1/findStoreId/my`,{
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
          }
        });
        const storeId = storeIdResponse.data;
        
        const promises = this.cart.map(item => {
          const orderData = {
            storeCommId: item.storeCommId,
            storeId: storeId, 
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
      } catch (error) {
        console.error('주문 요청 실패:', error);
        alert('주문 요청에 실패하였습니다.');
      }
    }
  },
  mounted() {
    this.fetchData('ingredients');
  }
};
</script>

<style>
.container {
  margin-top: 20px;
}
.card-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1rem;
}
.card {
  width: 100%;
  height: auto;
  margin-bottom: 1rem;
}
.card-body {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.card-img-top {
  max-height: 250px;
  object-fit: cover;
}
.cart {
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 5px;
  background-color: #f9f9f9;
  max-height: 400px; /* 장바구니 섹션의 최대 높이를 조정합니다 */
  overflow-y: auto; /* 섹션이 넘칠 경우 스크롤바를 표시합니다 */
}
.sticky-top {
  position: sticky;
  top: 20px;
}
</style>


