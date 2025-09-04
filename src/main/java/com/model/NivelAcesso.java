package com.model;

// Enum para representar e tornar semântico os níveis de acesso do banco
public enum NivelAcesso {
  USUARIO(0),
  ADMIN(1);

  private final int nivel;

  NivelAcesso(int nivel) {
    this.nivel = nivel;
  }

  public int nivel() {
    return nivel;
  }

  public static NivelAcesso fromInteger(int i) {
    return switch(i) {
      case 0 -> USUARIO;
      case 1 -> ADMIN;
      default -> null;
    };
  }
}
