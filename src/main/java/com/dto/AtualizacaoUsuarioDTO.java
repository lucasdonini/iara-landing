package com.dto;

import com.model.NivelAcesso;

public class AtualizacaoUsuarioDTO {
  private int id;
  private String nome;
  private String email;
  private NivelAcesso nivelAcesso;
  private boolean status;
  private int fkFabrica;

  public AtualizacaoUsuarioDTO(int id, String nome, String email, NivelAcesso nivelAcesso, boolean status, int fkFabrica) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.nivelAcesso = nivelAcesso;
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

  public NivelAcesso getNivelAcesso() {
    return nivelAcesso;
  }

  public void setNivelAcesso(NivelAcesso accessLevel) {
    this.nivelAcesso = accessLevel;
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
