package com.model;

// Enum para representar e tornar semântico os níveis de acesso do banco
public enum NivelAcesso {
  USUARIO(0, "Usuário"),
  ADMIN(1, "Administrador");

  private final int nivel;
  private final String desc;

  NivelAcesso(int nivel, String desc) {
    this.nivel = nivel;
    this.desc = desc;
  }

  public static NivelAcesso fromInteger(int i) {
    return switch (i) {
      case 0 -> USUARIO;
      case 1 -> ADMIN;
      default -> null;
    };
  }

  public int nivel() {
    return nivel;
  }

  @Override
  public String toString() {
    return desc;
  }
}
