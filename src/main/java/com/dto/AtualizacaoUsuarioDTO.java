package com.dto;

import com.model.TipoAcesso;

import java.util.UUID;

public class AtualizacaoUsuarioDTO {
  // Atributos
  private UUID id;
  private String nome;
  private String emailGerente;
  private String genero;
  private String cargo;
  private String email;
  private TipoAcesso tipoAcesso;
  private String descTipoAcesso;
  private Boolean status;
  private Integer fkFabrica;

  // Construtor
  public AtualizacaoUsuarioDTO(UUID id, String nome, String emailGerente, String genero, String cargo, String email, TipoAcesso tipoAcesso, String descTipoAcesso, Boolean status, Integer fkFabrica) {
    this.id = id;
    this.nome = nome;
    this.emailGerente = emailGerente;
    this.genero = genero;
    this.cargo = cargo;
    this.email = email;
    this.tipoAcesso = tipoAcesso;
    this.descTipoAcesso = descTipoAcesso;
    this.status = status;
    this.fkFabrica = fkFabrica;
  }

  // Getters e Setters

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
     this.id = id;
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

  public TipoAcesso getTipoAcesso() {
    return tipoAcesso;
  }

  public void setTipoAcesso(TipoAcesso accessLevel) {
    this.tipoAcesso = accessLevel;
  }

  public String getDescTipoAcesso() {
      return descTipoAcesso;
  }

  public void setDescTipoAcesso(String descTipoAcesso) {
      this.descTipoAcesso = descTipoAcesso;
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
    return "AtualizacaoUsuarioDTO{nome='%s', emailGerente='%s', genero='%s', cargo='%s', email='%s', tipoAcesso=%s, status=%b, fkFabrica=%d}"
        .formatted(nome, emailGerente, genero, cargo, email, tipoAcesso, status, fkFabrica);
  }
}
