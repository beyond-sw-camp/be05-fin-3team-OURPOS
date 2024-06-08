<template>
  <v-container fluid>
    <HeaderPage title="메뉴 상세" />
    <v-img
      class="mx-auto my-8"
      max-width="228"
      :src="'http://localhost:8080/images/' + menu.pictureUrl"
    ></v-img>
    <h1>{{ menu.name }}</h1>
    <p>{{ menu.price }}</p>
    <v-form>
      <v-row v-for="menuOptionGroup in menuOptionGroups" :key="menuOptionGroup.id">
        <v-col cols="12">
          <h2>{{ menuOptionGroup.name }}</h2>
          <template v-if="menuOptionGroup.exclusiveYn">
            <v-radio-group v-model="orderOptions[menuOptionGroup.id]">
              <v-radio
                v-for="menuOption in menuOptionGroup.menuOptionResponseDtos"
                :key="menuOption.menuOptionId"
                :label="menuOption.menuOptionName + ' (+' + menuOption.price + '₩)'"
                :value="{
                  'optionName': menuOption.menuOptionName,
                  'price': menuOption.price
                }"
              ></v-radio>
            </v-radio-group>
          </template>
          <template v-else>
            <v-checkbox
              v-model="orderOptions[menuOptionGroup.id]"
              v-for="menuOption in menuOptionGroup.menuOptionResponseDtos"
              :key="menuOption.menuOptionId"
              :label="menuOption.menuOptionName + ' (+' + menuOption.price + '₩)'"
              :value="{
                'optionName': menuOption.menuOptionName,
                'price': menuOption.price
              }"
            ></v-checkbox>
          </template>
        </v-col>
      </v-row>
    </v-form>

    <div class="text-center">
      <v-row>
        <v-col cols="12" md="4" sm="6">
          <v-btn @click="addToCart" rounded="lg" size="x-large" block class="mb-4">{{ totalPrices }}원 장바구니 담기</v-btn>
        </v-col>
      </v-row>

      <v-bottom-sheet v-model="sheet" inset>
        <v-card
          class="text-center"
        >
          <v-card-text>
            <div>
              장바구니에 추가되었습니다.
            </div>
            <v-btn
              variant="text"
              @click="sheet = !sheet"
            >
              close
            </v-btn>
          </v-card-text>
        </v-card>
      </v-bottom-sheet>
    </div>

  </v-container>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';
import {useRoute} from 'vue-router';
import HeaderPage from "@/components/HeaderPage.vue";

const route = useRoute();
const storeId = route.params.storeId;
const menuId = route.params.menuId;

const sheet = ref(false);

const menu = ref({});
const menuOptionGroups = ref([]);
const orderDetailDtos = ref([]);
const orderOptions = ref({});
const totalPrices = ref(0);

const findMenu = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/v1/menus/' + menuId, {
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
    const response = await axios.get('http://localhost:8080/api/v1/categories/' + menu.value.categoryId, {
      withCredentials: true
    });
    menuOptionGroups.value = response.data.data.menuOptionGroupResponseDtos;

    for (let i = 0; i < menuOptionGroups.value.length; i++) {
      const group = menuOptionGroups.value[i];
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

const addToCart = () => {
  const orderOptionGroups = menuOptionGroups.value.map(group => ({
    optionGroupName: group.name,
    orderOptions: Array.isArray(orderOptions.value[group.id])
      ? orderOptions.value[group.id]
      : [orderOptions.value[group.id]]
  }));

  const newOrderDetail = {
    menuId: menu.value.id,
    quantity: 1,
    orderOptionGroups: orderOptionGroups
  };

  let existingOrders = JSON.parse(localStorage.getItem('orderDetailDtos')) || [];

  existingOrders.push(newOrderDetail);
  orderDetailDtos.value = existingOrders;
  localStorage.setItem('orderDetailDtos', JSON.stringify(existingOrders));

  sheet.value = true;
};

findMenu();
</script>

<style scoped>
</style>
