package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "inventory_table")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @Column(name = "id_inventory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventoryId;
    @Column(name = "Iekārta")
    private String device;
    @Column(name = "Inventāra numurs")
    private String inventoryNumber;
    @Column(name = "Kabinets")
    private String room;

    @OneToMany(mappedBy = "inventory")
    private List<Lending> lending;

}
