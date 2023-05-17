import axios from "axios";
import {CategoryResponse} from "@/api/response/responses";
import {CategoryRequest} from "@/api/request/requests";

const API_BASE_URL = 'http://localhost:8081/api/board';

export default {
    getCategories(): Promise<CategoryResponse[]> {
        return axios.get(`${API_BASE_URL}/category`)
            .then((response) => response.data)
            .catch((error) => {
                console.log(error);
                throw error;
            });
    },

    putCategories(parentCategories: CategoryRequest[], removedCategoryIds: number[]): Promise<CategoryResponse[]> {
        return axios.put(`${API_BASE_URL}/category`, {
            "parentCategories": parentCategories,
            "removedCategoryIds" : removedCategoryIds
        })
            .then((response) => {
                if (response.status === 200) {
                    return this.getCategories();
                }
                return [];
            })
            .catch((error) => {
                console.log(error);
                throw error;
            });
    }
}