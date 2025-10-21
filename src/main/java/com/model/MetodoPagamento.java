package com.model;

import com.utils.StringUtils;

public enum MetodoPagamento {
    // Instâncias
    CREDITO(1, "Cartão de Crédito"),
    BOLETO(2, "Boleto Bancário"),
    PIX(3, "PIX"),
    TRANSFERENCIA(4, "Transferência Bancária"),
    DEBITO(5, "Débito Automático");

    // Atributos
    private final int nivel;
    private final String metodo;

    // Construtor
    MetodoPagamento(int nivel, String metodo){
        this.nivel = nivel;
        this.metodo = metodo;
    }

    // Métodos estáticos
    public static MetodoPagamento deNivel(int nivel){
        for (MetodoPagamento m : values()){
            if (m.nivel == nivel){
                return m;
            }
        }

        throw new IllegalArgumentException("Nível Inválido");
    }

    //Getters
    public int getNivel(){
        return nivel;
    }

    public String getMetodo(){
        return metodo;
    }

    // toString
    public String toString() {
        return StringUtils.capitalize(name());
    }

}
