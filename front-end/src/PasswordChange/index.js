import React, {useEffect, useState} from 'react';
import {Col, Form, Row} from "react-bootstrap";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";

const PasswordChange = () => {
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
                case "newPassword":
                    if (!value) {
                        stateObj[name] = "Please enter Password.";
                    } else if (response.confirmPassword && value !== response.confirmPassword) {
                        stateObj["confirmPassword"] = "Password and Confirm Password does not match.";
                    } else {
                        stateObj["confirmPassword"] = response.confirmPassword ? "" : error.confirmPassword;
                    }
                    break;

                case "confirmPassword":
                    if (!value) {
                        stateObj[name] = "Please enter Confirm Password.";
                    } else if (response.password && value !== response.password) {
                        stateObj[name] = "Password and Confirm Password does not match.";
                    }
                    break;

                default:
                    break;
            }

            return stateObj;
        });
    }

    function submit() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/password-change`, "PUT", user.jwt, response)
            .then((response) => {
                if (response === 0) {
                    updateError("oldPassword", "old password is incorrect");
                }
            });
    }

    return (
        <div>
            <Form.Group as={Row} className="my-3" controlId="oldPassword">
                <Form.Label column sm="3" md="2">
                    old password
                </Form.Label>
                <Col sm="9" md="8" lg="6">
                    <Form.Control
                        type="password"
                        onChange={(e) =>
                            updateResponse("oldPassword", e.target.value)
                        }
                    />
                    {error.oldPassword && <span className='err'>{error.oldPassword}</span>}
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="my-3" controlId="newPassword">
                <Form.Label column sm="3" md="2">
                    new password
                </Form.Label>
                <Col sm="9" md="8" lg="6">
                    <Form.Control
                        type="password"
                        name="newPassword"
                        onBlur={(e) =>
                            validateInput(e)}
                        onChange={(e) =>
                            updateResponse("newPassword", e.target.value)
                        }
                    />
                    {error.newPassword && <span className='err'>{error.newPassword}</span>}
                </Col>
            </Form.Group>

            <Form.Group as={Row} className="my-3" controlId="confirmPassword">
                <Form.Label column sm="3" md="2">
                    confirm new password
                </Form.Label>
                <Col sm="9" md="8" lg="6">
                    <Form.Control
                        type="password"
                        name="confirmPassword"
                        onBlur={(e) =>
                            validateInput(e)}
                        onChange={(e) =>
                            updateResponse("confirmPassword", e.target.value)
                        }
                    />
                    {error.confirmPassword && <span className='err'>{error.confirmPassword}</span>}
                </Col>
            </Form.Group>
            <button onClick={() => submit()}>Submit</button>
        </div>

    );
};

export default PasswordChange;