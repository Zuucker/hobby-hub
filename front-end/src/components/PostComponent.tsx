import { useEffect, useState } from "react";
import { Endpoints, Post } from "../scripts/Types";
import PostToolBar from "./PostToolBar";
import { TextField } from "@mui/material";
import axiosInstance from "../scripts/AxiosInstance";

function PostComponent(props: Post) {
  const [postText, setPostText] = useState<string>("");
  const [embedURL, setEmbedUrl] = useState<string>();
  const [shouldScrap, setShouldScrap] = useState<boolean>(false);
  const [scrappedImage, setScrappedImage] = useState<string>("");

  useEffect(() => {
    if (props.type === "text") {
      fetch("../media/posts/text/" + props.id + ".txt")
        .then((response) => response.text())
        .then((data) => {
          const urlPattern =
            /^(https?:\/\/)?([\w.-]+)\.([a-zA-Z]{2,})(\/\S*)?$/;
          if (urlPattern.test(data)) {
            if (data.includes("youtube")) {
              const regex =
                /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
              const match = data.match(regex);
              if (match && match[1]) {
                setEmbedUrl("https://www.youtube.com/embed/" + match[1]);
              }
            } else {
              setPostText(data);
              setShouldScrap(true);
            }
          } else {
            setPostText(data);
          }
        })
        .catch((error) => {
          console.error("Error fetching text file:", error);
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [props]);

  useEffect(() => {
    if (shouldScrap && postText) {
      axiosInstance
        .post(Endpoints.scrap, { url: postText })
        .then((response) => {
          if (response.status === 200) {
            const responseData = response.data.data;
            if (responseData && responseData.url) {
              setScrappedImage(responseData.url);
            }
          }
        });
    }
  }, [shouldScrap, postText]);

  const shouldDisplaylink = !window.location.href.includes("post");

  return (
    <div className="post">
      <div className="col d-flex justify-content-end">
        <div className="post-author">
          <a href={"http://localhost:3000/profile/" + props.author}>
            {props.author}
          </a>
        </div>
      </div>
      <div className="col d-flex justify-content-between">
        <div className="post-title">{props.title}</div>
        <div className="">
          <a href={"http://localhost:3000/group/" + props.group}>
            {props.group}
          </a>
        </div>
      </div>

      {/* img */}
      {props.type === "image" && shouldDisplaylink && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content">
            <img src={props.link} alt="post"></img>
          </div>
        </a>
      )}

      {props.type === "image" && !shouldDisplaylink && (
        <div className="content">
          <img src={props.link} alt="post"></img>
        </div>
      )}

      {/* normal text */}
      {props.type === "text" && shouldDisplaylink && !embedURL && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content col">
            <TextField value={postText} size="small" disabled multiline />
          </div>
        </a>
      )}

      {props.type === "text" && !shouldDisplaylink && !embedURL && (
        <div className="content col">
          <TextField value={postText} disabled multiline />
        </div>
      )}

      {/* yt */}
      {props.type === "text" && shouldDisplaylink && embedURL && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content col d-flex">
            <iframe
              className="col embedded"
              src={embedURL}
              allow="autoplay; encrypted-media"
              allowFullScreen
              title="video"
            />
          </div>
        </a>
      )}

      {props.type === "text" && !shouldDisplaylink && embedURL && (
        <div className="content col d-flex">
          <iframe
            className="col embedded"
            src={embedURL}
            allow="autoplay; encrypted-media"
            allowFullScreen
            title="video"
          />
        </div>
      )}

      {/* scrapped */}
      {props.type === "text" && shouldDisplaylink && scrappedImage && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content">
            <a href={postText}>
              <img src={scrappedImage} alt="post"></img>
            </a>
          </div>
        </a>
      )}

      {props.type === "text" && !shouldDisplaylink && scrappedImage && (
        <div className="content">
          <a href={postText}>
            <img src={scrappedImage} alt="post"></img>
          </a>
        </div>
      )}

      {/* video */}
      {props.type === "video" && shouldDisplaylink && (
        <a href={"http://localhost:3000/post/" + props.id}>
          <div className="content">
            <video controls>
              <source src={props.link} type="video/mp4" />
              Your browser does not support the video tag.
            </video>
          </div>
        </a>
      )}

      {props.type === "video" && !shouldDisplaylink && (
        <div className="content">
          <video controls>
            <source src={props.link} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
        </div>
      )}

      <PostToolBar
        upVotes={props.upVotes}
        downVotes={props.downVotes}
        id={props.id}
        isUpVoted={props.isUpVoted}
        isDownVoted={props.isDownVoted}
        authorId={props.authorId}
      />
    </div>
  );
}

export default PostComponent;
