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
    conn.setAutoCommit(true);
  }

  // Método para realizar o login, comparando os dados originais do banco com os inseridos pelo usuário
  public SuperAdmDTO login(LoginDTO credenciais) throws SQLException {

    String sql = "SELECT id, nome, cargo, senha FROM super_adm WHERE email = ?";

    String senhaHash, nome, cargo;
    SuperAdmDTO superAdm;
    int id;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, credenciais.getEmail());

      try (ResultSet rs = pstmt.executeQuery()) {

        // Se não encontrar retorna null (o login falhou)
        if (!rs.next()) {
          return null;
        }

        // Caso contrário, busca o hash da senha no banco
        senhaHash = rs.getString("senha");

        // Se a senha não for compatível retorna null (login falhou)
        if (!SenhaUtils.comparar(credenciais.getSenha(), senhaHash)) {
          return null;
        }

        id = rs.getInt("id");
        nome = rs.getString("nome");
        cargo = rs.getString("cargo");

        superAdm = new SuperAdmDTO(id, nome, cargo, credenciais.getEmail());
      }
    }

    return superAdm;
  }
}
