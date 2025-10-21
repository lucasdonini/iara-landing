package com.dto;

import com.model.MetodoPagamento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PagamentoDTO {

    // Atributos
    private Integer id;
    private Double valor;
    private Boolean status;
    private LocalDate dataVencimento;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataInicio;
    private MetodoPagamento metodoPagamento;
    private String nomeFabrica;
    private String nomePlano;

    // Construtor
    public PagamentoDTO(Integer id, Double valor, Boolean status, LocalDate dataVencimento, LocalDateTime dataPagamento, LocalDateTime dataInicio, MetodoPagamento metodoPagamento, String nomeFabrica, String nomePlano){
        this.id = id;
        this.valor = valor;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.dataInicio = dataInicio;
        this.metodoPagamento = metodoPagamento;
        this.nomeFabrica = nomeFabrica;
        this.nomePlano = nomePlano;
    }

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
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

    public String getNomeFabrica() {
        return nomeFabrica;
    }

    public void setNomeFabrica(String nomeFabrica) {
        this.nomeFabrica = nomeFabrica;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    // toString
    public String toString(){
        return "PagamentoDTO(id=%d, valor=%.2f, status=%b, dataVencimento=%s, dataPagamento=%s, dataIncio=%s, metodoPagamento=%s, nomeFabrica=%s, nomePlano=%s".formatted(valor, status, dataVencimento, dataPagamento, dataInicio, metodoPagamento, nomeFabrica, nomePlano);
    }
}
