import React, {useEffect, useState} from 'react';
import SideBar from "../SideBar";
import './ProjectDetails.css'
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {InputText} from "primereact/inputtext";
import {FilterMatchMode} from "primereact/api";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";

const ProjectDetailsView = () => {

    const user = useUser();
    const projectId = window.location.href.split("/projects/details/")[1]
    const [project, setProject] = useState({});
    const [projectPersonnel, setProjectPersonnel] = useState([]);
    const [projectTickets, setProjectTickets] = useState([]);
    const [personnelFilters, setPersonnelFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    const [ticketFilters, setTicketFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });
    let editUrl = '/projects/';

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/details/${projectId}`, "GET", user.jwt)
            .then((response) => {
                setProject(response.project);
                setProjectPersonnel(response.projectPersonnel);
                setProjectTickets(response.tickets);
                console.log(response);
            });
    }, []);

    const actionBodyTemplate = (rowData) => {
        let url = "/tickets/details/"
        return <div>
            <Link to={url.concat(rowData.id)}>Details</Link>
        </div>
    };

    function createNewTicket() {
        grabAndAuthorizeRequestFromTheServer("/api/v1/ticket", "POST", user.jwt, {
            "id": projectId
        })
            .then((ticket) => {
                window.location.href = `/tickets/${ticket.id}`;
            });
    }

    return (
        <div>
            <SideBar/>
            <div className="card project-details-card-1">
                <h2 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                    Details for project - {project.name}
                </h2>
                <div className='d-inline py-1'>
                    <Link className="px-3" to={editUrl.concat(projectId)}>Edit</Link>
                    <Link onClick={() => createNewTicket()}>Create New Ticket</Link>
                </div>
                <div className="container">
                    <div className="row project-details-row-1 d-inline">
                        <div className="col-sm">
                            <label className="project-details-label-1">
                                Project Description
                            </label>
                            <p className="project-details-p-1">{project.description}</p>
                        </div>
                    </div>
                </div>
                <div className='card project-details-card-2'>
                    <div class="container">
                        <div class="row">
                            <div class="card project-details-card-2 col-5">
                                    <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Assigned Personnel
                                </h3>
                                <p className="project-details-p-2">Current users on this project</p>
                                <div className='d-flex p-2 pt-0'>
                                    <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                        Search:
                                    </label>
                                    <InputText
                                        onInput={(e) =>
                                            setPersonnelFilters({
                                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                            })
                                        }
                                        className='project-details-input-1'
                                        style={{fontSize: '12px'}}
                                    />
                                </div>
                                <DataTable value={projectPersonnel} stripedRows sortMode="multiple" filters={personnelFilters} tableStyle={{ minWidth: '30rem' }}
                                           paginator rows={6} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                                    <Column field="wholeName" header="Name" sortable style={{fontSize: '12px', width: '35%', padding: '2px' }}/>
                                    <Column field="email" header="Email" sortable style={{fontSize: '12px', width: '45%', padding: '2px' }}/>
                                    <Column field="srole" header="Role" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                </DataTable>
                            </div>
                            <div class="card project-details-card-2 col-7">
                                <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Tickets for this project
                                </h3>
                                <p className="project-details-p-2">Condensed ticket details</p>
                                <div className='d-flex p-2 pt-0'>
                                    <label className='project-details-label-2' style={{fontSize: '12px'}}>
                                        Search:
                                    </label>
                                    <InputText
                                        onInput={(e) =>
                                            setTicketFilters({
                                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                                            })
                                        }
                                        className='project-details-input-1'
                                        style={{fontSize: '12px'}}
                                    />
                                </div>
                                <DataTable value={projectTickets} stripedRows sortMode="multiple" filters={ticketFilters} tableStyle={{ minWidth: '30rem' }}
                                           paginator rows={8} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                                    <Column field="title" header="Title" sortable style={{fontSize: '12px', width: '23%', padding: '2px' }}/>
                                    <Column field="submitter" header="Submitter" sortable style={{fontSize: '12px', width: '23%', padding: '2px' }}/>
                                    <Column field="developer" header="Developer" sortable style={{fontSize: '12px', width: '23%', padding: '2px' }} />
                                    <Column field="status" header="Status" sortable style={{fontSize: '12px', width: '10%', padding: '2px' }} />
                                    <Column field="created" header="Created" sortable style={{fontSize: '12px', width: '20%', padding: '2px' }} />
                                    <Column field="id" style={{padding: '2px', fontSize: '12px', paddingRight: '5px' }} body={actionBodyTemplate} />
                                </DataTable>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProjectDetailsView;