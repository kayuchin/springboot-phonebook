import { ListGroup } from "react-bootstrap";
import ContactListItem from "./ContactListItem";

interface ContactProps {
  id: number;
  name: string;
  phoneNumber: string;
  address: string;
  avatarColor: string;
}

interface ContactPropsList {
  contactList: ContactProps[];
  openPopup: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

const ContactList = (contacts: ContactPropsList) => {
  const { contactList, openPopup } = contacts;
  return (
    <ListGroup variant="flush">
      {contactList.map((contact) => (
        <ListGroup.Item
          key={contact.id}
          id={String(contact.id)}
          action
          onClick={openPopup}
        >
          <ContactListItem
            name={contact.name}
            avatarColor={contact.avatarColor}
          />
        </ListGroup.Item>
      ))}
    </ListGroup>
  );
};

export default ContactList;
