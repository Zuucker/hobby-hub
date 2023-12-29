import "../styles/SearchOptions.css";
import { useEffect, useState } from "react";
import { SearchTypeEnum } from "../scripts/Types";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";

interface searchOptionsProps {
  toggleAsc: (arg: boolean) => void;
  toggleSearchType: (arg: SearchTypeEnum) => void;
  searchFunction: () => void;
  togglePopup: (arg: boolean) => void;
}

function SearchOptionsComponent(props: searchOptionsProps) {
  const [order, setOrder] = useState<boolean>(true);
  const [searchType, setSearchType] = useState<SearchTypeEnum>(
    SearchTypeEnum.UGP
  );

  useEffect(() => {
    props.toggleSearchType(searchType);
  }, [searchType]);

  useEffect(() => {
    props.toggleAsc(order);
  }, [order]);

  return (
    <>
      <span>Search Options</span>
      <div className="d-collumn justify-content-center">
        <div className="d-flex justify-content-between col-12">
          <div className="col-6">
            <FormControl className="col-12">
              <InputLabel id="demo-simple-select-label">Search for</InputLabel>
              <Select
                className="col-12"
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={searchType}
                onChange={(e: any) => {
                  setSearchType(e.target.value as SearchTypeEnum);
                }}>
                <MenuItem value={SearchTypeEnum.UGP}>ALL</MenuItem>
                <MenuItem value={SearchTypeEnum.U}>Users</MenuItem>
                <MenuItem value={SearchTypeEnum.G}>Groups</MenuItem>
                <MenuItem value={SearchTypeEnum.P}>Posts</MenuItem>
                <MenuItem value={SearchTypeEnum.GP}>Groups & Posts</MenuItem>
                <MenuItem value={SearchTypeEnum.UG}>Users & Groups</MenuItem>
                <MenuItem value={SearchTypeEnum.UP}>Users & Posts</MenuItem>
              </Select>
            </FormControl>
          </div>
          <div className="col-6">
            <FormControl className="col-12">
              <InputLabel id="demo-simple-select-label">Order</InputLabel>
              <Select
                className="col-12"
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={order}
                onChange={(e: any) => {
                  setOrder(e.target.value === "true" ? true : false);
                }}>
                <MenuItem value={"true"}>Ascending</MenuItem>
                <MenuItem value={"false"}>Descending</MenuItem>
              </Select>
            </FormControl>
          </div>
        </div>
        <div className="d-flex justify-content-between col-12">
          <button
            className="btn-purple col-4"
            onClick={() => {
              props.togglePopup(false);
            }}>
            Return
          </button>
          <button
            className="btn-purple col-4"
            onClick={() => {
              props.searchFunction();
              props.togglePopup(false);
            }}>
            Search
          </button>
        </div>
      </div>
    </>
  );
}

export default SearchOptionsComponent;
