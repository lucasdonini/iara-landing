package com.dao;

import com.model.Endereco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO extends DAO {
  // Construtor
  public EnderecoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Outros Métodos
  // === CREATE ===
  public void cadastrar(Endereco credenciais) throws SQLException {
    // Variáveis
    String complemento = credenciais.getComplemento();
    String cep = credenciais.getCep();
    String rua = credenciais.getRua();
    int idFabrica = credenciais.getIdFabrica();
    int numero = credenciais.getNumero();

    // Define o complemento como null (se estiver vazio)
    complemento = (complemento == null || complemento.isBlank() ? null : complemento);

    //Comando SQL
    String sql = """
        INSERT INTO endereco (cep, numero, rua, complemento, id_fabrica)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      // Definindo variáveis do comando SQL
      pstmt.setString(1, cep);
      pstmt.setInt(2, numero);
      pstmt.setString(3, rua);
      pstmt.setString(4, complemento);
      pstmt.setInt(5, idFabrica);

      //Cadastra endereço no banco de dados
      pstmt.executeUpdate();

      //Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      //Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public Endereco pesquisarPorIdFabrica(int idFabrica) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM endereco WHERE id_fabrica = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, idFabrica);

      // Pesquisa endereço pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Variáveis
          String complemento = rs.getString("complemento");
          String cep = rs.getString("cep");
          String rua = rs.getString("rua");
          int numero = rs.getInt("numero");
          int id = rs.getInt("id");

          // Instância e retorno do Model
          return new Endereco(id, cep, numero, rua, complemento, idFabrica);

        } else {
          throw new SQLException("Falha ao recuperar endereço");
        }
      }
    }
  }

  // === UPDATE ===
  public void atualizar(Endereco original, Endereco alterado) throws SQLException {
    // Variáveis
    String complemento = alterado.getComplemento();
    String rua = alterado.getRua();
    String cep = alterado.getCep();
    int numero = alterado.getNumero();
    int id = original.getId();

    // Construção do comando SQL dinâmico
    StringBuilder sql = new StringBuilder("UPDATE endereco SET ");
    List<Object> valores = new ArrayList<>();

    if (!original.getRua().equals(rua) && !rua.isBlank()) {
      sql.append("rua = ?, ");
      valores.add(rua);
    }

    if (!original.getCep().equals(cep) && !cep.isBlank()) {
      sql.append("cep = ?, ");
      valores.add(cep);
    }

    if (complemento != null && !complemento.equals(original.getComplemento()) && !complemento.isBlank()) {
      sql.append("complemento = ?, ");
      valores.add(complemento);
    }

    if (original.getNumero() != numero) {
      sql.append("numero = ?, ");
      valores.add(numero);
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
      // Definindo variáveis do comando SQL
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Atualiza endereço no banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === DELETE ===
  public void remover(int id) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM endereco WHERE id = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
      // Definindo variáveis do comando SQL
      pstmt.setInt(1, id);

      // Deleta o endereço do banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }
}
