package com.coding.project.airBnbapp.startegy;


import com.coding.project.airBnbapp.entity.Inventory;


import java.math.BigDecimal;


public interface PricingStartegy {

    BigDecimal calculatePrice(Inventory inventory);
}
