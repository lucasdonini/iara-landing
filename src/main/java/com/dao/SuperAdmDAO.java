package com.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dto.CadastroSuperAdmDTO;
import com.dto.SuperAdmDTO;

public class SuperAdmDAO extends DAO {
  public SuperAdmDAO() throws SQLException {
    super();
  }

  public SuperAdmDTO cadastrar(CadastroSuperAdmDTO credenciais) throws SQLException {
    // Armazena as informações do cadastro em variáveis
    String nome = credenciais.getNome();
    String email = credenciais.getEmail();
    String cargo = credenciais.getCargo();
    int id;

    // Prepara o comando
    String sql = """
        INSERT INTO super_adm (nome, email, cargo)
        VALUES (?, ?, ?)
        RETURNING id
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, cargo);

      // Executa o update
      pstmt.executeUpdate();

      // Recupera o id gerado
      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt("id");
        } else {
          throw new SQLException("Falha ao recuperar campos autogerados");
        }
      }

      // Commita as alterações no banco
      conn.commit();

      return new SuperAdmDTO(id, nome, cargo, email);

    } catch (SQLException e) {

      // Faz o rollback das alterações
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
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

    } catch (SQLException e) {

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void remover(SuperAdmDTO superAdm) throws SQLException {
    // Recupera o id
    int id = superAdm.getId();

    // Prepara o comando
    String sql = "DELETE FROM super_adm WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setInt(1, id);

      // Executa o comando e commita as mudanças
      pstmt.executeUpdate();
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback das modificações
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void atualizar(SuperAdmDTO alteracoes) throws SQLException {
    // Armazena os atributos do DTO em variáveis por praticidade
    int id = alteracoes.getId();
    String nome = alteracoes.getNome();
    String cargo = alteracoes.getCargo();
    String email = alteracoes.getEmail();

    // Monta o comando de acordo com os campos alterados
    StringBuilder sql = new StringBuilder("UPDATE super_adm SET ");
    List<Object> valores = new ArrayList<>();

    if (nome != null) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (cargo != null) {
      sql.append("cargo = ?, ");
      valores.add(cargo);
    }

    if (email != null) {
      sql.append("email = ?, ");
      valores.add(email);
    }

    // Sái do método se nada foi alterado
    if (valores.isEmpty()) {
      return;
    }

    // Remove o último espaço e a última vírgula
    sql.setLength(sql.length() - 2);

    // Adiciona o WHERE
    sql.append("WHERE id = ?");
    valores.add(id);

    // Prepara, preenche e executa o comando
    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Commita as alterações
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback das alterações
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

}