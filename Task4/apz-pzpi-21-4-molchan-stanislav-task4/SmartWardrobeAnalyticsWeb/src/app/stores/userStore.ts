import agent from "../api/agent";
import {makeAutoObservable, runInAction} from 'mobx'
import { store } from "./store";
import { User, UserFormValues, UserList } from "../models/user";
import { router } from "../router/Routes";
import { Token } from "../models/identity";

export default class UserStore {
    user: User | null = null;
    users: UserList[] = [];
    token: Token | null = null;
    loading = false;
    loadingInitial = false;

    constructor(){
        makeAutoObservable(this)
    }

    get isLoggedIn(){
        return !! this.user;
    }

    login = async (creds: UserFormValues) => {
      try {
          const token = await agent.Account.login(creds);
          console.log("Token received:", token.accessToken);
          store.commonStore.setToken(token.accessToken);
  
          await this.getUser();
          
          runInAction(() => {
            if (this.user?.roles.some(role => role.name === 'Business')) {
                router.navigate('/brands');
            } else {
                router.navigate('/collections');
            }
        });
      } catch (error) {
          console.error("Login error:", error);
          throw error;
      }
  }

    
    register = async (creds: UserFormValues) =>{
        // eslint-disable-next-line no-useless-catch
        try{
            const user = await agent.Account.register(creds);
            runInAction(()=>{
                this.user = user;
                user.roles = user.roles || [];
                user.roles.push("User")
            })
            console.log(user);
        } catch (error) {
            throw error;
        }

    }

    loadUsers = async () => {
        this.setLoadingInitial(true);
        try{
            const usersLocal = await agent.Users.list();
            runInAction(()=>{
                this.users = usersLocal;
            })
            console.log("ARR:", this.users);
            this.setLoadingInitial(false);
        } catch (error) {
            console.log(error);
            this.setLoadingInitial(false);
        }
    }

    ban = async (id: string) => {
        try {
          await agent.Account.ban(id);
          runInAction(() => {
            const updatedUsers = this.users.map((user) =>
              user.id === id ? { ...user, isDeleted: true } : user
            );
            this.users = updatedUsers;
          });
        } catch (error) {
          console.error("Error banning user", error);
        }
      };

      unban = async (id: string) => {
        try {
          await agent.Account.unban(id);
          runInAction(() => {
            const updatedUsers = this.users.map((user) =>
              user.id === id ? { ...user, isDeleted: false } : user
            );
            this.users = updatedUsers;
          });
        } catch (error) {
          console.error("Error banning user", error);
        }
      };

    setLoadingInitial = (state: boolean) => {
        this.loadingInitial = state;
    }

    logout = () => {
        store.commonStore.setToken(null);
        this.user = null;
    }

    getCurrentUserId = (): string | null => {
        return this.user?.id ?? null;
      };

    getUser = async () => {
        try{
            const user = await agent.Account.current();
            this.user = user;
            runInAction(() => this.user = user)
            console.log("Current user:" + this.user?.email)
        }catch (error){
            console.log(error);
        }
    }
}