package com.model;

// tabela: endereco
public class Endereco {

  private Integer id; // coluna: id
  private String cep; // coluna: cep
  private Integer numero; // coluna: numero
  private String rua; // coluna: rua
  private String complemento; //coluna: complemento
  private Integer idFabrica; // coluna: id_fabrica
  private String bairro; // coluna: bairro
  private String cidade; // coluna: cidade
  private String estado; // coluna: estado

  public Endereco(Integer id, String cep, Integer numero, String rua, String complemento, Integer idFabrica, String bairro, String cidade, String estado) {
    this.id = id;
    this.cep = cep;
    this.numero = numero;
    this.rua = rua;
    this.complemento = complemento;
    this.idFabrica = idFabrica;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
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

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @Override
  public String toString() {
    return "Endereco{id=%d, cep='%s', numero=%d, rua='%s', complemento='%s', idFabrica=%d, bairro='%s', cidade='%s', estado='%s'}"
        .formatted(id, cep, numero, rua, complemento, idFabrica, bairro, cidade, estado);
  }
}
