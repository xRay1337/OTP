package org.example.service;

import org.smpp.Connection;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;

import java.util.Properties;

public class NotificationServiceSmpp implements NotificationService {
    @Override
    public void sendCode(String destination, String code) {
        Connection connection;
        Session session;

        Properties config = loadConfig();

        try {
            // 1. Установка соединения
            connection = new TCPIPConnection(config.getProperty("smpp.host"), Integer.parseInt(config.getProperty("smpp.port")));
            session = new Session(connection);
            // 2. Подготовка Bind Request
            BindTransmitter bindRequest = new BindTransmitter();
            bindRequest.setSystemId(config.getProperty("smpp.system_id"));
            bindRequest.setPassword(config.getProperty("smpp.password"));
            bindRequest.setSystemType(config.getProperty("smpp.system_type"));
            bindRequest.setInterfaceVersion((byte) 0x34); // SMPP v3.4
            bindRequest.setAddressRange(config.getProperty("smpp.source_addr"));
            // 3. Выполнение привязки
            BindResponse bindResponse = session.bind(bindRequest);
            if (bindResponse.getCommandStatus() != 0) {
                throw new Exception("Bind failed: " + bindResponse.getCommandStatus());
            }
            // 4. Отправка сообщения
            SubmitSM submitSM = new SubmitSM();
            submitSM.setSourceAddr(config.getProperty("smpp.source_addr"));
            submitSM.setDestAddr(destination);
            submitSM.setShortMessage("Ваш код: " + code);

            session.submit(submitSM);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(NotificationServiceSmpp.class.getClassLoader()
                    .getResourceAsStream("sms.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email configuration", e);
        }
    }
}