package com.dao;

import com.dto.CadastroFabricaDTO;
import com.model.Endereco;
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

  public void cadastrar(CadastroFabricaDTO credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNome();
    String cnpj = credenciais.getCnpj();
    String email = credenciais.getEmail();
    String nomeEmpresa = credenciais.getNomeEmpresa();
    String ramo = credenciais.getRamo();
    int idEndereco = credenciais.getFkEndereco();
    boolean status = true;

    // Preparação do comando
    String sql = """
        INSERT INTO fabrica (nome, cnpj_unidade, email_corporativo, nome_industria, status, ramo, id_endereco)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Preenchimento dos placeholders
      pstmt.setString(1, nome);
      pstmt.setString(2, cnpj);
      pstmt.setString(3, email);
      pstmt.setString(4, nomeEmpresa);
      pstmt.setBoolean(5, status);
      pstmt.setString(6, ramo);
      pstmt.setInt(7, idEndereco);

      // Execução do update
      pstmt.executeUpdate();

      // Commit das alterações
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback das alterações
      conn.rollback();
      throw e;
    }
  }

  public List<Fabrica> listarFabricas() throws SQLException {
    List<Fabrica> fabricas = new ArrayList<>();

    String sql = """
        SELECT
            f.id as "id_fabrica", f.*,
            e.id as "id_endereco", e.*
        FROM fabrica f
        JOIN endereco e ON e.id = f.id_endereco
        ORDER BY f.id
        """;

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        // Informações da fábrica
        int idFabrica = rs.getInt("id_fabrica");
        String nome = rs.getString("nome");
        String cnpj = rs.getString("cnpj_unidade");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");

        // Informações do endereco
        int idEndereco = rs.getInt("id_endereco");
        String cep = rs.getString("cep");
        int numero = rs.getInt("numero");
        String rua = rs.getString("rua");
        String complemento = rs.getString("complemento");

        // Cria e armazena os objetos
        Endereco endereco = new Endereco(idEndereco, cep, numero, rua, complemento);
        Fabrica fabrica = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco);
        fabricas.add(fabrica);
      }

      return fabricas;
    }
  }

  public void atualizar(Fabrica original, Fabrica alteracoes) throws SQLException {
    // Desempacotamento do model
    int id = alteracoes.getId();
    String nome = alteracoes.getNome();
    String cnpj = alteracoes.getCnpj();
    Boolean status = alteracoes.getStatus();
    String email = alteracoes.getEmail();
    String nomeEmpresa = alteracoes.getNomeEmpresa();
    String ramo = alteracoes.getRamo();

    // Contrução do script dinâmico
    StringBuilder sql = new StringBuilder("UPDATE fabrica SET ");
    List<Object> valores = new ArrayList<>();

    if (!original.getNome().equals(nome)) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (!original.getCnpj().equals(cnpj)) {
      sql.append("cnpj_unidade = ?, ");
      valores.add(cnpj);
    }

    if (original.getStatus() != status) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (!original.getEmail().equals(email)) {
      sql.append("email_corporativo = ?, ");
      valores.add(email);
    }

    if (!original.getNomeEmpresa().equals(nomeEmpresa)) {
      sql.append("nome_industria = ?, ");
      valores.add(nomeEmpresa);
    }

    if (!original.getRamo().equals(ramo)) {
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
    sql.append(" WHERE id = ?");
    valores.add(id);

    // Execução do comando
    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      pstmt.executeUpdate();
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback das modificações e propaga a exceção
      conn.rollback();
      throw e;
    }
  }

  public void remover(int id) throws SQLException {
    String sql = "DELETE FROM fabrica WHERE id = ?";

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

  public Fabrica getFabricaById(int id) throws SQLException {
    // Prepara o comando
    String sql = """
         SELECT
            f.id as "id_fabrica", f.*,
            e.id as "id_endereco", e.*
        FROM fabrica f
        JOIN endereco e ON e.id = f.id_endereco
        WHERE f.id = ?
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Informações da fábrica
          String nome = rs.getString("nome");
          String cnpj = rs.getString("cnpj_unidade");
          boolean status = rs.getBoolean("status");
          String email = rs.getString("email_corporativo");
          String nomeEmpresa = rs.getString("nome_industria");
          String ramo = rs.getString("ramo");

          // Informações do endereco
          int idEndereco = rs.getInt("id_endereco");
          String cep = rs.getString("cep");
          int numero = rs.getInt("numero");
          String rua = rs.getString("rua");
          String complemento = rs.getString("complemento");

          // Cria e retorna o objeto
          Endereco endereco = new Endereco(idEndereco, cep, numero, rua, complemento);
          return new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, endereco);

        } else {
          throw new SQLException("Falha ao recuperar o usuário");
        }
      }
    }
  }

  public Fabrica getFabricaByCnpj(String cnpj) throws SQLException {
    // Prepara o comando
    String sql = """
         SELECT
            f.id as "id_fabrica", f.*,
            e.id as "id_endereco", e.*
        FROM fabrica f
        JOIN endereco e ON e.id = f.id_endereco
        WHERE f.cnpj_unidade = ?
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cnpj);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Informações da fábrica
          int idFabrica = rs.getInt("id_fabrica");
          String nome = rs.getString("nome");
          boolean status = rs.getBoolean("status");
          String email = rs.getString("email_corporativo");
          String nomeEmpresa = rs.getString("nome_industria");
          String ramo = rs.getString("ramo");

          // Informações do endereco
          int idEndereco = rs.getInt("id_endereco");
          String cep = rs.getString("cep");
          int numero = rs.getInt("numero");
          String rua = rs.getString("rua");
          String complemento = rs.getString("complemento");

          // Cria e retorna o objeto
          Endereco endereco = new Endereco(idEndereco, cep, numero, rua, complemento);
          return new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco);

        } else {
          throw new SQLException("Falha ao recuperar o usuário");
        }
      }
    }
  }
}