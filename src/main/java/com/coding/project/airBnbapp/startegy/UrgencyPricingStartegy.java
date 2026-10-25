package com.coding.project.airBnbapp.startegy;

import com.coding.project.airBnbapp.entity.Inventory;
import lombok.RequiredArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;


@RequiredArgsConstructor
public class UrgencyPricingStartegy implements PricingStartegy {

    private final PricingStartegy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        LocalDate today = LocalDate.now();
        if (inventory.getDate().isBefore(today.plusDays(7))) {
            price = price.multiply(BigDecimal.valueOf(1.90));
        }
        return price;
    }
}
