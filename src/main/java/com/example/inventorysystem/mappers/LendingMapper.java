package com.example.inventorysystem.mappers;

import com.example.inventorysystem.models.dto.LendingDTO;
import com.example.inventorysystem.models.*;

public class LendingMapper {
    public static LendingDTO toDTO(Lending lending) {
        LendingDTO dto = new LendingDTO();
        dto.setLendingId(lending.getLendingId());
        dto.setDate(lending.getDate());
        dto.setEstimatedReturnDate(lending.getEstimatedReturnDate());
        dto.setReceived(lending.isReceived());
        dto.setReturned(lending.isReturned());
        dto.setComments(lending.getComments());
        if (lending.getInventory() != null)
            dto.setInventoryId(lending.getInventory().getInventoryId());
        if (lending.getBorrower() != null)
            dto.setBorrowerId(lending.getBorrower().getPersonId());
        if (lending.getLender() != null)
            dto.setLenderId(lending.getLender().getPersonId());
        return dto;
    }

    public static Lending toEntity(LendingDTO dto, Inventory inventory, Person borrower, Person lender) {
        Lending lending = new Lending();
        lending.setLendingId(dto.getLendingId());
        lending.setDate(dto.getDate());
        lending.setEstimatedReturnDate(dto.getEstimatedReturnDate());
        lending.setReceived(dto.isReceived());
        lending.setReturned(dto.isReturned());
        lending.setComments(dto.getComments());
        lending.setInventory(inventory);
        lending.setBorrower(borrower);
        lending.setLender(lender);
        return lending;
    }
}
