<template>
    <div class="container-fluid text-center">
      <div class="row">
        <div class="col">
          <div class="card">
            <h5 class="card-header">주문 내역</h5>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-bordered">
                  <thead>
                    <tr>
                      <th style="width: 20%;">주문 번호</th>
                      <th style="width: 20%;">주문 일자</th>
                      <th style="width: 20%;">주문 금액</th>
                      <th style="width: 20%;">주문 상태</th>
                      <th style="width: 20%;"></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(order, index) in orders" :key="index">
                      <td>{{ order.storeOrderId }}</td>
                      <td>{{ order.storeOrderDate }}</td>
                      <td>{{ order.storeCommPrice }}</td>
                      <td>{{ order.storeOrderStatus }}</td>
                      <td>
                        <button class="btn btn-warning" @click="cancelOrder(order.storeOrderId)" style="margin-right: 10px;">취소</button>
                        <button class="btn btn-primary" @click="openOrderDetails(order)">상세</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 주문 상세 정보 모달 -->
      <div v-if="selectedOrder !== null" class="modal fade show" style="display: block;" id="orderDetailModal" tabindex="-1" role="dialog" aria-labelledby="orderDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="orderDetailModalLabel">주문 상세 정보</h5>
              <button type="button" class="close" @click="closeOrderDetails">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="table-responsive">
                <table class="table table-bordered">
                  <thead>
                    <tr>
                      <th>이름</th>
                      <th>가격</th>
                      <th>수량</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in selectedOrder.items" :key="index">
                      <td>{{ item.storeCommName }}</td>
                      <td>{{ item.storeCommPrice }}</td>
                      <td>{{ item.storeOrderDetailQuantity }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-primary" @click="closeOrderDetails">닫기</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        orders: [],
        selectedOrder: null
      };
    },
    created() {
      this.fetchOrders();
    },
    methods: {
      async fetchOrders() {
        try {
          const response = await axios.get('http://localhost:8080/api/v1/storeorder/1/checkforstore',{
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            }
          });
          this.orders = response.data.data;
        } catch (error) {
          console.error('Failed to fetch orders:', error);
        }
      },
      async cancelOrder(storeOrderId) {
        try {
          const response = await axios.delete(`http://localhost:8080/api/v1/storeorder/${storeOrderId}`,{
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            }
          });
          if (response.status === 200) {
            this.orders = this.orders.filter(order => order.storeOrderId !== storeOrderId);
            alert('주문이 성공적으로 취소되었습니다.');
          }
        } catch (error) {
          console.error('Failed to cancel order:', error);
          alert('주문 취소에 실패했습니다.');
        }
      },
      async openOrderDetails(order) {
        // 주문 상세 정보를 불러오는 API 요청
        try {
          const response = await axios.get(`http://localhost:8080/api/v1/storeorder/${order.storeOrderId}`,{
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('token')
            }
          });
          order.items = response.data; // 주문 항목 추가
          this.selectedOrder = order; // 선택된 주문 설정
        } catch (error) {
          console.error('Failed to fetch order details:', error);
        }
      },
      closeOrderDetails() {
        this.selectedOrder = null; // 모달 닫기
      }
    }
  };
  </script>
  
  <style scoped>

  </style>
  