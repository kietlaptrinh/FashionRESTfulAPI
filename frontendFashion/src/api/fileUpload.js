import axios from "axios";
import { API_BASE_URL, getHeaders } from "./constant";

export const fileUploadAPI = async (formData) => {
  const url = API_BASE_URL + `/api/file`;
  try {
    const response = await axios.post(url, formData, {
      headers: {
        ...getHeaders(),
        "Content-Type": "multipart/form-data",
      },
    });
    return response?.data;
  } catch (err) {
    console.error("File upload failed:", err.message);
    throw new Error(err);
  }
};
