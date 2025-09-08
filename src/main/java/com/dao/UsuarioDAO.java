package com.dao;

import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.model.NivelAcesso;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends DAO {
  public UsuarioDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  public void cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {
    // Armazena as informações do DTO em variáveis e declara as outras informações
    // fixas do cadastro
    String email = credenciais.getEmail();
    String senha = credenciais.getSenha();
    String nome = credenciais.getNome();
    int fkFabrica = credenciais.getFkFabrica();
    int nivelAcesso = NivelAcesso.ADMIN.nivel();
    boolean status = true;

    // Prepara o comando
    String sql = """
        INSERT INTO usuario(nome, email, senha, nivel_acesso, status, fk_fabrica)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, senha);
      pstmt.setInt(4, nivelAcesso);
      pstmt.setBoolean(5, status);
      pstmt.setInt(6, fkFabrica);

      // Executa o update
      pstmt.executeUpdate();

      // Commita as alterações no banco
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback da operação
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void remover(int id) throws SQLException {
    // Prepara o comando
    String sql = "DELETE FROM usuario WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa o parâmetro faltante, executa o comando e commita
      pstmt.setInt(1, id);
      pstmt.executeUpdate();
      conn.commit();

    } catch (SQLException e) {

      // Faz o rollback da operação
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public List<UsuarioDTO> listarUsuarios() throws SQLException {
    List<UsuarioDTO> usuarios = new ArrayList<>();

    // Prepara o comado e executa
    String sql = "SELECT * FROM usuario ORDER BY id";

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        // Armazenamento do resultado em variáveis
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        NivelAcesso nivelAcesso = NivelAcesso.fromInteger(rs.getInt("nivel_acesso"));

        // Conversão da data
        Date temp = rs.getDate("data_criacao");
        LocalDate dtCriacao = (temp == null ? null : temp.toLocalDate());

        boolean status = rs.getBoolean("status");
        int fkFabrica = rs.getInt("fk_fabrica");

        // Adição na lista
        usuarios.add(new UsuarioDTO(id, nome, email, nivelAcesso, dtCriacao, status, fkFabrica));
      }

      return usuarios;

    } catch (SQLException e) {

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void atualizar(AtualizacaoUsuarioDTO original, AtualizacaoUsuarioDTO alterado) throws SQLException {
    // Desempacotamento do DTO de alteração
    int id = alterado.getId();
    String nome = alterado.getNome();
    String email = alterado.getEmail();
    NivelAcesso nivelAcesso = alterado.getNivelAcesso();
    boolean status = alterado.getStatus();
    int fkFabrica = alterado.getFkFabrica();

    // Preparação dinâmica do comando
    StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
    List<Object> valores = new ArrayList<>();

    if (!nome.equals(original.getNome())) {
      sql.append("nome = ?,  ");
      valores.add(nome);
    }

    if (!email.equals(original.getEmail())) {
      sql.append("email = ?, ");
      valores.add(email);
    }

    if (!nivelAcesso.equals(original.getNivelAcesso())) {
      sql.append("nivel_acesso = ?, ");
      valores.add(nivelAcesso.nivel());
    }

    if (status != original.getStatus()) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (fkFabrica != original.getFkFabrica()) {
      sql.append("fk_fabrica = ?, ");
      valores.add(fkFabrica);
    }

    if (valores.isEmpty()) {
      return;
    }

    sql.setLength(sql.length() - 2);
    sql.append(" WHERE id = ?");
    valores.add(id);

    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
      // Preenchimento dos placeholders
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Execução e commit das alterações
      pstmt.executeUpdate();
      conn.commit();

    } catch (SQLException e) {
      // Faz o rollback das alterações e propaga a exceção
      conn.rollback();
      throw e;
    }
  }

  public AtualizacaoUsuarioDTO getCamposAlteraveis(int id) throws SQLException {
    String sql = "SELECT * FROM usuario WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          String nome = rs.getString("nome");
          String email = rs.getString("email");
          int temp = rs.getInt("nivel_acesso");
          NivelAcesso nivelAcesso = NivelAcesso.fromInteger(temp);
          boolean status = rs.getBoolean("status");
          int fkFabrica = rs.getInt("fk_fabrica");

          return new AtualizacaoUsuarioDTO(id, nome, email, nivelAcesso, status, fkFabrica);
        } else {
          throw new SQLException("Falha ao recuperar informações do usuário");
        }
      }
    }
  }
}
