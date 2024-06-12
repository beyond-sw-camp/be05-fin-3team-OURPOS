<template>
  <div class="landing-page">
    <v-toolbar dark prominent class="navigation-bar">
      <v-toolbar-title>OUR POS</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>mdi-export</v-icon>
      </v-btn>
    </v-toolbar>

    <v-container fluid>
      <v-row no-gutters>
        <v-col cols="12" class="card-group">
          <v-row>
            <v-col cols="3.5">
              <router-link to="/super-admin/headOfficeLanding/menuManage">
                <v-card class="same-card" title="메뉴/카테고리 관리" link></v-card>
              </router-link>
              <router-link to="/super-admin/headOfficeLanding/menuOptionGroupManage">
                <v-card class="same-card" title="메뉴옵션그룹/메뉴옵션 관리 관리" link></v-card>
              </router-link>
            </v-col>                                   <v-col cols="3.5">
              <v-card class="same-card" title="비품/식자재 관리" link></v-card>
              <v-card class="same-card" title="비품/식자재 주문 관리" link></v-card>
            </v-col>
            <v-col cols="3.5">
              <v-card class="same-card" title="비즈니스 인사이트 관리" link></v-card>
              <v-card class="same-card" title="재고 관리" link></v-card>
            </v-col>
          </v-row>
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
  height: 10vh;
  margin-bottom: 10px;
}

.card-group {
  height: 90vh;
}

.menu-card {
  height: 90vh;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #ccc;
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
