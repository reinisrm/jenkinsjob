package com.example.InventorySystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@Table(name = "lending_table")
@NoArgsConstructor
public class Lending {
    @Id
    @Column(name = "id_lending")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lendingId;
    @NotNull
    @Column(name = "Date")
    private LocalDate date;
    @NotNull
    @Column(name = "Estimated_return_date")
    private LocalDate estimatedReturnDate;
    @NotNull
    @Column(name = "isReceived")
    private boolean isReceived;
    @NotNull
    @Column(name = "isReturned")
    private boolean isReturned;
    @Column(name = "Comments")
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

    public Lending(LocalDate date, Inventory inventory, Person borrower, Person lender, LocalDate estimatedReturnDate, boolean isReceived, boolean isReturned,
                   String comments) {
        this.date = date;
        this.inventory = inventory;
        this.borrower = borrower;
        this.lender = lender;
        this.estimatedReturnDate = estimatedReturnDate;
        this.isReceived = isReceived;
        this.isReturned = isReturned;
        this.comments = comments;
    }


}
