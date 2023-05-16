<template>
    <v-card>
        <v-layout>
            <v-navigation-drawer expand-on-hover>
                <v-list>
                    <v-list-item>
                        <v-list-item-title
                                class="cursor-pointer"
                                @click="() => navigateTo('/')"
                        >Home
                        </v-list-item-title>
                        <template v-slot:append>
                            <v-btn
                                    icon
                                    @click="() => navigateTo('/category')"
                            >
                                <v-icon color="grey-lighten">mdi-cog</v-icon>
                            </v-btn>
                        </template>
                    </v-list-item>
                </v-list>

                <v-divider></v-divider>

                <v-list v-for="category in categoryResponses" :key="category.categoryId">
                    <v-list-group
                            :value="category.title"
                    >
                        <template v-slot:activator="{ props }">
                            <v-list-item
                                    v-bind="props"
                                    :title="category.title"
                            ></v-list-item>
                        </template>

                        <v-list-item
                                v-for="childCategory in category.childCategoryResponses"
                                :key="childCategory.categoryId"
                                :value="childCategory.title"
                        >
                            <v-list-item-title>
                                {{ childCategory.title }}
                            </v-list-item-title>
                        </v-list-item>
                    </v-list-group>
                </v-list>
            </v-navigation-drawer>
            <v-main>
                <router-view/>
            </v-main>
        </v-layout>
    </v-card>
</template>

<script setup lang="ts">
import categoryApi from "@/api/categoryApi";
import {ref, onMounted, onUpdated} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import router from "@/router";

const categoryResponses = ref<CategoryResponse[]>([]);

onMounted(async () => {
    categoryResponses.value = await categoryApi.getCategories();
})
onUpdated(async () => {
    categoryResponses.value = await categoryApi.getCategories();
})
const navigateTo = (to) => {
    router.push(to);
}
</script>

<style>
.cursor-pointer {
    cursor: pointer;
}
</style>