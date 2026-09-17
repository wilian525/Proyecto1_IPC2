<%-- 
    Document   : inicio
    Created on : 13 sep 2026, 10:03:47 p.m.
    Author     : wilian
--%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio- Gestion de Flota </title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main> 
            <jsp:include page="/includes/header.jsp"/>
            <link rel="stylesheet"
                  href="${pageContext.servletContext.contextPath}/resources/SCSS/SCSS.CSS">

            <!-- TÍTULO DE LA EMPRESA -->

            <div class="container text-center mt-4">
                <h1>
                    <i class="bi bi-bus-front-fill"></i>
                    Gestión de Flota de Buses
                </h1>
                <p class="lead">
                    Sistema de gestión y administración de transporte
                </p>

            </div>

            <!-- CONTENIDO PARA CUALQUIER USUARIO LOGUEADO -->

            <div class="container mt-4">
                <c:if test="${not empty usuario}">
                    <div class="card">
                        <div class="card-body text-center">
                            <h3>
                                Bienvenido, ${usuario.nombre}
                            </h3>
                            <p class="text-muted">
                                Seleccione una opción para continuar.
                            </p>
                            <div class="d-flex justify-content-center gap-2 flex-wrap">
                                <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=perfil"
                                   class="btn btn-primary">
                                    <i class="bi bi-person"></i>
                                    Mi perfil
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=cartera"
                                   class="btn btn-primary">
                                    <i class="bi bi-wallet2"></i>
                                    Mi cartera
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/AutenticacionServlet?accion=logout"
                                   class="btn btn-danger">
                                    <i class="bi bi-box-arrow-right"></i>
                                    Cerrar sesión
                                </a>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>

            <!-- OPCIONES SEGÚN EL ROL -->

            <div class="container mt-4"> 

                <c:choose>

                    <c:when test="${rol == 'ADMIN_SISTEMA'}">
                        <div class="card">
                            <div class="card-header">
                                <h4>
                                    <i class="bi bi-gear"></i>
                                    Administración del sistema
                                </h4>
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.servletContext.contextPath}/AdminSistemaServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-people"></i>
                                    Administración
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/ReporteSistemaServlet"
                                   class="btn btn-secondary">
                                    <i class="bi bi-bar-chart"></i>
                                    Reportes del sistema
                                </a>
                                <a href="${pageContext.request.contextPath}/AdminSistemaServlet?accion=configuracion"
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-speedometer2"></i>
                                    Depreciación
                                </a>
                                <a href="${pageContext.request.contextPath}/AdminSistemaServlet?accion=usuarios"
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-people"></i>
                                    Usuarios
                                </a>
                            </div>
                        </div>
                    </c:when>


                    <c:when test="${rol == 'ADMIN_SUCURSAL'}">

                        <div class="card">
                            <div class="card-header">
                                <h4>
                                    <i class="bi bi-building"></i>
                                    Administración de sucursal
                                </h4>
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.servletContext.contextPath}/FlotaServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-bus-front"></i>
                                    Flota
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/ViajesServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-signpost-2"></i>
                                    Viajes
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-calendar-event"></i>
                                    Alquileres
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/ReporteSucursalServlet"
                                   class="btn btn-secondary">
                                    <i class="bi bi-bar-chart"></i>
                                    Reportes
                                </a>
                            </div>
                        </div>
                    </c:when>



                    <c:otherwise>
                        <div class="card">
                            <div class="card-header">
                                <h4>
                                    <i class="bi bi-person"></i>
                                    Servicios disponibles
                                </h4>
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.servletContext.contextPath}/BoletoServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-ticket-perforated"></i>
                                    Boletos
                                </a>
                                <a href="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                   class="btn btn-primary">
                                    <i class="bi bi-bus-front"></i>
                                    Alquileres
                                </a>
                            </div>
                        </div>

                    </c:otherwise>
                </c:choose>
            </div>
            <jsp:include page="/includes/footer.jsp"/>
        </main> 
    </body>
</html>
