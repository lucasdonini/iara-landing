package com.dao;

import com.dto.SuperAdmDTO;
import com.model.SuperAdm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SuperAdmDAO extends DAO {
  // Constantes
  public static final Map<String, String> camposFiltraveis = Map.of(
      "id", "Id",
      "nome", "Nome",
      "cargo", "Cargo",
      "email", "Email"
  );

  // Construtor
  public SuperAdmDAO() throws SQLException, ClassNotFoundException {
    super();
  }

  // Converter Valor
  public Object converterValor(String campo, String valor){
      if (valor == null || valor.isBlank()){
          return null;
      } else{
          return switch(campo){
              case "id" -> Integer.parseInt(valor);
              case "nome", "cargo", "email" -> valor;
              default -> throw new IllegalArgumentException();
          };
      }
  }

  // Outros Métodos

  // === CREATE ===
  public void cadastrar(SuperAdm credenciais) throws SQLException {
    // Variáveis
    String nome = credenciais.getNome();
    String email = credenciais.getEmail();
    String cargo = credenciais.getCargo();
    String senha = credenciais.getSenha();

    // Comando SQL
    String sql = "INSERT INTO super_adm (nome, email, cargo, senha) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variáveis do comando SQL
      pstmt.setString(1, nome);
      pstmt.setString(2, email);
      pstmt.setString(3, cargo);
      pstmt.setString(4, senha);

      // Cadastra o super adm no banco de dados
      pstmt.executeUpdate();

      // Efetuando transação
      conn.commit();

    } catch (SQLException e) {
      // Cancelando transação
      conn.rollback();
      throw e;
    }
  }

  // === READ ===
  public List<SuperAdmDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {
    // Lista de super adms
    List<SuperAdmDTO> superAdms = new ArrayList<>();

    // Comando SQL
    String sql = "SELECT id, nome, cargo, email FROM super_adm";

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
      //    Definindo variável do comando SQL
      if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
        pstmt.setObject(1, valorFiltro);
      }

      // Resgata do banco de dados a lista de super adms
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          // Variáveis
          int id = rs.getInt("id");
          String nome = rs.getString("nome");
          String cargo = rs.getString("cargo");
          String email = rs.getString("email");

          // Adicionando instância do DTO na lista de super adms
          superAdms.add(new SuperAdmDTO(id, nome, cargo, email));
        }
      }
    }

    // Retorna a lista de super adms
    return superAdms;
  }

  public SuperAdmDTO pesquisarPorId(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT nome, cargo, email FROM super_adm WHERE id = ?";

    // Objeto não instanciado de super adm
    SuperAdmDTO superAdm;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

       // Pesquisa super adm pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        String nome = rs.getString("nome");
        String cargo = rs.getString("cargo");
        String email = rs.getString("email");

        // Instância do DTO
        superAdm = new SuperAdmDTO(id, nome, cargo, email);
      }
    }

    // Retorna super adm
    return superAdm;
  }

  public SuperAdmDTO pesquisarPorEmail(String email) throws SQLException {
    // Comando SQL
    String sql = "SELECT id, nome, cargo FROM super_adm WHERE email = ?";

    //  Objeto não instanciado de super adm
    SuperAdmDTO superAdm;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setString(1, email);

      // Pesquisa super adm pelo email
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não encontrar retorna null
        if (!rs.next()) {
          return null;
        }

        // Variáveis
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String cargo = rs.getString("cargo");

        // Instância do DTO
        superAdm = new SuperAdmDTO(id, nome, cargo, email);
      }
    }

    // Retorna super adm
    return superAdm;
  }

  public SuperAdm getCamposAlteraveis(int id) throws SQLException {
    // Comando SQL
    String sql = "SELECT * FROM super_adm WHERE id = ?";

    // Objeto não instanciado de super adm
    SuperAdm superAdm;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);


      // Pesquisa super adm pelo ID
      try (ResultSet rs = pstmt.executeQuery()) {
        // Se não achar lança exceção
        if (!rs.next()) {
          throw new SQLException("Erro ao recuperar as informações do super adm");
        }

        // Variáveis
        String nome = rs.getString("nome");
        String cargo = rs.getString("cargo");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        // Instância do Model
        superAdm = new SuperAdm(id, nome, cargo, email, senha);
      }
    }

    // Retorna super adm
    return superAdm;
  }

  // === UPDATE ===
  public void atualizar(SuperAdm original, SuperAdm alterado) throws SQLException {
    // Variáveis
    int id = alterado.getId();
    String nome = alterado.getNome();
    String cargo = alterado.getCargo();
    String email = alterado.getEmail();
    String senha = alterado.getSenha();

    // Construção do comando SQL dinâmico
    StringBuilder sql = new StringBuilder("UPDATE super_adm SET ");
    List<Object> valores = new ArrayList<>();

    if (!Objects.equals(nome, original.getNome())) {
      sql.append("nome = ?, ");
      valores.add(nome);
    }

    if (!Objects.equals(cargo, original.getCargo())) {
      sql.append("cargo = ?, ");
      valores.add(cargo);
    }

    if (!Objects.equals(email, original.getEmail())) {
      sql.append("email = ?, ");
      valores.add(email);
    }

    if (senha != null && !senha.equals(original.getSenha()) && !senha.isBlank()) {
      sql.append("senha = ?, ");
      valores.add(senha);
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

      // Atualiza o super adm no banco de dados
      pstmt.executeUpdate();

      // Efetuando alteração
      conn.commit();

    } catch (SQLException e) {
      // Cancelando alteração
      conn.rollback();
      throw e;
    }
  }

  // === DELETE ===
  public void remover(int id) throws SQLException {
    // Comando SQL
    String sql = "DELETE FROM super_adm WHERE id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Definindo variável do comando SQL
      pstmt.setInt(1, id);

      // Deleta o super adm do banco de dados
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