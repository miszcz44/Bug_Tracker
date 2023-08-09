import React, {useEffect, useState} from 'react';
import './AllProjects.css'
import SideBar from "../SideBar";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column"
import {FilterMatchMode} from "primereact/api";
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css"
import {InputText} from "primereact/inputtext";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";
import {Link} from "react-router-dom";

const AllProjectsView = () => {

    const user = useUser();
    const [projects, setProjects] = useState([]);
    const [filters, setFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });

    function deleteProject(id) {
        for(let i = 0; i < projects.length; i++) {
            if(projects[i].id === id) {
                projects.splice(i, 1);
            }
        }
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project`, "DELETE", user.jwt, id);
    }

    const actionBodyTemplate = (rowData) => {
        let detailsUrl = "/projects/details/"
        let editUrl = "/projects/"
        return <div>
            <Link style={{textDecoration: 'none'}} className='px-4' to={detailsUrl.concat(rowData.id)}>Details </Link>
            <Link className='d-block all-projects-span-1'to={editUrl.concat(rowData.id)}>Edit</Link>
        </div>
    };

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project`, "GET", user.jwt)
            .then((response) => {
                setProjects(response);
            });
    }, []);

    function createNewProject() {
        grabAndAuthorizeRequestFromTheServer("api/v1/project", "POST", user.jwt)
            .then((project) => {
                window.location.href = `/projects/${project.id}`;
            });
    }

    return (
        <div className='all-projects-div-1'>
            <SideBar/>
            <button className="all-projects-button-1" onClick={() => createNewProject()}>Create new project</button>
            <div className="card all-projects-card-1">
                <h3 className='p-2'>Your projects</h3>
                <div className='d-flex p-2'>
                    <label className='all-projects-label-1' style={{fontSize: '12px'}}>
                        Search:
                    </label>
                    <InputText
                        onInput={(e) =>
                            setFilters({
                                global: { value: e.target.value, matchMode: FilterMatchMode.CONTAINS}
                            })
                        }
                        className='all-projects-input-1'
                        style={{fontSize: '12px'}}
                    />
                </div>
                <DataTable value={projects} stripedRows sortMode="multiple" filters={filters} tableStyle={{ minWidth: '50rem' }}
                           paginator rows={5} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                    <Column field="name" header="Name" sortable style={{ width: '20%', padding: '10px' }}/>
                    <Column field="description" header="Description" sortable style={{ width: '65%', padding: '10px' }}/>
                    <Column field="id" style={{ width: '15%', padding: '10px' }} headerStyle={{ width: '5rem', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate} />
                </DataTable>
            </div>
        </div>
    );
};

export default AllProjectsView;