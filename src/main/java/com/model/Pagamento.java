package com.model;

import java.time.LocalDate;

public class Pagamento {
    //Atributos
    private Integer idPagamento;
    private Boolean status;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String tipoPagamento;
    private Integer fkPlano;

    //Construtor
    public Pagamento(Integer idPagamento, Boolean status, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamnto, Integer fkPlano){
        this.idPagamento = idPagamento;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamnto;
        this.fkPlano = fkPlano;
    }

    //toString
    public String toString(){
        return String.format("ID: %d\nStatus: %b\nData de Vencimento: %s\nData do Pagamento: %s\nTipo de Pagamento: %s\n", this.idPagamento, this.status, this.dataVencimento, this.dataPagamento, this.tipoPagamento);
    }

    //Getters

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public Boolean getStatus() {
        return status;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public Integer getFkPlano() {
        return fkPlano;
    }

    //Setters

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public void setFkPlano(Integer fkPlano) {
        this.fkPlano = fkPlano;
    }
}
