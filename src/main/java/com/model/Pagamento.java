package com.model;

import java.time.LocalDate;
import java.util.Map;

// tabela: pagamento
public class Pagamento {
  // Constantes
  public static final Map<String, String> tiposPagamento = Map.of(
      "debito", "Débito",
      "credito", "Crédito",
      "pix", "Pix"
  );

  // Atributos
  private Integer id; // coluna: id
  private Double valor; // coluna: valor
  private Boolean status; // coluna: status
  private LocalDate dataVencimento; // coluna: data_vencimento
  private LocalDate dataPagamento; // coluna data_pagamento
  private String tipoPagamento; // coluna: tipo_pagamento
  private Integer idFabrica; // coluna: id_fabrica

  // Construtor
  public Pagamento(Integer id, Double valor, Boolean status, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamnto, Integer idFabrica) {
    this.id = id;
    this.valor = valor;
    this.status = status;
    this.dataVencimento = dataVencimento;
    this.dataPagamento = dataPagamento;
    this.tipoPagamento = tipoPagamnto;
    this.idFabrica = idFabrica;
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Double getValor() {
    return this.valor;
  }

  public void setValor(Double valor) {
    this.valor = valor;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public LocalDate getDataVencimento() {
    return dataVencimento;
  }

  public void setDataVencimento(LocalDate dataVencimento) {
    this.dataVencimento = dataVencimento;
  }

  public LocalDate getDataPagamento() {
    return dataPagamento;
  }

  public void setDataPagamento(LocalDate dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public String getTipoPagamento() {
    return tipoPagamento;
  }

  public void setTipoPagamento(String tipoPagamento) {
    this.tipoPagamento = tipoPagamento;
  }

  public Integer getIdFabrica() {
    return this.idFabrica;
  }

  public void setIdFabrica(Integer idFabrica) {
    this.idFabrica = idFabrica;
  }

  // toString
  @Override
  public String toString() {
    return "Pagamento{id=%d, valor=%.2f, status=%b, dataVencimento=%s, dataPagamento=%s, tipoPagamento='%s', idFabrica=%d}"
        .formatted(id, valor, status, dataVencimento, dataPagamento, tipoPagamento, idFabrica);
  }
}
