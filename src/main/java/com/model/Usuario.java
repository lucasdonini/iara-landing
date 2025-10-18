package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// tabela: usuario
public class Usuario {
  // Atributos
  private UUID id; // coluna: id
  private UUID idGerente; // coluna: id_gerente
  private String nome; // coluna: nome
  private String genero; // coluna: genero
  private LocalDate dataNascimento; // coluna: data_nascimento
  private String email; // coluna: email
  private String senha; // coluna: senha
  private String cargo; // coluna: cargo
  private TipoAcesso tipoAcesso; // coluna: tipo_acesso
  private String descTipoAcesso; // coluna: desc_tipoacesso
  private LocalDateTime dataCriacao; // coluna: data_criacao
  private Boolean status; // coluna: status
  private Integer fkFabrica; // coluna: fk_fabrica

  // Construtor
  public Usuario(UUID id, UUID idGerente, String nome, String genero, LocalDate dataNascimento, String email, String senha, String cargo, TipoAcesso tipoAcesso, String descTipoAcesso, LocalDateTime dataCriacao, Boolean status, Integer idFabrica) {
    this.id = id;
    this.idGerente = idGerente;
    this.nome = nome;
    this.genero = genero;
    this.dataNascimento = dataNascimento;
    this.email = email;
    this.senha = senha;
    this.tipoAcesso = tipoAcesso;
    this.descTipoAcesso = descTipoAcesso;
    this.dataCriacao = dataCriacao;
    this.status = status;
    this.fkFabrica = idFabrica;
  }

  // Getters e Setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getIdGerente() {
      return idGerente;
  }

  public void setIdGerente(UUID idGerente) {
      this.idGerente = idGerente;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
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

  public String getCargo() {
      return cargo;
  }

  public void setCargo(String cargo) {
      this.cargo = cargo;
  }

  public TipoAcesso getTipoAcesso() {
    return tipoAcesso;
  }

  public void setTipoAcesso(TipoAcesso tipoAcesso) {
    this.tipoAcesso = tipoAcesso;
  }

  public String getDescTipoAcesso() {
      return descTipoAcesso;
  }

  public void setDescTipoAcesso(String descTipoAcesso) {
      this.descTipoAcesso = descTipoAcesso;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(LocalDateTime dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public Integer getFkFabrica() {
    return fkFabrica;
  }

  public void setFkFabrica(Integer fkFabrica) {
    this.fkFabrica = fkFabrica;
  }

  // toString
  @Override
  public String toString() {
    return "Usuario{id=%d, idGerente=%d, nome='%s', genero=%s, dataNascimento=%s, email='%s', senha='%s', cargo=%s, tipoAcesso=%s, descTipoAcesso=%s, dataCriacao=%s, status=%b, idFabrica=%d}"
        .formatted(id, nome, email, senha, tipoAcesso, dataCriacao, status, fkFabrica);
  }
}
