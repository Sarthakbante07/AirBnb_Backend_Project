package com.coding.project.airBnbapp.dto;

import com.coding.project.airBnbapp.entity.User;
import lombok.Data;

@Data
public class GuestDto {
    private long id;
    private User user;
    private String name;
    private String gender;
    private Integer age;
}
