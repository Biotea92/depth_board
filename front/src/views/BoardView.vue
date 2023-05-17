<template>
  <div class="board">
    <h1>{{ route.params.title }}</h1>
    <v-row justify="end" class="mr-1">
      <v-btn class="ml-3" variant="outlined" @click="addDialog = true">글 작성</v-btn>
    </v-row>
    <h2 v-show="posts.length <= 0"> 작성된 글이 없습니다. </h2>
    <v-list-item
        v-for="(post) in posts"
        :key="post.postId"
        class="mt-3 cursor-move cursor-pointer"
    >
      <v-card variant="outlined" class="card-padding">
        <v-row>
          <v-col cols="8">
            <v-list-item-title>
              {{ post.title }}
            </v-list-item-title>
            <v-list-item-subtitle>
              {{ post.content }}
            </v-list-item-subtitle>
            <v-list-subheader>
              {{ post.createdAt }}
            </v-list-subheader>
          </v-col>
        </v-row>
      </v-card>
    </v-list-item>
  </div>
  <v-dialog
      v-model="addDialog"
      width="auto"
  >
    <v-card>
      <v-card-text>
        제목과 내용을 입력 해주세요.
      </v-card-text>
      <v-text-field
          label="글 제목"
          v-model="newPostTitle"
          required
      ></v-text-field>
      <v-text-field
          label="글 내용"
          v-model="newPostContent"
          required
      ></v-text-field>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" variant="text" @click="addPost">confirm</v-btn>
        <v-btn color="primary" variant="text" @click="addDialog = false">cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <div class="extra-space">
  </div>
</template>

<script setup lang="ts">
import {useRoute} from 'vue-router';
import {watchEffect, ref} from "vue";
import {PostResponse} from "@/api/response/responses";
import postApi from "@/api/postApi";
import {PostRequest} from "@/api/request/requests";


const route = useRoute();

const categoryId = ref<number>(-1);
const posts = ref<PostResponse[]>([]);
const addDialog = ref<boolean>(false);
const newPostTitle = ref<string>('');
const newPostContent = ref<string>('');

watchEffect(async () => {
  categoryId.value = Number(route.params.id);
  posts.value = await postApi.getPosts(categoryId.value);
})

const addPost = async () => {
  const newPost: PostRequest = {
    title: newPostTitle.value,
    content: newPostContent.value
  };
  // api 호출
  await postApi.createPost(categoryId.value, newPost)
  newPostTitle.value = '';
  newPostContent.value = '';
  addDialog.value = false;
  posts.value = await postApi.getPosts(categoryId.value);
};

</script>

<style scoped>
.extra-space {
  height: 100px;
}

.card-padding {
  padding: 10px;
}
</style>