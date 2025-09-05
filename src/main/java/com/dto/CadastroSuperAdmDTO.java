package com.dto;

public class CadastroSuperAdmDTO {
  private String nome;
  private String cargo;
  private String email;

  public CadastroSuperAdmDTO(String nome, String cargo, String email) {
    this.nome = nome;
    this.cargo = cargo;
    this.email = email;
  }

  // Getters
  public String getNome() {
    return nome;
  }

  public String getCargo() {
    return cargo;
  }

  public String getEmail() {
    return email;
  }

  // Setters
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
