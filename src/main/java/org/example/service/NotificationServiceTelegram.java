package org.example.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Properties;

public class NotificationServiceTelegram extends TelegramLongPollingBot implements NotificationService {
    private final String botUsername;
    private final String botToken;

    public NotificationServiceTelegram() {
        this.botUsername = "OtpBot";
        this.botToken = "1b8faGbdjfo86DntqpENg0lGVG8a";
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void sendCode(String destination, String code) {
        SendMessage message = new SendMessage();
        message.setChatId(destination);
        message.setText("Ваш код: " + code);

        try {
            execute(message); // Отправка сообщения
            System.out.println("Уведомление отправлено успешно.");
        } catch (TelegramApiException e) {
            System.out.println("Ошибка при отправке уведомления: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}