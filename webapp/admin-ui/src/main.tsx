import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import Frontpage from "./pages/Frontpage.tsx";
import ErrorPage from "./error-page.tsx";
import Frame from "./Frame.tsx";
import UsersPage from "./pages/UsersPage.tsx";
import EventTypesPage from "./pages/EventTypesPage.tsx";
import EventsPage from "./pages/EventsPage.tsx";
import RecurrenceConfigurationsPage from "./pages/RecurrenceConfigurationsPage.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Frame/>,
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
