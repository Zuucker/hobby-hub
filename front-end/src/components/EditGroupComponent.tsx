import { useEffect, useState } from "react";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";
import { TextField } from "@mui/material";

type GroupDataProps = {
  name: string;
  description: string;
  ownerName: string;
  id: number;
  ownerId: number;
};

function EditGroupComponent(props: GroupDataProps) {
  const [canEdit, setCanEdit] = useState<boolean>(false);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [isLeaving, setIsLeaving] = useState<boolean>(false);
  const [newData, setNewData] = useState<GroupDataProps>({
    id: props.id,
    name: props.name,
    description: props.description,
    ownerName: props.ownerName,
    ownerId: props.ownerId,
  });

  useEffect(() => {
    axiosInstance.post(Endpoints.getUserDataFromJwt).then((response) => {
      const userId = response.data.data.id;
      setCanEdit(userId === props.ownerId);
    });
  }, []);

  const handleChange = (e: any) => {
    switch (e.target.id) {
      case "groupName":
        setNewData({ ...newData, name: e.target.value });
        break;

      case "groupDescription":
        setNewData({ ...newData, description: e.target.value });
        break;

      default:
        break;
    }
  };

  const editGroup = () => {
    axiosInstance
      .post(Endpoints.editGroup, {
        groupId: newData.id,
        groupName: newData.name,
        groupDescription: newData.description,
      })
      .then(() => {
        window.location.reload();
      });
  };

  const leaveGroup = () => {
    axiosInstance
      .post(Endpoints.leaveGroup, {
        groupId: newData.id,
      })
      .then((response) => {
        window.location.href = "/";
      });
  };

  return (
    <div className="group-options d-collumn">
      <div className="d-flex justify-content-between align-items-center">
        {!isLeaving && (
          <button
            className="btn-purple"
            onClick={() => {
              setIsLeaving(true);
              setIsEditing(false);
            }}>
            {canEdit ? "Delete " : "Leave "} Group
          </button>
        )}
        {isLeaving && (
          <>
            <span>
              {canEdit
                ? "Are you sure you want to delete the group?"
                : "Are you sure you want to leave the group?"}
            </span>
            <button className="btn-purple" onClick={leaveGroup}>
              Yes
            </button>

            <button
              className="btn-purple"
              onClick={() => {
                setIsLeaving(false);
              }}>
              No
            </button>
          </>
        )}
        {canEdit && (
          <button
            className="btn-purple"
            onClick={() => {
              setIsEditing(!isEditing);
              setIsLeaving(false);
            }}>
            Edit
          </button>
        )}
      </div>
      <div>
        <TextField
          value={newData.name}
          disabled={!isEditing}
          onChange={handleChange}
          label="Group Name"
          size="small"
          id="groupName"
        />
      </div>
      <div>
        <TextField
          value={newData.description}
          disabled={!isEditing}
          onChange={handleChange}
          label="Group Description"
          size="small"
          id="groupDescription"
        />
      </div>
      <div>
        {isEditing && (
          <button
            className="btn-purple btn-save"
            onClick={() => {
              setIsEditing(false);
              editGroup();
            }}>
            Save
          </button>
        )}
      </div>
    </div>
  );
}

export default EditGroupComponent;
