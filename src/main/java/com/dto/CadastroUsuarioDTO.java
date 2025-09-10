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

  // Getters e Setters
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

  public String getSenha() {
    return senha;
  }

  public void setSenha(String password) {
    this.senha = password;
  }

  public int getFkFabrica() {
    return fkFabrica;
  }

  public void setFkFabrica(int fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
