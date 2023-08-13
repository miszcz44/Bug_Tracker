import React from "react";
import { createRoot } from "react-dom/client";
import {
    createBrowserRouter,
    RouterProvider,
    Route,
    Link,
    Outlet,
    createRoutesFromElements,
} from "react-router-dom";

import Sidebar from "./index.js";
import * as AiIcons from 'react-icons/ai';
import * as BsIcons from 'react-icons/bs'
// import "./App.css";
import UserProfile from "../UserProfile";
import RoleManagement from "../RoleManagement";
import Dashboard from "../ProjectManagerViewDashboard";
import jwt_decode from "jwt-decode";
import {useUser} from "../UserProvider";



export const AdminSidebarData = [
    {
        title: 'Dashboard home',
        path: '/dashboard',
        icon: <AiIcons.AiFillHome />,
        cname: 'nav-text'
    },
    {
        title: 'Manage role assignment',
        path: '/user-management',
        icon: <BsIcons.BsFillPeopleFill />,
        cname: 'nav-text'
    },
    {
        title: 'My Projects',
        path: '/projects',
        icon: <BsIcons.BsList />,
        cname: 'nav-text'
    },
    {
        title: 'My Tickets',
        path: '/tickets',
        icon: <BsIcons.BsListUl />,
        cname: 'nav-text'
    },
    {
        title: 'User Profile',
        path: '/user-profile',
        icon: <AiIcons.AiOutlineUser />,
        cname: 'nav-text'
    }
]