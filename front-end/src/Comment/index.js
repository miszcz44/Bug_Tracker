import React from 'react';
import jwt_decode from "jwt-decode";
import {useUser} from "../UserProvider";

const Comment = (props) => {
    const {id, commentator, message, emitDeleteComment, emitEditComment} = props
    const user = useUser();
    const decodedJwt = jwt_decode(user.jwt);
    return (
        <div className="comment-bubble">
            <div className="d-flex gap-5" style={{ fontWeight: "bold" }}>
                <div>{`${commentator.email}`}</div>
                {decodedJwt.sub === commentator.email ? (
                    <>
                        <div
                            onClick={() => emitEditComment(id)}
                            style={{ cursor: "pointer", color: "blue" }}
                        >
                            edit
                        </div>
                        <div
                            onClick={() => emitDeleteComment(id)}
                            style={{ cursor: "pointer", color: "red" }}
                        >
                            delete
                        </div>
                    </>
                ) : (
                    <></>
                )}
            </div>
            <div>{message}</div>
        </div>
    );
};

export default Comment;