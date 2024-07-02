import api from "@/services/apiService";

export const findCategories = async () => {
  const response = await api.get("/categories");
  return response.data;
};

export const findMenus = async (category) => {
  const response = await api.get("/menus/store", {
    params: {
      category: category,
    }
  });
  return response.data;
};

export const findMenuOptionGroups = async (categoryId) => {
  const response = await api.get(`/categories/${categoryId}`);
  return response.data;
}
