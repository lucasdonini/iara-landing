package com.dao;

import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.model.Fabrica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FabricaDAO extends DAO {
    // Constante dos campos utilizados para ordenação e filtragem da listagem dos dados
    public static final Map<String, String> camposFiltraveis = Map.of(
            "id", "Id",
            "cnpj", "CNPJ",
            "nome_unidade", "Nome da Unidade",
            "status", "Status",
            "email_corporativo", "Email Corporativo",
            "nome_industria", "Nome da Indústria",
            "ramo", "Ramo",
            "endereco", "Endereço",
            "plano", "Plano"
    );

    public FabricaDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Método que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id" -> Integer.parseInt(valor);
                case "status" -> Boolean.parseBoolean(valor);
                case "email_corporativo", "nome_unidade", "nome_industria", "cnpj", "ramo" -> valor;
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // === CREATE ===
    public int cadastrar(CadastroFabricaDTO credenciais) throws SQLException {

        String nome = credenciais.getNomeUnidade();
        String cnpj = credenciais.getCnpj();
        String email = credenciais.getEmailCorporativo();
        String nomeEmpresa = credenciais.getNomeIndustria();
        String ramo = credenciais.getRamo();
        int id, idPlano = credenciais.getIdPlano();
        boolean status = true;

        String sql = """
                INSERT INTO fabrica (nome_unidade, cnpj_unidade, email_corporativo, nome_industria, status, ramo, fk_plano)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cnpj);
            pstmt.setString(3, email);
            pstmt.setString(4, nomeEmpresa);
            pstmt.setBoolean(5, status);
            pstmt.setString(6, ramo);
            pstmt.setInt(7, idPlano);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Caso ocorra algum erro interno ao cadastrar a fábrica, lança exceção
                if (!rs.next()) {
                    throw new SQLException("Erro inesperado ao criar fábrica");
                }

                id = rs.getInt("id");
            }

            conn.commit();

            return id;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === READ ===
    public List<FabricaDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // Variável de apoio para verificar se a listagem será filtrada
        boolean temFiltro = true;

        List<FabricaDTO> fabricas = new ArrayList<>();

        String sql = "SELECT id, nome_unidade, cnpj, status, email_corporativo, nome_industria, ramo, endereco, plano FROM exibicao_fabrica";

        // Verificando o campo de filtragem
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
                    int idFabrica = rs.getInt("id");
                    String nome = rs.getString("nome_unidade");
                    String cnpj = rs.getString("cnpj");
                    boolean status = rs.getBoolean("status");
                    String email = rs.getString("email_corporativo");
                    String nomeEmpresa = rs.getString("nome_industria");
                    String ramo = rs.getString("ramo");
                    String endereco = rs.getString("endereco");
                    String plano = rs.getString("plano");

                    fabricas.add(new FabricaDTO(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco, plano));
                }
            }
        }

        conn.commit();
        return fabricas;
    }

    // === UPDATE ===
    public void atualizar(Fabrica original, Fabrica alteracoes) throws SQLException {
        int id = alteracoes.getId();
        int idPlano = alteracoes.getIdPlano();
        String nomeUnidade = alteracoes.getNomeUnidade();
        String cnpj = alteracoes.getCnpj();
        Boolean status = alteracoes.getStatus();
        String email = alteracoes.getEmailCorporativo();
        String nomeIndustria = alteracoes.getNomeIndustria();
        String ramo = alteracoes.getRamo();

        StringBuilder sql = new StringBuilder("UPDATE fabrica SET ");
        List<Object> valores = new ArrayList<>();

        // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
        if (!Objects.equals(nomeUnidade, original.getNomeUnidade())) {
            sql.append("nome_unidade = ?, ");
            valores.add(nomeUnidade);
        }

        if (!Objects.equals(cnpj, original.getCnpj())) {
            sql.append("cnpj_unidade = ?, ");
            valores.add(cnpj);
        }

        if (original.getStatus() != status) {
            sql.append("status = ?, ");
            valores.add(status);
        }

        if (!Objects.equals(email, original.getEmailCorporativo())) {
            sql.append("email_corporativo = ?, ");
            valores.add(email);
        }

        if (!Objects.equals(nomeIndustria, original.getNomeIndustria())) {
            sql.append("nome_industria = ?, ");
            valores.add(nomeIndustria);
        }

        if (!Objects.equals(ramo, original.getRamo())) {
            sql.append("ramo = ?, ");
            valores.add(ramo);
        }

        if (original.getIdPlano() != idPlano) {
            sql.append("fk_plano = ?, ");
            valores.add(idPlano);
        }

        // Retorno vazio se nada foi alterado
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

        String sql = "DELETE FROM fabrica WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Resgata a fábrica pelo CNPJ
    public Fabrica pesquisarPorCnpj(String cnpj) throws SQLException {

        String sql = "SELECT id, fk_plano, email_corporativo, nome_unidade, status, nome_industria, ramo FROM fabrica WHERE cnpj_unidade = ?";
        Fabrica f;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cnpj);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar a fábrica retorna null
                if (!rs.next()) {
                    return null;
                }

                int idFabrica = rs.getInt("id");
                String nome = rs.getString("nome_unidade");
                boolean status = rs.getBoolean("status");
                String email = rs.getString("email_corporativo");
                String nomeEmpresa = rs.getString("nome_industria");
                String ramo = rs.getString("ramo");
                int idPlano = rs.getInt("id_plano");

                f = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);
            }
        }

        conn.commit();
        return f;
    }

    // HashMap das fábricas, onde a chave é o ID e o valor o nome da unidade
    public Map<Integer, String> getMapIdNome() throws SQLException {

        String sql = "SELECT id, nome_unidade FROM fabrica";

        Map<Integer, String> map = new HashMap<>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome_unidade");

                map.put(id, nome);
            }
        }

        conn.commit();
        return map;
    }

    // Resgata a fábrica pelo ID
    public Fabrica pesquisarPorId(int id) throws SQLException {

        String sql = "SELECT fk_plano, email_corporativo, nome_unidade, cnpj_unidade, status, nome_industria, ramo FROM fabrica WHERE id = ?";

        Fabrica f;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar a fábrica retorna null
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("nome_unidade");
                String cnpj = rs.getString("cnpj");
                boolean status = rs.getBoolean("status");
                String email = rs.getString("email_corporativo");
                String nomeEmpresa = rs.getString("nome_industria");
                String ramo = rs.getString("ramo");
                int idPlano = rs.getInt("id_plano");

                f = new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);
            }
        }

        conn.commit();
        return f;
    }
}