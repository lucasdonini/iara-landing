package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

// tabela: pagamento
public class Pagamento {

  private Integer id; // coluna: id
  private Double valor; // coluna: valor
  private Boolean status; // coluna: status
  private LocalDate dataVencimento; // coluna: data_vencimento
  private LocalDateTime dataPagamento; // coluna: data_pagamento
  private LocalDateTime dataInicio; // coluna: data_inicio
  private MetodoPagamento metodoPagamento; // coluna: fk_metodopag
  private Integer fkFabrica; // coluna: fk_fabrica
  private Integer fkPlano; // coluna: fk_plano


  public Pagamento(Integer id, Double valor, Boolean status, LocalDate dataVencimento, LocalDateTime dataPagamento, LocalDateTime dataInicio, MetodoPagamento metodoPagamento, Integer idFabrica, Integer fkPlano) {
    this.id = id;
    this.valor = valor;
    this.status = status;
    this.dataVencimento = dataVencimento;
    this.dataPagamento = dataPagamento;
    this.dataInicio = dataInicio;
    this.metodoPagamento = metodoPagamento;
    this.fkFabrica = idFabrica;
    this.fkPlano = fkPlano;
  }


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

  public LocalDateTime getDataPagamento() {
    return dataPagamento;
  }

  public void setDataPagamento(LocalDateTime dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public LocalDateTime getDataInicio() {
      return dataInicio;
  }

  public void setDataInicio(LocalDateTime dataInicio) {
      this.dataInicio = dataInicio;
  }

  public MetodoPagamento getMetodoPagamento() {
    return metodoPagamento;
  }

  public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
    this.metodoPagamento = metodoPagamento;
  }

  public Integer getFkFabrica() {
    return this.fkFabrica;
  }

  public void setFkFabrica(Integer fkFabrica) {
    this.fkFabrica = fkFabrica;
  }

  public Integer getFkPlano() {
      return fkPlano;
  }

  public void setFkPlano(Integer fkPlano) {
      this.fkPlano = fkPlano;
  }


  @Override
  public String toString() {
    return "Pagamento{id=%d, valor=%.2f, status=%b, dataVencimento=%s, dataPagamento=%s, dataInicio=%s, metodoPagamento='%s', fkFabrica=%d, fkPlano=%d}"
        .formatted(id, valor, status, dataVencimento, dataPagamento, dataInicio, metodoPagamento, fkFabrica, fkPlano);
  }
}
