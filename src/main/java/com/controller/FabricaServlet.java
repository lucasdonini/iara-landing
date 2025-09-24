package com.controller;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import com.dto.CadastroFabricaDTO;
import com.exception.ExcecaoDePagina;
import com.model.Endereco;
import com.model.Fabrica;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
// TODO: criar opções de filtragem para o READ

@WebServlet("/area-restrita/fabricas")
public class FabricaServlet extends HttpServlet {
  private static final String PAGINA_PRINCIPAL = "jsp/fabricas.jsp";
  private static final String PAGINA_CADASTRO = "jsp/cadastro-fabrica.jsp";
  private static final String PAGINA_EDICAO = "jsp/editar-fabrica.jsp";
  private static final String PAGINA_ERRO = "html/erro.html";
  private static final String ESSE_ENDPOINT = "area-restrita/fabricas";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action").trim();

    // Dados da resposta
    boolean erro = true;
    String destino = null;

    try {
      switch (action) {
        case "read" -> {
          List<Fabrica> fabricas = getListaFabricas();
          req.setAttribute("fabricas", fabricas);
          destino = PAGINA_PRINCIPAL;
        }

        case "update" -> {
          Fabrica fabrica = getInformacoesAlteraveis(req);
          req.setAttribute("infosFabrica", fabrica);
          destino = PAGINA_CADASTRO;
        }

        case "create" -> destino = PAGINA_CADASTRO;
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      erro = false;

    } catch (SQLException e) {
      // Se houver alguma exceção, registra no terminal
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona a request par a página jsp
    if (erro) {
      resp.sendRedirect(req.getContextPath() + '/' + PAGINA_ERRO);

    } else {
      RequestDispatcher rd = req.getRequestDispatcher(destino);
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action").trim();

    // Dados da resposta
    String destino = PAGINA_ERRO;

    try {
      // Faz a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarFabrica(req);
        case "update" -> atualizarFabrica(req);
        case "delete" -> removerFabrica(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      destino = ESSE_ENDPOINT + "?action=read";

    } catch (ExcecaoDePagina e) {
      req.setAttribute("erro", e.getMessage());
      doGet(req, resp);
      return;

    } catch (SQLException e) {
      // Se houver alguma exceção, registra no terminal
      System.err.println("Erro ao executar operação no banco:");
      e.printStackTrace(System.err);

    } catch (ClassNotFoundException e) {
      System.err.println("Falha ao carregar o driver postgresql:");
      e.printStackTrace(System.err);

    } catch (Throwable e) {
      System.err.println("Erro inesperado:");
      e.printStackTrace(System.err);
    }

    // Redireciona para a página de destino
    resp.sendRedirect(req.getContextPath() + '/' + destino);
  }

  private void registrarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    // --- Endereço ---
    String temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String cep = req.getParameter("cep").trim();
    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();

    Endereco endereco = new Endereco(null, cep, numero, rua, complemento);

    // --- Fábrica ---
    String nome = req.getParameter("nome").trim();
    String cnpj = req.getParameter("cnpj").trim();
    String email = req.getParameter("email").trim();
    String empresa = req.getParameter("empresa").trim();
    String ramo = req.getParameter("ramo").trim();

    CadastroFabricaDTO credenciais = new CadastroFabricaDTO(nome, cnpj, email, empresa, ramo, 0);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Cadastra o endereço, se necessário, e recupera o id
      Integer id = eDao.getIdEndereco(endereco.getCep(), endereco.getNumero());
      if (id == null) {
        id = eDao.cadastrar(endereco);
      }

      // Verifica se o cadastro não viola a chave UNIQUE de cnpj
      Fabrica teste = fDao.getFabricaByCnpj(cnpj);
      if (teste != null) {
        throw ExcecaoDePagina.cnpjDuplicado();
      }

      // setta o id do endereco nas credenciais da fabrica
      credenciais.setFkEndereco(id);

      // Cadastra o usuário
      fDao.cadastrar(credenciais);
    }
  }

  private void atualizarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    // --- Endereço ---
    String temp = req.getParameter("id_endereco").trim();
    int idEndereco = Integer.parseInt(temp);

    String cep = req.getParameter("cep").trim();

    temp = req.getParameter("numero").trim();
    int numero = Integer.parseInt(temp);

    String rua = req.getParameter("logradouro").trim();
    String complemento = req.getParameter("complemento").trim();

    Endereco endereco = new Endereco(idEndereco, cep, numero, rua, complemento);

    // --- Fabrica ---
    temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    String nome = req.getParameter("nome").trim();
    String cnpj = req.getParameter("cnpj").trim();

    temp = req.getParameter("status").trim();
    boolean status = Boolean.parseBoolean(temp);

    String email = req.getParameter("email").trim();
    String nomeEmpresa = req.getParameter("nome_empresa").trim();
    String ramo = req.getParameter("ramo").trim();

    Fabrica alterado = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco
      Fabrica original = fDao.getFabricaById(idFabrica);

      // Verifica se as alterações não violam a chave UNIQUE de cnpj
      Fabrica teste = fDao.getFabricaByCnpj(cnpj);
      if (teste != null && teste.getId() != idFabrica) {
        throw ExcecaoDePagina.cnpjDuplicado();
      }

      // Salva as informações no banco
      fDao.atualizar(original, alterado);
      eDao.atualizar(original.getEndereco(), alterado.getEndereco());
    }
  }

  private Fabrica getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO dao = new FabricaDAO()) {
      // Recupera os dados originais da fábrica e retorna
      return dao.getFabricaById(idFabrica);
    }
  }

  private List<Fabrica> getListaFabricas() throws SQLException, ClassNotFoundException {
    try (FabricaDAO dao = new FabricaDAO()) {
      return dao.listarFabricas();
    }
  }

  private void removerFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id_fabrica").trim();
    int idFabrica = Integer.parseInt(temp);

    try (FabricaDAO dao = new FabricaDAO()) {
      // Deleta a fábrica
      dao.remover(idFabrica);
    }
  }
}
