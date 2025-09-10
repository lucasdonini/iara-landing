package com.controller;

import com.dao.PlanosDAO;
import com.dto.PlanosDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet ("/area-restrita/planos/create-plano")
public class CreateReadPlanosServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Pegando atributos
        String nome = req.getParameter("nome");
        Double valor = Double.parseDouble(req.getParameter("valor"));
        String descricao = req.getParameter("descricao");

        //Instanciando objetos
        PlanosDTO planosDTO = new PlanosDTO(null,nome, valor, descricao);
        try (PlanosDAO planosDAO = new PlanosDAO()){
            //Chamando metodo de cadastro
            planosDAO.cadastrar(planosDTO);

            resp.sendRedirect(req.getContextPath() + "/area-restrita/index.jsp");
            //Redirecionando para area restrita
            
        } catch(SQLException sql){
            System.err.println("Erro no banco de dados");
            resp.sendRedirect(req.getContextPath()+"/erro.html");
        } catch(ClassNotFoundException cnfe){
            System.err.println("Driver do postgreSQL não encontrado");
            resp.sendRedirect(req.getContextPath()+"/erro.html");
        } catch(Throwable t){
            System.err.println("Erro não identificado");
            resp.sendRedirect(req.getContextPath()+"/erro.html");
        }
    }
}
