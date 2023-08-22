import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {useUser} from "../UserProvider";
import {useNavigate} from "react-router-dom";

const DemoUser = () => {
    const user = useUser();
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorInfo, setErrorInfo] = useState("");

    // useEffect(() => {
    //     if (user.jwt) navigate("/dashboard");
    // }, [user]);
    function sendLoginRequest() {

        const reqBody = {
            email: email,
            password: password,
        };

        fetch("api/v1/registration/login", {
            headers: {
                "Content-type": "application/json"
            },
            method: "post",
            body: JSON.stringify(reqBody),
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
            .catch((message) => {
                //window.location.reload()
                setErrorInfo(message)
            });

    }

    return (
        <>
            <div className='wrapper bg-dark d-flex align-items-center justify-content-center w-100'>
                <div className='login'>
                    <h2 className='mb-3'>Demo User Login</h2>
                    <div className='row'>
                        <div className='col-6'>
                            Demo Admin
                        </div>
                        <div className='col-6'>
                            Demo Project Manager
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-6'>
                            Demo Developer
                        </div>
                        <div className='col-6'>
                            Demo Submitter
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default DemoUser;