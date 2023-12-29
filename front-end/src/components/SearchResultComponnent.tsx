import { SearchResult, SearchResultsType } from "../scripts/Types";
import "../styles/SearchResult.css";

type SearchResultProps = SearchResult & {
  variant?: string;
  type: SearchResultsType;
  searchQuery?: string;
};

function SearchResultComponnent(props: SearchResultProps) {
  const shortTitle =
    props.postTitle?.slice(0, 30) +
    (props.postTitle && props.postTitle.length > 30 ? "..." : "");

  const newUrl =
    props.type === SearchResultsType.group && !props.url.includes("group")
      ? "group/" + props.url
      : props.url;

  return (
    <a href={"http://localhost:3000/" + newUrl}>
      <div className="search-result row d-flex align-items-center">
        {props.type === SearchResultsType.user && (
          <>
            <div className="col-6">{props.username}</div>
            <div className="col-6 d-flex justify-content-end">
              <img
                className="result-profile-picture ml-auto"
                src={props.profilePicture}
                alt="avatar"></img>
            </div>
          </>
        )}

        {props.type === SearchResultsType.group &&
          props.variant !== "small" && (
            <>
              <div className="col-6">{props.groupName}</div>
            </>
          )}

        {props.type === SearchResultsType.group &&
          props.variant === "small" && (
            <>
              <div className="col">{props.groupName}</div>
            </>
          )}

        {props.type === SearchResultsType.post && (
          <>
            <div className="col-6">{shortTitle}</div>
            <div className="col-6 d-flex justify-content-end">
              <div>{props.groupName}</div>
            </div>
          </>
        )}
      </div>
    </a>
  );
}

export default SearchResultComponnent;
