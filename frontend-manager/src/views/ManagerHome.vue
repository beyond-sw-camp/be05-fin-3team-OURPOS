<template>
  <div class="landing-page">
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

    <div class="container-fluid">
      <div class="row no-gutters">
        <div class="col-9 card-group">
          <div class="row">
            <div class="col-4">
              <router-link to="/admin/storeLanding/hallOrderManage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">홀 주문 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-4">
              <router-link to="/store-landing/delivery-order-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">배달 주문 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-4">
              <router-link to="/manager/insight">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">비즈니스 인사이트 확인</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-4">
              <router-link to="/store-landing/menu-status-manage">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">메뉴 상태 관리</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-4">
              <router-link to="/storeorder">
                <div class="card same-card">
                  <div class="card-body">
                    <h5 class="card-title">비품/식자재 주문</h5>
                  </div>
                </div>
              </router-link>
            </div>
            <div class="col-4">
              <div class="card same-card">
                <div class="card-body">
                  <h5 class="card-title">라이더 위치 확인</h5>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-3">
          <div class="card double-height-card">
            <div class="card-body">
              <h5 class="card-title">재고 관리</h5>
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

.card-group {
  height: 90vh;
}

.same-card {
  height: calc(90vh / 2 - 8px);
  border: 1px solid #ccc;
  margin-top: 4px;
}

.double-height-card {
  height: 90vh;
  border: 1px solid #ccc;
  margin-top: 4px;
}
</style>
