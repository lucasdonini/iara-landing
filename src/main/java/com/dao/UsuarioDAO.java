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

  // Converter Valor
  public Object converterValor(String campo, String valor){
      return switch(campo){
          case "id", "id_fabrica" -> Integer.parseInt(valor);
          case "status" -> Boolean.parseBoolean(valor);
          case "data_criacao" -> LocalDate.parse(valor);
          case "nome", "email", "tipo_acesso" -> String.valueOf(valor);
          default -> throw new IllegalArgumentException();
      };
  }

  // Outros Métodos

  // === CREATE ===
  public void cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {
    // Variáveis
    String email = credenciais.getEmail();
    String senha = credenciais.getSenha();
    String nome = credenciais.getNome();
    int idFabrica = credenciais.getIdFabrica();
    int tipoAcesso = TipoAcesso.GERENCIAMENTO.nivel();
    boolean status = true;

    // Comando SQL
    String sql = """
        INSERT INTO usuario(nome, email, senha, tipo_acesso, status, id_fabrica)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //  Definindo variáveis do comando SQL
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, senha);
      pstmt.setInt(4, tipoAcesso);
      pstmt.setBoolean(5, status);
      pstmt.setInt(6, idFabrica);

      //  Cadastra o usuário no banco de dados
      pstmt.executeUpdate();

      // Efetuando a alteração
      conn.commit();

    } catch (SQLException e) {
      // Cancelando a alteração
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<UsuarioDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    // Lista de usuários
    List<UsuarioDTO> usuarios = new ArrayList<>();

    // Comando SQL
    String sql = "SELECT id, id_fabrica, email, nome, tipo_acesso, status, data_criacao FROM usuario";

    // Verificando campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE %s = ?".formatted(campoFiltro);
    }

    // Verificando campo e direcao da ordenação
    if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
      sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);

    } else {
      sql += " ORDER BY id ASC";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setObject(1, valorFiltro);
      }

      // Resgata do banco de dados a lista de usuários
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          // Variáveis
          int id = rs.getInt("id");
          String nome = rs.getString("nome");
          String email = rs.getString("email");
          TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

          Date temp = rs.getDate("data_criacao");
          LocalDate dtCriacao = (temp == null ? null : temp.toLocalDate());

          boolean status = rs.getBoolean("status");
          int idFabrica = rs.getInt("id_fabrica");

          // Adicionando instância do DTO na lista de usuários
          usuarios.add(new UsuarioDTO(id, nome, email, tipoAcesso, dtCriacao, status, idFabrica));
        }
      }
    }

    // Retorna a lista de usuários
    return usuarios;
  }

  public AtualizacaoUsuarioDTO getCamposAlteraveis(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT nome, email, tipo_acesso, status, id_fabrica FROM usuario WHERE id = ?";

    // Objeto não instanciado de usuário
    AtualizacaoUsuarioDTO usuario;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Pesquisa usuário pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar lança exceção
        if (!rs.next()) {
          throw new SQLException("Falha ao recuperar informações do usuário");
        }

        // Variáveis
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        int temp = rs.getInt("tipo_acesso");
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(temp);
        boolean status = rs.getBoolean("status");
        int idFabrica = rs.getInt("id_fabrica");

        // Instância do DTO
        usuario = new AtualizacaoUsuarioDTO(id, nome, email, tipoAcesso, status, idFabrica);
      }
    }

    // Retorna usuário
    return usuario;
  }

  public UsuarioDTO pesquisarPorEmail(String email) throws SQLException {
    // Comando SQL
    String sql = "SELECT id, nome, tipo_acesso, data_criacao, status, id_fabrica FROM usuario WHERE email = ?";

    // Objeto não instanciado de usuário
    UsuarioDTO usuario;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo váriavel do comando SQl
      pstmt.setString(1, email);

      // Pesquisa usuário pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encotrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

        Date dtCriacaoDate = rs.getDate("data_criacao");
        LocalDate dtCriacao = (dtCriacaoDate == null ? null : dtCriacaoDate.toLocalDate());

        boolean status = rs.getBoolean("status");
        int fkFabrica = rs.getInt("id_fabrica");

        // Instância do DTO
        usuario = new UsuarioDTO(id, nome, email, tipoAcesso, dtCriacao, status, fkFabrica);
      }
    }

    // Retorna usuário
    return usuario;
  }

  // === UPDATE ===
  public void atualizar(AtualizacaoUsuarioDTO original, AtualizacaoUsuarioDTO alterado) throws SQLException {
    // Variáveis
    int id = alterado.getId();
    String nome = alterado.getNome();
    String email = alterado.getEmail();
    TipoAcesso tipoAcesso = alterado.getTipoAcesso();
    boolean status = alterado.getStatus();
    int fkFabrica = alterado.getIdFabrica();

    // Construção do comando SQL dinâmico
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

    // Retorna vazio se nada foi alterado
    if (valores.isEmpty()) {
      return;
    }

    // Remoção da última ", "
    sql.setLength(sql.length() - 2);

    // Adiciona a cláusula WHERE
    sql.append(" WHERE id = ?");
    valores.add(id);

    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
      // Definindo variáveis do comando SQL
      for (int i = 0; i < valores.size(); i++) {
        pstmt.setObject(i + 1, valores.get(i));
      }

      // Atualiza o usuário no banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === DELETE ===
  public void remover(int id) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM usuario WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Deleta o usuário do banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }
}
