<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${not empty producto ? 'Editar' : 'Nuevo'} Producto</title>
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

<div class="container mt-4" style="max-width: 600px;">

    <h4>${not empty producto ? 'Editar Producto' : 'Nuevo Producto'}</h4>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/productos" method="POST" id="formProducto">
        <c:if test="${not empty producto}">
            <input type="hidden" name="id" value="${producto.id}">
        </c:if>

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre *</label>
            <input type="text" class="form-control" id="nombre" name="nombre"
                   value="${producto.nombre}" required minlength="3" maxlength="100">
        </div>

        <div class="mb-3">
            <label for="descripcion" class="form-label">Descripción</label>
            <textarea class="form-control" id="descripcion" name="descripcion"
                      rows="3" maxlength="500">${producto.descripcion}</textarea>
        </div>

        <div class="row">
            <div class="col-md-4 mb-3">
                <label for="precio" class="form-label">Precio *</label>
                <input type="number" class="form-control" id="precio" name="precio"
                       value="${producto.precio}" required min="0.01" step="0.01">
            </div>
            <div class="col-md-4 mb-3">
                <label for="stock" class="form-label">Stock *</label>
                <input type="number" class="form-control" id="stock" name="stock"
                       value="${not empty producto ? producto.stock : '0'}" required min="0">
            </div>
            <div class="col-md-4 mb-3">
                <label for="categoria" class="form-label">Categoría *</label>
                <select class="form-select" id="categoria" name="categoria" required>
                    <option value="">Seleccionar...</option>
                    <option value="Electrónica"  ${producto.categoria == 'Electrónica'  ? 'selected' : ''}>Electrónica</option>
                    <option value="Periféricos"  ${producto.categoria == 'Periféricos'  ? 'selected' : ''}>Periféricos</option>
                    <option value="Audio"        ${producto.categoria == 'Audio'        ? 'selected' : ''}>Audio</option>
                    <option value="Almacenamiento" ${producto.categoria == 'Almacenamiento' ? 'selected' : ''}>Almacenamiento</option>
                    <option value="Mobiliario"   ${producto.categoria == 'Mobiliario'   ? 'selected' : ''}>Mobiliario</option>
                    <option value="Software"     ${producto.categoria == 'Software'     ? 'selected' : ''}>Software</option>
                    <option value="Accesorios"   ${producto.categoria == 'Accesorios'   ? 'selected' : ''}>Accesorios</option>
                </select>
            </div>
        </div>

        <div class="mb-3 form-check">
            <input class="form-check-input" type="checkbox" id="activo" name="activo"
                   ${empty producto || producto.activo ? 'checked' : ''}>
            <label class="form-check-label" for="activo">Producto activo</label>
        </div>

        <button type="submit" class="btn btn-primary">
            ${not empty producto ? 'Actualizar' : 'Crear'}
        </button>
        <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary ms-2">Cancelar</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Validación JavaScript del lado del cliente
    document.getElementById('formProducto').addEventListener('submit', function(e) {
        const nombre   = document.getElementById('nombre').value.trim();
        const precio   = parseFloat(document.getElementById('precio').value);
        const categoria = document.getElementById('categoria').value;
        let errores = [];

        if (nombre.length < 3)   errores.push('El nombre debe tener al menos 3 caracteres.');
        if (!precio || precio <= 0) errores.push('El precio debe ser mayor a 0.');
        if (!categoria)           errores.push('Selecciona una categoría.');

        if (errores.length > 0) {
            e.preventDefault();
            alert(errores.join('\n'));
        }
    });
</script>
</body>
</html>
