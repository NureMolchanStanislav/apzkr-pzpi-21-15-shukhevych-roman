import { makeAutoObservable, runInAction } from 'mobx';
import agent from '../api/agent';

export default class StatisticsStore {
    comboStatistics = [];
    popularItems = [];
    seasonalItemUsage = [];
    loading = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadComboStatistics = async () => {
        this.loading = true;
        try {
            const stats = await agent.Statistics.getComboStatistics();
            runInAction(() => {
                this.comboStatistics = stats;
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to load combo statistics:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    loadPopularItems = async (brandId, startDate, endDate, topCount) => {
        this.loading = true;
        try {
            const stats = await agent.Statistics.getPopularItems(brandId, startDate, endDate, topCount);
            runInAction(() => {
                this.popularItems = stats;
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to load popular items:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    loadSeasonalItemUsage = async (brandId) => {
        this.loading = true;
        try {
            const stats = await agent.Statistics.getSeasonalItemUsage(brandId);
            runInAction(() => {
                this.seasonalItemUsage = stats;
                this.loading = false;
            });
        } catch (error) {
            console.error("Failed to load seasonal item usage:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };
}