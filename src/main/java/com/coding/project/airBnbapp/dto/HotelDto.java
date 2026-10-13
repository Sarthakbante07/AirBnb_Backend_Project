package com.coding.project.airBnbapp.dto;

import com.coding.project.airBnbapp.entity.HotelContactInfo;
import lombok.Data;


@Data
public class HotelDto {

    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private Boolean active;
    private HotelContactInfo contact_info;
}
