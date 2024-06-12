<template>
  <v-container>
    <v-row justify="center" align="center" style="height: 100vh;" v-if="Object.keys(userInfo).length !== 0">
      <v-col cols="12" md="6">
        <v-card class="text-center">
          <v-card-title>마이 페이지</v-card-title>
          <v-card-text>
            <v-avatar size="100">
              <img v-if="userInfo.data.profileImage" :src="userInfo.data.profileImage" alt="User Avatar">
              <img v-else src="https://cambridgeleisure.co.uk/sites/cambridge_leisure/files/styles/whats_on_gallery_small/public/images/shops/gallery/five_guys.jpg?itok=YKUdHJuQ" alt="User Avatar">
            </v-avatar>
            <v-divider class="my-4"></v-divider>
            <v-list dense>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>이름</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.name }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>닉네임</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.nickname }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>아이디</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.loginId }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>전화번호</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.phone }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>성별</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.gender }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="mb-4">
                <v-list-item-content>
                  <v-list-item-title>나이</v-list-item-title>
                  <v-list-item-subtitle>{{ userInfo.data.ageRange }}</v-list-item-subtitle>
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
import axios from 'axios';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', 
  withCredentials: true, 
});

export default {
  components: {
    BottomNav
  },
  data() {
    return {
      userInfo: {}
    }
  },
  mounted() {

    // 페이지가 마운트될 때 고객 정보를 요청
    this.fetchCustomerInfo();
    
  },
  methods: {
    fetchCustomerInfo(){
      
      // Axios를 사용하여 백엔드 API에 요청을 보냄
      axiosInstance.get('http://localhost:8080/api/v1/customers/my')
      .then(response => {
        this.userInfo = response.data;
        console.log("Error fetching customer info:",response.data);
      })
      .catch(error => {
        console.error("Error fetching customer info:",error);
      });
    },
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