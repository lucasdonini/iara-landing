package com.dao;

import com.model.Plano;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanoDAO extends DAO {
  //Map
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "nome", "Nome",
      "valor", "Valor",
      "descricao", "Descrição"
  );

  //Construtor
  public PlanoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo Plano
  public void cadastrar(Plano plano) throws SQLException {
    //Comando SQL
    String sql = "INSERT INTO plano(nome, valor, descricao) VALUES (?, ?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setString(1, plano.getNome());
      pstmt.setDouble(2, plano.getValor());
      pstmt.setString(3, plano.getDescricao());
      //Salvando alterações no banco
      pstmt.execute();
      //Confirmando transações
      this.conn.commit();
    } catch (SQLException e) {
      //Cancelando transações
      conn.rollback();
      //Enviando exceção
      throw e;
    }

  }

  public Plano getPlanoById(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM plano WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        return new Plano(
            id,
            rs.getString("nome"),
            rs.getDouble("valor"),
            rs.getString("descricao"));
      }
    }
  }

  public Plano getPlanoByNome(String nome) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM plano WHERE nome = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nome);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        return new Plano(
            rs.getInt("id"),
            nome,
            rs.getDouble("valor"),
            rs.getString("descricao"));
      }
    }
  }

  //Remover plano
  public void remover(int id) throws SQLException {
    // Prepara o comando
    String sql = "DELETE FROM plano WHERE id = ?";

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

  public void atualizar(Plano original, Plano alterado) throws SQLException {
    // Desempacotamento do model alterado
    int id = alterado.getId();
    String nome = alterado.getNome();
    double valor = alterado.getValor();
    String descricao = alterado.getDescricao();

    // Monta o comando de acordo com os campos alterados
    StringBuilder sql = new StringBuilder("UPDATE plano SET ");
    List<Object> valores = new ArrayList<>();

    if (!original.getNome().equals(nome)) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (original.getValor() != valor) {
      sql.append("valor = ?, ");
      valores.add(valor);
    }

    if (!original.getDescricao().equals(descricao)) {
      sql.append("descricao = ?, ");
      valores.add(descricao);
    }

    // Sái do metodo se nada foi alterado
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

  //Converter valor
  public Object converterValor(String campo, String valor) throws DateTimeParseException {
    if (campo == null || campo.isBlank()) {
      return null;
    }

    return switch (campo) {
      case "id" -> Integer.parseInt(valor);
      case "valor" -> Double.parseDouble(valor);
      case "nome", "descricao" -> String.valueOf(valor);
      default -> throw new IllegalArgumentException();
    };
  }

  //Listar planos
  public List<Plano> listarPlanos(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    List<Plano> planos = new ArrayList<>();

    // Prepara o comando
    String sql = "SELECT * FROM plano";

    // Verificando campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    //Verificando campo para ordenar a consulta
    if (campoSequencia != null && camposFiltraveis.containsKey((campoSequencia))) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //Definindo parâmetro vazio
      if (campoFiltro != null && !campoFiltro.isBlank()) {
        pstmt.setObject(1, valorFiltro);
      }

      //Instanciando um ResultSet
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        double valor = rs.getDouble("valor");
        String descricao = rs.getString("descricao");

        planos.add(new Plano(id, nome, valor, descricao));
      }
    }

    return planos;
  }

  //Campos Alteráveis
  public Plano getCamposAlteraveis(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM plano WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          String nome = rs.getString("nome");
          double valor = rs.getDouble("valor");
          String descricao = rs.getString("descricao");

          return new Plano(id, nome, valor, descricao);
        } else {
          throw new SQLException("Erro ao recuperar as informações do super adm");
        }
      }
    }
  }

  public Map<Integer, String> getMapIdNome() throws SQLException {
    Map<Integer, String> map = new HashMap<>();

    // Prepara o comando
    String sql = "SELECT id, nome FROM plano";

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        map.put(id, nome);
      }
    }

    return map;
  }
}
