package com.dao;

import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.model.Fabrica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class FabricaDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "cnpj", "CNPJ",
      "nome_unidade", "Nome da Unidade",
      "status", "Status",
      "email_corporativo", "Email Corporativo",
      "nome_industria", "Nome da Indústria",
      "ramo", "Ramo",
      "endereco", "Endereço",
      "plano", "Plano"
  );

  // Construtor
  public FabricaDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Métodos Estáticos
  public static Object converterValor(String campo, String valor) {
    if (campo == null || campo.isBlank()) {
      return null;
    }

    return switch (campo) {
      case "id" -> Integer.parseInt(valor);
      case "status" -> Boolean.parseBoolean(valor);
      case "cnpj", "nome_unidade", "email_corporativo", "nome_industria", "ramo" -> valor;
      default -> throw new IllegalArgumentException();
    };
  }

  // Outros Métodos
  // === CREATE ===
  public int cadastrar(CadastroFabricaDTO credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNomeUnidade();
    String cnpj = credenciais.getCnpj();
    String email = credenciais.getEmailCorporativo();
    String nomeEmpresa = credenciais.getNomeIndustria();
    String ramo = credenciais.getRamo();
    int id, idPlano = credenciais.getIdPlano();
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

  // === READ ===
  public List<FabricaDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    List<FabricaDTO> fabricas = new ArrayList<>();

    //Prepara o comando
    String sql = "SELECT * FROM exibicao_fabrica";

    //Verificando o campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    //Verificando campo e direcao para ordernar a consulta
    if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //Definindo parâmetro vazio
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setObject(1, valorFiltro);
      }

      //Instanciando um ResultSet
      try (ResultSet rs = pstmt.executeQuery()) {
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

          fabricas.add(new FabricaDTO(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco, plano));
        }
      }
    }

    return fabricas;
  }

  public Fabrica pesquisarPorCnpj(String cnpj) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM fabrica WHERE cnpj = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, cnpj);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        // Informações da fábrica
        int idFabrica = rs.getInt("id");
        String nome = rs.getString("nome_unidade");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");
        int idPlano = rs.getInt("id_plano");

        return new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);
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

  public Fabrica pesquisarPorId(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM fabrica WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

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
      }
    }
  }

  // === UPDATE ===
  public void atualizar(Fabrica original, Fabrica alteracoes) throws SQLException {
    // Desempacotamento do model
    int id = alteracoes.getId();
    int idPlano = alteracoes.getIdPlano();
    String nomeUnidade = alteracoes.getNomeUnidade();
    String cnpj = alteracoes.getCnpj();
    Boolean status = alteracoes.getStatus();
    String email = alteracoes.getEmailCorporativo();
    String nomeIndustria = alteracoes.getNomeIndustria();
    String ramo = alteracoes.getRamo();

    // Construção do comando dinâmico
    StringBuilder sql = new StringBuilder("UPDATE fabrica SET ");
    List<Object> valores = new ArrayList<>();

    if (!Objects.equals(nomeUnidade, original.getNomeUnidade())) {
      sql.append("nome_unidade = ?, ");
      valores.add(nomeUnidade);
    }

    if (!Objects.equals(cnpj, original.getCnpj())) {
      sql.append("cnpj = ?, ");
      valores.add(cnpj);
    }

    if (original.getStatus() != status) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (!Objects.equals(email, original.getEmailCorporativo())) {
      sql.append("email_corporativo = ?, ");
      valores.add(email);
    }

    if (!Objects.equals(nomeIndustria, original.getNomeIndustria())) {
      sql.append("nome_industria = ?, ");
      valores.add(nomeIndustria);
    }

    if (!Objects.equals(ramo, original.getRamo())) {
      sql.append("ramo = ?, ");
      valores.add(ramo);
    }

    if (original.getIdPlano() != idPlano) {
      sql.append("id_plano = ?, ");
      valores.add(idPlano);
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

  // === DELETE ===
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
      throw e;
    }
  }
}