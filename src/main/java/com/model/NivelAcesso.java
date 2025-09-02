package com.model;

public enum NivelAcesso {
  USUARIO(0),
  ADMIN(1),
  SUPER_ADMIN(2);

  private final int nivel;

  NivelAcesso(int nivel) {
    this.nivel = nivel;
  }

  public int nivel() {
    return nivel;
  }
}
