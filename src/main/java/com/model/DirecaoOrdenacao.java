package com.model;

public enum DirecaoOrdenacao {
  CRESCENTE("ASC"),
  DECRESCENTE("DESC");

  private final String sql;

  DirecaoOrdenacao(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }
}
