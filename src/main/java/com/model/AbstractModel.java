package com.model;

public abstract class AbstractModel {
//    atributos
    private final int id;
    private final String nome;

    //    construtor
    public AbstractModel(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    //    toString
    public String toString(){
        return String.format("ID: %d\nNome: %s\n", this.id, this.nome);
    }

    //    getters
    public int getId(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
}
