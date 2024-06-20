<template>
  <div class="landing-page">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark navigation-bar">
      <div class="mr-auto">
        <div class="POS-name">OUR POS</div>
      </div>
      <div class="ml-auto">
        <router-link to="/">
          <button class="btn btn-outline-light">
            <i class="mdi mdi-export"></i> 뒤로 가기
          </button>
        </router-link>
      </div>
    </nav>

    <!-- 메인 콘텐츠 -->
    <div class="container-fluid">
      <div class="row no-gutters">

        <!-- 왼쪽 콘텐츠 영역 - 메인 컨텐츠 -->

        <div class="col-lg-5">
          <div class="row">
            <!-- 첫 번째 카드 - 홀 주문 관리 -->
            <div class="col-6">
              <router-link to="/admin/storeLanding/hallOrderManage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">홀 주문 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <!-- 두 번째 카드 - 배달 주문 관리 -->
            <div class="col-6">
              <router-link to="/store-landing/delivery-order-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">배달 주문 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
          </div>

          <div class="row">
            <!-- 세 번째 카드 - 메뉴 상태 관리 -->
            <div class="col-6">
              <router-link to="/store-landing/menu-status-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">메뉴 상태 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <!-- 네 번째 카드 - 비품/식자재 주문 -->
            <div class="col-6">
              <router-link to="/storeorder">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">비품/식자재 주문</h5>
                  </div>
                </div>
              </router-link>
            </div>
          </div>
        </div>


        <!-- 오른쪽 콘텐츠 영역 - 서브 컨텐츠 -->
        <div class="col-lg-7">
          <!-- 첫 번째 열 - 재고 관리 -->
          <div class="row">
            <div class="col-4">
              <router-link to="/manager/insight">
                <div class="card double-height-card">
                  <div class="card-body">
                    <h5 class="card-title">비즈니스 인사이트</h5>
                  </div>
                </div>
              </router-link>
            </div>

            <!-- 두 번째 열 - 새로운 기능 추가 -->
            <div class="col-8">
              <PreStock/>
              <NowStock/>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.POS-name {
  font-size: 3rem; /* Increased font size */
  color: #f0f0f0;
  text-shadow: 1px 1px 2px #000;
}

.btn {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 20px; /* Increased padding for height */
  font-size: 1.25rem; /* Increased font size */
  text-align: center;
  cursor: pointer;
  background-color: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
}

.landing-page {
  height: 100vh;
  background-color: #f5f5f5;
}

.navigation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 10vh;
  margin-bottom: 10px;
  background-color: #2c3e50;
}

.card {
  height: 100%; /* Make the card fill its container */
  background-color: #ffffff;
  border: 1px solid #ccc;
  margin-top: 4px;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

.same-card {
  height: calc(40vh - 4px);
  display: flex;
  justify-content: center;
  align-items: center;
}

.double-height-card {
  height: calc(80vh - 8px);
  display: flex;
  justify-content: center;
  align-items: center;
}

.card-title {
  font-size: 1.5rem;
  color: #333;
}

.container-fluid {
  padding: 0 20px;
}

.row {
  margin: 0 -10px;
}

.col-4, .col-6, .col-8, .col-lg-5, .col-lg-7 {
  padding: 0 10px;
}
</style>

<script setup>

import PreStock from "./components/Insight/PreStockInsight.vue";
import NowStock from "./components/Insight/NowStockInsight.vue";

</script>
