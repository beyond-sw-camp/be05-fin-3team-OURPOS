<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col>
          <v-btn @click="execDaumPostcode">주소 검색</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field v-model="form.addressSi" label="시/도" readonly></v-text-field>
        </v-col>
        <v-col>
          <v-text-field v-model="form.addressGu" label="구/군" readonly></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field v-model="form.streetName" label="도로명" readonly></v-text-field>
        </v-col>
        <v-col>
          <v-text-field v-model="form.addressDetail" label="상세 주소"></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-btn @click="addAddress">주소 추가</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-alert v-if="alertMessage" :type="alertType">{{ alertMessage }}</v-alert>
        </v-col>
      </v-row>
      <div id="guide" style="display:none"></div>
    </v-container>
  </v-app>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const form = ref({
  addressSi: '',
  addressGu: '',
  streetName: '',
  addressDetail: ''
})

const alertMessage = ref('')
const alertType = ref('')

const execDaumPostcode = () => {
  new daum.Postcode({
    oncomplete: function (data) {
      const roadAddr = data.roadAddress
      let extraRoadAddr = ''

      if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraRoadAddr += data.bname
      }
      if (data.buildingName !== '' && data.apartment === 'Y') {
        extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName)
      }
      if (extraRoadAddr !== '') {
        extraRoadAddr = ' (' + extraRoadAddr + ')'
      }

      form.value.addressSi = data.sido
      form.value.addressGu = data.sigungu
      form.value.streetName = roadAddr + extraRoadAddr

      const guideTextBox = document.getElementById('guide')
      if (data.autoRoadAddress) {
        const expRoadAddr = data.autoRoadAddress + extraRoadAddr
        guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')'
        guideTextBox.style.display = 'block'
      } else if (data.autoJibunAddress) {
        const expJibunAddr = data.autoJibunAddress
        guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')'
        guideTextBox.style.display = 'block'
      } else {
        guideTextBox.innerHTML = ''
        guideTextBox.style.display = 'none'
      }
    }
  }).open()
}

const addAddress = () => {
  axios.post('http://localhost:8080/api/v1/customers/address', {
    addressSi: form.value.addressSi,
    addressGu: form.value.addressGu,
    streetName: form.value.streetName,
    addressDetail: form.value.addressDetail
  }, { withCredentials: true })
    .then(response => {
      if (response.data.code === 200) {
        alertType.value = 'success';
        alertMessage.value = '주소가 추가되었습니다.'
      } else if (response.data.code === 400) {
        alertType.value = 'warning'
        alertMessage.value = '주소는 3개 까지만 추가 가능합니다.'
      } else {
        alertType.value = 'error'
        alertMessage.value = '주소 추가에 실패했습니다.'
      }
    })
    .catch(error => {
      console.error(error)
      alertType.value = 'error'
      alertMessage.value = '서버와의 통신에 실패했습니다.'
    })
}
</script>

<style scoped>
#guide {
  margin-top: 10px;
  color: gray;
}
</style>
