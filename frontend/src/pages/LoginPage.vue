<template>
  <div>
    <v-img
      class="mx-auto my-8 pa-12"
      max-width="228"
      src="https://cdn.vuetifyjs.com/docs/images/logos/vuetify-logo-v3-slim-text-light.svg"
    ></v-img>

    <v-card-text
      class="mx-auto pl-12 pr-12"
      max-width="400"
      rounded="lg"
    >

      <v-card
        class="mb-12"
        color="surface-variant"
        variant="tonal"
      >
        <v-card-text class="text-medium-emphasis text-caption">
          본 페이지는 실제 서비스가 아닙니다. 프로젝트를 위해 만들어진 테스트 페이지 입니다! 로그인을 통해 서비스를 이용해 보실 수 있습니다.
        </v-card-text>
      </v-card>

      <v-img
        class="mx-auto my-6"
        max-width="228"
        src="../img/login_kakao.png"

        @click="onKakaoLogin">
      </v-img>

      <v-img
        class="mx-auto my-6"
        max-width="228"
        src="../img/login_naver.png"
        @click="onNaverLogin">
      </v-img>

    </v-card-text>
  </div>
</template>

<script setup>

const onNaverLogin = () => {
  console.log('onNaverLogin')
  window.location.href = 'http://localhost:8080/oauth2/authorization/naver'
}

const onKakaoLogin = () => {
  console.log('onKakaoLogin')
  window.location.href = 'http://localhost:8080/oauth2/authorization/kakao'
}
</script>

<style scoped>

</style>
