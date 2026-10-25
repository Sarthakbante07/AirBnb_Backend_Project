package com.coding.project.airBnbapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {
    private String city;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private Integer roomsCount;

    private Integer page = 0;
    private Integer size = 10;


}
