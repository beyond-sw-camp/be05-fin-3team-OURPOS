<template>
  <v-container fluid>
    <AppDeliveryHeader :title="title" />

    <v-row class="justify-center" v-show="!isNaN(menu.price)">
      <v-col cols="12" md="6" class="text-center">
        <v-img
          class="mx-auto my-4"
          :src="'https://api.ourpos.org/images/' + menu.pictureUrl"
          max-height="228"
        ></v-img>
        <h1 class="mb-2">{{ menu.name }}</h1>
        <h2 class="mb-4">{{ Number(menu.price).toLocaleString() }}원</h2>
      </v-col>
      <v-btn variant="plain" @click="decrementQuantity" :disabled="quantity === 1" class="mr-2">
        <h2>-</h2>
      </v-btn>
      <h2>{{ quantity }}</h2>
      <v-btn variant="plain" @click="incrementQuantity" class="ml-2">
        <h2>+</h2>
      </v-btn>
    </v-row>

    <v-form>
      <v-row v-for="menuOptionGroup in menuOptionGroups" :key="menuOptionGroup.id" class="my-4">
        <v-col cols="12" md="8" class="mx-auto">
          <h2 class="my-2">{{ menuOptionGroup.name }}</h2>
          <template v-if="menuOptionGroup.exclusiveYn">
            <v-radio-group v-model="orderOptions[menuOptionGroup.id]">
              <v-radio
                v-for="menuOption in menuOptionGroup.menuOptionResponseDtos"
                :key="menuOption.menuOptionId"
                :label="menuOption.menuOptionName + ' (+' + menuOption.price + '원)'"
                :value="{
                  optionName: menuOption.menuOptionName,
                  price: menuOption.price
                }"
                class="mb-2"
              ></v-radio>
            </v-radio-group>
          </template>
          <template v-else>
            <v-checkbox-btn
              v-model="orderOptions[menuOptionGroup.id]"
              v-for="menuOption in menuOptionGroup.menuOptionResponseDtos"
              :key="menuOption.menuOptionId"
              :label="menuOption.menuOptionName + ' (+' + menuOption.price + '원)'"
              :value="{
                optionName: menuOption.menuOptionName,
                price: menuOption.price
              }"
              class="mb-2"
            ></v-checkbox-btn>
          </template>
        </v-col>
      </v-row>
    </v-form>

    <v-bottom-sheet v-model="sheet" inset>
      <v-card class="text-center py-4">
        <v-card-text>
          <v-icon color="primary" size="48">mdi-check-circle</v-icon>
          <div>장바구니에 추가되었습니다.</div>
          <v-btn variant="text" @click="navigateToCart">장바구니로 이동</v-btn>
          <v-btn variant="text" @click="navigateToMenus">다른 메뉴 더보기</v-btn>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>

<!--    <v-bottom-sheet v-model="errorSheet" inset>-->
<!--      <v-card class="text-center py-4 red lighten-4">-->
<!--        <v-card-text class="red&#45;&#45;text">-->
<!--          <v-icon color="red" large>mdi-alert-circle</v-icon>-->
<!--          <div class="my-2">-->
<!--            장바구니에는 같은 지점의 메뉴만 담을 수 있습니다.-->
<!--          </div>-->
<!--          <div class="text-medium-emphasis text-caption my-2">-->
<!--            장바구니로 이동하여 기존 메뉴를 삭제하시겠습니까?-->
<!--          </div>-->
<!--          <v-btn variant="text" @click="navigateToCart">-->
<!--            장바구니 이동-->
<!--          </v-btn>-->
<!--          <v-btn variant="text" @click="errorSheet = !errorSheet">-->
<!--            닫기-->
<!--          </v-btn>-->
<!--        </v-card-text>-->
<!--      </v-card>-->
<!--    </v-bottom-sheet>-->

    <!-- 하단 고정 떠다니는 버튼 -->
    <v-btn
      fab
      dark
      bottom
      right
      color="primary"
      @click="addToCart"
      class="floating-btn"
    >
      {{ Number(totalPrices).toLocaleString() }}원 장바구니 담기
    </v-btn>
  </v-container>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import AppDeliveryHeader from "@/components/AppDeliveryHeader.vue";

