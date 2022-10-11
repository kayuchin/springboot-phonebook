package com.kychin.phonebook.contact;

import com.kychin.phonebook.contact.exception.BadRequestException;
import com.kychin.phonebook.contact.exception.ContactNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public void addNewContact(Contact contact) {
        boolean existsByPhoneNumber = contactRepository.existsByPhoneNumber(contact.getPhoneNumber());
        if (existsByPhoneNumber) {
            throw new BadRequestException("Phone number already exists!");
        }
        contactRepository.save(contact);
    }

    @Transactional
    public void updateContact(Long contactId, String name, String phoneNumber, String address) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact does not exist!"));

        if (!"".equalsIgnoreCase(phoneNumber) && !Objects.equals(contact.getPhoneNumber(), phoneNumber)) {
            boolean existsByPhoneNumber = contactRepository.existsByPhoneNumber(phoneNumber);
            if (existsByPhoneNumber) {
                throw new BadRequestException("Phone number already exists!");
            }
            contact.setPhoneNumber(phoneNumber);
        }

        if (!"".equalsIgnoreCase(name) && !Objects.equals(contact.getName(), name)) {
            contact.setName(name);
        }

        if (!"".equalsIgnoreCase(address) && !Objects.equals(contact.getAddress(), address)) {
            contact.setAddress(address);
        }
    }

    public void deleteContact(Long contactId) {
        if(!contactRepository.existsById(contactId)) {
            throw new ContactNotFoundException("Contact does not exist!");
        }
        contactRepository.deleteById(contactId);
    }
}
