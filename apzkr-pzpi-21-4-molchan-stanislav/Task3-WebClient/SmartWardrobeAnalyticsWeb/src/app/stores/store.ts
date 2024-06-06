import { createContext, useContext } from "react";
import CollectionStore from "./collectionStore";
import UserStore from "./userStore";
import CommonStore from "./commonStore";
import ItemStore from "./itemStore";
import BrandStore from "./brandStore";
import TagStore from "./tagStore";
import OfferStore from "./offerStore";
import StatisticsStore from "./statisticsStore";
import BonusStore from "./bonusStore";
import UsageStore from "./usageStore";

interface Store{
    collectionStore: CollectionStore,
    userStore: UserStore,
    commonStore: CommonStore,
    itemStore: ItemStore,
    brandStore: BrandStore,
    tagStore: TagStore,
    offerStore: OfferStore,
    statisticsStore: StatisticsStore,
    bonusStore: BonusStore,
    usageStore: UsageStore
}

export const store: Store = {
    collectionStore: new CollectionStore(),
    userStore: new UserStore(),
    commonStore: new CommonStore(),
    itemStore: new ItemStore(),
    brandStore: new BrandStore(),
    tagStore: new TagStore(),
    offerStore: new OfferStore(),
    statisticsStore: new StatisticsStore(),
    bonusStore: new BonusStore(),
    usageStore: new UsageStore()
}

export const StoreContext = createContext(store);

export function useStore(){
    return useContext(StoreContext)
}