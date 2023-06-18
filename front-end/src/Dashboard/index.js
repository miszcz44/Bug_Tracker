import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";


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
        <div style={{ margin: "2em" }}>
            {tickets ? (
                tickets.map((ticket) => (
                    <div key={ticket.id}>
                        <Link to={`/tickets/${ticket.id}`}>
                            Ticket id: {ticket.id}
                        </Link>
                    </div>
                    ))
                ) : (
                    <></>
                )}
            {projects ? (
                projects.map((project) => (
                    <div key={project.id}>
                        <Link to={`/projects/${project.id}`}>
                            Project id: {project.id}
                        </Link>
                    </div>
                ))
            ) : (
                <></>
            )}
            <Link to={"/user-management"}>Manage user roles</Link>
            {<button onClick={() => createTicket()}>Create new ticket</button>}
            {<button onClick={() => createProject()}>Create new project</button>}

        </div>
    );
};

export default Dashboard;