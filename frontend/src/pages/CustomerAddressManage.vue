<template>
  <v-container>
    <v-row justify="center" align="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title>주소 관리</v-card-title>
          <v-card-text>
            <v-list dense>
              <!-- 기본주소 -->
              <v-list-item class="address-item">
                <v-list-item-content>
                  <v-icon>mdi-map-marker</v-icon>
                  <v-list-item-title>기본주소</v-list-item-title>
                  <v-list-item-subtitle>{{ mainAddress.addressBase }}</v-list-item-subtitle>
                  <v-list-item-subtitle>{{ mainAddress.addressDetail }}</v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-btn icon @click="editMainAddress">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
              <v-divider class="my-4"></v-divider>
              <!-- 서브주소 리스트 -->
              <v-list-item v-for="(address, index) in subAddresses" :key="address.customerAddressId" class="address-item">
                <v-list-item-content>
                  <v-icon>mdi-map-marker</v-icon>
                  <v-list-item-title>서브주소 {{ index + 1 }}</v-list-item-title>
                  <v-list-item-subtitle>{{ address.addressBase }}</v-list-item-subtitle>
                  <v-list-item-subtitle>{{ address.addressDetail }}</v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-btn small outlined color="orange-lighten-5" class="rounded-btn" @click="editSubAddress(address, index)">
                    수정
                  </v-btn>
                  <v-btn small outlined color="orange-lighten-5" class="rounded-btn ml-2" @click="deleteSubAddress(address.id)">
                    삭제
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>
            <v-row justify="end">
              <v-btn icon @click="addSubAddress">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- 기본주소 수정 다이얼로그 -->
    <v-dialog v-model="dialogMainAddress" max-width="500px">
      <v-card>
        <v-card-title>기본주소 수정</v-card-title>
        <v-card-text>
          <v-text-field v-model="tempMainAddress" label="기본주소" />
          <input type="text" id="main_postcode" placeholder="우편번호">
          <v-btn @click="execDaumPostcode('main')">우편번호 찾기</v-btn><br>
          <input type="text" id="main_address" placeholder="주소">
          <input type="text" id="main_detailAddress" placeholder="상세주소">
          <input type="text" id="main_extraAddress" placeholder="참고항목">
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialogMainAddress = false">취소</v-btn>
          <v-btn text @click="saveMainAddress">저장</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 서브주소 수정 다이얼로그 -->
    <v-dialog v-model="dialogSubAddress" max-width="500px">
      <v-card>
        <v-card-title>서브주소 수정</v-card-title>
        <v-card-text>
          <v-text-field v-model="tempSubAddress" label="서브주소" />
          <input type="text" id="sub_postcode" placeholder="우편번호">
          <v-btn @click="execDaumPostcode('sub')">우편번호 찾기</v-btn><br>
          <input type="text" id="sub_address" placeholder="주소">
          <input type="text" id="sub_detailAddress" placeholder="상세주소">
          <input type="text" id="sub_extraAddress" placeholder="참고항목">
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialogSubAddress = false">취소</v-btn>
          <v-btn text @click="saveSubAddress">저장</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 서브주소 추가 다이얼로그 -->
    <v-dialog v-model="dialogAddSubAddress" max-width="500px">
      <v-card>
        <v-card-title>서브주소 추가</v-card-title>
        <v-card-text>
          <v-text-field v-model="newSubAddress" label="서브주소" />
          <input type="text" id="new_postcode" placeholder="우편번호">
          <v-btn @click="execDaumPostcode('new')">우편번호 찾기</v-btn><br>
          <input type="text" id="new_address" placeholder="주소">
          <input type="text" id="new_detailAddress" placeholder="상세주소">
          <input type="text" id="new_extraAddress" placeholder="참고항목">
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialogAddSubAddress = false">취소</v-btn>
          <v-btn text @click="saveNewSubAddress">추가</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
  <BottomNav/>
</template>

<script>
import BottomNav from "@/components/BottomNav.vue";
import axios from 'axios';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', 
  withCredentials: true, // 쿠키를 전송하기 위해 필요
});

