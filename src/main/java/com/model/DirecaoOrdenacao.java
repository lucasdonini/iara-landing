package com.model;

import com.utils.StringUtils;

/*
* ENUM de simplificação referente as direções de ordenação do banco de dados, com o objetivo de facilitar a manipulação da ordenação das listagens dos dados do banco de dados.
* */
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

  @Override
  public String toString() {
    return StringUtils.capitalize(name());
  }
}
