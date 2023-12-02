import React from "react";
import * as AiIcons from 'react-icons/ai';
import * as BsIcons from 'react-icons/bs'
export const SidebarData = [
    {
        title: 'Dashboard home',
        path: '/dashboard',
        icon: <AiIcons.AiFillHome />,
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