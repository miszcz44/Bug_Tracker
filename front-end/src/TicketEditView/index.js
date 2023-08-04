import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService"
import {
    ButtonGroup,
    DropdownButton,
    Dropdown,
    Col,
    Form,
    Row,
    Container,
} from "react-bootstrap";
import Select from "react-select";
import SideBar from "../SideBar";
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {Link} from "react-router-dom";
const TicketEditView = () => {
    const [jwt, setJwt] = useLocalState("", "jwt");
    const ticketId = window.location.href.split("tickets/edit/")[1];
    const [project, setProject] = useState({});
    const [currentManager, setCurrentManager] = useState({});
    const currentManagerLabel = {value: currentManager, label: currentManager.wholeName + ' | ' + currentManager.email}
    const [projectManagers, setProjectManagers] = useState([]);
    const projectManagersLabel = projectManagers.map(manager => ({value: manager, label: manager.wholeName + ' | ' + manager.email}))
    const [selectedProjectManager, setSelectedProjectManager] = useState();
    const [projectPersonnel, setProjectPersonnel] = useState([]);
    const [allNotParticipatingUsers, setAllNotParticipatingUsers] = useState([]);
    const allNotParticipatingUsersLabel = allNotParticipatingUsers.map(user => ({value: user, label: user.wholeName + ' | ' + user.email + ' | ' + user.srole}))
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [personnelFilters, setPersonnelFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    //const usersEmails = allUsers.map(user => ({value:user.email, label:user.email}));
    let selectedUserCount = 0;

    const actionBodyTemplate = (rowData) => {

        return <div>
            <Link onClick={() => deleteUserFromProject(rowData.id)}>{rowData.id}</Link>
        </div>
    };
    function updateProject(prop, value) {
        const newProject = { ...project }
        newProject[prop] = value;
        setProject(newProject);
        console.log(project);
        console.log(projectPersonnel);
        console.log(selectedProjectManager);
    }

    // function handleOptionChange(selectedOption) {
    //     setUserEmail(selectedOption);
    //     setSelectedOption(selectedOption);
    // }

    function save() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${ticketId}`, "PUT", jwt, {
            "project": project,
            "currentManager": selectedProjectManager ? selectedProjectManager.value : currentManager,
            "projectPersonnel": projectPersonnel,
            "allUsersNotInProject": null,
            "projectManagers": null
        })
            .then((projectData) => {
                setProject(projectData);
            })
        window.location.href = `/projects/${project.id}`;
    }

    function createTicket() {
        grabAndAuthorizeRequestFromTheServer("/api/v1/ticket/to-project", "POST", jwt, ticketId)
            .then((ticket) => {
                window.location.href = `/projects/${ticketId}/tickets/${ticket.id}`;
            });
    }

    function addUserToProject() {
        for(let i=0; i<selectedUsers.length; i++) {
            projectPersonnel.push(selectedUsers[i].value);
            allNotParticipatingUsers.splice(allNotParticipatingUsers.indexOf(selectedUsers[i].value), 1);
            console.log(projectPersonnel);
        }
        setSelectedUsers([]);
        // grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${ticketId}/add-user-to-project`, "PUT", jwt, selectedUsers.map(email => email.value))
        //     .then((projectData) => {
        //         setProject(projectData);
        //     })
        // window.location.href = `/projects/${project.id}`;
    }

    function deleteUserFromProject(id) {
        const newPersonnel = [ ...projectPersonnel];
        for(let i=0; i<projectPersonnel.length; i++) {
            if(newPersonnel[i].id === id) {
                allNotParticipatingUsers.push(newPersonnel[i]);
                newPersonnel.splice(i, 1);
                break;
            }
        }
        setProjectPersonnel(newPersonnel);
        // grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${ticketId}/delete-user-from-project`, "DELETE", jwt, id)
        //     .then((msg) => {
        //     });
        // window.location.href = `/projects/${project.id}`;
    }

    function handleSelect(data) {
        setSelectedUsers(data);
    }

    function handleManagerSelect(data) {
        const currentOption = selectedProjectManager ? selectedProjectManager.value : currentManager;
        if(currentOption.id !== data.value.id) {
            projectPersonnel.push(currentOption);
            for(let i = 0; i < projectPersonnel.length ; i++) {
                if(projectPersonnel[i].id === data.value.id) {
                    projectPersonnel.splice(i, 1);
                }
            }
            setSelectedProjectManager(data);
        }
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/edit/${ticketId}`, "GET", jwt)
            .then((response) => {
                setProject(response.project);
                setCurrentManager(response.currentManager);
                setProjectManagers(response.projectManagers);
                setProjectPersonnel(response.projectPersonnel);
                setAllNotParticipatingUsers(response.allUsersNotInProject);
                console.log(response);
            });
    }, []);

    return (
        <>
            <div style={{backgroundColor: '#efefef', height: '753px'}}>
                <SideBar/>
                <div className='card project-view-card-1'>
                    <div className='d-flex'>
                        <h2 className="pt-1 px-2 d-inline" style={{marginBottom: '4px'}}>Edit Ticket</h2>
                        <button className='project-view-button-3' onClick={() => window.location.href = `/projects/details/${ticketId}`}>
                            Back To Details
                        </button>
                        <button className='project-view-button-2' onClick={() => save()}>
                            Save Changes
                        </button>
                    </div>
                    <p className="px-4">Change Ticket properties</p>
                    {project ? (
                        <>
                            <div className='container'>
                                <div className='row'>
                                    <div className='col-sm'>
                                        <Form.Group as={Row} className="my-0" controlId="projectName">
                                            <Form.Label className='project-view-label-1' column sm="4" md="4">
                                                Title
                                            </Form.Label>
                                            <p>
                                                <Col sm="9" md="8" lg="6">
                                                    <Form.Control
                                                        onChange={(e) =>
                                                            updateProject("name", e.target.value)
                                                        }
                                                        type="text"
                                                        value={project.name}
                                                        placeholder="name"
                                                        style={{width:'300px'}}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                        <Form.Group as={Row} className="my-0" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Project Manager
                                            </Form.Label>
                                            <p>
                                                <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersLabel}
                                                        value={selectedProjectManager ? selectedProjectManager : currentManager ? currentManagerLabel : ""}
                                                        isSearchable={true}
                                                        onChange={handleManagerSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                    <div className='col-sm'>
                                        <Form.Group as={Row} className="my-0" controlId="projectDescription">
                                            <Form.Label className='project-view-label-1' column sm="4" md="4">
                                                Description
                                            </Form.Label>
                                            <p>
                                                <Col sm="9" md="8" lg="6">
                                                    <textarea
                                                        onChange={(e) =>
                                                            updateProject("description", e.target.value)
                                                        }
                                                        type="text"
                                                        value={project.description}
                                                        placeholder="description"
                                                        style={{width:'300px', height:'130px', resize:'none'}}
                                                        className='project-view-textarea-1'
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                </div>
                                <div className='row'>
                                    <div className='col-6'>
                                        <Form.Group as={Row} className="my-0" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Assigned Developer
                                            </Form.Label>
                                            <p>
                                                <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersLabel}
                                                        value={selectedProjectManager ? selectedProjectManager : currentManager ? currentManagerLabel : ""}
                                                        isSearchable={true}
                                                        onChange={handleManagerSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                    <div className='col-6'>
                                        <Form.Group as={Row} className="my-0" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Ticket Priority
                                            </Form.Label>
                                            <p>
                                                <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersLabel}
                                                        value={selectedProjectManager ? selectedProjectManager : currentManager ? currentManagerLabel : ""}
                                                        isSearchable={true}
                                                        onChange={handleManagerSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                </div>
                                <div className='row'>
                                    <div className='col-6'>
                                        <Form.Group as={Row} className="my-0" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Ticket Status
                                            </Form.Label>
                                            <p>
                                                <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersLabel}
                                                        value={selectedProjectManager ? selectedProjectManager : currentManager ? currentManagerLabel : ""}
                                                        isSearchable={true}
                                                        onChange={handleManagerSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                    <div className='col-6 mb-5'>
                                        <Form.Group as={Row} className="my-0" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Ticket Type
                                            </Form.Label>
                                            <p>
                                                <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersLabel}
                                                        value={selectedProjectManager ? selectedProjectManager : currentManager ? currentManagerLabel : ""}
                                                        isSearchable={true}
                                                        onChange={handleManagerSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                </div>
                            </div>
                        </>

                    ) : (
                        <></>
                    )}
                </div>
            </div>
        </>
    );
};

export default TicketEditView;