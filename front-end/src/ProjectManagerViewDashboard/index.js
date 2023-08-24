import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import SideBar from "../SideBar";
import DefaultSidebar from "../SideBar";
import {Container} from "react-bootstrap";
import './dashboard.css'
import jwt_decode from "jwt-decode";


const Dashboard = () => {
    const user = useUser();
    const [projectId, setProjectId] = useState();
    const [projectTitle, setProjectTitle] = useState("");
    const [projectDescription, setProjectDescription] = useState("");
    const [ticketId, setTicketId] = useState();
    const [ticketTitle, setTicketTitle] = useState("");
    const [ticketDescription, setTicketDescription] = useState("");
    console.log(user.jwt);
    let projectDetailsUrl = "/projects/details/";
    let ticketDetailsUrl = "/tickets/details/";

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer("api/v1/user/dashboard", "GET", user.jwt)
        .then(response => {
            setProjectId(response.projectId);
            setProjectTitle(response.projectTitle);
            setProjectDescription(response.projectDescription);
            setTicketId(response.ticketId);
            setTicketTitle(response.ticketTitle);
            setTicketDescription(response.ticketDescription);
        });
    }, []);
    function createTicket() {
        grabAndAuthorizeRequestFromTheServer("api/v1/ticket", "POST", user.jwt)
            .then((ticket) => {
        window.location.href = `/tickets/${ticket.id}`;
        });
    }

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }

    function createNewProject() {
        grabAndAuthorizeRequestFromTheServer("api/v1/project", "POST", user.jwt)
            .then((project) => {
                window.location.href = `/projects/${project.id}`;
            });
    }

    return (
        <>
            <DefaultSidebar/>
            <Container style={{marginRight: 0 + 'em', padding: 0}}>
                {
                    getRoleFromJWT() === "PROJECT_MANAGER" || getRoleFromJWT() === "DEMO_PROJECT_MANAGER" ?
                    <div className="row dashboard-row-1">
                        <div className="col dashboard-col-1">
                            <div className="card dashboard-card-1">
                                <img src={require("./img/DashboardProjectTemplate4.png")} className="card-img-top" alt="..."/>
                                {projectId ?
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">One of your projects - {projectTitle}</h5>
                                        <p className="card-text">{projectDescription}</p>
                                        <a href={projectDetailsUrl.concat(projectId)} className="btn btn-primary">See More</a>
                                    </div>
                                    :
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">New Project</h5>
                                        <p className="card-text">Get started and create your first project</p>
                                        <a onClick={createNewProject} className="btn btn-primary">Create Project</a>
                                    </div>
                                }
                            </div>
                        </div>
                        <div className="col dashboard-col-1 px-0">
                            {ticketId ?
                                <div className="card dashboard-card-1">
                                    <img src={require("./img/DashboardTicketTemplate2.png")} className="card-img-top" alt="..."/>
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">One of your tickets - {ticketTitle}</h5>
                                        <p className="card-text">{ticketDescription}</p>
                                        <a href={ticketDetailsUrl.concat(ticketId)} className="btn btn-primary">See More</a>
                                    </div>
                                </div>
                                :
                                <div className="card dashboard-card-2">
                                    <img src={require("./img/DashboardProfileTemplate.png")} className="card-img-top" alt="..."/>
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">Profile</h5>
                                        <p className="card-text">Your profile details and actions</p>
                                        <a href="/user-profile" className="btn btn-primary">See More</a>
                                    </div>
                                </div>
                            }
                        </div>
                    </div>
                    :
                    getRoleFromJWT() === "DEVELOPER" || getRoleFromJWT() === "SUBMITTER" ||
                        getRoleFromJWT() === "DEMO_DEVELOPER" || getRoleFromJWT() === "DEMO_SUBMITTER" ?
                    <div className="row dashboard-row-1">
                        <div className="col dashboard-col-1">
                            {projectId ?
                                <div className="card dashboard-card-1">
                                    <img src={require("./img/DashboardProjectTemplate4.png")} className="card-img-top" alt="..."/>
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">One of your projects - {projectTitle}</h5>
                                        <p className="card-text">{projectDescription}</p>
                                        <a href={projectDetailsUrl.concat(projectId)} className="btn btn-primary">See More</a>
                                    </div>
                                </div>
                                :
                                <div className="card dashboard-card-2" style={{height: '400px'}}>
                                    <img src={require("./img/DashboardProfileTemplate.png")} className="card-img-top" alt="..."/>
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">Profile</h5>
                                        <p className="card-text">Your profile details and actions</p>
                                        <a href="/user-profile" className="btn btn-primary">See More</a>
                                    </div>
                                </div>
                            }
                        </div>
                        <div className="col dashboard-col-1 px-0">
                            {ticketId ?
                                <div className="card dashboard-card-1">
                                    <img src={require("./img/DashboardTicketTemplate2.png")} className="card-img-top" alt="..."/>
                                    <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                        <h5 className="card-title">One of your
                                            {getRoleFromJWT() === "DEVELOPER" || getRoleFromJWT() === "DEMO_DEVELOPER" ?
                                                " assigned" : " submitted"} tickets - {ticketTitle}</h5>
                                        <p className="card-text">{ticketDescription}</p>
                                        <a href={ticketDetailsUrl.concat(ticketId)} className="btn btn-primary">See More</a>
                                    </div>
                                </div>
                                : projectId ?
                                    <div className="card dashboard-card-2" style={{height: '400px'}}>
                                        <img src={require("./img/DashboardProfileTemplate.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">Profile</h5>
                                            <p className="card-text">Your profile details and actions</p>
                                            <a href="/user-profile" className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                    :
                                    <></>
                            }
                        </div>
                    </div>
                    :
                    getRoleFromJWT() === "NONE" ?
                        <div className="row dashboard-row-1">
                            <div className="col dashboard-col-1">
                                {projectId ?
                                    <div className="card dashboard-card-1">
                                        <img src={require("./img/DashboardProjectTemplate4.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">One of your projects - {projectTitle}</h5>
                                            <p className="card-text">{projectDescription}</p>
                                            <a href={projectDetailsUrl.concat(projectId)} className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                    :
                                    <div className="card dashboard-card-2" style={{height: '400px'}}>
                                        <img src={require("./img/DashboardProfileTemplate.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">Profile</h5>
                                            <p className="card-text">Your profile details and actions</p>
                                            <a href="/user-profile" className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                }
                            </div>
                            <div className="col dashboard-col-1 px-0">
                                {projectId ?
                                    <div className="card dashboard-card-2" style={{height: '400px'}}>
                                        <img src={require("./img/DashboardProfileTemplate.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">Profile</h5>
                                            <p className="card-text">Your profile details and actions</p>
                                            <a href="/user-profile" className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                    :
                                    <></>
                                }
                            </div>
                        </div>
                        :
                        getRoleFromJWT() === "ADMIN" || getRoleFromJWT() === "DEMO_ADMIN" ?
                            <div className="row dashboard-row-1">
                                <div className="col dashboard-col-1">
                                    <div className="card dashboard-card-1">
                                        <img src={require("./img/DashboardAllProjectsTemplate2.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">All Projects</h5>
                                            <p className="card-text">List of all projects ever created</p>
                                            <a href="/projects" className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                </div>
                                <div className="col dashboard-col-1 px-0">
                                    <div className="card dashboard-card-1">
                                        <img src={require("./img/DashboardAllTicketsTemplate2.png")} className="card-img-top" alt="..."/>
                                        <div className="card-body" style={{backgroundColor: '#ddd'}}>
                                            <h5 className="card-title">All Tickets</h5>
                                            <p className="card-text">List of all tickets ever created</p>
                                            <a href="/tickets" className="btn btn-primary">See More</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            :
                            <></>
                }

        {/*<div style={{ margin: "2em" }}>*/}
        {/*    {tickets ? (*/}
        {/*        tickets.map((ticket) => (*/}
        {/*            <div key={ticket.id}>*/}
        {/*                <Link to={`/tickets/${ticket.id}`}>*/}
        {/*                    Ticket id: {ticket.id}*/}
        {/*                </Link>*/}
        {/*            </div>*/}
        {/*            ))*/}
        {/*        ) : (*/}
        {/*            <></>*/}
        {/*        )}*/}
        {/*    {projects ? (*/}
        {/*        projects.map((project) => (*/}
        {/*            <div key={project.id}>*/}
        {/*                <Link to={`/projects/${project.id}`}>*/}
        {/*                    Project id: {project.id}*/}
        {/*                </Link>*/}
        {/*            </div>*/}
        {/*        ))*/}
        {/*    ) : (*/}
        {/*        <></>*/}
        {/*    )}*/}
        {/*    <Link to={"/user-management"}>Manage user roles</Link>*/}
        {/*    {<button onClick={() => createTicket()}>Create new ticket</button>}*/}
        {/*    {<button onClick={() => createProject()}>Create new project</button>}*/}

        {/*</div>*/}
            </Container>
        </>
    );
};

export default Dashboard;