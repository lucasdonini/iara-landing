<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  
  <style>
      nav {
          display: flex;
          flex-direction: column;
      }
  </style>
</head>

<body>
<h1>Área Restrita</h1>
<nav>
  <ul>
    <li><a href="${pageContext.request.contextPath}/index.html">Página inicial</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/usuarios?action=read">Usuários</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/superadms?action=read">Super Administradores</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/planos?action=read">Planos</a></li>
    <li><a href="${pageContext.request.contextPath}/fabricas?action=read">Fábricas</a></li>
    <li><a href="${pageContext.request.contextPath}/pagamentos?action=read">Pagamentos</a></li>
  </ul>
</nav>
<form action="${pageContext.request.contextPath}/login-handler" method="post">
  <input type="hidden" name="action" value="logout">
  <button type="submit">Logout</button>
</form>
</body>

</html>