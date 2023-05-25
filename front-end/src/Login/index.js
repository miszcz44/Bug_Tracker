import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {useUser} from "../UserProvider";
import {useNavigate} from "react-router-dom";

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
            <div>
                <label htmlFor="email">Username</label>
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
            </div>

            <div>
                <label htmlFor="password">Password</label>
                <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            </div>

            <div>
                <button id="submit" type="button" onClick={() => sendLoginRequest()} />
            </div>
        </>
    );
};

export default Login;