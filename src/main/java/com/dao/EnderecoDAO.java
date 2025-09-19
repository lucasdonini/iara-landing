package com.dao;

import com.model.Endereco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EnderecoDAO extends DAO {
  //Construtor
  public EnderecoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo endereço e retornar o id gerado
  public int cadastrar(Endereco credenciais) throws SQLException {
    // Desempacotamento do objeto Endereco
    String cep = credenciais.getCep();
    int numero = credenciais.getNumero();
    String rua = credenciais.getRua();
    String complemento = credenciais.getComplemento();
    int id;

    // Insere null se o complemento estiver vazio
    complemento = complemento.isBlank() ? null : complemento;

    //Comando SQL
    String sql = """
        INSERT INTO endereco (cep, numero, rua, complemento)
        VALUES (?,?,?,?)
        RETURNING id
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do comando SQL
      pstmt.setString(1, cep);
      pstmt.setInt(2, numero);
      pstmt.setString(3, rua);
      pstmt.setString(4, complemento);

      // Recuperando o id gerado ao executar inserção
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          id = rs.getInt("id");
        } else {
          throw new SQLException("Algo deu errado ao inserir o endereço");
        }
      }
      //Efetuando transação
      conn.commit();

      // Retornando o id gerado
      return id;

    } catch (SQLException e) {
      //Cancelando transação
      conn.rollback();
      //Lançando exceção
      throw e;
    }
  }

  // Buscar o id do endereço
  public Integer getIdEndereco(String cep, int numero) throws SQLException {
    // prepara o comando
    String sql = "SELECT id FROM endereco WHERE cep = ? AND numero = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cep);
      pstmt.setInt(2, numero);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");

        } else {
          return null;
        }
      }
    }
  }

  //Remover endereço
  public void remover(int id) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM endereco WHERE id = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
      //Definindo variáveis no código SQL
      pstmt.setInt(1, id);
      //Salvando alterações no banco
      if (pstmt.executeUpdate() != 1) {
        throw new SQLException();
      }
      //Confirmando transações
      conn.commit();
    } catch (SQLException e) {
      //Cancelando transações
      conn.rollback();
      //Lançando excessões
      throw e;
    }
  }

  public void atualizar(Endereco original, Endereco alterado) throws SQLException {
    // Desempacotamento do objeto atualizado
    int id = alterado.getId();
    String rua = alterado.getRua();
    String cep = alterado.getCep();
    String complemento = alterado.getComplemento();
    int numero = alterado.getNumero();

    // Contrução do script dinâmico
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

    if (!complemento.equals(original.getComplemento()) && !complemento.isBlank()) {
      sql.append("complemento = ?, ");
      valores.add(complemento);
    }

    if (original.getNumero() != numero) {
      sql.append("numero = ?, ");
      valores.add(numero);
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
}
