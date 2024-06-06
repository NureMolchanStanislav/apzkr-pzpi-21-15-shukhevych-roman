import { makeAutoObservable, runInAction } from 'mobx';
import agent from '../api/agent';
import { Bonus } from '../models/bonus';

export default class BonusStore {
    bonuses: Bonus[] = [];
    selectedBonus: Bonus | undefined = undefined;
    loading = false;
    loadingInitial = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadBonuses = async () => {
        this.setLoadingInitial(true);
        try {
            const bonuses = await agent.Bonuses.list();
            runInAction(() => {
                this.bonuses = bonuses;
                this.setLoadingInitial(false);
            });
        } catch (error) {
            console.error("Failed to load bonuses:", error);
            this.setLoadingInitial(false);
        }
    };

    loadBonus = async (id: string) => {
        this.setLoadingInitial(true);
        try {
            const bonus = await agent.Bonuses.details(id);
            runInAction(() => {
                this.selectedBonus = bonus;
                this.setLoadingInitial(false);
            });
            return bonus;
        } catch (error) {
            console.error("Failed to load bonus:", error);
            this.setLoadingInitial(false);
        }
    };

    createBonus = async (bonus: Bonus) => {
        this.loading = true;
        try {
            await agent.Bonuses.create(bonus);
            runInAction(() => {
                this.bonuses.push(bonus);
                this.selectedBonus = bonus;
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to create bonus:", error);
            this.loading = false;
        }
    };

    updateBonus = async (bonus: Bonus) => {
        this.loading = true;
        try {
            await agent.Bonuses.update(bonus);
            runInAction(() => {
                this.bonuses = [...this.bonuses.filter(b => b.id !== bonus.id), bonus];
                this.selectedBonus = bonus;
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to update bonus:", error);
            this.loading = false;
        }
    };

    deleteBonus = async (id: string) => {
        this.loading = true;
        try {
            await agent.Bonuses.delete(id);
            runInAction(() => {
                this.bonuses = this.bonuses.filter(b => b.id !== id);
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to delete bonus:", error);
            this.loading = false;
        }
    };

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    };
}
