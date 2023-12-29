import React, {FC, ReactNode, useEffect} from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider, useNavigate,} from "react-router-dom";
import Frontpage from "./pages/Frontpage.tsx";
import ErrorPage from "./error-page.tsx";
import Frame from "./Frame.tsx";
import UsersPage from "./pages/UsersPage.tsx";
import EventTypesPage from "./pages/EventTypesPage.tsx";
import EventsPage from "./pages/EventsPage.tsx";
import RecurrenceConfigurationsPage from "./pages/RecurrenceConfigurationsPage.tsx";
import {AuthControl, AuthProvider, useAuth} from "./authentication/UseAuthentication.tsx";
import {User} from "./model/User.ts";
import LoginPage from "./authentication/LoginPage.tsx";


export const ProtectedRoute: FC<{ children?: ReactNode }> = ({children}) => {
    const authMemory: AuthControl = useAuth();
    const navigate = useNavigate();
    let authUser: User | null = authMemory.user;

    if (!authUser) {
        console.log("This is a protected route - No user is logged in");
        useEffect(() => navigate("/Login", {replace: true}), [authUser]);
    }

    return children;
};


const router = createBrowserRouter([
    {
        path: "/Login",
        element: <AuthProvider> <LoginPage/> </AuthProvider>,
    },
    {
        path: "/",
        element: <AuthProvider> <ProtectedRoute> <Frame/> </ProtectedRoute> </AuthProvider>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/",
                element: <Frontpage/>,
                errorElement: <ErrorPage/>,
            },
            {
                path: "/users",
                element: <UsersPage/>,
            },
            {
                path: "/users/:userId",
                element: <UsersPage/>,
            },
            {
                path: "/recurrenceConfigurations",
                element: <RecurrenceConfigurationsPage/>,
            },
            {
                path: "/recurrenceConfigurations/:recurrenceConfigurationId",
                element: <RecurrenceConfigurationsPage/>,
            },
            {
                path: "/eventTypes",
                element: <EventTypesPage/>,
            },
            {
                path: "/eventTypes/:eventTypeId",
                element: <EventTypesPage/>,
            },
            {
                path: "/events",
                element: <EventsPage/>,
            },
            {
                path: "/events/:eventId",
                element: <EventsPage/>,
            }
        ]
    }
])


ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)
