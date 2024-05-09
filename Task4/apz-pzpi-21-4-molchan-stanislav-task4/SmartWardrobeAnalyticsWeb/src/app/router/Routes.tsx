import { RouteObject, createBrowserRouter } from "react-router-dom";
import LoginForm from "../../features/users/identity/LoginForm";
import HomePage from "../../features/home/HomePage";
import App from "../layout/App";
import CollectionDashboard from "../../features/collections/dashboard/CollectionDashboard";
import RegisterForm from "../../features/users/identity/RegisterForm";

export const routes: RouteObject[] = [
    {
        path: '/',
        element: <App />,
        children: [
            {path: '', element: <HomePage />},
            {path: 'collections', element: <CollectionDashboard />},
            {path: 'login', element: <LoginForm />},
            {path: 'register', element: <RegisterForm />},
        ]
    }
]

export const router = createBrowserRouter(routes);