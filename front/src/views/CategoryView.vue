<template>
    <div class="category">
        <h1>This is an about category</h1>
        <div v-for="category in tmpCategories" :key="category.categoryId">
            <CategoryComponent
                :category="category"
            >

            </CategoryComponent>
        </div>
    </div>
</template>

<script setup lang="ts">
import categoryApi from "@/api/categoryApi";
import { ref, onMounted } from "vue";
import { CategoryResponse } from "@/api/response/responses";
import CategoryComponent from "@/components/CategoryComponent.vue";

const categoryResponses = ref<CategoryResponse[]>([]);
const tmpCategories = ref<CategoryResponse[]>([]);

onMounted(async () => {
    categoryResponses.value = await categoryApi.getCategories();
    tmpCategories.value = categoryResponses.value.slice();
})
</script>
