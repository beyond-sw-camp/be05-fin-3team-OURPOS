<template>
  <!-- 결제 성공 시 -->
  <section v-if="confirmed">
    <div class="box_section">
      <img style="width: 100px" src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" />
      <h2>결제를 완료했어요</h2>
      <p>주문번호: {{ jsonData.orderId }}</p>
    </div>
  </section>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { confirmPayment } from "@/confirmPayment";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const confirmed = ref(false);
    const jsonData = ref(null);

    onMounted(async () => {
      const requestData = {
        orderId: route.query.orderId,
        amount: route.query.amount,
        paymentKey: route.query.paymentKey,
      };

      async function confirm() {
        try {
          const { response, json } = await confirmPayment(requestData);
          console.log(json);
          if (!response.ok) {
            router.push(`/fail?message=${json.message}&code=${json.code}`);
          } else {
            confirmed.value = true;
            jsonData.value = json;
          }
        } catch (error) {
          console.error(error);
          router.push(`/fail?message=${error.message}`);
        }
      }

      confirm();
    });

    return {
      confirmed,
      jsonData,
    };
  },
};
</script>

<style scoped>
@import '@/public/style.css';

</style>
