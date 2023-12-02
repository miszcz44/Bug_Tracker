import React, {useEffect, useState} from 'react';
import {useUser} from "../UserProvider";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";

const CommentContainer = (props) => {
    const {ticketId} = props;
    const user = useUser();

    const emptyComment = {
        id: null,
        message: "",
        ticketId: ticketId
    }

    const [comment, setComment] = useState(emptyComment);
    const [comments, setComments] = useState([]);
    useEffect(() => {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/comments?ticketId=${ticketId}`, "GET", user.jwt)
            .then((commentsData) => {
                setComments(commentsData);
            });
    }, []);

    function updateComment(value) {
        const commentCopy = { ...comment }
        commentCopy.message = value;
        setComment(commentCopy);
    }

    function submitComment() {
        if(comment.message !== "") {
            grabAndAuthorizeRequestFromTheServer("/api/v1/comments", "POST", user.jwt, comment)
                .then((data) => {
                    const commentsCopy = [...comments];
                    commentsCopy.push(data);
                    setComments(commentsCopy);
                    setComment(emptyComment);
                });
        }
    }

    return (
        <>
            <div className="mt-5">
                <textarea
                    style={{width: "100%", borderRadius: "0.25em"}}
                    onChange={(e) => updateComment(e.target.value)}
                    value={comment.message}
                ></textarea>
                <button onClick={() => submitComment()}>Post comment</button>
            </div>
        </>
    );
};

export default CommentContainer;