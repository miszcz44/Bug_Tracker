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
import './ProjectView.css';
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {Link} from "react-router-dom";
import jwt_decode from "jwt-decode";
import {useUser} from "../UserProvider";
const ProjectView = () => {
    const user = useUser();
    const [jwt, setJwt] = useLocalState("", "jwt");
    const projectId = window.location.href.split("/projects/")[1];
    let errorCode = 0;
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


    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }
    const actionBodyTemplate = (rowData) => {

        return <div>
            <Link className='project-view-link-1' onClick={() => deleteUserFromProject(rowData.id)}>Delete</Link>
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
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/edit/${projectId}`, "PUT", jwt, {
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
    function addUserToProject() {
        for(let i=0; i<selectedUsers.length; i++) {
            projectPersonnel.push(selectedUsers[i].value);
            allNotParticipatingUsers.splice(allNotParticipatingUsers.indexOf(selectedUsers[i].value), 1);
            console.log(projectPersonnel);
        }
        setSelectedUsers([]);
        // grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}/add-user-to-project`, "PUT", jwt, selectedUsers.map(email => email.value))
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
        // grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}/delete-user-from-project`, "DELETE", jwt, id)
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
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/edit/${projectId}`, "GET", jwt)
            .then(response => {
                console.log(response);
                if(!response.status) {
                    setProject(response.project);
                    setCurrentManager(response.currentManager);
                    setProjectManagers(response.projectManagers);
                    setProjectPersonnel(response.projectPersonnel);
                    setAllNotParticipatingUsers(response.allUsersNotInProject);
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

    return (
        <>
            <div style={{backgroundColor: '#efefef', height: '753px'}}>
                <SideBar/>
                <div className='card project-view-card-1'>
                    <div className='d-flex'>
                        <h2 className="pt-1 px-2 d-inline" style={{marginBottom: '4px'}}>Edit Project</h2>
                        <button className='project-view-button-4' onClick={() => window.location.href = `/projects/details/${projectId}`}>
                            To details
                        </button>
                        {
                            getRoleFromJWT() === "DEMO_ADMIN" || getRoleFromJWT() === "DEMO_PROJECT_MANAGER" ?
                                <button disabled className='project-view-button-2' onClick={() => save()}>
                                    Save Changes
                                </button>
                                :
                                <button className='project-view-button-2' onClick={() => save()}>
                                    Save Changes
                                </button>
                        }

                    </div>
                    <p className="px-4">Change project properties and personnel</p>
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
                            </div>
                            <Form.Group as={Row} className="my-0" controlId="allUsers">
                                <Form.Label className='project-view-label-2' column sm="4" md="4">
                                    Selected New Users
                                </Form.Label>
                                <p className='d-flex'>
                                    <Col sm="9" md="8" lg="6"><Select
                                        placeholder="Select user"
                                        options={allNotParticipatingUsersLabel}
                                        value={selectedUsers}
                                        onChange={handleSelect}
                                        isSearchable={true}
                                        isMulti
                                        className='project-view-select-1'
                                    />
                                    </Col>
                                    <Col>
                                        {<button className='project-view-button-1' onClick={() => addUserToProject()}>Add To Project</button>}
                                    </Col>
                                </p>

                            </Form.Group>
                            <div className="project-details-card-2">
                                <h3 className="px-2" style={{marginBottom: '4px'}}>
                                    Project Personnel
                                </h3>
                                <p className="project-details-p-2">
                                    Current users on this project
                                    <div className='d-inline p-2 pt-0'>
                                        <label className='project-view-label-3 d-inline'>
                                            Search:
                                        </label>
                                        <InputText
                                            onInput={(e) =>
                                                setPersonnelFilters({
                                                    global: {value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                                })
                                            }
                                            className='project-view-input-1'
                                            style={{fontSize: '12px'}}
                                        />
                                    </div>
                                </p>

                                <DataTable value={projectPersonnel} stripedRows sortMode="multiple"
                                           filters={personnelFilters} tableStyle={{minWidth: '30rem'}}
                                           paginator rows={5} style={{backgroundColor: '#111111'}}
                                           className='project-view-table-1 px-2'>
                                    <Column field="wholeName" header="Name" sortable
                                            style={{fontSize: '12px', width: '35%', padding: '2px'}}/>
                                    <Column field="email" header="Email" sortable
                                            style={{fontSize: '12px', width: '45%', padding: '2px'}}/>
                                    <Column field="srole" header="Role" sortable
                                            style={{fontSize: '12px', width: '20%', padding: '2px'}}/>
                                    <Column field="id" style={{padding: '2px', fontSize: '12px', paddingRight: '5px' }}
                                            body={actionBodyTemplate} />
                                </DataTable>
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

export default ProjectView;