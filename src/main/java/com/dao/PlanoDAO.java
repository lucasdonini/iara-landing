package com.dao;

import com.model.Plano;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeParseException;
import java.util.*;

public class PlanoDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "nome", "Nome",
      "valor", "Valor",
      "descricao", "Descrição"
  );

  // Construtor
  public PlanoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Outros Métodos

  // === CREATE ===
  public void cadastrar(Plano plano) throws SQLException {
    //Comando SQL
    String sql = "INSERT INTO plano(nome, valor, descricao) VALUES (?, ?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setString(1, plano.getNome());
      pstmt.setDouble(2, plano.getValor());
      pstmt.setString(3, plano.getDescricao());

      // Cadastra o plano no banco de dados
      pstmt.execute();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<Plano> listar(String campoFiltro, String valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    // Lista de planos
    List<Plano> planos = new ArrayList<>();

    // Comando SQL
    String sql = "SELECT * FROM plano";

    // Verificando campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s::varchar = ?".formatted(campoFiltro);
    }

    // Verificando campo e direcao da ordenação
    if (campoSequencia != null && camposFiltraveis.containsKey((campoSequencia))) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variáveis do comando SQL
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setString(1, valorFiltro);
      }

      // Resgata do banco de dados a lista de planos
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          // Variáveis
          int id = rs.getInt("id");
          String nome = rs.getString("nome");
          double valor = rs.getDouble("valor");
          String descricao = rs.getString("descricao");

          // Adicionando instância do DTO na lista de planos
          planos.add(new Plano(id, nome, valor, descricao));
        }
      }
    }

    // Retorna a lista de planos
    return planos;
  }

  public Plano pesquisarPorId(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM plano WHERE id = ?";

    // Objeto não instanciado de plano
    Plano plano;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Pesquisa plano pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        String nome = rs.getString("nome");
        double valor = rs.getDouble("valor");
        String descricao = rs.getString("descricao");

        // Instância do Model
        plano = new Plano(id, nome, valor, descricao);
      }
    }

    // Retorna plano
    return plano;
  }

  public Plano pesquisarPorNome(String nome) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM plano WHERE nome = ?";

    // Objeto não instanciado de plano
    Plano plano;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setString(1, nome);

      // Pesquisa plano pelo nome
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        int id = rs.getInt("id");
        double valor = rs.getDouble("valor");
        String descricao = rs.getString("descricao");

        // Instância do Model
        plano = new Plano(id, nome, valor, descricao);
      }
    }

    // Retorna plano
    return plano;
  }

  public Plano getCamposAlteraveis(int id) throws SQLException {
    // Instância do Model
    Plano plano = pesquisarPorId(id);

    // Se for vazio lança exceção
    if (plano == null) {
      throw new SQLException("Falha ao recuperar plano");
    }

    // Retorna plano
    return plano;
  }

  public Map<Integer, String> getMapIdNome() throws SQLException {
    // Instância do HashMap
    Map<Integer, String> map = new HashMap<>();

    // Comando SQL
    String sql = "SELECT id, nome FROM plano";

    // Lista dos IDs e nomes dos planos
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      // Variáveis
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");

        // Adicionando chave e valor no map
        map.put(id, nome);
      }
    }

    // Retorna map
    return map;
  }

  // === UPDATE ===
  public void atualizar(Plano original, Plano alterado) throws SQLException {
    // Variáveis
    int id = alterado.getId();
    String nome = alterado.getNome();
    double valor = alterado.getValor();
    String descricao = alterado.getDescricao();

    // Construção do comando SQL dinâmico
    StringBuilder sql = new StringBuilder("UPDATE plano SET ");
    List<Object> valores = new ArrayList<>();

    if (!Objects.equals(nome, original.getNome())) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (original.getValor() != valor) {
      sql.append("valor = ?, ");
      valores.add(valor);
    }

    if (!Objects.equals(descricao, original.getDescricao())) {
      sql.append("descricao = ?, ");
      valores.add(descricao);
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
      // Adiciona a cláusula WHERE
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Atualiza o plano no banco de dados
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
    String sql = "DELETE FROM plano WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Deleta o plano do banco de dados
      pstmt.setInt(1, id);

      // Efetuando transação
      pstmt.executeUpdate();
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }
}
