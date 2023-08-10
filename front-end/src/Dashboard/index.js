import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import SideBar from "../SideBar";
import DefaultSidebar from "../SideBar";
import {Container} from "react-bootstrap";
import './dashboard.css'


const Dashboard = () => {
    const user = useUser();
    console.log(user.jwt);
    const [jwt, setJwt] = useLocalState("", "jwt");
    const [tickets, setTickets] = useState(null);
    const [projects, setProjects] = useState(null);

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer("api/v1/ticket", "GET", user.jwt)
        .then((ticketsData) => {
        setTickets(ticketsData);
        });
    }, []);

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer("api/v1/project", "GET", user.jwt)
            .then((projectsData) => {
                setProjects(projectsData);
            });
    }, []);



    function createTicket() {
        grabAndAuthorizeRequestFromTheServer("api/v1/ticket", "POST", user.jwt)
            .then((ticket) => {
        window.location.href = `/tickets/${ticket.id}`;
        });
    }

    function createProject() {
        grabAndAuthorizeRequestFromTheServer("api/v1/project", "POST", user.jwt)
            .then((project) => {
                window.location.href = `/projects/${project.id}`;
            });
    }



    return (
        <>
            <DefaultSidebar/>
            <Container style={{marginRight: 0 + 'em', padding: 0}}>
                <div className="row g-4 dashboard-row-1">
                    <div className="col dashboard-col-1">
                        <div className="card dashboard-card-1">
                            <img src={require("./img/DashboardProjectTemplate4.png")} className="card-img-top" alt="..."/>
                            <div className="card-body">
                                <h5 className="card-title">Card title</h5>
                                <p className="card-text">This is a longer card with supporting text below as a
                                    natural lead-in to additional content. This content is a little bit longer.</p>
                                <a href="/projects" className="btn btn-primary">Go somewhere</a>
                            </div>
                        </div>
                    </div>
                    <div className="col dashboard-col-1">
                        <div className="card dashboard-card-1">
                            <img src={require("./img/DashboardTicketTemplate2.png")} className="card-img-top" alt="..."/>
                            <div className="card-body">
                                <h5 className="card-title">Card title</h5>
                                <p className="card-text">This is a longer card with supporting text below as a
                                    natural lead-in to additional content. This content is a little bit longer.</p>
                                <a href="/tickets" className="btn btn-primary">Go somewhere</a>
                            </div>
                        </div>
                    </div>
                </div>
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