export default {
  components: {
    BottomNav
  },
  data() {
    return {
      mainAddress: '',
      subAddresses: [],
      dialogMainAddress: false,
      dialogSubAddress: false,
      dialogAddSubAddress: false,
      tempMainAddress: '',
      tempSubAddress: '',
      newSubAddress: '',
      editIndex: -1,
    };
  },
  async mounted() {
    // Daum Postcode API 스크립트 동적 로드
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.onload = () => {
      console.log('Daum Postcode script loaded');
    };
    document.head.appendChild(script);

    // 로그인한 사용자의 주소 데이터 로드
    await this.loadAddresses();
  },
  methods: {
    async loadAddresses() {
      try {
        const response = await axiosInstance.get('http://localhost:8080/api/v1/customers/my/addresses');
        const addresses = response.data.data;

        // 응답 데이터가 배열인지 확인
        if (Array.isArray(addresses)) {
          const mainAddress = addresses.find(address => address.defaultYn);
          this.mainAddress = mainAddress ? mainAddress : '';

          this.subAddresses = addresses.filter(address => !address.defaultYn);
          console.log('Addresses loaded:', this.mainAddress, this.subAddresses);
        } else {
          console.error('Addresses data is not an array:', addresses);
        }
      } catch (error) {
        console.error('Error loading addresses:', error);
      }
    },
    editMainAddress() {
      this.tempMainAddress = this.mainAddress;
      this.dialogMainAddress = true;
    },
    async saveMainAddress() {
      try {
        const mainAddressId = this.subAddresses.find(address => address.addressBase === this.tempMainAddress)?.id;
        if (mainAddressId) {
          await axiosInstance.put(`http://localhost:8080/api/v1/customers/addresses/${mainAddressId}/default`);
          this.mainAddress = this.tempMainAddress;
          this.dialogMainAddress = false;
          await this.loadAddresses();
        } else {
          console.error('Main address ID not found');
        }
      } catch (error) {
        console.error('Error saving main address:', error);
      }
    },
    editSubAddress(address, index) {
      this.editIndex = index;
      this.tempSubAddress = address.addressBase;
      this.dialogSubAddress = true;
    },
    async saveSubAddress() {
      try {
        const addressId = this.subAddresses[this.editIndex].id;
        await axiosInstance.put(`http://localhost:8080/api/v1/customers/addresses/${addressId}`, {
          addressBase: this.tempSubAddress
        });
        this.subAddresses[this.editIndex].addressBase = this.tempSubAddress;
        this.dialogSubAddress = false;
      } catch (error) {
        console.error('Error saving sub address:', error);
      }
    },
    async deleteSubAddress(addressId) {
      try {
        await axiosInstance.delete(`http://localhost:8080/api/v1/customers/addresses/${addressId}`);
        this.subAddresses = this.subAddresses.filter(address => address.id !== addressId);
      } catch (error) {
        console.error('Error deleting sub address:', error);
      }
    },
    addSubAddress() {
      this.newSubAddress = '';
      this.dialogAddSubAddress = true;
    },
    async saveNewSubAddress() {
      try {
        if (this.newSubAddress) {
          await axiosInstance.post('http://localhost:8080/api/v1/customers/addresses', {
            addressBase: this.newSubAddress
          });
          await this.loadAddresses();
          this.dialogAddSubAddress = false;
        }
      } catch (error) {
        console.error('Error saving new sub address:', error);
      }
    },
    execDaumPostcode(type) {
      new daum.Postcode({
        oncomplete: (data) => {
          let addr = '';
          let extraAddr = '';

          if (data.userSelectedType === 'R') {
            addr = data.roadAddress;
          } else {
            addr = data.jibunAddress;
          }

          if(data.userSelectedType === 'R'){
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
              extraAddr += data.bname;
            }
            if(data.buildingName !== '' && data.apartment === 'Y'){
              extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if(extraAddr !== ''){
              extraAddr = ' (' + extraAddr + ')';
            }
            document.getElementById(`${type}_extraAddress`).value = extraAddr;
          } else {
            document.getElementById(`${type}_extraAddress`).value = '';
          }

          document.getElementById(`${type}_postcode`).value = data.zonecode;
          document.getElementById(`${type}_address`).value = addr;
          document.getElementById(`${type}_detailAddress`).focus();
        }
      }).open();
    }
  },
};
</script>

<style scoped>
.v-avatar img {
  object-fit: cover;
}
.mb-4 {
  margin-bottom: 1rem;
}
.rounded-btn {
  border-radius: 40px;
  font-size: 0.6rem;
  padding: 1px 5px;
  min-width: 40px;
  height: 24px;
}
.ml-2 {
  margin-left: 8px;
}
.address-item {
  margin-bottom: 8px;
}
</style>