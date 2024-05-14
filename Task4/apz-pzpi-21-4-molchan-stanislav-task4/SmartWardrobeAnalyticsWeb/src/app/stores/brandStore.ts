import { makeAutoObservable, runInAction } from 'mobx';
import { Brand } from '../models/brand';
import agent from '../api/agent';

export default class BrandStore {
    brands: Brand[] = [];
    brandRegistry = new Map<string, Brand>();
    selectedBrand: Brand | undefined = undefined;
    editMode = false;
    loading = false;
    loadingInitial = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadBrands = async () => {
        this.setLoadingInitial(true);
        try {
            const brands = await agent.Brands.list();
            runInAction(() => {
                this.brands = brands;
                brands.forEach((brand: Brand) => {
                    this.brandRegistry.set(brand.id, brand);
                });
            });
            this.setLoadingInitial(false);
        } catch (error) {
            console.error("Помилка завантаження брендів:", error);
            this.setLoadingInitial(false);
        }
    };

    loadForUserBrands = async () => {
        this.setLoadingInitial(true);
        try {
            const brands = await agent.Brands.listByUser();
            runInAction(() => {
                this.brands = brands;
                brands.forEach((brand: Brand) => {
                    this.brandRegistry.set(brand.id, brand);
                });
            });
            this.setLoadingInitial(false);
        } catch (error) {
            console.error("Помилка завантаження брендів:", error);
            this.setLoadingInitial(false);
        }
    };

    loadBrand = async (id: string) => {
        this.setLoadingInitial(true);
        try {
            const brand = await agent.Brands.details(id);
            runInAction(() => {
                this.selectedBrand = brand;
                this.brandRegistry.set(brand.id, brand);
            });
            this.setLoadingInitial(false);
            return brand;
        } catch (error) {
            console.error("Помилка завантаження бренду:", error);
            this.setLoadingInitial(false);
        }
    };

    createBrand = async (brandData: Brand) => {
        this.loading = true;
        try {
            const brand = await agent.Brands.create(brandData);
            runInAction(() => {
                this.brands.push(brand);
                this.selectedBrand = brand;
                this.editMode = false;
                this.loading = false;
            });
        } catch (error) {
            console.error("Помилка створення бренду:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    updateBrand = async (brand: Brand) => {
        this.loading = true;
        try {
            console.log("Відправка на оновлення бренду:", JSON.stringify(brand));
            await agent.Brands.update(brand);
            runInAction(() => {
                this.brands = [...this.brands.filter(b => b.id !== brand.id), brand];
                this.selectedBrand = brand;
                this.editMode = false;
                this.loading = false;
            });
        } catch (error) {
            console.error("Помилка оновлення бренду:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    deleteBrand = async (id: string) => {
        this.loading = true;
        try {
            await agent.Brands.delete(id);
            runInAction(() => {
                this.brands = this.brands.filter(b => b.id !== id);
                this.loading = false;
            });
        } catch (error) {
            console.error("Error deleting brand:", error);
            runInAction(() => {
                this.loading = false;
            });
        }
    };

    private getBrand = (id: string) => {
        return this.brandRegistry.get(id);
    };

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    };
}
