import React, {useEffect, useState} from 'react';
import {useUser} from "../UserProvider";
import grabAndAuthorizeRequestFromTheServer from "../Services/fetchService";
import Comment from "../Comment/index.js";

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

    function handleEditComment(commentId) {
        const i = comments.findIndex((comment) => comment.id === commentId);
        const commentCopy = {
            id: comments[i].id,
            message: comments[i].message,
            ticketId: comments[i].ticket.id
        }
        setComment(commentCopy);
    }

    function handleDeleteComment(commentId) {
        grabAndAuthorizeRequestFromTheServer(`/api/v1/comments/${commentId}`, "DELETE", user.jwt)
            .then((msg) => {
                const commentsCopy = [...comments];
                const i = commentsCopy.findIndex((comment) => comment.id === commentId);
                commentsCopy.splice(i, 1);
                setComments(commentsCopy);
                console.log(comments);
            });
    }

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
        console.log(comments)
    }

    function submitComment() {
        if(comment.id && comment.message !== "") {
            grabAndAuthorizeRequestFromTheServer(`/api/v1/comments/${comment.id}`, "PUT", user.jwt, comment.message)
                .then((data) => {
                    const commentsCopy = [...comments];
                    console.log(comment.message);
                    const i = commentsCopy.findIndex((comment) => comment.id === data.id);
                    commentsCopy[i] = data;
                    setComments(commentsCopy);
                    setComment(emptyComment);
                });
        }
        else if(comment.message !== "") {
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