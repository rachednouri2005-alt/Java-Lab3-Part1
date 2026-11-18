package org.touragency.model;

import org.touragency.exception.PromoCodeExpiredException;

public class CorporateSubscription extends Subscription {
    private final double corporateDiscount;

    public CorporateSubscription(String ownerName, double basePrice) {
        super(ownerName, basePrice);
        this.corporateDiscount = 10.0;
    }
    @Override
    public double calculateBillingPrice()
            throws PromoCodeExpiredException {
        double finalPrice = getBasePrice();
        finalPrice = finalPrice * (1 - corporateDiscount / 100);
        for (PromoCode promo : getAppliedPromos()) {
            promo.checkExpiration();
            double discount = promo.getDiscount();
            finalPrice = finalPrice * (1 - discount / 100);
        }
        return finalPrice;
    }
}
