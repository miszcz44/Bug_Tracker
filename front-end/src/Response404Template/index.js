import React from 'react';
import './response403template.css';

const Response404Template = () => {
    return (
        <div className="text-wrapper response-403-wrapper">
            <div className="title" data-content="404">
                404 - RESOURCE NOT FOUND
            </div>

            <div className="subtitle">
                Oops, looks like the page you wanted to access does not exist.
            </div>
            <div className="isi">
                A web server may return a 404 Not Found HTTP status code in response to a request from a client for a
                web page or resource that does not exist.
            </div>

            <div className="buttons">
                <a className="button response-403-button" href="/dashboard">Go to homepage</a>
            </div>
        </div>
    );
};

export default Response404Template;