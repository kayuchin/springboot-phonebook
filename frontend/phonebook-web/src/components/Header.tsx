import PermContactCalendarIcon from "@mui/icons-material/PermContactCalendar";
import SearchIcon from "@mui/icons-material/Search";
import {
  Button,
  Container,
  FormControl,
  InputGroup,
  Navbar,
} from "react-bootstrap";

interface HeaderProps {
  onSearch: (event: React.ChangeEvent<HTMLInputElement>) => void;
  openPopup: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

const Header = ({ onSearch, openPopup }: HeaderProps) => {
  return (
    <Navbar>
      <Container
        style={{ justifyContent: "flex-start" }}
        className="contact-header"
      >
        <span>
          <PermContactCalendarIcon />
        </span>
        <span>Contacts</span>
        <span>
          <InputGroup>
            <InputGroup.Text id="search-addon">
              <SearchIcon />
            </InputGroup.Text>
            <FormControl
              id="search-box"
              size="sm"
              placeholder="Search..."
              aria-label="Search"
              aria-describedby="search-addon"
              onChange={onSearch}
            />
          </InputGroup>
        </span>
        <Button size="sm" onClick={openPopup}>
          New Contact
        </Button>
      </Container>
    </Navbar>
  );
};

export default Header;
