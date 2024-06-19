<template>
  <v-dialog v-model="localIsVisible" max-width="600px" persistent>
    <v-card>
      <v-card-title>{{ menu.name }}</v-card-title>
      <v-card-text>
        <v-img :src="'http://localhost:8080/images/' + menu.pictureUrl" max-height="300"></v-img>
        <h2>{{ Number(menu.price).toLocaleString() }}원</h2>
        <p>{{ menu.description }}</p>
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
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="addToCart">장바구니 담기</v-btn>
        <v-btn @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { findMenuOptionGroups } from '@/services/menusApi';

const props = defineProps({
  menu: Object,
  isVisible: Boolean,
  category: String
});

const emit = defineEmits(['update:isVisible', 'cartUpdated']);

const menuOptionGroups = ref([]);
const orderOptions = ref({});
const totalPrices = ref(0);
const quantity = ref(1);
const optionTotalPrice = ref(0);

const localIsVisible = ref(props.isVisible);

watch(() => props.isVisible, (newVal) => {
  localIsVisible.value = newVal;
  if (newVal) {
    getMenuOptionGroups();
  }
});

const getMenuOptionGroups = async () => {
  console.log('Category:', props.category);
  if (!props.category) {
    return;
  }

  try {
    console.log('Menu:', props.menu);
    let response = await findMenuOptionGroups(props.menu.categoryId);
    if (response.code !== 200) {
      alert(response.message);
      return;
    }
    menuOptionGroups.value = response.data.menuOptionGroupResponseDtos;

    // Initialize orderOptions based on the retrieved menuOptionGroups
    for (const group of menuOptionGroups.value) {
      if (group.exclusiveYn) {
        // Select the first option by default
        const firstOption = group.menuOptionResponseDtos[0];
        orderOptions.value[group.id] = {
          optionName: firstOption.menuOptionName,
          price: firstOption.price
        };
      } else {
        orderOptions.value[group.id] = []; // Multiple selections
      }
    }
  } catch (error) {
    console.error(error.message);
  }
};

const calculateTotalPrice = () => {
  let optionTotal = 0;
  for (const groupId in orderOptions.value) {
    const options = orderOptions.value[groupId];
    if (Array.isArray(options)) {
      optionTotal += options.reduce((sum, option) => sum + option.price, 0);
    } else if (options) {
      optionTotal += options.price;
    }
  }
  totalPrices.value = (props.menu.price + optionTotal) * quantity.value;
  optionTotalPrice.value = optionTotal;
};

watch(orderOptions, calculateTotalPrice, { deep: true });
watch(quantity, calculateTotalPrice);

const addToCart = () => {
  const orderOptionGroups = menuOptionGroups.value.map(group => ({
    optionGroupName: group.name,
    orderOptions: Array.isArray(orderOptions.value[group.id])
      ? orderOptions.value[group.id]
      : [orderOptions.value[group.id]]
  }));

  const newOrderDetail = {
    menuId: props.menu.id,
    menuName: props.menu.name,
    menuPictureUrl: props.menu.pictureUrl,
    quantity: quantity.value,
    totalPrice: totalPrices.value,
    menuPrice: props.menu.price + optionTotalPrice.value,
    orderOptionGroups
  };

  const existing = JSON.parse(localStorage.getItem('orders'));

  const existingOrders = existing?.orderDetailDtos || [];
  existingOrders.push(newOrderDetail);
  const orders = {
    storeId: props.menu.storeId,
    storeName: existing ? existing.storeName : props.menu.storeName,
    orderTakeoutYn: existing ? existing.orderTakeoutYn : true,
    minOrderAmount: existing ? existing.minOrderAmount : 0,
    orderDetailDtos: existingOrders
  };

  localStorage.setItem('orders', JSON.stringify(orders));

  emit('cartUpdated');
  emit('update:isVisible', false);
};

const close = () => {
  emit('update:isVisible', false);
};

onMounted(() => {
  if (localIsVisible.value) {
    getMenuOptionGroups();
  }
});
</script>

<style scoped>
.order-summary {
  position: fixed;
  bottom: 16px;
  right: 16px;
  width: 300px;
  border: 1px solid #ccc;
  background: white;
  padding: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
