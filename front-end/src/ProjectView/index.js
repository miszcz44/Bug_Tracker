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
            <Container className="mt-5">
                <Row className="d-flex align-items-center">
                    <Col>
                        <h1>
                            {projectId}
                        </h1>
                        {project ? (
                            <>
                                <h1>{project.name}</h1>
                            </>
                        ) : (
                            <></>
                        )}
                    </Col>
                </Row>
                {project ? (
                    <>
                        <Form.Group as={Row} className="my-3" controlId="projectName">
                            <Form.Label column sm="3" md="2">
                                name:
                            </Form.Label>
                            <Col sm="9" md="8" lg="6">
                                <Form.Control
                                    onChange={(e) =>
                                        updateProject("name", e.target.value)
                                    }
                                    type="text"
                                    value={project.name}
                                    placeholder="name"
                                />
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="my-3" controlId="projectDescription">
                            <Form.Label column sm="3" md="2">
                                description:
                            </Form.Label>
                            <Col sm="9" md="8" lg="6">
                                <Form.Control
                                    onChange={(e) =>
                                        updateProject("description", e.target.value)
                                    }
                                    type="text"
                                    value={project.description}
                                    placeholder="description"
                                />
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="my-3" controlId="projectManager">
                            <Form.Label column sm="3" md="2">
                                project manager:
                            </Form.Label>
                            <Col sm="9" md="8" lg="6">
                                <Form.Control
                                    type="text"
                                    value={managerName + " (You)"}
                                    disabled
                                />
                            </Col>
                        </Form.Group>
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
            </Container>
        </>
    );
};

export default ProjectView;