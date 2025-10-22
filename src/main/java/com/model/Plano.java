package com.model;

import org.postgresql.util.PGInterval;

// tabela: plano
public class Plano {
  // Atributos
  private Integer id; // coluna: id
  private String nome; // coluna: nome
  private Double valor; // coluna: valor
  private String descricao; // coluna: descricao
  private PGInterval duracao; // coluna: duracao

  // Construtor
  public Plano(Integer id, String nome, Double valor, String descricao, PGInterval duracao) {
    this.id = id;
    this.nome = nome;
    this.valor = valor;
    this.descricao = descricao;
    this.duracao = duracao;
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

  public PGInterval getDuracao() {
      return duracao;
  }

  public void setDuracao(PGInterval duracao) {
      this.duracao = duracao;
  }

    // toString
  @Override
  public String toString() {
    return "Plano{id=%d, nome='%s', valor=%.2f, descricao='%s', duracao=%s}".formatted(id, nome, valor, descricao, duracao);
  }
}
