package com.dto;

public class SuperAdmDTO {
  private int id;
  private String nome;
  private String cargo;
  private String email;

  public SuperAdmDTO(int id, String nome, String cargo, String email) {
    this.id = id;
    this.nome = nome;
    this.cargo = cargo;
    this.email = email;
  }

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
}
