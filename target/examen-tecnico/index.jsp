<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Sistema de Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/login"/>
</c:if>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sistema de Productos</a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="${pageContext.request.contextPath}/productos">Productos</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/api/productos" target="_blank">API JSON</a>
            <span class="nav-link text-muted">Hola, ${sessionScope.nombreCompleto}</span>
            <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout">Salir</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <h4>Bienvenido, ${sessionScope.nombreCompleto}</h4>
    <p class="text-muted">Rol: <strong>${sessionScope.rol}</strong> &nbsp;|&nbsp; Session ID: <code>${pageContext.session.id}</code></p>

    <hr>

    <!-- Estadísticas vía AJAX -->
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title" id="totalProductos">-</h5>
                    <p class="card-text text-muted">Total Productos</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title" id="totalActivos">-</h5>
                    <p class="card-text text-muted">Activos</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title" id="totalCategorias">-</h5>
                    <p class="card-text text-muted">Categorías</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center">
                <div class="card-body">
                    <h5 class="card-title" id="totalStock">-</h5>
                    <p class="card-text text-muted">Stock Total</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Accesos rápidos -->
    <a href="${pageContext.request.contextPath}/productos" class="btn btn-primary me-2">Ver Productos</a>
    <a href="${pageContext.request.contextPath}/productos?accion=nuevo" class="btn btn-success me-2">Nuevo Producto</a>
    <a href="${pageContext.request.contextPath}/api/productos" target="_blank" class="btn btn-secondary">API JSON</a>

    <hr>

    <!-- Cookies activas -->
    <h6>Cookies activas</h6>
    <div id="cookiesList"><em>Cargando...</em></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Estadísticas vía AJAX (JavaScript + JSON)
    fetch('${pageContext.request.contextPath}/api/productos')
        .then(r => r.json())
        .then(data => {
            document.getElementById('totalProductos').textContent = data.length;
            document.getElementById('totalActivos').textContent = data.filter(p => p.activo).length;
            document.getElementById('totalCategorias').textContent = new Set(data.map(p => p.categoria)).size;
            document.getElementById('totalStock').textContent = data.reduce((s, p) => s + p.stock, 0);
        });

    // Mostrar cookies (JavaScript + Cookies)
    const cookies = document.cookie.split(';').map(c => c.trim()).filter(c => c);
    const div = document.getElementById('cookiesList');
    if (cookies.length === 0) {
        div.innerHTML = '<em>Sin cookies configuradas</em>';
    } else {
        div.innerHTML = cookies.map(c => '<code class="me-3">' + c + '</code>').join('<br>');
    }
</script>
</body>
</html>
