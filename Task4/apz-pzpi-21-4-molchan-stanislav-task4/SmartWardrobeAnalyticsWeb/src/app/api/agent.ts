import axios, { AxiosResponse } from 'axios';
import { Collection } from '../models/collection';
import { User, UserFormValues } from '../models/user';
import { Token } from '../models/identity';
import { Item, PaginatedResult } from '../models/item';
import { Brand } from '../models/brand';
import { Tag } from '../models/tag';
import { Offer } from '../models/offer';
import { Bonus } from '../models/bonus';
import { Usage } from '../models/usage';

const sleep = (delay: number) => {
    return new Promise((resolve) => {
        setTimeout(resolve, delay)
    })
}

axios.defaults.baseURL = 'http://localhost:5002/api';

const responseBody = <T> (response: AxiosResponse<T>) => response.data;
const responseBodyItems = <T> (response: AxiosResponse<T>) => response.data.items;

const requests = 
{
    getMapping: <T> (url: string) => axios.get<T>(url).then(responseBodyItems),
    get: <T> (url: string) => axios.get<T>(url).then(responseBody),
    post: <T> (url: string, body: object) => axios.post<T>(url, body).then(responseBody),
    postUrl: <T> (url: string) => axios.post<T>(url).then(responseBody),
    put: <T> (url: string, body: object) => axios.put<T>(url, body).then(responseBody),
    delete: <T> (url: string) => axios.delete<T>(url).then(responseBody)
}

const Collections ={
    list: () => requests.getMapping<Collection[]>("/Collections?pageNumber=1&pageSize=20"),
    all: () => requests.get<Collection[]>("/Collections/all"),
    details: (id: string) => requests.get<Collection>(`/Collections/${id}`),
    create: (collection: Collection) => axios.post<void>(`/Collections`, collection),
    update: (collection: Collection) => axios.put<void>('/Collections', collection),
    delete: (id: string) => axios.delete<void>(`/sportcomplexes/delete/${id}`)
}

const Items ={
    list: (pageNumber: number, pageSize: number): Promise<PaginatedResult<Item>> =>
        requests.get<PaginatedResult<Item>>(`/Items?pageNumber=${pageNumber}&pageSize=${pageSize}`),
    listForCollection: (id: string) => requests.get<Item[]>(`/Items/collection/${id}`),
    details: (id: string) => requests.get<Item>(`/Items/${id}`),
    create: (item: Item) => axios.post<void>(`/Items`, item),
    update: (item: Item) => axios.put<void>('/Items', item),
    delete: (id: string) => axios.delete<void>(`/Items/${id}`),
    getMonthlyStatistics: (id: string) => requests.get(`/Statistics/item-statistic/${id}?months=3`),
    getUsages: (id: string) => requests.get(`/Statistics/item-usages/${id}`)
}

const Brands ={
    listByUser: () => requests.get<Brand[]>("/Brands/by-user"),
    list: () => requests.getMapping<Brand[]>("/Brands?pageNumber=1&pageSize=500"),
    details: (id: string) => requests.get<Brand>(`/Brands/${id}`),
    create: (brand: Brand) => axios.post<void>(`/Brands`, brand),
    update: (brand: Brand) => axios.put<void>('/Brands', brand),
    delete: (id: string) => axios.delete<void>(`/Brands/${id}`),
    getMonthlyStatistics: (id: string) => requests.get(`/Statistics/item-statistic/${id}?months=3`)
}

const Bonuses = {
    list: () => requests.get<Bonus[]>('/BrandBonuses'),
    details: (id: string) => requests.get<Bonus>(`/BrandBonuses/${id}`),
    create: (bonus: Bonus) => requests.post<void>('/BrandBonuses', bonus),
    update: (bonus: Bonus) => requests.put<void>(`/BrandBonuses`, bonus),
    delete: (id: string) => requests.delete<void>(`/BrandBonuses/${id}`)
};


const Tags ={
    list: () => requests.get<Tag[]>("/RFIDTags"),
    update: (id: string, itemId: string ) => axios.put<void>(`/RFIDTags/update/${id}/${itemId}`),
}

const Offers ={
    list: () => requests.get<Offer[]>("/Offers/user-offer"),
}


const Users = {
    list: () => requests.getMapping<User[]>("/users?pageNumber=1&pageSize=20"),
}

const Account = {
    current: () => requests.get<User>('/users/get'),
    login: (user: UserFormValues) => requests.post<Token>('/users/login', user),
    register: (user: UserFormValues) => requests.post<User>('users/register', user),
    ban: (id: string) => requests.delete(`/users/ban/${id}`),
    unban: (id: string) => requests.postUrl(`/users/unban/${id}`)
}

const Statistics = {
    getComboStatistics: () => requests.get('/Statistics/combo'),
    getPopularItems: (brandId, startDate, endDate, topCount) => 
        requests.get(`/Statistics/popular-items?brandId=${brandId}&startDate=${startDate}&endDate=${endDate}&topCount=${topCount}`),
    getSeasonalItemUsage: (brandId) =>
        requests.get(`/Statistics/seasonal-item-usage?brandId=${brandId}`)
}

const Usages = {
    list: (pageNumber: number, pageSize: number) => requests.get<PaginatedResult<Usage>>(`/Usages?pageNumber=${pageNumber}&pageSize=${pageSize}`),
    details: (id: string) => requests.get<Usage>(`/usages/${id}`),
    create: (usage: Usage) => requests.post<void>('/Usages', usage),
    update: (usage: Usage) => requests.put<void>(`/usages/${usage.id}`, usage),
    delete: (id: string) => requests.delete<void>(`/usages/${id}`)
};

const agent = 
{
    Collections,
    Users,
    Account,
    Items,
    Brands,
    Tags, 
    Offers,
    Statistics,
    Bonuses,
    Usages
}

export default agent;