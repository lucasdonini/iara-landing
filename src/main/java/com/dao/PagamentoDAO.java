package com.dao;

import com.model.Pagamento;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagamentoDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "valor", "Valor Pago",
      "status", "Status",
      "tipo_pagamento", "Tipo de Pagamento",
      "data_vencimento", "Data de Vencimento",
      "data_pagamento", "Data do Pagamento",
      "id_fabrica", "Fábrica"
  );

  // Construtor
  public PagamentoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Converter Valor
  public Object converterValor(String campo, String valor){
      try{
          return switch(campo){
              case "id" -> Integer.parseInt(valor);
              case "valor" -> Double.parseDouble(valor);
              case "status" -> Boolean.parseBoolean(valor);
              case "data_pagamento", "data_vencimento" -> LocalDate.parse(valor);
              case "tipo_pagamento" -> valor;
              default -> new IllegalArgumentException();
          };
      } catch(DateTimeParseException | IllegalArgumentException | NullPointerException e){
          return null;
      }
  }
  // Outros Métodos

  // === CREATE ===
  public void cadastrar(Pagamento pagamento) throws SQLException {
    //Comando SQL
    String sql = """
        INSERT INTO pagamento(valor, status, data_vencimento, data_pagamento, tipo_pagamento, id_fabrica)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Transformando Date em LocalDate
      LocalDate dtPagamento = pagamento.getDataPagamento();
      LocalDate dtVencimento = pagamento.getDataVencimento();

      // Definindo variáveis do comando SQL
      pstmt.setDouble(1, pagamento.getValor());
      pstmt.setBoolean(2, pagamento.getStatus());
      pstmt.setDate(3, (dtVencimento == null ? null : Date.valueOf(dtVencimento)));
      pstmt.setDate(4, (dtPagamento == null ? null : Date.valueOf(dtPagamento)));
      pstmt.setString(5, pagamento.getTipoPagamento());
      pstmt.setInt(6, pagamento.getIdFabrica());

      // Cadastra o pagamento no banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transaçao
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<Pagamento> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    // Lista de pagamentos
    List<Pagamento> pagamentos = new ArrayList<>();

    // Comando SQL
    String sql = "SELECT * FROM pagamento";

    //Verificando o campo do filtro
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

      // Resgata do banco de dados a lista de pagamentos
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          // Variáveis
          int id = rs.getInt("id");
          double valorPago = rs.getDouble("valor");
          boolean status = rs.getBoolean("status");

          Date dtVencimentoDate = rs.getDate("data_vencimento");
          LocalDate dtVencimento = (dtVencimentoDate == null ? null : dtVencimentoDate.toLocalDate());

          Date dtPagtoDate = rs.getDate("data_pagamento");
          LocalDate dtPagto = (dtPagtoDate == null ? null : dtPagtoDate.toLocalDate());

          String tipoPagamento = rs.getString("tipo_pagamento");
          int idFabrica = rs.getInt("id_fabrica");

          // Adicionando instância do DTO na lista de pagamentos
          pagamentos.add(new Pagamento(id, valorPago, status, dtVencimento, dtPagto, tipoPagamento, idFabrica));
        }
      }
    }

    // Retorna a lista de pagamentos
    conn.commit();
    return pagamentos;
  }

  public Pagamento pesquisarPorId(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM pagamento WHERE id = ?";

    // Objeto não instanciado de pagamento
    Pagamento pagamento;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Pesquisa pagamento pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        double valorPago = rs.getDouble("valor");
        boolean status = rs.getBoolean("status");

        Date dtVencimentoDate = rs.getDate("data_vencimento");
        LocalDate dtVencimento = (dtVencimentoDate == null ? null : dtVencimentoDate.toLocalDate());

        Date dtPagtoDate = rs.getDate("data_pagamento");
        LocalDate dtPagto = (dtPagtoDate == null ? null : dtPagtoDate.toLocalDate());

        String tipoPagamento = rs.getString("tipo_pagamento");
        int idFabrica = rs.getInt("id_fabrica");

        // Instância do Model
        pagamento = new Pagamento(id, valorPago, status, dtVencimento, dtPagto, tipoPagamento, idFabrica);
      }
    }

    // Retorna pagamento
    conn.commit();
    return pagamento;
  }

  public Pagamento getCamposAlteraveis(int id) throws SQLException {
    // Instância do Model
    Pagamento p = pesquisarPorId(id);

    // Se for vazio lança exceção
    if (p == null) {
      throw new SQLException("Falha ao recuperar pagamento");
    }

    // Retorna pagamento
    conn.commit();
    return p;
  }

  // === UPDATE ===
  public void atualizar(Pagamento original, Pagamento alterado) throws SQLException {
    // Variáveis
    int id = alterado.getId();
    double valorPago = alterado.getValor();
    boolean status = alterado.getStatus();
    LocalDate dataVencimento = alterado.getDataVencimento();
    LocalDate dataPagamento = alterado.getDataPagamento();
    String tipoPagamento = alterado.getTipoPagamento();
    int idFabrica = alterado.getIdFabrica();

    // Construção do comando SQL dinâmico
    StringBuilder sql = new StringBuilder("UPDATE pagamento SET ");
    List<Object> valores = new ArrayList<>();

    if (original.getValor() != valorPago) {
      sql.append("valor = ?, ");
      valores.add(valorPago);
    }

    if (original.getStatus() != status) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (!Objects.equals(dataVencimento, original.getDataVencimento())) {
      sql.append("data_vencimento = ?, ");
      valores.add(dataVencimento);
    }

    if (!Objects.equals(dataPagamento, original.getDataPagamento())) {
      sql.append("data_pagamento = ?, ");
      valores.add(dataPagamento);
    }

    if (!Objects.equals(tipoPagamento, original.getTipoPagamento())) {
      sql.append("tipo_pagamento = ?, ");
      valores.add(tipoPagamento);
    }

    if (original.getIdFabrica() != idFabrica) {
      sql.append("id_fabrica = ?, ");
      valores.add(idFabrica);
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
      // Definindo variáveis do comando SQL
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Atualiza o pagamento no banco de dados
      pstmt.executeUpdate();

      // Efetuando trasação
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
    String sql = "DELETE FROM pagamento WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variáveis do comando SQL
      pstmt.setInt(1, id);

      // Deleta o pagamento do banco de dados
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
