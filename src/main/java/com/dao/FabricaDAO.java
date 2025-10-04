package com.dao;

import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.model.Fabrica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabricaDAO extends DAO {
  //Map
  public static final Map<String, String> camposFiltraveis = Map.of(
      "ID", "id",
      "CNPJ", "cnpj",
      "Nome da Unidade", "nome_unidade",
      "Status", "status",
      "Email Corporativo", "email_corporativo",
      "Nome da Indústria", "nome_industria",
      "Ramo", "ramo",
      "Endereço", "endereco",
      "Plano", "plano"
  );

  public FabricaDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  public int cadastrar(CadastroFabricaDTO credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNome();
    String cnpj = credenciais.getCnpj();
    String email = credenciais.getEmail();
    String nomeEmpresa = credenciais.getNomeEmpresa();
    String ramo = credenciais.getRamo();
    int idPlano = credenciais.getIdPlano();
    boolean status = true;

    // Preparação do comando
    String sql = """
        INSERT INTO fabrica (nome_unidade, cnpj, email_corporativo, nome_industria, status, ramo, id_plano)
        VALUES (?, ?, ?, ?, ?, ?, ?)
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
      pstmt.setInt(7, idPlano);

      // Cadastra a fábrica e recupera o id gerado
      int id;
      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Erro inesperado ao criar fábrica");
        }

        id = rs.getInt("id");
      }

      // Commit das alterações
      conn.commit();

      // Retorna o id gerado
      return id;

    } catch (SQLException e) {

      // Faz o rollback das alterações
      conn.rollback();
      throw e;
    }
  }

  public Object converterValor(String campo, String valor) {
    if (campo == null) {
      return null;
    }

    return switch (campo) {
      case "id" -> Integer.parseInt(valor);
      case "status" -> Boolean.parseBoolean(valor);
      case "cnpj", "nome_unidade", "email_corporativo", "nome_industria", "ramo" -> String.valueOf(valor);
      default -> throw new IllegalArgumentException();
    };
  }

  public List<FabricaDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    List<FabricaDTO> fabricas = new ArrayList<>();

    //Prepara o comando
    String sql = "SELECT * FROM exibicao_fabrica";

    //Verificando o campo do filtro
    if (campoFiltro != null && !campoFiltro.isBlank()) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    //Verificando campo e direcao para ordernar a consulta
    if (campoSequencia != null && !campoSequencia.isBlank()) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //Definindo parâmetro vazio
      if (campoFiltro != null && !campoFiltro.isBlank()) {
        pstmt.setObject(1, valorFiltro);
      }

      //Instanciando um ResultSet
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int idFabrica = rs.getInt("id");
        String nome = rs.getString("nome_unidade");
        String cnpj = rs.getString("cnpj");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");
        String endereco = rs.getString("endereco");
        String plano = rs.getString("plano");

        FabricaDTO f = new FabricaDTO(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco, plano);
        fabricas.add(f);
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

  public Fabrica pesquisarPorId(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM fabrica WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Informações da fábrica
          String nome = rs.getString("nome_unidade");
          String cnpj = rs.getString("cnpj");
          boolean status = rs.getBoolean("status");
          String email = rs.getString("email_corporativo");
          String nomeEmpresa = rs.getString("nome_industria");
          String ramo = rs.getString("ramo");
          int idPlano = rs.getInt("id_plano");

          // Cria e retorna o objeto
          return new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);

        } else {
          throw new SQLException("Falha ao recuperar fábrica");
        }
      }
    }
  }

  public Fabrica pesquisarPorCnpj(String cnpj) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM fabrica WHERE cnpj = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cnpj);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          // Informações da fábrica
          int idFabrica = rs.getInt("id");
          String nome = rs.getString("nome_unidade");
          boolean status = rs.getBoolean("status");
          String email = rs.getString("email_corporativo");
          String nomeEmpresa = rs.getString("nome_industria");
          String ramo = rs.getString("ramo");
          int idPlano = rs.getInt("id_plano");

          return new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);

        } else {
          return null;
        }
      }
    }
  }

  public Map<Integer, String> getMapIdNome() throws SQLException {
    String sql = "SELECT id, nome_unidade FROM fabrica";
    Map<Integer, String> map = new HashMap<>();

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome_unidade");

        map.put(id, nome);
      }
    }

    return map;
  }
}