import axios from "axios";
import router from "@/router";

const API_BASE_URL = "http://localhost:8080/api/v1";
const UNAUTHORIZED = 401;
const FORBIDDEN = 403;

const onUnauthroized = () => {
  router.push('/login');
};

const onForbidden = () => {
  alert("권한이 없습니다.");
}

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Authorization": localStorage.getItem("token"),
  },
});

// 요청 인터셉터
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response.status === UNAUTHORIZED) {
      onUnauthroized();
    }
    if (error.response.status === FORBIDDEN) {
      onForbidden();
    }
    return Promise.reject(error);
  }
);

export const saveTempOrder = async (orderId, amount) => {
  const response = await api.post("/orders/temp-order", { orderId, amount });
  return response.data;
};

export const deleteTempOrder = async (orderId) => {
  const response = await api.delete("/orders/temp-order", {
    data: { orderId },
  });
  return response.data;
};

export default api;
