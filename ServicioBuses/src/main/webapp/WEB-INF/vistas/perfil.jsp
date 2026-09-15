<%-- 
    Document   : perfil
    Created on : 13 sep 2026, 10:04:06 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mi perfil</title>
         <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
         <main>

            <jsp:include page="/includes/header.jsp"/>

            <div class="container py-4">

                <div class="text-center mb-4">
                    <h1 class="fw-bold">
                        <i class="bi bi-person-circle"></i>
                        Mi Perfil
                    </h1>

                    <p class="text-muted">
                        Consulta y actualiza tus datos personales
                    </p>
                </div>


                <c:if test="${param.resultado == 'true'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle"></i>
                        Los datos de tu perfil fueron actualizados correctamente.
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>
                    </div>
                </c:if>


                <c:if test="${param.resultado == 'false'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle"></i>
                        No fue posible actualizar los datos del perfil.
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>
                    </div>
                </c:if>


                <div class="row justify-content-center">

                    <div class="col-12 col-lg-8">

                        <div class="card shadow-sm">

                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="bi bi-person-vcard"></i>
                                    Información personal
                                </h5>
                            </div>

                            <div class="card-body">

                                <form action="${pageContext.servletContext.contextPath}/UsuarioServlet"
                                      method="POST">

                                    <input type="hidden"
                                           name="accion"
                                           value="actualizarPerfil">


                                    <div class="mb-3">

                                        <label for="nombre" class="form-label">
                                            Nombre completo
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="nombre"
                                               name="nombre"
                                               value="${perfil.nombre}"
                                               required>

                                    </div>


                                    <div class="row">

                                        <div class="col-md-6 mb-3">

                                            <label for="nit" class="form-label">
                                                NIT
                                            </label>

                                            <input type="text"
                                                   class="form-control"
                                                   id="nit"
                                                   name="nit"
                                                   value="${perfil.nit}"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label for="dpi" class="form-label">
                                                DPI
                                            </label>

                                            <input type="text"
                                                   class="form-control"
                                                   id="dpi"
                                                   name="dpi"
                                                   value="${perfil.dpi}"
                                                   required>

                                        </div>

                                    </div>


                                    <div class="mb-3">

                                        <label for="telefono" class="form-label">
                                            Teléfono
                                        </label>

                                        <input type="tel"
                                               class="form-control"
                                               id="telefono"
                                               name="telefono"
                                               value="${perfil.telefono}"
                                               required>

                                    </div>


                                    <div class="mb-3">

                                        <label for="direccion" class="form-label">
                                            Dirección
                                        </label>

                                        <textarea class="form-control"
                                                  id="direccion"
                                                  name="direccion"
                                                  rows="3"
                                                  required>${perfil.direccion}</textarea>

                                    </div>


                                    <div class="d-flex justify-content-between mt-4">

                                        <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=inicio"
                                           class="btn btn-secondary">

                                            <i class="bi bi-arrow-left"></i>
                                            Regresar
                                        </a>


                                        <button type="submit"
                                                class="btn btn-primary">

                                            <i class="bi bi-save"></i>
                                            Guardar cambios

                                        </button>

                                    </div>

                                </form>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

            <jsp:include page="/includes/footer.jsp"/>

        </main>
    </body>
</html>
