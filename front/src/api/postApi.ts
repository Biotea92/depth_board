import axios from "axios";
import {PostResponse} from "@/api/response/responses";
import {PostRequest} from "@/api/request/requests";

const API_BASE_URL = 'http://localhost:8081/api/board';

export default {
    getPosts(categoryId: number): Promise<PostResponse[]> {
        return axios.get(`${API_BASE_URL}/category/${categoryId}/posts`)
            .then((response) => response.data)
            .catch((error) => {
                console.log(error);
                throw error;
            });
    },

    createPost(categoryId: number, postRequest: PostRequest): Promise<void> {
        return axios.post(`${API_BASE_URL}/category/${categoryId}/post`, {
            "title": postRequest.title,
            "content": postRequest.content
        }).then((response) => response.data)
            .catch((error) => {
                console.log(error);
                throw error;
            });
    }
}