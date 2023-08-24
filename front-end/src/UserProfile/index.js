import React, {useEffect, useState} from 'react';
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import jwt_decode from "jwt-decode";
import FormInput from "../Registration/components/FormInput";
import {Col, Form, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import SideBar from "../SideBar";
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import './UserProfile.css'

const UserProfile = () => {

    const user = useUser();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [role, setRole] = useState("");
    const [email, setEmail] = useState("");
    const [numberOfManagedProjects, setNumberOfManagedProjects] = useState(0);
    const [numberOfSubmittedTickets, setNumberOfSubmittedTickets] = useState(0);
    const [numberOfAssignedTickets, setNumberOfAssignedTickets] = useState(0);
    const [numberOfBelongingProjects, setNumberOfBelongingProjects] = useState(0);

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer("/api/v1/user/user-profile", "GET", user.jwt)
            .then((response) => {
                setFirstName(response.firstName);
                setLastName(response.lastName);
                setRole(response.role);
                setEmail(response.email);
                setNumberOfSubmittedTickets(response.numberOfSubmittedTickets);
                setNumberOfManagedProjects(response.numberOfManagedProjects);
                setNumberOfBelongingProjects(response.numberOfBelongingProjects);
                setNumberOfAssignedTickets(response.numberOfAssignedTickets);
            });
    }, []);
    return (
        <div>
            <SideBar/>
            <div className="user-profile-container-1">
                <div className="row">
                    <div className="col card ticket-details-card-1">
                        <div className='d-flex p-2'>
                            <h3 className="pt-2 pl-2" style={{marginBottom: '4px'}}>
                                User Profile
                            </h3>
                            <button className='user-profile-button-2' onClick={() => window.location.href = '/change-email'}>
                                Change Email
                            </button>
                            <button className='user-profile-button-1' onClick={() => window.location.href = '/change-password'}>
                                Change Password
                            </button>
                        </div>
                        <div className="row mt-4 p-2">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    First Name
                                </label>
                                <p className="ticket-details-p-1">{firstName}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Last Name
                                </label>
                                <p className="ticket-details-p-1">{lastName}</p>
                            </div>
                        </div>
                        <div className="row mt-4 p-2">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Email
                                </label>
                                <p className="ticket-details-p-1">{email}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Role
                                </label>
                                <p className="ticket-details-p-1">{role}</p>
                            </div>
                        </div>
                        {
                            getRoleFromJWT() === "PROJECT_MANAGER" || getRoleFromJWT() === "ADMIN" ||
                                getRoleFromJWT() === "DEMO_PROJECT_MANAGER" || getRoleFromJWT() === "DEMO_ADMIN" ?
                                <div className="row mt-4 mb-4 p-2">
                                    <div className="col-6">
                                        <label className="project-details-label-1">
                                            Managed Projects Count
                                        </label>
                                        <p className="ticket-details-p-1">{numberOfManagedProjects}</p>
                                    </div>
                                    <div className="col-6">
                                        <label className="project-details-label-1">
                                            Submitted Tickets Count
                                        </label>
                                        <p className="ticket-details-p-1">{numberOfSubmittedTickets}</p>
                                    </div>
                                </div> :
                                getRoleFromJWT() === "DEVELOPER" || getRoleFromJWT() === "DEMO_DEVELOPER" ?
                                    <div className="row mt-4 mb-4 p-2">
                                        <div className="col-6">
                                            <label className="project-details-label-1">
                                                Assigned Projects Count
                                            </label>
                                            <p className="ticket-details-p-1">{numberOfBelongingProjects}</p>
                                        </div>
                                        <div className="col-6">
                                            <label className="project-details-label-1">
                                                Assigned Tickets Count
                                            </label>
                                            <p className="ticket-details-p-1">{numberOfAssignedTickets}</p>
                                        </div>
                                    </div> :
                                    getRoleFromJWT() === "SUBMITTER" || getRoleFromJWT() === "DEMO_SUBMITTER" ?
                                        <div className="row mt-4 mb-4 p-2">
                                            <div className="col-6">
                                                <label className="project-details-label-1">
                                                    Assigned Projects Count
                                                </label>
                                                <p className="ticket-details-p-1">{numberOfBelongingProjects}</p>
                                            </div>
                                            <div className="col-6">
                                                <label className="project-details-label-1">
                                                    Submitted Tickets Count
                                                </label>
                                                <p className="ticket-details-p-1">{numberOfSubmittedTickets}</p>
                                            </div>
                                        </div> :
                                        getRoleFromJWT() === "NONE" ?
                                            <div className="row mt-4 mb-4 p-2">
                                                <div className="col-6">
                                                    <label className="project-details-label-1">
                                                        Assigned Projects Count
                                                    </label>
                                                    <p className="ticket-details-p-1">{numberOfBelongingProjects}</p>
                                                </div>
                                            </div> :
                                            <></>
                        }

                    </div>
                </div>
            </div>
        </div>
    );
};

export default UserProfile;