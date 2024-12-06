import axios from "axios";
import { API_BASE_URL, getHeaders } from "./constant";

export const placeOrderAPI = async (data) => {
  const url = API_BASE_URL + "/api/order";
  try {
    const response = await axios(url, {
      method: "POST",
      data: data,
      headers: getHeaders(),
    });
    return response?.data;
  } catch (err) {
    throw new Error(err);
  }
};

export const confirmPaymentAPI = async (data) => {
  const url = API_BASE_URL + "/api/order/update-payment";
  try {
    const response = await axios(url, {
      method: "POST",
      data: data,
      headers: getHeaders(),
    });
    return response?.data;
  } catch (err) {
    throw new Error(err);
  }
};

export const confirmPaymentPayPalAPI = async (data) => {
  const url = API_BASE_URL + "/api/order/paypal/success";
  try {
    const response = await axios(url, {
      method: "POST",
      data: data,
      headers: getHeaders(),
    });
    return response?.data;
  } catch (err) {
    console.error("PayPal API Error: ", err.response || err.message);
    // Nếu muốn xử lý lỗi cụ thể hơn
    if (err.response) {
      throw new Error(err.response.data.message || "Error processing payment");
    } else {
      throw new Error("Network Error");
    }
  }
};
