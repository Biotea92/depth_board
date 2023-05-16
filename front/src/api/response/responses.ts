export interface CategoryResponse {
    categoryId: number;
    title: string;
    depth: number;
    sequence: number;
    hasPost: boolean;
    childCategoryResponses: CategoryResponse[];
}