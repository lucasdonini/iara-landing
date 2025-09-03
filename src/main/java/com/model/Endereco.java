package com.model;

public class Endereco {
//    atributos
    private int id;
    private String cep;
    private int numero;
    private String rua;
    private String complemento;

//    construtor
    public Endereco(int id, String cep, int numero, String rua, String complemento){
        this.id = id;
        this.cep = cep;
        this.numero = numero;
        this.rua = rua;
        this.complemento = complemento;
    }

//    toString
    public String toString(){
        return String.format("ID: %s\nCEP: %s\nNúmero: %d\nRua: %s\nComplemento: %s\n", this.id, this.cep, this.numero, this.rua, this.complemento);
    }

//    getters

    public int getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public int getNumero() {
        return numero;
    }

    public String getRua() {
        return rua;
    }

    public String getComplemento() {
        return complemento;
    }
}
