<template>
    <div class ="landing-page">
      <v-toolbar dark prominent class="navigation-bar">
        <v-toolbar-title>OUR POS</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon>
          <v-icon>mdi-export</v-icon>
        </v-btn>
      </v-toolbar>
  
      <v-container fluid>
        <v-row no-gutters>
          <v-col cols="9" class="card-group">
            <v-row>
              <v-col cols="4">
                <router-link to="/admin/storeLanding/hallOrderManage">
                  <v-card class="mx-auto same-card" title="홀 주문 관리" link></v-card>
                </router-link>
              </v-col> 
              <v-col cols="4">
                <router-link to="/admin/storeLanding/deliveryOrderManage">
                  <v-card class="mx-auto same-card" title="배달 주문 관리" link></v-card>
                </router-link>
              </v-col>
              <v-col cols="4">
                <v-card class="mx-auto same-card" title="비즈니스 인사이트 확인" link></v-card>
              </v-col> 
              <v-col cols="4">
                <router-link to="/admin/storeLanding/menuStatusManage">
                  <v-card class="mx-auto same-card" title="메뉴 상태 관리" link></v-card>
                </router-link>
              </v-col>
              <v-col cols="4">
                <v-card class="mx-auto same-card" title="비품/식자재 주문" link></v-card>
              </v-col> 
              <v-col cols="4">
                <v-card class="mx-auto same-card" title="라이더 위치 확인" link></v-card>
              </v-col> 
            </v-row>
          </v-col>
          
          <v-col cols="3">
            <v-card class="mx-auto double-height-card" title="재고 관리" link></v-card>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </template>
  
  <script setup> 
  
  </script>
  
  <style scoped>
  .landing-page {
    height: 100vh;
  }
  
  .navigation-bar {
    height:10vh;
    margin-bottom: 10px;
  }
  
  .card-group {
    height: 90vh;
  }
  
  .same-card {
    height: calc(90vh/2 - 8px);
    border: 1px solid #ccc;
    margin-top:4px;
    
  }
  
  .double-height-card {
    height: 90vh;
    border:1px solid #ccc;
    margin-top:4px;
  }
  </style>