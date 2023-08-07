import React, {useEffect, useState} from 'react';
import SideBar from "../SideBar";
import "./TicketDetails.css"
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import CommentContainer from "../CommentContainer";
import jwt_decode from "jwt-decode";
const TicketDetails = () => {

    const user = useUser();
    const decodedJwt = jwt_decode(user.jwt);
    const ticketId = window.location.href.split("/tickets/details/")[1]
    const [ticket, setTicket] = useState({});
    const [developerName, setDeveloperName] = useState("");
    const [submitterName, setSubmitterName] = useState("");
    const [projectName, setProjectName] = useState("");
    const [comments, setComments] = useState([]);
    const [emptyComment, setEmptyComment] = useState({
        id: null,
        commentatorEmail: decodedJwt.sub,
        message: "",
        created: null
    })
    const [comment, setComment] = useState(emptyComment);
    const [historyFields, setHistoryFields] = useState([]);
    const [commentsFilters, setCommentsFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    const [historyFilters, setHistoryFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    const [attachmentsFilters, setAttachmentsFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });

    let editUrl = '/tickets/';

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket/${ticketId}`, "GET", user.jwt)
            .then((response) => {
                setTicket(response.ticket);
                setDeveloperName(response.developerName);
                setSubmitterName(response.submitterName);
                setProjectName(response.projectName);
                setComments(response.comments)
                setHistoryFields(response.ticketHistoryField);
                console.log(response);
            });
    }, []);

    const actionBodyTemplate = (rowData) => {
        let url = "/projects/details/"
        return <div>
            <Link to={url.concat(rowData.id)}>Details</Link>
        </div>
    };

    function addComment() {
        const newComment = { ...comment};
        newComment["created"] = null;
        setComment(newComment);
        grabAndAuthorizeRequestFromTheServer(`/api/v1/comments/${ticketId}`, "POST", user.jwt, comment)
        const commentsCopy = [...comments];
        newComment["created"] = "Just Now";
        commentsCopy.push(newComment);
        setComments(commentsCopy);
        setComment(emptyComment);
    }

    function updateComment(value) {
        const newComment = { ...comment};
        newComment["message"] = value;
        setComment(newComment);
        console.log(comment);
    }

    return (
        <div>
            <SideBar/>
            <div className="container ticket-details-container-1">
                <div className="row">
                    <div className="col card ticket-details-card-1">
                        <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                            Details for ticket
                        </h3>
                        <div className='d-inline py-0'>
                            <Link className="px-3" to="/tickets">Back To List</Link>
                            <Link to={editUrl.concat(ticketId)}>Edit Ticket</Link>
                        </div>
                        <div className="row mt-4">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Title
                                </label>
                                <p className="ticket-details-p-1">{ticket.title}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Description
                                </label>
                                <p className="ticket-details-p-1">{ticket.description}</p>
                            </div>
                        </div>
                        <div className="row mt-4">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Assigned Developer
                                </label>
                                <p className="ticket-details-p-1">{developerName}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Submitter
                                </label>
                                <p className="ticket-details-p-1">{submitterName}</p>
                            </div>
                        </div>
                        <div className="row mt-4">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Project Name
                                </label>
                                <p className="ticket-details-p-1">{projectName}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Priority
                                </label>
                                <p className="ticket-details-p-1">{ticket.priority}</p>
                            </div>
                        </div>
                        <div className="row mt-4">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Status
                                </label>
                                <p className="ticket-details-p-1">{ticket.status}</p>
                            </div>
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Type
                                </label>
                                <p className="ticket-details-p-1">{ticket.type}</p>
                            </div>
                        </div>
                        <div className="row mt-4">
                            <div className="col-6">
                                <label className="project-details-label-1">
                                    Created
                                </label>
                                <p style={{marginBottom:'12px'}} className="ticket-details-p-1">{ticket.createdAt}</p>
                            </div>
                        </div>
                    </div>
                    <div className="col card ticket-details-card-2">
                        <div className="card">
                            <div className='d-flex'>
                                <h3 className="pt-1 px-2 d-inline" style={{marginBottom: '4px'}}>Ticket Comments</h3>
                            </div>
                            <p className="px-4">All comments for this Ticket</p>
                            <div className='d-flex p-2 pt-0'>
                                <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                    Search:
                                </label>
                                <InputText
                                    onInput={(e) =>
                                        setCommentsFilters({
                                            global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                        })
                                    }
                                    className='project-details-input-1'
                                    style={{fontSize: '12px'}}
                                />
                            </div>
                            <DataTable value={comments} stripedRows sortMode="multiple" filters={commentsFilters} tableStyle={{ minWidth: '30rem' }}
                                       paginator rows={6} style={{backgroundColor: '#111111'}} className='ticket-details-table-1'>
                                <Column field="commentatorEmail" header="Email" sortable style={{fontSize: '12px', width: '35%', padding: '2px' }}/>
                                <Column field="message" header="Message" sortable style={{fontSize: '12px', width: '45%', padding: '2px' }}/>
                                <Column field={"created" ? "created" : "just now"} header="Created" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                            </DataTable>
                        </div>
                        <label className="ticket-details-label-1">
                            Your Comment
                        </label>
                        <div className='d-flex'>
                            <textarea className='ticket-details-textarea-1' value={comment.message ? comment.message : ""} onChange={(e) => updateComment(e.target.value)}/>
                            <button className="ticket-details-button-1" onClick={() => addComment()}>Add Comment</button>
                        </div>
                    </div>
                </div>
                <div className='row mt-4'>
                    <div className='col card ticket-details-card-1'>
                        <div className='d-flex'>
                            <h3 className="pt-1 px-0 d-inline" style={{marginBottom: '4px'}}>Ticket History</h3>
                        </div>
                        <p className="px-2">All History Information for this Ticket</p>
                        <div className='d-flex p-2 pt-0'>
                            <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                Search:
                            </label>
                            <InputText
                                onInput={(e) =>
                                    setHistoryFilters({
                                        global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                    })
                                }
                                className='project-details-input-1'
                                style={{fontSize: '12px'}}
                            />
                        </div>
                        <DataTable value={historyFields} stripedRows sortMode="multiple" filters={historyFilters} tableStyle={{ minWidth: '30rem' }}
                                   paginator rows={6} style={{backgroundColor: '#111111'}} className='ticket-details-table-1'>
                            <Column field="property" header="Property" sortable style={{fontSize: '12px', width: '35%', padding: '2px' }}/>
                            <Column field="oldValue" header="Old Value" sortable style={{fontSize: '12px', width: '45%', padding: '2px' }}/>
                            <Column field="newValue" header="New Value" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                            <Column field="dateChanged" header="Changed" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                        </DataTable>
                    </div>
                    <div className='col card ticket-details-card-2'>
                        <h1>ATTACHMENTS -------> Coming Soon</h1>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TicketDetails;