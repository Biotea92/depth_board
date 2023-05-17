import {defineStore} from "pinia";
import {ref} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import categoryApi from "@/api/categoryApi";
import {CategoryRequest} from "@/api/request/requests";

export const useCategoryStore = defineStore('category', () => {
    const categories = ref<CategoryResponse[]>([]);

    const setCategories = (async () => {
        categories.value = await categoryApi.getCategories();
    })

    const updateCategories = (async (parentCategories: CategoryRequest[], removedCategoryIds: number[]) => {
        categories.value = await categoryApi.putCategories(parentCategories, removedCategoryIds);
    })

    return { categories , setCategories , updateCategories}
})