<template>
  <v-app>
    <v-main>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-card>
              <v-card-title>메인 페이지</v-card-title>
              <v-card-text>
                <p>메인 페이지입니다.</p>
                <v-btn @click="hallOrder">홀/포장 주문</v-btn>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

const hallOrder = () => {
  router.push('/stores');
};

// 쿠키에 Authorization이 있는지 확인

</script>

<style scoped>

</style>
