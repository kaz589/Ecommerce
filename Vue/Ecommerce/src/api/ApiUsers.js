import instance from "@/utils/http";

export const ApiUsers = {
  //分頁搜尋測試
  findAllUsers: () => instance.get(`/users/All`),
}