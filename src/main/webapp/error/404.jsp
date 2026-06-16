<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Página no encontrada</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>

<div class="login-container">
    <div class="login-card text-center slide-up">
        <div style="font-size: 5rem; color: var(--primary);">
            <i class="bi bi-emoji-frown"></i>
        </div>
        <h1 class="text-white fw-bold mt-3">404</h1>
        <p style="color: rgba(255,255,255,0.7);">La página que buscas no existe o ha sido movida.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-3">
            <i class="bi bi-house me-1"></i>Volver al Inicio
        </a>
    </div>
</div>

</body>
</html>
