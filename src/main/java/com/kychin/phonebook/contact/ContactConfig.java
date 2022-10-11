package com.kychin.phonebook.contact;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ContactConfig {

    @Bean
    CommandLineRunner commandLineRunner(ContactRepository contactRepository) {
        return args -> {
            Contact spongeBob = new Contact(
                    "SpongeBob",
                    "+6012-1234567",
                    "Bikini Bottom Pineapple House",
                    "yellow"
            );
            Contact patrick = new Contact(
                    "Patrick",
                    "+6011-12345678",
                    "Bikini Bottom Stone House",
                    "pink"
            );
            Contact sandy = new Contact(
                    "Sandy",
                    "+6018-88888888",
                    "Bikini Bottom Tree Under The Sea",
                    "brown"
            );
            contactRepository.saveAll(List.of(spongeBob, patrick, sandy));
        };
    }
}
