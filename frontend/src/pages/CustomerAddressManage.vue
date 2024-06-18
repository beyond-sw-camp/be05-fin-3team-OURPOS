<template>
  <v-container>
    <v-row justify="center" align="center">
      <v-col cols="12" md="8">
        <h1>
          <v-icon @click="goToMypage">mdi-chevron-left</v-icon>
          주소 관리
        </h1>
        <v-card>
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
              </v-list-item>
              <v-divider class="my-4"></v-divider>
              <!-- 서브주소 리스트 -->
              <v-list-item
                v-for="(address, index) in subAddresses"
                :key="address.customerAddressId"
                class="address-item"
              >
                <v-list-item-content>
                  <v-icon>mdi-map-marker-outline</v-icon>
                  <v-list-item-title>서브주소 {{ index + 1 }}</v-list-item-title>
                  <v-list-item-subtitle>{{ address.addressBase }}</v-list-item-subtitle>
                  <v-list-item-subtitle>{{ address.addressDetail }}</v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-btn
                    small
                    outlined
                    color="amber-lighten-4"
                    class="rounded-btn"
                    @click="editSubAddress(address, index)"
                  >
                    수정
                  </v-btn>
                  <v-btn
                    small
                    outlined
                    color="amber-lighten-4"
                    class="rounded-btn"
                    @click="deleteSubAddress(address.customerAddressId)"
                  >
                    삭제
                  </v-btn>
                  <v-btn
                    small
                    outlined
                    color="amber-lighten-4"
                    class="rounded-btn"
                    @click="updateSubAddress(index)"
                  >
                    기본주소로 변경
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>
            <v-row justify="end">
              <v-btn icon @click="addSubAddress">
                <v-icon>mdi-map-plus</v-icon>
              </v-btn>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- 서브주소 수정 다이얼로그 -->
    <v-dialog v-model="dialogSubAddress" max-width="500px">
      <v-card>
        <v-card-title>
          <v-icon>mdi-alert-box-outline</v-icon>
        </v-card-title>
        <v-card-text>
          <v-form ref="subAddressForm" v-model="isSubAddressFormValid">
            <v-text-field
              v-model="tempSubAddress.name"
              :rules="nameRules"
              label="이름"
            />
            <v-text-field
              v-model="tempSubAddress.receiverName"
              label="수령인 이름"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="tempSubAddress.telNo"
              label="전화번호"
              :rules="requiredRules"
            />
            <v-btn color="warning" @click="execDaumPostcode('sub')"
              >우편번호 찾기</v-btn
            ><br />
            <v-text-field
              v-model="tempSubAddress.zipcode"
              label="우편번호"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="tempSubAddress.addressBase"
              label="주소"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="tempSubAddress.addressDetail"
              label="상세주소"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="tempSubAddress.extraAddress"
              label="참고항목"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialogSubAddress = false">취소</v-btn>
          <v-btn :disabled="!isSubAddressFormValid" text @click="saveSubAddress"
            >저장</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 서브주소 추가 다이얼로그 -->
    <v-dialog v-model="dialogAddSubAddress" max-width="500px">
      <v-card>
        <v-card-title>서브주소 추가</v-card-title>
        <v-card-text>
          <v-form ref="addSubAddressForm" v-model="isAddSubAddressFormValid">
            <v-text-field
              v-model="newSubAddress.name"
              label="이름"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="newSubAddress.receiverName"
              label="수령인 이름"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="newSubAddress.telNo"
              label="전화번호"
              :rules="requiredRules"
            />
            <v-btn color="warning" @click="execDaumPostcode('new')"
              >우편번호 찾기</v-btn
            ><br />
            <v-text-field
              v-model="newSubAddress.zipcode"
              label="우편번호"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="newSubAddress.addressBase"
              label="주소"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="newSubAddress.addressDetail"
              label="상세주소"
              :rules="requiredRules"
            />
            <v-text-field
              v-model="newSubAddress.extraAddress"
              label="참고항목"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialogAddSubAddress = false">취소</v-btn>
          <v-btn
            :disabled="!isAddSubAddressFormValid"
            text
            @click="saveNewSubAddress"
            >추가</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar v-model="snackbar" :timeout="3000">
      {{ snackbarMessage }}
      <template v-slot:action="{ attrs }">
        <v-btn color="pink" text v-bind="attrs" @click="snackbar = false">
          닫기
        </v-btn>
      </template>
    </v-snackbar>
  </v-container>
  <BottomNav />
</template>

<script>
import BottomNav from "@/components/BottomNav.vue";
import axios from "axios";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // 쿠키를 전송하기 위해 필요
});

