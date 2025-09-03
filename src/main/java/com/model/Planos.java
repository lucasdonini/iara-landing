package com.model;

public class Planos{
//    atributos
    private int id;
    private String nome;
    private double valor;
    private String descricao;

//    construtor
    public Planos(int id, String nome, double valor, String descricao){
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
    }

//    toString
    public String toString(){
        return String.format("ID: %d\nNome: %s\nValor: R$%.2f\nDescrição: %s\n", this.id, this.nome, this.valor, this.descricao);
    }

//    getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

}
