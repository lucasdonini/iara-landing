package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

// Classe concreta para abrir e fechar conexões com o bano através de JDBC
public class ConnectionFactory {
    private static final Dotenv dotenv = Dotenv.load();

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Objects.requireNonNull(dotenv.get("DB_URL")),
                dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD")
        );
    }

    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
