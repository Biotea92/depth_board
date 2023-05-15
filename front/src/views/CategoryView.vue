<template>
  <div class="category">
    <h1>Category Setting</h1>
    <v-btn
        :disabled="!isSaveDisabled"
        variant="outlined">
      SAVE
    </v-btn>
    <v-btn
        class="ml-3"
        variant="outlined">
      CANCEL
    </v-btn>
    <VueDraggableNext
        class="dragArea list-group w-full"
        :list="tmpCategories"
        ghost-class="ghost-card"
        :animation="200"
        @end="dragEnd"
        :checkMove="checkMove"
        group="group"
    >
      <v-list
          v-for="(category, index) in tmpCategories"
          :key="index"
          class="mt-3 cursor-move cursor-pointer"
      >
        <v-card variant="outlined" class="card-padding">
          <v-list-item-title>
            {{ category.title }}
          </v-list-item-title>

          <VueDraggableNext
              class="dragArea list-group w-full inner-draggable"
              :list="category.childCategoryResponses"
              ghost-class="ghost-card"
              :animation="200"
              @end="dragEnd"
              :checkMove="checkMove"
              group="group"
          >
            <v-list
                v-for="(childCategory, index) in category.childCategoryResponses"
                :key="index"
                class="mt-3 cursor-move cursor-pointer"
            >
              <v-card variant="tonal" class="card-padding">
                <v-list-item-title>
                  {{ childCategory.title }}
                </v-list-item-title>
              </v-card>
            </v-list>
          </VueDraggableNext>
        </v-card>
      </v-list>
    </VueDraggableNext>
  </div>
</template>

<script setup lang="ts">
import categoryApi from "@/api/categoryApi";
import {ref, onMounted, computed} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import {VueDraggableNext} from 'vue-draggable-next'

const categoryResponses = ref<CategoryResponse[]>([]);
const tmpCategories = ref<CategoryResponse[]>([]);

let isSaveDisabled = computed(() => {
  return JSON.stringify(categoryResponses.value) !== JSON.stringify(tmpCategories.value);
});

onMounted(async () => {
  categoryResponses.value = await categoryApi.getCategories();
  tmpCategories.value = categoryResponses.value.slice();
})

const checkMove = () => {
  // here you can add your custom logic
  return true;
};

const dragEnd = () => {
  console.log(tmpCategories.value);
  // save the new order of tmpCategories
  // possibly make an API call to update the order on your backend
};
</script>

<style scoped>
.inner-draggable {
  margin-left: 3rem;
  margin-right: 3rem;
}

.cursor-pointer {
  cursor: pointer;
}

.card-padding {
  padding: 10px;
}
</style>