package org.example.dao;

import java.sql.Connection;

public interface DbConnector {
    Connection getConnection();
}
