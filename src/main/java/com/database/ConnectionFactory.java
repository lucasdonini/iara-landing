package com.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

// Classe concreta para abrir e fechar conexões com o bano por JDBC
public class ConnectionFactory {
  private static Dotenv dotenv;

  private Dotenv getDotenv() {
    if (dotenv == null) {
      dotenv = Dotenv.configure().load();
    }

    return dotenv;
  }

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Dotenv env = getDotenv();

    Class.forName("org.postgresql.Driver");

    return DriverManager.getConnection(
        Objects.requireNonNull(env.get("DB_URL")),
        env.get("DB_USER"), env.get("DB_PASSWORD")
    );
  }

  public void closeConnection(Connection conn) throws SQLException {
    if (conn != null && !conn.isClosed()) {
      conn.close();
    }
  }
}
