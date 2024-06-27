<template>
  <div class="landing-page">
    <Navbar2/>

    <!-- 메인 콘텐츠 -->
    <div class="container-fluid">
      <div class="row no-gutters">

        <!-- 왼쪽 콘텐츠 영역 - 메인 컨텐츠 -->
        <div class="col-lg-5">
          <div class="row">
            <!-- 첫 번째 카드 - 홀 주문 관리 -->
            <div class="col-6">
              <router-link to="/hallOrderManage">
                <div class="card same-card">
                  <div class="card-body">
                    <div class="card-content">
                      <i class="fas fa-utensils fa-3x"></i>
                      <h5 class="card-title">홀 주문 관리</h5>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
            <!-- 두 번째 카드 - 배달 주문 관리 -->
            <div class="col-6">
              <router-link to="/store-landing/delivery-order-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <div class="card-content">
                      <i class="fas fa-truck fa-3x"></i>
                      <h5 class="card-title">배달 주문 관리</h5>
                    </div>
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
                    <div class="card-content">
                      <i class="fas fa-list-alt fa-3x"></i>
                      <h5 class="card-title">메뉴 상태 관리</h5>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
            <!-- 네 번째 카드 - 비품/식자재 주문 -->
            <div class="col-6">
              <router-link to="/storeorder">
                <div class="card same-card">
                  <div class="card-body">
                    <div class="card-content">
                      <i class="fas fa-boxes fa-3x"></i>
                      <h5 class="card-title">비품/식자재 주문</h5>
                    </div>
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
                    <div class="card-content">
                      <i class="fas fa-chart-line fa-3x"></i>
                      <h5 class="card-title">비즈니스 인사이트</h5>
                    </div>
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

<script setup>
import PreStock from "./components/Insight/PreStockInsight.vue";
import NowStock from "./components/Insight/NowStockInsight.vue";
import Navbar2 from "@/examples/Navbars/Navbar2.vue";
</script>

<style scoped>
/* 필요한 스타일 추가 */
.landing-page {
  min-height: 100vh; /* 최소 높이로 설정하여 내용에 따라 늘어날 수 있도록 함 */
  background-color: #f5f5f5;
}

.card {
  background-color: #ffffff;
  border: 1px solid #ccc;
  margin-top: 10px; /* 각 카드 사이에 여백을 조정 */
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

.same-card {
  height: 37vh; /* 원하는 높이로 조정 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.double-height-card {
  height: 76.5vh; /* 원하는 높이로 조정 */
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

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>
