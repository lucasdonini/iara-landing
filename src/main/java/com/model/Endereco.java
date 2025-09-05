package com.model;

public class Endereco {
    // atributos
    private Integer id;
    private String cep;
    private Integer numero;
    private String rua;
    private String complemento;

    // construtor
    public Endereco(Integer id, String cep, Integer numero, String rua, String complemento) {
        this.id = id;
        this.cep = cep;
        this.numero = numero;
        this.rua = rua;
        this.complemento = complemento;
    }

    // toString
    public String toString() {
        return String.format("ID: %s\nCEP: %s\nNúmero: %d\nRua: %s\nComplemento: %s\n", this.id, this.cep, this.numero,
                this.rua, this.complemento);
    }

    // getters

    public Integer getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getRua() {
        return rua;
    }

    public String getComplemento() {
        return complemento;
    }
}
