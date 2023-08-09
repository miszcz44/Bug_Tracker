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
    const ticketId = window.location.href.split("tickets/")[1];
    const [ticket, setTicket] = useState("");
    const [projectName, setProjectName] = useState("");
    const [currentDeveloper, setCurrentDeveloper] = useState({});
    const currentDeveloperLabel = currentDeveloper ? {value: currentDeveloper, label: currentDeveloper.wholeName + ' | ' + currentDeveloper.email} : null;
    const [developers, setDevelopers] = useState([]);
    const developersLabel = developers.map(developer => ({value: developer, label: developer.wholeName + ' | ' + developer.email}))
    const [selectedDeveloper, setSelectedDeveloper] = useState();
    const [types, setTypes] = useState([]);
    const typeLabel = ticket.type ? {value: ticket.type, label: ticket.type} : "";
    const typesLabel = types.map(type => ({value: type.name, label: type.name}))
    const [selectedType, setSelectedType] = useState();
    const [priorities, setPriorities] = useState([]);
    const priorityLabel = ticket.priority ? {value: ticket.priority, label: ticket.priority} : "";
    const prioritiesLabel = priorities.map(priority => ({value: priority.name, label: priority.name}))
    const [selectedPriority, setSelectedPriority] = useState();
    const [status, setStatuses] = useState([]);
    const statusLabel = ticket.status ? {value: ticket.status, label: ticket.status} : "";
    const statusesLabel = status.map(progressStatus => ({value: progressStatus.name, label: progressStatus.name}))
    const [selectedStatus, setSelectedStatus] = useState();
    const [personnelFilters, setPersonnelFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    //const usersEmails = allUsers.map(user => ({value:user.email, label:user.email}));
    function updateTicket(prop, value) {
        const newTicket = { ...ticket }
        newTicket[prop] = value;
        setTicket(newTicket);
    }

    // function handleOptionChange(selectedOption) {
    //     setUserEmail(selectedOption);
    //     setSelectedOption(selectedOption);
    // }

    function save() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}`, "PUT", jwt, {
            "ticket": ticket,
            "developer": selectedDeveloper ? selectedDeveloper.value : currentDeveloper
        })
        window.location.href = `/tickets/${ticket.id}`;
    }

    function handleDeveloperSelect(data) {
        setSelectedDeveloper(data);
    }

    function handlePrioritySelect(data) {
        setSelectedPriority(data);
        ticket["priority"] = data.value;
    }

    function handleStatusSelect(data) {
        setSelectedStatus(data);
        ticket["status"] = data.value;
    }

    function handleTypeSelect(data) {
        setSelectedType(data);
        ticket["type"] = data.value;
    }


    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/edit/${ticketId}`, "GET", jwt)
            .then((response) => {
                setTicket(response.ticket);
                setProjectName(response.projectName);
                setCurrentDeveloper(response.developer);
                setDevelopers(response.possibleDevelopers);
                setTypes(response.types);
                setPriorities(response.priorities);
                setStatuses(response.progressStatuses);
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
                    <form onSubmit={() => save()}>
                        <div className='container'>
                            <div className='row'>
                                <div className='col-sm'>
                                    <Form.Group as={Row} className="my-0" controlId="title">
                                        <Form.Label className='project-view-label-1' column sm="4" md="4">
                                            Title
                                        </Form.Label>
                                        <p>
                                            <Col sm="9" md="8" lg="6">
                                                <Form.Control
                                                    onChange={(e) =>
                                                        updateTicket("title", e.target.value)
                                                    }
                                                    type="text"
                                                    value={ticket.title}
                                                    placeholder="name"
                                                    style={{width:'300px'}}
                                                    required
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                    <Form.Group as={Row} className="my-0" controlId="projectName">
                                        <Form.Label className='project-view-label-1' column sm="6" md="6">
                                            Project Name
                                        </Form.Label>
                                        <p>
                                            <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                <Form.Control
                                                    type="text"
                                                    value={projectName}
                                                    placeholder="name"
                                                    style={{width:'300px'}}
                                                    disabled
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                                <div className='col-sm'>
                                    <Form.Group as={Row} className="my-0" controlId="ticketDescription">
                                        <Form.Label className='project-view-label-1' column sm="4" md="4">
                                            Description
                                        </Form.Label>
                                        <p>
                                            <Col sm="9" md="8" lg="6">
                                                <textarea
                                                    onChange={(e) =>
                                                        updateTicket("description", e.target.value)
                                                    }
                                                    type="text"
                                                    value={ticket.description}
                                                    placeholder="description"
                                                    style={{width:'300px', height:'130px', resize:'none'}}
                                                    className='project-view-textarea-1'
                                                    required
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                            </div>
                            <div className='row'>
                                <div className='col-6'>
                                    <Form.Group as={Row} className="my-0" controlId="developer">
                                        <Form.Label className='project-view-label-1' column sm="6" md="6">
                                            Assigned Developer
                                        </Form.Label>
                                        <p>
                                            <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                <Select
                                                    placeholder="Select developer"
                                                    options={developersLabel}
                                                    value={selectedDeveloper ? selectedDeveloper : currentDeveloper ? currentDeveloperLabel : ""}
                                                    isSearchable={true}
                                                    onChange={handleDeveloperSelect}
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                                <div className='col-6'>
                                    <Form.Group as={Row} className="my-0" controlId="priority">
                                        <Form.Label className='project-view-label-1' column sm="6" md="6">
                                            Ticket Priority
                                        </Form.Label>
                                        <p>
                                            <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                <Select
                                                    placeholder="Select priority"
                                                    options={prioritiesLabel}
                                                    value={selectedPriority ? selectedPriority : priorityLabel ? priorityLabel : ""}
                                                    onChange={handlePrioritySelect}
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                            </div>
                            <div className='row'>
                                <div className='col-6'>
                                    <Form.Group as={Row} className="my-0" controlId="type">
                                        <Form.Label className='project-view-label-1' column sm="6" md="6">
                                            Ticket type
                                        </Form.Label>
                                        <p>
                                            <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                <Select
                                                    placeholder="Select type"
                                                    options={typesLabel}
                                                    value={selectedType ? selectedType : typeLabel ? typeLabel : ""}
                                                    onChange={handleTypeSelect}
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                                <div className='col-6 mb-5'>
                                    <Form.Group as={Row} className="my-0" controlId="status">
                                        <Form.Label className='project-view-label-1' column sm="6" md="6">
                                            Ticket Status
                                        </Form.Label>
                                        <p>
                                            <Col style={{width:'300px'}} sm="9" md="8" lg="6">
                                                <Select
                                                    placeholder="Select status"
                                                    options={statusesLabel}
                                                    value={selectedStatus ? selectedStatus : statusLabel ? statusLabel: ""}
                                                    onChange={handleStatusSelect}
                                                />
                                            </Col>
                                        </p>
                                    </Form.Group>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default TicketEditView;