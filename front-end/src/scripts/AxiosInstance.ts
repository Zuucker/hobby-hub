import axios from "axios";
import { readCookie } from "./Cookies";

const token: string | undefined = readCookie("jwtToken");

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    config.data = { ...config.data, jwtToken: token };
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
