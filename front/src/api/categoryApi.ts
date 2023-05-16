import axios from "axios";
import {CategoryResponse} from "@/api/response/responses";

const API_BASE_URL = 'http://localhost:8081/api/board';

export default {
    getCategories() : Promise<CategoryResponse[]> {
        return axios.get(`${API_BASE_URL}/category`)
            .then((response) => response.data)
            .catch((error) => {
                console.log(error);
                throw error;
        });
    }
}