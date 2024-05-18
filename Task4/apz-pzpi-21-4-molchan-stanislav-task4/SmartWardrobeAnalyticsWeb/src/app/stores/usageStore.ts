import { makeAutoObservable, runInAction } from 'mobx';
import agent from '../api/agent';
import { Usage } from '../models/usage';

export default class UsageStore {
    usages: Usage[] = [];
    selectedUsage: Usage | undefined = undefined;
    loading = false;
    loadingInitial = false;
    totalPages = 0;
    pageNumber = 1;
    pageSize = 20;

    constructor() {
        makeAutoObservable(this);
    }

    loadUsages = async () => {
        this.setLoadingInitial(true);
        try {
            const result = await agent.Usages.list(this.pageNumber, this.pageSize);
            runInAction(() => {
                this.usages = result.items;
                this.totalPages = result.totalPages;
                this.setLoadingInitial(false);
            });
        } catch (error) {
            console.error("Failed to load usages:", error);
            runInAction(() => {
                this.setLoadingInitial(false);
            });
        }
    };

    loadUsage = async (id: string) => {
        this.setLoadingInitial(true);
        try {
            const usage = await agent.Usages.details(id);
            runInAction(() => {
                this.selectedUsage = usage;
                this.setLoadingInitial(false);
            });
            return usage;
        } catch (error) {
            console.error("Failed to load usage:", error);
            runInAction(() => {
                this.setLoadingInitial(false);
            });
        }
    };

    createUsage = async (usage: Usage) => {
        this.loading = true;
        try {
            await agent.Usages.create(usage);
            runInAction(() => {
                this.usages.push(usage);
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to create usage:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    updateUsage = async (usage: Usage) => {
        this.loading = true;
        try {
            await agent.Usages.update(usage);
            runInAction(() => {
                this.usages = this.usages.map(u => u.id === usage.id ? usage : u);
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to update usage:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    deleteUsage = async (id: string) => {
        this.loading = true;
        try {
            await agent.Usages.delete(id);
            runInAction(() => {
                this.usages = this.usages.filter(u => u.id !== id);
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to delete usage:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    setPageNumber = (pageNumber: number) => {
        this.pageNumber = pageNumber;
        this.loadUsages();
    };

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    };
}
