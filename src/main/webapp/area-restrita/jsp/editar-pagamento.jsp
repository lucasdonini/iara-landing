<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
    Pagamento pagamento = (Pagamento) request.getAttribute("pagamento");
    String erro = (String) request.getAttribute("erro");
%>

<html lang="pt-BR">

<head>
    <title>Editar Pagamento | Área Restrita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-pagamento.css">
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos" class="btn-sair">Cancelar</a>

<main class="login-container">
    <img src="${pageContext.request.contextPath}/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo"
         class="bg-particles">

    <div class="left-side">
        <img src="${pageContext.request.contextPath}/assets/Cadastro/iara-direita_1-removebg-preview%201.png"
             alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png"
                 alt="Logo IARA" class="logo">
            <h2>Editar Pagamento</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post">
                <div id="divisao">
                    <div id="coluna1">
                        <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                        <input type="hidden" name="action" value="update">

                        <label for="valor_pago">Valor Pago:</label>
                        <input name="valor_pago" id="valor_pago" value="<%= pagamento.getValor() %>"
                               type="number"
                               placeholder="Digite o novo valor"
                               step="0.01"
                               min="0"
                               required
                        >

                        <label for="status">Status:</label>
                        <select id="status" name="status" required>
                            <option value="true" <%= pagamento.getStatus() ? "selected" : "" %>>
                                Pagamento realizado ✅
                            </option>
                            <option value="false" <%= !pagamento.getStatus() ? "selected" : "" %>>
                                Pagamento pendente ❌
                            </option>
                        </select>

                        <%
                            String dtInicioStr = pagamento.getDataInicio() == null ? ""
                                    : pagamento.getDataInicio().toLocalDate().toString();
                        %>
                        <label for="data_inicio">Data de Ínicio:</label>
                        <input name="data_inicio" id="data_inicio" value="<%= dtInicioStr %>"
                               type="date"
                               data-placeholder="Selecione a Data de Início"
                        >

                        <label for="data_vencimento">Data de Vencimento:</label>
                        <input name="data_vencimento" id="data_vencimento" value="<%= pagamento.getDataVencimento() %>"
                               type="date"
                               data-placeholder="Selecione a Data de Vencimento"
                               required
                        >
                    </div>

                    <div id="coluna2">
                        <%
                            String dtPagtoStr = pagamento.getDataPagamento() == null ? ""
                                    : pagamento.getDataPagamento().toLocalDate().toString();
                        %>
                        <label for="data_pagamento">Data de Pagamento:</label>
                        <input name="data_pagamento" id="data_pagamento" value="<%= dtPagtoStr %>"
                               type="date"
                               data-placeholder="Selecione a Data de Pagamento"
                        >

                        <label for="metodo_pagamento">Método de Pagamento:</label>
                        <select id="metodo_pagamento" name="metodo_pagamento" required>
                            <% for (MetodoPagamento m : MetodoPagamento.values()) { %>
                            <option value="<%= m.getId() %>" <%= m.equals(pagamento.getMetodoPagamento()) ? "selected" : "" %>>
                                <%= m.toString() %>
                            </option>
                            <% } %>
                        </select>

                        <label for="fk_fabrica">Fábrica:</label>
                        <select id="fk_fabrica" name="fk_fabrica" required>
                            <% for (int id : fabricas.keySet()) { %>
                            <option value="<%= id %>" <%=id == pagamento.getFkFabrica() ? "selected" : "" %>>
                                <%= fabricas.get(id) %>
                            </option>
                            <% } %>
                        </select>

                        <label for="fk_plano">Plano:</label>
                        <select id="fk_plano" name="fk_plano" required>
                            <% for (int id : planos.keySet()) { %>
                            <option value="<%= id %>" <%= id == pagamento.getFkPlano() ? "selected" : "" %>>
                                <%= planos.get(id) %>
                            </option>
                            <% } %>
                        </select>
                    </div>
                </div>


                <button type="submit">Salvar</button>
            </form>

            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>
        </div>
    </div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/script.js"></script>
</body>

</html>