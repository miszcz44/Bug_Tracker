import React from 'react';
import jwt_decode from "jwt-decode";
import {useLocalState} from "../util/useLocalStorage";
import {useUser} from "../UserProvider";

const Comment = (props) => {
    const {id, createdAt, commentator, message, emitDeleteComment, emitEditComment} = props
    const user = useUser();
    console.log(user.jwt);
    //const decodedJwt = jwt_decode(user.jwt);
    //console.log(decodedJwt);
    return (
        <div>
            <span style={{fontWeight: "bold"}}>
                {commentator.email}
            </span>
            {message}
            <div>
                <span onClick={() => emitEditComment(id)}>
                    update
                </span>
                <span onClick={() => emitDeleteComment(id)} style={{marginLeft: 5}}>
                    delete
                </span>
            </div>
        </div>
    );
};

export default Comment;