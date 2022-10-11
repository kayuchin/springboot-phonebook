import React, { useEffect, useState } from "react";
import axios, { AxiosResponse } from "axios";
import { Alert } from "react-bootstrap";

import Header from "./components/Header";
import ContactList from "./components/contact/ContactList";
import ContactModal from "./components/contact/ContactModal";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

type ButtonAction = "Save" | "Update";
type AlertText = "Success!" | "Oh Snap! Something Wrong!";
interface ContactProps {
  id: number;
  name: string;
  phoneNumber: string;
  address: string;
  avatarColor: string;
}

function App() {
  // hooks for contact lists
  const [contacts, setContacts] = useState<ContactProps[]>([]);
  const [searched, setSearched] = useState<ContactProps[]>([]);

  // hooks for alert
  const [showAlert, setShowAlert] = useState<boolean>(false);
  const [alertText, setAlertText] = useState<AlertText>("Success!");

  // hooks for modal
  const [showModal, setShowModal] = useState<boolean>(false);
  const [buttonText, setButtonText] = useState<ButtonAction>("Save");
  const [modalContact, setModalContact] = useState<ContactProps>({
    id: 0,
    name: "",
    phoneNumber: "",
    address: "",
    avatarColor: "",
  });

  // search function
  const filterContactList = async (
    searchValue: string,
    contacts: ContactProps[]
  ) => {
    const currentSearched = contacts.filter(
      (contact) => contact.name.toLowerCase().indexOf(searchValue) > -1
    );
    setSearched(currentSearched);
  };

  const onSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const searchValue = event.currentTarget.value.toLowerCase();
    if (searchValue === "") {
      setSearched(contacts);
      return;
    }
    filterContactList(searchValue, contacts);
  };

  // modal function
  const openAddPopup = (event: React.MouseEvent<HTMLButtonElement>): void => {
    event.preventDefault();

    const randomColor = "#" + Math.floor(Math.random() * 16777215).toString(16);

    setModalContact({
      id: 0,
      name: "",
      phoneNumber: "",
      address: "",
      avatarColor: randomColor,
    });
    setButtonText("Save");
    setShowModal(true);
  };

  const openEditPopup = (event: React.MouseEvent<HTMLButtonElement>): void => {
    event.preventDefault();

    const button: HTMLButtonElement = event.currentTarget;
    const id = parseInt(button.id);
    const selectedContact = contacts.filter((contact) => contact.id === id)[0];

    setModalContact(selectedContact);
    setButtonText("Update");
    setShowModal(true);
  };

  const closePopup = (): void => {
    setShowModal(false);
  };

  // edit modal fields function
  const handleNameChanges = (
    event: React.ChangeEvent<HTMLInputElement>
  ): void => {
    const newModalContact: ContactProps = { ...modalContact };
    const updatedValue = event.currentTarget.value;
    newModalContact.name = updatedValue;
    setModalContact(newModalContact);
  };

  const handlePhoneChanges = (
    event: React.ChangeEvent<HTMLInputElement>
  ): void => {
    const newModalContact: ContactProps = { ...modalContact };
    const updatedValue = event.currentTarget.value;
    newModalContact.phoneNumber = updatedValue;
    setModalContact(newModalContact);
  };

  const handleAddressChanges = (
    event: React.ChangeEvent<HTMLInputElement>
  ): void => {
    const newModalContact: ContactProps = { ...modalContact };
    const updatedValue = event.currentTarget.value;
    newModalContact.address = updatedValue;
    setModalContact(newModalContact);
  };

  // consume API
  // GET
  const getAllContacts = async (): Promise<ContactProps[]> => {
    const result = await axios.get<ContactProps[]>(
      "http://localhost:8080/api/v1/contacts"
    );
    const allContacts = result.data;
    const sortedContacts = allContacts.sort((a, b) =>
      a.name.localeCompare(b.name)
    );
    return sortedContacts;
  };

  // POST / PUT
  const addOrEditContacts = async (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();

    const newContact = { ...modalContact };
    let axiosRequest: Promise<AxiosResponse<any, any>>;

    if (buttonText === "Save") {
      axiosRequest = axios.post(
        "http://localhost:8080/api/v1/contacts",
        newContact
      );
    } else {
      const id = newContact.id;
      axiosRequest = axios.put(
        `http://localhost:8080/api/v1/contacts/${id}`,
        newContact
      );
    }

    try {
      await axiosRequest;
      setAlertText("Success!");
    } catch (error) {
      setAlertText("Oh Snap! Something Wrong!");
    }

    setShowModal(false);
    setShowAlert(true);

    const allContacts = await getAllContacts();
    setContacts(allContacts);

    const searchValue = (
      document.getElementById("search-box") as HTMLInputElement
    ).value;
    await filterContactList(searchValue, allContacts);
  };

  // DELETE
  const deleteContact = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    const contactToBeDeleted = { ...modalContact };
    const id = contactToBeDeleted.id;

    try {
      await axios.delete(`http://localhost:8080/api/v1/contacts/${id}`);
      setAlertText("Success!");
    } catch (error) {
      setAlertText("Oh Snap! Something Wrong!");
    }

    setShowModal(false);
    setShowAlert(true);

    const allContacts = await getAllContacts();
    setContacts(allContacts);

    const searchValue = (
      document.getElementById("search-box") as HTMLInputElement
    ).value;
    await filterContactList(searchValue, allContacts);
  };

  const initializeContacts = async () => {
    const allContacts = await getAllContacts();
    setContacts(allContacts);
    setSearched(allContacts);
  };

  useEffect(() => {
    document.title = "Phone Book App";
    initializeContacts();
  }, []);

  return (
    <div className="App">
      <Header onSearch={onSearch} openPopup={openAddPopup} />
      <div className="contact-alert">
        <Alert
          show={showAlert}
          variant={alertText === "Success!" ? "success" : "danger"}
          onClose={() => setShowAlert(false)}
          dismissible
        >
          {alertText}
        </Alert>
      </div>
      <ContactModal
        {...modalContact}
        show={showModal}
        buttonText={buttonText}
        buttonAction={addOrEditContacts}
        handleNameChanges={handleNameChanges}
        handlePhoneChanges={handlePhoneChanges}
        handleAddressChanges={handleAddressChanges}
        handleDelete={deleteContact}
        closePopup={closePopup}
      />
      <ContactList contactList={searched} openPopup={openEditPopup} />
    </div>
  );
}

export default App;
