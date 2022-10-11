import { LocalPhone } from "@mui/icons-material";
import PersonIcon from "@mui/icons-material/Person";
import HomeIcon from "@mui/icons-material/Home";
import {
  Button,
  Col,
  Container,
  Form,
  FormControl,
  InputGroup,
  Modal,
  Row,
} from "react-bootstrap";

import ContactAvatar from "./ContactAvatar";

type ButtonAction = "Save" | "Update";
interface ContactModalProps {
  id: number;
  name: string;
  phoneNumber: string;
  address: string;
  avatarColor: string;
  show: boolean;
  buttonText: ButtonAction;
  buttonAction: (event: React.MouseEvent<HTMLButtonElement>) => Promise<void>;
  handleNameChanges: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handlePhoneChanges: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handleAddressChanges: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handleDelete: (event: React.MouseEvent<HTMLButtonElement>) => Promise<void>;
  closePopup: () => void;
}

const ContactModal = (contact: ContactModalProps) => {
  const {
    name,
    phoneNumber,
    address,
    avatarColor,
    show,
    buttonText,
    buttonAction,
    handleNameChanges,
    handlePhoneChanges,
    handleAddressChanges,
    handleDelete,
    closePopup,
  } = contact;
  return (
    <Modal show={show} onHide={closePopup}>
      <Modal.Header closeButton></Modal.Header>
      <Modal.Body>
        <Container>
          <Form>
            <ContactAvatar name={name} avatarColor={avatarColor} size="lg" />
            <InputGroup className="my-3">
              <InputGroup.Text id="name-addon">
                <PersonIcon />
              </InputGroup.Text>
              <FormControl
                required
                placeholder="Enter name"
                aria-label="Name"
                aria-describedby="name-addon"
                value={name}
                onChange={handleNameChanges}
              />
            </InputGroup>
            <InputGroup className="mb-3">
              <InputGroup.Text id="phone-addon">
                <LocalPhone />
              </InputGroup.Text>
              <FormControl
                required
                placeholder="Enter phone number"
                aria-label="Phone Number"
                aria-describedby="phone-addon"
                value={phoneNumber}
                onChange={handlePhoneChanges}
              />
            </InputGroup>
            <InputGroup className="mb-3">
              <InputGroup.Text id="address-addon">
                <HomeIcon />
              </InputGroup.Text>
              <FormControl
                required
                placeholder="Enter address"
                aria-label="Address"
                aria-describedby="address-addon"
                value={address}
                onChange={handleAddressChanges}
              />
            </InputGroup>
            <Row>
              <Col className="d-grid gap-2">
                <Button variant="primary" type="submit" onClick={buttonAction}>
                  {buttonText}
                </Button>
                {buttonText === "Save" ? (
                  ""
                ) : (
                  <Button variant="danger" onClick={handleDelete}>
                    Delete
                  </Button>
                )}
              </Col>
            </Row>
          </Form>
        </Container>
      </Modal.Body>
    </Modal>
  );
};

export default ContactModal;
