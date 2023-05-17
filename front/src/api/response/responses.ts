export interface CategoryResponse {
    categoryId: number;
    title: string;
    depth?: number;
    sequence?: number;
    hasPost?: boolean;
    childCategoryResponses?: CategoryResponse[];
    tempId?: string;
}

export interface PostResponse {
    postId: number;
    title: string;
    content: string;
    createdAt: string;
    categoryResponse?: CategoryResponse
}