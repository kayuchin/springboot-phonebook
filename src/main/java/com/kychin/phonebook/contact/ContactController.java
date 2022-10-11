package com.kychin.phonebook.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/contacts")
public class ContactController {

    private final ContactService contactService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping
    public void addNewContact(@RequestBody Contact contact) {
        logger.info("Create New Contact: %s".formatted(contact.toString()));
        contactService.addNewContact(contact);
    }

    @PutMapping(path = "{contactId}")
    public void updateContact(
            @PathVariable("contactId") Long contactId,
            @RequestBody Contact contact) {
        logger.info("Update Contact ID: %d -> %s".formatted(contactId, contact.toString()));
        contactService.updateContact(
                contactId,
                contact.getName(),
                contact.getPhoneNumber(),
                contact.getAddress()
        );
    }

    @DeleteMapping(path = "{contactId}")
    public void deleteContact(@PathVariable("contactId") Long contactId) {
        logger.info("Delete Contact ID: %d".formatted(contactId));
        contactService.deleteContact(contactId);
    }
}
