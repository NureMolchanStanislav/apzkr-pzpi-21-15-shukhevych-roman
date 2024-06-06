import { makeAutoObservable, runInAction } from 'mobx';
import agent from '../api/agent';
import { Offer } from '../models/offer';

export default class OfferStore {
    offers: Offer[] = [];
    loadingInitial = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadOffers = async () => {
        this.loadingInitial = true;
        try {
            const offers = await agent.Offers.list();
            runInAction(() => {
                this.offers = offers.map(offer => ({
                    discount: offer.discount,
                    brandName: offer.brandName
                }));
                this.loadingInitial = false;
            });
        } catch (error) {
            console.error("Error loading offers:", error);
            runInAction(() => {
                this.loadingInitial = false;
            });
        }
    };
}
