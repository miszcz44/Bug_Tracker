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
const ProjectView = () => {
    const [jwt, setJwt] = useLocalState("", "jwt");
    const projectId = window.location.href.split("/projects/")[1];
    const [project, setProject] = useState({});
    const [managerName, setmanagerName] = useState();
    const [projectPersonnel, setProjectPersonnel] = useState([]);
    const [allUsers, setAllUsers] = useState([]);
    const [selectedEmails, setSelectedEmails] = useState([]);
    const usersEmails = allUsers.map(user => ({value:user.email, label:user.email}));
    function updateProject(prop, value) {
        const newProject = { ...project }
        newProject[prop] = value;
        setProject(newProject);
        console.log(project);
        console.log(allUsers);
    }

    // function handleOptionChange(selectedOption) {
    //     setUserEmail(selectedOption);
    //     setSelectedOption(selectedOption);
    // }

    function save() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}`, "PUT", jwt, project)
            .then((projectData) => {
                setProject(projectData);
            })
    }

    function createTicket() {
        grabAndAuthorizeRequestFromTheServer("/api/v1/ticket/to-project", "POST", jwt, projectId)
            .then((ticket) => {
                window.location.href = `/projects/${projectId}/tickets/${ticket.id}`;
            });
    }

    function addUserToProject() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}/add-user-to-project`, "PUT", jwt, selectedEmails.map(email => email.value))
            .then((projectData) => {
                setProject(projectData);
            })
        window.location.href = `/projects/${project.id}`;
    }

    function deleteUserFromProject(id) {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}/delete-user-from-project`, "DELETE", jwt, id)
            .then((msg) => {
            });
        window.location.href = `/projects/${project.id}`;
    }

    function handleSelect(data) {
        setSelectedEmails(data);
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/${projectId}`, "GET", jwt)
            .then((projectResponse) => {
                let projectData = projectResponse.project;
                if (projectData.title === null);
                setProject(projectData);
                setmanagerName(projectResponse.managerName);
                setProjectPersonnel(projectResponse.projectPersonnel);
                setAllUsers(projectResponse.allUsers);
            });
    }, []);

    return (
        <>
            <div style={{backgroundColor: '#efefef', height: '753px'}}>
                <SideBar/>
                <div className='card project-view-card-1'>
                    <h2 className="pt-2 px-2" style={{marginBottom: '4px'}}>Edit Project</h2>
                    <p className="px-4">Change project properties and personnel</p>
                    {project ? (
                        <>
                            <div className='container'>
                                <div className='row'>
                                    <div className='col-sm'>
                                        <Form.Group as={Row} className="my-1" controlId="projectName">
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
                                        <Form.Group as={Row} className="my-1" controlId="projectManager">
                                            <Form.Label className='project-view-label-1' column sm="6" md="6">
                                                Project Manager
                                            </Form.Label>
                                            <p>
                                                <Col sm="9" md="8" lg="6">
                                                    <Select
                                                        placeholder="Select project manager"
                                                        options={projectManagersEmails}
                                                        value={selectedProjectManager}
                                                        type="text"
                                                        style={{width:'500px'}}
                                                        isSearchable={true}
                                                        onChange={handleSelect}
                                                    />
                                                </Col>
                                            </p>
                                        </Form.Group>
                                    </div>
                                    <div className='col-sm'>
                                        <Form.Group as={Row} className="my-1" controlId="projectDescription">
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
                            <Form.Group as={Row} className="my-3" controlId="projectPersonnel">
                                <Form.Label column sm="3" md="2">
                                    project personnel:
                                </Form.Label>
                                {projectPersonnel.map((person) => (
                                <Col sm="9" md="8" lg="6">
                                    {person.email} <button onClick={() => deleteUserFromProject(person.id)}> delete user</button>
                                </Col>
                                ))}
                            </Form.Group>
                            <Form.Group as={Row} className="my-3" controlId="allUsers">
                                <Form.Label column sm="3" md="2">
                                    all users:
                                </Form.Label>
                                <Col sm="9" md="8" lg="6"><Select
                                    placeholder="Select user"
                                    options={usersEmails}
                                    value={selectedEmails}
                                    onChange={handleSelect}
                                    isSearchable={true}
                                    isMulti
                                />

                                    {/*<DropdownButton*/}
                                    {/*    as={ButtonGroup}*/}
                                    {/*    variant={"info"}*/}
                                    {/*    title={*/}
                                    {/*        selectedOption*/}
                                    {/*            ? selectedOption*/}
                                    {/*            : "select user"*/}
                                    {/*    }*/}
                                    {/*    onSelect={(selectedElement) => {*/}
                                    {/*        handleOptionChange(selectedElement);*/}
                                    {/*    }}*/}
                                    {/*>*/}
                                    {/*    {allUsers.map((user) => (*/}
                                    {/*        <Dropdown.Item*/}
                                    {/*            key={user.email}*/}
                                    {/*            eventKey={user.email}*/}
                                    {/*        >*/}
                                    {/*            {user.email};*/}
                                    {/*        </Dropdown.Item>*/}
                                    {/*    ))}*/}
                                    {/*</DropdownButton>*/}
                                </Col>
                                <Col>
                                    {<button onClick={() => addUserToProject()}> add user</button>}
                                </Col>
                            </Form.Group>
                            {<button onClick={() => createTicket()}>Create new ticket</button>}
                            <button onClick={() => save()}>Save</button>
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