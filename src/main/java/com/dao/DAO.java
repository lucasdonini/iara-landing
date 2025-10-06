package com.dao;

import com.database.FabricaDeConexoes;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO implements AutoCloseable {
  // Atributos
  protected static final FabricaDeConexoes fc = new FabricaDeConexoes();
  protected Connection conn;

  // Construtor
  protected DAO() throws SQLException, ClassNotFoundException {
    conn = fc.getConnection();
    conn.setAutoCommit(false);
  }

  // Outros Métodos
  @Override
  public void close() throws SQLException {
    fc.closeConnection(conn);
  }
}
