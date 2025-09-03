package com.model;

public class PlanosModel extends AbstractModel {
//    atributos
    private double valor;
    private String descricao;

//    construtor
    public PlanosModel(int id, String nome, double valor, String descricao){
        super(id, nome);
        this.valor = valor;
        this.descricao = descricao;
    }

//    toString
    public String toString(){
        return String.format("%sValor: R$%.2f\nDescrição: %s\n", super.toString(), this.valor, this.descricao);
    }

//    getters
    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

}
