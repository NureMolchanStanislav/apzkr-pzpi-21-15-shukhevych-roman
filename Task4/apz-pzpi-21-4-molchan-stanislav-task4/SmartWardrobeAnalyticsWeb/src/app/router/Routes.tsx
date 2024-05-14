import { RouteObject, createBrowserRouter } from "react-router-dom";
import LoginForm from "../../features/users/identity/LoginForm";
import HomePage from "../../features/home/HomePage";
import App from "../layout/App";
import CollectionDashboard from "../../features/collections/dashboard/CollectionDashboard";
import RegisterForm from "../../features/users/identity/RegisterForm";
import CollectionForm from "../../features/collections/form/CollectionForm";
import ItemDashboard from "../../features/items/ItemDashboard";
import ItemDetails from "../../features/items/details/ItemDetails";
import ItemForm from "../../features/items/form/ItemForm";
import UserProfileForm from "../../features/users/form/UserProfileForm";
import OfferList from "../../features/offers/list/OfferList";
import BrandDashboard from "../../features/brands/list/BrandDashboard";
import ComboStatistics from "../../features/statistics/ComboStatistics";
import PopularItemsStatistics from "../../features/statistics/PopularItemsStatistics";
import SeasonalItemUsageStatistics from "../../features/statistics/SeasonalItemUsageStatistics";
import BrandForm from "../../features/brands/form/BrandForm";
import BonusDashboard from "../../features/brandBonuses/list/BonusDashboard";
import BonusForm from "../../features/brandBonuses/form/BonusForm";

export const routes: RouteObject[] = [
    {
        path: '/',
        element: <App />,
        children: [
            {path: '', element: <HomePage />},
            {path: 'collections', element: <CollectionDashboard />},
            {path: 'collections/:id', element: <ItemDashboard />},
            {path: 'item/:id', element: <ItemDetails />},
            {path: 'update/item/:id', element: <ItemForm />},
            {path: 'create/item', element: <ItemForm />},
            {path: 'login', element: <LoginForm />},
            {path: 'register', element: <RegisterForm />},
            {path: 'user/profile/:id', element: <UserProfileForm />},
            {path: 'user/offers', element: <OfferList />},
            {path: 'update/collections/:id', element: <CollectionForm />},
            {path: 'collections/create', element: <CollectionForm/>},

            //Brands

            {path: 'brands', element: <BrandDashboard />},
            {path: 'update/brand/:id', element: <BrandForm />},
            {path: 'create/brand', element: <BrandForm />},

            //BrandsBonuses

            {path: 'bonus-system', element: <BonusDashboard/>},
            {path: 'update/bonus/:id', element: <BonusForm/>},
            {path: 'create/bonus', element: <BonusForm/>},

            //Statistics

            { path: "/statistics/combo", element: <ComboStatistics/>},
            { path: "/statistics/top", element: <PopularItemsStatistics/>},
            { path: "/statistics/seasonal", element: <SeasonalItemUsageStatistics/>}
        ]
    }
]

export const router = createBrowserRouter(routes);