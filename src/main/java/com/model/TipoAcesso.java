package com.model;

public enum TipoAcesso {
  // Instâncias
  LEITURA(0, "Leitura"),
  SUGESTAO(1, "Sugestão"),
  ALTERACAO(2, "Alteração"),
  GERENCIAMENTO(3, "Gerenciamento");

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
    return "TipoAcesso{nivel=%d, descricao='%s'}".formatted(nivel, descricao);
  }
}
