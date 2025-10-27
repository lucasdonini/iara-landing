<%@ page import="java.util.Map" %>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas"); 
  String erro = (String) request.getAttribute("erro");
  %>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/cadastro-pagamento.css">
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
        <h2>Cadastre um Pagamento!</h2>
        <form action="${pageContext.request.contextPath}/pagamentos" method="post" class="LoginForm">
          <input type="hidden" name="action" value="create">
          <select name="status">
            <option value="" selected>Selecione o status do Pagamento</option>
            <option value="true">Pagamento Realizado ✅</option>
            <option value="false">Pagamento Pendente ❌</option>
          </select>

          <input type="date" name="dataVencimento" data-placeholder="Selecione a Data de Vencimento">
          <input type="date" name="dataPagamento" data-placeholder="Selecione a Data do Pagamento">

          <select name="tipoPagamento">
            <option value="" selected>Selecione o tipo do Pagamento</option>
            <option value="debito">Débito</option>
            <option value="credito">Crédito</option>
            <option value="pix">PIX</option>
          </select>

          <select name="fkFabrica">
            <option value="" selected>Selecione o ID da Fábrica que o pagamento refere-se</option>

            <% for (int id : fabricas.keySet()) { %>
            <option value="<%= id %>">
              <%= fabricas.get(id) %>
            </option>
            <% } %>
          </select>
          <button type="submit">Cadastrar</button>
        </form>

      </div>
    </div>
  </main>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/javascript/script.js"></script>

  <%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>

</html>