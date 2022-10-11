package com.kychin.phonebook.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String avatarColor;

    public Contact(String name, String phoneNumber, String address, String avatarColor) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.avatarColor = avatarColor;
    }
}
