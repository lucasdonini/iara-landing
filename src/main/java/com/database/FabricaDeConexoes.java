package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe para criar e desfazer conexão com o banco de dados

public class FabricaDeConexoes {

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    String url = System.getenv("DB_URL");

    Class.forName("org.postgresql.Driver");

    if (url == null) {
      throw new IllegalStateException("Variável de ambiente 'DB_URL' não encontrada");
    }

    return DriverManager.getConnection(url, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
  }

  public void closeConnection(Connection conn) throws SQLException {
    if (conn != null && !conn.isClosed()) {
      conn.close();
    }
  }
}
