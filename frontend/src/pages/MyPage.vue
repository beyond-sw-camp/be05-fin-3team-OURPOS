<template>
    <div>
      <h1>MyPage</h1>
      <UserInfo />
     
    </div>
  </template>
  
  <script>
  import UserInfo from '../components/UserInfo.vue';
  
  export default {
    name: 'MyPage',
    components: {
      UserInfo,
        
    }
  }
  </script>
  
  <style scoped>
  h1 {
    color: #42b983;
  }
  </style>