package org.touragency;

import org.touragency.billing.Billing;
import org.touragency.exception.*;
import org.touragency.model.*;
import org.touragency.service.EmailNotificationService;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TOUR AGENCY LAB 3 ===");
        System.out.println("Variant 13");
        Billing billing = new Billing(new EmailNotificationService());
        try {
            PromoCode summer = new PromoCode(
                    "SUMMER10", 10, LocalDate.of(2027, 12, 31));
            PromoCode vip = new PromoCode(
                    "VIP20", 20, LocalDate.of(2027, 12, 31));
            UserSubscription user1 =
                    new UserSubscription("Alice", 10000);
            user1.addPromo(summer);
            user1.addPromo(vip);
            UserSubscription user2 =
                    new UserSubscription("Bob", 8000);
            user2.addPromo(summer);
            CorporateSubscription company =
                    new CorporateSubscription("Travel Company", 20000);
            company.addPromo(vip);
            company.addPromo(summer);
            billing.registerSubscription("alice@gmail.com", user1);
            billing.registerSubscription("bob@gmail.com", user2);
            billing.registerSubscription("company@gmail.com", company);
            billing.deposit("alice@gmail.com", 15000);
            billing.deposit("bob@gmail.com", 10000);
            billing.deposit("company@gmail.com", 25000);

            System.out.println("\n--- SCENARIO 1: SUCCESS ---");
            billing.processPaymentByEmail("alice@gmail.com");

            System.out.println("\n--- SCENARIO 2: NOT FOUND ---");
            try {
                billing.processPaymentByEmail("fake@mail.com");
            } catch (SubscriptionNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Program continues!");

            System.out.println("\n--- SCENARIO 3: INVALID PRICE ---");
            try {
                new UserSubscription("Invalid User", -500);
            } catch (InvalidSubscriptionException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\n--- SCENARIO 4: DUPLICATE EMAIL ---");
            try {
                billing.registerSubscription("alice@gmail.com", user2);
            } catch (DuplicateSubscriptionException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\n--- SCENARIO 5: INVALID EMAIL ---");
            try {
                billing.registerSubscription("wrong-email", user2);
            } catch (InvalidEmailException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\n--- SCENARIO 6: INVALID PROMO ---");
            try {
                new PromoCode("BAD CODE", 150, LocalDate.now());
            } catch (InvalidPromoCodeException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\n--- SCENARIO 7: EXPIRED PROMO ---");
            try {
                PromoCode expired = new PromoCode(
                        "OLD10", 10, LocalDate.of(2020, 1, 1));
                UserSubscription expiredUser =
                        new UserSubscription("Charlie", 5000);
                expiredUser.addPromo(expired);
                billing.registerSubscription("charlie@gmail.com", expiredUser);
                billing.deposit("charlie@gmail.com", 10000);
                billing.processPaymentByEmail("charlie@gmail.com");
            } catch (PromoCodeExpiredException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\n--- SCENARIO 8: INSUFFICIENT FUNDS ---");
            try {
                UserSubscription poorUser =
                        new UserSubscription("David", 10000);
                billing.registerSubscription("david@gmail.com", poorUser);
                billing.deposit("david@gmail.com", 2000);
                billing.processPaymentByEmail("david@gmail.com");
            } catch (InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (DuplicateSubscriptionException |
                 SubscriptionNotFoundException |
                 PromoCodeExpiredException |
                 InsufficientFundsException e) {
            System.out.println("System error: " + e.getMessage());
        } finally {
            System.out.println("\n=== PROGRAM FINISHED ===");
        }
    }
}
