package com.coding.project.airBnbapp.startegy;

import com.coding.project.airBnbapp.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory) {
        PricingStartegy pricingStartegy = new BaserPriceingStartegy();

        //apply the additional startegy
        pricingStartegy = new SurgePriceStartegy(pricingStartegy);
        pricingStartegy = new OccupancyPricingStartegy(pricingStartegy);
        pricingStartegy = new UrgencyPricingStartegy(pricingStartegy);
        pricingStartegy = new HolidayPricingStartegy(pricingStartegy);

        return pricingStartegy.calculatePrice(inventory);
    }
}
