package com.dao;

import com.dto.CadastroSuperAdmDTO;
import com.dto.SuperAdmDTO;
import com.model.SuperAdm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SuperAdmDAO extends DAO {
  public SuperAdmDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  public void cadastrar(CadastroSuperAdmDTO credenciais) throws SQLException {
    // Armazena as informações do cadastro em variáveis
    String nome = credenciais.getNome();
    String email = credenciais.getEmail();
    String cargo = credenciais.getCargo();
    String senha = credenciais.getSenha();

    // Prepara o comando
    String sql = """
        INSERT INTO super_adm (nome, email, cargo, senha)
        VALUES (?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, cargo);
      pstmt.setString(4, senha);

      // Executa o update
      pstmt.executeUpdate();

      // Commita as alterações no banco
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback das alterações e propaga a exceção
      conn.rollback();
      throw e;
    }
  }

  public SuperAdmDTO getSuperAdmById(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM super_adm WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        return new SuperAdmDTO(
            id,
            rs.getString("nome"),
            rs.getString("cargo"),
            rs.getString("email"));
      }
    }
  }

  public void remover(int id) throws SQLException {
    // Prepara o comando
    String sql = "DELETE FROM super_adm WHERE id = ?";

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

  public void atualizar(SuperAdm original, SuperAdm alterado) throws SQLException {
    // Desempacotamento do model alterado
    int id = alterado.getId();
    String nome = alterado.getNome();
    String cargo = alterado.getCargo();
    String email = alterado.getEmail();
    String senha = alterado.getSenha();

    // Monta o comando de acordo com os campos alterados
    StringBuilder sql = new StringBuilder("UPDATE super_adm SET ");
    List<Object> valores = new ArrayList<>();

    if (!original.getNome().equals(nome)) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (!original.getCargo().equals(cargo)) {
      sql.append("cargo = ?, ");
      valores.add(cargo);
    }

    if (!original.getEmail().equals(email)) {
      sql.append("email = ?, ");
      valores.add(email);
    }

    if (!original.getSenha().equals(senha) && !senha.isBlank()) {
      sql.append("senha = ?, ");
      valores.add(senha);
    }

    // Sái do método se nada foi alterado
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

  public List<SuperAdmDTO> listarSuperAdms() throws SQLException {
    List<SuperAdmDTO> superAdms = new ArrayList<>();

    // Prepara o comando
    String sql = "SELECT * FROM super_adm ORDER BY id";

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cargo = rs.getString("cargo");
        String email = rs.getString("email");

        superAdms.add(new SuperAdmDTO(id, nome, cargo, email));
      }
    }

    return superAdms;
  }

  public SuperAdm getCamposAlteraveis(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM super_adm WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          String nome = rs.getString("nome");
          String cargo = rs.getString("cargo");
          String email = rs.getString("email");
          String senha = rs.getString("senha");

          return new SuperAdm(id, nome, cargo, email, senha);
        } else {
          throw new SQLException("Erro ao recuperar as informações do super adm");
        }
      }
    }
  }
}