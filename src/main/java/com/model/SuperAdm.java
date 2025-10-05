package com.model;

// tabela: super_adm
public class SuperAdm {
  // Atributos
  private Integer id; // coluna: id
  private String nome; // coluna: nome
  private String cargo; // coluna: cargo
  private String email; // coluna: email
  private String senha; // coluna: senha

  // Construtor
  public SuperAdm(Integer id, String nome, String cargo, String email, String senha) {
    this.id = id;
    this.nome = nome;
    this.cargo = cargo;
    this.email = email;
    this.senha = senha;
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCargo() {
    return cargo;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  // toString
  @Override
  public String toString() {
    return "SuperAdm{id=%d, nome='%s', cargo='%s', email='%s', senha='%s'}".formatted(id, nome, cargo, email, senha);
  }
}