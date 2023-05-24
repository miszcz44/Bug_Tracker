import { useState } from "react";
import "./registration.css";
import FormInput from "./components/FormInput";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useLocalState} from "../util/useLocalStorage";
import axios from 'axios';



const Registration = () => {
    const [values, setValues] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
    });
    const [jwt, setJwt] = useLocalState("", "jwt");

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
            pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
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
            const response = await axios.post('/api/v1/registration', userData);
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
    };

    const onChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
        console.log(values);
    };

    return (
        <div className="app">
            <form onSubmit={handleSubmit}>
                <h1>Register</h1>
                {inputs.map((input) => (
                    <FormInput
                        key={input.id}
                        {...input}
                        value={values[input.name]}
                        onChange={onChange}
                    />
                ))}
                <button onClick={() => registerUser(values)}>Submit</button>
            </form>
        </div>
    );
};

export default Registration;