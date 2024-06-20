<template>
  <div class="landing-page">
    <!-- 네비게이션 바 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark navigation-bar">
      <a class="navbar-brand" href="#">OUR POS</a>
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

        <div class="col-lg-4">
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
        <div class="col-lg-8">
          <!-- 첫 번째 열 - 재고 관리 -->
          <div class="row">
            <div class="col-3">
              <router-link to="/manager/insight">
                <div class="card double-height-card">
                  <div class="card-body">
                    <h5 class="card-title">비즈니스 인사이트</h5>
                  </div>
                </div>
              </router-link>
            </div>

            <!-- 두 번째 열 - 새로운 기능 추가 -->
            <div class="col-9">
              <h4>입고 예정 재고량</h4>
              <PreStock/>
            </div>




          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.landing-page {
  height: 100vh;
}

.navigation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 10vh;
  margin-bottom: 10px;
}

.same-card {
  height: calc(40vh - 4px);
  border: 1px solid #ccc;
  margin-top: 4px;
}

.double-height-card {
  height: calc(80vh - 8px);
  border: 1px solid #ccc;
  margin-top: 4px;
}


</style>

<script setup>

import PreStock from "./PreStockInsight.vue";

</script>
