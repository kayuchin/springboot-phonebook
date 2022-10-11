package com.kychin.phonebook.contact;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkIfContactPhoneNumberExists() {
        // given
        String phoneNumber = "+6012-1234567";
        Contact contact = new Contact(
                "SpongeBob",
                phoneNumber,
                "Bikini Bottom Pineapple House",
                "yellow"
        );
        underTest.save(contact);

        // when
        boolean expected = underTest.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void checkIfContactPhoneNumberDoesNotExists() {
        // given
        String phoneNumber = "+6012-1234567";

        // when
        boolean expected = underTest.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(expected).isFalse();
    }
}