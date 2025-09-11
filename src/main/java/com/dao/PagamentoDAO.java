package com.dao;

import com.dto.PagamentoDTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PagamentoDAO extends DAO {
  //Construtor
  public PagamentoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo pagamento
  public void cadastrar(PagamentoDTO pagamento) throws SQLException {
    //Comando SQL
    String sql = "INSERT INTO pagamento(status, data_vencimento, data_pagamento, tipo_pagamento) VALUES (?,?,?,?,?)";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setBoolean(1, pagamento.getStatus());
      pstmt.setDate(2, Date.valueOf(pagamento.getDataVencimento()));
      pstmt.setDate(3, Date.valueOf(pagamento.getDataPagamento()));
      pstmt.setString(4, pagamento.getTipoPagamento());
      //Salvando alterações no banco de dados
      pstmt.execute();
      //Realizando transações
      this.conn.commit();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      //Cancelando transações
      this.conn.rollback();
      //Lançando excessão
      throw e;
    }
  }

  //Remover pagamento
  public void remover(PagamentoDTO pagamento) throws SQLException {
    //Comando SQL
    String sql = "DELETE FROM pagamento WHERE id = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setInt(5, pagamento.getId());
      //Salvando alterações no banco de dados
      pstmt.execute();
      //Realizando transações
      this.conn.commit();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      //Cancelando transações
      this.conn.rollback();
      //Lançando excessão
      throw e;
    }
  }

  //Atualizar status
  private void atualizarStatus(PagamentoDTO pagamento) throws SQLException {
    //Comando SQL
    String sql = "UPDATE pagamento WHERE id = ? SET status = true";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setInt(1, pagamento.getId());
      //Salvando no banco de dados
      pstmt.executeUpdate();
      //Realizando transação
      conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançanco excessão
      throw e;
    }
  }

  //Atualizar data de vencimento
  private void atualizarDataVencimento(PagamentoDTO pagamento, LocalDate novaDataVencimento) throws SQLException {
    //Comando SQL
    String sql = "UPDATE pagamento WHERE id = ? SET data_vencimento = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setInt(1, pagamento.getId());
      pstmt.setDate(1, Date.valueOf(novaDataVencimento));
      //Salvando no banco de dados
      pstmt.executeUpdate();
      //Realizando transação
      conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançanco excessão
      throw e;
    }
  }

  //Atualizar data do pagamento
  private void atualizarDataPagamento(PagamentoDTO pagamento, LocalDate novaDataPagamento) throws SQLException {
    //Comando SQL
    String sql = "UPDATE pagamento WHERE id = ? SET data_pagamento = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setInt(1, pagamento.getId());
      pstmt.setDate(1, Date.valueOf(novaDataPagamento));
      //Salvando no banco de dados
      pstmt.executeUpdate();
      //Realizando transação
      conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançanco excessão
      throw e;
    }
  }

  //Atualizar tipo de pagamento
  private void atualizarTipoPagamento(PagamentoDTO pagamento, String novoTipoPagamento) throws SQLException {
    //Comando SQL
    String sql = "UPDATE pagamento WHERE id = ? SET tipo_pagamento = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do código SQL
      pstmt.setInt(1, pagamento.getId());
      pstmt.setString(1, novoTipoPagamento);
      //Salvando no banco de dados
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançanco excessão
      throw e;
    }
  }

  //Metodo para chamar o metodo update adequado
  public void alterar(PagamentoDTO pagamento, LocalDate dataVencimento, LocalDate dataPagamento, String tipoPagamento) throws SQLException {

    try {
      //Condicionais para chamar metodo atualizar
      if (dataVencimento != null) {
        this.atualizarDataVencimento(pagamento, dataVencimento);
      } else if (dataPagamento != null) {
        this.atualizarDataPagamento(pagamento, dataPagamento);
        this.atualizarStatus(pagamento);
      } else if (tipoPagamento != null) {
        this.atualizarTipoPagamento(pagamento, tipoPagamento);
      }

      //Realizando transações
      conn.commit();

    } catch (SQLException sql) {
      System.out.println(sql.getMessage());
      ;
      //Cancelando transações
      conn.rollback();
      //Lançando excessão
      throw sql;
    }
  }
}
