package com.kychin.phonebook.contact;

import com.kychin.phonebook.contact.exception.BadRequestException;
import com.kychin.phonebook.contact.exception.ContactNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;
    private ContactService underTest;

    @BeforeEach
    void setup() {
        underTest = new ContactService(contactRepository);
    }

    @Test
    void canGetAllContacts() {
        // when
        underTest.getAllContacts();
        // then
        verify(contactRepository).findAll();
    }

    @Test
    void canAddNewContact() {
        // given
        Contact contact = new Contact(
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );

        // when
        underTest.addNewContact(contact);

        // then
        ArgumentCaptor<Contact> contactArgumentCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(contactRepository).save(contactArgumentCaptor.capture());
        Contact savedContact = contactArgumentCaptor.getValue();
        assertThat(savedContact).isEqualTo(contact);
    }

    @Test
    void willThrowErrorWhenPhoneNumberAlreadyExists() {
        // given
        Contact contact = new Contact(
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );

        given(contactRepository.existsByPhoneNumber(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewContact(contact))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Phone number already exists!");

        verify(contactRepository, never()).save(any());
    }

    @Test
    void canUpdateContactPhoneNumber() {
        // given
        long id = 10;
        Contact contact = new Contact(
                id,
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );
        given(contactRepository.findById(id)).willReturn(Optional.of(contact));

        // when
        String newPhoneNumber = "+6012-1234568";
        underTest.updateContact(id, contact.getName(), newPhoneNumber, contact.getAddress());

        // then
        Contact updatedContact = contactRepository.findById(id).get();
        assertThat(updatedContact.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    void cannotUpdateContactPhoneNumber() {
        // given
        long id = 10;
        Contact contact = new Contact(
                id,
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );

        given(contactRepository.findById(id)).willReturn(Optional.of(contact));
        given(contactRepository.existsByPhoneNumber(anyString()))
                .willReturn(true);

        // when
        String newPhoneNumber = "+6012-1234568";

        // then
        assertThatThrownBy(() -> underTest.updateContact(id, contact.getName(), newPhoneNumber, contact.getAddress()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Phone number already exists!");
    }

    @Test
    void canUpdateContactName() {
        // given
        long id = 10;
        Contact contact = new Contact(
                id,
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );
        given(contactRepository.findById(id)).willReturn(Optional.of(contact));

        // when
        String newName = "SquidWard";
        underTest.updateContact(id, newName, contact.getPhoneNumber(), contact.getAddress());

        // then
        Contact updatedContact = contactRepository.findById(id).get();
        assertThat(updatedContact.getName()).isEqualTo(newName);
    }

    @Test
    void canUpdateContactAddress() {
        // given
        long id = 10;
        Contact contact = new Contact(
                id,
                "SpongeBob",
                "+6012-1234567",
                "Bikini Bottom Pineapple House",
                "yellow"
        );
        given(contactRepository.findById(id)).willReturn(Optional.of(contact));

        // when
        String newAddress = "Bikini Bottom SquidWard House";
        underTest.updateContact(id, contact.getName(), contact.getPhoneNumber(), newAddress);

        // then
        Contact updatedContact = contactRepository.findById(id).get();
        assertThat(updatedContact.getAddress()).isEqualTo(newAddress);
    }

    @Test
    void canDeleteContact() {
        // given
        long id = 10;
        given(contactRepository.existsById(id))
                .willReturn(true);

        // when
        underTest.deleteContact(id);

        // then
        verify(contactRepository).deleteById(id);
    }

    @Test
    void willThrowErrorWhenDeleteContactNotFound() {
        // given
        long id = 10;
        given(contactRepository.existsById(id))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteContact(id))
                .isInstanceOf(ContactNotFoundException.class)
                .hasMessageContaining("Contact does not exist!");

        verify(contactRepository, never()).deleteById(any());
    }
}