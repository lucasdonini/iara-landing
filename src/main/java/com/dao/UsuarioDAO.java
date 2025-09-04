package com.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.model.NivelAcesso;

// TODO: fazer o hash da senha com o método do Chris antes de guardar no banco

public class UsuarioDAO extends DAO {
  public UsuarioDAO() throws SQLException {
    super();
  }

  public UsuarioDTO cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {
    // Armazena as informações do DTO em variáveis e declara as outras informações
    // fixas do cadastro
    String email = credenciais.getEmail();
    String senha = credenciais.getSenha();
    String nome = credenciais.getNome();
    int fkFabrica = credenciais.getFkFabrica();
    int nivelAcesso = NivelAcesso.ADMIN.nivel();
    boolean status = true;
    LocalDate dtCriacao;
    int id;

    // Prepara o comando
    String sql = """
        INSERT INTO usuario(nome, email, senha, nivel_acesso, status, fk_fabrica)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING id, data_criacao
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

      // Recupera as chaves autogeradas
      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt("id");

          // Conversão da data
          Date temp = rs.getDate("data_criacao");
          dtCriacao = temp != null ? temp.toLocalDate() : null;
        } else {
          throw new SQLException("Failed to get autognerated keys");
        }
      }

      // Commita as alterações no banco
      conn.commit();

      // Inicializa e retorna o DTO de retorno
      return new UsuarioDTO(id, nome, email, NivelAcesso.ADMIN, dtCriacao, status, fkFabrica);

    } catch (SQLException e) {

      // Faz o rollback da operação
      conn.rollback();

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

  public void remover(UsuarioDTO usuario) throws SQLException {
    // Recupera o id do usuário
    int id = usuario.getId();

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
    String sql = "SELECT * FROM usuario";

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
}
