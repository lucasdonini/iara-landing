package com.dao;

import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends DAO {
  public LoginDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  public SuperAdmDTO login(LoginDTO credenciais) throws SQLException {
    // Prepara o comando
    String sql = "SELECT * FROM super_adm WHERE email = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setString(1, credenciais.getEmail());

      // Captura o resultado
      try (ResultSet rs = pstmt.executeQuery()) {

        // Se não houver retorno, o login falhou
        if (!rs.next()) {
          return null;
        }

        // Caso contrário, busca o hash da senha no banco
        String senhaHash = rs.getString("senha");

        // Se a senha não confere, login falhou
        if (!SenhaUtils.comparar(credenciais.getSenha(), senhaHash)) {
          return null;
        }

        // Se o login der certo, retorna as informações do usuário
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cargo = rs.getString("cargo");

        return new SuperAdmDTO(id, nome, cargo, credenciais.getEmail());
      }
    } catch (SQLException e) {

      // Registra o erro no terminal e o propaga
      System.err.println(e.getMessage());
      throw e;
    }
  }

}
