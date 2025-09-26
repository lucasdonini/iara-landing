package com.dto;

import com.model.Permissao;

import java.time.LocalDate;

public class UsuarioDTO {
  private int id;
  private String nome;
  private String email;
  private Permissao permissao;
  private LocalDate dtCriacao;
  private boolean status;
  private int fkFabrica;

  public UsuarioDTO(int id, String nome, String email, Permissao permissao, LocalDate dtCriacao,
                    boolean status, int fkFabrica) {

    this.id = id;
    this.nome = nome;
    this.email = email;
    this.permissao = permissao;
    this.dtCriacao = dtCriacao;
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

  public Permissao getPermissao() {
    return permissao;
  }

  public void setPermissao(Permissao accessLevel) {
    this.permissao = accessLevel;
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

  public int getFkFabrica() {
    return fkFabrica;
  }

  public void setFkFabrica(int fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
