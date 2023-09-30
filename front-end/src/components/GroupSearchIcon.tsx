import SearchIcon from "@mui/icons-material/Search";

interface GroupSearchIconProps {
  searchFunction: () => void;
}

function GroupSearchIcon(props: GroupSearchIconProps) {
  return (
    <SearchIcon className="mg-glass pointer" onClick={props.searchFunction} />
  );
}

export default GroupSearchIcon;
