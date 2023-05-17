<template>
  <div class="category">
    <h1>Category Setting</h1>
    <v-row justify="end" class="mr-1">
      <v-btn class="ml-3" variant="outlined" @click="addDialog = true">ADD</v-btn>
      <v-btn class="ml-3" :disabled="!isSaveDisabled" variant="outlined" @click="save">SAVE</v-btn>
      <v-btn class="ml-3" variant="outlined" @click="cancel">CANCEL</v-btn>
    </v-row>
    <VueDraggableNext
        class="dragArea list-group w-full"
        :list="tmpCategories"
        ghost-class="ghost-card"
        :animation="200"
        @end="dragEnd"
        :move="checkMove"
        group="group"
    >
      <v-list-item
          v-for="(category, parentIndex) in tmpCategories"
          :key="category.categoryId !== -1 ? category.categoryId : category.tempId"
          class="mt-3 cursor-move cursor-pointer"
      >
        <v-card variant="outlined" class="card-padding">
          <v-row>
            <v-col cols="8">
              <v-list-item-title>
                {{ category.title }}
              </v-list-item-title>
            </v-col>

            <v-col cols="4" class="text-right">
              <v-btn icon @click="editCategory(parentIndex)" class="mr-2">
                <v-icon color="grey-lighten-0">mdi-pencil</v-icon>
              </v-btn>
              <v-btn icon @click="deleteCategory(parentIndex, '해당 카테고리, 하위 카테고리, 하위 게시물이 모두 삭제됩니다. 삭제하시겠습니까?')">
                <v-icon color="grey-lighten-0">mdi-delete</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <VueDraggableNext
              class="dragArea list-group w-full inner-draggable"
              :list="category.childCategoryResponses"
              ghost-class="ghost-card"
              :animation="200"
              @end="dragEnd"
              :move="checkMove"
              group="group"
          >
            <v-list-item
                v-for="(childCategory, childIndex) in category.childCategoryResponses"
                :key="childCategory.categoryId !== -1 ? childCategory.categoryId : childCategory.tempId"
                class="mt-3 cursor-move cursor-pointer"
            >
              <v-card variant="tonal" class="card-padding">
                <v-row>
                  <v-col cols="8">
                    <v-list-item-title>
                      {{ childCategory.title }}
                    </v-list-item-title>
                  </v-col>
                  <v-col cols="4" class="text-right">
                    <v-btn icon @click="editChildCategory(category, parentIndex, childIndex)"
                           class="mr-2">
                      <v-icon color="grey-lighten-0">mdi-pencil</v-icon>
                    </v-btn>
                    <v-btn icon
                           @click="deleteChildCategory(category, childIndex, '해당 카테고리 하위 게시물이 모두 삭제됩니다. 삭제하시겠습니까?')">
                      <v-icon color="grey-lighten-0">mdi-delete</v-icon>
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card>
            </v-list-item>
          </VueDraggableNext>
        </v-card>
      </v-list-item>
    </VueDraggableNext>
    <v-alert
        density="compact"
        type="warning"
        variant="outlined"
        transition="slide-y-transition"
        v-model="alert"
    >
      {{ alertMessage }}
    </v-alert>
    <!--        카테고리 추가 다이얼로그 -->
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
    <!--        카테고리 수정 다이얼로그-->
    <v-dialog
        v-model="editDialog"
        width="auto"
    >
      <v-card>
        <v-card-text>
          수정 할 카테고리 제목을 입력해주세요.
        </v-card-text>
        <v-text-field
            label="카테고리 제목"
            v-model="newCategoryTitle"
            required
        ></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" variant="text" @click="editCategoryTitle">confirm</v-btn>
          <v-btn color="primary" variant="text" @click="editDialog = false">cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-row justify="center">
      <v-dialog
          v-model="confirmDialog"
          persistent
          width="auto"
      >
        <v-card>
          <v-card-title class="text-h5">
            {{ confirmDialogMessage }}
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color="green-darken-1"
                variant="text"
                @click="confirmDialog = false"
            >
              Disagree
            </v-btn>
            <v-btn
                color="green-darken-1"
                variant="text"
                @click="confirmAndClose"
            >
              Agree
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
  <div class="extra-space">
  </div>
</template>

<script setup lang="ts">
import {useCategoryStore} from '@/store/piniaStore'
import {storeToRefs} from "pinia";
import {ref, onMounted, computed} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import {VueDraggableNext} from 'vue-draggable-next';

const store = useCategoryStore();
const {categories}: { categories: any } = storeToRefs(store)
const {setCategories, updateCategories} = store

const tmpCategories = ref<CategoryResponse[]>([]);
const newCategoryTitle = ref<string>("");
const addDialog = ref<boolean>(false);
const confirmDialog = ref<boolean>(false);
const confirmDialogMessage = ref<string>("");
const editDialog = ref<boolean>(false);
const editParentIndex = ref<number>(-1);
const editChildIndex = ref<number>(-1);
const alert = ref<boolean>(false);
const alertMessage = ref<string>("");
const removedCategoryIds = ref<number[]>([]);

