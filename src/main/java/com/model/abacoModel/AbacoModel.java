package com.model.abacoModel;

import com.model.AbstractModel;

import java.time.LocalDate;

public class AbacoModel extends AbstractModel {
//    atributos
    private String descricao;
    private boolean ativo;
    private LocalDate dataCriacao;
    private ColunaModel coluna;
    private CorPesoModel corPeso;
    private LinhaModel linha;

//    constructor
    public AbacoModel(int id, String nome, String descricao, boolean ativo, LocalDate dataCriacao, ColunaModel coluna, CorPesoModel corPeso, LinhaModel linha){
        super(id, nome);
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.coluna = coluna;
        this.corPeso = corPeso;
        this.linha = linha;
    }

//    toString
    public String toString(){
        return String.format("%sDescrição: %s\nAtivo? %s\nData de Criação: %s\nColuna: %s\nCor Peso: %s\nLinha%s\n", super.toString(), this.descricao, this.ativo, this.dataCriacao, this.coluna, this.corPeso, this.linha);
    }

//    getters

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public ColunaModel getColuna() {
        return coluna;
    }

    public CorPesoModel getCorPeso() {
        return corPeso;
    }

    public LinhaModel getLinha() {
        return linha;
    }
}
