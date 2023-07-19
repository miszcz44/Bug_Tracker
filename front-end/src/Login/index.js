import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {useUser} from "../UserProvider";
import {useNavigate} from "react-router-dom";
import './LoginForm.css'

const Login = () => {
    const user = useUser();
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

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
                alert(message)
            });

    }

    return (
        <>
            <div className='wrapper bg-dark d-flex align-items-center justify-content-center w-100'>
                <div className='login'>
                    <h2 className='mb-3'>Login Form</h2>
                    <form className='needs-Validation'>
                        <div className='form-group was-validated mb-2'>
                            <label htmlFor="email" className='form-label'>Username</label>
                            <input type="email" id="email" className='form-control' value={email} onChange={(e) => setEmail(e.target.value)}/>
                            <div className='invalid-feedback'>
                                Please Enter your email
                            </div>
                        </div>
                        <div className='form-group mb-2'>
                            <label htmlFor="password" className='form-label'>Password</label>
                            <input type="password" id="password" className='form-control' value={password} onChange={(e) => setPassword(e.target.value)}/>
                        </div>
                        <div>
                            <button id="submit" type="button" className='btn btn-success w-100 mt-2' onClick={() => sendLoginRequest()}>
                                Sign in
                            </button>
                        </div>
                    </form>
                </div>
            </div>




        </>
    );
};

export default Login;