package com.model;

import java.time.LocalDate;

// tabela: usuario
public class Usuario {
  // Atributos
  private Integer id; // coluna: id
  private String nome; // coluna: nome
  private String email; // coluna: email
  private String senha; // coluna: senha
  private TipoAcesso tipoAcesso; // coluna: tipo_acesso
  private LocalDate dataCriacao; // coluna: data_criacao
  private Boolean status; // coluna: status
  private Integer idFabrica; // coluna: id_fabrica

  // Construtor
  public Usuario(Integer id, String nome, String email, String senha, TipoAcesso tipoAcesso, LocalDate dataCriacao, Boolean status, Integer idFabrica) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.tipoAcesso = tipoAcesso;
    this.dataCriacao = dataCriacao;
    this.status = status;
    this.idFabrica = idFabrica;
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

  public TipoAcesso getTipoAcesso() {
    return tipoAcesso;
  }

  public void setTipoAcesso(TipoAcesso tipoAcesso) {
    this.tipoAcesso = tipoAcesso;
  }

  public LocalDate getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(LocalDate dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public Integer getIdFabrica() {
    return idFabrica;
  }

  public void setIdFabrica(Integer idFabrica) {
    this.idFabrica = idFabrica;
  }

  // toString
  @Override
  public String toString() {
    return "Usuario{id=%d, nome='%s', email='%s', senha='%s', tipoAcesso=%s, dataCriacao=%s, status=%b, idFabrica=%d}"
        .formatted(id, nome, email, senha, tipoAcesso, dataCriacao, status, idFabrica);
  }
}
