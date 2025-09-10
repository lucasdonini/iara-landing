package com.model;

//tabela: Endereco
public class Endereco {
  //    Atributos
  private Integer id; // coluna: id
  private String cep; // coluna: cep
  private int numero; // coluna: numero
  private String rua; // coluna: rua
  private String complemento; //coluna: complemento

  // Construtor
  public Endereco(Integer id, String cep, int numero, String rua, String complemento) {
    this.id = id;
    this.cep = cep;
    this.numero = numero;
    this.rua = rua;
    this.complemento = complemento;
  }

  // toString
  public String toString() {
    String complemento = this.complemento == null ? "" : " - " + this.complemento;
    return "%s, nº %d %s".formatted(rua, numero, complemento);
  }

  // Getters e Setters
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

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
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
}
