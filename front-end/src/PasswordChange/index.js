import React, {useState} from 'react';
import {Col, Form, Row} from "react-bootstrap";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import Sidebar from "../SideBar";
import "./PasswordChange.css";
import jwt_decode from "jwt-decode";

const PasswordChange = () => {
    let errorCode = 0;
    const user = useUser();
    const emptyResponse = {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
    }
    const [response, setResponse]  = useState(emptyResponse);
    const [error, setError] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
    })
    let regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

    const handleSubmit = (e) => {
        e.preventDefault();
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/password-change`, "PUT", user.jwt, response)
            .then((response) => {
                if (!response.status) {
                    alert("Password changed successfully!")
                }
                else if(!response.ok) {
                    errorCode = response.status;
                    throw Error(response.status);
                }
            })
            .catch(() => {
                errorCode === 403 ? updateError("oldPassword", "Password doesn't match") : <></>
            });
    }
    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }
    function updateResponse(prop, value) {
        const newResponse = { ...response }
        newResponse[prop] = value;
        setResponse(newResponse);
        if (prop === "oldPassword") {
            updateError("oldPassword", "")
        }
    }

    function updateError(prop, value) {
        const newError = { ...error }
        newError[prop] = value;
        setError(newError);
    }

    const validateInput = e => {
        let {name, value} = e.target;
        setError((prev) => {
            const stateObj = {...prev, [name]: ""};

            switch (name) {
                case "oldPassword":
                    if(!value) {
                        stateObj[name] = "Please enter old Password";
                    }
                    break;
                case "newPassword":
                    if (!value) {
                        stateObj[name] = "Please enter Password.";
                    } else if (!regex.test(value)) {
                        stateObj[name] = "Password must contain one lowercase and uppercase character, a digit, a special character and a total of minimum 8 characters";
                    }
                    else if (response.confirmPassword && value !== response.confirmPassword) {
                        stateObj["confirmPassword"] = "Password and Confirm Password do not match.";
                    } else {
                        stateObj["confirmPassword"] = response.confirmPassword ? "" : error.confirmPassword;
                    }
                    break;

                case "confirmPassword":
                    if (!value) {
                        stateObj[name] = "Please enter Confirm Password.";
                    } else if (response.newPassword && value !== response.newPassword) {
                        stateObj[name] = "Password and Confirm Password do not match.";
                    }
                    break;

                default:
                    break;
            }

            return stateObj;
        });
    }
    return (
        <>
        <Sidebar/>
        <div className='password-change-container-1'>
            <form onSubmit={handleSubmit}>
                <Form.Group as={Row} className="mt-5 password-change-form-1" controlId="oldPassword" >
                    <Form.Label className='password-change-label-1' column sm="6" md="6">
                        Old Password
                    </Form.Label>
                    <p>
                        <Col>
                            <Form.Control
                                type="password"
                                onChange={(e) =>
                                    updateResponse("oldPassword", e.target.value)
                                }
                                className='password-change-control-1'
                            />
                            {error.oldPassword && <p className='err'>{error.oldPassword}</p>}
                        </Col>
                    </p>
                </Form.Group>

                <Form.Group as={Row} className="my-1 password-change-form-1" controlId="newPassword">
                    <Form.Label className='password-change-label-1' column sm="6" md="6">
                        New Password
                    </Form.Label>
                    <p>
                        <Col>
                            <Form.Control
                                type="password"
                                name="newPassword"
                                onBlur={(e) =>
                                    validateInput(e)}
                                onChange={(e) =>
                                    updateResponse("newPassword", e.target.value)
                                }
                                className='password-change-control-1'
                            />
                            {error.newPassword && <p className='err'>{error.newPassword}</p>}
                        </Col>
                    </p>
                </Form.Group>

                <Form.Group as={Row} className="my-1 password-change-form-1" controlId="confirmPassword">
                    <Form.Label className='password-change-label-1' column sm="6" md="6">
                        Confirm New Password
                    </Form.Label>
                    <p>
                        <Col>
                            <Form.Control
                                type="password"
                                name="confirmPassword"
                                onBlur={(e) =>
                                    validateInput(e)}
                                onChange={(e) =>
                                    updateResponse("confirmPassword", e.target.value)
                                }
                                className='password-change-control-1'
                                pattern={response.newPassword}
                            />
                            {error.confirmPassword && <p className='err'>{error.confirmPassword}</p>}
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
export default PasswordChange;