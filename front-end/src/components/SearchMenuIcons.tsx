import SearchIcon from "@mui/icons-material/Search";
import MoreVertIcon from "@mui/icons-material/MoreVert";

interface searchMenuProps {
  searchFunction: () => void;
  toggleOptions: (arg: boolean) => void;
}

function SearchMenuComponent(props: searchMenuProps) {
  return (
    <>
      <SearchIcon className="mg-glass pointer" onClick={props.searchFunction} />

      <MoreVertIcon
        className="dots pointer"
        onClick={() => {
          props.toggleOptions(true);
        }}
      />
    </>
  );
}

export default SearchMenuComponent;
