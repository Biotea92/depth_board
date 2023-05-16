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
<!--            <transition-group name="fade" tag="v-list">-->
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
                                <v-btn icon @click="deleteCategory(parentIndex, '하위 카테고리가 모두 삭제 됩니다. 삭제하시겠습니까?')">
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
                                :checkMove="checkMove"
                                group="group"
                        >
<!--                            <transition-group name="fade" tag="v-list">-->
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
                                                <v-btn icon @click="editChildCategory(category, parentIndex, childIndex)" class="mr-2">
                                                    <v-icon color="grey-lighten-0">mdi-pencil</v-icon>
                                                </v-btn>

                                                <v-btn icon @click="deleteChildCategory(category, childIndex, '해당 카테고리 하위 게시물이 모두 삭제됩니다. 삭제하시겠습니까?')">
                                                    <v-icon color="grey-lighten-0">mdi-delete</v-icon>
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                    </v-card>
                                </v-list-item>
<!--                            </transition-group>-->
                        </VueDraggableNext>
                    </v-card>

                </v-list-item>
<!--            </transition-group>-->
        </VueDraggableNext>
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
                    카테고리 제목을 입력해주세요.
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
                <!--                <template v-slot:activator="{ props }">-->
                <!--                    <v-btn-->
                <!--                            color="primary"-->
                <!--                            v-bind="props"-->
                <!--                    >-->
                <!--                        Open Dialog-->
                <!--                    </v-btn>-->
                <!--                </template>-->
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
import categoryApi from "@/api/categoryApi";
import {ref, onMounted, computed} from "vue";
import {CategoryResponse} from "@/api/response/responses";
import {VueDraggableNext} from 'vue-draggable-next'

const categoryResponses = ref<CategoryResponse[]>([]);
const tmpCategories = ref<CategoryResponse[]>([]);
const newCategoryTitle = ref<string>("");
const addDialog = ref<boolean>(false);
const confirmDialog = ref<boolean>(false);
const confirmDialogMessage = ref<string>("");
let confirmCallback: () => void;
const editDialog = ref<boolean>(false);
const editParentIndex = ref<number>(-1);
const editChildIndex = ref<number>(-1);

let isSaveDisabled = computed(() => {
    return JSON.stringify(categoryResponses.value) !== JSON.stringify(tmpCategories.value);
});

onMounted(async () => {
    categoryResponses.value = await categoryApi.getCategories();
    tmpCategories.value = JSON.parse(JSON.stringify(categoryResponses.value));
})

const cancel = () => {
    tmpCategories.value = JSON.parse(JSON.stringify(categoryResponses.value));
}

const addCategory = () => {
    const newCategory: { childCategoryResponses: any[]; title: string; categoryId: number; tempId: string} = {
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
    } else {
        editChildCategoryTitle()
    }
}

const deleteCategory = (index: number, message: string) => {
    confirmDialogMessage.value = message;
    confirmCallback = () => {
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
    editChildIndex.value = -1;
}

const deleteChildCategory = (category: any, index: number, message: string) => {
    confirmDialogMessage.value = message;
    confirmCallback = () => {
        category.childCategoryResponses.splice(index, 1);
    };
    confirmDialog.value = true;
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
</style>