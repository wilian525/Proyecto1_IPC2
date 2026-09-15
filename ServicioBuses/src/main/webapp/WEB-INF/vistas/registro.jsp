<%-- 
    Document   : registro
    Created on : 13 sep 2026, 10:03:39 p.m.
    Author     : wilian
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear cuenta</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
         <main>

            <jsp:include page="/includes/header.jsp"/>

            <div class="container mt-4 mb-4">

                <div class="row justify-content-center">

                    <div class="col-md-8 col-lg-6">

                        <div class="card shadow-sm">

                            <div class="card-header text-center">

                                <h3 class="mb-1">
                                    <i class="bi bi-person-plus"></i>
                                    Crear cuenta
                                </h3>

                                <p class="text-muted mb-0">
                                    Regístrese para utilizar el sistema
                                </p>

                            </div>

                            <div class="card-body">

                                <c:if test="${not empty error}">

                                    <div class="alert alert-danger" role="alert">

                                        <i class="bi bi-exclamation-triangle-fill"></i>
                                        ${error}

                                    </div>

                                </c:if>

                                <c:if test="${not empty mensaje}">

                                    <div class="alert alert-success" role="alert">

                                        <i class="bi bi-check-circle-fill"></i>
                                        ${mensaje}

                                    </div>

                                </c:if>

                                <form action="${pageContext.servletContext.contextPath}/UsuarioServlet"
                                      method="POST">

                                    <input type="hidden"
                                           name="accion"
                                           value="registrar">


                                    <div class="mb-3">

                                        <label for="nombre" class="form-label">
                                            Nombre
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="nombre"
                                               name="nombre"
                                               placeholder="Ingrese su nombre"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="nit" class="form-label">
                                            NIT
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="nit"
                                               name="nit"
                                               placeholder="Ingrese su NIT"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="dpi" class="form-label">
                                            DPI
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="dpi"
                                               name="dpi"
                                               placeholder="Ingrese su DPI"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="telefono" class="form-label">
                                            Teléfono
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="telefono"
                                               name="telefono"
                                               placeholder="Ingrese su teléfono"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="direccion" class="form-label">
                                            Dirección
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="direccion"
                                               name="direccion"
                                               placeholder="Ingrese su dirección"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="username" class="form-label">
                                            Usuario
                                        </label>

                                        <div class="input-group">

                                            <span class="input-group-text">
                                                <i class="bi bi-person"></i>
                                            </span>

                                            <input type="text"
                                                   class="form-control"
                                                   id="username"
                                                   name="username"
                                                   placeholder="Ingrese su usuario"
                                                   required>

                                        </div>

                                    </div>


                                    <div class="mb-3">

                                        <label for="password" class="form-label">
                                            Contraseña
                                        </label>

                                        <div class="input-group">

                                            <span class="input-group-text">
                                                <i class="bi bi-lock"></i>
                                            </span>

                                            <input type="password"
                                                   class="form-control"
                                                   id="password"
                                                   name="password"
                                                   placeholder="Ingrese su contraseña"
                                                   required>

                                        </div>

                                    </div>


                                    <div class="d-grid mt-4">

                                        <button type="submit"
                                                class="btn btn-primary">

                                            <i class="bi bi-person-plus"></i>
                                            Crear cuenta

                                        </button>

                                    </div>

                                </form>

                            </div>

                            <div class="card-footer text-center">

                                <p class="mb-0">

                                    ¿Ya tienes una cuenta?

                                    <a href="${pageContext.servletContext.contextPath}/AutenticacionServlet">
                                        Iniciar sesión
                                    </a>

                                </p>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

            <jsp:include page="/includes/footer.jsp"/>

        </main>

    </body>
</html>
