package com.dto;

// DTO da tabela fabrica utilizado para a visualização dos dados registrados no banco de dados

public class FabricaDTO {

  private Integer id;
  private String nomeUnidade;
  private String cnpj;
  private Boolean status;
  private String emailCorporativo;
  private String nomeIndustria;
  private String ramo;
  private String endereco;
  private String plano;

  public FabricaDTO(Integer id, String nomeUnidade, String cnpj, Boolean status, String emailCorporativo, String nomeIndustria, String ramo, String endereco, String plano) {
    this.id = id;
    this.nomeUnidade = nomeUnidade;
    this.cnpj = cnpj;
    this.status = status;
    this.emailCorporativo = emailCorporativo;
    this.nomeIndustria = nomeIndustria;
    this.ramo = ramo;
    this.endereco = endereco;
    this.plano = plano;
  }

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

  // Método que formata o CNPJ no formato correto para armazenar no banco de dados
  public String cnpjFormatado() {
    StringBuilder sb = new StringBuilder(cnpj);
    sb.insert(12, "-");
    sb.insert(8, "/");
    sb.insert(5, ".");
    sb.insert(2, ".");
    return sb.toString();
  }

  @Override
  public String toString() {
    return "FabricaDTO{id=%d, nomeUnidade='%s', cnpj='%s', status=%b, emailCorporativo='%s', nomeIndustria='%s', ramo='%s', endereco='%s', plano='%s'}"
        .formatted(id, nomeUnidade, cnpj, status, emailCorporativo, nomeIndustria, ramo, endereco, plano);
  }
}
