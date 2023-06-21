import React, {useEffect, useState} from 'react';
import {ButtonGroup, Col, Dropdown, DropdownButton, Form, Row} from "react-bootstrap";
import Select from "react-select";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import jwt_decode from "jwt-decode";

const RoleManagement = () => {
    const user = useUser();
    const changeRoleResponse = {
        usersEmails: [],
        role: " "
    }

    const [nonAdminUsers, setNonAdminUsers] = useState([]);
    const [allUsers, setAllUsers] = useState([]);
    const [userRoles, setUserRoles] = useState([]);
    const [selectedEmails, setSelectedEmails] = useState([]);
    const [selectedRole, setSelectedRole] = useState("");
    let nonAdminUsersEmails = nonAdminUsers.map(appUser => ({value:appUser.email, label:appUser.email}));

    function handleSelect(data) {
        setSelectedEmails(data);
    }

    function getRolesFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.authorities;
        }
        return [];
    }

    function updateSelectedElement(element) {
        setSelectedRole(element);
        console.log(userRoles);
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user`, "GET", user.jwt)
            .then((userResponse) => {
                setAllUsers(userResponse.users);
                setUserRoles(userResponse.userRoles);
            });
    }, []);

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/non-admin`, "GET", user.jwt)
            .then((data) => {
                setNonAdminUsers(data);
            });
    }, []);


    function getDifference() {
        nonAdminUsersEmails = nonAdminUsersEmails.filter(object1 => {
            return !selectedEmails.some(object2 => {
                return object1.value === object2.value;
            });
        });
    }

    function assignRoleToUsers() {
        setSelectedEmails([]);
        changeRoleResponse.usersEmails = selectedEmails.map(email => email.value);
        changeRoleResponse.role = selectedRole;
        const nonSelectedUsers = allUsers.filter(user => {
            return !changeRoleResponse.usersEmails.includes(user.email);
        })
        const selectedUsers = allUsers.filter(user => {
            return changeRoleResponse.usersEmails.includes(user.email);
        })
        for(let i=0; i<selectedUsers.length; i++) {
            selectedUsers[i].appUserRole.name = selectedRole;
        }
        setAllUsers(selectedUsers.concat(nonSelectedUsers));
        if(changeRoleResponse.role === 'Admin') {
            setNonAdminUsers(nonSelectedUsers);
        }
        grabAndAuthorizeRequestFromTheServer(`api/v1/user/change-role`, "PUT", user.jwt, changeRoleResponse)
    }

    return (
        <>
            <Form.Group as={Row} className="my-3" controlId="nonAdminUsers">
                <Form.Label column sm="3" md="2">
                    all users:
                </Form.Label>
                <Col sm="9" md="8" lg="6"><Select
                    placeholder="Select user"
                    options={nonAdminUsersEmails}
                    value={selectedEmails}
                    onChange={handleSelect}
                    isSearchable={true}
                    isMulti
                />
                </Col>
                <Form.Group as={Row} className="my-3" controlId="roles">
                    <Form.Label column sm="3" md="2">
                        role
                    </Form.Label>
                    <Col sm="9" md="8" lg="6">
                        <DropdownButton
                            as={ButtonGroup}
                            variant={"info"}
                            title={
                                selectedRole
                                    ? selectedRole
                                    : "Select role"
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


            </Form.Group>
            <button onClick={() => assignRoleToUsers()}>assign role</button>
            <Form.Group as={Row} className="my-3" controlId="allUsers">
                <Form.Label column sm="3" md="2">
                    Your personnel:
                </Form.Label>
                {allUsers.map((appUser) => (
                    <div sm="9" md="8" lg="6">
                        {appUser.firstName + " " + appUser.lastName } {appUser.email} {appUser.appUserRole.name}
                    </div>
                ))}
            </Form.Group>

        </>
    );
};

export default RoleManagement;