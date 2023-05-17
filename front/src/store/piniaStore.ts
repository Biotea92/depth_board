import {defineStore} from "pinia";
import {ref} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import categoryApi from "@/api/categoryApi";

export const useCategoryStore = defineStore('category', () => {
    const categories = ref<CategoryResponse[]>([]);

    const setCategories = (async () => {
        categories.value = await categoryApi.getCategories();
    })

    return { categories , setCategories }
})