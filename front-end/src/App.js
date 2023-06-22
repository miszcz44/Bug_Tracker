import './App.css';
import {useEffect, useState} from "react";
import {useLocalState} from "./util/useLocalStorage";
import {Route, Routes} from "react-router-dom";
import Dashboard from "./Dashboard";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import TicketView from "./TicketView";
import 'bootstrap/dist/css/bootstrap.min.css';
import ProjectView from "./ProjectView";
import Registration from "./Registration";
import {UserProvider, useUser} from "./UserProvider";
import RoleManagement from "./RoleManagement";
import jwt_decode from "jwt-decode";
import grabAndAuthorizeRequestFromTheServer from "./Services/fetchService";
import DeveloperTicketView from "./DeveloperTicketView";

function App() {

    const user = useUser();
    const [role, setRole] = useState(getRoleFromJWT())


    function getRoleFromJWT() {
        if (user.jwt) {
            const decodedJwt = jwt_decode(user.jwt);
            console.log(decodedJwt);
            return decodedJwt.role;
        }
    }
    return (
        <Routes>
            <Route path="dashboard" element={
                <PrivateRoute>
                    <Dashboard/>
                </PrivateRoute>
            }/>
            <Route
                path="/tickets/:id"
                element={
                role.authority === "DEVELOPER" ? (
                <PrivateRoute>
                    <DeveloperTicketView/>
                </PrivateRoute>
                ) : (
                <PrivateRoute>
                    <TicketView/>
                </PrivateRoute>
                )
                }
            />
            <Route
                path="/projects/:id"
                element={
                    <PrivateRoute>
                        <ProjectView/>
                    </PrivateRoute>
                }
            />
            <Route
                path="/projects/:id/tickets/:ticketId"
                element={
                    <PrivateRoute>
                        <TicketView/>
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
