package com.model;

// tabela: fabrica
public class Fabrica extends AbstractModel {
  // Atributos
  private String cnpj; // coluna: cnpj_unidade
  private boolean status; // coluna: status
  private String email; // coluna: email_corporativo
  private String nomeEmpresa; // coluna: nome_industria
  private String ramo; // coluna: ramo

  // Construtores
  public Fabrica(int id, String nome, String cnpj, boolean status, String email, String nomeEmpresa,
      String ramo) {
    super(id, nome);
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
  }

  public Fabrica() {
    super();
  }

  // Getters  
  public String getCnpj() {
    return cnpj;
  }
  
  public boolean getStatus() {
    return status;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getNomeEmpresa() {
    return nomeEmpresa;
  }
  
  public String getRamo() {
    return ramo;
  }

  // Setters
  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setNomeEmpresa(String factory_name) {
    this.nomeEmpresa = factory_name;
  }

  public void setRamo(String sector) {
    this.ramo = sector;
  }
}
