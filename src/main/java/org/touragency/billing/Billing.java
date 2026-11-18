package org.touragency.billing;

import org.touragency.exception.*;
import org.touragency.model.Subscription;
import org.touragency.service.NotificationService;
import java.util.HashMap;
import java.util.Map;

public class Billing {
    private final Map<String, Subscription> database = new HashMap<>();
    private final Map<String, Double> balances = new HashMap<>();
    private final NotificationService notificationService;

    public Billing(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void registerSubscription(String email, Subscription sub)
            throws DuplicateSubscriptionException {
        if (email == null ||
                !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidEmailException("Invalid email address!");
        }
        if (sub == null) {
            throw new IllegalArgumentException("Subscription cannot be null!");
        }
        if (database.containsKey(email)) {
            throw new DuplicateSubscriptionException(
                    "Customer with email " + email + " already exists!");
        }
        database.put(email, sub);
        balances.put(email, 0.0);
        System.out.println("Customer registered: " + email);
    }

    public void deposit(String email, double amount)
            throws SubscriptionNotFoundException {
        if (!database.containsKey(email)) {
            throw new SubscriptionNotFoundException(
                    "Customer not found: " + email);
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be positive!");
        }
        balances.put(email, balances.get(email) + amount);
    }

    public void processPaymentByEmail(String email)
            throws SubscriptionNotFoundException,
                   PromoCodeExpiredException,
                   InsufficientFundsException {
        Subscription sub = database.get(email);
        if (sub == null) {
            throw new SubscriptionNotFoundException(
                    "Customer with email " + email + " is not registered!");
        }
        double price = sub.calculateBillingPrice();
        double balance = balances.get(email);
        if (balance < price) {
            throw new InsufficientFundsException(
                    "Insufficient funds! Required: " + price +
                    " RUB, available: " + balance + " RUB.");
        }
        balances.put(email, balance - price);
        System.out.printf(
                "Payment successful! Customer: %s, Amount: %.2f RUB%n",
                sub.getOwnerName(), price);
        notificationService.sendNotification(email,
                "Payment successful! Amount: " + price + " RUB.");
    }
}
