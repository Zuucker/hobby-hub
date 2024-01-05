import "../styles/PostContainer.css";
import { GroupData } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";
import { TextField } from "@mui/material";

type PostContainerProps = {
  groups: GroupData[];
};

function GroupContainer(props: PostContainerProps) {
  return (
    <div className="col">
      <div className="post-container d-collumn align-items-center">
        {props.groups &&
          props.groups.map((gr) => (
            <div key={uuidv4()}>
              <a href={"http://localhost:3000/group/" + gr.name}>
                <div className="group-container container-fluid">
                  <div className="group-toolbar options">
                    <div className="d-flex justify-content-between">
                      <div className="">{gr.name}</div>
                      <div className="d-flex">
                        <a
                          href={
                            "http://localhost:3000/profile/" + gr.ownerName
                          }>
                          {gr.ownerName}
                        </a>
                      </div>
                    </div>
                    <div className="" id="description">
                      <TextField
                        className="description"
                        value={gr.description}
                        disabled
                        size="small"
                        multiline
                      />
                    </div>
                  </div>
                </div>
              </a>
            </div>
          ))}
        {(!props.groups ||
          !props.groups.length ||
          props.groups.length === 0) && (
          <div className="d-flex justify-content-center align-items-center">
            <div>You haven't joined any groups yet!</div>
          </div>
        )}
      </div>
    </div>
  );
}

export default GroupContainer;
