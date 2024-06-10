<template>
  <v-container>
    <v-row justify="center" align="center" style="height: 100vh;">
      <v-col cols="12" md="6">
        <v-card class="text-center">
          <v-card-title>마이 페이지</v-card-title>
          <v-card-text>
            <v-avatar size="100">
              <img src="https://cambridgeleisure.co.uk/sites/cambridge_leisure/files/styles/whats_on_gallery_small/public/images/shops/gallery/five_guys.jpg?itok=YKUdHJuQ" alt="User Avatar">
            </v-avatar>
            <v-divider class="my-4"></v-divider>
            <v-list dense>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>이름</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.name }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>아이디</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.username }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>전화번호</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.phoneNumber }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>성별</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.gender }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content class="text-center">
                  <v-list-item-title>기본주소</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.address }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content class="text-center">
                  <v-btn color="transparent" dark @click="changeAddress">
                    <v-icon left>mdi-map-marker</v-icon>
                    주소 관리
                  </v-btn>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content class="text-center">
                  <v-btn color="red-accent-2" dark @click="manageOrder">
                    <v-icon left>mdi-cart</v-icon>
                    주문 조회
                  </v-btn>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <BottomNav/>
</template>

<script>
import BottomNav from "@/components/BottomNav.vue";
export default {
  components: {
    BottomNav
  },
  data() {
    return {
      userInfo: {
        name: '사용자 이름',
        username: '사용자 아이디',
        phoneNumber: '010-1234-5678',
        address: '서울시 강남구',
        gender: '남성'
      }
    }
  },
  methods: {
    changeAddress() {
      this.$router.push('/change-address');
    },
    manageOrder() {
      this.$router.push('/order-check');
    }
  }
}
</script>

<style scoped>
.v-avatar img {
  object-fit: cover;
}
</style>