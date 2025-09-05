package com.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;

public class LoginDAO extends DAO {
  public LoginDAO() throws SQLException {
    super();
  }

  public SuperAdmDTO login(LoginDTO credenciais) throws SQLException {

    // Prepara o comando
    String sql = "SELECT * FROM super_adm WHERE email = ? AND senha = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Completa os parâmetros faltantes
      pstmt.setString(1, credenciais.getEmail());
      pstmt.setString(2, credenciais.getSenha());

      // Captura o resultado
      try (ResultSet rs = pstmt.executeQuery()) {

        // Se não houver retorno, o login falhou
        if (!rs.next()) {
          return null;
        }

        // Caso contrário, armazena os atributos e retorna o DTO
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
