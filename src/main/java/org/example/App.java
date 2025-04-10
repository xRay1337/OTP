package org.example;

import org.example.dao.DbContext;
import org.example.service.*;
import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Запуск приложения");
        Connection connection = DbContext.getConnection(logger);
        Config config = DbContext.getConfig(connection, logger);
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Введите роль(admin/user), логин и пароль через пробел или exit для выхода:");

            String inputLine = in.nextLine();

            logger.debug("Переданные параметры: {}", inputLine);

            if (inputLine.equals("exit")) break;

            String[] inputParts = inputLine.split(" ");

            User user;

            if (inputParts.length == 3 && (inputParts[0].equals("admin") || inputParts[0].equals("user"))) {
                user = new User(inputParts[0], inputParts[1], inputParts[2]);
                boolean userLogin = DbContext.userIdentification(connection, user, logger);

                if (userLogin) logger.info("Вход {} подтверждён.", user.getLogin());
                else {
                    logger.warn("Неверный логин/пароль: {}", user.getLogin());
                    continue;
                }
            } else {
                logger.debug("Неверные параметры идентификации: {}", inputLine);
                continue;
            }

            if (user.isAdmin()) {
                System.out.println("Режим администратора.");
                System.out.println("Введите команду вида: (length или timeout) и целое число.");

                inputLine = in.nextLine();
                inputParts = inputLine.split(" ");

                if (inputParts.length == 2 && inputParts[0].equals("length")) {
                    logger.info("length = {}", inputParts[1]);
                    config.setCodeLength(Integer.parseInt(inputParts[1]));
                } else if (inputParts.length == 2 && inputParts[0].equals("timeout")) {
                    logger.info("timeout = {}", inputParts[1]);
                    config.setLifetime(Integer.parseInt(inputParts[1]));
                }

                DbContext.saveConfig(connection, config, logger);
            } else {
                System.out.println("Режим пользователя.");
                System.out.println("Введите способ доставки(console/email/smpp/telegram) и адрес.");

                inputLine = in.nextLine();
                inputParts = inputLine.split(" ");

                if (inputParts.length == 2) {
                    String deliverySystem = inputParts[0];
                    String address = inputParts[1];

                    String[] deliverySystems = {"console", "email", "smpp", "telegram"};

                    if (Arrays.asList(deliverySystems).contains(deliverySystem)) {
                        String code = DbContext.createCode(connection, config, logger);
                        String message = "Ваш одноразовый код: " + code;

                        if (deliverySystems.equals("console")) System.out.println(message);
                        else Messenger.sendCode(deliverySystem, address, message);

                        System.out.println("Введите одноразовый код:");
                        inputLine = in.nextLine();

                        String currentCodeStatus = DbContext.getCodeStatus(connection, code, logger);

                        if (currentCodeStatus.equals("ACTIVE") && inputLine.equals(code)) {
                            logger.info("Использован код: {}", code);
                            DbContext.useCode(connection, code, logger);
                        } else if (currentCodeStatus.equals("EXPIRED")) {
                            logger.debug("Введён просроченный код: {}", code);
                        } else {
                            logger.warn("Введён неверный код: {}", code);
                        }
                    }
                } else System.out.println("Необходимо ввести 2 параметра.");
            }
        }

        logger.info("Остановка приложения");
    }
}