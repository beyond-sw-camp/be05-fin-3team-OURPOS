<script>
export default {
  methods: {
    goBack() {
      this.$router.push('/manager');  // Navigate to the previous page
    },
    logout() {
      localStorage.removeItem('token');  // Remove the token
      this.$router.push('/sign-up');  // Navigate to the login page
    }
  }
}
</script>

<template>
  <nav class="navigation-bar">
    <div class="mr-auto">
      <img class="POS-name" src ="@/assets/img/logos/ourpos.-removebg-preview.png" />
    </div>
    <div class="ml-auto button-container">
      <button class="btn" @click="goBack">
        <i class="mdi mdi-arrow-left"></i>뒤로가기
      </button>
      <button class="btn" @click="logout">
        <i class="mdi mdi-logout"></i>로그아웃
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

.button-container {
  display: flex;
  gap: 10px;
}

.btn {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  font-size: 1.25rem;
  cursor: pointer;
  background-color: #344767;
  color: #E5E6D9;
  border: 1px solid #E5E6D9;
  border-radius: 10px;
  transition: background-color 0.3s, color 0.3s;
}

.btn i {
  margin-right: 5px;
}

.btn:hover {
  background-color: #E5E6D9;
  color: #344767;
}
</style>
