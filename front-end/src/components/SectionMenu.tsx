import "../styles/SectionMenu.css";
import { TextField } from "@mui/material";
import { useEffect, useState } from "react";
import GroupSearchIcon from "./GroupSearchIcon";
import SearchResultComponnent from "./SearchResultComponnent";
import { Endpoints, SearchResult, SearchResultsType } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";
import axiosInstance from "../scripts/AxiosInstance";

interface SectionMenuProps {
  isLoggedIn: boolean;
}

function SectionMenu(props: SectionMenuProps) {
  const [groupSearchQuery, setGroupSearchQuery] = useState<string>("");
  const [groups, setGroups] = useState<SearchResult[]>([]);
  const [groupsToDisplay, setGroupsToDisplay] = useState<SearchResult[]>([]);

  useEffect(() => {
    if (props.isLoggedIn) {
      axiosInstance.post(Endpoints.getUserGroups).then((response) => {
        const responseGroups = response.data.data.groups;

        if (!responseGroups) return;

        const grs: SearchResult[] = [];
        responseGroups.forEach((gr: any) => {
          const group: SearchResult = {
            groupName: gr.name.substring(0, 15),
            url: gr.name,
            type: SearchResultsType.group,
          };
          grs.push(group);
        });

        setGroups(grs);
      });
    } else {
      axiosInstance.post(Endpoints.getTopGroups).then((response) => {
        const responseGroups = response.data.data.groups;

        if (!responseGroups) return;

        const grs: SearchResult[] = [];
        responseGroups.forEach((gr: any) => {
          const group: SearchResult = {
            groupName: gr.name.substring(0, 15),
            url: gr.name,
            type: SearchResultsType.group,
          };
          grs.push(group);
        });

        setGroups(grs);
      });
    }
  }, []);

  useEffect(() => {
    if (groups.length > 0) {
      setGroupsToDisplay(groups);
    }
  }, [groups]);

  useEffect(() => {
    if (groupsToDisplay.length > 9)
      setGroupsToDisplay(groupsToDisplay.slice(0, 9));
  }, [groupsToDisplay]);

  const searchGroups = () => {
    const grs = groups.filter((gr) => {
      if (gr.groupName?.includes(groupSearchQuery)) return gr;
    });
    setGroupsToDisplay(grs);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setGroupSearchQuery(e.target.value);
  };

  return (
    <div className="section-menu-container col">
      {props.isLoggedIn && (
        <TextField
          className="col"
          placeholder="Search groups"
          value={groupSearchQuery}
          onChange={handleChange}
          InputProps={{
            endAdornment: <GroupSearchIcon searchFunction={searchGroups} />,
          }}
        />
      )}
      {!props.isLoggedIn && (
        <span className="col d-flex justify-content-center">
          Top 10 Groups!
        </span>
      )}
      <div className="group-search-results">
        {groupsToDisplay.map((g) => (
          <SearchResultComponnent {...g} variant="small" key={uuidv4()} />
        ))}
      </div>
      <div className=" d-flex justify-content-center">
        <div className="group-button-container">
          <a href={"/search/" + groupSearchQuery}>
            <button className="col-12 btn-purple">See more groups</button>
          </a>
        </div>
      </div>
    </div>
  );
}

export default SectionMenu;
