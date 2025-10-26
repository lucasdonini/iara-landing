package com.dao;

import com.model.MetodoPagamento;
import com.model.Pagamento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagamentoDAO extends DAO {
    // Constante dos campos utilizados para ordenação e filtragem da listagem dos dados
    public static final Map<String, String> camposFiltraveis = Map.of(
            "id", "Id",
            "valor", "Valor Pago",
            "status", "Status",
            "tipo_pagamento", "Tipo de Pagamento",
            "data_vencimento", "Data de Vencimento",
            "data_pagamento", "Data do Pagamento",
            "fk_metodopag", "Fábrica"
    );

    public PagamentoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Método que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id", "fk_metodopag" -> Integer.parseInt(valor);
                case "valor" -> Double.parseDouble(valor);
                case "status" -> Boolean.parseBoolean(valor);
                case "data_vencimento" -> LocalDate.parse(valor);
                case "data_pagamento", "data_inicio" -> LocalDateTime.parse(valor);
                default -> new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // === CREATE ===
    public void cadastrar(Pagamento pagamento) throws SQLException {

        String sql = """
                INSERT INTO pagamento(valor, status, data_vencimento, data_pagamento, data_inicio, fk_metodopag, fk_fabrica, fk_plano)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LocalDateTime dtPagamento = pagamento.getDataPagamento();
            LocalDateTime dtInicio = pagamento.getDataInicio();
            LocalDate dtVencimento = pagamento.getDataVencimento();

            pstmt.setDouble(1, pagamento.getValor());
            pstmt.setBoolean(2, pagamento.getStatus());
            pstmt.setDate(3, (dtVencimento == null ? null : Date.valueOf(dtVencimento)));
            pstmt.setTimestamp(4, (dtPagamento == null ? null : Timestamp.valueOf(dtPagamento)));
            pstmt.setTimestamp(5, (dtInicio == null ? null : Timestamp.valueOf(dtInicio)));
            pstmt.setInt(6, pagamento.getMetodoPagamento().getNivel());
            pstmt.setInt(7, pagamento.getFkFabrica());
            pstmt.setInt(8, pagamento.getFkPlano());

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === READ ===
    public List<Pagamento> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // Variável de apoio para verificar se a listagem será filtrada
        boolean temFiltro = true;

        List<Pagamento> pagamentos = new ArrayList<>();

        String sql = "SELECT * FROM pagamento";

        //Verificando o campo de filtragem
        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        // Verificando campo e direcao da ordenação
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

        } else {
            sql += " ORDER BY id ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Verifica se tem filtro, se sim define a variável do comando SQL
            if (temFiltro) {
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    double valorPago = rs.getDouble("valor");
                    boolean status = rs.getBoolean("status");

                    Date dtVencimentoDate = rs.getDate("data_vencimento");
                    LocalDate dtVencimento = (dtVencimentoDate == null ? null : dtVencimentoDate.toLocalDate());

                    Timestamp dtPagtoTimestamp = rs.getTimestamp("data_pagamento");
                    LocalDateTime dtPagto = (dtPagtoTimestamp == null ? null : dtPagtoTimestamp.toLocalDateTime());

                    Timestamp dtInicioTimestamp = rs.getTimestamp("data_inicio");
                    LocalDateTime dtInicio = (dtInicioTimestamp == null ? null : dtInicioTimestamp.toLocalDateTime());

                    MetodoPagamento metodoPagamento = MetodoPagamento.deNivel(rs.getInt("fk_metodopag"));
                    Integer fkFabrica = rs.getInt("fk_fabrica");
                    Integer fkPlano = rs.getInt("fk_plano");

                    pagamentos.add(new Pagamento(id, valorPago, status, dtVencimento, dtPagto, dtInicio, metodoPagamento, fkFabrica, fkPlano));
                }
            }
        }

        conn.commit();
        return pagamentos;
    }

    // === UPDATE ===
    public void atualizar(Pagamento original, Pagamento alterado) throws SQLException {

        int id = alterado.getId();
        double valorPago = alterado.getValor();
        boolean status = alterado.getStatus();
        LocalDate dataVencimento = alterado.getDataVencimento();
        LocalDateTime dataPagamento = alterado.getDataPagamento();
        LocalDateTime dataInicio = alterado.getDataInicio();
        MetodoPagamento metodoPagamento = alterado.getMetodoPagamento();
        int fkFabrica = alterado.getFkFabrica();
        int fkPlano = alterado.getFkPlano();

        StringBuilder sql = new StringBuilder("UPDATE pagamento SET ");
        List<Object> valores = new ArrayList<>();

        // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
        if (original.getValor() != valorPago) {
            sql.append("valor = ?, ");
            valores.add(valorPago);
        }

        if (original.getStatus() != status) {
            sql.append("status = ?, ");
            valores.add(status);
        }

        if (!Objects.equals(dataVencimento, original.getDataVencimento())) {
            sql.append("data_vencimento = ?, ");
            valores.add(dataVencimento);
        }

        if (!Objects.equals(dataPagamento, original.getDataPagamento())) {
            sql.append("data_pagamento = ?, ");
            valores.add(dataPagamento);
        }

        if (!Objects.equals(dataInicio, original.getDataInicio())) {
            sql.append("data_inicio = ?, ");
            valores.add(dataInicio);
        }

        if (!Objects.equals(metodoPagamento, original.getMetodoPagamento())) {
            sql.append("fk_metodopag = ?, ");
            valores.add(metodoPagamento.getNivel());
        }

        if (original.getFkFabrica() != fkFabrica) {
            sql.append("fk_fabrica = ?, ");
            valores.add(fkFabrica);
        }

        if (original.getFkPlano() != fkPlano) {
            sql.append("fk_plano = ?, ");
            valores.add(fkPlano);
        }

        // Retorna vazio se nada foi alterado
        if (valores.isEmpty()) {
            return;
        }

        // Remoção da última ", "
        sql.setLength(sql.length() - 2);

        // Adiciona a cláusula WHERE
        sql.append(" WHERE id = ?");
        valores.add(id);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === DELETE ===
    public void remover(int id) throws SQLException {

        String sql = "DELETE FROM pagamento WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Resgata pagamento pelo ID
    public Pagamento pesquisarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM pagamento WHERE id = ?";

        Pagamento pagamento;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o pagamento retorna null
                if (!rs.next()) {
                    return null;
                }

                double valorPago = rs.getDouble("valor");
                boolean status = rs.getBoolean("status");

                Date dtVencimentoDate = rs.getDate("data_vencimento");
                LocalDate dtVencimento = dtVencimentoDate.toLocalDate();

                Timestamp dtPagtoTimestamp = rs.getTimestamp("data_pagamento");
                LocalDateTime dtPagto = (dtPagtoTimestamp == null ? null : dtPagtoTimestamp.toLocalDateTime());

                Timestamp dtInicioTimestamp = rs.getTimestamp("data_inicio");
                LocalDateTime dtInicio = (dtInicioTimestamp == null ? null : dtInicioTimestamp.toLocalDateTime());

                MetodoPagamento metodoPagamento = MetodoPagamento.deNivel(rs.getInt("fk_metodopag"));
                int fkFabrica = rs.getInt("fk_fabrica");
                int fkPlano = rs.getInt("fk_plano");

                pagamento = new Pagamento(id, valorPago, status, dtVencimento, dtPagto, dtInicio, metodoPagamento, fkFabrica, fkPlano);
            }
        }

        conn.commit();
        return pagamento;
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    public Pagamento getCamposAlteraveis(int id) throws SQLException {

        Pagamento p = pesquisarPorId(id);

        // Se não encontrar o pagamento lança exceção
        if (p == null) {
            throw new SQLException("Falha ao recuperar pagamento");
        }

        conn.commit();
        return p;
    }
}