const title = ref('메뉴 상세');
const route = useRoute();
const router = useRouter();
const storeId = route.params.storeId;
const menuId = route.params.menuId;

const sheet = ref(false);
const errorSheet = ref(false);
const menu = ref({});
const menuOptionGroups = ref([]);
const orderDetailDtos = ref([]);
const orderOptions = ref({});
const totalPrices = ref(0);
const quantity = ref(1);
const optionTotalPrice = ref(0);

const findMenu = async () => {
  try {
    const response = await axios.get(`https://api.ourpos.org/api/v1/menus/${menuId}`, {
      withCredentials: true
    });
    menu.value = response.data.data;
    totalPrices.value = menu.value.price;
    await findMenuOptionGroups();
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const findMenuOptionGroups = async () => {
  try {
    const response = await axios.get(`https://api.ourpos.org/api/v1/categories/${menu.value.categoryId}`, {
      withCredentials: true
    });
    menuOptionGroups.value = response.data.data.menuOptionGroupResponseDtos;

    for (const group of menuOptionGroups.value) {
      if (group.exclusiveYn && group.menuOptionResponseDtos.length > 0) {
        orderOptions.value[group.id] = {
          optionName: group.menuOptionResponseDtos[0].menuOptionName,
          price: group.menuOptionResponseDtos[0].price
        };
      } else {
        orderOptions.value[group.id] = [];
      }
    }
  } catch (error) {
    console.error("Error fetching items:", error);
  }
};

const calculateTotalPrice = () => {
  let optionTotal = 0;
  for (const groupId in orderOptions.value) {
    const options = orderOptions.value[groupId];
    if (Array.isArray(options)) {
      optionTotal += options.reduce((sum, option) => sum + option.price, 0);
    } else {
      optionTotal += options.price;
    }
  }
  totalPrices.value = (menu.value.price + optionTotal) * quantity.value;
  optionTotalPrice.value = optionTotal;
};

watch(orderOptions, calculateTotalPrice, { deep: true });
watch(quantity, calculateTotalPrice);

const incrementQuantity = () => {
  quantity.value++;
};

const decrementQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

const addToCart = () => {
  const orderOptionGroups = menuOptionGroups.value.map(group => ({
    optionGroupName: group.name,
    orderOptions: Array.isArray(orderOptions.value[group.id])
      ? orderOptions.value[group.id]
      : [orderOptions.value[group.id]]
  }));

  const newOrderDetail = {
    menuId: menu.value.id,
    menuName: menu.value.name,
    menuPictureUrl: menu.value.pictureUrl,
    quantity: quantity.value,
    totalPrice: totalPrices.value,
    menuPrice: menu.value.price + optionTotalPrice.value,
    orderOptionGroups
  };

  const existingDeliveryOrder = JSON.parse(localStorage.getItem('deliveryOrder'));

  const existingOrders = existingDeliveryOrder?.orderDetailDtos || [];
  existingOrders.push(newOrderDetail);
  orderDetailDtos.value = existingOrders;

  const deliveryOrder = {
    storeId: storeId,
    storeName: existingDeliveryOrder ? existingDeliveryOrder.storeName : menu.value.storeName,
    orderDetailDtos: existingOrders,
    minOrderAmount: existingDeliveryOrder ? existingDeliveryOrder.minOrderAmount : menu.value.storeMinOrderAmount,
    disposalYn: existingDeliveryOrder ? existingDeliveryOrder.disposalYn : false,
    ownerMessage: existingDeliveryOrder ? existingDeliveryOrder.ownerMessage : '',
    riderMessage: existingDeliveryOrder ? existingDeliveryOrder.riderMessage : '',
    orderAddressRequestDto: existingDeliveryOrder ? existingDeliveryOrder.orderAddressRequestDto : {}
  };

  localStorage.setItem('deliveryOrder', JSON.stringify(deliveryOrder));

  sheet.value = true;
};

const navigateToCart = () => {
  errorSheet.value = false;
  router.push('/cart/delivery')
};

const navigateToMenus = () => {
  errorSheet.value = false;
  router.push(`/stores/${storeId}/delivery/menus`);
};

findMenu();
</script>

<style scoped>
.floating-btn {
  position: fixed;
  right: 16px;
  bottom: 16px;
  z-index: 1000;
}
</style>
