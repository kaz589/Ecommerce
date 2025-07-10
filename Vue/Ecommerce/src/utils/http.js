import axios from "axios";
import { useAuthStore } from "@/stores/Auth";
//import { useAuthStore } from "@/stores/auth";
//axios通用設定
const instance = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 5000,
});

//request 攔截器
instance.interceptors.request.use(
  (config) => {
 

     const auth = useAuthStore();
     if (auth.token) {
       //有token要帶入request header(Authorization 標頭)
       console.log("有攜帶token");

       config.headers.Authorization = `Bearer ${auth.token}`;
       // console.log("token : " + config.headers.Authorization);
     }

    return config;
  },
  (error) => {
    // 處理請求錯誤
    return Promise.reject(error);
  }
);

//response 攔截器
instance.interceptors.response.use(
  (response) => {
    return response.status === 200
      ? Promise.resolve(response)
      : Promise.reject(response);
  },
  (error) => {
    const { response } = error;
    if (response) {
      // 處理具體的錯誤狀態碼
      errorHandle(response.status, response.info);
    }
    return Promise.reject(error);
  }
);

//錯誤處理
const errorHandle = (status, info) => {
  switch (status) {
    case 400:
      console.log("語義有誤，請檢查API參數");
      break;
    case 401:
      console.log("服務器認證失敗");
      break;
    case 403:
      console.log("服務器拒絕訪問");
      break;
    case 404:
      console.log("地址錯誤");
      break;
    case 500:
      console.log("服務器遇到意外");
      break;
    case 502:
      console.log("服務器無響應");
      break;
    default:
      console.log(info);
      break;
  }
};
export default instance;
