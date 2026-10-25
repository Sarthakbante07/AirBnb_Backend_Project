package com.coding.project.airBnbapp.startegy;

import com.coding.project.airBnbapp.entity.Inventory;

import java.math.BigDecimal;

public class BaserPriceingStartegy implements PricingStartegy {
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
