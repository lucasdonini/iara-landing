package com.controller.planos;

import com.dao.PlanosDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet ("/area-restrita/delete-plano")
public class DeletePlanosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Dados da request
        int id = Integer.parseInt(req.getParameter("id"));

        // Dados da resposta
        String destino = "/erro.html";

        try (PlanosDAO dao = new PlanosDAO()) {
            // Deleta o superadm
            dao.remover(id);

            // Setta o destino para a visualização dos superadms
            destino = "/area-restrita/create-plano";

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
