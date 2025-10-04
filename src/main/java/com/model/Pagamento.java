package com.model;

import java.time.LocalDate;
import java.util.Map;

public class Pagamento {
  public static final Map<String, String> tiposPagamento = Map.of(
      "debito", "Débito",
      "credito", "Crédito",
      "pix", "Pix"
  );

  //Atributos
  private Integer id;
  private Double valorPago;
  private Boolean status;
  private LocalDate dataVencimento;
  private LocalDate dataPagamento;
  private String tipoPagamento;
  private Integer fkFabrica;

  //Construtor
  public Pagamento(Integer id, Double valorPago, Boolean status, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamnto, Integer fkFabrica) {
    this.id = id;
    this.valorPago = valorPago;
    this.status = status;
    this.dataVencimento = dataVencimento;
    this.dataPagamento = dataPagamento;
    this.tipoPagamento = tipoPagamnto;
    this.fkFabrica = fkFabrica;
  }

  //toString
  public String toString() {
    return String.format("ID: %d\nValor: R$%.2f\nStatus: %b\nData de Vencimento: %s\nData do Pagamento: %s\nTipo de Pagamento: %s\n", this.id, this.valorPago, this.status, this.dataVencimento, this.dataPagamento, this.tipoPagamento);
  }

  // Getters e Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Double getValorPago() {
    return this.valorPago;
  }

  public void setValorPago(Double valorPago) {
    this.valorPago = valorPago;
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

  public Integer getFkFabrica() {
    return this.fkFabrica;
  }

  public void setFkFabrica(Integer fkFabrica) {
    this.fkFabrica = fkFabrica;
  }
}
