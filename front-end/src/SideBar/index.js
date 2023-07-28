import React, { useState } from 'react';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';
import { Link } from 'react-router-dom';
import { SidebarData } from './SidebarData';
import './Sidebar.css';
import { IconContext } from 'react-icons';
import {useUser} from "../UserProvider";
import jwt_decode from "jwt-decode";

function Sidebar() {

    const user = useUser();

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role.authority;
        }
    }


    return (
        <>
            <div className='navbar'>
                <p className='sidebar-p'>Logged in as: {getRoleFromJWT().toLowerCase()}</p>
                <Link className='navbar-link' to={'/login'} onClick={() => user.jwt = ""}>Log out</Link>
            </div>
                <nav className='nav-menu active' >
                    <ul className='nav-menu-items'>
                        <h1 className='sidebar-h1'>Bug Tracker</h1>
                        {SidebarData.map((item, index) => {
                            return (
                                <div className='list-element'>
                                    <ul key={index} className={item.cName}>
                                        <Link className='sidebar-link' to={item.path}>
                                            {item.icon}
                                            <span className='sidebar-span'>{item.title}</span>
                                        </Link>
                                    </ul>
                                </div>
                            );
                        })}
                    </ul>
                </nav>
        </>
    );
}

export default Sidebar;