export interface CategoryRequest {
    categoryId: number;
    title: string;
    childCategories?: CategoryRequest[];
}