<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.dao.FabricaDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Pagamento pagamento = (Pagamento) request.getAttribute("pagamento");
  Map<String, String> tiposPagamento = Pagamento.tiposPagamento;
  String erro = (String) request.getAttribute("erro");
%>

<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/editar-pagamento.css">
</head>

<body>
  <a href="${pageContext.request.contextPath}/superadms?action=read" class="btn-sair">Cancelar</a>

  <main class="login-container">
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
      <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
      <div class="login-box">
        <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
        <h2>Editar Pagamento - ID: <%= pagamento.getId() %>
        </h2>

        <form action="${pageContext.request.contextPath}/pagamentos" method="post">
          <input type="hidden" name="id" value="<%= pagamento.getId() %>">
          <input type="hidden" name="action" value="update">

          <input type="number" placeholder="Digite o novo valor" name="valorPago" value="<%= pagamento.getValor() %>">

          <select name="status">
            <option value=""selected>Selecione o status do Pagamento</option>
            <option value="true" <%=pagamento.getStatus() ? "selected" : "" %>Pagamento realizado ✅</option>
            <option value="false" <%=!pagamento.getStatus() ? "selected" : "" %>Pagamento pendente ❌</option>
          </select>

          <input type="date" name="dataVencimento" data-placeholder="Selecione a Data de Vencimento" value="<%= pagamento.getDataVencimento() %>">

          <input type="date" name="dataPagamento" data-placeholder="Selecione a Data de Pagamento" value="<%= pagamento.getDataPagamento() %>">

          <select name="tipoPagamento">
            <option value="" selected>Selecione o tipo de Pagamento</option>
            <% for (String key : Pagamento.tiposPagamento.keySet()) { %>
              <option value="<%= key %>" <%=pagamento.getTipoPagamento().equals(key) ? "selected" : "" %>>
                <%= Pagamento.tiposPagamento.get(key) %>
              </option>
              <% } %>
          </select>

          <select name="fkFabrica">
            <option value="" selected>Selecione o ID da Fábrica que o pagamento refere-se</option>
            <% for (int id : fabricas.keySet()) { %>
              <option value="<%= id %>" <%=id==pagamento.getIdFabrica() ? "selected" : "" %>>
                <%= fabricas.get(id) %>
              </option>
              <% } %>
          </select>
          <button type="submit">Salvar</button>
        </form>
      </div>
    </div>
  </main>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/javascript/script.js"></script>

  <% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>

</body>

</html>