import React, { useState } from 'react';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';
import { Link } from 'react-router-dom';
import { SidebarData } from './SidebarData';
import './Sidebar.css';
import { IconContext } from 'react-icons';

function Sidebar() {
    const [sidebar, setSidebar] = useState(true);


    return (
        <>
            <IconContext.Provider value={{ color: '#fff' }}>
                <div className='navbar'></div>
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
            </IconContext.Provider>
        </>
    );
}

export default Sidebar;