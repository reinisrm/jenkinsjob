package com.example.inventorysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "inventory_table")
@NoArgsConstructor
public class Inventory {
    @Id
    @Column(name = "id_inventory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventoryId;
    @Column(name = "Device")
    private String device;
    @Column(name = "Inventory_Number")
    private String inventoryNumber;
    @Column(name = "Room")
    private String room;
    @Column(name = "Cabinet")
    private String cabinet;

    public Inventory(String device, String inventoryNumber, String room, String cabinet) {
        this.device = device;
        this.inventoryNumber = inventoryNumber;
        this.room = room;
        this.cabinet = cabinet;
    }
}