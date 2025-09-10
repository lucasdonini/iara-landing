package com.controller.fabrica;

import com.dao.EnderecoDAO;
import com.dao.FabricaDAO;
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

;

@WebServlet("/area-restrita/update-fabrica")
public class UpdateFabricaServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // Dados da request
    String temp = req.getParameter("id");
    int idFabrica = Integer.parseInt(temp);

    // Dados da resposta
    Fabrica fabrica;
    Endereco endereco;
    boolean erro = true;

    try (FabricaDAO dao = new FabricaDAO()) {
      // Recupera os dados originais da fábrica para display
      fabrica = dao.getFabricaById(idFabrica);

      // Prepara o atributo passado apara o .jsp
      req.setAttribute("infosFabrica", fabrica);

      // Setta erro como false
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

    if (erro) {
      resp.sendRedirect(req.getContextPath() + "/erro.html");
    } else {
      RequestDispatcher rd = req.getRequestDispatcher("fabrica/editar.jsp");
      rd.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    // Dados da resposta
    String destino = "/erro.html";

    try (FabricaDAO fDao = new FabricaDAO(); EnderecoDAO eDao = new EnderecoDAO()) {
      // Recupera as informações originais do banco
      Fabrica original = fDao.getFabricaById(idFabrica);

      // Salva as informações no banco
      fDao.atualizar(original, alterado);
      eDao.atualizar(original.getEndereco(), alterado.getEndereco());

      // Setta o destino para a página de display
      destino = "/area-restrita/create-read-fabrica";

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

    resp.sendRedirect(req.getContextPath() + destino);
  }
}
