package org.touragency.model;

import org.touragency.exception.InvalidPromoCodeException;
import org.touragency.exception.PromoCodeExpiredException;
import java.time.LocalDate;

public class PromoCode {
    private final String code;
    private final double discount;
    private final LocalDate expirationDate;

    public PromoCode(String code, double discount,
                     LocalDate expirationDate) {
        if (code == null || !code.matches("[A-Za-z0-9]+")) {
            throw new InvalidPromoCodeException("Invalid promo code!");
        }
        if (Double.isNaN(discount) || Double.isInfinite(discount)
                || discount < 0 || discount > 100) {
            throw new InvalidPromoCodeException(
                    "Discount must be between 0 and 100!");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be null!");
        }
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
    }
    public String getCode() { return code; }
    public double getDiscount() { return discount; }
    public LocalDate getExpirationDate() { return expirationDate; }

    public void checkExpiration() throws PromoCodeExpiredException {
        if (LocalDate.now().isAfter(expirationDate)) {
            throw new PromoCodeExpiredException(
                    "Promo code " + code + " has expired!");
        }
    }
}
