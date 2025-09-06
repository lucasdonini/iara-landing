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
  private Endereco endereco;

  public Fabrica(int id, String nome, String cnpj, Boolean status, String email, String nomeEmpresa,
      String ramo, Endereco endereco) {
    this.id = id;
    this.nome = nome;
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
    this.endereco = endereco;
  }

  // Getters
  public int getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getCnpj() {
    return cnpj;
  }

  public Boolean getStatus() {
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

  public Endereco getEndereco(){return this.endereco;}

  // Setters
  public void setId(int id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public void setStatus(Boolean status) {
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

  public void setEndereco(Endereco endereco){this.endereco = endereco;}
}
