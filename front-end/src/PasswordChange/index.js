import React, {useEffect, useState} from 'react';
import {Col, Form, Row} from "react-bootstrap";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {focus} from "@testing-library/user-event/dist/focus";

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

    const handleSubmit = (e) => {
        e.preventDefault();
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/password-change`, "PUT", user.jwt, response)
            .then((response) => {
                if (response === 0) {
                    updateError("oldPassword", "old password is incorrect");
                }
            });
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
                    } else if (response.newPassword && value !== response.newPassword) {
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
        if(error.newPassword) {
            console.log("baedbnvndbvs;ndbsnbesi;on");
        }
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/password-change`, "PUT", user.jwt, response)
            .then((response) => {
                if (response === 0) {
                    updateError("oldPassword", "old password is incorrect");
                }
            });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
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
                        {error.oldPassword && <p className='err'>{error.oldPassword}</p>}
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
                        {error.newPassword && <p className='err'>{error.newPassword}</p>}
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
                            pattern={response.newPassword}
                        />
                        {error.confirmPassword && <p className='err'>{error.confirmPassword}</p>}
                    </Col>
                </Form.Group>
                <button>Submit</button>
            </form>
        </div>

    );
};

export default PasswordChange;