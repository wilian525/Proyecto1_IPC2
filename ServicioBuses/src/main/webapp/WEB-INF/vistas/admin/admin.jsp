<%-- 
    Document   : admin
    Created on : 13 sep 2026, 10:08:13 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main>
            <div class="container py-5">

                <!-- ENCABEZADO -->

                <div class="text-center mb-5">

                    <h1 class="fw-bold">
                        Administración del Sistema
                    </h1>

                    <p class="text-muted">
                        Gestión de sucursales y administradores de sucursal
                    </p>

                </div>


                <!-- MENSAJE DE RESULTADO -->

                <c:if test="${param.resultado != null}">

                    <c:choose>

                        <c:when test="${param.resultado == 'true'}">

                            <div class="alert alert-success alert-dismissible fade show"
                                 role="alert">

                                La operación se realizó correctamente.

                                <button type="button"
                                        class="btn-close"
                                        data-bs-dismiss="alert">
                                </button>

                            </div>

                        </c:when>

                        <c:otherwise>

                            <div class="alert alert-danger alert-dismissible fade show"
                                 role="alert">

                                No fue posible realizar la operación.

                                <button type="button"
                                        class="btn-close"
                                        data-bs-dismiss="alert">
                                </button>

                            </div>

                        </c:otherwise>

                    </c:choose>

                </c:if>

                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=inicio"
                       class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left"></i>
                        Regresar al inicio
                    </a>
                </div>
                       
                <c:if test="${seccion == 'configuracion'}">

                    <div class="card shadow-sm mb-4">

                        <div class="card-header">
                            <h4 class="mb-0">
                                Configuración de depreciación
                            </h4>
                        </div>

                        <div class="card-body">

                            <form action="${pageContext.request.contextPath}/AdminSistemaServlet"
                                  method="POST">

                                <input type="hidden"
                                       name="accion"
                                       value="depreciacion">

                                <div class="mb-3">
                                    <label class="form-label">
                                        Monto de depreciación por kilómetro
                                    </label>

                                    <input type="number"
                                           class="form-control"
                                           name="monto"
                                           min="0"
                                           step="0.01"
                                           value="${configuracion.montoPorKilometraje}"
                                           required>
                                </div>

                                <button type="submit" class="btn btn-primary">
                                    Guardar configuración
                                </button>

                            </form>

                        </div>
                    </div>

                </c:if>

                <c:if test="${seccion == 'usuarios'}">

                    <div class="card shadow-sm mb-4">

                        <div class="card-header">
                            <h4 class="mb-0">
                                <i class="bi bi-people"></i>
                                Gestión de usuarios
                            </h4>
                        </div>

                        <div class="card-body">

                            <div class="table-responsive">
                                <table class="table table-hover align-middle">

                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Usuario</th>
                                            <th>Nombre</th>
                                            <th>Estado</th>
                                            <th>Acción</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        <c:forEach var="usuario" items="${usuarios}">

                                            <tr>
                                                <td>${usuario.id}</td>

                                                <td>${usuario.userName}</td>

                                                <td>${usuario.nombre}</td>

                                                <td>
                                                    <c:choose>
                                                        <c:when test="${usuario.estado}">
                                                            <span class="badge bg-success">
                                                                Activo
                                                            </span>
                                                        </c:when>

                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">
                                                                Inactivo
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>
                                                    <form method="POST"
                                                          action="${pageContext.request.contextPath}/AdminSistemaServlet">

                                                        <input type="hidden"
                                                               name="accion"
                                                               value="cambiarEstadoUsuario">

                                                        <input type="hidden"
                                                               name="usuarioId"
                                                               value="${usuario.id}">

                                                        <input type="hidden"
                                                               name="estado"
                                                               value="${!usuario.estado}">

                                                        <button type="submit"
                                                                class="btn btn-sm
                                                                ${usuario.estado ? 'btn-danger' : 'btn-success'}">

                                                            <c:choose>
                                                                <c:when test="${usuario.estado}">
                                                                    <i class="bi bi-person-x"></i>
                                                                    Desactivar
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <i class="bi bi-person-check"></i>
                                                                    Activar
                                                                </c:otherwise>
                                                            </c:choose>

                                                        </button>

                                                    </form>
                                                </td>

                                            </tr>

                                        </c:forEach>

                                    </tbody>

                                </table>
                            </div>

                        </div>
                    </div>

                </c:if>
                <!-- CREAR SUCURSAL -->

                <div class="card shadow-sm mb-4">

                    <div class="card-header">

                        <h4 class="mb-0">
                            Crear sucursal
                        </h4>

                    </div>

                    <div class="card-body">

                        <form action="${pageContext.request.contextPath}/AdminSistemaServlet"
                              method="POST">

                            <input type="hidden"
                                   name="accion"
                                   value="crearSucursal">


                            <h5 class="mb-3">
                                Datos de la sucursal
                            </h5>


                            <div class="row">

                                <div class="col-md-6 mb-3">

                                    <label for="nombreSucursal"
                                           class="form-label">

                                        Nombre de la sucursal

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="nombreSucursal"
                                           name="nombre"
                                           maxlength="100"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="telefonoSucursal"
                                           class="form-label">

                                        Teléfono

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="telefonoSucursal"
                                           name="telefono"
                                           maxlength="20"
                                           required>

                                </div>


                                <div class="col-12 mb-4">

                                    <label for="direccionSucursal"
                                           class="form-label">

                                        Dirección

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="direccionSucursal"
                                           name="direccion"
                                           maxlength="200"
                                           required>

                                </div>

                            </div>


                            <hr>


                            <h5 class="mb-3">
                                Administrador de sucursal
                            </h5>


                            <div class="row">

                                <div class="col-md-6 mb-3">

                                    <label for="adminNombre"
                                           class="form-label">

                                        Nombre completo

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminNombre"
                                           name="adminNombre"
                                           maxlength="100"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="adminNit"
                                           class="form-label">

                                        NIT

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminNit"
                                           name="adminNit"
                                           maxlength="20"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="adminDpi"
                                           class="form-label">

                                        DPI

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminDpi"
                                           name="adminDpi"
                                           maxlength="20"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="adminTelefono"
                                           class="form-label">

                                        Teléfono

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminTelefono"
                                           name="adminTelefono"
                                           maxlength="20"
                                           required>

                                </div>


                                <div class="col-12 mb-3">

                                    <label for="adminDireccion"
                                           class="form-label">

                                        Dirección

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminDireccion"
                                           name="adminDireccion"
                                           maxlength="200"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="adminUsername"
                                           class="form-label">

                                        Nombre de usuario

                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           id="adminUsername"
                                           name="adminUsername"
                                           maxlength="50"
                                           required>

                                </div>


                                <div class="col-md-6 mb-3">

                                    <label for="adminPassword"
                                           class="form-label">

                                        Contraseña

                                    </label>

                                    <input type="password"
                                           class="form-control"
                                           id="adminPassword"
                                           name="adminPassword"
                                           maxlength="255"
                                           required>

                                </div>

                            </div>


                            <div class="text-end mt-3">

                                <button type="submit"
                                        class="btn btn-primary">

                                    Crear sucursal y administrador

                                </button>

                            </div>

                        </form>

                    </div>

                </div>


                <!-- LISTADO DE SUCURSALES -->

                <div class="card shadow-sm">

                    <div class="card-header">

                        <h4 class="mb-0">
                            Sucursales registradas
                        </h4>

                    </div>

                    <div class="card-body">

                        <c:choose>

                            <c:when test="${empty sucursales}">

                                <div class="alert alert-secondary mb-0">

                                    No hay sucursales registradas.

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover align-middle">

                                        <thead>

                                            <tr>

                                                <th>ID</th>

                                                <th>Nombre</th>

                                                <th>Dirección</th>

                                                <th>Teléfono</th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach items="${sucursales}"
                                                       var="sucursal">

                                                <tr>

                                                    <td>
                                                        ${sucursal.id}
                                                    </td>

                                                    <td>
                                                        ${sucursal.nombre}
                                                    </td>

                                                    <td>
                                                        ${sucursal.direccion}
                                                    </td>

                                                    <td>
                                                        ${sucursal.telefono}
                                                    </td>

                                                </tr>

                                            </c:forEach>

                                        </tbody>

                                    </table>

                                </div>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

            </div>
        </main>
    </body>
</html>
