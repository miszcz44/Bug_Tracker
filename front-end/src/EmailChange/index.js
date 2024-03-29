import React, {useState} from 'react';
import {Col, Form, Row} from "react-bootstrap";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import Sidebar from "../SideBar";
import jwt_decode from "jwt-decode";

const EmailChange = () => {
    const user = useUser();
    let errorCode = 0;
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
    let regex = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
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

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }

    const validateInput = e => {
        let {name, value} = e.target;
        setError((prev) => {
            const stateObj = {...prev, [name]: ""};
            switch (name) {
                case "oldEmail":
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
                if (!response.status) {
                    alert("Email changed successfully!")
                }
                else if(!response.ok) {
                    errorCode = response.status;
                    throw Error(response.status);
                }
            })
            .catch(() => {
                errorCode === 409 ? updateError("newEmail", "Email already taken") :
                    errorCode === 403 ? updateError("password", "Password doesn't match") : <></>
            });
    }
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
                    {
                        getRoleFromJWT().startsWith("DEMO") ?
                            <button disabled className='password-change-button-2'>Submit</button>
                            :
                            <button className='password-change-button-1'>Submit</button>
                    }

                </form>
            </div>
        </>

    );
};
export default EmailChange;