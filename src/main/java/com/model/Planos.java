package com.model;

//tabela: Planos
public class Planos{
//    Atributos
    private int id;
    private String nome;
    private double valor;
    private String descricao;

//    Construtor
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

//    Getters

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

//    Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
