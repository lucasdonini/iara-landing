package com.model;

// tabela: fabrica
public class Fabrica {
  private int id;
  private String nome;
  private String cnpj;
  private Boolean status;
  private String email;
  private String nomeEmpresa;
  private String ramo;
  private int idPlano;

  public Fabrica(int id, String nome, String cnpj, Boolean status, String email, String nomeEmpresa, String ramo, int idPlano) {
    this.id = id;
    this.nome = nome;
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
    this.idPlano = idPlano;
  }

  // Getters e Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNomeEmpresa() {
    return nomeEmpresa;
  }

  public void setNomeEmpresa(String factory_name) {
    this.nomeEmpresa = factory_name;
  }

  public String getRamo() {
    return ramo;
  }

  public void setRamo(String sector) {
    this.ramo = sector;
  }

  public int getIdPlano() {
    return idPlano;
  }

  public void setIdPlano(int idPlano) {
    this.idPlano = idPlano;
  }
}
