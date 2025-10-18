package com.model;

import com.utils.StringUtils;

public enum TipoAcesso {
  // Instâncias
  FUNCIONARIO(0, "ler, tirar foto"),
  SUPERVISOR_CIF(1, "ler, tirar foto, aprovar foto"),
  GERENTE(2, "ler, tirar foto, aprovar foto, cadastrar ábaco, cadastrar pessoa");

  // Atributos
  private final int nivel;
  private final String descricao;

  // Construtor
  TipoAcesso(int nivel, String descricao) {
    this.nivel = nivel;
    this.descricao = descricao;
  }

  // Métodos Estáticos
  public static TipoAcesso deNivel(int nivel) {
    for (TipoAcesso t : values()) {
      if (t.nivel() == nivel) {
        return t;
      }
    }

    throw new IllegalArgumentException("Nível inválido");
  }

  // Getters
  public int nivel() {
    return nivel;
  }

  public String descricao() {
    return descricao;
  }

  // toString
  @Override
  public String toString() {
    return StringUtils.capitalize(name());
  }
}
