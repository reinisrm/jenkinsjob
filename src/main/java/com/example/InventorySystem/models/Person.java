package com.example.InventorySystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "borrower")
    private List<Lending> borrowing = new ArrayList<>();

    @OneToMany(mappedBy = "lender")
    private List<Lending> lending = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Person(String name, String surname, String phoneNumber, String courseName) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.courseName = courseName;
        this.borrowing = new ArrayList<>();
        this.lending = new ArrayList<>();
    }
}
