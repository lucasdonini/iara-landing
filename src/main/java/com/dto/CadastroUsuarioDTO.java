package com.dto;

public class CadastroUsuarioDTO {
  private String nome;
  private String email;
  private String senha;
  private int fkFabrica;

  public CadastroUsuarioDTO(String nome, String email, String senha, int fkFabrica) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.fkFabrica = fkFabrica;
  }

  // Getters
  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  public int getFkFabrica() {
    return fkFabrica;
  }

  // Setters
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSenha(String password) {
    this.senha = password;
  }

  public void setFkFabrica(int fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
