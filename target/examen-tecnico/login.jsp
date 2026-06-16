<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema de Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<div class="login-box">
    <h4 class="mb-3">Iniciar Sesión</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="POST">
        <div class="mb-3">
            <label for="username" class="form-label">Usuario</label>
            <input type="text" class="form-control" id="username" name="username"
                   value="${not empty username ? username : usuarioRecordado}" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Contraseña</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="recordar" name="recordar"
                   ${not empty usuarioRecordado ? 'checked' : ''}>
            <label class="form-check-label" for="recordar">Recordar usuario (cookie)</label>
        </div>
        <button type="submit" class="btn btn-primary w-100">Entrar</button>
    </form>

    <hr>
    <small class="text-muted">Credenciales: <strong>admin / admin123</strong> &nbsp;|&nbsp; user / user123</small>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Validación básica con JavaScript
    document.querySelector('form').addEventListener('submit', function(e) {
        const u = document.getElementById('username').value.trim();
        const p = document.getElementById('password').value.trim();
        if (u.length < 3 || p.length < 3) {
            e.preventDefault();
            alert('Usuario y contraseña deben tener al menos 3 caracteres.');
        }
    });
</script>
</body>
</html>
