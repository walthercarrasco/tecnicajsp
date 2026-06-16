<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle: ${producto.nombre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-light bg-light border-bottom">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sistema de Productos</a>
        <div class="navbar-nav flex-row gap-3">
            <a class="nav-link" href="${pageContext.request.contextPath}/productos">Productos</a>
            <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout">Salir</a>
        </div>
    </div>
</nav>

<div class="container mt-4" style="max-width: 700px;">

    <h4>Detalle del Producto</h4>

    <table class="table table-bordered">
        <tr><th style="width:30%">ID</th>          <td>${producto.id}</td></tr>
        <tr><th>Nombre</th>       <td>${producto.nombre}</td></tr>
        <tr><th>Descripción</th>  <td>${producto.descripcion}</td></tr>
        <tr><th>Precio</th>       <td>$<fmt:formatNumber value="${producto.precio}" pattern="#,##0.00"/></td></tr>
        <tr><th>Stock</th>        <td>${producto.stock}</td></tr>
        <tr><th>Categoría</th>    <td>${producto.categoria}</td></tr>
        <tr><th>Estado</th>       <td>
            <span class="badge ${producto.activo ? 'bg-success' : 'bg-secondary'}">
                ${producto.activo ? 'Activo' : 'Inactivo'}
            </span>
        </td></tr>
        <tr><th>Creado</th>       <td>${producto.fechaCreacion}</td></tr>
        <tr><th>Actualizado</th>  <td>${producto.fechaActualizacion}</td></tr>
    </table>

    <a href="${pageContext.request.contextPath}/productos?accion=editar&id=${producto.id}"
       class="btn btn-warning btn-sm">Editar</a>
    <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary btn-sm ms-2">Volver</a>

    <hr>

    <!-- JSON / XML via AJAX -->
    <h5>Representación API</h5>
    <ul class="nav nav-tabs mb-2" id="apiTabs">
        <li class="nav-item">
            <button class="nav-link active" onclick="mostrar('json')">JSON</button>
        </li>
        <li class="nav-item">
            <button class="nav-link" onclick="mostrar('xml')">XML</button>
        </li>
    </ul>
    <pre class="code-preview" id="apiOutput">Cargando...</pre>

    <a href="${pageContext.request.contextPath}/api/productos?id=${producto.id}"
       target="_blank" class="btn btn-outline-secondary btn-sm mt-2">Abrir endpoint JSON</a>
</div>

<script>
    // AJAX para JSON y XML (JavaScript + JSON/XML)
    const ctx = '${pageContext.request.contextPath}';
    const id  = '${producto.id}';

    function mostrar(formato) {
        document.querySelectorAll('#apiTabs .nav-link').forEach(b => b.classList.remove('active'));
        event.target.classList.add('active');
        document.getElementById('apiOutput').textContent = 'Cargando...';

        const headers = formato === 'xml'
            ? { 'Accept': 'application/xml' }
            : { 'Accept': 'application/json' };

        fetch(ctx + '/api/productos?id=' + id, { headers })
            .then(r => formato === 'json' ? r.json() : r.text())
            .then(data => {
                document.getElementById('apiOutput').textContent =
                    formato === 'json' ? JSON.stringify(data, null, 2) : data;
            })
            .catch(err => {
                document.getElementById('apiOutput').textContent = 'Error: ' + err.message;
            });
    }

    // Cargar JSON al abrir la página
    mostrar('json');
</script>
</body>
</html>
