package org.touragency.model;

import org.touragency.exception.InvalidSubscriptionException;
import org.touragency.exception.PromoCodeExpiredException;
import java.util.ArrayList;
import java.util.List;

public abstract class Subscription {
    private final String ownerName;
    private final double basePrice;
    private final List<PromoCode> appliedPromos = new ArrayList<>();

    public Subscription(String ownerName, double basePrice) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new InvalidSubscriptionException(
                    "Owner name cannot be empty!");
        }
        if (!Double.isFinite(basePrice) || basePrice <= 0) {
            throw new InvalidSubscriptionException(
                    "Base price must be greater than zero!");
        }
        this.ownerName = ownerName;
        this.basePrice = basePrice;
    }
    public String getOwnerName() { return ownerName; }
    public double getBasePrice() { return basePrice; }
    public List<PromoCode> getAppliedPromos() {
        return List.copyOf(appliedPromos);
    }
    public void addPromo(PromoCode promo) {
        if (promo == null) {
            throw new IllegalArgumentException(
                    "Promo code cannot be null!");
        }
        appliedPromos.add(promo);
    }
    public abstract double calculateBillingPrice()
            throws PromoCodeExpiredException;
}
