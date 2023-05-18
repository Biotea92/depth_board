<template>
  <v-card>
    <v-layout>
      <v-navigation-drawer expand-on-hover>
        <v-list>
          <v-list-item>
            <v-list-item-title
                class="cursor-pointer"
                @click="$router.push('/')"
            >Home
            </v-list-item-title>
            <template v-slot:append>
              <v-btn
                  icon
                  @click="$router.push('/category')"
              >
                <v-icon color="grey-lighten">mdi-cog</v-icon>
              </v-btn>
            </template>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list v-for="category in categories" :key="category.categoryId">
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
                @click="$router.push(`/board/${childCategory.categoryId}/${childCategory.title}`)"
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
import {onMounted} from "vue";
import {useCategoryStore} from '@/store/piniaStore'
import {storeToRefs} from "pinia";

const store = useCategoryStore();
const {categories}: { categories: any } = storeToRefs(store);
const {setCategories} = store

onMounted(async () => {
  await setCategories();
})
</script>

<style>
.cursor-pointer {
  cursor: pointer;
}
</style>