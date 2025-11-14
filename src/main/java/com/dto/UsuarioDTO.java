package com.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.model.Genero;
import com.model.TipoAcesso;

// DTO da tabela usuario utilizado para a visualização dos dados registrados no banco de dados

public class UsuarioDTO {

  private UUID id;
  private String nome;
  private String emailGerente;
  private Genero genero;
  private LocalDate dataNascimento;
  private String cargo;
  private String email;
  private TipoAcesso tipoAcesso;
  private LocalDateTime dataCriacao;
  private Boolean status;
  private String nomeFabrica;

  public UsuarioDTO(UUID id, String nome, String emailGerente, Genero genero, LocalDate dataNascimento, String email, String cargo, TipoAcesso tipoAcesso, LocalDateTime dataCriacao, Boolean status, String nomeFabrica) {
    this.id = id;
    this.nome = nome;
    this.emailGerente = emailGerente;
    this.genero = genero;
    this.dataNascimento = dataNascimento;
    this.cargo = cargo;
    this.email = email;
    this.tipoAcesso = tipoAcesso;
    this.dataCriacao = dataCriacao;
    this.status = status;
    this.nomeFabrica = nomeFabrica;
  }

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

  public Genero getGenero() {
      return genero;
  }

  public void setGenero(Genero genero) {
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

  public String getNomeFabrica() {
    return nomeFabrica;
  }

  public void setNomeFabrica(String nomeFabrica) {
    this.nomeFabrica = nomeFabrica;
  }

  @Override
  public String toString() {
    return "UsuarioDTO{id='%s', nome='%s', emailGerente='%s', genero='%s', dataNascimento=%s, cargo='%s', email='%s', tipoAcesso=%s, dataCriacao=%s, status=%b, nomeFabrica='%s'}"
        .formatted(id, nome, emailGerente, genero, dataNascimento, email, cargo, tipoAcesso, dataCriacao, status, nomeFabrica);
  }
}
