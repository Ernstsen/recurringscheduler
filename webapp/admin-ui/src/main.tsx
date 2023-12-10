import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import Frontpage from "./Frontpage.tsx";
import ErrorPage from "./error-page.tsx";


const router = createBrowserRouter([
    {
        path: "/",
        element: <Frontpage/>,
        errorElement: <ErrorPage />,
    },
    {
        path: "/console",
        element: <h1>Console</h1>
    }
])


ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>,
)
