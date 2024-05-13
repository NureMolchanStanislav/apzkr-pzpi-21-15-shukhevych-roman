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