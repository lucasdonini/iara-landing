package com.dao;

import com.model.Endereco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO extends DAO {

  public EnderecoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // === CREATE ===
  public void cadastrar(Endereco credenciais) throws SQLException {

    String complemento = credenciais.getComplemento();
    String cep = credenciais.getCep();
    String rua = credenciais.getRua();
    int idFabrica = credenciais.getIdFabrica();
    int numero = credenciais.getNumero();

    // Define o complemento como null (se estiver vazio)
    complemento = (complemento == null || complemento.isBlank() ? null : complemento);

    String sql = """
        INSERT INTO endereco (cep, numero, rua, complemento, fk_fabrica)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cep);
      pstmt.setInt(2, numero);
      pstmt.setString(3, rua);
      pstmt.setString(4, complemento);
      pstmt.setInt(5, idFabrica);

      pstmt.executeUpdate();

      conn.commit();

    } catch (SQLException e) {
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public Endereco pesquisarPorIdFabrica(int idFabrica) throws SQLException {

    String sql = "SELECT complemento, cep, rua, numero, id FROM endereco WHERE fk_fabrica = ?";

    Endereco e;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, idFabrica);

      try (ResultSet rs = pstmt.executeQuery()) {

        // Se não encontrar o endereço lança exceção
        if (!rs.next()) {
          throw new SQLException("Falha ao recuperar endereço");
        }

        String complemento = rs.getString("complemento");
        String cep = rs.getString("cep");
        String rua = rs.getString("rua");
        int numero = rs.getInt("numero");
        int id = rs.getInt("id");

        e = new Endereco(id, cep, numero, rua, complemento, idFabrica);
      }
    }

    conn.commit();
    return e;
  }

  // === UPDATE ===
  public void atualizar(Endereco original, Endereco alterado) throws SQLException {

    String complemento = alterado.getComplemento();
    String rua = alterado.getRua();
    String cep = alterado.getCep();
    int numero = alterado.getNumero();
    int id = original.getId();

    StringBuilder sql = new StringBuilder("UPDATE endereco SET ");
    List<Object> valores = new ArrayList<>();

    // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
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

    String sql = "DELETE FROM endereco WHERE id = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      pstmt.executeUpdate();

      conn.commit();

    } catch (SQLException e) {
      conn.rollback();
      throw e;
    }
  }
}
