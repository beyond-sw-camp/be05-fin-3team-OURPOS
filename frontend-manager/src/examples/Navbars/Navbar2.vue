<script>
export default {
  methods: {
    goBack() {
      this.$router.go(-1);  // 이전 페이지로 이동
    }
  }
}
</script>
<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark navigation-bar">
    <div class="mr-auto">
      <img class="POS-name" src ="@/assets/img/logos/ourpos.-removebg-preview.png" />
    </div>
    <div class="ml-auto">
        <button class="btn btn-outline-light" @click="goBack">
          <i class="mdi mdi-export"></i>뒤로가기
        </button>
    </div>
  </nav>
</template>

<style scoped>
.navigation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 12vh;
  margin-bottom: 10px;
  background-color: #F9F0D3;
}
.btn {
  display: block;
  width: 100%;
  height: 12vh;
  margin: 15px;
  font-size: 1.25rem; /* Increased font size */
  text-align: center;
  cursor: pointer;
  background-color: #E5E6D9;
  color: #344767;
  border: none;
  border-radius: 0;
}

</style>