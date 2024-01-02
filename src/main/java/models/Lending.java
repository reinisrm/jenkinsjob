package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "lending_table")
@AllArgsConstructor
@NoArgsConstructor
public class Lending {
    @Id
    @Column(name = "id_lending")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lendingId;
    @Column(name = "Datums")
    private LocalDate date;
    @Column(name = "Datums Atgriezt")
    private LocalDate estimatedReturnDate;
    @Column(name = "vaiSanemts")
    private boolean isReceived;
    @Column(name = "vaiAtgriezts")
    private boolean isReturned;
    @Column(name = "Komentāri")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Person borrower;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private Person lender;


}
