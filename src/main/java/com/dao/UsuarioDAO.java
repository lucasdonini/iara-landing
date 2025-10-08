package com.dao;

import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.model.TipoAcesso;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UsuarioDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "nome", "Nome",
      "email", "Email",
      "id_fabrica", "Fábrica",
      "tipo_acesso", "Tipo de Acesso",
      "status", "Status",
      "data_criacao", "Data de Registro"
  );

  // Construtor
  public UsuarioDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Métodos Estáticos
  public static Object converterValor(String campo, String valor) {
    if (campo == null || campo.isBlank()) {
      return null;
    }

    return switch (campo) {
      case "id", "id_fabrica", "tipo_acesso" -> Integer.parseInt(valor);
      case "status" -> Boolean.parseBoolean(valor);
      case "data_criacao" -> LocalDate.parse(valor);
      case "nome", "email" -> valor;
      default -> throw new IllegalArgumentException("Campo inválido: " + campo);
    };
  }

  // Outros Métodos
  // === CREATE ===
  public void cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {
    // Armazena as informações do DTO em variáveis e declara as outras informações
    // fixas do cadastro
    String email = credenciais.getEmail();
    String senha = credenciais.getSenha();
    String nome = credenciais.getNome();
    int idFabrica = credenciais.getIdFabrica();
    int tipoAcesso = TipoAcesso.GERENCIAMENTO.nivel();
    boolean status = true;

    // Prepara o comando
    String sql = """
        INSERT INTO usuario(nome, email, senha, tipo_acesso, status, id_fabrica)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Completa os parâmetros faltantes
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, senha);
      pstmt.setInt(4, tipoAcesso);
      pstmt.setBoolean(5, status);
      pstmt.setInt(6, idFabrica);

      // Executa o update
      pstmt.executeUpdate();

      // Commita as alterações no banco
      conn.commit();

    } catch (SQLException e) {
      // Faz o rollback da operação
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<UsuarioDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    List<UsuarioDTO> usuarios = new ArrayList<>();

    // Prepara o comado e executa
    String sql = "SELECT id, id_fabrica, email, nome, tipo_acesso, status, data_criacao FROM usuario";

    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    //Verificando campo para ordenar a consulta
    if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //Definindo parâmetro vazio
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setObject(1, valorFiltro);
      }

      //Instanciando um ResultSet
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          // Armazenamento do resultado em variáveis
          int id = rs.getInt("id");
          String nome = rs.getString("nome");
          String email = rs.getString("email");
          TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

          // Conversão da data
          Date temp = rs.getDate("data_criacao");
          LocalDate dtCriacao = (temp == null ? null : temp.toLocalDate());

          boolean status = rs.getBoolean("status");
          int idFabrica = rs.getInt("id_fabrica");

          // Adição na lista
          usuarios.add(new UsuarioDTO(id, nome, email, tipoAcesso, dtCriacao, status, idFabrica));
        }
      }
    }

    return usuarios;
  }

  public AtualizacaoUsuarioDTO getCamposAlteraveis(int id) throws SQLException {
    String sql = "SELECT nome, email, tipo_acesso, status, id_fabrica FROM usuario WHERE id = ?";
    AtualizacaoUsuarioDTO usuario;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Falha ao recuperar informações do usuário");
        }

        String nome = rs.getString("nome");
        String email = rs.getString("email");
        int temp = rs.getInt("tipo_acesso");
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(temp);
        boolean status = rs.getBoolean("status");
        int idFabrica = rs.getInt("id_fabrica");

        usuario = new AtualizacaoUsuarioDTO(id, nome, email, tipoAcesso, status, idFabrica);
      }
    }

    return usuario;
  }

  public UsuarioDTO pesquisarPorEmail(String email) throws SQLException {
    // Prepara o comando
    String sql = "SELECT id, nome, tipo_acesso, data_criacao, status, id_fabrica FROM usuario WHERE email = ?";
    UsuarioDTO usuario;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, email);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

        Date dtCriacaoDate = rs.getDate("data_criacao");
        LocalDate dtCriacao = (dtCriacaoDate == null ? null : dtCriacaoDate.toLocalDate());

        boolean status = rs.getBoolean("status");
        int fkFabrica = rs.getInt("id_fabrica");

        usuario = new UsuarioDTO(id, nome, email, tipoAcesso, dtCriacao, status, fkFabrica);
      }
    }

    return usuario;
  }

  // === UPDATE ===
  public void atualizar(AtualizacaoUsuarioDTO original, AtualizacaoUsuarioDTO alterado) throws SQLException {
    // Desempacotamento do DTO de alteração
    int id = alterado.getId();
    String nome = alterado.getNome();
    String email = alterado.getEmail();
    TipoAcesso tipoAcesso = alterado.getTipoAcesso();
    boolean status = alterado.getStatus();
    int fkFabrica = alterado.getIdFabrica();

    // Preparação dinâmica do comando
    StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
    List<Object> valores = new ArrayList<>();

    if (!Objects.equals(nome, original.getNome())) {
      sql.append("nome = ?,  ");
      valores.add(nome);
    }

    if (!Objects.equals(email, original.getEmail())) {
      sql.append("email = ?, ");
      valores.add(email);
    }

    if (!Objects.equals(tipoAcesso, original.getTipoAcesso())) {
      sql.append("tipo_acesso = ?, ");
      valores.add(tipoAcesso.nivel());
    }

    if (status != original.getStatus()) {
      sql.append("status = ?, ");
      valores.add(status);
    }

    if (fkFabrica != original.getIdFabrica()) {
      sql.append("id_fabrica = ?, ");
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

  // === DELETE ===
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
      throw e;
    }
  }
}
