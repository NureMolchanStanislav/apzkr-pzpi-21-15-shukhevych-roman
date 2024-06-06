import {makeAutoObservable, runInAction} from 'mobx'
import { Collection } from '../models/collection';
import agent from '../api/agent';

export default class CollectionStore
{
    collections: Collection[] = [];
    collectionRegistry = new Map<string, Collection>();
    selectedCollection: Collection | undefined = undefined;
    editMode = false;
    loading = false;
    loadingInitial = false;

    constructor()
    {
        makeAutoObservable(this)
    }

    loadCollections = async () => {
        this.setLoadingInitial(true);
        try{
            const sportComplexes = await agent.Collections.list();
            runInAction(()=>{
                this.collections = sportComplexes;
            })
            this.setLoadingInitial(false);
        } catch (error) {
            console.log(error);
            this.setLoadingInitial(false);
        }
    }

    loadAllCollections = async () => {
        this.setLoadingInitial(true);
        try{
            const sportComplexes = await agent.Collections.all();
            runInAction(()=>{
                this.collections = sportComplexes;
            })
            this.setLoadingInitial(false);
        } catch (error) {
            console.log(error);
            this.setLoadingInitial(false);
        }
    }

    loadCollection = async (id: string) => {

        this.setLoadingInitial(true);
        try {
            const collection = await agent.Collections.details(id);
            console.log("Отримана колекція: ", collection);
            runInAction(() => {
                this.selectedCollection = collection;
                this.collectionRegistry.set(collection.id, collection);
            });
            this.setLoadingInitial(false);
            return collection;
        } catch (error) {
            console.error("Помилка завантаження колекції:", error);
            this.setLoadingInitial(false);
        }
    };

    createCollection = async (collection: Collection) => {
        this.loading = true;
        try{
            await agent.Collections.create(collection);
            runInAction(()=>{
                this.collections.push(collection);
                this.selectedCollection = collection;
                this.editMode = false;
                this.loading = false;
            })
            await this.loadCollection;
        } catch (error) {
            console.log(error);
            runInAction(() => {
                this.loading = false;
            })
        }
    }

    updateCollection = async (collection: Collection) => {
        this.loading = true;
        try {
            console.log("Відправка на оновлення колекції:", JSON.stringify(collection));
            await agent.Collections.update(collection);
            runInAction(() => {
                this.collections = [...this.collections.filter(a => a.id !== collection.id), collection]
                this.selectedCollection = collection;
                this.editMode = false;
                this.loading = false;
            })
        } catch (error) {
            console.log(error);
            this.loading = false;
        }
    }


    deleteCollection = async (id: string) => {
        this.loading = true;
        try {
            await agent.Collections.delete(id);
            runInAction(() => {
                this.collections = [...this.collections.filter(a => a.id !== id)];
                this.loading = false;
            })
        } catch (error) {
            console.log(error);
            runInAction(()=> {
                this.loading = false;
            })
        }
    }

    private getCollection = (id: string) => {
        return this.collectionRegistry.get(id);
    }

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    }
}