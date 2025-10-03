package com.dao;

import com.model.Endereco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EnderecoDAO extends DAO {
  //Map
  public static final Map<String, String> camposFiltraveis = Map.of(
      "ID", "id",
      "CEP", "cep",
      "Número", "numero",
      "Rua", "rua",
      "Complemento", "complemento"
  );

  //Construtor
  public EnderecoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo endereço e retornar o id gerado
  public void cadastrar(Endereco credenciais) throws SQLException {
    // Desempacotamento do objeto Endereco
    String cep = credenciais.getCep();
    int numero = credenciais.getNumero();
    String rua = credenciais.getRua();
    String complemento = credenciais.getComplemento();
    int idFabrica = credenciais.getIdFabrica();

    // Insere null se o complemento estiver vazio
    complemento = complemento.isBlank() ? null : complemento;

    //Comando SQL
    String sql = """
        INSERT INTO endereco (cep, numero, rua, complemento, id_fabrica)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do comando SQL
      pstmt.setString(1, cep);
      pstmt.setInt(2, numero);
      pstmt.setString(3, rua);
      pstmt.setString(4, complemento);
      pstmt.setInt(5, idFabrica);

      pstmt.executeUpdate();

      //Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      //Cancelando transação
      conn.rollback();

      //Lançando exceção
      throw e;
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

  public Object converterValor(String campo, String valor) {
    return switch (campo) {
      case "id", "numero" -> Integer.parseInt(valor);
      case "cep", "rua", "complemento" -> String.valueOf(valor);
      default -> throw new IllegalArgumentException();
    };
  }

  public void atualizar(Endereco original, Endereco alterado) throws SQLException {
    // Desempacotamento do objeto atualizado
    int id = original.getId();
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

  public Endereco pesquisarPorIdFabrica(int idFabrica) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM endereco WHERE id_fabrica = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, idFabrica);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Informações do endereço
          int id = rs.getInt("id");
          String cep = rs.getString("cep");
          int numero = rs.getInt("numero");
          String rua = rs.getString("rua");
          String complemento = rs.getString("complemento");


          // Cria e retorna o objeto
          return new Endereco(id, cep, numero, rua, complemento, idFabrica);

        } else {
          throw new SQLException("Falha ao recuperar endereço");
        }
      }
    }
  }
}
