import "../styles/PostContainer.css";
import { Endpoints, User } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";
import axiosInstance from "../scripts/AxiosInstance";

type BlockedUsersContainerProps = {
  users: User[];
};

function BlockedUsersContainer(props: BlockedUsersContainerProps) {
  const unblock = (id: number) => {
    axiosInstance
      .post(Endpoints.unBlockUser, { userId: id })
      .then((response) => {
        if (response.status === 200) {
          window.location.reload();
        }
      });
  };

  return (
    <div className="col">
      <div className="post-container d-collumn align-items-center">
        {props.users &&
          props.users.map((u) => (
            <div key={uuidv4()}>
              <a href={"http://localhost:3000/profile/" + u.username}>
                <div className="group-container container-fluid">
                  <div className="group-toolbar options">
                    <div className="d-flex justify-content-between">
                      <div className="">{u.username}</div>
                      <div className="d-flex"></div>
                    </div>
                    <div className="d-flex justify-content-center">
                      <button
                        className="col-4 btn-purple"
                        onClick={() => {
                          unblock(u.userId);
                        }}>
                        Unblock
                      </button>
                    </div>
                  </div>
                </div>
              </a>
            </div>
          ))}
        {(!props.users || !props.users.length || props.users.length === 0) && (
          <div className="d-flex justify-content-center align-items-center">
            <div>There are no blocked users here yet!</div>
          </div>
        )}
      </div>
    </div>
  );
}

export default BlockedUsersContainer;
