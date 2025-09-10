package com.dao;

import com.dto.EnderecoDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EnderecoDAO extends DAO {
  //Construtor
  public EnderecoDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  //Cadastrar novo endereco
  public void cadastrar(EnderecoDTO endereco) throws SQLException {
    //Comando SQL
    String sql = "INSERT INTO endereco (cep, numero, rua, complemento) VALUES (?,?,?,?)";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis do comando SQL
      pstmt.setString(1, endereco.getCep());
      pstmt.setInt(2, endereco.getNumero());
      pstmt.setString(3, endereco.getRua());
      pstmt.setString(4, endereco.getComplemento());
      //Salvando inserção no banco
      pstmt.execute();
      //Efetuando transação
      this.conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Cancelando transação
      this.conn.rollback();
      //Lançando excessão
      throw e;
    }
  }

  //Remover endereco
  public void remover(EnderecoDTO endereco) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM endereco WHERE cep = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
      //Definindo variáveis no código SQL
      pstmt.setString(1, endereco.getCep());
      //Salvando alterações no banco
      if (pstmt.executeUpdate() != 1) {
        throw new SQLException();
      }
      //Confirmando transações
      this.conn.commit();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Cancelando transações
      this.conn.rollback();
      //Lançando excessões
      throw e;
    }
  }

  //Atualizar cep
  private void alterarCep(EnderecoDTO endereco, String novoCep) throws SQLException {
    //Comando SQL
    String sql = "UPDATE endereco WHERE cep = ? SET cep = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setString(1, endereco.getCep());
      pstmt.setString(2, novoCep);
      //Salvando alterações no banco de dados
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançando exceção
      throw e;
    }
  }

  //Atualizar numero
  private void alterarNumero(EnderecoDTO endereco, Integer novoNumero) throws SQLException {
    //Comando SQL
    String sql = "UPDATE endereco WHERE numero = ? SET numero = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setInt(1, endereco.getNumero());
      pstmt.setInt(2, novoNumero);
      //Salvando alterações no banco de dados
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançando exceção
      throw e;
    }
  }

  //Atualizar rua
  private void alterarRua(EnderecoDTO endereco, String novaRua) throws SQLException {
    //Comando SQL
    String sql = "UPDATE endereco WHERE rua = ? SET rua = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setString(1, endereco.getRua());
      pstmt.setString(2, novaRua);
      //Salvando alterações no banco de dados
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançando exceção
      throw e;
    }
  }

  //Atualizar complemento
  private void alterarComplemento(EnderecoDTO endereco, String novoComplemento) throws SQLException {
    //Comando SQL
    String sql = "UPDATE endereco WHERE rua = ? SET rua = ?";

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) { //Preparando comando SQL
      //Definindo variáveis no código SQL
      pstmt.setString(1, endereco.getComplemento());
      pstmt.setString(2, novoComplemento);
      //Salvando alterações no banco de dados
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      //Lançando exceção
      throw e;
    }
  }

  //Verificar requisição de atualização e confirmar alteração no banco
  public void alterar(EnderecoDTO endereco, String cep, Integer numero, String rua, String complemento) throws SQLException {
    try {
      //realizando verificações
      if (endereco.getCep() != null) {
        this.alterarCep(endereco, cep);
      } else if (endereco.getNumero() != null) {
        this.alterarNumero(endereco, numero);
      } else if (endereco.getRua() != null) {
        this.alterarRua(endereco, rua);
      } else if (endereco.getComplemento() != null) {
        this.alterarComplemento(endereco, complemento);
      }
      //Confirmando transação
      this.conn.commit();
    } catch (SQLException sql) {
      System.err.println(sql.getMessage());
      //Cancelando transação
      this.conn.rollback();
      //Lançando excessão
      throw sql;
    }

  }
}
