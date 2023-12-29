import TextField from "@mui/material/TextField";
import SearchMenuComponent from "./SearchMenuIcons";
import { useState, useEffect } from "react";
import SearchResultComponnent from "./SearchResultComponnent";
import {
  Endpoints,
  SearchResult,
  SearchResultsType,
  SearchTypeEnum,
} from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";
import axiosInstance from "../scripts/AxiosInstance";
import Popup from "reactjs-popup";
import SearchOptionsComponent from "./SearchOptionsComponent";

function SearchBar() {
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [searchUrl, setSearchUrl] = useState<string>("");
  const [searchType, setSearchType] = useState<SearchTypeEnum>(
    SearchTypeEnum.UGP
  );
  const [orderByAsc, setOrderByAsc] = useState<boolean>(true);
  const [shouldDisplayEmptyResults, setShouldDisplayEmptyResults] =
    useState<boolean>(false);
  const [showSearchOptions, setShowSearchOptions] = useState<boolean>(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
  };

  const closeSearchResultsFunction = (event: MouseEvent) => {
    const searchResults = document.getElementsByClassName(
      "search-bar-results"
    )[0] as HTMLElement;

    const searchResultsRect = searchResults?.getBoundingClientRect();
    const clickX = event.clientX;
    const clickY = event.clientY;

    if (searchResults) {
      if (
        clickX > searchResultsRect.left &&
        clickX < searchResultsRect.right &&
        clickY > searchResultsRect.top &&
        clickY < searchResultsRect.bottom
      ) {
      } else {
        setSearchResults([]);
        window.removeEventListener("click", closeSearchResultsFunction);
        setShouldDisplayEmptyResults(false);
      }
    }
  };

  window.addEventListener("click", closeSearchResultsFunction);

  useEffect(() => {
    setSearchUrl("/search/" + searchQuery);
  }, [searchQuery]);

  useEffect(() => {
    if (searchResults.length > 12) {
      setSearchResults(searchResults.slice(0, 12));
    }
  }, [searchResults]);

  const search = () => {
    if (searchQuery === "") {
      window.location.href = "/search/";
      return;
    }

    let requestData = {};

    if (searchType === SearchTypeEnum.G) {
      requestData = { ...requestData, groupsQuery: searchQuery };
    }

    if (searchType === SearchTypeEnum.U) {
      requestData = { ...requestData, usersQuery: searchQuery };
    }

    if (searchType === SearchTypeEnum.P) {
      requestData = { ...requestData, titleQuery: searchQuery };
    }

    if (searchType === SearchTypeEnum.GP) {
      requestData = {
        ...requestData,
        titleQuery: searchQuery,
        groupsQuery: searchQuery,
      };
    }

    if (searchType === SearchTypeEnum.UG) {
      requestData = {
        ...requestData,
        groupsQuery: searchQuery,
        usersQuery: searchQuery,
      };
    }

    if (searchType === SearchTypeEnum.UGP) {
      requestData = {
        ...requestData,
        titleQuery: searchQuery,
        groupsQuery: searchQuery,
        usersQuery: searchQuery,
      };
    }

    if (searchType === SearchTypeEnum.UP) {
      requestData = {
        ...requestData,
        titleQuery: searchQuery,
        usersQuery: searchQuery,
      };
    }

    requestData = { ...requestData, sortBy: orderByAsc ? "ASC" : "DESC" };

    axiosInstance.post(Endpoints.search, requestData).then((response) => {
      const responseData = response.data.data;
      if (responseData.searchResults) {
        const data: SearchResult[] = [];

        responseData.searchResults.forEach((result: SearchResult) => {
          let url = "";
          if (result.type === SearchResultsType.user) {
            url = "profile/" + result.username;
          }

          if (result.type === SearchResultsType.group) {
            url = "group/" + result.groupName;
          }

          if (result.type === SearchResultsType.post) {
            url = "post/" + result.postId;
          }

          const newResult: SearchResult = {
            ...result,
            url: url,
          };
          data.push(newResult);
        });

        setSearchResults(data);
      } else if (response.status === 200) {
        setShouldDisplayEmptyResults(true);
      }
    });
  };

  return (
    <div className="search-bar-container">
      <Popup
        closeOnDocumentClick={false}
        open={showSearchOptions}
        onClose={(e: any) => {
          setShowSearchOptions(false);
        }}>
        <div
          className="modal d-flex justify-content-center align-items-center"
          onClick={(event: any) => {
            const target = event.target as HTMLElement;
            if (target.classList.contains("modal")) setShowSearchOptions(false);
          }}>
          <div className="modal-content d-flex justify-content-center col-6">
            <SearchOptionsComponent
              toggleAsc={setOrderByAsc}
              toggleSearchType={setSearchType}
              togglePopup={setShowSearchOptions}
              searchFunction={search}
            />
          </div>
        </div>
      </Popup>
      <TextField
        id="search-field"
        variant="outlined"
        value={searchQuery}
        placeholder="Search new stuff! :)"
        fullWidth
        onChange={handleChange}
        InputProps={{
          endAdornment: (
            <SearchMenuComponent
              searchFunction={search}
              toggleOptions={setShowSearchOptions}
            />
          ),
        }}
      />
      {searchResults.length > 0 && (
        <div className="search-bar-results-container d-flex justify-content-center row">
          <div className="col-8 search-bar-results">
            {searchResults.map((r) => (
              <div key={uuidv4()}>
                <SearchResultComponnent
                  {...r}
                  type={r.type}
                  searchQuery={searchQuery}
                />
              </div>
            ))}
            <div className="col d-flex justify-content-center mb-2 mt-3">
              <a href={searchUrl}>
                <button className="col-6 btn-purple">See more results!</button>
              </a>
            </div>
          </div>
        </div>
      )}
      {searchResults.length === 0 && shouldDisplayEmptyResults && (
        <div className="search-bar-results-container d-flex justify-content-center row">
          <div className="col-8 search-bar-results">
            <div className="d-flex justify-content-center">
              <span className="no-results">No results found!</span>
            </div>
            <div className="col d-flex justify-content-center mb-2 mt-3">
              <a href={searchUrl}>
                <button className="col-6 btn-purple">See more results!</button>
              </a>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default SearchBar;
