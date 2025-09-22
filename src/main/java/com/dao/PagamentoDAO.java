package com.dao;

import com.dto.PagamentoDTO;
import com.model.Pagamento;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO extends DAO {
    public PagamentoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //Cadastrar novo Plano
    public void cadastrar(PagamentoDTO pagamento) throws SQLException {
        //Comando SQL
        String sql = "INSERT INTO pagamento(valor_pago, status, data_vencimento, data_pagamento, tipo_pagamento, fk_fabrica) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
            //Comando SQL de Query do valor do plano referente a fkFabrica
            String valorPagoSQL = "SELECT valor FROM planos p JOIN fabrica f ON p.id = f.fk_plano JOIN pagamento pa ON pa.fk_fabrica = f.id WHERE f.id = ?";
            //Preparando comando SQL
            PreparedStatement pstmtValor = conn.prepareStatement(valorPagoSQL);
            //Definindo variável relacionada a FK da fabrica
            pstmtValor.setInt(1, pagamento.getFkFabrica());
            //Executando a Query
            ResultSet rs = pstmtValor.executeQuery();

            //Definindo variáveis no código SQL
            if (rs.next()){
                pstmt.setDouble(1, rs.getDouble("valor"));
            } else{
                pstmt.setDouble(1, 0.0);
            }
            pstmt.setBoolean(2,pagamento.getStatus());
            pstmt.setDate(3, Date.valueOf(pagamento.getDataVencimento()));
            pstmt.setDate(4, Date.valueOf(pagamento.getDataPagamento()));
            pstmt.setString(5, pagamento.getTipoPagamento());
            pstmt.setInt(6, pagamento.getFkFabrica());
            //Salvando alterações no banco
            pstmt.execute();
            //Confirmando transações
            rs.close();
            pstmtValor.close();
            this.conn.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            //Cancelando transações
            this.conn.rollback();
            //Enviando exceção
            throw e;
        }
    }

    public PagamentoDTO getPagamentoById(int id) throws SQLException {
        // Prepara o comando
        String sql = "SELECT * FROM pagamento WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new PagamentoDTO(
                        id,
                        rs.getDouble("valor_pago"),
                        rs.getBoolean("status"),
                        rs.getDate("data_vencimento").toLocalDate(),
                        rs.getDate("data_pagamento").toLocalDate(),
                        rs.getString("tipo_pagamento"),
                        rs.getInt("fk_fabrica")
                );
            }
        }
    }

    //Remover plano
    public void remover(int id) throws SQLException {
        // Prepara o comando
        String sql = "DELETE FROM pagamento WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Completa os parâmetros faltantes
            pstmt.setInt(1, id);

            // Executa o comando e commita as mudanças
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {

            // Faz o rollback das modificações e propaga a exceção
            conn.rollback();
            throw e;
        }
    }

    public void atualizar(Pagamento original, Pagamento alterado) throws SQLException {
        // Desempacotamento do model alterado
        int id = alterado.getId();
        double valorPago = alterado.getValorPago();
        boolean status = alterado.getStatus();
        LocalDate dataVencimento = alterado.getDataVencimento();
        LocalDate dataPagamento = alterado.getDataPagamento();
        String tipoPagamento = alterado.getTipoPagamento();
        Integer fkFabrica = alterado.getFkFabrica();

        // Monta o comando de acordo com os campos alterados
        StringBuilder sql = new StringBuilder("UPDATE pagamento SET ");
        List<Object> valores = new ArrayList<>();

        if (original.getValorPago() != valorPago) {
            sql.append("valor_pago = ?, ");
            valores.add(valorPago);
        }

        if (!original.getStatus() == status) {
            sql.append("status = ?, ");
            valores.add(status);
        }

        if (!original.getDataVencimento().equals(dataVencimento)) {
            sql.append("data_vencimento = ?, ");
            valores.add(dataVencimento);
        }

        if (!original.getDataPagamento().equals(dataPagamento)) {
            sql.append("data_pagamento = ?, ");
            valores.add(dataPagamento);
        }

        if (!original.getTipoPagamento().equals(tipoPagamento)) {
            sql.append("tipo_pagamento = ?, ");
            valores.add(tipoPagamento);
        }

        if (!original.getFkFabrica().equals(fkFabrica)) {
            sql.append("fk_fabrica = ?, ");
            valores.add(fkFabrica);
        }

        // Sái do metodo se nada foi alterado
        if (valores.isEmpty()) {
            return;
        }

        // Remove o último espaço e a última vírgula
        sql.setLength(sql.length() - 2);

        // Adiciona o WHERE
        sql.append(" WHERE id = ?");
        valores.add(id);

        // Prepara, preenche e executa o comando
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            // Commita as alterações
            conn.commit();

        } catch (SQLException e) {

            // Faz o rollback das alterações e propaga a exceção
            conn.rollback();
            throw e;
        }
    }

    //Listar planos
    public List<PagamentoDTO> listarPagamentos(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
        List<PagamentoDTO> pagamentoDTOS = new ArrayList<>();

        // Prepara o comando
        StringBuilder sql = new StringBuilder("SELECT * FROM pagamento");

        // Verificando campo do filtro
        if (campoFiltro!=null){
            switch (campoFiltro) {
                case "id" -> sql.append(" WHERE id = ?");
                case "status" -> sql.append(" WHERE status = ?");
                case "tipo_pagamento" -> sql.append(" WHERE tipo_pagamento = ?");
                case "data_vencimento" -> sql.append(" WHERE data_vencimento = ?");
                case "fk_fabrica" -> sql.append(" WHERE fk_fabrica = ?");
                case "data_pagamento" -> sql.append(" WHERE data_pagamento = ?");
                default -> sql.append(" WHERE valor_pago = ?");
            }
        }

        //Verificando campo para ordenar a consulta
        if (campoSequencia!=null){
            switch (campoSequencia) {
                case "id" -> sql.append(" ORDER BY id");
                case "status" -> sql.append(" ORDER BY status");
                case "tipo_pagamento" -> sql.append(" ORDER BY tipo_pagamento");
                case "data_vencimento" -> sql.append(" ORDER BY data_vencimento");
                case "fk_fabrica" -> sql.append(" ORDER BY fk_fabrica");
                case "data_pagamento" -> sql.append(" ORDER BY data_pagamento");
                default -> sql.append(" ORDER BY valor_pago");
            }

            //Verificando direção da sequencia
            switch(direcaoSequencia){
                case "crescente" -> sql.append(" ASC");
                case "decrescente" -> sql.append(" DESC");
            }
        }

        try (PreparedStatement pstmt = conn.prepareStatement(String.valueOf(sql))) {
            //Definindo parâmetro vazio
            if (campoFiltro!=null){
                pstmt.setObject(1, valorFiltro);
            }

            //Instanciando um ResultSet
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double valorPago = rs.getDouble("valor_pago");
                boolean status = rs.getBoolean("status");
                LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
                LocalDate dataPagamento = rs.getDate("data_pagamento").toLocalDate();
                String tipoPagamento = rs.getString("tipo_pagamento");
                Integer fkFabrica = rs.getInt("fk_fabrica");

                pagamentoDTOS.add(new PagamentoDTO(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica));
            }
        }

        return pagamentoDTOS;
    }

    //Campos Alteraveis
    public Pagamento getCamposAlteraveis(int id) throws SQLException {
        // Prepara o comando
        String sql = "SELECT * FROM pagamento WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double valorPago = rs.getDouble("valor_pago");
                    boolean status = rs.getBoolean("status");
                    LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
                    LocalDate dataPagamento = rs.getDate("data_pagamento").toLocalDate();
                    String tipoPagamento = rs.getString("tipo_pagamento");
                    Integer fkFabrica = rs.getInt("fk_fabrica");

                    return new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);
                } else {
                    throw new SQLException("Erro ao recuperar as informações do super adm");
                }
            }
        }
    }
}
