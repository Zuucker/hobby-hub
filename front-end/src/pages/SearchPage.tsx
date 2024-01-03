import "../styles/SearchPage.css";
import EmptyPage from "../components/EmptyPageComponnent";
import { useEffect, useState } from "react";
import {
  Endpoints,
  SearchResult,
  SearchResultsType,
  SearchTypeEnum,
} from "../scripts/Types";
import SearchResultComponnent from "../components/SearchResultComponnent";
import SearchOptionsComponent from "../components/SearchOptionsComponent";
import { v4 as uuidv4 } from "uuid";
import Popup from "reactjs-popup";
import axiosInstance from "../scripts/AxiosInstance";
import { TextField } from "@mui/material";
import SearchMenuComponent from "../components/SearchMenuIcons";

function SearchPage() {
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>(
    window.location.href.replace("http://localhost:3000/search/", "")
  );
  const [searchType, setSearchType] = useState<SearchTypeEnum>(
    SearchTypeEnum.UGP
  );
  const [orderByAsc, setOrderByAsc] = useState<boolean>(true);
  const [shouldDisplayEmptyResults, setShouldDisplayEmptyResults] =
    useState<boolean>(false);
  const [showSearchOptions, setShowSearchOptions] = useState<boolean>(false);

  useEffect(() => {
    search();
  }, []);

  const search = () => {
    let requestData = {};

    const url = "http://localhost:3000/search/";
    window.history.pushState({}, "", url + searchQuery);

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

  const handlePressedKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      search();
    }
  };

  return (
    <EmptyPage>
      <div className="main-container container-fluid">
        <div className="main-content d-collumn">
          <div className="search-container d-flex justify-content-center">
            <TextField
              className="col-10"
              id="search-field"
              variant="outlined"
              value={searchQuery}
              placeholder="Search here for more results"
              fullWidth
              onKeyDown={handlePressedKey}
              onChange={(e: any) => {
                setSearchQuery(e.target.value);
              }}
              InputProps={{
                endAdornment: (
                  <SearchMenuComponent
                    searchFunction={search}
                    toggleOptions={setShowSearchOptions}
                  />
                ),
              }}
            />
          </div>
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
                if (target.classList.contains("modal"))
                  setShowSearchOptions(false);
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
          {searchResults.length > 0 && (
            <div className="search-bar-results-container d-flex justify-content-center">
              <div className="col-9 search-bar-results">
                {searchResults.map((r) => (
                  <div key={uuidv4()}>
                    <SearchResultComponnent
                      {...r}
                      type={r.type}
                      searchQuery={searchQuery}
                    />
                  </div>
                ))}
              </div>
            </div>
          )}
          {searchResults.length === 0 && shouldDisplayEmptyResults && (
            <div className="search-results-container d-flex justify-content-center row">
              <div className="col-8 search-bar-results">
                <div className="d-flex justify-content-center">
                  <span className="no-results">No results found!</span>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </EmptyPage>
  );
}

export default SearchPage;
