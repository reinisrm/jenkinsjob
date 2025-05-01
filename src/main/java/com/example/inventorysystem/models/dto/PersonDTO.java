package com.example.inventorysystem.models.dto;

import lombok.Data;

@Data
public class PersonDTO {
    private int personId;
    private String name;
    private String surname;
    private String phoneNumber;
    private String courseName;
    private int userId;
}
