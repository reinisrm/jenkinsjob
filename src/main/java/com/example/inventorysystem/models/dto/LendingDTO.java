package com.example.inventorysystem.models.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LendingDTO {
    private int lendingId;
    private LocalDate date;
    private LocalDate estimatedReturnDate;
    private boolean isReceived;
    private boolean isReturned;
    private String comments;
    private int inventoryId;
    private int borrowerId;
    private int lenderId;
}