export default {
  components: {
    BottomNav,
  },
  data() {
    return {
      mainAddress: {
        customerAddressId: null,
        addressBase: "",
        addressDetail: "",
        postcode: "",
        extraAddress: "",
      },
      subAddresses: [],
      dialogMainAddress: false,
      dialogSubAddress: false,
      dialogAddSubAddress: false,
      tempMainAddress: {
        customerAddressId: null,
        addressBase: "",
        addressDetail: "",
        postcode: "",
        extraAddress: "",
      },
      tempSubAddress: {
        customerAddressId: null,
        receiverName: "",
        telNo: "",
        addressBase: "",
        addressDetail: "",
        zipcode: "",
        extraAddress: "",
      },
      newSubAddress: {
        name: "",
        receiverName: "",
        telNo: "",
        addressBase: "",
        addressDetail: "",
        zipcode: "",
        extraAddress: "",
      },
      editIndex: -1,
      snackbar: false,
      snackbarMessage: "",
      isSubAddressFormValid: false,
      isAddSubAddressFormValid: false,
      requiredRules: [(value) => !!value || "This field is required"],
    };
  },
  async mounted() {
    // Daum Postcode API 스크립트 동적 로드
    const script = document.createElement("script");
    script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.onload = () => {
      console.log("Daum Postcode script loaded");
    };
    document.head.appendChild(script);

    // 로그인한 사용자의 주소 데이터 로드
    await this.loadAddresses();
  },
  methods: {
    async loadAddresses() {
      try {
        const response = await axiosInstance.get(
          "http://localhost:8080/api/v1/customers/my/addresses"
        );
        const addresses = response.data.data;

        // 응답 데이터가 배열인지 확인
        if (Array.isArray(addresses)) {
          const mainAddress = addresses.find((address) => address.defaultYn);
          this.mainAddress = mainAddress
            ? mainAddress
            : {
                addressBase: "",
                addressDetail: "",
                postcode: "",
                extraAddress: "",
              };

          this.subAddresses = addresses.filter((address) => !address.defaultYn);
        } else {
          console.error("Addresses data is not an array:", addresses);
        }
      } catch (error) {
        console.error("Error loading addresses:", error);
      }
    },
    editSubAddress(address, index) {
      this.tempSubAddress = { ...address };
      this.editIndex = index;
      this.dialogSubAddress = true;
    },
    async updateSubAddress(index) {
      try {
        const addressId = this.subAddresses[index].customerAddressId;

        // 서브 주소를 기본 주소로 변경
        await axiosInstance.put(
          `http://localhost:8080/api/v1/customers/addresses/${addressId}/default`
        );

        // 기존의 기본주소를 서브주소 리스트에 추가
        if (this.mainAddress.customerAddressId) {
          this.subAddresses.push({ ...this.mainAddress, defaultYn: false });
        }

        // 새로운 기본주소 설정
        const newMainAddress = this.subAddresses[index];
        this.mainAddress = { ...newMainAddress, defaultYn: true };

        // 서브주소 리스트에서 기본주소로 변경된 주소를 제거
        this.subAddresses.splice(index, 1);

        localStorage.removeItem("stores");

        let deliveryOrder = localStorage.getItem("deliveryOrder");
        if (deliveryOrder !== null) {
          deliveryOrder = JSON.parse(deliveryOrder);
          deliveryOrder.orderAddressRequestDto = this.mainAddress;
          localStorage.setItem("deliveryOrder", JSON.stringify(deliveryOrder));
        }
      } catch (error) {
        console.error("Error updating sub address:", error);
      }
    },
    async saveSubAddress() {
      try {
        const addressId = this.subAddresses[this.editIndex].customerAddressId;
        await axiosInstance.put(
          `http://localhost:8080/api/v1/customers/addresses/${addressId}`,
          this.tempSubAddress
        );
        this.subAddresses[this.editIndex] = { ...this.tempSubAddress };
        this.dialogSubAddress = false;
      } catch (error) {
        console.error("Error saving sub address:", error);
      }
    },
    async deleteSubAddress(addressId) {
      try {
        await axiosInstance.delete(
          `http://localhost:8080/api/v1/customers/addresses/${addressId}`
        );
        this.subAddresses = this.subAddresses.filter(
          (address) => address.customerAddressId !== addressId
        );
      } catch (error) {
        console.error("Error deleting sub address:", error);
      }
    },
    addSubAddress() {
      if (this.subAddresses.length > 2) {
        this.snackbarMessage = "서브주소는 최대 3개까지 등록 가능합니다.";
        this.snackbar = true;
      } else {
        this.newSubAddress = {
          name: "",
          receiverName: "",
          telNo: "",
          addressBase: "",
          addressDetail: "",
          zipcode: "",
          extraAddress: "",
        };
        this.dialogAddSubAddress = true;
      }
    },
    async saveNewSubAddress() {
      if (this.subAddresses.length >= 3) {
        this.snackbarMessage = "서브주소는 최대 3개까지 추가할 수 있습니다.";
        this.snackbar = true;
        this.dialogAddSubAdddress = false; // 다이얼로그 닫기
        return;
      }
      try {
        if (this.newSubAddress.addressBase) {
          await axiosInstance.post(
            "http://localhost:8080/api/v1/customers/addresses",
            this.newSubAddress
          );
          await this.loadAddresses();
          this.dialogAddSubAddress = false;
        }
      } catch (error) {
        console.error("Error saving new sub address:", error);
      }
    },
    execDaumPostcode(type) {
      new daum.Postcode({
        oncomplete: (data) => {
          let addr = "";
          let extraAddr = "";

          if (data.userSelectedType === "R") {
            addr = data.roadAddress;
          } else {
            addr = data.jibunAddress;
          }

          if (data.userSelectedType === "R") {
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              extraAddr += data.bname;
            }
            if (data.buildingName !== "" && data.apartment === "Y") {
              extraAddr +=
                extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
            }
            if (extraAddr !== "") {
              extraAddr = " (" + extraAddr + ")";
            }
          }

          if (type === "sub") {
            this.tempSubAddress.addressBase = addr;
            this.tempSubAddress.zipcode = data.zonecode;
            this.tempSubAddress.extraAddress = extraAddr;
          } else if (type === "new") {
            this.newSubAddress.addressBase = addr;
            this.newSubAddress.zipcode = data.zonecode;
            this.newSubAddress.extraAddress = extraAddr;
          }
        },
      }).open();
    },
    goToMypage() {
      // mypage로 이동하는 코드 추가
      this.$router.push("/mypage");
    },
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
  font-color: #fff;
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
