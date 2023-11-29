import { TextField } from "@mui/material";
import CommentIcon from "../icons/Comment.svg";
import Up from "../icons/Up.svg";
import UpClicked from "../icons/UpClicked.svg";
import Down from "../icons/Down.svg";
import DownClicked from "../icons/DownClicked.svg";
import Dots from "../icons/Dots.svg";
import { useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";

type PostToolbarProps = {
  upVotes: number;
  downVotes: number;
  id: number;
  isUpVoted: boolean;
  isDownVoted: boolean;
};

function PostToolBar(props: PostToolbarProps) {
  const [upVotes, setUpVotes] = useState<number>(props.upVotes);
  const [downVotes, setDownVotes] = useState<number>(props.downVotes);
  const [upVoted, setUpVoted] = useState<boolean>(props.isUpVoted);
  const [downVoted, setDownVoted] = useState<boolean>(props.isDownVoted);

  const upVote = () => {
    if (!upVoted) {
      setUpVotes(upVotes + 1);
      setUpVoted(true);
      axiosInstance
        .post(Endpoints.likePost, { postId: props.id })
        .then((response) => {
          console.log(response.data);
        });
    } else {
      setUpVotes(upVotes - 1);
      setUpVoted(false);
      axiosInstance
        .post(Endpoints.unLikePost, { postId: props.id })
        .then((response) => {
          console.log(response.data);
        });
    }
  };

  const downVote = () => {
    if (!downVoted) {
      console.log("donwote");
      setDownVotes(downVotes + 1);
      setDownVoted(true);
      axiosInstance
        .post(Endpoints.dislikePost, { postId: props.id })
        .then((response) => {
          console.log(response.data);
        });
    } else {
      setDownVotes(downVotes - 1);
      setDownVoted(false);
      axiosInstance
        .post(Endpoints.unDislikePost, { postId: props.id })
        .then((response) => {
          console.log(response.data);
        });
    }
  };

  return (
    <div className="post-toolbar col d-flex justify-content-between">
      <div className="d-flex">
        <div
          className="pointer d-flex justify-content-center align-items-center"
          onClick={upVote}>
          <img src={!upVoted ? Up : UpClicked}></img>
          {upVotes}
        </div>
        <div
          className="pointer d-flex justify-content-center align-items-center"
          onClick={downVote}>
          <img src={!downVoted ? Down : DownClicked}></img>
          {downVotes}
        </div>
      </div>
      <div>
        <TextField
          className="post-toolbar-comment"
          variant="standard"
          size="small"
          placeholder="Comment..."
          InputProps={{
            endAdornment: (
              <img
                src={CommentIcon}
                className="pointer"
                onClick={() => {
                  console.log("add comment");
                }}
              />
            ),
          }}
        />
      </div>
      <div className="post-toolbar-options d-flex justify-content-center align-items-center">
        <img
          src={Dots}
          className="dots pointer"
          onClick={() => {
            console.log("clicked options");
          }}
        />
      </div>
    </div>
  );
}

export default PostToolBar;
