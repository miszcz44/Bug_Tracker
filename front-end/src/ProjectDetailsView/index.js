import React, {useEffect, useState} from 'react';
import SideBar from "../SideBar";
import './ProjectDetails.css'
import {Link} from "react-router-dom";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import {useUser} from "../UserProvider";

const ProjectDetailsView = () => {

    const user = useUser();
    const projectId = window.location.href.split("/projects/details/")[1]
    const[project, setProject] = useState({});

    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/project/details/${projectId}`, "GET", user.jwt)
            .then((projectResponse) => {
                let projectData = projectResponse.project;
                if (projectData.title === null);
                setProject(projectData);
                console.log(projectResponse);
            });
    }, []);

    return (
        <div style={{backgroundColor: '#efefef', height: '753px'}}>
            <SideBar/>
            <div className="card project-details-card-1">
                <h2 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                    Details for project
                </h2>
                <Link className="px-3" to='/' onClick={() => console.log(project.projectPersonnel.srole)}>Edit</Link>
                <div className="container">
                    <div className="row project-details-row-1">
                        <div className="col-sm">
                            <label className="project-details-label-1">
                                Project Name
                            </label>
                            <p className="project-details-p-1">One of three columns</p>
                        </div>
                        <div className="col-sm">
                            <label className="project-details-label-1">
                                Project Description
                            </label>
                            <p className="project-details-p-1">One of three columns. somebocy once told me the world is gonna roll me. i aint the sharpest tool in the shed. she was looking kind of dumb with her finger and her thumb with her head going down on her forehead</p>
                        </div>
                    </div>
                </div>
                <div className='card project-details-card-2'>
                    <div class="container">
                        <div class="row">
                            <div class="card project-details-card-2 col-4">
                                <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Assigned Personnel
                                </h3>
                                <p className="project-details-p-2">Current users on this project</p>
                            </div>
                            <div class="card project-details-card-2 col-8">
                                <h3 className="pt-2 px-2" style={{marginBottom: '4px'}}>
                                    Tickets for this project
                                </h3>
                                <p className="project-details-p-2">Condensed ticket details</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProjectDetailsView;