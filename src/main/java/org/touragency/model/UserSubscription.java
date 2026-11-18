package org.touragency.model;

import org.touragency.exception.PromoCodeExpiredException;

public class UserSubscription extends Subscription {
    public UserSubscription(String ownerName, double basePrice) {
        super(ownerName, basePrice);
    }
    @Override
    public double calculateBillingPrice()
            throws PromoCodeExpiredException {
        double finalPrice = getBasePrice();
        for (PromoCode promo : getAppliedPromos()) {
            promo.checkExpiration();
            double discount = promo.getDiscount();
            finalPrice = finalPrice * (1 - discount / 100);
        }
        return finalPrice;
    }
}
