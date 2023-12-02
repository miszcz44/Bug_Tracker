import React, {useEffect, useState} from "react";
import "./registration.css";
import FormInput from "./components/FormInput";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import axios from 'axios';
import {ButtonGroup, Col, Dropdown, DropdownButton, Form, Row} from "react-bootstrap";
import {useUser} from "../UserProvider";
const Registration = () => {
    const user = useUser();
    const [values, setValues] = useState({
        firstName: "",
        lastName: "",
        email: "",
        role: "",
        password: "",
        confirmPassword: "",
    });
    const [selectedRole, setSelectedRole] = useState("");
    const [userRoles, setUserRoles] = useState([]);

    const inputs = [
        {
            id: 1,
            name: "firstName",
            type: "text",
            placeholder: "first name",
            errorMessage:
                "First name should have at least 1 character and no more than 40 characters",
            label: "first name",
            pattern: "^{1,40}$",
            required: true,
        },
        {
            id: 2,
            name: "lastName",
            type: "text",
            placeholder: "last name",
            errorMessage:
                "Last name should have at least 1 character and no more than 40 characters",
            label: "last name",
            pattern: "{1,40}$",
            required: true,
        },
        {
            id: 3,
            name: "email",
            type: "email",
            placeholder: "Email",
            errorMessage: "It should be a valid email address!",
            label: "Email",
            required: true,
        },
        {
            id: 4,
            name: "password",
            type: "password",
            placeholder: "Password",
            errorMessage:
                "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
            label: "Password",
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*.])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
            required: true,
        },
        {
            id: 5,
            name: "confirmPassword",
            type: "password",
            placeholder: "Confirm Password",
            errorMessage: "Passwords don't match!",
            label: "Confirm Password",
            pattern: values.password,
            required: true,
        },
    ];

    const registerUser = async (userData) => {
        try {
            await axios.post('/api/v1/registration', userData);
            window.location.href = "/login";
            // handle the successful response
        } catch (error) {
            if (error.response && error.response.status === 400) {
                // handle the bad request error
                console.error(error.response.data.message);
                alert(error.response.data.message)
                // display an error message to the user using a notification or alert component
            } else {
                // handle other errors
                console.error(error.message);
                // display a generic error message to the user using a notification or alert component
            }
        }
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        registerUser(values);
    }

    function updateSelectedElement(element) {
        setSelectedRole(element);
        setValues({...values, ["role"]: element})
    }

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/roles`, "GET", user.jwt)
            .then((roles) => {
                setUserRoles(roles);
            });
    }, []);

    return (
        <div className='registration'>
            <form className='register-form' onSubmit={handleSubmit}>
                <h1 className='register-h1'>Register</h1>
                {inputs.map((input) => (
                    <FormInput
                        key={input.id}
                        {...input}
                        value={values[input.name]}
                        onChange={onChange}
                    />
                ))}
                <Form.Group as={Row} className="my-3" controlId="roles">
                    <Form.Label column sm="6" md="5">
                        Choose role:
                    </Form.Label>
                    <Col sm="9" md="8" lg="6">
                            <DropdownButton
                                as={ButtonGroup}
                                variant={"info"}
                                title={
                                     selectedRole
                                     ? selectedRole
                                     : "None"
                                }
                                onSelect={(selectedElement) => {
                                    updateSelectedElement(selectedElement);
                                }}
                            >
                                {userRoles.map((role) => (
                                    <Dropdown.Item
                                        key={role.name}
                                        eventKey={role.name}
                                    >
                                        {role.name}
                                    </Dropdown.Item>
                                ))}
                            </DropdownButton>

                    </Col>
                </Form.Group>

                <button className='register-button'>Submit</button>
            </form>
        </div>
    );
};

export default Registration;