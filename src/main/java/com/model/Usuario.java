package com.model;

import java.time.LocalDate;

// tabela: usuario
public class Usuario {
  // Atributos
  private String nome;
  private String senha;
  private NivelAcesso nivelAcesso;
  private LocalDate dtCriacao;
  private boolean status;
  private int fkFabrica;

  // Construtores
  public Usuario(String nome, String senha, NivelAcesso nivelAcesso, LocalDate dtCriacao,
      boolean status, int fkFabrica) {

    this.nome = nome;
    this.senha = senha;
    this.nivelAcesso = nivelAcesso;
    this.dtCriacao = dtCriacao;
    this.status = status;
    this.fkFabrica = fkFabrica;
  }

  public Usuario() {
    super();
  }

  // Getters
  public String getNome() {
    return nome;
  }

  public String getSenha() {
    return senha;
  }

  public NivelAcesso getNivelAcesso() {
    return nivelAcesso;
  }

  public LocalDate getDtCriacao() {
    return dtCriacao;
  }

  public boolean getStatus() {
    return status;
  }

  public int getFkFabrica() {
    return fkFabrica;
  }

  // Setters
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setSenha(String password) {
    this.senha = password;
  }

  public void setNivelAcesso(NivelAcesso accessLevel) {
    this.nivelAcesso = accessLevel;
  }

  public void setDtCriacao(LocalDate creationDate) {
    this.dtCriacao = creationDate;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void setFkFabrica(int fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
