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

    private getCollection = (id: string) => {
        return this.collectionRegistry.get(id);
    }

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    }
}