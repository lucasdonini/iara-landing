package com.model;

// tabela: endereco
public class Endereco {

  private Integer id; // coluna: id
  private String cep; // coluna: cep
  private Integer numero; // coluna: numero
  private String rua; // coluna: rua
  private String complemento; //coluna: complemento
  private Integer idFabrica; // coluna: id_fabrica

  public Endereco(Integer id, String cep, Integer numero, String rua, String complemento, Integer idFabrica) {
    this.id = id;
    this.cep = cep;
    this.numero = numero;
    this.rua = rua;
    this.complemento = complemento;
    this.idFabrica = idFabrica;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public Integer getNumero() {
    return numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public String getRua() {
    return rua;
  }

  public void setRua(String rua) {
    this.rua = rua;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public Integer getIdFabrica() {
    return idFabrica;
  }

  public void setIdFabrica(Integer idFabrica) {
    this.idFabrica = idFabrica;
  }

  @Override
  public String toString() {
    return "Endereco{id=%d, cep='%s', numero=%d, rua='%s', complemento='%s', idFabrica=%d}"
        .formatted(id, cep, numero, rua, complemento, idFabrica);
  }
}
