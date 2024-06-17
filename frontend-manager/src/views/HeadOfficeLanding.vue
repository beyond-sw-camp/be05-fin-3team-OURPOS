<template>
  <div class="landing-page">
    <nav class="navbar navbar-dark bg-dark navigation-bar">
      <span class="navbar-brand">OUR POS</span>
      <button class="btn btn-outline-light ml-auto">
        <i class="mdi mdi-export"></i>
      </button>
    </nav>

    <div class="container-fluid">
      <div class="row no-gutters">
        <div class="col-12 card-group">
          <div class="row">
            <div class="col-3">
              <router-link to="/head-office-landing/menu-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title text-center">메뉴/카테고리 관리</h5>
                  </div>
                </div>
              </router-link>
              <router-link to="/head-office-landing/menu-option-group-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title text-center">메뉴옵션그룹/메뉴옵션 관리 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-3">
              <div class="card same-card">
                <div class="card-body">
                  <h5 class="card-title text-center">비품/식자재 관리</h5>
                </div>
              </div>
              <div class="card same-card">
                <div class="card-body">
                  <h5 class="card-title text-center">비품/식자재 주문 관리</h5>
                </div>
              </div>
            </div>
            <div class="col-3">
              <div class="card same-card">
                <div class="card-body">
                  <h5 class="card-title text-center">비즈니스 인사이트 관리</h5>
                </div>
              </div>
              <div class="card same-card">
                <div class="card-body">
                  <h5 class="card-title text-center">재고 관리</h5>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.landing-page {
  height: 100vh;
}

.navigation-bar {
  height: 10vh;
  margin-bottom: 10px;
}

.card-group {
  height: 90vh;
}

.same-card {
  height: calc(90vh / 2 - 8px);
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #ccc;
  margin-top: 4px;
}
</style>
