package com.example.inventorysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "person_table")
@NoArgsConstructor
public class Person {
    @Id
    @Column(name = "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @Column(name = "Name")
    private String name;
    @Column(name = "Surname")
    private String surname;
    @Column(name = "Phone_Number")
    private String phoneNumber;
    @Column(name = "Course_Name")
    private String courseName;

    public Person(String name, String surname, String phoneNumber, String courseName) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.courseName = courseName;
    }
}