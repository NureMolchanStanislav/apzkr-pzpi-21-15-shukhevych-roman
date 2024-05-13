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
            {path: 'collections/create', element: <CollectionForm/>}
        ]
    }
]

export const router = createBrowserRouter(routes);