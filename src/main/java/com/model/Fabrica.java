package com.model;

// tabela: fabrica
public class Fabrica {
  // Atributos
  private Integer id; // coluna: id
  private String nomeUnidade; // coluna: nome_unidade
  private String cnpj; // coluna: cnpj
  private Boolean status; // coluna: status
  private String emailCorporativo; // coluna: email_corporativo
  private String nomeIndustria; // coluna: nome_industria
  private String ramo; // coluna: ramo
  private Integer idPlano; // coluna: id_plano

  // Construtor
  public Fabrica(Integer id, String nomeUnidade, String cnpj, Boolean status, String emailCorporativo, String nomeIndustria, String ramo, Integer idPlano) {
    this.id = id;
    this.nomeUnidade = nomeUnidade;
    this.cnpj = cnpj;
    this.status = status;
    this.emailCorporativo = emailCorporativo;
    this.nomeIndustria = nomeIndustria;
    this.ramo = ramo;
    this.idPlano = idPlano;
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNomeUnidade() {
    return nomeUnidade;
  }

  public void setNomeUnidade(String nomeUnidade) {
    this.nomeUnidade = nomeUnidade;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getEmailCorporativo() {
    return emailCorporativo;
  }

  public void setEmailCorporativo(String emailCorporativo) {
    this.emailCorporativo = emailCorporativo;
  }

  public String getNomeIndustria() {
    return nomeIndustria;
  }

  public void setNomeIndustria(String nomeIndustria) {
    this.nomeIndustria = nomeIndustria;
  }

  public String getRamo() {
    return ramo;
  }

  public void setRamo(String ramo) {
    this.ramo = ramo;
  }

  public Integer getIdPlano() {
    return idPlano;
  }

  public void setIdPlano(Integer idPlano) {
    this.idPlano = idPlano;
  }

  // toString
  @Override
  public String toString() {
    return "Fabrica{id=%s, nomeUnidade='%s', cnpj='%s', status=%b, emailCorporativo='%s', nomeIndustria='%s', ramo='%s', idPlano=%d}"
        .formatted(id, nomeUnidade, cnpj, status, emailCorporativo, nomeIndustria, ramo, idPlano);
  }
}
