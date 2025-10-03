package com.dto;

public class FabricaDTO {
  private int id;
  private String nome;
  private String cnpj;
  private boolean status;
  private String email;
  private String nomeEmpresa;
  private String ramo;
  private String endereco;
  private String plano;

  // Construtor
  public FabricaDTO(int id, String nome, String cnpj, boolean status, String email, String nomeEmpresa, String ramo, String endereco, String plano) {
    this.id = id;
    this.nome = nome;
    this.cnpj = cnpj;
    this.status = status;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
    this.endereco = endereco;
    this.plano = plano;
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

  public void setNomeEmpresa(String nomeEmpresa) {
    this.nomeEmpresa = nomeEmpresa;
  }

  public String getRamo() {
    return ramo;
  }

  public void setRamo(String ramo) {
    this.ramo = ramo;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getPlano() {
    return plano;
  }

  public void setPlano(String plano) {
    this.plano = plano;
  }

  // toString()
  @Override
  public String toString() {
    return "%s - %s".formatted(nome, nomeEmpresa);
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
