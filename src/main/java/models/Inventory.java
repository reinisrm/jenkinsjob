package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "inventory_table")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventoryId;
    @Column(name = "Datums")
    private LocalDate date;
    @Column(name = "Vards")
    private String name;
    @Column(name = "Uzvards")
    private String surname;
    @Column(name = "Telefona Nr.")
    private String phoneNumber;
    @Column(name = "Kurss")
    private String courseName;
    @Column(name = "Iekārta")
    private String device;
    @Column(name = "Inventāra numurs")
    private String inventoryNumber;
    @Column(name = "Kabinets")
    private String room;
    @Column(name = "vaiSanemts")
    private boolean isReceived;
    @Column(name = "vaiAtgriezts")
    private boolean isReturned;
    @Column(name = "Komentāri")
    private String comments;


}
