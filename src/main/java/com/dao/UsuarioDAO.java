package com.dao;

import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.model.TipoAcesso;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UsuarioDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "ID",
      "nome", "Nome",
      "genero", "Gênero",
      "data_nascimento", "Data de Nascimento",
      "cargo", "Cargo",
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
      try{
          return switch(campo){
              case "tipo_acesso", "id_fabrica" -> Integer.parseInt(valor);
              case "status" -> Boolean.parseBoolean(valor);
              case "data_nascimento" -> LocalDate.parse(valor);
              case "data_criacao" -> LocalDateTime.parse(valor);
              case "nome", "genero", "cargo", "email" -> valor;
              default -> new IllegalArgumentException();
          };
      } catch(DateTimeParseException | NumberFormatException | NullPointerException e){
          return null;
      }
  }


  // Outros Métodos

  // === CREATE ===
  public void cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNome();
    String emailGerente = credenciais.getEmailGerente();
    String genero = credenciais.getGenero();
    LocalDate dataNascimento = credenciais.getDataNascimento();
    String cargo = credenciais.getCargo();
    String email = credenciais.getEmail();
    String senha = credenciais.getSenha();
    Integer fkFabrica = credenciais.getFkFabrica();
    TipoAcesso tipoAcesso = TipoAcesso.GERENTE;
    boolean status = true;

    // Comando SQL
    String sql = """
        INSERT INTO usuario(nome, id_gerente, genero, data_nascimento, cargo, email, senha, tipo_acesso, status, fk_fabrica, desc_tipoacesso)
        VALUES (?, (SELECT id FROM usuario WHERE email = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      //  Definindo variáveis do comando SQL
      pstmt.setString(1, nome);
      pstmt.setString(2, emailGerente);
      pstmt.setString(3, genero);
      pstmt.setDate(4, Date.valueOf(dataNascimento));
      pstmt.setString(5, cargo);
      pstmt.setString(6, email);
      pstmt.setString(7, senha);
      pstmt.setInt(8, tipoAcesso.nivel());
      pstmt.setBoolean(9, status);
      pstmt.setInt(10, fkFabrica);
      pstmt.setString(11, tipoAcesso.descricao());

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
    String sql = """
        SELECT u.id, u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.email, u.cargo, u.tipo_acesso, u.desc_tipoacesso, u.data_criacao, u.status, f.nome_unidade as "nome_fabrica"
        FROM usuario u
        LEFT JOIN usuario g ON g.id = u.id_gerente
        JOIN fabrica f ON f.id = u.fk_fabrica
      """;

    // Verificando campo do filtro
    if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
      sql += " WHERE u.%s = ?".formatted(campoFiltro);
    }

    // Verificando campo e direcao da ordenação
    if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
      sql += " ORDER BY u.%s %s".formatted(campoSequencia, direcaoSequencia);

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
          UUID id = UUID.fromString(rs.getString("id"));
          String nome = rs.getString("nome");
          String emailGerente = rs.getString("email_gerente");
          String genero = rs.getString("genero");

          Date temp = rs.getDate("data_nascimento");
          LocalDate dataNascimento = (temp == null ? null : temp.toLocalDate());

          String cargo = rs.getString("cargo");
          String email = rs.getString("email");
          TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));
          String descTipoAcesso = rs.getString("desc_tipoacesso");

          Timestamp temp2 = rs.getTimestamp("data_criacao");
          LocalDateTime dataCriacao = (temp2 == null ? null : temp2.toLocalDateTime());

          boolean status = rs.getBoolean("status");
          String nomeFabrica = rs.getString("nome_fabrica");

          // Adicionando instância do DTO na lista de usuários
          usuarios.add(new UsuarioDTO(id, nome, emailGerente, genero, dataNascimento, email, cargo, tipoAcesso, descTipoAcesso, dataCriacao, status, nomeFabrica));
        }
      }
    }

    // Retorna a lista de usuários
    conn.commit();
    return usuarios;
  }

  public List<String> emailGerentes() throws SQLException{
      // Lista de emails
      List<String> emailGerentes = new ArrayList<>();

      // Comando SQL
      String sql = "SELECT email FROM email_gerentes";

      try(PreparedStatement pstmt = conn.prepareStatement(sql)){
          // Resgata do banco de dados a lista de email dos gerentes
          try(ResultSet rs = pstmt.executeQuery()){
              while(rs.next()){
                  // Adicionando email na lista de email dos gerentes
                  emailGerentes.add(rs.getString("email"));
              }
          }
      }

      // Retorna a lista de email dos gerentes
      return emailGerentes;
  }

  public AtualizacaoUsuarioDTO getCamposAlteraveis(UUID id) throws SQLException {
    // Comando SQL
    String sql = """
        SELECT u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.cargo, u.email, u.tipo_acesso, u.status, u.fk_fabrica, u.desc_tipoacesso
        FROM usuario u
        LEFT JOIN usuario g ON g.id = u.id_gerente
        WHERE u.id = ?
    """;

    // Objeto não instanciado de usuário
    AtualizacaoUsuarioDTO usuario;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setObject(1, id);

      // Pesquisa usuário pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar lança exceção
        if (!rs.next()) {
          throw new SQLException("Falha ao recuperar informações do usuário");
        }

        // Variáveis
          String nome = rs.getString("nome");
          String emailGerente = rs.getString("email_gerente");
          String genero = rs.getString("genero");
          String cargo = rs.getString("cargo");
          String email = rs.getString("email");
          TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));
          String descTipoAcesso = rs.getString("desc_tipoacesso");
          boolean status = rs.getBoolean("status");
          Integer fkFabrica = rs.getInt("fk_fabrica");


          // Instância do DTO
        usuario = new AtualizacaoUsuarioDTO(id, nome, emailGerente, genero, cargo, email, tipoAcesso, descTipoAcesso, status, fkFabrica);
      }
    }

    // Retorna usuário
    conn.commit();
    return usuario;
  }

  public UsuarioDTO pesquisarPorEmail(String email) throws SQLException {
    // Comando SQL
    String sql = """
        SELECT u.id, u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.email, u.cargo, u.tipo_acesso, u.desc_tipoacesso, u.data_criacao, u.status, f.nome_unidade as "nome_fabrica"
        FROM usuario u
        JOIN usuario g ON g.id = u.id_gerente
        JOIN fabrica f ON f.id = u.fk_fabrica
        WHERE u.email = ?
    """;

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
        // TODO: testar o getObject
        UUID id = UUID.fromString(rs.getString("id"));
        String nome = rs.getString("nome");
        String emailGerente = rs.getString("email_gerente");
        String genero = rs.getString("genero");

        Date temp = rs.getDate("data_nascimento");
        LocalDate dataNascimento = (temp == null ? null : temp.toLocalDate());

        String cargo = rs.getString("cargo");
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));
        String descTipoAcesso = rs.getString("desc_tipoacesso");

        Timestamp temp1 = rs.getTimestamp("data_criacao");
        LocalDateTime dataCriacao = (temp1 == null ? null : temp1.toLocalDateTime());

        boolean status = rs.getBoolean("status");
        String nomeFabrica = rs.getString("nome_fabrica");

        // Instância do DTO
        usuario = new UsuarioDTO(id, nome, emailGerente, genero, dataNascimento, email, cargo, tipoAcesso, descTipoAcesso, dataCriacao, status, nomeFabrica);
      }
    }

    // Retorna usuário
    conn.commit();
    return usuario;
  }

  // === UPDATE ===
  public void atualizar(AtualizacaoUsuarioDTO original, AtualizacaoUsuarioDTO alterado) throws SQLException {
    // Variáveis
    String nome = alterado.getNome();
    String emailGerente = alterado.getEmailGerente();
    String genero = alterado.getGenero();
    String cargo = alterado.getCargo();
    String email = alterado.getEmail();
    TipoAcesso tipoAcesso = alterado.getTipoAcesso();
    boolean status = alterado.getStatus();
    Integer fkFabrica = alterado.getFkFabrica();

    // Construção do comando SQL dinâmico
    StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
    List<Object> valores = new ArrayList<>();

    if (!Objects.equals(nome, original.getNome())) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (!Objects.equals(emailGerente, original.getEmailGerente())){
        sql.append("id_gerente = (SELECT id FROM usuario WHERE email = ?), ");
        valores.add(emailGerente);
    }

    if (!Objects.equals(genero, original.getGenero())){
        sql.append("genero = ?, ");
        valores.add(genero);
    }

    if (!Objects.equals(cargo, original.getCargo())){
        sql.append("cargo = ?, ");
        valores.add(cargo);
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

    if (!Objects.equals(fkFabrica, original.getFkFabrica())) {
      sql.append("fk_fabrica = ?, ");
      valores.add(fkFabrica);
    }

    // Retorna vazio se nada foi alterado
    if (valores.isEmpty()) {
      return;
    }

    // Remoção da última ", "
    sql.setLength(sql.length() - 2);

    // Adiciona a cláusula WHERE
    sql.append(" WHERE email = ?");
    valores.add(email);

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
  public void remover(UUID id) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM usuario WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setObject(1, id);

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
