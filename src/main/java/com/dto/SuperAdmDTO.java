package com.dto;

// DTO da tabela super_adm utilizado para a visualização dos dados registrados no banco de dados

public class SuperAdmDTO {

  private Integer id;
  private String nome;
  private String cargo;
  private String email;

  public SuperAdmDTO(Integer id, String nome, String cargo, String email) {
    this.id = id;
    this.nome = nome;
    this.cargo = cargo;
    this.email = email;
  }

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

  @Override
  public String toString() {
    return "SuperAdmDTO{id=%d, nome='%s', cargo='%s', email='%s'}".formatted(id, nome, cargo, email);
  }
}
