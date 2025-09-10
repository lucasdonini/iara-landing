package com.dao;

import com.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

// Classe abstrata para facilitar o gerenciamento de conexões dos DAOs
public abstract class DAO implements AutoCloseable {
  protected static final ConnectionFactory connF = new ConnectionFactory();
  protected Connection conn;

  protected DAO() throws SQLException, ClassNotFoundException {
    conn = connF.getConnection();
    conn.setAutoCommit(false);
  }

  @Override
  public void close() throws SQLException {
    connF.closeConnection(conn);
  }
}
