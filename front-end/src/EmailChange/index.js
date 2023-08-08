import React, {useEffect, useState} from 'react';
import {Col, Form, Row} from "react-bootstrap";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {focus} from "@testing-library/user-event/dist/focus";
import Sidebar from "../SideBar";
import jwt_decode from "jwt-decode";
import axios from "axios";

const EmailChange = () => {
    const user = useUser();
    const emptyResponse = {
        oldEmail: "",
        newEmail: "",
        password: ""
    }
    const [response, setResponse]  = useState(emptyResponse);
    const [error, setError] = useState({
        oldEmail: "",
        newEmail: "",
        password: ""
    })
    let regex = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
    // const handleSubmit = (e) => {
    //     e.preventDefault();
    //     grabAndAuthorizeRequestFromTheServer(`/api/v1/user/email-change`, "PUT", user.jwt, response)
    //         .then((response) => {
    //             if (response === 0) {
    //                 updateError("oldPassword", "old password is incorrect");
    //             }
    //             else {
    //                 alert("Password changed successfully!")
    //             }
    //         });
    // }

    // function handleSubmit() {
    //     grabAndAuthorizeRequestFromTheServer("api/v1/user/email-change", "PUT", user.jwt, response)
    //         .then(answer => {
    //             console.log(answer);
    //         })
    //         .catch(error => {
    //             console.log(error);
    //         })
    // }
    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.sub;
        }
        return "null";
    }
    function updateResponse(prop, value) {
        const newResponse = { ...response }
        newResponse[prop] = value;
        setResponse(newResponse);
    }

    function updateError(prop, value) {
        const newError = { ...error }
        newError[prop] = value;
        setError(newError);
    }

    const validateInput = e => {
        let {name, value} = e.target;
        console.log(error);
        setError((prev) => {
            const stateObj = {...prev, [name]: ""};
            switch (name) {
                case "oldEmail":
                    console.log(value);
                    if(value !== getEmailFromJWT()) {
                        stateObj[name] = "That is not your current email";
                    }
                    break;
                case "newEmail":
                    if (!value) {
                        stateObj[name] = "Please enter new Email.";
                    }
                    else if (!regex.test(value)) {
                        stateObj[name] = "Please provide a correct Email address";
                    }
                    break;

                case "password":
                    if (!value) {
                        stateObj[name] = "Please enter Password.";
                    }
                    break;

                default:
                    break;
            }

            return stateObj;
        });
    }


    const handleSubmit = (e) => {
        e.preventDefault();
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/email-change`, "PUT", user.jwt, response)
            .then((response) => {
                if (response === 0) {
                    updateError("newEmail", "This email address is taken");
                }
                else if (response === -1) {
                    updateError("password", "Password is not correct");
                }
                else {
                    user.jwt = "";
                    window.location.href = "/login";
                    alert("Email changed successfully. Please use your new email to log in");
                }
            });
    }
    // function submit() {
    //     grabAndAuthorizeRequestFromTheServer(`/api/v1/user/password-change`, "PUT", user.jwt, response)
    //         .then((response) => {
    //             if (response === 0) {
    //                 updateError("oldPassword", "old password is incorrect");
    //             }
    //             else {
    //                 alert("Password changed succesfully!")
    //             }
    //         });
    // }

    return (
        <>
            <Sidebar/>
            <div className='password-change-container-1'>
                <form onSubmit={handleSubmit}>
                    <Form.Group as={Row} className="mt-5 password-change-form-1" controlId="oldEmail" >
                        <Form.Label className='password-change-label-1' column sm="6" md="6">
                            Old Email
                        </Form.Label>
                        <p>
                            <Col>
                                <Form.Control
                                    type="email"
                                    name="oldEmail"
                                    onBlur={(e) =>
                                        validateInput(e)}
                                    onChange={(e) =>
                                        updateResponse("oldEmail", e.target.value)
                                    }
                                    className='password-change-control-1'
                                />
                                {error.oldEmail && <p className='err'>{error.oldEmail}</p>}
                            </Col>
                        </p>
                    </Form.Group>

                    <Form.Group as={Row} className="my-1 password-change-form-1" controlId="newEmail">
                        <Form.Label className='password-change-label-1' column sm="6" md="6">
                            New Email
                        </Form.Label>
                        <p>
                            <Col>
                                <Form.Control
                                    type="text"
                                    name="newEmail"
                                    onBlur={(e) =>
                                        validateInput(e)}
                                    onChange={(e) =>
                                        updateResponse("newEmail", e.target.value)
                                    }
                                    className='password-change-control-1'
                                />
                                {error.newEmail && <p className='err'>{error.newEmail}</p>}
                            </Col>
                        </p>
                    </Form.Group>

                    <Form.Group as={Row} className="my-1 password-change-form-1" controlId="password">
                        <Form.Label className='password-change-label-1' column sm="6" md="6">
                            Password
                        </Form.Label>
                        <p>
                            <Col>
                                <Form.Control
                                    type="password"
                                    name="password"
                                    onBlur={(e) =>
                                        validateInput(e)}
                                    onChange={(e) =>
                                        updateResponse("password", e.target.value)
                                    }
                                    className='password-change-control-1'
                                    pattern={response.password}
                                />
                                {error.password && <p className='err'>{error.password}</p>}
                            </Col>
                        </p>
                    </Form.Group>
                    <button className='password-change-button-1'>Submit</button>
                </form>
            </div>
        </>

    );
};

export default EmailChange;