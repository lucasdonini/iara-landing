package com.model;

import com.utils.StringUtils;

/*
* ENUM referente a tabela metodo_pagamento, onde cada instância é uma simplificação do nome presente no banco, e cada atributo é referente a uma coluna do banco de dados:
*
*  nivel -> id
*  metodo -> tipo_pagamento
* */
public enum MetodoPagamento {

    CREDITO(1, "Cartão de Crédito"),
    BOLETO(2, "Boleto Bancário"),
    PIX(3, "PIX"),
    TRANSFERENCIA(4, "Transferência Bancária"),
    DEBITO(5, "Débito Automático");

    private final int nivel;
    private final String metodo;

    MetodoPagamento(int nivel, String metodo){
        this.nivel = nivel;
        this.metodo = metodo;
    }

    // Método que recebe como parâmetro um número e retorna a instância que tem nível compatível com o número recebido
    public static MetodoPagamento deNivel(int nivel){
        for (MetodoPagamento m : values()){
            if (m.nivel == nivel){
                return m;
            }
        }

        throw new IllegalArgumentException("Nível Inválido");
    }

    public int getNivel(){
        return nivel;
    }

    public String getMetodo(){
        return metodo;
    }

    public String toString() {
        return StringUtils.capitalize(name());
    }

}
