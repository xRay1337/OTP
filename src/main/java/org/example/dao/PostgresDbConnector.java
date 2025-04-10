package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDbConnector implements DbConnector {
    private String connecionString;
    private String login;
    private String password;

    public PostgresDbConnector(String connecionString, String login, String password) {
        this.connecionString = connecionString;
        this.login = login;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver"); // Загрузка драйвера PostgreSQL
        } catch (
                ClassNotFoundException e) {
            System.out.println("Не найден драйвер PostgreSQL.");
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connecionString, login, password);
            System.out.println("Успешное подключение к базе данных: " + connecionString);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных:");
            e.printStackTrace();
        }

        return connection;
    }
}