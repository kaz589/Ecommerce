import instance from "@/utils/http";

export const ApiAirport = {
  //分頁搜尋測試
  findAllUsers: () => instance.get(`/users/All`),
}