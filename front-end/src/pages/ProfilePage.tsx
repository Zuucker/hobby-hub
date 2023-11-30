import "../styles/Profile.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { TextField } from "@mui/material";
import { useEffect, useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints, GroupData, Post } from "../scripts/Types";
import CancelIcon from "../icons/CancelIcon.svg";
import EditIcon from "../icons/EditIcon.svg";
import SaveIcon from "../icons/checkMark.svg";
import PostContainer from "../components/PostContainer";
import GroupContainer from "../components/GroupContainer";

type ProfileData = {
  username: string;
  email: string;
  password: string;
  newPassword: string;
  profilePic: string;
  bio: string;
  age: number;
};

function ProfilePage() {
  const [displayPosts, setDisplayPosts] = useState<boolean>(true);
  const [isForeignProfile, setIsForeignProfile] = useState<boolean>(false);
  const [isEditing, setIsEditing] = useState<boolean>(false);

  const [posts, setPosts] = useState<Post[]>([]);
  const [groups, setGroups] = useState<GroupData[]>([]);

  const [userData, setUserData] = useState<ProfileData>({
    username: "",
    email: "",
    password: "",
    newPassword: "",
    profilePic: "",
    bio: "",
    age: 0,
  });

  const [inputData, setInputData] = useState<ProfileData>({
    username: "",
    email: "",
    password: "",
    newPassword: "",
    profilePic: "",
    bio: "",
    age: 0,
  });

  useEffect(() => {
    const username = window.location.pathname.split("/").pop();

    axiosInstance.post(Endpoints.getUsernameFromJwt).then((response) => {
      setIsForeignProfile(response.data.value === username);
    });

    axiosInstance
      .post(Endpoints.getUserData, { username: username })
      .then((response) => {
        const recievedData = response.data.data as ProfileData;

        const registerDate = new Date(response.data.data.registerDate);
        const date = new Date();
        const age: number = Math.floor(
          (date.getTime() - registerDate.getTime()) / (1000 * 60 * 60 * 24)
        );

        recievedData.age = age;
        recievedData.profilePic = "../" + response.data.data.profilePic;

        setUserData(recievedData);
        setInputData(recievedData);
      });

    axiosInstance.post(Endpoints.getUserPosts).then((response) => {
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
            link: "../media/posts/image/" + p.id + ".jpg",
            type: p.type,
            upVotes: p.upVotes,
            downVotes: p.downVotes,
            isUpVoted: p.isUpVoted,
            isDownVoted: p.isDownVoted,
          });
        });

        setPosts(postsData);
      }
    });

    axiosInstance.post(Endpoints.getUserGroups).then((response) => {
      const groupData: GroupData[] = [];
      const responseData = response.data.data.groups;
      if (responseData && responseData.length > 0) {
        responseData.forEach((g: any) => {
          groupData.push({
            id: g.id,
            name: g.name,
            description: g.description,
            ownerId: g.ownerId,
            ownerName: g.owner,
          });
        });
      }

      setGroups(groupData);
    });
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    switch (e.target.id) {
      case "username":
        setInputData({ ...inputData, username: e.target.value });
        break;

      case "password":
        setInputData({ ...inputData, password: e.target.value });
        break;

      case "bio":
        setInputData({ ...inputData, bio: e.target.value });
        break;

      case "confirmPassword":
        setInputData({ ...inputData, newPassword: e.target.value });
        break;

      default:
        break;
    }
  };

  const selectAvatar = () => {
    const fileChooser = document.getElementById("avatarInput") as HTMLElement;
    fileChooser.click();
  };

  const changeAvatar = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileReader = new FileReader();
    if (e.target.files!.length > 0)
      fileReader.readAsDataURL(e.target.files![0]);
    fileReader.onload = (e) => {
      setInputData({
        ...inputData,
        profilePic: e.target?.result ? e.target?.result.toString() : "",
      });
    };
  };

  const sendData = () => {
    axiosInstance.post(Endpoints.editUser, inputData).then((response) => {});
  };

  return (
    <EmptyPage>
      <div className="profile-container d-collumn col-11 align-items-center justify-content-center p-0">
        <div className="profile-info d-flex col">
          <span className="age-container">{inputData.age + " days"}</span>
          <div className="col-3">
            <img
              src={inputData.profilePic}
              alt="avatar"
              className={isEditing ? "blurred" : ""}
            />
            {isEditing && (
              <div className="d-flex justify-content-center">
                <img
                  src={EditIcon}
                  alt="edit avatar"
                  className="edit-avatar light-background"
                  onClick={selectAvatar}
                />
                <input
                  id="avatarInput"
                  type="file"
                  accept="image/png, image/jpeg, image/gif"
                  className="disabled"
                  onChange={changeAvatar}
                />
              </div>
            )}
          </div>
          <div className="col-9 d-flex justify-content-center">
            <TextField
              className="col"
              id="bio"
              multiline={true}
              value={inputData.bio}
              disabled={!isEditing}
              rows={4}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="edit-profile d-flex col">
          <div className="col-6 d-flex justify-content-center">
            <TextField
              id="username"
              value={inputData.username}
              disabled={!isEditing}
              label="Username"
              InputLabelProps={{ shrink: false }}
              onChange={handleChange}
            />
          </div>
          <div className="col-6 d-flex justify-content-center">
            <TextField
              id="email"
              value={inputData.email}
              disabled
              label="Email"
              InputLabelProps={{ shrink: false }}
              onChange={handleChange}
            />
          </div>
          {isForeignProfile && (
            <>
              <div className="d-flex align-items-center">
                <img
                  src={isEditing ? CancelIcon : EditIcon}
                  className="icon"
                  alt="edit profile"
                  onClick={() => {
                    if (isEditing) {
                      setInputData(userData);
                    }
                    setIsEditing(!isEditing);
                  }}
                />
              </div>
            </>
          )}
        </div>
        {isEditing && (
          <>
            <div className="edit-password d-flex col">
              <div className="col-6 d-flex justify-content-center">
                <TextField
                  id="password"
                  value={inputData.password}
                  disabled={!isEditing}
                  placeholder="Password"
                  type="password"
                  onChange={handleChange}
                />
              </div>
              <div className="col-6 d-flex justify-content-center">
                <TextField
                  id="confirmPassword"
                  value={inputData.newPassword}
                  disabled={!isEditing}
                  placeholder="Confirm Password"
                  type="password"
                  onChange={handleChange}
                />
              </div>
              <div className="d-flex align-items-center">
                <img
                  src={SaveIcon}
                  className="icon"
                  alt="save changes"
                  onClick={() => {
                    console.log(inputData);
                    setUserData(inputData);
                    setIsEditing(false);
                    sendData();
                  }}
                />
              </div>
            </div>
          </>
        )}

        <div className="tab-profile d-flex col">
          <div className="col-6 d-flex justify-content-center">
            <div
              className={
                displayPosts
                  ? "btn-purple d-flex justify-content-center align-items-center"
                  : "btn-purple btn-purple-outline d-flex justify-content-center align-items-center"
              }
              onClick={() => {
                setDisplayPosts(true);
              }}>
              <div className="group-content row">Posts</div>
            </div>
          </div>
          <div className="col-6 d-flex justify-content-center">
            <div
              className={
                !displayPosts
                  ? "btn-purple d-flex justify-content-center align-items-center"
                  : "btn-purple btn-purple-outline d-flex justify-content-center align-items-center"
              }
              onClick={() => {
                setDisplayPosts(false);
              }}>
              Groups
            </div>
          </div>
        </div>
        {displayPosts ? (
          <PostContainer posts={posts} />
        ) : (
          <GroupContainer groups={groups} />
        )}
      </div>
    </EmptyPage>
  );
}

export default ProfilePage;
