import "../styles/Group.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { useEffect, useState } from "react";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints, Post } from "../scripts/Types";
import { TextField } from "@mui/material";
import Popup from "reactjs-popup";
import EditGroupComponent from "../components/EditGroupComponent";
import PostContainer from "../components/PostContainer";

type GroupData = {
  name: string;
  description: string;
  ownerName: string;
  id: number;
  ownerId: number;
};

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

  useEffect(() => {
    const optionsDiv = document.getElementsByClassName(
      "options"
    )[0] as HTMLElement;

    const groupName = window.location.href.split("/").pop()?.toString()!;

    const grData = groupData;
    grData.name = groupName;

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

    optionsButton.style.left = contentContaineRight - optionsButtonwidth + "px";

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
        contentContaineRight - optionsButtonwidth + "px";
    });

    axiosInstance
      .post(Endpoints.getGroupData, {
        groupName: groupName,
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
            console.log(responseData);

            responseData.forEach((p: Post) => {
              postsData.push({
                id: p.id,
                authorId: p.authorId,
                author: p.author,
                groupId: p.groupId,
                group: p.group,
                title: p.title,
                link: "../media/posts/image/" + p.id + ".jpg",
                type: p.type,
                upVotes: p.upVotes,
                downVotes: p.downVotes,
                isUpVoted: p.isUpVoted,
                isDownVoted: p.isDownVoted,
              });
            });

            console.log(postsData);

            setPosts(postsData);
          });

        setGroupData(grData);
      });
  }, []);

  return (
    <EmptyPage>
      <div
        className="pointer options"
        onClick={() => {
          setIsEditingGroup(true);
        }}>
        <MoreVertIcon />
      </div>
      <div className="group-container container-fluid">
        <div className="group-toolbar options">
          <Popup
            open={isEditingGroup}
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
