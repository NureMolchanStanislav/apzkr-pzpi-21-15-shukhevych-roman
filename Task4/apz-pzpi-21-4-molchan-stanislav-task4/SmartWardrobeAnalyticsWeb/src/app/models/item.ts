export interface Item {
    id: string
    name: string
    description: string
    categories: number
    brandId: string
    collectionId: string
}


export interface ItemCreate {
    name: string
    description: string
    categories: number
    brandId: string
    collectionId: string
}

export interface PaginatedResult<T> {
    items: T[];
    pageNumber: number;
    pageSize: number;
    totalPages: number;
    totalItems: number;
    hasPreviousPage: boolean;
    hasNextPage: boolean;
  }