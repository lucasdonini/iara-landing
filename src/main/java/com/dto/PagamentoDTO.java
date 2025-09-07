package com.dto;

import com.model.Pagamento;

import java.time.LocalDate;

public class PagamentoDTO extends Pagamento {
    //Construtor
    public PagamentoDTO(Integer id, Boolean status, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamento, Integer fkPlano){
        super(id, status, dataVencimento, dataPagamento, tipoPagamento, fkPlano);
    }
}
