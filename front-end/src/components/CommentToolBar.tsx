import { TextField } from "@mui/material";
import ReplyIcon from "../icons/Reply.svg";
import Up from "../icons/Up.svg";
import UpClicked from "../icons/UpClicked.svg";
import Down from "../icons/Down.svg";
import DownClicked from "../icons/DownClicked.svg";
import Dots from "../icons/Dots.svg";
import { useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";

type CommentToolbarProps = {
  id: number;
  points: number;
  interacted: boolean;
  upvoted: boolean;
};

function CommentToolBar(props: CommentToolbarProps) {
  const [points, setPoints] = useState<number>(props.points);
  const [interacted, setInteracted] = useState<boolean>(props.interacted);
  const [upvoted, setUpVoted] = useState<boolean>(props.upvoted);
  const [comment, setComment] = useState<string>("");

  const interactWithComment = (up: boolean) => {
    if (interacted) {
      if (up === upvoted)
        if (up) {
          setPoints(points - 1);
          setInteracted(false);
        } else {
          setInteracted(false);
          setPoints(points + 1);
        }
      else {
        if (up) {
          setUpVoted(true);
          setPoints(points + 2);
        } else {
          setUpVoted(false);
          setPoints(points - 2);
        }
        setInteracted(true);
      }
    } else {
      if (up) {
        setUpVoted(true);
        setPoints(points + 1);
      } else {
        setUpVoted(false);
        setPoints(points - 1);
      }
      setInteracted(true);
    }
    axiosInstance.post(Endpoints.interactWithComment, {
      commentId: props.id,
      isCommentLiked: up,
    });
  };

  const addSubComment = () => {
    if (comment && comment.length > 0) {
      axiosInstance
        .post(Endpoints.addComment, {
          commentId: props.id,
          content: comment,
        })
        .then((response) => {
          if (response.status === 200) {
            window.location.reload();
          }
        });
      setComment("");
    }
  };

  return (
    <div className="comment-toolbar col d-flex justify-content-between">
      <div className="d-flex">
        <div
          className="pointer d-flex justify-content-center align-items-center"
          onClick={() => {
            interactWithComment(true);
          }}>
          <img
            src={interacted && upvoted ? UpClicked : Up}
            alt="Upvote subcomment"></img>
        </div>
        <div className="points">{points}</div>
        <div
          className="pointer d-flex justify-content-center align-items-center"
          onClick={() => {
            interactWithComment(false);
          }}>
          <img
            src={interacted && !upvoted ? DownClicked : Down}
            alt="Downvote subcomment"></img>
        </div>
      </div>
      <div className="col">
        <TextField
          className="sub-comment-toolbar"
          variant="outlined"
          size="small"
          placeholder="Comment..."
          value={comment}
          onChange={(e) => {
            setComment(e.target.value);
          }}
          InputProps={{
            endAdornment: (
              <img
                src={ReplyIcon}
                className="pointer"
                onClick={addSubComment}
                alt="Add subcomment"
              />
            ),
          }}
        />
      </div>
      <div className="comment-toolbar-options d-flex justify-content-center align-items-center">
        <img
          src={Dots}
          className="dots pointer"
          onClick={() => {
            console.log("clicked options");
          }}
          alt="Comment options"
        />
      </div>
    </div>
  );
}

export default CommentToolBar;
