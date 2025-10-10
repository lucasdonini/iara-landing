package com.dao;

import com.dto.LoginDTO;
import com.dto.SuperAdmDTO;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends DAO {
  // Construtor
  public LoginDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Outros Métodos
  public SuperAdmDTO login(LoginDTO credenciais) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM super_adm WHERE email = ?";

    // Variáveis
    String senhaHash, nome, cargo;
    int id;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setString(1, credenciais.getEmail());

      // Resgata do banco de dados o super adm correspondente ao email
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

        // Variáveis
        id = rs.getInt("id");
        nome = rs.getString("nome");
        cargo = rs.getString("cargo");

        // Retorno e instância do DTO
        return new SuperAdmDTO(id, nome, cargo, credenciais.getEmail());
      }
    }
  }
}
