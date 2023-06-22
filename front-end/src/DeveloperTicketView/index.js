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

const DeveloperTicketView = () => {
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

    function updateTicket(prop, value) {
        const newTicket = { ...ticket }
        newTicket[prop] = value;
        setTicket(newTicket);
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
        window.location.reload();
    }

    function deleteTicket() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket`, "DELETE", user.jwt, ticketId)
        window.location.href = "/dashboard";
    }

    function handleDeleteComment(commentId) {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/comments/${commentId}`, "DELETE", user.jwt)
            .then((msg) => {
                const commentsCopy = [...comments];
                const i = commentsCopy.findIndex((comment) => comment.id === commentId);
                commentsCopy.splice(i, 1);
                setComments(commentsCopy);
                console.log(comments);
            });
    }

    function handleEditComment(commentId) {
        const i = comments.findIndex((comment) => comment.id === commentId);
        const commentCopy = {
            id: comments[i].id,
            message: comments[i].message,
            ticketId: comments[i].ticket.id
        }
        setComment(commentCopy);
    }
    function assignDeveloperToTicket() {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}/add-developer-to-ticket`, "PUT", user.jwt, developerEmail.value)
            .then((ticketData) => {
                setTicket(ticketData);
            })
    }

    function submitComment() {
        if(comment.id && comment.message !== "") {
            grabAndAuthorizeRequestFromTheServer(`/api/v1/comments/${comment.id}`, "PUT", user.jwt, comment.message)
                .then((data) => {
                    const commentsCopy = [...comments];
                    console.log(comment.message);
                    const i = commentsCopy.findIndex((comment) => comment.id === data.id);
                    commentsCopy[i] = data;
                    setComments(commentsCopy);
                    setComment(emptyComment);
                });
        }
        else if(comment.message !== "") {
            grabAndAuthorizeRequestFromTheServer("/api/v1/comments", "POST", user.jwt, comment)
                .then((data) => {
                    const commentsCopy = [...comments];
                    commentsCopy.push(data);
                    setComments(commentsCopy);
                    setComment(emptyComment);
                });
        }

    }

    function updateComment(value) {
        const commentCopy = { ...comment }
        commentCopy.message = value;
        setComment(commentCopy);
        console.log(comments)
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/comments?ticketId=${ticketId}`, "GET", user.jwt)
            .then((commentsData) => {
                setComments(commentsData);
            });
    }, []);

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
                                    disabled
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
                                    disabled
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
                                    disabled
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
                                    disabled
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
                                    isDisabled={true}
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
                        <div className="mt-5">
                        <textarea
                            style={{width: "100%", borderRadius: "0.25em"}}
                            onChange={(e) => updateComment(e.target.value)}
                            value={comment.message}
                            nonempty
                        ></textarea>
                            <button onClick={() => submitComment()}>Post comment</button>
                        </div>
                        <div className="mt-5">
                            {comments.map((comment) => (
                                <Comment
                                    id = {comment.id}
                                    createdAt = {comment.createdAt}
                                    commentator = {comment.commentator}
                                    message = {comment.message}
                                    emitDeleteComment = {handleDeleteComment}
                                    emitEditComment = {handleEditComment}
                                />
                            ))}
                        </div>
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
                        <Form.Group>
                            <input type="file"  onChange={(e) =>
                                setFile(e.target.files[0])
                            }/>
                            <Col sm="9" md="8" lg="6">
                                <Form.Control
                                    onChange={(e) =>
                                        updateAttachment("notes", e.target.value)
                                    }
                                    type="text"
                                    value={attachment.notes}
                                    placeholder="description"
                                />
                            </Col>
                            <button onClick={() => saveAttachment()}>add </button>
                            <div className="mt-5">
                                {attachments.map((singleAttachment) => (
                                    <Col>>
                                        <a target='_blank'>
                                            {/*{singleAttachment.file.name}*/}
                                        </a>
                                        , {singleAttachment.notes}
                                    </Col>
                                ))}
                            </div>
                        </Form.Group>
                    </>

                ) : (
                    <></>
                )}
            </Container>
        </>
    );
};

export default DeveloperTicketView;