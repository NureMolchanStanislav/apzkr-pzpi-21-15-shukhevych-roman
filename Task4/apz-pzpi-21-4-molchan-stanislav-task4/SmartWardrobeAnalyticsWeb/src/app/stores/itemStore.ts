import { makeAutoObservable, runInAction } from 'mobx';
import { Item } from '../models/item';
import agent from '../api/agent';

export default class ItemStore {
    items: Item[] = [];
    itemRegistry = new Map<string, Item>();
    selectedItem: Item | undefined = undefined;
    editMode = false;
    loading = false;
    loadingInitial = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadItems = async () => {
        this.setLoadingInitial(true);
        try {
            const items = await agent.Items.list();
            runInAction(() => {
                this.items = items;
            });
            this.setLoadingInitial(false);
        } catch (error) {
            console.error("Помилка завантаження елементів:", error);
            this.setLoadingInitial(false);
        }
    };

    loadItemForCollection = async (id: string) => {
        this.setLoadingInitial(true);
        try {
            const items = await agent.Items.listForCollection(id);
            console.log("ITEMS : " + items)
            runInAction(() => {
                this.items = items;
            });
            this.setLoadingInitial(false);
        } catch (error) {
            console.error("Помилка завантаження елементів:", error);
            this.setLoadingInitial(false);
        }
    };

    loadItem = async (id: string) => {
        this.setLoadingInitial(true);
        try {
            const item = await agent.Items.details(id);
            console.log("ITEM IN STORE", item)
            runInAction(() => {
                this.selectedItem = item;
            });
            this.setLoadingInitial(false);
            return item;
        } catch (error) {
            console.error("Помилка завантаження елемента:", error);
            this.setLoadingInitial(false);
        }
    };

    createItem = async (itemData: Item) => {
        this.loading = true;
        try {
            const item = await agent.Items.create(itemData);
            runInAction(() => {
                this.items.push(item);
                this.selectedItem = item;
                this.editMode = false;
                this.loading = false;
            });
        } catch (error) {
            console.error("Помилка створення елемента:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    updateItem = async (item: Item) => {
        this.loading = true;
        try {
            console.log("Відправка на оновлення елемента:", JSON.stringify(item));
            await agent.Items.update(item);
            runInAction(() => {
                this.items = [...this.items.filter(a => a.id !== item.id), item];
                this.selectedItem = item;
                this.editMode = false;
                this.loading = false;
            });
        } catch (error) {
            console.error("Помилка оновлення елемента:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };
    

    async getMonthlyItemUsageStatistics(itemId: string) {
        try {
          const statistics = await agent.Items.getMonthlyStatistics(itemId);
          console.error("СТАТИСТИКА:", statistics);
          return statistics;
        } catch (error) {
          console.error("Error getting monthly item usage statistics:", error);
          throw error;
        }
      }

    private getItem = (id: string) => {
        return this.itemRegistry.get(id);
    };

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    };

    getUsages = async (id: string) => {
        try {
            const usages = await agent.Items.getUsages(id);
            runInAction(() => {
                this.setLoadingInitial(false);
            });
            return usages;
        } catch (error) {
            runInAction(() => {
                this.setLoadingInitial(false);
            });
            console.error("Error getting item usages:", error);
            throw error;
        }
    };

    deleteItem = async (id: string) => {
        this.loading = true;
        try {
            await agent.Items.delete(id);
            runInAction(() => {
                this.items = [...this.items.filter(a => a.id !== id)];
                this.loading = false;
            })
        } catch (error) {
            console.log(error);
            runInAction(()=> {
                this.loading = false;
            })
        }
    }
}
