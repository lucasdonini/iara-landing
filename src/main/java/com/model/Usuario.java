package com.model;

import java.time.LocalDate;

public class Usuario {
  private int id;
  private String nome;
  private String login;
  private String senha;
  private NivelAcesso nivelAcesso;
  private LocalDate dtCriacao;
  private boolean status;

  public Usuario(int id, String nome, String login, String senha, NivelAcesso nivelAcesso, LocalDate dtCriacao,
      boolean status) {
    this.id = id;
    this.nivelAcesso = nivelAcesso;
    this.dtCriacao = dtCriacao;
    this.login = login;
    this.nome = nome;
    this.senha = senha;
    this.status = status;
  }

  public Usuario() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String name) {
    this.nome = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String password) {
    this.senha = password;
  }

  public NivelAcesso getNivelAcesso() {
    return nivelAcesso;
  }

  public void setNivelAcesso(NivelAcesso accessLevel) {
    this.nivelAcesso = accessLevel;
  }

  public LocalDate getDtCriacao() {
    return dtCriacao;
  }

  public void setDtCriacao(LocalDate creationDate) {
    this.dtCriacao = creationDate;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
