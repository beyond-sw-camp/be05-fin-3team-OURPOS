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

    <div class="container-fluid">
      <div class="row">
        <div class="col-4 mb-4">
          <router-link to="/admin/storeLanding/hallOrderManage">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">홀 주문 관리</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="col-4 mb-4">
          <router-link to="/store-landing/delivery-order-manage">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">배달 주문 관리</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="col-4 mb-4">
          <router-link to="/manager/insight">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">비즈니스 인사이트 확인</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="col-4 mb-4">
          <router-link to="/store-landing/menu-status-manage">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">메뉴 상태 관리</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="col-4 mb-4">
          <router-link to="/storeorder">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">비품/식자재 주문</h5>
              </div>
            </div>
          </router-link>
        </div>
        <div class="col-4 mb-4">
          <router-link to="/storeorder">
            <div class="card same-card">
              <div class="card-body">
                <h5 class="card-title">재고관리</h5>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.POS-name {
  font-size: 3rem; /* Increased font size */
  color: aliceblue;
}

.btn {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 20px; /* Increased padding for height */
  font-size: 1.25rem; /* Increased font size */
  text-align: center;
  cursor: pointer;
}

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

.card {
  height: 100%; /* Make the card fill its container */
}

.card-group {
  height: 90vh;
}

.same-card {
  height: calc((90vh / 2) - 8px);
  border: 1px solid #ccc;
  margin-top: 4px;
  align-items: center;
  
}

.card-title {
  font-size: 5rem; /* Increased font size */
}
</style>
