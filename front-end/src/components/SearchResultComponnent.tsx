import { SearchResult, SearchResultsType } from "../scripts/Types";
import "../styles/SearchResult.css";

type SearchResultProps = SearchResult & {
  variant?: string;
  type: SearchResultsType;
  searchQuery?: string;
};

function SearchResultComponnent(props: SearchResultProps) {
  var url;
  if (props.type === SearchResultsType.user) url = "user/" + props.url;
  if (props.type === SearchResultsType.group) url = "group/" + props.url;
  if (props.type === SearchResultsType.post) url = "post/" + props.url;

  return (
    <a href={url}>
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
            <div className="col-6">{props.postTitle}</div>
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
