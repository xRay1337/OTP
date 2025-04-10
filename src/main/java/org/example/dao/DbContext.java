package org.example.dao;

import org.example.service.Config;
import org.example.service.User;
import org.slf4j.Logger;

import java.sql.*;
import java.util.Random;

public class DbContext {
    public static boolean useCode(Connection connection, String code, Logger logger) {
        String updateConfig = "UPDATE otp SET was_used = TRUE WHERE code = ?";
        int rowsAffected;

        try (PreparedStatement statement = connection.prepareStatement(updateConfig)) {
            statement.setString(1, code);

            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return rowsAffected == 1;
    }

    public static String createCode(Connection connection, Config config, Logger logger) {
        String code;

        do code = generateCode(config.getCodeLength(), logger);
        while (getCodeStatus(connection, code, logger) != null);

        String createOtp = "INSERT INTO otp(code, lifetime) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(createOtp)) {
            statement.setString(1, code);
            statement.setInt(2, config.getLifetime());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return code;
    }

    public static String getCodeStatus(Connection connection, String code, Logger logger) {
        String selectCodeStatus = "SELECT code_status FROM otp_statuses WHERE code = ?";
        String codeStatus = null;

        try (PreparedStatement pstmt = connection.prepareStatement(selectCodeStatus)) {
            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) codeStatus = rs.getString("code_status");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return codeStatus;
    }

    public static boolean userIdentification(Connection connection, User user, Logger logger) {
        String selectUserPwdHash = "SELECT user_password_hash_code FROM users WHERE user_role = ? AND user_login = ?";

        boolean userIdentified;

        try (PreparedStatement pstmt = connection.prepareStatement(selectUserPwdHash)) {
            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getLogin());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userPasswordHashCode = rs.getInt("user_password_hash_code");

                    userIdentified = userPasswordHashCode == user.getPasswordHashCode();
                } else userIdentified = createUser(connection, user, logger);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return userIdentified;
    }

    public static boolean createUser(Connection connection, User user, Logger logger) {
        String createUser = "INSERT INTO users(user_role, user_login, user_password_hash_code) VALUES (?, ?, ?)";
        int rowsAffected;

        try (PreparedStatement statement = connection.prepareStatement(createUser)) {
            statement.setString(1, user.getRole());
            statement.setString(2, user.getLogin());
            statement.setInt(3, user.getPasswordHashCode());

            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return rowsAffected == 1;
    }

    public static boolean saveConfig(Connection connection, Config config, Logger logger) {
        String updateConfig = "UPDATE config SET code_length = ?, lifetime = ? WHERE id = 1";
        int rowsAffected;

        try (PreparedStatement statement = connection.prepareStatement(updateConfig)) {
            statement.setInt(1, config.getCodeLength());
            statement.setInt(2, config.getLifetime());

            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return rowsAffected == 1;
    }

    private static String generateCode(int length, Logger logger) {
        if (length <= 0 || length > 32) {
            logger.error("Длина кода должна быть от 1 до 32.");
            throw new IllegalArgumentException("Длина кода должна быть от 1 до 32.");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) sb.append(random.nextInt(10));

        return sb.toString();
    }

    public static Config getConfig(Connection connection, Logger logger) {
        Config config = null;

        try (Statement statement = connection.createStatement()) {
            String query = "SELECT code_length, lifetime FROM config WHERE id = 1";
            ResultSet rs = statement.executeQuery(query);
            rs.next();

            int code_length = rs.getInt("code_length");
            int lifetime = rs.getInt("lifetime");
            config = new Config(code_length, lifetime);

            rs.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return config;
    }

    public static Connection getConnection(Logger logger) {
        String connectionString = "jdbc:postgresql://localhost:5438/postgres";
        String login = "postgres";
        String password = "qwerty";

        DbConnector db = new PostgresDbConnector(connectionString, login, password);

        Connection connection = db.getConnection();
        logger.debug("Соединение с базой установлено");

        return connection;
    }
}
