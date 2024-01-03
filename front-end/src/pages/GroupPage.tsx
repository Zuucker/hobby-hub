import "../styles/Group.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { useEffect, useState } from "react";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints, GroupData, Post } from "../scripts/Types";
import { TextField } from "@mui/material";
import Popup from "reactjs-popup";
import EditGroupComponent from "../components/EditGroupComponent";
import PostContainer from "../components/PostContainer";

const URLParse = require("url-parse");

function GroupPage() {
  const [groupData, setGroupData] = useState<GroupData>({
    name: "",
    description: "",
    ownerName: "",
    id: -1,
    ownerId: -1,
  });

  const [isEditingGroup, setIsEditingGroup] = useState<boolean>(false);
  const [posts, setPosts] = useState<Post[]>([]);
  const [hasJoinedGroup, setHasJoinedGroup] = useState<boolean>(false);

  useEffect(() => {
    const optionsDiv = document.getElementsByClassName(
      "options"
    )[0] as HTMLElement;

    const groupName = window.location.href.split("/").pop()?.toString()!;

    const grData = groupData;
    grData.name = decodeURI(groupName);

    setGroupData(grData);

    const navbar = document.getElementById("navbar") as HTMLElement;
    const navbarHeight = navbar.getBoundingClientRect().height;
    optionsDiv.style.top = navbarHeight + "px";

    const optionsButton = document.getElementsByClassName(
      "pointer options"
    )[0] as HTMLElement;

    const contentContainer = document.getElementsByClassName(
      "content-container"
    )[0] as HTMLElement;

    const contentContaineRight = contentContainer.getBoundingClientRect().right;
    const optionsButtonwidth = optionsButton.getBoundingClientRect().width;

    optionsButton.style.left =
      contentContaineRight - optionsButtonwidth + 20 + "px";

    window.addEventListener("resize", () => {
      const optionsButton = document.getElementsByClassName(
        "pointer options"
      )[0] as HTMLElement;

      const contentContainer = document.getElementsByClassName(
        "content-container"
      )[0] as HTMLElement;

      const contentContaineRight =
        contentContainer.getBoundingClientRect().right;
      const optionsButtonwidth = optionsButton.getBoundingClientRect().width;

      optionsButton.style.left =
        contentContaineRight - optionsButtonwidth + 10 + "px";
    });

    axiosInstance
      .post(Endpoints.getGroupData, {
        groupName: decodeURI(groupName),
      })
      .then((response) => {
        const responseData = response.data.data;
        const grData: GroupData = {
          name: groupData.name,
          description: responseData.description,
          ownerName: responseData.owner,
          id: responseData.id,
          ownerId: responseData.ownerId,
        };

        axiosInstance
          .post(Endpoints.getGroupPosts, {
            groupId: responseData.id,
          })
          .then((response) => {
            const postsData: Post[] = [];
            const responseData = response.data.data.posts;
            if (responseData && responseData.length > 0) {
              responseData.forEach((p: Post) => {
                postsData.push({
                  id: p.id,
                  authorId: p.authorId,
                  author: p.author,
                  groupId: p.groupId,
                  group: p.group,
                  title: p.title,
                  link:
                    p.type === "image"
                      ? "../media/posts/image/" + p.id + ".jpg"
                      : "../media/posts/video/" + p.id + ".mp4",
                  type: p.type,
                  upVotes: p.upVotes,
                  downVotes: p.downVotes,
                  isUpVoted: p.isUpVoted,
                  isDownVoted: p.isDownVoted,
                });
              });

              setPosts(postsData);
            }
          })
          .then(() => {
            axiosInstance
              .post(Endpoints.hasJoinedGroup, { groupId: grData.id })
              .then((response) => {
                setHasJoinedGroup(response.data.status);
              });
          });

        setGroupData(grData);
      });
  }, []);

  const joinGroup = () => {
    axiosInstance
      .post(Endpoints.subscribeToGroup, {
        groupId: groupData.id,
      })
      .then(() => {
        window.location.reload();
      });
  };

  return (
    <EmptyPage>
      <div
        className="pointer options"
        onClick={() => {
          if (!hasJoinedGroup) {
            joinGroup();
          } else {
            setIsEditingGroup(true);
          }
        }}>
        {hasJoinedGroup && (
          <div>
            <MoreVertIcon />
          </div>
        )}
        {!hasJoinedGroup && <button className="btn-purple mt-2">Join</button>}
      </div>

      <div className="group-container container-fluid">
        <div className="group-toolbar options">
          <Popup
            open={isEditingGroup}
            position={"right center"}
            onClose={() => {
              setIsEditingGroup(false);
            }}>
            <div
              className="modal d-flex justify-content-center align-items-center"
              onClick={(event: any) => {
                const target = event.target as HTMLElement;
                if (target.classList.contains("modal"))
                  setIsEditingGroup(false);
              }}>
              <div className="modal-content col-6">
                <EditGroupComponent {...groupData} />
              </div>
            </div>
          </Popup>
          <div className="d-flex justify-content-between">
            <div className="">{groupData.name}</div>
            <div className="d-flex">{groupData.ownerName}</div>
          </div>
          <div className="" id="description">
            <TextField
              className="description"
              value={groupData.description}
              disabled
              size="small"
              multiline
            />
          </div>
        </div>

        <div className="group-content row">
          <PostContainer posts={posts} />
        </div>
      </div>
    </EmptyPage>
  );
}

export default GroupPage;
