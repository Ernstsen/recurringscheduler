import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import Frontpage from "./pages/Frontpage.tsx";
import ErrorPage from "./error-page.tsx";
import Console from "./pages/Console.tsx";
import Frame from "./Frame.tsx";
import Users from "./pages/UsersPage.tsx";

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
                path: "/console",
                element: <Console/>,
            },
            {
                path: "/users",
                element: <Users/>,
            },
            {
                path: "/users/:userId",
                element: <Users/>,
            }
        ]
    }
])


ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)
