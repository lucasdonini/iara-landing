package com.dto;

import java.time.LocalDate;

// DTO da tabela usuario utilizado para o cadastro de novos usuários no banco de dados

public class CadastroUsuarioDTO {

  private String nome;
  private String emailGerente;
  private String genero;
  private LocalDate dataNascimento;
  private String cargo;
  private String email;
  private String senha;
  private Integer fkFabrica;

  public CadastroUsuarioDTO(String nome, String emailGerente, String genero, LocalDate dataNascimento, String cargo, String email, String senha, Integer nomefabrica) {
    this.nome = nome;
    this.emailGerente = emailGerente;
    this.genero = genero;
    this.dataNascimento = dataNascimento;
    this.cargo = cargo;
    this.email = email;
    this.senha = senha;
    this.fkFabrica = nomefabrica;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmailGerente() {
      return emailGerente;
  }

  public void setEmailGerente(String emailGerente) {
      this.emailGerente = emailGerente;
  }

  public String getGenero() {
      return genero;
  }

  public void setGenero(String genero) {
      this.genero = genero;
  }

  public LocalDate getDataNascimento() {
      return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
      this.dataNascimento = dataNascimento;
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

  public Integer getFkFabrica() {
    return fkFabrica;
  }

  public void setFkFabrica(Integer fkFabrica) {
    this.fkFabrica = fkFabrica;
  }

  @Override
  public String toString() {
    return "CadastroUsuarioDTO{nome='%s', emailGerente='%s', genero='%s', dataNascimento=%s, cargo='%s', email='%s', senha='%s', fkFabrica=%d}".formatted(nome, emailGerente, genero, dataNascimento, cargo, email, senha, fkFabrica);
  }
}
