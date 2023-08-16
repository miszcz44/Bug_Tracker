import React from 'react';
const OtherErrorTemplate = () => {
    return (
        <div className="text-wrapper response-403-wrapper">
            <div className="title" data-content="404">
                An error occured
            </div>

            <div className="subtitle">
                Oops, something went wrong.
            </div>
            <div className="isi">
                Web server returned unlikely response.
            </div>

            <div className="buttons">
                <a className="button response-403-button" href="/dashboard">Go to homepage</a>
            </div>
        </div>
    );
};

export default OtherErrorTemplate;