export interface CategoryRequest {
    categoryId: number;
    title: string;
    childCategories?: CategoryRequest[];
}

export interface PostRequest {
    title: string;
    content: string;
}