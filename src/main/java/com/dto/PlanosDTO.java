package com.dto;

import com.model.Planos;

public class PlanosDTO extends Planos {
    //Construtor
    public PlanosDTO(String nome, Double valor, String descricao){
        super(null, nome, valor, descricao);
    }

    //Cancelando métodos com ID
    @Override
    public Integer getId(){
        throw new UnsupportedOperationException();
    }

    public void setId(Integer id){
        throw new UnsupportedOperationException();
    }
}
