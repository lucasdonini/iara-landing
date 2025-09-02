package com.model;

public class Fabrica {
  private int id;
  private String nomeUnidade;
  private String cnpj;
  private boolean status;
  private String email;
  private String nomeEmpresa;
  private String ramo;

  public Fabrica(int id, String nomeUnidade, String cnpj, boolean status, String email, String nomeEmpresa,
      String ramo) {
    this.id = id;
    this.nomeUnidade = nomeUnidade;
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
  }

  public Fabrica() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNomeUnidade() {
    return nomeUnidade;
  }

  public void setNomeUnidade(String name) {
    this.nomeUnidade = name;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
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
}
