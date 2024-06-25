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
  <nav class="navigation-bar">
    <div class="mr-auto">
      <img class="POS-name" src ="@/assets/img/logos/ourpos.-removebg-preview.png" />
    </div>
    <div class="ml-auto">
      <button class="btn" @click="goBack">
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
  padding: 0 20px;
  margin-bottom: 10px;
  background-color: #344767;
}

.POS-name {
  height: 12vh;
}

.btn {
  display: block;
  width: auto;
  padding: 10px 20px;
  font-size: 1.25rem;
  text-align: center;
  cursor: pointer;
  background-color: #344767;
  color: #E5E6D9;
  border: 1px solid #E5E6D9;
  border-radius: 10px;
}

.btn i {
  margin-right: 5px;
}
</style>