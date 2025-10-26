package com.dto;

// DTO composto de atributos da tabela super_adm utilizado para o login

public class LoginDTO {

  private String email;
  private String senha;

  public LoginDTO(String email, String senha) {
    this.email = email;
    this.senha = senha;
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

  @Override
  public String toString() {
    return "LoginDTO{email='%s', senha='%s'}".formatted(email, senha);
  }
}
