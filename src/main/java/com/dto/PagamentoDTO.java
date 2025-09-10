package com.dto;

import java.time.LocalDate;

public class PagamentoDTO {
  //Atributos
  private Integer id;
  private Boolean status;
  private LocalDate dataVencimento;
  private LocalDate dataPagamento;
  private String tipoPagamento;
  private Integer fkPlano;

  //Construtor
  public PagamentoDTO(Integer id, Boolean status, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamnto, Integer fkPlano) {
    this.id = id;
    this.status = status;
    this.dataVencimento = dataVencimento;
    this.dataPagamento = dataPagamento;
    this.tipoPagamento = tipoPagamnto;
    this.fkPlano = fkPlano;
  }

  //toString
  public String toString() {
    return String.format("ID: %d\nStatus: %b\nData de Vencimento: %s\nData do Pagamento: %s\nTipo de Pagamento: %s\n", this.id, this.status, this.dataVencimento, this.dataPagamento, this.tipoPagamento);
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Integer getFkPlano() {
    return fkPlano;
  }

  public void setFkPlano(Integer fkPlano) {
    this.fkPlano = fkPlano;
  }
}
