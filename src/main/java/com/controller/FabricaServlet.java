package com.controller;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
import com.dto.CadastroFabricaDTO;
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
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action");

    // Dados da resposta
    boolean erro = true;
    String destino = "";

    try {
      switch (action) {
        case "read" -> {
          List<Fabrica> fabricas = getListaFabricas();
          req.setAttribute("fabricas", fabricas);
          destino = "jsp/fabricas.jsp";
        }

        case "update" -> {
          Fabrica fabrica = getInformacoesAlteraveis(req);
          req.setAttribute("infosFabrica", fabrica);
          destino = "jsp/editar-fabrica.jsp";
        }

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
      resp.sendRedirect(req.getContextPath() + "/html/erro.html");

    } else {
      RequestDispatcher rd = req.getRequestDispatcher(destino);
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String action = req.getParameter("action");

    // Dados da resposta
    String destino = "/html/erro.html";

    try {
      // Faz a ação correspondente à escolha
      switch (action) {
        case "create" -> registrarFabrica(req);
        case "update" -> atualizarFabrica(req);
        case "delete" -> removerFabrica(req);
        default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
      }

      destino = "/area-restrita/fabricas?action=read";

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
    resp.sendRedirect(req.getContextPath() + destino);
  }

  private void registrarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("numero");
    Endereco endereco = new Endereco(
        null,
        req.getParameter("cep"),
        Integer.parseInt(temp),
        req.getParameter("logradouro"),
        req.getParameter("complemento")
    );
    CadastroFabricaDTO credenciais = new CadastroFabricaDTO(
        req.getParameter("nome"),
        req.getParameter("cnpj"),
        req.getParameter("email"),
        req.getParameter("empresa"),
        req.getParameter("ramo"),
        0
    );

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Cadastra o endereço, se necessário, e recupera o id
      Integer id = eDao.getIdEndereco(endereco.getCep(), endereco.getNumero());
      if (id == null) {
        id = eDao.cadastrar(endereco);
      }

      // setta o id do endereco nas credenciais da fabrica
      credenciais.setFkEndereco(id);

      // Cadastra o usuário
      fDao.cadastrar(credenciais);
    }
  }

  private void atualizarFabrica(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id_fabrica");
    int idFabrica = Integer.parseInt(temp);
    String nome = req.getParameter("nome");
    String cnpj = req.getParameter("cnpj");
    temp = req.getParameter("status");
    boolean status = Boolean.parseBoolean(temp);
    String email = req.getParameter("email");
    String nomeEmpresa = req.getParameter("nome_empresa");
    String ramo = req.getParameter("ramo");

    temp = req.getParameter("id_endereco");
    int idEndereco = Integer.parseInt(temp);
    String cep = req.getParameter("cep");
    temp = req.getParameter("numero");
    int numero = Integer.parseInt(temp);
    String rua = req.getParameter("logradouro");
    String complemento = req.getParameter("complemento");

    Endereco endereco = new Endereco(idEndereco, cep, numero, rua, complemento);
    Fabrica alterado = new Fabrica(idFabrica, nome, cnpj, status, email, nomeEmpresa, ramo, endereco);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco
      Fabrica original = fDao.getFabricaById(idFabrica);

      // Salva as informações no banco
      fDao.atualizar(original, alterado);
      eDao.atualizar(original.getEndereco(), alterado.getEndereco());
    }
  }

  private Fabrica getInformacoesAlteraveis(HttpServletRequest req) throws SQLException, ClassNotFoundException {
    // Dados da request
    String temp = req.getParameter("id");
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
    String temp = req.getParameter("id_fabrica");
    int idFabrica = Integer.parseInt(temp);

    temp = req.getParameter("id_endereco");
    int idEndereco = Integer.parseInt(temp);

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Deleta o superadm e o endereço
      eDao.remover(idEndereco);
      fDao.remover(idFabrica);
    }
  }
}
