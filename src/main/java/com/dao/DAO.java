package com.dao;

// Classe abstrata utilizada para criar conexões com o banco de dados e encerrá-las automaticamente quando o bloco try é ncerrado (AutoCloseable)

import com.database.FabricaDeConexoes;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO implements AutoCloseable {

  protected static final FabricaDeConexoes fc = new FabricaDeConexoes();
  protected Connection conn;

  protected DAO() throws SQLException, ClassNotFoundException {
    conn = fc.getConnection();
    conn.setAutoCommit(false);
  }

  @Override
  public void close() throws SQLException {
    fc.closeConnection(conn);
  }
}
