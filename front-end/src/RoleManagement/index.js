import React, {useEffect, useState} from 'react';
import {ButtonGroup, Col, Container, Dropdown, DropdownButton, Form, Row} from "react-bootstrap";
import Select from "react-select";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import jwt_decode from "jwt-decode";
import Sidebar from "../SideBar";
import "./RoleManagement.css";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column"
import {FilterMatchMode} from "primereact/api";
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css"
import {InputText} from "primereact/inputtext";

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
    const [filters, setFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
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
        console.log(allUsers);
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/role-management`, "GET", user.jwt)
            .then((userResponse) => {
                setAllUsers(userResponse.users);
                setUserRoles(userResponse.userRoles);
            });
    }, []);

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/role-management/non-admin`, "GET", user.jwt)
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
            selectedUsers[i].srole = selectedRole;
        }
        setAllUsers(selectedUsers.concat(nonSelectedUsers));
        if(changeRoleResponse.role === 'Admin') {
            setNonAdminUsers(nonSelectedUsers);
        }
        grabAndAuthorizeRequestFromTheServer(`/api/v1/user/role-management/change-role`, "PUT", user.jwt, changeRoleResponse)
    }

    return (
        <>
            <Sidebar/>
            <Container className='role-management-container-1' style={{marginRight: 0 + 'em', padding: 0}}>
            <Form.Group className="my-3" controlId="nonAdminUsers">
                <div >
                <h1 className="role-management-label-1">
                    Manage user roles
                </h1>
                <Col className='role-management-label-1 role-management-select-1' sm="3" md="8" lg="8">
                    <Select
                    placeholder="Select user(s)"
                    options={nonAdminUsersEmails}
                    value={selectedEmails}
                    onChange={handleSelect}
                    isSearchable={true}
                    isMulti
                />
                </Col>
                </div>
                {/*<Form.Group as={Row} className="my-3" controlId="roles">*/}
                    <Col className='role-management-label-1 role-management-select-2' sm="9" md="8" lg="6">
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
                {/*</Form.Group>*/}
            <button className="role-management-button-1" onClick={() => assignRoleToUsers()}>Assign role</button>
            </Form.Group>
            <div className='role-management-container-2'>

            <div className="card role-management-card">
                <div className='d-flex p-2'>
                    <label className='role-management-label-2' style={{fontSize: '12px'}}>
                        Search:
                    </label>
                    <InputText
                        onInput={(e) =>
                            setFilters({
                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                            })
                        }
                        className='role-management-input-1'
                        style={{fontSize: '12px'}}
                    />
                </div>
                <DataTable value={allUsers} stripedRows showGridlines sortMode="multiple" filters={filters} tableStyle={{ minWidth: '50rem' }}
                paginator rows={10} style={{backgroundColor: '#111111'}}>
                    <Column field="email" header="email" sortable style={{ width: '33%', fontSize: '12px', padding: '6px' }}/>
                    <Column field="wholeName" header="Name" sortable style={{ width: '33%', fontSize: '12px', padding: '6px' }}/>
                    <Column field="srole" header="Role" sortable style={{ width: '33%', fontSize: '12px', padding: '6px' }}/>
                </DataTable>
            </div>
             </div>
             </Container>
         </>
    );
};

export default RoleManagement;