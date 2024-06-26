import { createApp } from "vue";
import App from "./App.vue";
import store from "./store";
import router from "./router";
import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";
import MaterialDashboard from "./material-dashboard";
import SockJS from 'sockjs-client';
import {Stomp} from "@stomp/stompjs";

const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    stompClient.subscribe('/topic/orders', (message) => {
        const orderNotification = JSON.parse(message.body);
        alert(`Order ID: ${orderNotification.orderId}, Message: ${orderNotification.message}`);
    });
});

const appInstance = createApp(App);
appInstance.use(store);
appInstance.use(router);
appInstance.use(MaterialDashboard);
appInstance.mount("#app");
