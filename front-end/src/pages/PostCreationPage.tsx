import "../styles/CreatePost.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { TextField } from "@mui/material";
import { useEffect, useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints, GroupData } from "../scripts/Types";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { v4 as uuidv4 } from "uuid";

function PostCreationPage() {
  const [groups, setGroups] = useState<GroupData[]>([]);
  const [choosenGroupId, setChoosenGroupId] = useState<number | string>("");
  const [postType, setPostType] = useState<string>("image");
  const [postTitle, setPostTitle] = useState<string>("");
  const [choosenGroup, setChoosenGroup] = useState<GroupData>();
  const [imageContent, setImageContent] = useState<string>("");
  const [textContent, setTextContent] = useState<string>("");
  const [videoContent, setVideoContent] = useState<string | ArrayBuffer | null>(
    null
  );

  useEffect(() => {
    axiosInstance
      .post(Endpoints.getUserDataFromJwt)
      .then((response) => {
        const responseGroups = response.data.data;
        return responseGroups.id;
      })
      .then((id: number) => {
        axiosInstance
          .post(Endpoints.getUserGroups, { userId: id })
          .then((response) => {
            const responseData = response.data.data.groups;
            setGroups(responseData);
            if (responseData.length > 0) {
              setChoosenGroupId(responseData[0].id);
            } else {
              console.log("pls first join some groups");
            }
          });
      });
  }, []);

  useEffect(() => {
    setChoosenGroup(groups.find((g) => g.id === choosenGroupId));
  }, [choosenGroupId, groups]);

  const chooseFile = () => {
    document.getElementById("postInput")?.click();
  };

  const changeContent = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileReader = new FileReader();
    if (e.target.files!.length > 0)
      fileReader.readAsDataURL(e.target.files![0]);
    fileReader.onload = (e) => {
      if (postType === "image")
        setImageContent(e.target?.result ? e.target?.result.toString() : "");
      else if (postType === "video") {
        setVideoContent(e.target?.result ? e.target?.result : null);
        setTimeout(() => {
          resizeVideo();
        }, 20);
      }
    };
  };

  const addPost = () => {
    if (
      (videoContent && postType === "video") ||
      (imageContent && postType === "image") ||
      (textContent && postType === "text") ||
      (textContent && postType === "link")
    ) {
      const newPostType = postType === "link" ? "text" : postType;

      axiosInstance
        .post(Endpoints.addPost, {
          postAuthorId: 1,
          groupId: choosenGroupId,
          postTitle: postTitle,
          postType: newPostType,
          postLink: "",
          videoContent: videoContent,
          imageContent: imageContent,
          textContent: textContent,
        })
        .then((response) => {
          console.log(response);
          if (response.status === 200) {
            window.location.href = "/group/" + choosenGroup?.name;
          }
        });
    }
  };

  const resizeVideo = () => {
    const videoContainer = document.getElementById("video") as HTMLVideoElement;
    const parent = videoContainer.parentNode as HTMLElement;
    const parentwidth = parent.getBoundingClientRect().width;
    videoContainer.width = parentwidth;
  };

  const handlePressedKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      addPost();
    }
  };

  return (
    <EmptyPage>
      <div className="add-post-container col-10 d-flex justify-content-center">
        <div className="col-12">
          <span className="d-flex justify-content-center">Post Creation</span>
          <div className="d-flex justify-content-center">
            <div className="col-10">
              <div className="d-flex justify-content-between">
                <FormControl>
                  <InputLabel id="demo-simple-select-label">
                    Choose Group
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={choosenGroupId}
                    label="Age"
                    onChange={(e) => {
                      setChoosenGroupId(Number(e.target.value));
                    }}>
                    {groups.length > 0 &&
                      groups.map((g) => (
                        <MenuItem value={g.id} key={uuidv4()}>
                          {g.name}
                        </MenuItem>
                      ))}
                  </Select>
                </FormControl>

                <FormControl>
                  <InputLabel id="demo-simple-select-label">
                    Choose post type
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={postType}
                    label="Age"
                    onChange={(e) => {
                      setPostType(String(e.target.value));
                      setImageContent("");
                      setVideoContent(null);
                      setTextContent("");
                    }}>
                    <MenuItem value={"image"}>Image</MenuItem>
                    <MenuItem value={"text"}>Text</MenuItem>
                    <MenuItem value={"video"}>Video</MenuItem>
                    <MenuItem value={"link"}>Link</MenuItem>
                  </Select>
                </FormControl>
              </div>
            </div>
          </div>
          <TextField
            placeholder={"Group description"}
            value={
              choosenGroup && choosenGroup.description
                ? choosenGroup.description
                : ""
            }
            rows={2}
            size="small"
            className="col-12"
            disabled
          />
          <TextField
            placeholder="Title"
            value={postTitle}
            onKeyDown={handlePressedKey}
            onChange={(e: any) => {
              if (e.target.value.length < 80) setPostTitle(e.target.value);
            }}
            className="col-12"
          />

          {postType === "text" && (
            <TextField
              placeholder="Text"
              value={textContent}
              onChange={(e: any) => {
                setTextContent(e.target.value);
              }}
              className="col-12"
            />
          )}

          {postType === "link" && (
            <TextField
              placeholder="Link"
              value={textContent}
              onChange={(e: any) => {
                setTextContent(e.target.value);
              }}
              className="col-12"
            />
          )}

          {postType === "image" && (
            <input
              className="invisible"
              id="postInput"
              type="file"
              accept="image/*"
              onChange={changeContent}
            />
          )}

          {postType === "video" && (
            <input
              className="invisible"
              id="postInput"
              type="file"
              accept="video/mp4"
              onChange={changeContent}
            />
          )}

          {imageContent !== "" && (
            <div className="col-12 d-flex">
              <img src={imageContent} className="col" alt="content" />
            </div>
          )}

          {videoContent && (
            <div>
              <video id="video" width="640px" controls>
                <source src={videoContent.toString()} type="video/mp4" />
                Your browser does not support the video tag.
              </video>
            </div>
          )}

          {postType !== "text" && postType !== "link" && (
            <div className="button-container col d-flex justify-content-center">
              <button className="btn-purple" onClick={chooseFile}>
                {imageContent !== "" ? "Change post" : "Upload"}
              </button>
            </div>
          )}

          <div className="button-container pointer col-12 d-flex justify-content-center">
            <div
              className="col-6 btn-purple d-flex justify-content-center align-items-center"
              onClick={addPost}>
              {choosenGroup?.name ? "Add post to " + choosenGroup?.name : ""}
            </div>
          </div>
        </div>
      </div>
    </EmptyPage>
  );
}

export default PostCreationPage;
