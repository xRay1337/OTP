package org.example.service;

public class Messenger {
    public static void sendCode(String deliverySystem, String destination, String code) {
        (switch (deliverySystem) {
            case "email" -> new NotificationServiceEmail();
            case "smpp" -> new NotificationServiceSmpp();
            case "telegram" -> new NotificationServiceTelegram();
            default -> throw new IllegalArgumentException("Нереализованный способ доставки.");
        }).sendCode(destination, code);
    }
}