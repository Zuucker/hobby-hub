import "../styles/SectionMenu.css";
import { TextField } from "@mui/material";
import { useEffect, useState } from "react";
import GroupSearchIcon from "./GroupSearchIcon";
import SearchResultComponnent from "./SearchResultComponnent";
import { SearchResult, SearchResultsType } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";

interface SectionMenuProps {
  isLoggedIn: boolean;
}

function SectionMenu(props: SectionMenuProps) {
  const [groupSearchQuery, setGroupSearchQuery] = useState<string>("");
  const [groups, setGroups] = useState<SearchResult[]>([]);

  useEffect(() => {
    const group: SearchResult = {
      groupName: "group",
    };
    setGroups([group, group, group, group]);
  }, []);

  const searchGroups = () => {
    console.log("searchgroups");
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
        {groups.map((g) => (
          <SearchResultComponnent
            type={SearchResultsType.group}
            {...g}
            variant="small"
            key={uuidv4()}
          />
        ))}
      </div>
      <div className=" d-flex justify-content-center">
        <div className="group-button-container">
          <a href={"Xd"}>
            <button className="col-12 btn-purple">See more groups</button>
          </a>
        </div>
      </div>
    </div>
  );
}

export default SectionMenu;
