package com.model;

// Enum para representar e tornar semântico os níveis de acesso do banco
public enum Permissao {
  LEITURA(0, "Leitura"),
  SUGESTAO(1, "Sugestão"),
  ALTERACAO(2, "Alteração"),
  GERENCIAMENTO(3, "Gerenciamento");

  private final int nivel;
  private final String desc;

  Permissao(int nivel, String desc) {
    this.nivel = nivel;
    this.desc = desc;
  }

  public static Permissao fromInteger(int i) {
    for (Permissao p : values()) {
      if (p.nivel() == i) {
        return p;
      }
    }

    return null;
  }

  public int nivel() {
    return nivel;
  }

  @Override
  public String toString() {
    return desc;
  }
}
