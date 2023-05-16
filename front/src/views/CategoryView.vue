<template>
  <div class="category">
    <h1>Category Setting</h1>
      <v-row justify="end" class="mr-1">
          <v-btn
                  class="ml-3"
                  variant="outlined"
                  @click="addDialog = true"
          >ADD
          </v-btn>
          <v-btn
                  class="ml-3"
                  :disabled="!isSaveDisabled"
                  variant="outlined"
          >SAVE
          </v-btn>
          <v-btn
                  class="ml-3"
                  variant="outlined"
                  @click="cancel"
          >CANCEL
          </v-btn>
      </v-row>
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
      <v-dialog
              v-model="addDialog"
              width="auto"
      >
          <v-card>
              <v-card-text>
                  카테고리 제목을 입력해주세요.
              </v-card-text>
              <v-text-field
                      label="카테고리 제목"
                      v-model="newCategoryTitle"
                      required
              ></v-text-field>
              <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" variant="text" @click="addCategory">confirm</v-btn>
                  <v-btn color="primary" variant="text" @click="addDialog = false">cancel</v-btn>
              </v-card-actions>
          </v-card>
      </v-dialog>
  </div>
</template>

<script setup lang="ts">
import categoryApi from "@/api/categoryApi";
import {ref, onMounted, computed} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import {VueDraggableNext} from 'vue-draggable-next'

const categoryResponses = ref<CategoryResponse[]>([]);
const tmpCategories = ref<CategoryResponse[]>([]);
const newCategoryTitle = ref<string>("");
const addDialog = ref<boolean>(false);

let isSaveDisabled = computed(() => {
  return JSON.stringify(categoryResponses.value) !== JSON.stringify(tmpCategories.value);
});

onMounted(async () => {
  categoryResponses.value = await categoryApi.getCategories();
    tmpCategories.value = JSON.parse(JSON.stringify(categoryResponses.value));
})

const cancel= () => {
    tmpCategories.value = JSON.parse(JSON.stringify(categoryResponses.value));
}

const addCategory = () => {
    const newCategory: CategoryResponse = {
        categoryId: -1,
        title: newCategoryTitle.value,
        childCategoryResponses: [],
    };

    tmpCategories.value.push(newCategory);
    newCategoryTitle.value = '';
    addDialog.value = false;
};


const checkMove = () => {
  // here you can add your custom logic
  return true;
};

const dragEnd = () => {
  console.log('response', categoryResponses.value)
  console.log('tmp', tmpCategories.value);
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