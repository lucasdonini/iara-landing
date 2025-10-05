package com.model;

// tabela: plano
public class Plano {
  // Atributos
  private Integer id; // coluna: id
  private String nome; // coluna: nome
  private Double valor; // coluna: valor
  private String descricao; // coluna: descricao

  // Construtor
  public Plano(Integer id, String nome, Double valor, String descricao) {
    this.id = id;
    this.nome = nome;
    this.valor = valor;
    this.descricao = descricao;
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Double getValor() {
    return valor;
  }

  public void setValor(Double valor) {
    this.valor = valor;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  // toString
  @Override
  public String toString() {
    return "Plano{id=%d, nome='%s', valor=%.2f, descricao='%s'}".formatted(id, nome, valor, descricao);
  }
}
