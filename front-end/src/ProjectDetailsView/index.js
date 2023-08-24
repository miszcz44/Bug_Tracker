import React, {useEffect, useState} from 'react';
import SideBar from "../SideBar";
import './ProjectDetails.css'
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import jwt_decode from "jwt-decode";

const ProjectDetailsView = () => {

    const user = useUser();
    const projectId = window.location.href.split("/projects/details/")[1]
    let errorCode = 0;
    const [project, setProject] = useState({});
    const [projectManagerEmail, setProjectManagerEmail] = useState("");
    const [projectManagerName, setProjectManagerName] = useState("");
    const [projectPersonnel, setProjectPersonnel] = useState([]);
    const [projectTickets, setProjectTickets] = useState([]);
    const [personnelFilters, setPersonnelFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    const [ticketFilters, setTicketFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    let editUrl = '/projects/';

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/details/${projectId}`, "GET", user.jwt)
            .then((response) => {
                if(!response.status) {
                    setProject(response.project);
                    setProjectManagerEmail(response.projectManagerEmail);
                    setProjectManagerName(response.projectManagerName);
                    setProjectPersonnel(response.projectPersonnel);
                    setProjectTickets(response.tickets);
                    console.log(response);
                }
                else if(!response.ok) {
                    errorCode = response.status;
                    throw Error(response.status);
                }
            })
            .catch(err => {
                errorCode === 403 ? window.location.href = "/403" :
                    errorCode === 404 ? window.location.href = "/404" :
                        window.location.href = "/otherError";
            });
    }, []);

    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.sub;
        }
        return "null";
    }

    const actionBodyTemplate = (rowData) => {
        let url = "/tickets/details/"
        return <div>
            <Link to={url.concat(rowData.id)}>Details</Link>
        </div>
    };

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }

    function createNewTicket() {
        grabAndAuthorizeRequestFromTheServer("/api/v1/ticket", "POST", user.jwt, {
            "id": projectId
        })
            .then(ticket => {
                window.location.href = `/projects/${projectId}/tickets/${ticket.id}`;
            });
    }
    function deleteProject() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project`, "DELETE", user.jwt, projectId)
            .then(response => {
                window.location.href = '/projects';
            })
    }

    return (
        <div>
            <SideBar/>
            <div className="card project-details-card-1">
                <div className='d-flex'>
                    <h2 className="pt-2 px-2" style={{marginBottom: '4px', width: '80%'}}>
                        Details for project - {project.name}
                    </h2>
                    {
                        (getEmailFromJWT() === projectManagerEmail || getRoleFromJWT() === "ADMIN") &&
                        !getRoleFromJWT().startsWith("DEMO") ?
                            <button className='project-details-button-1' onClick={() => deleteProject()}>
                                Delete Project
                            </button>
                        :
                            <></>
                    }
                    {
                        (getEmailFromJWT() === projectManagerEmail && getRoleFromJWT() === "DEMO_PROJECT_MANAGER") ||
                            getRoleFromJWT() === "DEMO_ADMIN" ?
                            <button disabled className='project-details-button-2' onClick={() => deleteProject()}>
                                Delete Project
                            </button>
                            :
                            <></>
                    }
                </div>
                <div className='d-inline py-1'>
                    {
                        (getEmailFromJWT() === projectManagerEmail || getRoleFromJWT() === "ADMIN") &&
                            !getRoleFromJWT().startsWith("DEMO") ?
                            <>
                                <Link className="px-3" to={editUrl.concat(projectId)}>Edit</Link>
                                <Link onClick={() => createNewTicket()}>Create New Ticket</Link>
                            </>
                        :
                            <></>
                    }
                    {
                        (getRoleFromJWT() === "PROJECT_MANAGER" && getEmailFromJWT() !== projectManagerEmail)
                        || (getRoleFromJWT() === "SUBMITTER") ?
                            <Link className="px-3" onClick={() => createNewTicket()}>Create New Ticket</Link>
                        :
                            <></>
                    }
                    {
                        (getRoleFromJWT() === "DEMO_PROJECT_MANAGER" && getEmailFromJWT() !== projectManagerEmail)
                        || (getRoleFromJWT() === "DEMO_SUBMITTER") ?
                            <Link style={{color: "#777"}} className="project-details-link-1 px-3">Create New Ticket</Link>
                            :
                            <></>
                    }
                    {
                        (getRoleFromJWT() === "DEMO_PROJECT_MANAGER" && getEmailFromJWT() === projectManagerEmail) ||
                        getRoleFromJWT() === "DEMO_ADMIN" ?
                            <>
                                <Link className="px-3" to={editUrl.concat(projectId)}>Edit</Link>
                                <Link style={{color: "#777"}} className="project-details-link-1">Create New Ticket</Link>
                            </>
                            :
                            <></>
                    }

                </div>
                <div className="container">
                    <div className="row project-details-row-1 d-flex">
                        <div className="col-9">
                            <label className="project-details-label-1">
                                Project Description
                            </label>
                            <p className="project-details-p-1">{project.description}</p>
                        </div>
                        <div className="col-3">
                            <label className="project-details-label-1">
                                Project Manager
                            </label>
                            <p className="project-details-p-1">{projectManagerName}</p>
                        </div>
                    </div>
                </div>
                <div className='card project-details-card-2'>
                    <div class="container">
                        <div class="row">
                            <div class="card project-details-card-2 col-5">
                                    <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Assigned Personnel
                                </h3>
                                <p className="project-details-p-2">Current users on this project</p>
                                <div className='d-flex p-2 pt-0'>
                                    <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                        Search:
                                    </label>
                                    <InputText
                                        onInput={(e) =>
                                            setPersonnelFilters({
                                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                            })
                                        }
                                        className='project-details-input-1'
                                        style={{fontSize: '12px'}}
                                    />
                                </div>
                                <DataTable value={projectPersonnel} stripedRows sortMode="multiple" filters={personnelFilters} tableStyle={{ minWidth: '30rem' }}
                                           paginator rows={6} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                                    <Column field="wholeName" header="Name" sortable style={{fontSize: '12px', width: '35%', padding: '2px' }}/>
                                    <Column field="email" header="Email" sortable style={{fontSize: '12px', width: '45%', padding: '2px' }}/>
                                    <Column field="srole" header="Role" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                </DataTable>
                            </div>
                            <div class="card project-details-card-2 col-7">
                                <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Tickets for this project
                                </h3>
                                <p className="project-details-p-2">Condensed ticket details</p>
                                <div className='d-flex p-2 pt-0'>
                                    <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                        Search:
                                    </label>
                                    <InputText
                                        onInput={(e) =>
                                            setTicketFilters({
                                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                            })
                                        }
                                        className='project-details-input-1'
                                        style={{fontSize: '12px'}}
                                    />
                                </div>
                                <DataTable value={projectTickets} stripedRows sortMode="multiple" filters={ticketFilters} tableStyle={{ minWidth: '30rem' }}
                                           paginator rows={8} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                                    <Column field="title" header="Title" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }}/>
                                    <Column field="submitter" header="Submitter" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }}/>
                                    <Column field="developer" header="Developer" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                    <Column field="status" header="Status" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                    <Column field="created" header="Created" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                    {
                                        getRoleFromJWT() === "PROJECT_MANAGER" || getRoleFromJWT() === "ADMIN" ||
                                            getRoleFromJWT() === "DEMO_PROJECT_MANAGER" || getRoleFromJWT() === "DEMO_ADMIN" ?
                                        <Column field="id" style={{padding: '2px', fontSize: '12px', paddingRight: '5px' }} body={actionBodyTemplate} />
                                        :
                                        <></>
                                    }
                                </DataTable>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default ProjectDetailsView;