package com.model;

public enum DirecaoOrdenacao {
  // Instâncias
  CRESCENTE("ASC"),
  DECRESCENTE("DESC");

  // Atributos
  private final String sql;

  // Construtor
  DirecaoOrdenacao(String sql) {
    this.sql = sql;
  }

  // Outros Métodos
  public String getSql() {
    return sql;
  }

  // toString
  @Override
  public String toString() {
    return "DirecaoOrdenacao{sql='%s'}".formatted(sql);
  }
}
