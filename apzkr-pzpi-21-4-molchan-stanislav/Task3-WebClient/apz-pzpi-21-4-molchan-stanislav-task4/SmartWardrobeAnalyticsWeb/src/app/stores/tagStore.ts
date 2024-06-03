import { makeAutoObservable, runInAction } from 'mobx';
import agent from '../api/agent';
import { Tag } from '../models/tag';

export default class TagStore {
    tags: Tag[] = [];
    loading = false;
    loadingInitial = false;

    constructor() {
        makeAutoObservable(this);
    }

    loadTags = async () => {
        this.loadingInitial = true;
        try {
            const tags = await agent.Tags.list();
            runInAction(() => {
                this.tags = tags;
                this.loadingInitial = false;
            });
        } catch (error) {
            console.error("Error loading tags:", error);
            this.loadingInitial = false;
        }
    };

    updateTagWithNewItem = async (tagId: string, newItemId: string) => {
        try {
            await agent.Tags.update(tagId, newItemId);  // Pass as two arguments
            runInAction(() => {
                const index = this.tags.findIndex(tag => tag.id === tagId);
                if (index !== -1) {
                    this.tags[index].itemId = newItemId;
                }
            });
        } catch (error) {
            console.error("Error updating tag:", error);
        }
    };
}
