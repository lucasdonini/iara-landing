package com.dao;

import com.dto.CadastroFabricaDTO;
import com.dto.FabricaDTO;
import com.model.Fabrica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

  // Converter Valor
  public Object converterValor(String campo, String valor){
      return switch(campo){
          case "id", "id_plano" -> Integer.parseInt(valor);
          case "status" -> Boolean.parseBoolean(valor);
          case "email_corporativo", "nome_unidade", "nome_industria", "cnpj", "ramo" -> String.valueOf(valor);
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

    // Comando SQL
    String sql = """
        INSERT INTO fabrica (nome_unidade, cnpj, email_corporativo, nome_industria, status, ramo, id_plano)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        RETURNING id
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variáveis do comando SQL
      pstmt.setString(1, nome);
      pstmt.setString(2, cnpj);
      pstmt.setString(3, email);
      pstmt.setString(4, nomeEmpresa);
      pstmt.setBoolean(5, status);
      pstmt.setString(6, ramo);
      pstmt.setInt(7, idPlano);

      // Cadastra a fábrica no banco de dados e cria variável com ID gerado no cadastro
      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Erro inesperado ao criar fábrica");
        }

        id = rs.getInt("id");
      }

      // Efetuando transação
      conn.commit();

      // Retorna o id gerado no cadastro
      return id;

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<FabricaDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    // Lista de fábricas
    List<FabricaDTO> fabricas = new ArrayList<>();

    // Comando SQL
    String sql = "SELECT * FROM exibicao_fabrica";

    // Verificando o campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    // Verificando campo e direcao da ordenação
    if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setObject(1, valorFiltro);
      }

      // Resgata do banco de dados a lista de fábricas
      try (ResultSet rs = pstmt.executeQuery()) {
        // Variáveis
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

          // Adicionando instância do DTO dentro da lista de fábricas
          fabricas.add(new FabricaDTO(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco, plano));
        }
      }
    }

    // Retorna a lista de fábricas
    return fabricas;
  }

  public Fabrica pesquisarPorCnpj(String cnpj) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM fabrica WHERE cnpj = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setString(1, cnpj);

      // Pesquisa fábrica pelo CNPJ
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        int idFabrica = rs.getInt("id");
        String nome = rs.getString("nome_unidade");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");
        int idPlano = rs.getInt("id_plano");

        // Instância e retorno do Model
        return new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);
      }
    }
  }

  public Map<Integer, String> getMapIdNome() throws SQLException {
    // Comando SQL
    String sql = "SELECT id, nome_unidade FROM fabrica";

    // Instância do HashMap
    Map<Integer, String> map = new HashMap<>();

    // Lista dos IDs e nomes das unidades das fábricas
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      // Variáveis
      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome_unidade");

        // Adicionando chave e valor no map
        map.put(id, nome);
      }
    }

    // Retorna o map
    return map;
  }

  public Fabrica pesquisarPorId(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM fabrica WHERE id = ?";

    // Definindo variável do comando SQL
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      // Pesquisa fábrica pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        String nome = rs.getString("nome_unidade");
        String cnpj = rs.getString("cnpj");
        boolean status = rs.getBoolean("status");
        String email = rs.getString("email_corporativo");
        String nomeEmpresa = rs.getString("nome_industria");
        String ramo = rs.getString("ramo");
        int idPlano = rs.getInt("id_plano");

        // Instância e retorno do Model
        return new Fabrica(id, nome, cnpj, status, email, nomeEmpresa, ramo, idPlano);
      }
    }
  }

  // === UPDATE ===
  public void atualizar(Fabrica original, Fabrica alteracoes) throws SQLException {
    // Variáveis
    int id = alteracoes.getId();
    int idPlano = alteracoes.getIdPlano();
    String nomeUnidade = alteracoes.getNomeUnidade();
    String cnpj = alteracoes.getCnpj();
    Boolean status = alteracoes.getStatus();
    String email = alteracoes.getEmailCorporativo();
    String nomeIndustria = alteracoes.getNomeIndustria();
    String ramo = alteracoes.getRamo();

    // Construção do comando SQL dinâmico
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

      // Atualiza fábrica no banco de dados
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
    String sql = "DELETE FROM fabrica WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Deleta a fábrica do banco de dados
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