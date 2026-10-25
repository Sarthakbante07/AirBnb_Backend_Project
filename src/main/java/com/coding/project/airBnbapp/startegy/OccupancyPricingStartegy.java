package com.coding.project.airBnbapp.startegy;

import com.coding.project.airBnbapp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class OccupancyPricingStartegy implements PricingStartegy {
    private final PricingStartegy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();

        if (occupancyRate > 0.80) {
            price = price.multiply(BigDecimal.valueOf(1.20));
        }
        return price;
    }
}
