package com.dto;

public class LoginDTO {
  private String email;
  private String senha;

  public LoginDTO(String email, String senha) {
    this.email = email;
    this.senha = senha;
  }

  // Getters
  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  // Setters
  public void setEmail(String email) {
    this.email = email;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
