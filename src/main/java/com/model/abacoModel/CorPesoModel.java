package com.model.abacoModel;

import com.model.AbstractModel;

public class CorPesoModel extends AbstractModel {
//    attributes
    private int pesoValor;

//    constructor
    public CorPesoModel(int id, String nome, int pesoValor){
        super(id, nome);
        this.pesoValor = pesoValor;
    }

//    toString
    public String toString(){
        return String.format("%sValor: %d\n", super.toString(), this.pesoValor);
    }
//    getters
    public int getPesoValor() {
        return pesoValor;
    }
}
