<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty sessionScope.usuario}">
        <c:redirect url="/login"/>
    </c:when>
    <c:otherwise>
        <c:redirect url="/productos"/>
    </c:otherwise>
</c:choose>
