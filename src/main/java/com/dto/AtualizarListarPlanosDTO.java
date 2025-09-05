package com.dto;

import com.model.Planos;

public class AtualizarListarPlanosDTO extends Planos {
    //Construtor
    public AtualizarListarPlanosDTO(String nome, Double valor, String descricao){
        super(null, nome, valor, descricao);
    }

    //Cancelando métodos com ID
    public Integer getId(){
        throw new UnsupportedOperationException();
    }

    public void setId(Integer id){
        throw new UnsupportedOperationException();
    }
}
