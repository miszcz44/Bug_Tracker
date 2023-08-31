import React, {useEffect, useState} from 'react';
import "./demo.css";
import { adminData, projectManagerData, developerData, submitterData } from "./demoUserData";
import * as FaIcons from 'react-icons/fa';
import * as BsIcons from 'react-icons/bs'
import {useLocalState} from "../util/useLocalStorage";
import {useUser} from "../UserProvider";
import {useNavigate} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";

const DemoUser = () => {

    const user = useUser();
    function login(id) {
        let data;
        {
            id === "admin" ?
                data = adminData
                :
                id === "project manager" ?
                    data = projectManagerData
                    :
                    id === "developer" ?
                        data = developerData
                        :
                        data = submitterData
        }
        fetch("api/v1/registration/login", {
            headers: {
                "Content-type": "application/json"
            },
            method: "post",
            body: JSON.stringify(data),
        })
            .then((response) => {
                if(response.status === 200)
                    return Promise.all([response.json(), response.headers]);
                else return Promise.reject("Invalid login attempt");
            })
            .then(([body, headers]) => {
                console.log(headers);
                user.setJwt(headers.get("authorization"));
                console.log(user.jwt);
                window.location.href = "dashboard";
            })
    }

    return (
        <>
            <div className='wrapper bg-dark d-flex align-items-center justify-content-center w-100'>
                <div className='login'>
                    <h2 className='mb-2'>Demo User Login</h2>
                    <p style={{fontSize: "18px"}}>Log in as:</p>
                    <div className='row'>
                        <div className='col-6'>
                            <button type="button" className='demo-button-1 btn btn-primary' onClick={() => login("admin")}>
                                 Demo Administrator
                            </button>
                        </div>
                        <div className='col-6'>
                            <button type="button" className='demo-button-1 btn btn-secondary' onClick={() => login("project manager")}>
                                 Demo Project Manager
                            </button>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-6'>
                            <button type="button" className='demo-button-1 btn btn-success' onClick={() => login("developer")}>
                                 Demo Developer
                            </button>
                        </div>
                        <div className='col-6'>
                            <button type="button" className='demo-button-1 btn btn-danger' onClick={() => login("submitter")}>
                                 Demo Submitter
                            </button>
                        </div>
                    </div>
                    <button id="submit" type="button" className='btn btn-warning w-100 mt-2' onClick={() => window.location.href = "/login"}>
                        <b>Go back</b>
                    </button>
                </div>
            </div>
        </>
    );
};

export default DemoUser;