import { createContext, useContext } from "react";
import CollectionStore from "./collectionStore";
import UserStore from "./userStore";
import CommonStore from "./commonStore";

interface Store{
    collectionStore: CollectionStore,
    userStore: UserStore,
    commonStore: CommonStore
}

export const store: Store = {
    collectionStore: new CollectionStore(),
    userStore: new UserStore(),
    commonStore: new CommonStore()
}

export const StoreContext = createContext(store);

export function useStore(){
    return useContext(StoreContext)
}