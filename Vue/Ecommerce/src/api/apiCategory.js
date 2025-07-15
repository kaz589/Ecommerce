import instance from "@/utils/http";

export const ApiCategory = {
   findDistinctCategory: () => instance.get(`/category/distinct`),
}