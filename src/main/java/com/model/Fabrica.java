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

  public Endereco getEndereco() {
    return this.endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  // toString()
  @Override
  public String toString() {
    return "%s - %s, %s".formatted(nome, nomeEmpresa, endereco.getRua());
  }

  // Outros métodos
  public String getCnpjFormatado() {
    StringBuilder sb = new StringBuilder(cnpj);
    sb.insert(12, "-");
    sb.insert(8, "/");
    sb.insert(5, ".");
    sb.insert(2, ".");
    return sb.toString();
  }
}
