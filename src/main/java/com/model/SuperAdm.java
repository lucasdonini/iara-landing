package com.model;

// tabela: Super_Adm
public class SuperAdm {
  // Atributos
  private int id;
  private String nome;
  private String cargo;
  private String email;
  private String senha;

  // Construtores
  public SuperAdm(int id, String nome, String cargo, String email, String senha) {
    this.id = id;
    this.nome = nome;
    this.cargo = cargo;
    this.email = email;
    this.senha = senha;
  }

  // Getters
  public int getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getCargo() {
    return cargo;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  // Setters
  public void setId(int id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}