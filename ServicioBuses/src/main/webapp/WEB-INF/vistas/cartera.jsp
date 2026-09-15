<%-- 
    Document   : cartera
    Created on : 13 sep 2026, 10:04:42 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mi cartera</title>
         <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main>

            <jsp:include page="/includes/header.jsp"/>
            <div class="container py-4">
                <div class="text-center mb-4">
                    <h1 class="fw-bold">
                        <i class="bi bi-wallet2"></i>
                        Mi Cartera Digital
                    </h1>
                    <p class="text-muted">
                        Consulta tu saldo y realiza una recarga.
                    </p>
                </div>
                
                <c:if test="${param.resultado == 'true'}">
                    <div class="alert alert-success alert-dismissible fade show"
                         role="alert">
                        <i class="bi bi-check-circle"></i>
                        La recarga se realizó correctamente.
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>
                    </div>
                </c:if>

                <c:if test="${param.resultado == 'false'}">
                    <div class="alert alert-danger alert-dismissible fade show"
                         role="alert">
                        <i class="bi bi-exclamation-triangle"></i>
                        No fue posible realizar la recarga.
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>
                    </div>
                </c:if>


                <c:choose>
                    <c:when test="${cartera != null}">
                        <div class="row justify-content-center">
                            <div class="col-12 col-md-8 col-lg-6">
                                <div class="card shadow-sm mb-4">
                                    <div class="card-header text-center">
                                        <h5 class="mb-0">
                                            <i class="bi bi-wallet2"></i>
                                            Saldo disponible
                                        </h5>
                                    </div>
                                    <div class="card-body text-center">
                                        <h2 class="fw-bold">
                                            Q ${cartera.saldo}
                                        </h2>
                                    </div>
                                </div>


                                <div class="card shadow-sm">
                                    <div class="card-header">
                                        <h5 class="mb-0">
                                            <i class="bi bi-plus-circle"></i>
                                            Recargar cartera
                                        </h5>
                                    </div>
                                    <div class="card-body">
                                        <form action="${pageContext.servletContext.contextPath}/UsuarioServlet"
                                              method="POST">
                                            <input type="hidden"
                                                   name="accion"
                                                   value="recarga">

                                            <div class="mb-3">
                                                <label for="monto"
                                                       class="form-label">
                                                    Monto a recargar
                                                </label>

                                                <div class="input-group">
                                                    <span class="input-group-text">
                                                        Q
                                                    </span>

                                                    <input type="number"
                                                           class="form-control"
                                                           id="monto"
                                                           name="monto"
                                                           min="0.01"
                                                           step="0.01"
                                                           placeholder="Ingrese el monto"
                                                           required>
                                                </div>
                                            </div>

                                            <div class="d-grid">
                                                <button type="submit"
                                                        class="btn btn-primary">
                                                    <i class="bi bi-plus-circle"></i>
                                                    Recargar saldo
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>

                    <c:otherwise>

                        <div class="alert alert-warning text-center">
                            <i class="bi bi-exclamation-triangle"></i>
                            No se encontró una cartera digital asociada
                            a tu cuenta.
                        </div>

                    </c:otherwise>
                </c:choose>

                <div class="text-center mt-4">
                    <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=inicio"
                       class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i>
                        Regresar al inicio
                    </a>
                </div>
            </div>
            <jsp:include page="/includes/footer.jsp"/>
        </main>
    </body>
</html>
