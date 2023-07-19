import React, {useEffect, useState} from 'react';
import {useLocalState} from "../util/useLocalStorage";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import Comment from "../Comment/index.js";

import {
    ButtonGroup,
    DropdownButton,
    Dropdown,
    Col,
    Form,
    Row,
    Container,
} from "react-bootstrap";
import {Link, useOutlet} from "react-router-dom";
import Select from "react-select";
import {useUser} from "../UserProvider";
import axios from "axios";
import grabAndAuthorizeRequestFromTheServerFiles from "../Services/fetchService2";
import jwt_decode from "jwt-decode";
import CommentContainer from "../CommentContainer";

const TicketView = () => {
    const user = useUser();
    const ticketId = window.location.href.split("/tickets/")[1];
    const [ticket, setTicket] = useState({
        title: ""
    });
    const emptyComment = {
        id: null,
        message: "",
        ticketId: ticketId
    }
    const emptyAttachment = {
        id: null,
        file: null,
        notes: "",
        ticketId: ticketId

    }
    const [comment, setComment] = useState(emptyComment);
    const [comments, setComments] = useState([]);
    const [attachment, setAttachment] = useState(emptyAttachment);
    const [notes, setNotes] = useState("");
    const [file, setFile] = useState();
    const [attachments, setAttachments] = useState([]);
    const [ticketTypes, setTicketTypes] = useState([]);
    const [ticketPriorities, setTicketPriorities] = useState([]);
    const [ticketStatuses, setTicketStatuses] = useState([]);
    const [projectPersonnel, setProjectPersonnel] = useState([]);
    const [historyFields, setHistoryFields] = useState([]);
    const [developer, setDeveloper] = useState({});
    const [developerEmail, setDeveloperEmail] = useState();
    const [devChangeFlag, setDevChangeFlag] = useState(0);
    const personnelEmails = projectPersonnel.map(user => ({value:user.email, label:user.email}));
    const [imagePreview, setImagePreview] = useState(null);
    const [imageData, setImageData] = useState(null);
    const [imageName, setImageName] = useState("");

    const [files, setFiles] = useState('');
    //state for checking file size
    const [fileSize, setFileSize] = useState(true);
    // for file upload progress message
    const [fileUploadProgress, setFileUploadProgress] = useState(false);
    //for displaying response message
    const [fileUploadResponse, setFileUploadResponse] = useState(null);


    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.sub;
        }
    }
    const uploadFileHandler = (event) => {
        setFiles(event.target.files);
    };

    const fileSubmitHandler = (event) => {
        event.preventDefault();
        setFileSize(true);
        setFileUploadProgress(true);
        setFileUploadResponse(null);

        const formData = new FormData();

        for (let i = 0; i < files.length; i++) {
            if (files[i].size > 10240000){
                console.log(files[i].size);
                setFileSize(false);
                setFileUploadProgress(false);
                setFileUploadResponse(null);
                return;
            }

            formData.append(`files`, files[i]);
            formData.append('notes', notes);
            formData.append('ticketId', ticketId);
            formData.append('email', getEmailFromJWT());
        }

        const requestOptions = {
            method: 'POST',
            body: formData
        };

        fetch('/api/v1/attachment', requestOptions)
            .then(async response => {
                const isJson = response.headers.get('content-type')?.includes('application/json');
                const data = isJson && await response.json();

                // check for error response
                if (!response.ok) {
                    // get error message
                    const error = (data && data.message) || response.status;
                    setFileUploadResponse(data.message);
                    return Promise.reject(error);
                }

                console.log(data.message);
                setFileUploadResponse(data.message);
            })
            .catch(error => {
                console.error('Error while uploading file!', error);
            });
        setFileUploadProgress(false);
    };


    function updateTicket(prop, value) {
        const newTicket = { ...ticket }
        newTicket[prop] = value;
        setTicket(newTicket);
        console.log(newTicket);
    }

    function updateAttachment(prop, value) {
        const newAttachment = { ...attachment }
        newAttachment[prop] = value;
        setAttachment(newAttachment);
        console.log(attachment);
        console.log(attachments);
        console.log(file);
    }


    function handleSelect(data) {
        setDeveloperEmail(data);
        setDevChangeFlag(1);
    }

    function save() {
        if(devChangeFlag === 1) {
            console.log("asdgDGSAADGSADGSADGSGF")
            grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}`, "PUT", user.jwt, ticket)
                .then((ticketData) => {
                    setTicket(ticketData);
                })
                .then(() =>
                    grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}/add-developer-to-ticket`, "PUT", user.jwt, developerEmail.value))
                .then((ticketData) => {
                    setTicket(ticketData);
                });
        }
        else {
            console.log(devChangeFlag);
            grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}`, "PUT", user.jwt, ticket)
                .then((ticketData) => {
                    setTicket(ticketData);
                });
        }
        //window.location.reload();
    }

    function deleteTicket() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket`, "DELETE", user.jwt, ticketId)
        window.location.href = "/dashboard";
    }




    function assignDeveloperToTicket() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}/add-developer-to-ticket`, "PUT", user.jwt, developerEmail.value)
            .then((ticketData) => {
                setTicket(ticketData);
            })
        }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}`, "GET", user.jwt)
        .then((ticketResponse) => {
            let ticketData = ticketResponse.ticket;
            if (ticketData.title === null);
            setTicket(ticketData);
            setTicketTypes(ticketResponse.types);
            setTicketPriorities(ticketResponse.priorities);
            setTicketStatuses(ticketResponse.progressStatuses);
            setProjectPersonnel(ticketResponse.projectPersonnel);
            setDeveloper(ticketResponse.developer);
            setHistoryFields(ticketResponse.historyFields);
            setAttachments(ticketResponse.attachments);
        });
    }, []);

    // function saveAttachment() {
    //     grabAndAuthorizeRequestFromTheServer("/api/v1/attachment", "POST", user.jwt, attachment)
    //         .then((data) => {
    //             const attachmentsCopy = [...attachments];
    //             attachmentsCopy.push(data);
    //             setAttachments(attachmentsCopy);
    //             setAttachment(emptyAttachment);
    //         });
    // }




    function saveAttachment() {
        console.log(file);
        grabAndAuthorizeRequestFromTheServer("/api/v1/file/upload", "POST", user.jwt, file)
            .then((data) => {
                // const attachmentsCopy = [...attachments];
                // attachmentsCopy.push(data);
                // setAttachments(attachmentsCopy);
                // setAttachment(emptyAttachment);
            });
    }

    const handleUploadClick = event => {
        let file = event.target.files[0];
        const imageData = new FormData();
        imageData.append('imageFile', file);
        setImageData(imageData);
        setImagePreview(URL.createObjectURL(file));
    };

    const handleChange = event => {
        setImageName(event.target.value)
    };

    // function updateNotes(value) {
    //     const notesCopy = { ...notes }
    //     commentCopy.message = value;
    //     setComment(commentCopy);
    //     console.log(comments)
    // }

    return (
        <>
        <Container className="mt-5">
            <Row className="d-flex align-items-center">
                <Col>
                    <h1>
                        {ticketId}
                    </h1>
                    {ticket ? (
                        <>
                            <h1>{ticket.title}</h1>
                        </>
                    ) : (
                        <></>
                    )}
                </Col>
            </Row>
            {ticket ? (
                <>
                    <Form.Group as={Row} className="my-3" controlId="ticketTitle">
                        <Form.Label column sm="3" md="2">
                            title:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Form.Control
                                onChange={(e) =>
                                    updateTicket("title", e.target.value)
                                }
                                type="text"
                                value={ticket.title}
                                placeholder="title"
                            />
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="my-3" controlId="ticketDescription">
                        <Form.Label column sm="3" md="2">
                            description:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Form.Control
                                onChange={(e) =>
                                    updateTicket("description", e.target.value)
                                }
                                type="text"
                                value={ticket.description}
                                placeholder="description"
                            />
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="my-3" controlId="ticketType">
                        <Form.Label column sm="3" md="2">
                            type:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <DropdownButton
                                as={ButtonGroup}
                                variant={"info"}
                                title={
                                    ticket.type
                                        ? `${ticket.type}`
                                        : "Select a type"
                                }
                                onSelect={(selectedElement) => {
                                    updateTicket("type", selectedElement);
                                }}
                            >
                                {ticketTypes.map((type) => (
                                    <Dropdown.Item
                                        key={type.name}
                                        eventKey={type.name}
                                    >
                                        {type.name}
                                    </Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="my-3" controlId="ticketPriority">
                        <Form.Label column sm="3" md="2">
                            priority:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <DropdownButton
                                as={ButtonGroup}
                                variant={"info"}
                                title={
                                    ticket.priority
                                        ? `${ticket.priority}`
                                        : "Select a priority"
                                }
                                onSelect={(selectedElement) => {
                                    updateTicket("priority", selectedElement);
                                }}
                            >
                                {ticketPriorities.map((priority) => (
                                    <Dropdown.Item
                                        key={priority.name}
                                        eventKey={priority.name}
                                    >
                                        {priority.name}
                                    </Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="my-3" controlId="ticketStatus">
                        <Form.Label column sm="3" md="2">
                            status:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <DropdownButton
                                as={ButtonGroup}
                                variant={"info"}
                                title={
                                    ticket.status
                                        ? `${ticket.status}`
                                        : "Select a priority"
                                }
                                onSelect={(selectedElement) => {
                                    updateTicket("status", selectedElement);
                                }}
                            >
                                {ticketStatuses.map((status) => (
                                    <Dropdown.Item
                                        key={status.name}
                                        eventKey={status.name}
                                    >
                                        {status.name}
                                    </Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Col>
                    </Form.Group>
                    <Form.Group as={Row} className="my-3" controlId="developer">
                        <Form.Label column sm="3" md="2">
                            developer:
                        </Form.Label>
                        <Col sm="9" md="8" lg="6">
                            <Select
                                placeholder="Select user"
                                options={personnelEmails}
                                value={developerEmail ? developerEmail : developer ? {value: developer.email, label: developer.email} : ""}
                                onChange={handleSelect}
                                isSearchable={true}
                            />
                            {/*<DropdownButton*/}
                            {/*    as={ButtonGroup}*/}
                            {/*    variant={"info"}*/}
                            {/*    title={*/}
                            {/*            selectedOption*/}
                            {/*            ? selectedOption*/}
                            {/*            : ticket.assignedDeveloper*/}
                            {/*            ? ticket.assignedDeveloper.email*/}
                            {/*            : "select user"*/}
                            {/*    }*/}
                            {/*    onSelect={(selectedElement) => {*/}
                            {/*        handleOptionChange(selectedElement);*/}
                            {/*    }}*/}
                            {/*>*/}
                            {/*    {projectPersonnel.map((developer) => (*/}
                            {/*        <Dropdown.Item*/}
                            {/*            key={developer.email}*/}
                            {/*            eventKey={developer.email}*/}
                            {/*        >*/}
                            {/*            {developer.email};*/}
                            {/*        </Dropdown.Item>*/}
                            {/*    ))}*/}
                            {/*</DropdownButton>*/}
                        </Col>
                    </Form.Group>
                    <button onClick={() => save()}>Save</button>
                    <button onClick={() => deleteTicket()}>Delete</button>
                    <CommentContainer ticketId={ticketId} />
                    <Form.Group as={Row} className="my-3" controlId="historyFields">
                        <Form.Label column sm="3" md="2">
                            history field:
                        </Form.Label>
                            {historyFields.map((field) => (
                        <Col sm="9" md="8" lg="6">
                                {field.property}, {field.newValue}, {field.oldValue}, {field.dateChanged}
                        </Col>
                            ))}
                    </Form.Group>
                    {/*<Form.Group>*/}
                    {/*    <input type="file"  onChange={handleUploadClick}/>*/}
                    {/*    <Col sm="9" md="8" lg="6">*/}
                    {/*        <Form.Control*/}
                    {/*            onChange={handleChange}*/}
                    {/*            type="text"*/}
                    {/*            value={attachment.notes}*/}
                    {/*            placeholder="description"*/}
                    {/*        />*/}
                    {/*    </Col>*/}
                    {/*    <button onClick={() => uploadImageWithAdditionalData()}>add </button>*/}
                    {/*    <div className="mt-5">*/}
                    {/*        {attachments.map((singleAttachment) => (*/}
                    {/*            <Col>>*/}
                    {/*                <a target='_blank'>*/}
                    {/*                    /!*{singleAttachment.file.name}*!/*/}
                    {/*                </a>*/}
                    {/*                    , {singleAttachment.notes}*/}
                    {/*            </Col>*/}
                    {/*        ))}*/}
                    {/*    </div>*/}
                    {/*</Form.Group>*/}
                    <form onSubmit={fileSubmitHandler} enctype="multipart/form-data">
                        <input type="file" multiple onChange={uploadFileHandler} required/>
                        <div>
                            <textarea
                                style={{width: "30%", borderRadius: "0.3em"}}
                                onChange={(e) => setNotes(e.target.value)}
                            ></textarea>
                        </div>
                        <button type='submit'>Upload</button>
                        {!fileSize && <p style={{color:'red'}}>File size exceeded!!</p>}
                        {fileUploadProgress && <p style={{color:'red'}}>Uploading File(s)</p>}
                        {fileUploadResponse!=null && <p style={{color:'green'}}>{fileUploadResponse}</p>}
                    </form>
                </>

            ) : (
                <></>
            )}
        </Container>
        </>
    );
};

export default TicketView;