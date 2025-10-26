package com.dao;

import com.model.Plano;
import org.postgresql.util.PGInterval;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class PlanoDAO extends DAO {
    // Constante dos campos utilizados para ordenação e filtragem da listagem dos dados
    public static final Map<String, String> camposFiltraveis = Map.of(
            "id", "Id",
            "nome", "Nome",
            "valor", "Valor",
            "descricao", "Descrição",
            "duracao", "Duração do Plano"
    );

    public PlanoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Método que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id" -> Integer.parseInt(valor);
                case "valor" -> Double.parseDouble(valor);
                case "descricao", "nome" -> valor;
                case "duracao" -> new PGInterval(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException | SQLException e) {
            return null;
        }
    }

    // === CREATE ===
    public void cadastrar(Plano plano) throws SQLException {

        String sql = "INSERT INTO plano(nome, valor, descricao, duracao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plano.getNome());
            pstmt.setDouble(2, plano.getValor());
            pstmt.setString(3, plano.getDescricao());
            pstmt.setObject(4, plano.getDuracao());

            pstmt.execute();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === READ ===
    public List<Plano> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // Variável de apoio para verificar se a listagem será filtrada
        boolean temFiltro = true;

        List<Plano> planos = new ArrayList<>();

        String sql = "SELECT id, nome, valor, descricao, duracao FROM plano";

        // Verificando campo de filtragem
        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        // Verificando campo e direcao da ordenação
        if (campoSequencia != null && camposFiltraveis.containsKey((campoSequencia))) {
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
                    String nome = rs.getString("nome");
                    double valor = rs.getDouble("valor");
                    String descricao = rs.getString("descricao");

                    String duracaoS = String.valueOf(rs.getObject("duracao"));
                    PGInterval duracao = new PGInterval(duracaoS);

                    planos.add(new Plano(id, nome, valor, descricao, duracao));
                }
            }
        }

        conn.commit();
        return planos;
    }

    // === UPDATE ===
    public void atualizar(Plano original, Plano alterado) throws SQLException {

        int id = alterado.getId();
        String nome = alterado.getNome();
        double valor = alterado.getValor();
        String descricao = alterado.getDescricao();
        PGInterval duracao = alterado.getDuracao();


        StringBuilder sql = new StringBuilder("UPDATE plano SET ");
        List<Object> valores = new ArrayList<>();

        // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
        if (!Objects.equals(nome, original.getNome())) {
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (original.getValor() != valor) {
            sql.append("valor = ?, ");
            valores.add(valor);
        }

        if (!Objects.equals(descricao, original.getDescricao())) {
            sql.append("descricao = ?, ");
            valores.add(descricao);
        }

        if (!Objects.equals(duracao, original.getDuracao())) {
            sql.append("duracao = ?, ");
            valores.add(duracao);
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

        String sql = "DELETE FROM plano WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Resgata o plano pelo ID
    public Plano pesquisarPorId(int id) throws SQLException {

        String sql = "SELECT nome, valor, descricao, duracao FROM plano WHERE id = ?";

        Plano plano;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o plano retorna null
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("nome");
                double valor = rs.getDouble("valor");
                String descricao = rs.getString("descricao");

                String duracaoS = String.valueOf(rs.getObject("duracao"));
                PGInterval duracao = new PGInterval(duracaoS);

                plano = new Plano(id, nome, valor, descricao, duracao);
            }
        }

        conn.commit();
        return plano;
    }

    // Resgata o plano pelo nome
    public Plano pesquisarPorNome(String nome) throws SQLException {

        String sql = "SELECT id, valor, descricao, duracao FROM plano WHERE nome = ?";

        Plano plano;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o plano retorna null
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                double valor = rs.getDouble("valor");
                String descricao = rs.getString("descricao");

                String duracaoS = String.valueOf(rs.getObject("duracao"));
                PGInterval duracao = new PGInterval(duracaoS);

                plano = new Plano(id, nome, valor, descricao, duracao);
            }
        }

        conn.commit();
        return plano;
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    public Plano getCamposAlteraveis(int id) throws SQLException {

        Plano plano = pesquisarPorId(id);

        // Se não encontrar o plano lança exceção
        if (plano == null) {
            throw new SQLException("Falha ao recuperar plano");
        }

        conn.commit();
        return plano;
    }

    // HashMap dos planos, onde a chave é o ID e o valor o nome
    public Map<Integer, String> getMapIdNome() throws SQLException {

        Map<Integer, String> map = new HashMap<>();

        String sql = "SELECT id, nome FROM plano";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                map.put(id, nome);
            }
        }

        conn.commit();
        return map;
    }
}
