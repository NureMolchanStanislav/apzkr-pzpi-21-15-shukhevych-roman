import axios, { AxiosResponse } from 'axios';
import { Collection } from '../models/collection';
import { User, UserFormValues } from '../models/user';
import { Token } from '../models/identity';

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
}


const Users = {
    list: () => requests.getMapping<User[]>("/users?pageNumber=1&pageSize=20"),
}

const Account = {
    current: () => requests.get('/users/get'),
    login: (user: UserFormValues) => requests.post<Token>('/users/login', user),
    register: (user: UserFormValues) => requests.post<User>('users/register', user),
    ban: (id: string) => requests.delete(`/users/ban/${id}`),
    unban: (id: string) => requests.postUrl(`/users/unban/${id}`)
}

const agent = 
{
    Collections,
    Users,
    Account
}

export default agent;