import React from 'react';
import './response403template.css';

const Response403Template = () => {
    return (
        <div className="text-wrapper response-403-wrapper">
            <div className="title" data-content="404">
                403 - ACCESS DENIED
            </div>

            <div className="subtitle">
                Oops, You don't have permission to access this page.
            </div>
            <div className="isi">
                A web server may return a 403 Forbidden HTTP status code in response to a request from a client for a
                web page or resource to indicate that the server can be reached and understood the request, but refuses
                to take any further action. Status code 403 responses are the result of the web server being configured
                to deny access, for some reason, to the requested resource by the client.
            </div>

            <div className="buttons">
                <a className="button response-403-button" href="/dashboard">Go to homepage</a>
            </div>
        </div>
    );
};

export default Response403Template;