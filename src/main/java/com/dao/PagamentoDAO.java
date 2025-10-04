package com.dao;

import com.model.Pagamento;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagamentoDAO extends DAO {
  //Map
  public static final Map<String, String> camposFiltraveis = Map.of(
      "ID", "id",
      "Valor", "valor",
      "Status", "status",
      "Tipo de Pagamento", "tipo_pagamento",
      "Data de Vencimento", "data_vencimento",
      "Data de Pagamento", "data_pagamento",
      "Id da Fábrica", "id_fabrica"
  );

  public PagamentoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo Plano
  public void cadastrar(Pagamento pagamento) throws SQLException {
    //Comando SQL
    String sql = "INSERT INTO pagamento(valor, status, data_vencimento, data_pagamento, tipo_pagamento, id_fabrica) VALUES (?,?,?,?,?,?)";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Comando SQL de Query do valor do plano referente a fkFabrica
      String valorPagoSQL = "SELECT p.valor FROM plano p JOIN fabrica f ON p.id = f.id_plano JOIN pagamento pa ON pa.id_fabrica = f.id WHERE f.id = ?";
      //Preparando comando SQL
      PreparedStatement pstmtValor = conn.prepareStatement(valorPagoSQL);
      //Definindo variável relacionada a FK da fabrica
      pstmtValor.setInt(1, pagamento.getFkFabrica());
      //Executando a Query
      ResultSet rs = pstmtValor.executeQuery();

      //Definindo variáveis no código SQL
      if (rs.next()) {
        pstmt.setDouble(1, rs.getDouble("valor"));
      } else {
        pstmt.setDouble(1, 0.0);
      }

      LocalDate dtPagamento = pagamento.getDataPagamento();
      pstmt.setBoolean(2, pagamento.getStatus());
      pstmt.setDate(3, Date.valueOf(pagamento.getDataVencimento()));
      pstmt.setDate(4, dtPagamento == null ? null : Date.valueOf(dtPagamento));
      pstmt.setString(5, pagamento.getTipoPagamento());
      pstmt.setInt(6, pagamento.getFkFabrica());
      //Salvando alterações no banco
      pstmt.execute();
      //Confirmando transações
      rs.close();
      pstmtValor.close();
      this.conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Cancelando transações
      this.conn.rollback();
      //Enviando exceção
      throw e;
    }
  }

  public Pagamento getPagamentoById(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM pagamento WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        Date dtPagtoDate = rs.getDate("data_pagamento");
        LocalDate dtPagto = dtPagtoDate == null ? null : dtPagtoDate.toLocalDate();

        return new Pagamento(
            id,
            rs.getDouble("valor"),
            rs.getBoolean("status"),
            rs.getDate("data_vencimento").toLocalDate(),
            dtPagto,
            rs.getString("tipo_pagamento"),
            rs.getInt("id_fabrica")
        );
      }
    }
  }

  //Remover plano
  public void remover(int id) throws SQLException {
    // Prepara o comando
    String sql = "DELETE FROM pagamento WHERE id = ?";

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

  public Object converterValor(String campo, String valor) {
    if (campo == null || campo.isBlank()) {
      return null;
    }

    return switch (campo) {
      case "id", "id_fabrica" -> Integer.parseInt(valor);
      case "valor" -> Double.parseDouble(valor);
      case "data_vencimento", "data_pagamento" -> LocalDate.parse(valor);
      case "tipo_pagamento" -> String.valueOf(valor);
      default -> throw new IllegalArgumentException();
    };
  }

  public void atualizar(Pagamento original, Pagamento alterado) throws SQLException {
    // Desempacotamento do model alterado
    int id = alterado.getId();
    double valorPago = alterado.getValorPago();
    boolean status = alterado.getStatus();
    LocalDate dataVencimento = alterado.getDataVencimento();
    LocalDate dataPagamento = alterado.getDataPagamento();
    String tipoPagamento = alterado.getTipoPagamento();
    Integer fkFabrica = alterado.getFkFabrica();

    // Monta o comando de acordo com os campos alterados
    StringBuilder sql = new StringBuilder("UPDATE pagamento SET ");
    List<Object> valores = new ArrayList<>();

    if (original.getValorPago() != valorPago) {
      sql.append("valor = ?, ");
      valores.add(valorPago);
    }

    if (!original.getStatus() == status) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (!original.getDataVencimento().equals(dataVencimento)) {
      sql.append("data_vencimento = ?, ");
      valores.add(dataVencimento);
    }

    if (!Objects.equals(original.getDataPagamento(), dataPagamento)) {
      sql.append("data_pagamento = ?, ");
      valores.add(dataPagamento);
    }

    if (!original.getTipoPagamento().equals(tipoPagamento)) {
      sql.append("tipo_pagamento = ?, ");
      valores.add(tipoPagamento);
    }

    if (!original.getFkFabrica().equals(fkFabrica)) {
      sql.append("id_fabrica = ?, ");
      valores.add(fkFabrica);
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

  public List<Pagamento> listarPagamentos(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    List<Pagamento> pagamentos = new ArrayList<>();

    // Prepara o comando
    String sql = "SELECT * FROM pagamento";

    //Verificando o campo do filtro
    if (campoFiltro != null && !campoFiltro.isBlank()) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    //Verificando campo e direcao para ordernar a consulta
    if (campoFiltro != null && !campoSequencia.isBlank()) {
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
        int id = rs.getInt("id");
        double valorPago = rs.getDouble("valor");
        boolean status = rs.getBoolean("status");
        LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();

        Date dtPagtoDate = rs.getDate("data_pagamento");
        LocalDate dtPagto = dtPagtoDate == null ? null : dtPagtoDate.toLocalDate();

        String tipoPagamento = rs.getString("tipo_pagamento");
        int fkFabrica = rs.getInt("id_fabrica");

        pagamentos.add(new Pagamento(id, valorPago, status, dataVencimento, dtPagto, tipoPagamento, fkFabrica));
      }
    }

    return pagamentos;
  }

  //Campos Alteraveis
  public Pagamento getCamposAlteraveis(int id) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM pagamento WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {

        if (rs.next()) {
          double valorPago = rs.getDouble("valor");
          boolean status = rs.getBoolean("status");
          LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();

          Date dtPagtoDate = rs.getDate("data_pagamento");
          LocalDate dataPagamento = dtPagtoDate == null ? null : dtPagtoDate.toLocalDate();

          String tipoPagamento = rs.getString("tipo_pagamento");
          Integer fkFabrica = rs.getInt("id_fabrica");

          return new Pagamento(id, valorPago, status, dataVencimento, dataPagamento, tipoPagamento, fkFabrica);
        } else {
          throw new SQLException("Erro ao recuperar as informações do super adm");
        }
      }
    }
  }
}
