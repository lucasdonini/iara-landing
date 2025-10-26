package com.dao;

import com.dto.SuperAdmDTO;
import com.model.SuperAdm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SuperAdmDAO extends DAO {
    // Constante dos campos utilizados para ordenação e filtragem da listagem dos dados
    public static final Map<String, String> camposFiltraveis = Map.of(
            "id", "Id",
            "nome", "Nome",
            "cargo", "Cargo",
            "email", "Email"
    );

    public SuperAdmDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Método que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id" -> Integer.parseInt(valor);
                case "nome", "cargo", "email" -> valor;
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // === CREATE ===
    public void cadastrar(SuperAdm credenciais) throws SQLException {

        String nome = credenciais.getNome();
        String email = credenciais.getEmail();
        String cargo = credenciais.getCargo();
        String senha = credenciais.getSenha();

        String sql = "INSERT INTO super_adm (nome, email, cargo, senha) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, cargo);
            pstmt.setString(4, senha);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === READ ===
    public List<SuperAdmDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // Variável de apoio para verificar se a listagem será filtrada
        boolean temFiltro = true;

        List<SuperAdmDTO> superAdms = new ArrayList<>();

        String sql = "SELECT id, nome, cargo, email FROM super_adm";

        // Verificando campo de filtragem
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
                    String nome = rs.getString("nome");
                    String cargo = rs.getString("cargo");
                    String email = rs.getString("email");

                    superAdms.add(new SuperAdmDTO(id, nome, cargo, email));
                }
            }
        }

        conn.commit();
        return superAdms;
    }

    // === UPDATE ===
    public void atualizar(SuperAdm original, SuperAdm alterado) throws SQLException {

        int id = alterado.getId();
        String nome = alterado.getNome();
        String cargo = alterado.getCargo();
        String email = alterado.getEmail();
        String senha = alterado.getSenha();

        StringBuilder sql = new StringBuilder("UPDATE super_adm SET ");
        List<Object> valores = new ArrayList<>();

        // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
        if (!Objects.equals(nome, original.getNome())) {
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(cargo, original.getCargo())) {
            sql.append("cargo = ?, ");
            valores.add(cargo);
        }

        if (!Objects.equals(email, original.getEmail())) {
            sql.append("email = ?, ");
            valores.add(email);
        }

        if (senha != null && !senha.equals(original.getSenha()) && !senha.isBlank()) {
            sql.append("senha = ?, ");
            valores.add(senha);
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

        String sql = "DELETE FROM super_adm WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Resgata o super_adm pelo ID
    public SuperAdmDTO pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT nome, cargo, email FROM super_adm WHERE id = ?";

        SuperAdmDTO superAdm;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar a fábrica retorna null
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("nome");
                String cargo = rs.getString("cargo");
                String email = rs.getString("email");

                superAdm = new SuperAdmDTO(id, nome, cargo, email);
            }
        }

        conn.commit();
        return superAdm;
    }

    // Resgata o super_adm pelo email
    public SuperAdmDTO pesquisarPorEmail(String email) throws SQLException {

        String sql = "SELECT id, nome, cargo FROM super_adm WHERE email = ?";

        SuperAdmDTO superAdm;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar a fábrica retorna null
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cargo = rs.getString("cargo");

                superAdm = new SuperAdmDTO(id, nome, cargo, email);
            }
        }

        conn.commit();
        return superAdm;
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    public SuperAdm getCamposAlteraveis(int id) throws SQLException {

        String sql = "SELECT * FROM super_adm WHERE id = ?";

        SuperAdm superAdm;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);


            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar a fábrica lança exceção
                if (!rs.next()) {
                    throw new SQLException("Erro ao recuperar as informações do super adm");
                }

                String nome = rs.getString("nome");
                String cargo = rs.getString("cargo");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                superAdm = new SuperAdm(id, nome, cargo, email, senha);
            }
        }

        conn.commit();
        return superAdm;
    }
}