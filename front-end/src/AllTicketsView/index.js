import React, {useEffect, useState} from 'react';
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
import jwt_decode from "jwt-decode";

const AllTicketsView = () => {
    const user = useUser();
    const [tickets, setTickets] = useState([]);
    const [filters, setFilters] = useState({
        global: {value: null, matchMode: FilterMatchMode.CONTAINS}
    });

    function getEmailFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            return decodedJwt.sub;
        }
        return "null";
    }

    const actionBodyTemplate = (rowData) => {
        let detailsUrl = "/tickets/details/"
        let editUrl = "/tickets/"
        return <div>
            <Link style={{textDecoration: 'none'}} className='px-4' to={detailsUrl.concat(rowData.id)}>Details </Link>
            {
                getEmailFromJWT() === rowData.projectManagerEmail || getEmailFromJWT() === rowData.submitterEmail ||
                    getRoleFromJWT() === "ADMIN" || getRoleFromJWT() === "DEMO_ADMIN" ?
                <Link style={{textDecoration: 'none'}} className='all-projects-span-1' to={editUrl.concat(rowData.id)}>Edit</Link>
                :
                <></>
            }
        </div>
    };

    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            return decodedJwt.role.authority;
        }
        return "null";
    }

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/ticket`, "GET", user.jwt)
            .then((response) => {
                setTickets(response);
            });
    }, []);
    return (
        <div className='all-projects-div-1'>
            <SideBar/>
            <div className="card all-projects-card-1">
                <h2 className='p-2'>Your tickets</h2>
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
                <DataTable value={tickets} stripedRows sortMode="multiple" filters={filters} tableStyle={{ minWidth: '50rem' }}
                           paginator rows={6} style={{backgroundColor: '#111111'}} className='all-projects-table-1'>
                    <Column field="title" header="Title" sortable style={{ width: '22%', padding: '5px' }}/>
                    <Column field="projectName" header="Project Name" sortable style={{ width: '18%', padding: '5px' }}/>
                    <Column field="developerName" style={{ width: '15%', padding: '5px' }} header="Developer Name" sortable />
                    <Column field="priority" style={{ width: '5%', padding: '5px' }} header="Priority" sortable />
                    <Column field="status" style={{ width: '15%', padding: '5px' }} header="Status" sortable />
                    <Column field="type" style={{ width: '15%', padding: '5px' }} header="Type" sortable />
                    <Column field="created" style={{ width: '15%', padding: '5px' }} header="Created" sortable />
                    <Column field="id" style={{ width: '15%', padding: '5px' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate} />
                </DataTable>
            </div>
        </div>
    );
};

export default AllTicketsView;