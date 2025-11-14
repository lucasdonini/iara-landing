package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dao.FabricaDAO;
import com.dao.UsuarioDAO;
import com.dto.AtualizacaoUsuarioDTO;
import com.dto.CadastroUsuarioDTO;
import com.dto.UsuarioDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Genero;
import com.model.TipoAcesso;
import com.utils.SenhaUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/area-restrita/usuarios")
public class UsuarioServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "jsp/usuarios.jsp";
    private static final String PAGINA_CADASTRO = "jsp/cadastro-usuario.jsp";
    private static final String PAGINA_EDICAO = "jsp/editar-usuario.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        action = (action == null ? "read" : action.trim());

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> {
                    List<UsuarioDTO> usuarios = getListaUsuarios(req);
                    Map<Integer, String> fabricas = getMapFabricas();
                    List<String> emailGerentes = getListaEmailsGerentes();

                    req.setAttribute("usuarios", usuarios);
                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("emailGerentes", emailGerentes);
                    destino = PAGINA_PRINCIPAL;
                }

                case "update" -> {
                    AtualizacaoUsuarioDTO usuario = getInformacoesAlteraveis(req);
                    Map<Integer, String> fabricas = getMapFabricas();
                    List<String> emailGerentes = getListaEmailsGerentes();

                    req.setAttribute("usuario", usuario);
                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("emailGerentes", emailGerentes);
                    destino = PAGINA_EDICAO;
                }

                case "create" -> {
                    Map<Integer, String> fabricas = getMapFabricas();
                    List<String> emailGerentes = getListaEmailsGerentes();

                    req.setAttribute("fabricas", fabricas);
                    req.setAttribute("emailGerentes", emailGerentes);
                    destino = PAGINA_CADASTRO;
                }

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            erro = false;

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (ClassNotFoundException e) {
            System.err.println("Falha ao carregar o driver postgresql:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            resp.sendRedirect(req.getContextPath() + PAGINA_ERRO);

        } else {
            req.getRequestDispatcher(destino).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action").trim();

        String destino = PAGINA_ERRO;

        try {
            switch (action) {
                case "create" -> registrarUsuario(req);
                case "update" -> atualizarUsuario(req);
                case "delete" -> removerUsuario(req);
                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = req.getServletPath();

        }
        // Se houver alguma exceção de JSP, aciona o método doGet
        catch (ExcecaoDeJSP e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
            return;

        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);

        } catch (ClassNotFoundException e) {
            System.err.println("Falha ao carregar o driver postgresql:");
            e.printStackTrace(System.err);

        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        resp.sendRedirect(req.getContextPath() + destino);
    }

    // === CREATE ===
    private void registrarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

        String nome = req.getParameter("nome").trim();
        String emailGerente = req.getParameter("email_gerentes").trim();
        String genero = req.getParameter("genero").trim();

        String temp = req.getParameter("data_nascimento").trim();
        LocalDate dataNascimento = LocalDate.parse(temp);

        String cargo = req.getParameter("cargo").trim();
        String email = req.getParameter("email").trim();

        String senhaOriginal = req.getParameter("senha");
        String hashSenha = SenhaUtils.hashear(senhaOriginal);

        temp = req.getParameter("fk_fabrica");
        // Verifica se fábrica foi preenchida
        if (temp.isBlank()) {
            throw ExcecaoDeJSP.campoNecessarioFaltante("fabrica");
        }
        Integer fkFabrica = Integer.parseInt(temp);

        CadastroUsuarioDTO credenciais = new CadastroUsuarioDTO(nome, emailGerente, genero, dataNascimento, cargo, email, hashSenha, fkFabrica);

        // Se a senha não tiver no mínimo 8 caracteres, lança uma exceção de JSP
        if (!senhaOriginal.matches(".{8,}")) {
            throw ExcecaoDeJSP.senhaCurta(8);
        }

        try (UsuarioDAO dao = new UsuarioDAO()) {
            // Verifica se o cadastro não viola a constraint UNIQUE de 'email' em 'usuario'
            if (dao.pesquisarPorEmail(email) != null) {
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(credenciais);
        }
    }

    // === READ ===
    private List<UsuarioDTO> getListaUsuarios(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String campoFiltro = req.getParameter("campo_filtro");
        String campoSequencia = req.getParameter("campo_sequencia");
        String direcaoSequencia = req.getParameter("direcao_sequencia");
        String valorFiltro = null;

        // Verifica se o campo é 'null' para realizar o switch. Se for 'null', o valor do filtro fica como nulo também
        if (campoFiltro != null){

            // Resgata um parâmetro diferente de acordo com o nome do campo de filtragem
            switch(campoFiltro) {
                case "gerente" -> valorFiltro = req.getParameter("valor_gerente");
                case "fk_fabrica" -> valorFiltro = req.getParameter("valor_fabrica");
                default -> valorFiltro = req.getParameter("valor_filtro");
            }
        }

        try (UsuarioDAO dao = new UsuarioDAO()) {
            Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

            return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);
        }
    }

    // === UPDATE ===
    private void atualizarUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

        UUID id = UUID.fromString(req.getParameter("id"));
        String nome = req.getParameter("nome").trim();
        String emailGerente = req.getParameter("email_gerente").trim();
        Genero genero = Genero.parse(req.getParameter("genero").trim());
        String cargo = req.getParameter("cargo").trim();
        String email = req.getParameter("email").trim();

        String temp = req.getParameter("status").trim();
        boolean status = Boolean.parseBoolean(temp);

        temp = req.getParameter("fk_fabrica").trim();
        // Verifica se fábrica foi preenchida
        if (temp.isBlank()) {
            throw ExcecaoDeJSP.campoNecessarioFaltante("fabrica");
        }
        int fkFabrica = Integer.parseInt(temp);

        temp = req.getParameter("nivel_acesso").trim();
        int nivelAcessoInt = Integer.parseInt(temp);
        TipoAcesso tipoAcesso = TipoAcesso.deNivel(nivelAcessoInt);

        AtualizacaoUsuarioDTO alteracoes = new AtualizacaoUsuarioDTO(id, nome, emailGerente, genero, cargo, email, tipoAcesso, status, fkFabrica);

        try (UsuarioDAO dao = new UsuarioDAO()) {
            // Busca no banco de dados o usuário original
            AtualizacaoUsuarioDTO original = dao.getCamposAlteraveis(id);

            // Verifica se as alterações não violam a chave UNIQUE de 'email' em 'usuario'
            UsuarioDTO teste = dao.pesquisarPorEmail(email);
            if (teste != null && !teste.getId().equals(id)) {
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alteracoes);
        }
    }

    // === DELETE ===
    private void removerUsuario(HttpServletRequest req) throws SQLException, ClassNotFoundException {

        UUID id = UUID.fromString(req.getParameter("id"));

        try (UsuarioDAO dao = new UsuarioDAO()) {
            dao.remover(id);
        }
    }

    // Método que lista os emails da view 'email_gerentes'
    private List<String> getListaEmailsGerentes() throws SQLException, ClassNotFoundException {
        try (UsuarioDAO dao = new UsuarioDAO()) {
            return dao.listarEmailGerentes();
        }
    }

    // HashMap das fábricas, onde a chave é o ID e o valor o nome da unidade
    private Map<Integer, String> getMapFabricas() throws SQLException, ClassNotFoundException {
        try (FabricaDAO dao = new FabricaDAO()) {
            return dao.getMapIdNome();
        }
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    private AtualizacaoUsuarioDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {

        UUID id = UUID.fromString(req.getParameter("id"));

        try (UsuarioDAO dao = new UsuarioDAO()) {
            return dao.getCamposAlteraveis(id);
        }
    }
}
