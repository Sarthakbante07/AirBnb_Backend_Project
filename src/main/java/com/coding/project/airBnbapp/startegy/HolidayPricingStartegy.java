package com.coding.project.airBnbapp.startegy;

import com.coding.project.airBnbapp.entity.Inventory;
import com.coding.project.airBnbapp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStartegy implements PricingStartegy {

    private final PricingStartegy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isTodayHolday = true;  // call API or Check with local date
        if (isTodayHolday) {
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