let confirmCallback: () => void;

let isSaveDisabled = computed(() => {
  return JSON.stringify(categories.value) !== JSON.stringify(tmpCategories.value);
});

onMounted(async () => {
  // 현재 이 페이지에서 categoryStore에 업데이트 하는중 나중에는 빼도 될듯
  await setCategories()
  tmpCategories.value = JSON.parse(JSON.stringify(categories.value));
})

const save = async () => {
  const categoryRequest = tmpCategories.value.map(parentCategory => {
    return {
      categoryId: parentCategory.categoryId,
      title: parentCategory.title,
      childCategories: parentCategory.childCategoryResponses ? parentCategory.childCategoryResponses.map(childCategory => {
        return {
          categoryId: childCategory.categoryId,
          title: childCategory.title
        }
      }) : undefined
    }
  });
  await updateCategories(categoryRequest, removedCategoryIds.value);
  tmpCategories.value = JSON.parse(JSON.stringify(categories.value));
}

const cancel = () => {
  tmpCategories.value = JSON.parse(JSON.stringify(categories.value));
  removedCategoryIds.value = [];
}

const addCategory = () => {
  const newCategory: { childCategoryResponses: any[]; title: string; categoryId: number; tempId: string } = {
    categoryId: -1,
    title: newCategoryTitle.value,
    childCategoryResponses: [],
    tempId: Date.now().toString()
  };
  tmpCategories.value.push(newCategory);
  newCategoryTitle.value = '';
  addDialog.value = false;
};

const confirmAndClose = () => {
  confirmCallback();
  confirmDialog.value = false;
};

const editCategory = (index: number) => {
  editDialog.value = true;
  newCategoryTitle.value = tmpCategories.value[index].title;
  editParentIndex.value = index;
};

const editCategoryTitle = () => {
  if (editChildIndex.value === -1) {
    if (editParentIndex.value !== -1) {
      tmpCategories.value[editParentIndex.value].title = newCategoryTitle.value;
    }
    newCategoryTitle.value = '';
    editDialog.value = false;
    editParentIndex.value = -1;
  } else {
    editChildCategoryTitle()
  }
}

const deleteCategory = (index: number, message: string) => {
  confirmDialogMessage.value = message
  confirmCallback = () => {
    removedCategoryIds.value.push(tmpCategories.value[index].categoryId);
    tmpCategories.value.splice(index, 1);
  };
  confirmDialog.value = true;
};

const editChildCategory = (category: any, parentIndex: number, childIndex: number) => {
  editDialog.value = true;
  newCategoryTitle.value = category.childCategoryResponses[childIndex].title;
  editParentIndex.value = parentIndex;
  editChildIndex.value = childIndex;
};

const editChildCategoryTitle = () => {
  if (editChildIndex.value !== -1) {
    tmpCategories.value[editParentIndex.value].childCategoryResponses[editChildIndex.value].title = newCategoryTitle.value;
  }
  newCategoryTitle.value = '';
  editDialog.value = false;
  editParentIndex.value = -1;
  editChildIndex.value = -1;
}

const deleteChildCategory = (category: any, index: number, message: string) => {
  if (category.childCategoryResponses[index].hasPost !== null && category.childCategoryResponses[index].hasPost) {
    confirmDialogMessage.value = message + ' (하위 게시물이 존재합니다.)';
  } else {
    confirmDialogMessage.value = message + ' (하위 게시물이 없습니다.)';
  }
  confirmCallback = () => {
    removedCategoryIds.value.push(category.childCategoryResponses[index].categoryId);
    category.childCategoryResponses.splice(index, 1);
  };
  confirmDialog.value = true;
};

const checkMove = (event: any) => {
  // 움직일 때마다 반환됨
  if (event.draggedContext.element.hasPost) {
    alertWarning("게시물이 존재 합니다. 상위카테고리로 이동시 게시물이 보이지 않습니다.");
  }
  if (event.draggedContext.element.childCategoryResponses.length > 0) {
    alertWarning("하위 카테고리가 존재 합니다. 다른 카테고리로 이동하고 저장시 하위의 카테고리가 삭제됩니다.");
  }
  return true;
};

const alertWarning = (title: string) => {
  alertMessage.value = title;
  alert.value = true;
}

const dragEnd = () => {
  alert.value = false
};
</script>

<style scoped>
.ghost-card {
  opacity: 0.5;
  background: #F7FAFC;
  border: 1px solid #4299e1;
}

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

.fade-leave-active {
  transition: all 0.5s ease;
}

.fade-leave-from {
  opacity: 1;
  transform: translateX(0);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(100px);
}

.extra-space {
  height: 100px;
}

.v-alert {
  position: fixed;
  left: 50%;
  top: 11%;
  transform: translate(-50%, -50%);
}
</style>