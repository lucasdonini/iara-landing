package com.model;

import com.utils.StringUtils;

/*
* ENUM de composição utilizado no model da tabela usuário, onde o nome da instância é a referência utilizada no front-end em relação ao nível de acesso, e cada atributo do ENUM diz respeito a uma coluna do banco de dados:
*
*  nivel = tipo_acesso
*  descricao = desc_tipoacesso
* */
public enum TipoAcesso {

  FUNCIONARIO(0, "ler, tirar foto"),
  SUPERVISOR_CIF(1, "ler, tirar foto, aprovar foto"),
  GERENTE(2, "ler, tirar foto, aprovar foto, cadastrar ábaco, cadastrar pessoa");

  private final int nivel;
  private final String descricao;

  TipoAcesso(int nivel, String descricao) {
    this.nivel = nivel;
    this.descricao = descricao;
  }

  // Método que recebe como parâmetro um número e retorna a instância que tem nível compatível com o número recebido
  public static TipoAcesso deNivel(int nivel) {
    for (TipoAcesso t : values()) {
      if (t.nivel() == nivel) {
        return t;
      }
    }

    throw new IllegalArgumentException("Nível inválido");
  }

  public int nivel() {
    return nivel;
  }

  public String descricao() {
    return descricao;
  }

  @Override
  public String toString() {
    return StringUtils.capitalize(name());
  }
}
