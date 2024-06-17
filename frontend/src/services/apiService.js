import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1";

export const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export const findLoginCustomer = async () => {
  const response = await api.get("/customers/my");
  return response.data.data;
};

export const saveTempOrder = async (orderId, amount) => {
  const response = await api.post("/orders/temp-order", { orderId, amount });
  return response.data;
};

export const deleteTempOrder = async (orderId) => {
  const response = await api.delete("/orders/temp-order", {
    data: { orderId },
  });
  return response.data;
}
