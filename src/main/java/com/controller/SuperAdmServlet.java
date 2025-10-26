package com.controller;

import com.dao.SuperAdmDAO;
import com.dto.SuperAdmDTO;
import com.exception.ExcecaoDeJSP;
import com.model.SuperAdm;
import com.utils.SenhaUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/area-restrita/superadms")
public class SuperAdmServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "jsp/superadms.jsp";
    private static final String PAGINA_CADASTRO = "jsp/cadastro-superadm.jsp";
    private static final String PAGINA_EDICAO = "jsp/editar-superadm.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String action = req.getParameter("action");
        action = (action == null ? "read" : action.trim());

        boolean erro = true;
        String destino = null;

        try {
            switch (action) {
                case "read" -> {
                    List<SuperAdmDTO> superAdms = getListaSuperAdms(req);
                    req.setAttribute("superAdms", superAdms);
                    destino = PAGINA_PRINCIPAL;
                }

                case "update" -> {
                    SuperAdmDTO superAdm = getInformacoesAlteraveis(req);
                    req.setAttribute("superAdm", superAdm);
                    destino = PAGINA_EDICAO;
                }

                case "create" -> destino = PAGINA_CADASTRO;
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
                case "create" -> registrarSuperAdm(req);
                case "update" -> atualizarSuperAdm(req);
                case "delete" -> removerSuperAdm(req);
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
    private void registrarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        String senhaOriginal = req.getParameter("senha");
        String senhaHash = SenhaUtils.hashear(senhaOriginal);

        String nome = req.getParameter("nome").trim();
        String cargo = req.getParameter("cargo").trim();
        String email = req.getParameter("email").trim();

        SuperAdm credenciais = new SuperAdm(null, nome, cargo, email, senhaHash);

        // Se a senha não tiver no mínimo 8 caracteres, lança uma exceção de JSP
        if (!senhaOriginal.matches(".{8,}")) {
            throw ExcecaoDeJSP.senhaCurta(8);
        }

        try (SuperAdmDAO dao = new SuperAdmDAO()) {
            // Verifica se o super adm não viola a chave UNIQUE de 'email' em 'super_adm'
            SuperAdmDTO teste = dao.pesquisarPorEmail(email);

            if (teste != null) {
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(credenciais);
        }
    }

    // === READ ===
    private List<SuperAdmDTO> getListaSuperAdms(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String campoFiltro = req.getParameter("campo_filtro");
        String campoSequencia = req.getParameter("campo_sequencia");
        String direcaoSequencia = req.getParameter("direcao_sequencia");
        String valorFiltro = req.getParameter("valor_filtro");

        try (SuperAdmDAO dao = new SuperAdmDAO()) {
            Object valorFiltroConvertido = dao.converterValor(campoFiltro, valorFiltro);

            return dao.listar(campoFiltro, valorFiltroConvertido, campoSequencia, direcaoSequencia);
        }
    }

    // === UPDATE ===
    private void atualizarSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {

        String nome = req.getParameter("nome").trim();
        String cargo = req.getParameter("cargo").trim();
        String email = req.getParameter("email").trim();
        String senhaAtual = req.getParameter("senha_atual").trim();
        String novaSenha = req.getParameter("nova_senha").trim();

        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        SuperAdm alterado = new SuperAdm(id, nome, cargo, email, novaSenha);

        try (SuperAdmDAO dao = new SuperAdmDAO()) {
            // Recupera as informações originais do banco
            SuperAdm original = dao.getCamposAlteraveis(id);

            // Verifica se as alterações não violam a chave UNIQUE de 'email' em 'super_adm'
            SuperAdmDTO teste = dao.pesquisarPorEmail(email);
            if (teste != null && teste.getId() != id) {
                throw ExcecaoDeJSP.emailDuplicado();
            }

            if (!novaSenha.isBlank()) {
                // Verifica se a nova senha tem no mínimo 8 caracteres
                if (!novaSenha.matches(".{8,}")) {
                    throw ExcecaoDeJSP.senhaCurta(8);
                }

                // Verifica se a senha inserida pelo usuário corresponde com a senha cadastrada no banco de dados
                if (!SenhaUtils.comparar(senhaAtual, original.getSenha())) {
                    throw ExcecaoDeJSP.senhaIncorreta();
                }

                // Faz o hash da senha antes de atualizar no banco de dados
                String novaSenhaHash = SenhaUtils.hashear(alterado.getSenha());
                alterado.setSenha(novaSenhaHash);
            }

            dao.atualizar(original, alterado);
        }
    }

    // === DELETE ===
    private void removerSuperAdm(HttpServletRequest req) throws SQLException, ClassNotFoundException {
        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        try (SuperAdmDAO dao = new SuperAdmDAO()) {
            dao.remover(id);
        }
    }

    // Resgata os dados do banco de dados que podem ser atualizados, utilizados no método UPDATE
    private SuperAdmDTO getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {

        String temp = req.getParameter("id").trim();
        int id = Integer.parseInt(temp);

        try (SuperAdmDAO dao = new SuperAdmDAO()) {
            return dao.pesquisarPorId(id);
        }
    }
}
