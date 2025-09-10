package com.dao;

import com.dto.CadastroFabricaDTO;
import com.model.Fabrica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FabricaDAO extends DAO {
  public FabricaDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  public Fabrica cadastrar(CadastroFabricaDTO credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNome();
    String cnpj = credenciais.getCnpj();
    String email = credenciais.getEmail();
    String nomeEmpresa = credenciais.getNomeEmpresa();
    String ramo = credenciais.getRamo();
    boolean status = true;
    int id;

    // Preparação do comando
    String sql = """
        INSERT INTO fabrica (nome, cnpj_unidade, email_corporativo, nome_industria, status, ramo)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING id
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Preenchimento dos placeholders
      pstmt.setString(1, nome);
      pstmt.setString(2, cnpj);
      pstmt.setString(3, email);
      pstmt.setString(4, nomeEmpresa);
      pstmt.setBoolean(5, status);
      pstmt.setString(6, ramo);

      // Execução do update
      pstmt.executeUpdate();

      // Recuperação e armazenamento do id gerado
      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt("id");
        } else {
          throw new SQLException("Falha ao recuperar campos autogerados");
        }
      }

      // Commit das alterações e retorno
      conn.commit();
      return new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, null);

    } catch (SQLException e) {

      // Faz o rollback das alterações
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public List<Fabrica> listarFabricas() throws SQLException {
    List<Fabrica> fabricas = new ArrayList<>();

    String sql = "SELECT * FROM fabrica";

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cnpj = rs.getString("cnpj_unidade");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");

        fabricas.add(new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, null));
      }

      return fabricas;

    } catch (SQLException e) {

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void atualizar(Fabrica alteracoes) throws SQLException {
    // Variáveis
    int id = alteracoes.getId();
    String nome = alteracoes.getNome();
    String cnpj = alteracoes.getCnpj();
    Boolean status = alteracoes.getStatus();
    String email = alteracoes.getEmail();
    String nomeEmpresa = alteracoes.getNomeEmpresa();
    String ramo = alteracoes.getRamo();

    // Contrução do script dinâmico
    StringBuilder sql = new StringBuilder("UPDATE fabrica SET");
    List<Object> valores = new ArrayList<>();

    if (nome != null) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (cnpj != null) {
      sql.append("cnpj_unidade = ?, ");
      valores.add(cnpj);
    }

    if (status != null) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (email != null) {
      sql.append("email_corporativo = ?, ");
      valores.add(email);
    }

    if (nomeEmpresa != null) {
      sql.append("nome_industria = ?, ");
      valores.add(nomeEmpresa);
    }

    if (ramo != null) {
      sql.append("ramo = ?, ");
      valores.add(ramo);
    }

    // Retorno se nada foi alterado
    if (valores.isEmpty()) {
      return;
    }

    // Remoção do último ", "
    sql.setLength(sql.length() - 2);

    // Adiciona a cláusula WHERE
    sql.append("WHERE id = ?");
    valores.add(id);

    // Execução do comando
    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

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

  public void remover(Fabrica fabrica) throws SQLException {
    String sql = "DELETE FROM fabrica WHERE id = ?";
    int id = fabrica.getId();

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
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
}