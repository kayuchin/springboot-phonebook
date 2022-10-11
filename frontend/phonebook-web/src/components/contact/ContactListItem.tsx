import { Container } from "react-bootstrap";
import ContactAvatar from "./ContactAvatar";

interface ContactListItemProps {
  name: string;
  avatarColor: string;
}

const ContactListItem = ({ name, avatarColor }: ContactListItemProps) => {
  return (
    <Container className="contact-list-item">
      <span>
        <ContactAvatar name={name} avatarColor={avatarColor} size="sm" />
      </span>
      <span>{name}</span>
    </Container>
  );
};

export default ContactListItem;
