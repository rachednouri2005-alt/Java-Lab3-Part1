package org.touragency.service;

public class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String email, String message) {
        System.out.println("----- EMAIL NOTIFICATION -----");
        System.out.println("To: " + email);
        System.out.println("Message: " + message);
        System.out.println("------------------------------");
    }
}
