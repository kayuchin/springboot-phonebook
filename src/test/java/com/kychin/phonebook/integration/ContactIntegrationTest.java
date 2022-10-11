package com.kychin.phonebook.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kychin.phonebook.contact.Contact;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.kychin.phonebook.contact.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
class ContactIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    private final Faker faker = new Faker();

    @Test
    void canAddNewContact() throws Exception {
        // given
        Contact contact = new Contact(
                String.format("%s %s",
                        faker.name().firstName(),
                        faker.name().lastName()),
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress(),
                faker.color().name()
        );

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)));

        // then
        resultActions.andExpect(status().isOk());
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts)
                .usingElementComparatorIgnoringFields("id")
                .contains(contact);
    }

    @Test
    void canDeleteStudent() throws Exception {
        // given
        Contact contact = new Contact(
                String.format("%s %s",
                        faker.name().firstName(),
                        faker.name().lastName()),
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress(),
                faker.color().name()
        );

        mockMvc.perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());

        MvcResult getContactsResult = mockMvc.perform(get("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getContactsResult
                .getResponse()
                .getContentAsString();

        List<Contact> contacts = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = contacts.stream()
                .filter(c -> c.getPhoneNumber().equals(contact.getPhoneNumber()))
                .map(Contact::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Contact with Phone Number: %s Not Found!".formatted(contact.getPhoneNumber())));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/contacts/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = contactRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
