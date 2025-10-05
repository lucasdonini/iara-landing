package com.dto;

import com.model.TipoAcesso;

public class AtualizacaoUsuarioDTO {
  private int id;
  private String nome;
  private String email;
  private TipoAcesso tipoAcesso;
  private boolean status;
  private int fkFabrica;

  public AtualizacaoUsuarioDTO(int id, String nome, String email, TipoAcesso tipoAcesso, boolean status, int fkFabrica) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.tipoAcesso = tipoAcesso;
    this.status = status;
    this.fkFabrica = fkFabrica;
  }

  // Getters e Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public TipoAcesso getPermissao() {
    return tipoAcesso;
  }

  public void setPermissao(TipoAcesso accessLevel) {
    this.tipoAcesso = accessLevel;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public int getFkFabrica() {
    return fkFabrica;
  }

  public void setFkFabrica(int fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
