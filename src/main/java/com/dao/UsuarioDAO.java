package com.dao;

import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.model.Genero;
import com.model.TipoAcesso;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UsuarioDAO extends DAO {

    // Constante dos campos utilizados para ordenação e filtragem da listagem dos dados
    public static final Map<String, String> camposFiltraveis = Map.of(
            "nome", "Nome",
            "genero", "Gênero",
            "data_nascimento", "Data de Nascimento",
            "cargo", "Cargo",
            "email", "Email",
            "tipo_acesso", "Tipo de Acesso",
            "status", "Status",
            "data_criacao", "Data de Registro"
    );

    public UsuarioDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Método que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "tipo_acesso", "fk_fabrica" -> Integer.parseInt(valor);
                case "status" -> Boolean.parseBoolean(valor);
                case "data_nascimento" -> LocalDate.parse(valor);
                case "data_criacao" -> LocalDateTime.parse(valor);
                case "nome", "genero", "cargo", "email", "gerente" -> valor;
                default -> new IllegalArgumentException();
            };
        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    // === CREATE ===
    public void cadastrar(CadastroUsuarioDTO credenciais) throws SQLException {

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

        String sql = """
                INSERT INTO usuario(nome, id_gerente, genero, data_nascimento, cargo, email, senha, tipo_acesso, status, fk_fabrica, desc_tipoacesso)
                VALUES (?, (SELECT id FROM usuario WHERE email = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === READ ===
    public List<UsuarioDTO> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // Variável de apoio para verificar se a listagem será filtrada
        boolean temFiltro = true;

        List<UsuarioDTO> usuarios = new ArrayList<>();

        String sql = """
                  SELECT u.id, u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.email, u.cargo, u.tipo_acesso, u.data_criacao, u.status, f.nome_unidade as "nome_fabrica"
                  FROM usuario u
                  LEFT JOIN usuario g ON g.id = u.id_gerente
                  JOIN fabrica f ON f.id = u.fk_fabrica
                """;

        // Verificando campo da filtragem
        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
            sql += " WHERE u.%s = ?".formatted(campoFiltro);

        } else if ("gerente".equals(campoFiltro)) {
            sql += " WHERE g.email = ?";

        }  else if ("fk_fabrica".equals(campoFiltro)) {
            sql += "WHERE u.fk_fabrica = ?";
        }else {
            temFiltro = false;
        }

        // Verificando campo e direcao da ordenação
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
            sql += " ORDER BY u.%s %s".formatted(campoSequencia, direcaoSequencia);

        } else if ("gerente".equals(campoSequencia)) {
            sql += " ORDER BY g.email %s".formatted(direcaoSequencia);

        } else {
            sql += " ORDER BY u.id ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Verifica se tem filtro, se sim define a variável do comando SQL
            if (temFiltro) {
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String nome = rs.getString("nome");
                    String emailGerente = rs.getString("email_gerente");
                    Genero genero = Genero.parse(rs.getString("genero"));

                    Date dtNascimentoDate = rs.getDate("data_nascimento");
                    LocalDate dataNascimento = (dtNascimentoDate == null ? null : dtNascimentoDate.toLocalDate());

                    String cargo = rs.getString("cargo");
                    String email = rs.getString("email");
                    TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

                    Timestamp dtCriacaoTimestamp = rs.getTimestamp("data_criacao");
                    LocalDateTime dataCriacao = (dtCriacaoTimestamp == null ? null : dtCriacaoTimestamp.toLocalDateTime());

                    boolean status = rs.getBoolean("status");
                    String nomeFabrica = rs.getString("nome_fabrica");

                    usuarios.add(new UsuarioDTO(id, nome, emailGerente, genero, dataNascimento, email, cargo, tipoAcesso, dataCriacao, status, nomeFabrica));
                }
            }
        }

        conn.commit();
        return usuarios;
    }

    // === UPDATE ===
    public void atualizar(AtualizacaoUsuarioDTO original, AtualizacaoUsuarioDTO alterado) throws SQLException {
        String nome = alterado.getNome();
        String emailGerente = alterado.getEmailGerente();
        Genero genero = alterado.getGenero();
        String cargo = alterado.getCargo();
        String email = alterado.getEmail();
        TipoAcesso tipoAcesso = alterado.getTipoAcesso();
        boolean status = alterado.getStatus();
        Integer fkFabrica = alterado.getFkFabrica();

        StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
        List<Object> valores = new ArrayList<>();

        // Verifica se os valores atualizados são iguais aos registrados, se não atualiza no banco de dados
        if (!Objects.equals(nome, original.getNome())) {
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(emailGerente, original.getEmailGerente())) {
            sql.append("id_gerente = (SELECT id FROM usuario WHERE email = ?), ");
            valores.add(emailGerente);
        }

        if (!Objects.equals(genero, original.getGenero())) {
            sql.append("genero = ?, ");
            valores.add(genero.name().toLowerCase());
        }

        if (!Objects.equals(cargo, original.getCargo())) {
            sql.append("cargo = ?, ");
            valores.add(cargo);
        }

        if (!Objects.equals(email, original.getEmail())) {
            sql.append("email = ?, ");
            valores.add(email);
        }

        if (!Objects.equals(tipoAcesso, original.getTipoAcesso())) {
            sql.append("tipo_acesso = ?, desc_tipoacesso = ?, ");
            valores.add(tipoAcesso.nivel());
            valores.add(tipoAcesso.descricao());
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
        sql.append(" WHERE id = ?");
        valores.add(original.getId());

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // === DELETE ===
    public void remover(UUID id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Método que lista os emails da view 'email_gerentes'
    public List<String> listarEmailGerentes() throws SQLException {

        List<String> emailGerentes = new ArrayList<>();

        String sql = "SELECT email FROM email_gerentes";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emailGerentes.add(rs.getString("email"));
                }
            }
        }

        conn.commit();
        return emailGerentes;
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    public AtualizacaoUsuarioDTO getCamposAlteraveis(UUID id) throws SQLException {
        String sql = """
                    SELECT u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.cargo, u.email, u.tipo_acesso, u.status, u.fk_fabrica
                    FROM usuario u
                    LEFT JOIN usuario g ON g.id = u.id_gerente
                    WHERE u.id = ?
                """;

        AtualizacaoUsuarioDTO usuario;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o usuário lança exceção
                if (!rs.next()) {
                    throw new SQLException("Falha ao recuperar informações do usuário");
                }

                String nome = rs.getString("nome");
                String emailGerente = rs.getString("email_gerente");
                Genero genero = Genero.parse(rs.getString("genero"));
                String cargo = rs.getString("cargo");
                String email = rs.getString("email");
                TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));
                boolean status = rs.getBoolean("status");
                Integer fkFabrica = rs.getInt("fk_fabrica");


                usuario = new AtualizacaoUsuarioDTO(id, nome, emailGerente, genero, cargo, email, tipoAcesso, status, fkFabrica);
            }
        }

        conn.commit();
        return usuario;
    }

    // Resgata o usuário pelo email
    public UsuarioDTO pesquisarPorEmail(String email) throws SQLException {
        String sql = """
                    SELECT u.id, u.nome, g.email as "email_gerente", u.genero, u.data_nascimento, u.email, u.cargo, u.tipo_acesso, u.data_criacao, u.status, f.nome_unidade as "nome_fabrica"
                    FROM usuario u
                    JOIN usuario g ON g.id = u.id_gerente
                    JOIN fabrica f ON f.id = u.fk_fabrica
                    WHERE u.email = ?
                """;

        UsuarioDTO usuario;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encotrar o usuário retorna null
                if (!rs.next()) {
                    return null;
                }

                UUID id = UUID.fromString(rs.getString("id"));
                String nome = rs.getString("nome");
                String emailGerente = rs.getString("email_gerente");
                Genero genero = Genero.parse(rs.getString("genero"));

                Date temp = rs.getDate("data_nascimento");
                LocalDate dataNascimento = (temp == null ? null : temp.toLocalDate());

                String cargo = rs.getString("cargo");
                TipoAcesso tipoAcesso = TipoAcesso.deNivel(rs.getInt("tipo_acesso"));

                Timestamp temp1 = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = (temp1 == null ? null : temp1.toLocalDateTime());

                boolean status = rs.getBoolean("status");
                String nomeFabrica = rs.getString("nome_fabrica");

                usuario = new UsuarioDTO(id, nome, emailGerente, genero, dataNascimento, email, cargo, tipoAcesso, dataCriacao, status, nomeFabrica);
            }
        }

        conn.commit();
        return usuario;
    }
}
