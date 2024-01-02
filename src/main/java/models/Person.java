package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "person_table")
@NoArgsConstructor
public class Person {
    @Id
    @Column(name = "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @Column(name = "Vards")
    private String name;
    @Column(name = "Uzvards")
    private String surname;
    @Column(name = "Telefona Nr")
    private String phoneNumber;
    @Column(name = "Kurss")
    private String courseName;

    @OneToMany(mappedBy = "borrower")
    private List<Lending> borrowing;

    @OneToMany(mappedBy = "lender")
    private List<Lending> lending;

    public Person(String name, String surname, String phoneNumber, String courseName) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.courseName = courseName;
    }
}
