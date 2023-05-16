<template>
    <div :class="`category depth-${category.depth}`">
        <div class="category-title">
            {{ category.title }}
        </div>
        <button @click="editCategory">
            Edit
        </button>
        <button @click="deleteCategory">
            Delete
        </button>
        <div v-if="category.childCategoryResponses && category.childCategoryResponses.length > 0">
            <CategoryComponent
                    v-for="child in category.childCategoryResponses"
                    :key="child.categoryId"
                    :category="child"
                    @edit="editCategory"
                    @delete="deleteCategory"
            />
        </div>
    </div>
</template>

<script setup lang="ts">
// import {onMounted, ref} from "vue";
import {CategoryResponse} from "@/api/response/responses";

defineProps<{
    category?: CategoryResponse
}>()

// const category = ref();
//
// onMounted(() => {
//     category.value = props.category;
// })

function editCategory() {
    let count = 0;
    count++;
}

function deleteCategory() {
    let count = 0;
    count++;
}


</script>

<style scoped>
.category {
    /* common styles for all categories */
}

.category.depth-1 {
    /* styles for depth 1 categories */
}

.category.depth-2 {
    /* styles for depth 2 categories */
}

/* add more depth classes as necessary */
</style>