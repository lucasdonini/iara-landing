package com.dao;

import com.dto.PagamentoDTO;
import com.model.MetodoPagamento;
import com.model.Pagamento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
      "fk_metodopag", "Fábrica"
  );

  // Construtor
  public PagamentoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Converter Valor
  public Object converterValor(String campo, String valor){
      try{
          return switch(campo){
              case "id", "fk_metodopag" -> Integer.parseInt(valor);
              case "valor" -> Double.parseDouble(valor);
              case "status" -> Boolean.parseBoolean(valor);
              case "data_vencimento" -> LocalDate.parse(valor);
              case "data_pagamento", "data_inicio" -> LocalDateTime.parse(valor);
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
        INSERT INTO pagamento(valor, status, data_vencimento, data_pagamento, data_inicio, fk_metodopag, fk_fabrica, fk_plano)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Transformando Date em LocalDate
      LocalDateTime dtPagamento = pagamento.getDataPagamento();
      LocalDateTime dtInicio = pagamento.getDataInicio();
      LocalDate dtVencimento = pagamento.getDataVencimento();

      // Definindo variáveis do comando SQL
      pstmt.setDouble(1, pagamento.getValor());
      pstmt.setBoolean(2, pagamento.getStatus());
      pstmt.setDate(3, (dtVencimento == null ? null : Date.valueOf(dtVencimento)));
      pstmt.setTimestamp(4, (dtPagamento == null ? null : Timestamp.valueOf(dtPagamento)));
      pstmt.setTimestamp(5, (dtInicio == null ? null : Timestamp.valueOf(dtInicio)));
      pstmt.setInt(6, pagamento.getMetodoPagamento().getNivel());
      pstmt.setInt(7, pagamento.getFkFabrica());
      pstmt.setInt(8, pagamento.getFkPlano());

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

          Timestamp dtPagtoTimestamp = rs.getTimestamp("data_pagamento");
          LocalDateTime dtPagto = (dtPagtoTimestamp == null ? null : dtPagtoTimestamp.toLocalDateTime());

          Timestamp dtInicioTimestamp = rs.getTimestamp("data_inicio");
          LocalDateTime dtInicio = (dtInicioTimestamp == null ? null : dtInicioTimestamp.toLocalDateTime());

          MetodoPagamento metodoPagamento = MetodoPagamento.deNivel(rs.getInt("fk_metodopag"));
          Integer fkFabrica = rs.getInt("fk_fabrica");
          Integer fkPlano = rs.getInt("fk_plano");

          // Adicionando instância do DTO na lista de pagamentos
          pagamentos.add(new Pagamento(id, valorPago, status, dtVencimento, dtPagto, dtInicio, metodoPagamento, fkFabrica, fkPlano));
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
        LocalDate dtVencimento = dtVencimentoDate.toLocalDate();

        Timestamp dtPagtoTimestamp = rs.getTimestamp("data_pagamento");
        LocalDateTime dtPagto = (dtPagtoTimestamp == null ? null : dtPagtoTimestamp.toLocalDateTime());

        Timestamp dtInicioTimestamp = rs.getTimestamp("data_inicio");
        LocalDateTime dtInicio = (dtInicioTimestamp == null ? null : dtInicioTimestamp.toLocalDateTime());

        MetodoPagamento metodoPagamento = MetodoPagamento.deNivel(rs.getInt("fk_metodopag"));
        int fkFabrica = rs.getInt("fk_fabrica");
        int fkPlano = rs.getInt("fk_plano");

        // Instância do Model
        pagamento = new Pagamento(id, valorPago, status, dtVencimento, dtPagto, dtInicio, metodoPagamento, fkFabrica, fkPlano);
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
    LocalDateTime dataPagamento = alterado.getDataPagamento();
    LocalDateTime dataInicio = alterado.getDataInicio();
    MetodoPagamento metodoPagamento = alterado.getMetodoPagamento();
    int fkFabrica = alterado.getFkFabrica();
    int fkPlano = alterado.getFkPlano();

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

    if (!Objects.equals(dataInicio, original.getDataInicio())) {
      sql.append("data_inicio = ?, ");
      valores.add(dataInicio);
    }

    if (!Objects.equals(metodoPagamento, original.getMetodoPagamento())){
        sql.append("fk_metodopag = ?, ");
        valores.add(metodoPagamento.getNivel());
    }

    if (original.getFkFabrica() != fkFabrica) {
      sql.append("fk_fabrica = ?, ");
      valores.add(fkFabrica);
    }

    if (original.getFkPlano() != fkPlano) {
        sql.append("fk_plano = ?, ");
        valores.add(fkPlano);
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
