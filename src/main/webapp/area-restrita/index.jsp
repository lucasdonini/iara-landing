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
    <li><a href="${pageContext.request.contextPath}/area-restrita/logout">Logout</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/create-read-usuario">Usuários</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/create-read-superadm">Super Administradores</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/create-plano">Planos</a></li>
    <li><a href="${pageContext.request.contextPath}/area-restrita/fabricas?action=read">Fábricas</a></li>
  </ul>
</nav>
</body>

</html>