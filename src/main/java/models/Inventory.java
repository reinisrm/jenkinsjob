package models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "inventory_table")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @Column(name = "inventory_id")
    private int inventoryId;

}
