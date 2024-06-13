<template>
    <div class="userInfo">
      <h2>User Information</h2>
      <!--
      <p>Name: {{ user.name }}</p>
      <p>Email: {{ user.email }}</p>
      -->
      <img src="@/img/사용자.png" alt="John">
      <p><strong>Name:</strong> 이윤경</p>
      <p><strong>Id:</strong> yun072</p>
      <p><strong>phone:</strong> 010-6722-6591</p>
  
      <div class="under-navbar">
          <v-layout class="overflow-visible" style="height: 56px;">
      <v-bottom-navigation
        v-model="value"
        color="primary"
        active
      >
        <v-btn>
          <v-icon>mdi-heart</v-icon>
  
          Home
        </v-btn>
  
        <v-btn>
          <v-icon>mdi-heart</v-icon>
  
          Menu
        </v-btn>
  
        <v-btn>
          <v-icon>mdi-heart</v-icon>
  
          <span>Order</span>
        </v-btn>
        
        <v-btn>
          <v-icon>mdi-heart</v-icon>
  
          <span>Store</span>
        </v-btn>
        
        <v-btn>
          <v-icon>mdi-heart</v-icon>
  
          <span>Other</span>
        </v-btn>
      </v-bottom-navigation>
    </v-layout>
      </div>
  
    </div>
    
    
    
  </template>
  
  <script>
  import UserInfo from '@/components/UserInfo.vue'
  export default {
      name: 'UserInfo',
      data: () => ({ value: 1 })
  
  }
  </script>
  
  <style scoped>
  .userInfo {
      margin-top: 20px;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
      background-color: #f9f9f9;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      position: relative;
      text-align: center;
  }
  
  .infoContainer {
      display: flex;
      align-items: center;
  }
  
  .profile-picture {
      width: 100px; /* 이미지 크기 조정 */
      height: 100px; /* 이미지 크기 조정 */
      border-radius: 50%; /* 원형 이미지로 만들기 위해 추가 */
      margin-right: 20px; /* 이미지와 텍스트 사이에 간격 추가 */
  }
  
  .textInfo {
      display: flex;
      flex-direction: column;
  }
  
  .textInfo p {
      margin: 5px 0; /* 텍스트 정보 사이의 간격 조정 */
  }
  .under-navbar {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background-color: white; /* Ensure the background color covers content behind */
    box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1); /* Optional: shadow for better separation */
  }
  </style>