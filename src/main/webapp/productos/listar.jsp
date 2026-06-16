<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<!-- Navbar Simplificado -->
<nav class="navbar navbar-light bg-light border-bottom">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sistema de Productos</a>
        <div class="navbar-nav flex-row gap-3 align-items-center">
            <span class="text-muted">Hola, <strong>${sessionScope.usuario}</strong></span>
            <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/logout">Salir</a>
        </div>
    </div>
</nav>

<div class="container mt-4">

    <!-- Mensajes flash -->
    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-${sessionScope.tipoMensaje} alert-dismissible fade show" role="alert">
            ${sessionScope.mensaje}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("mensaje"); session.removeAttribute("tipoMensaje"); %>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Encabezado -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">Listado de Productos</h4>
        <a href="${pageContext.request.contextPath}/productos?accion=nuevo" class="btn btn-success btn-sm">+ Nuevo Producto</a>
    </div>

    <!-- Tabla CRUD -->
    <table class="table table-bordered table-hover table-sm">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty productos}">
                    <c:forEach var="p" items="${productos}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nombre}</td>
                            <td>${p.categoria}</td>
                            <td>$<fmt:formatNumber value="${p.precio}" pattern="#,##0.00"/></td>
                            <td>${p.stock}</td>
                            <td>
                                <span class="badge ${p.activo ? 'bg-success' : 'bg-secondary'}">
                                    ${p.activo ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/productos?accion=editar&id=${p.id}"
                                   class="btn btn-warning btn-sm">Editar</a>
                                <a href="${pageContext.request.contextPath}/productos?accion=eliminar&id=${p.id}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('¿Eliminar ${p.nombre}?')">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" class="text-center text-muted">No hay productos.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <small class="text-muted">Total: ${productos.size()} producto(s)</small>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
