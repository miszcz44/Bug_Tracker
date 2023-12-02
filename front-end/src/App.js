import './App.css';
import {Route, Routes} from "react-router-dom";
import Dashboard from "./DashboardView";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import 'bootstrap/dist/css/bootstrap.min.css';
import ProjectView from "./ProjectView/index";
import Registration from "./Registration";
import RoleManagement from "./RoleManagement";
import UserProfile from "./UserProfile";
import PasswordChange from "./PasswordChange";
import AllProjectsView from "./AllProjectsView";
import ProjectDetailsView from "./ProjectDetailsView";
import AllTicketsView from "./AllTicketsView";
import TicketDetails from "./TicketDetails";
import TicketEditView from "./TicketEditView";
import EmailChange from "./EmailChange";
import TicketCreateView from "./TicketCreateView";
import Response403Template from "./Response403Template";
import OtherErrorTemplate from "./OtherErrorTemplate";
import Response404Template from "./Response404Template";
import DemoUser from "./DemoUser";

function App() {
    return (
        <Routes>
            <Route path="dashboard" element={
                <PrivateRoute>
                    <Dashboard/>
                </PrivateRoute>
            }/>
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
            <Route path="/demo-user" element={<DemoUser/>}/>
            <Route path="/register" element={<Registration/>}/>
            <Route path="/403" element={<Response403Template/>}/>
            <Route path="/404" element={<Response404Template/>}/>
            <Route path="/otherError" element={<OtherErrorTemplate/>}/>
            <Route path="/" element={() => {
                return <div>home</div>
            }
            }/>
            <Route path="*" element={
            <PrivateRoute>
                <Response404Template/>
            </PrivateRoute>}
            />
        </Routes>
    );
}

export default App;
