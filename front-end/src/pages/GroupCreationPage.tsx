import "../styles/CreateGroup.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { TextField } from "@mui/material";
import InputAdornment from "@mui/material/InputAdornment";
import CheckMark from "../icons/checkMark.svg";
import { useEffect, useState } from "react";
import * as yup from "yup";
import YupPassword from "yup-password";
import axiosInstance from "../scripts/AxiosInstance";
import { Endpoints } from "../scripts/Types";

YupPassword(yup);

const checkmarkIcon = () => {
  return {
    endAdornment: (
      <InputAdornment position="start">
        <img
          id="usernameCheckmark"
          className="checkmark"
          src={CheckMark}
          alt="checkmark"></img>
      </InputAdornment>
    ),
  };
};

const errorMessageTranslator = (message: string) => {
  if (message === "") return message;

  const keyWords = [{ old: /groupName\b/i, new: "Group Name" }];

  for (let i = 0; i < keyWords.length; i++) {
    message = message.replace(keyWords[i].old, keyWords[i].new);
  }

  return message;
};

let userSchema = yup.object({
  groupName: yup.string().required().min(3).max(20),
  groupDescription: yup.string().max(200),
});

type GroupCreationData = {
  groupName: string;
  groupDescription: string;
};

function GroupCreationPage() {
  const [message, setMessage] = useState<string>("");
  const [isNameFree, setIsNameFree] = useState<boolean>(false);
  const [inputData, setInputData] = useState<GroupCreationData>({
    groupName: "",
    groupDescription: "",
  });

  useEffect(() => {
    if (inputData.groupName !== "") {
      axiosInstance
        .post(Endpoints.checkGroupNameAvailability, inputData)
        .then((response) => {
          setIsNameFree(response.data.status);
        })
        .catch(() => {
          setMessage("Service unavailable. Try again later");
        });
    } else {
      const usernameCheckmark = document.getElementById(
        "usernameCheckmark"
      ) as HTMLElement;
      usernameCheckmark.style.visibility = "hidden";
      setIsNameFree(false);
    }
  }, [inputData, inputData.groupName]);

  useEffect(() => {
    const usernameCheckmark = document.getElementById(
      "usernameCheckmark"
    ) as HTMLElement;
    usernameCheckmark.style.visibility =
      isNameFree && inputData.groupName.length > 3 ? "visible" : "hidden";
  }, [isNameFree, inputData.groupName.length]);

  const validate = () => {
    userSchema
      .validate(inputData)
      .then(() => {
        axiosInstance.post(Endpoints.addGroup, inputData).then((response) => {
          setMessage(
            response.data.status
              ? "Group Added!"
              : "Name in use or try again later"
          );
          if (response.data.status)
            window.location.href = "/group/" + inputData.groupName;
        });
      })
      .catch((e) => {
        const message = errorMessageTranslator(e.message);
        setMessage(message);
      });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    switch (e.target.placeholder) {
      case "Name":
        setInputData({ ...inputData, groupName: e.target.value });
        break;

      case "Description":
        setInputData({ ...inputData, groupDescription: e.target.value });
        break;

      default:
        break;
    }
  };

  const handlePressedKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      validate();
    }
  };

  return (
    <EmptyPage>
      <div className="register-container d-flex justify-content-center">
        <div className="col-8">
          <span className="d-flex justify-content-center">Group Creation</span>
          <TextField
            placeholder="Name"
            InputProps={checkmarkIcon()}
            onChange={handleChange}
            className="col-12"
            onKeyDown={handlePressedKey}
          />
          <TextField
            placeholder="Description"
            onChange={handleChange}
            className="col-12"
            onKeyDown={handlePressedKey}
          />
          <span
            className={
              message !== ""
                ? "error-message d-flex justify-content-center"
                : "invisible"
            }>
            {message}
          </span>

          <div className="button-container col-12 d-flex justify-content-center">
            <div
              className="col-12 btn-purple d-flex justify-content-center align-items-center"
              onClick={() => {
                validate();
              }}>
              Create Group!
            </div>
          </div>
        </div>
      </div>
    </EmptyPage>
  );
}

export default GroupCreationPage;
