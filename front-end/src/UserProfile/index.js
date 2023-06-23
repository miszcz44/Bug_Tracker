import React, {useEffect, useState} from 'react';
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import jwt_decode from "jwt-decode";
import FormInput from "../Registration/components/FormInput";
import {Col, Form, Row} from "react-bootstrap";

const UserProfile = () => {

    const user = useUser();
    const [appUser, setAppUser] = useState({});
    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            return decodedJwt.sub;
        }
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/${getEmailFromJWT()}`, "GET", user.jwt)
            .then((userResponse) => {
                setAppUser(userResponse);
            });
    }, []);

    function updateAppUser(prop, value) {
        const newAppUser = { ...appUser }
        newAppUser[prop] = value;
        setAppUser(newAppUser);
    }

    function saveUser() {
        grabAndAuthorizeRequestFromTheServer()
    }

    return (
        <div>
            {appUser ? (
                <>
                <Form.Group as={Row} className="my-3" controlId="firstName">
                    <Form.Label column sm="3" md="2">
                        first name
                    </Form.Label>
                    <Col sm="9" md="8" lg="6">
                        <Form.Control
                            onChange={(e) =>
                                updateAppUser("firstName", e.target.value)
                            }
                            type="text"
                            value={appUser.firstName}
                            placeholder="first name"
                        />
                    </Col>
                </Form.Group>

                    <Form.Group as={Row} className="my-3" controlId="lastName">
                        <Form.Label column sm="3" md="2">
                            last name
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Form.Control
                                onChange={(e) =>
                                    updateAppUser("lastName", e.target.value)
                                }
                                type="text"
                                value={appUser.lastName}
                                placeholder="last name"
                            />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="my-3" controlId="email">
                        <Form.Label column sm="3" md="2">
                            email
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Form.Control
                                disabled
                                type="text"
                                value={appUser.email}
                                placeholder="email"
                            />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row} className="my-3" controlId="role">
                        <Form.Label column sm="3" md="2">
                            role
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Form.Control
                                disabled
                                type="text"
                                value={appUser.appUserRole.name}
                                placeholder="role"
                            />
                        </Col>
                    </Form.Group>
                    <button onClick={() => saveUser()}>save</button>

                </>
            ) : (<></>)
            }
        </div>
    );
};

export default UserProfile;