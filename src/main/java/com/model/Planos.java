package com.model;

public class Planos {
    // atributos
    private Integer id;
    private String nome;
    private Double valor;
    private String descricao;

    // construtor
    public Planos(Integer id, String nome, Double valor, String descricao) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
    }

    // toString
    public String toString() {
        return String.format("ID: %d\nNome: %s\nValor: R$%.2f\nDescrição: %s\n", this.id, this.nome, this.valor,
                this.descricao);
    }

    // getters

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

}
