package com.dto;

public class CadastroFabricaDTO {
  private String nome;
  private String cnpj;
  private String email;
  private String nomeEmpresa;
  private String ramo;

  public CadastroFabricaDTO(String nome, String cnpj, String email, String nomeEmpresa,
      String ramo) {
    this.nome = nome;
    this.cnpj = cnpj;
    this.email = email;
    this.nomeEmpresa = nomeEmpresa;
    this.ramo = ramo;
  }

  // Getters
  public String getNome() {
    return nome;
  }

  public String getCnpj() {
    return cnpj;
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
  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
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
