package com.dto;

import com.model.Endereco;

public class EnderecoDTO {
    //    Atributos
    private Integer id; // coluna: id
    private String cep; // coluna: cep
    private Integer numero; // coluna: numero
    private String rua; // coluna: rua
    private String complemento; //coluna: complemento

    //    Construtor
    public EnderecoDTO(Integer id, String cep, Integer numero, String rua, String complemento){
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

//    Getters

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

//    Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
