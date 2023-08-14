import './App.css';
import {useEffect, useState} from "react";
import {useLocalState} from "./util/useLocalStorage";
import {Route, Routes} from "react-router-dom";
import Dashboard from "./ProjectManagerViewDashboard";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import TicketView from "./TicketView";
import 'bootstrap/dist/css/bootstrap.min.css';
import ProjectView from "./ProjectView/index";
import Registration from "./Registration";
import {UserProvider, useUser} from "./UserProvider";
import RoleManagement from "./RoleManagement";
import jwt_decode from "jwt-decode";
import grabAndAuthorizeRequestFromTheServer from "./Services/fetchService";
import DeveloperTicketView from "./DeveloperTicketView";
import UserProfile from "./UserProfile";
import PasswordChange from "./PasswordChange";
import AllProjectsView from "./AllProjectsView";
import ProjectDetailsView from "./ProjectDetailsView";
import AllTicketsView from "./AllTicketsView";
import TicketDetails from "./TicketDetails";
import TicketEditView from "./TicketEditView";
import EmailChange from "./EmailChange";
import TicketCreateView from "./TicketCreateView";
import ProjectManagerViewDashboard from "./ProjectManagerViewDashboard";
import DeveloperViewDashboard from "./DeveloperViewDashboard";
import NoRoleViewDashboard from "./NoRoleViewDashboard";

function App() {

    const user = useUser();
    const [role, setRole] = useState(getRoleFromJWT())


    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role;
        }
        return "null";
    }
    return (
        <Routes>
            <Route path="dashboard" element={
                <PrivateRoute>
                    <Dashboard/>
                </PrivateRoute>
            }/>
            {/*<Route*/}
            {/*    path="/tickets/:id"*/}
            {/*    element={*/}
            {/*    role.authority === "DEVELOPER" ? (*/}
            {/*    <PrivateRoute>*/}
            {/*        <DeveloperTicketView/>*/}
            {/*    </PrivateRoute>*/}
            {/*    ) : (*/}
            {/*    <PrivateRoute>*/}
            {/*        <TicketView/>*/}
            {/*    </PrivateRoute>*/}
            {/*    )*/}
            {/*    }*/}
            {/*/>*/}
            {/* BEHAVIOR BASED ON ROLES!!!!!!!*/}
            <Route
                path="/projects/:id"
                element={
                    <PrivateRoute>
                        <ProjectView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/projects/details/:id"
                element={
                    <PrivateRoute>
                        <ProjectDetailsView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/projects"
                element={
                    <PrivateRoute>
                        <AllProjectsView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/tickets/details/:ticketId"
                element={
                    <PrivateRoute>
                        <TicketDetails/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/tickets/:ticketId"
                element={
                    <PrivateRoute>
                        <TicketEditView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/projects/:projectId/tickets/:ticketId"
                element={
                    <PrivateRoute>
                        <TicketCreateView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/tickets"
                element={
                    <PrivateRoute>
                        <AllTicketsView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/user-profile"
                element={
                    <PrivateRoute>
                        <UserProfile/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/change-email"
                element={
                    <PrivateRoute>
                        <EmailChange/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/change-password"
                element={
                    <PrivateRoute>
                        <PasswordChange/>
                    </PrivateRoute>
                }
            />
            <Route
                path="user-management"
                element={
                    <PrivateRoute>
                        <RoleManagement/>
                    </PrivateRoute>
                }
            />
            <Route path="/login" element={<Login/>}/>
            <Route path="/register" element={<Registration/>}/>
            <Route path="/" element={() => {
                return <div>home</div>
            }
            }/>
        </Routes>

    );
}

export default App;
