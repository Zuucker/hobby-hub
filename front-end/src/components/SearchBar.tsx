import TextField from "@mui/material/TextField";
import SearchMenuComponent from "./SearchMenuIcons";
import { useState, useEffect } from "react";
import SearchResultComponnent from "./SearchResultComponnent";
import { SearchResult, SearchResultsType } from "../scripts/Types";
import { v4 as uuidv4 } from "uuid";

function SearchBar() {
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [searchUrl, setSearchUrl] = useState<string>("");

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
      }
    }
  };

  window.addEventListener("click", closeSearchResultsFunction);

  useEffect(() => {
    setSearchUrl("/search/" + searchQuery);
  }, [searchQuery]);

  const search = () => {
    setTimeout(() => {
      const result: SearchResult = {
        username: "user",
        groupName: "group",
        profilePicture: "profile_pic.jpg",
        postTitle: "post",
        url: "link",
      };
      const arr = [result, result, result, result, result];
      setSearchResults(arr);
    }, 500);
  };

  return (
    <div className="search-bar-container">
      <TextField
        id="search-field"
        variant="outlined"
        value={searchQuery}
        placeholder="Search new stuff!"
        fullWidth
        onChange={handleChange}
        InputProps={{
          endAdornment: <SearchMenuComponent searchFunction={search} />,
        }}
      />
      {searchResults.length > 0 && (
        <div className="search-bar-results-container d-flex justify-content-center row">
          <div className="col-8 search-bar-results">
            {searchResults.map((r) => (
              <div key={uuidv4()}>
                <SearchResultComponnent
                  {...r}
                  type={SearchResultsType.post}
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
    </div>
  );
}

export default SearchBar;
