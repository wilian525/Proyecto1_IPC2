<%-- 
    Document   : flota
    Created on : 13 sep 2026, 10:08:45 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main>

            <jsp:include page="/includes/header.jsp"/>

            <!-- ENCABEZADO -->

            <div class="container mt-4">

                <div class="d-flex justify-content-between align-items-center">

                    <div>
                        <h1>
                            <i class="bi bi-bus-front-fill"></i>
                            Gestión de Flota
                        </h1>

                        <p class="text-muted">
                            Administración de buses, choferes y mantenimiento.
                        </p>
                    </div>

                    <div>
                        <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=inicio"
                           class="btn btn-secondary">

                            <i class="bi bi-arrow-left"></i>
                            Regresar
                        </a>
                    </div>

                </div>

            </div>


            <!-- MENSAJES -->

            <c:if test="${param.resultado == 'true'}">

                <div class="container mt-3">

                    <div class="alert alert-success alert-dismissible fade show"
                         role="alert">

                        <i class="bi bi-check-circle"></i>

                        La operación se realizó correctamente.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </div>

            </c:if>


            <c:if test="${param.resultado == 'false'}">

                <div class="container mt-3">

                    <div class="alert alert-danger alert-dismissible fade show"
                         role="alert">

                        <i class="bi bi-exclamation-triangle"></i>

                        No fue posible realizar la operación.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </div>

            </c:if>


            <!-- NAVEGACIÓN DE LA SECCIÓN -->

            <div class="container mt-4">

                <div class="card">

                    <div class="card-body">

                        <div class="d-flex justify-content-center gap-2 flex-wrap">

                            <a href="${pageContext.servletContext.contextPath}/FlotaServlet?accion=buses"
                               class="btn ${seccion == 'buses' ? 'btn-primary' : 'btn-outline-primary'}">

                                <i class="bi bi-bus-front"></i>
                                Buses

                            </a>


                            <a href="${pageContext.servletContext.contextPath}/FlotaServlet?accion=choferes"
                               class="btn ${seccion == 'choferes' ? 'btn-primary' : 'btn-outline-primary'}">

                                <i class="bi bi-person-badge"></i>
                                Choferes

                            </a>


                            <a href="${pageContext.servletContext.contextPath}/FlotaServlet?accion=mantenimiento"
                               class="btn ${seccion == 'mantenimiento' ? 'btn-primary' : 'btn-outline-primary'}">

                                <i class="bi bi-tools"></i>
                                Mantenimiento

                            </a>

                        </div>

                    </div>

                </div>

            </div>


            <!-- ====================================================== -->
            <!-- BUSES -->
            <!-- ====================================================== -->

            <c:if test="${seccion == 'buses'}">

                <div class="container mt-4">

                    <div class="card">

                        <div class="card-header">

                            <div class="d-flex justify-content-between align-items-center">

                                <h4 class="mb-0">

                                    <i class="bi bi-bus-front"></i>

                                    Buses de la sucursal

                                </h4>

                                <button type="button"
                                        class="btn btn-success"
                                        data-bs-toggle="modal"
                                        data-bs-target="#modalCrearBus">

                                    <i class="bi bi-plus-circle"></i>

                                    Crear bus

                                </button>

                            </div>

                        </div>


                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty buses}">

                                    <div class="alert alert-info">

                                        <i class="bi bi-info-circle"></i>

                                        No hay buses registrados en esta sucursal.

                                    </div>

                                </c:when>


                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>

                                                    <th>Foto</th>
                                                    <th>Placa</th>
                                                    <th>Marca</th>
                                                    <th>Modelo</th>
                                                    <th>Año</th>
                                                    <th>Capacidad</th>
                                                    <th>Kilometraje</th>
                                                    <th>Estado</th>
                                                    <th>Acciones</th>

                                                </tr>

                                            </thead>


                                            <tbody>

                                                <c:forEach var="bus" items="${buses}">

                                                    <tr>

                                                        <td class="text-center">

                                                            <c:choose>

                                                                <c:when test="${not empty bus.foto}">

                                                                    <img src="${bus.foto}"
                                                                         alt="Foto del bus"
                                                                         style="width:80px;height:55px;object-fit:cover;"
                                                                         class="rounded">

                                                                </c:when>

                                                                <c:otherwise>

                                                                    <i class="bi bi-bus-front fs-2 text-muted"></i>

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>


                                                        <td>
                                                            ${bus.placa}
                                                        </td>

                                                        <td>
                                                            ${bus.marca}
                                                        </td>

                                                        <td>
                                                            ${bus.modelo}
                                                        </td>

                                                        <td>
                                                            ${bus.añoFabricacion}
                                                        </td>

                                                        <td>
                                                            ${bus.capacidad}
                                                        </td>

                                                        <td>
                                                            ${bus.kilometrajeActual}
                                                        </td>


                                                        <td>

                                                            <c:choose>

                                                                <c:when test="${bus.estado}">

                                                                    <span class="badge bg-success">
                                                                        Activo
                                                                    </span>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    <span class="badge bg-danger">
                                                                        Inactivo
                                                                    </span>

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>


                                                        <td>

                                                            <div class="d-flex gap-1 flex-wrap">


                                                                <!-- ACTUALIZAR BUS -->

                                                                <button type="button"
                                                                        class="btn btn-sm btn-warning"
                                                                        data-bs-toggle="modal"
                                                                        data-bs-target="#modalEditarBus${bus.id}">

                                                                    <i class="bi bi-pencil"></i>

                                                                </button>


                                                                <!-- DESACTIVAR BUS -->

                                                                <c:if test="${bus.estado}">

                                                                    <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                                                          method="POST">

                                                                        <input type="hidden"
                                                                               name="accion"
                                                                               value="desactivarBus">

                                                                        <input type="hidden"
                                                                               name="busId"
                                                                               value="${bus.id}">

                                                                        <button type="submit"
                                                                                class="btn btn-sm btn-danger"
                                                                                onclick="return confirm('¿Desea desactivar este bus?');">

                                                                            <i class="bi bi-x-circle"></i>

                                                                        </button>

                                                                    </form>

                                                                </c:if>

                                                            </div>

                                                        </td>

                                                    </tr>


                                                    <!-- MODAL EDITAR BUS -->

                                                <div class="modal fade"
                                                     id="modalEditarBus${bus.id}"
                                                     tabindex="-1">

                                                    <div class="modal-dialog modal-lg">

                                                        <div class="modal-content">

                                                            <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                                                  method="POST">

                                                                <div class="modal-header">

                                                                    <h5 class="modal-title">

                                                                        <i class="bi bi-pencil"></i>

                                                                        Actualizar bus

                                                                    </h5>

                                                                    <button type="button"
                                                                            class="btn-close"
                                                                            data-bs-dismiss="modal">
                                                                    </button>

                                                                </div>


                                                                <div class="modal-body">

                                                                    <input type="hidden"
                                                                           name="accion"
                                                                           value="actualizarBus">

                                                                    <input type="hidden"
                                                                           name="busId"
                                                                           value="${bus.id}">


                                                                    <div class="row">


                                                                        <div class="col-md-6 mb-3">

                                                                            <label class="form-label">
                                                                                Foto
                                                                            </label>

                                                                            <input type="text"
                                                                                   name="foto"
                                                                                   class="form-control"
                                                                                   value="${bus.foto}">

                                                                        </div>


                                                                        <div class="col-md-6 mb-3">

                                                                            <label class="form-label">
                                                                                Placa
                                                                            </label>

                                                                            <input type="text"
                                                                                   name="placa"
                                                                                   class="form-control"
                                                                                   value="${bus.placa}"
                                                                                   required>

                                                                        </div>


                                                                        <div class="col-md-4 mb-3">

                                                                            <label class="form-label">
                                                                                Marca
                                                                            </label>

                                                                            <input type="text"
                                                                                   name="marca"
                                                                                   class="form-control"
                                                                                   value="${bus.marca}"
                                                                                   required>

                                                                        </div>


                                                                        <div class="col-md-4 mb-3">

                                                                            <label class="form-label">
                                                                                Modelo
                                                                            </label>

                                                                            <input type="text"
                                                                                   name="modelo"
                                                                                   class="form-control"
                                                                                   value="${bus.modelo}"
                                                                                   required>

                                                                        </div>


                                                                        <div class="col-md-4 mb-3">

                                                                            <label class="form-label">
                                                                                Año de fabricación
                                                                            </label>

                                                                            <input type="number"
                                                                                   name="anioFabricacion"
                                                                                   class="form-control"
                                                                                   value="${bus.añoFabricacion}"
                                                                                   required>

                                                                        </div>


                                                                        <div class="col-md-6 mb-3">

                                                                            <label class="form-label">
                                                                                Capacidad
                                                                            </label>

                                                                            <input type="number"
                                                                                   name="capacidad"
                                                                                   class="form-control"
                                                                                   value="${bus.capacidad}"
                                                                                   min="1"
                                                                                   required>

                                                                        </div>


                                                                        <div class="col-md-6 mb-3">

                                                                            <label class="form-label">
                                                                                Kilometraje actual
                                                                            </label>

                                                                            <input type="number"
                                                                                   name="kilometraje"
                                                                                   class="form-control"
                                                                                   value="${bus.kilometrajeActual}"
                                                                                   min="0"
                                                                                   step="0.01"
                                                                                   required>

                                                                        </div>

                                                                    </div>

                                                                </div>


                                                                <div class="modal-footer">

                                                                    <button type="button"
                                                                            class="btn btn-secondary"
                                                                            data-bs-dismiss="modal">

                                                                        Cancelar

                                                                    </button>

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

                                            </c:forEach>

                                            </tbody>

                                        </table>

                                    </div>

                                </c:otherwise>

                            </c:choose>

                        </div>

                    </div>

                </div>


                <!-- MODAL CREAR BUS -->

                <div class="modal fade"
                     id="modalCrearBus"
                     tabindex="-1">

                    <div class="modal-dialog modal-lg">

                        <div class="modal-content">

                            <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                  method="POST">

                                <div class="modal-header">

                                    <h5 class="modal-title">

                                        <i class="bi bi-bus-front"></i>

                                        Crear bus

                                    </h5>

                                    <button type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal">
                                    </button>

                                </div>


                                <div class="modal-body">

                                    <input type="hidden"
                                           name="accion"
                                           value="crearBus">


                                    <div class="row">


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Foto
                                            </label>

                                            <input type="text"
                                                   name="foto"
                                                   class="form-control"
                                                   placeholder="URL de la foto">

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Placa
                                            </label>

                                            <input type="text"
                                                   name="placa"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-4 mb-3">

                                            <label class="form-label">
                                                Marca
                                            </label>

                                            <input type="text"
                                                   name="marca"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-4 mb-3">

                                            <label class="form-label">
                                                Modelo
                                            </label>

                                            <input type="text"
                                                   name="modelo"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-4 mb-3">

                                            <label class="form-label">
                                                Año de fabricación
                                            </label>

                                            <input type="number"
                                                   name="anioFabricacion"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Capacidad
                                            </label>

                                            <input type="number"
                                                   name="capacidad"
                                                   class="form-control"
                                                   min="1"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Kilometraje actual
                                            </label>

                                            <input type="number"
                                                   name="kilometraje"
                                                   class="form-control"
                                                   min="0"
                                                   step="0.01"
                                                   required>

                                        </div>

                                    </div>

                                </div>


                                <div class="modal-footer">

                                    <button type="button"
                                            class="btn btn-secondary"
                                            data-bs-dismiss="modal">

                                        Cancelar

                                    </button>

                                    <button type="submit"
                                            class="btn btn-success">

                                        <i class="bi bi-save"></i>

                                        Crear bus

                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </div>

            </c:if>


            <!-- ====================================================== -->
            <!-- CHOFERES -->
            <!-- ====================================================== -->

            <c:if test="${seccion == 'choferes'}">

                <div class="container mt-4">

                    <div class="card">

                        <div class="card-header">

                            <div class="d-flex justify-content-between align-items-center">

                                <h4 class="mb-0">
                                    <i class="bi bi-person-badge"></i>
                                    Choferes de la sucursal
                                </h4>

                                <button type="button"
                                        class="btn btn-success"
                                        data-bs-toggle="modal"
                                        data-bs-target="#modalCrearChofer">

                                    <i class="bi bi-person-plus"></i>
                                    Crear chofer

                                </button>

                            </div>

                        </div>


                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty choferes}">

                                    <div class="alert alert-info">

                                        <i class="bi bi-info-circle"></i>
                                        No hay choferes registrados en esta sucursal.

                                    </div>

                                </c:when>


                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>

                                                    <th>Foto</th>
                                                    <th>Nombre</th>
                                                    <th>DPI</th>
                                                    <th>Teléfono</th>
                                                    <th>Licencia</th>
                                                    <th>Tipo</th>
                                                    <th>Vencimiento</th>
                                                    <th>Salario</th>
                                                    <th>Estado</th>
                                                    <th>Acciones</th>

                                                </tr>

                                            </thead>


                                            <tbody>

                                                <c:forEach var="chofer" items="${choferes}">

                                                    <tr>

                                                        <td class="text-center">

                                                            <c:choose>

                                                                <c:when test="${not empty chofer.foto}">

                                                                    <img src="${chofer.foto}"
                                                                         alt="Foto del chofer"
                                                                         style="width:60px;height:60px;object-fit:cover;"
                                                                         class="rounded-circle">

                                                                </c:when>

                                                                <c:otherwise>

                                                                    <i class="bi bi-person-circle fs-2 text-muted"></i>

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>


                                                        <td>
                                                            ${chofer.nombre}
                                                        </td>


                                                        <td>
                                                            ${chofer.dpi}
                                                        </td>


                                                        <td>
                                                            ${chofer.telefono}
                                                        </td>


                                                        <td>
                                                            ${chofer.numeroLicencia}
                                                        </td>


                                                        <td>
                                                            ${chofer.tipoLicencia}
                                                        </td>


                                                        <td>
                                                            ${chofer.fechaVencimiento}
                                                        </td>


                                                        <td>
                                                            ${chofer.salarioBasePorViaje}
                                                        </td>


                                                        <td>

                                                            <c:choose>

                                                                <c:when test="${chofer.estado}">

                                                                    <span class="badge bg-success">
                                                                        Activo
                                                                    </span>

                                                                </c:when>

                                                                <c:otherwise>

                                                                    <span class="badge bg-danger">
                                                                        Inactivo
                                                                    </span>

                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>


                                                        <td>

                                                            <div class="d-flex gap-1 flex-wrap">


                                                                <!-- EDITAR -->

                                                                <button type="button"
                                                                        class="btn btn-sm btn-warning"
                                                                        data-bs-toggle="modal"
                                                                        data-bs-target="#modalEditarChofer${chofer.id}">

                                                                    <i class="bi bi-pencil"></i>

                                                                </button>


                                                                <!-- CAMBIAR ESTADO -->

                                                                <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                                                      method="POST">

                                                                    <input type="hidden"
                                                                           name="accion"
                                                                           value="estadoChofer">

                                                                    <input type="hidden"
                                                                           name="choferId"
                                                                           value="${chofer.id}">

                                                                    <input type="hidden"
                                                                           name="estado"
                                                                           value="${!chofer.estado}">

                                                                    <button type="submit"
                                                                            class="btn btn-sm ${chofer.estado ? 'btn-danger' : 'btn-success'}">

                                                                        <c:choose>

                                                                            <c:when test="${chofer.estado}">

                                                                                <i class="bi bi-person-x"></i>

                                                                            </c:when>

                                                                            <c:otherwise>

                                                                                <i class="bi bi-person-check"></i>

                                                                            </c:otherwise>

                                                                        </c:choose>

                                                                    </button>

                                                                </form>

                                                            </div>

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


                <!-- ====================================================== -->
                <!-- MODALES EDITAR CHOFER                                  -->
                <!-- ====================================================== -->

                <c:forEach var="chofer" items="${choferes}">

                    <div class="modal fade"
                         id="modalEditarChofer${chofer.id}"
                         tabindex="-1"
                         aria-hidden="true">

                        <div class="modal-dialog modal-lg">

                            <div class="modal-content">

                                <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                      method="POST">

                                    <div class="modal-header">

                                        <h5 class="modal-title">

                                            <i class="bi bi-pencil"></i>
                                            Actualizar chofer

                                        </h5>

                                        <button type="button"
                                                class="btn-close"
                                                data-bs-dismiss="modal">
                                        </button>

                                    </div>


                                    <div class="modal-body">

                                        <input type="hidden"
                                               name="accion"
                                               value="actualizarChofer">

                                        <input type="hidden"
                                               name="chofer"
                                               value="${chofer.id}">


                                        <div class="row">


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Nombre
                                                </label>

                                                <input type="text"
                                                       name="nombre"
                                                       class="form-control"
                                                       value="${chofer.nombre}"
                                                       required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    NIT
                                                </label>

                                                <input type="text"
                                                       name="nit"
                                                       class="form-control"
                                                       value="${chofer.nit}"
                                                       required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    DPI
                                                </label>

                                                <input type="text"
                                                       name="dpi"
                                                       class="form-control"
                                                       value="${chofer.dpi}"
                                                       required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Teléfono
                                                </label>

                                                <input type="text"
                                                       name="telefono"
                                                       class="form-control"
                                                       value="${chofer.telefono}"
                                                       required>

                                            </div>


                                            <div class="col-md-12 mb-3">

                                                <label class="form-label">
                                                    Dirección
                                                </label>

                                                <input type="text"
                                                       name="direccion"
                                                       class="form-control"
                                                       value="${chofer.direccion}"
                                                       required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Foto
                                                </label>

                                                <input type="text"
                                                       name="foto"
                                                       class="form-control"
                                                       value="${chofer.foto}">

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Número de licencia
                                                </label>

                                                <input type="text"
                                                       name="numeroLicencia"
                                                       class="form-control"
                                                       value="${chofer.numeroLicencia}"
                                                       required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Tipo de licencia
                                                </label>

                                                <select name="tipoLicencia"
                                                        class="form-select"
                                                        required>

                                                    <option value="A"
                                                            <c:if test="${chofer.tipoLicencia == 'A'.charAt(0)}">selected</c:if>>
                                                                A
                                                            </option>

                                                            <option value="B"
                                                            <c:if test="${chofer.tipoLicencia == 'B'.charAt(0)}">selected</c:if>>
                                                                B
                                                            </option>

                                                            <option value="C"
                                                            <c:if test="${chofer.tipoLicencia == 'C'.charAt(0)}">selected</c:if>>
                                                                C
                                                            </option>

                                                    </select>

                                                </div>


                                                <div class="col-md-6 mb-3">

                                                    <label class="form-label">
                                                        Fecha de vencimiento
                                                    </label>

                                                    <input type="date"
                                                           name="fechaVencimiento"
                                                           class="form-control"
                                                           value="${chofer.fechaVencimiento}"
                                                    required>

                                            </div>


                                            <div class="col-md-6 mb-3">

                                                <label class="form-label">
                                                    Salario base por viaje
                                                </label>

                                                <input type="number"
                                                       name="salario"
                                                       class="form-control"
                                                       value="${chofer.salarioBasePorViaje}"
                                                       min="0"
                                                       step="0.01"
                                                       required>

                                            </div>

                                        </div>

                                    </div>


                                    <div class="modal-footer">

                                        <button type="button"
                                                class="btn btn-secondary"
                                                data-bs-dismiss="modal">

                                            Cancelar

                                        </button>


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

                </c:forEach>


                <!-- ====================================================== -->
                <!-- MODAL CREAR CHOFER                                     -->
                <!-- ====================================================== -->

                <div class="modal fade"
                     id="modalCrearChofer"
                     tabindex="-1"
                     aria-hidden="true">

                    <div class="modal-dialog modal-lg">

                        <div class="modal-content">

                            <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                  method="POST">


                                <div class="modal-header">

                                    <h5 class="modal-title">

                                        <i class="bi bi-person-plus"></i>
                                        Crear chofer

                                    </h5>

                                    <button type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal">
                                    </button>

                                </div>


                                <div class="modal-body">

                                    <input type="hidden"
                                           name="accion"
                                           value="crearChofer">


                                    <div class="row">


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Nombre
                                            </label>

                                            <input type="text"
                                                   name="nombre"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                NIT
                                            </label>

                                            <input type="text"
                                                   name="nit"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                DPI
                                            </label>

                                            <input type="text"
                                                   name="dpi"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Teléfono
                                            </label>

                                            <input type="text"
                                                   name="telefono"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-12 mb-3">

                                            <label class="form-label">
                                                Dirección
                                            </label>

                                            <input type="text"
                                                   name="direccion"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Username
                                            </label>

                                            <input type="text"
                                                   name="username"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Password
                                            </label>

                                            <input type="password"
                                                   name="password"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Foto
                                            </label>

                                            <input type="text"
                                                   name="foto"
                                                   class="form-control"
                                                   placeholder="URL de la foto">

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Número de licencia
                                            </label>

                                            <input type="text"
                                                   name="numeroLicencia"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Tipo de licencia
                                            </label>

                                            <select name="tipoLicencia"
                                                    class="form-select"
                                                    required>

                                                <option value="">
                                                    Seleccione
                                                </option>

                                                <option value="A">
                                                    A
                                                </option>

                                                <option value="B">
                                                    B
                                                </option>

                                                <option value="C">
                                                    C
                                                </option>

                                            </select>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Fecha de vencimiento
                                            </label>

                                            <input type="date"
                                                   name="fechaVencimiento"
                                                   class="form-control"
                                                   required>

                                        </div>


                                        <div class="col-md-6 mb-3">

                                            <label class="form-label">
                                                Salario base por viaje
                                            </label>

                                            <input type="number"
                                                   name="salario"
                                                   class="form-control"
                                                   min="0"
                                                   step="0.01"
                                                   required>

                                        </div>

                                    </div>

                                </div>


                                <div class="modal-footer">

                                    <button type="button"
                                            class="btn btn-secondary"
                                            data-bs-dismiss="modal">

                                        Cancelar

                                    </button>


                                    <button type="submit"
                                            class="btn btn-success">

                                        <i class="bi bi-person-plus"></i>
                                        Crear chofer

                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </div>

            </c:if>


            <!-- ====================================================== -->
            <!-- MANTENIMIENTO -->
            <!-- ====================================================== -->

            <c:if test="${seccion == 'mantenimiento'}">

                <div class="container mt-4">

                    <div class="card">

                        <div class="card-header">

                            <h4 class="mb-0">

                                <i class="bi bi-tools"></i>

                                Mantenimiento de buses

                            </h4>

                        </div>


                        <div class="card-body">


                            <c:choose>

                                <c:when test="${empty buses}">

                                    <div class="alert alert-info">

                                        <i class="bi bi-info-circle"></i>

                                        No hay buses registrados para realizar mantenimiento.

                                    </div>

                                </c:when>


                                <c:otherwise>

                                    <div class="row">

                                        <div class="col-md-5">

                                            <div class="card">

                                                <div class="card-header">

                                                    <strong>
                                                        Seleccionar bus
                                                    </strong>

                                                </div>


                                                <div class="list-group list-group-flush">

                                                    <c:forEach var="bus"
                                                               items="${buses}">

                                                        <a href="${pageContext.servletContext.contextPath}/FlotaServlet?accion=mantenimiento&busId=${bus.id}"
                                                           class="list-group-item list-group-item-action ${busId == bus.id ? 'active' : ''}">

                                                            <div class="d-flex justify-content-between">

                                                                <span>

                                                                    <i class="bi bi-bus-front"></i>

                                                                    ${bus.placa}

                                                                </span>

                                                                <span>

                                                                    ${bus.marca}

                                                                </span>

                                                            </div>

                                                        </a>

                                                    </c:forEach>

                                                </div>

                                            </div>

                                        </div>


                                        <div class="col-md-7 mt-3 mt-md-0">

                                            <c:choose>

                                                <c:when test="${empty busId}">

                                                    <div class="alert alert-info">

                                                        <i class="bi bi-arrow-left"></i>

                                                        Seleccione un bus para ver su mantenimiento.

                                                    </div>

                                                </c:when>


                                                <c:otherwise>

                                                    <div class="card">

                                                        <div class="card-header">

                                                            <div class="d-flex justify-content-between align-items-center">

                                                                <strong>
                                                                    Historial de mantenimiento
                                                                </strong>


                                                                <button type="button"
                                                                        class="btn btn-success btn-sm"
                                                                        data-bs-toggle="modal"
                                                                        data-bs-target="#modalMantenimiento">

                                                                    <i class="bi bi-plus-circle"></i>

                                                                    Registrar

                                                                </button>

                                                            </div>

                                                        </div>


                                                        <div class="card-body">

                                                            <c:choose>

                                                                <c:when test="${empty mantenimiento}">

                                                                    <div class="alert alert-warning">

                                                                        No hay mantenimientos registrados para este bus.

                                                                    </div>

                                                                </c:when>


                                                                <c:otherwise>

                                                                    <div class="table-responsive">

                                                                        <table class="table table-bordered table-hover">

                                                                            <thead class="table-dark">

                                                                                <tr>

                                                                                    <th>Fecha</th>
                                                                                    <th>Mano de obra</th>
                                                                                    <th>Repuesto</th>

                                                                                </tr>

                                                                            </thead>


                                                                            <tbody>

                                                                                <c:forEach var="m"
                                                                                           items="${mantenimiento}">

                                                                                    <tr>

                                                                                        <td>
                                                                                            ${m.fecha}
                                                                                        </td>

                                                                                        <td>
                                                                                            ${m.montoManoObra}
                                                                                        </td>

                                                                                        <td>
                                                                                            ${m.montoRepuesto}
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


                                                    <!-- MODAL MANTENIMIENTO -->

                                                    <div class="modal fade"
                                                         id="modalMantenimiento"
                                                         tabindex="-1">

                                                        <div class="modal-dialog">

                                                            <div class="modal-content">

                                                                <form action="${pageContext.servletContext.contextPath}/FlotaServlet"
                                                                      method="POST">

                                                                    <div class="modal-header">

                                                                        <h5 class="modal-title">

                                                                            <i class="bi bi-tools"></i>

                                                                            Registrar mantenimiento

                                                                        </h5>

                                                                        <button type="button"
                                                                                class="btn-close"
                                                                                data-bs-dismiss="modal">
                                                                        </button>

                                                                    </div>


                                                                    <div class="modal-body">

                                                                        <input type="hidden"
                                                                               name="accion"
                                                                               value="mantenimiento">

                                                                        <input type="hidden"
                                                                               name="busId"
                                                                               value="${busId}">


                                                                        <div class="mb-3">

                                                                            <label class="form-label">
                                                                                Fecha
                                                                            </label>

                                                                            <input type="date"
                                                                                   name="fecha"
                                                                                   class="form-control"
                                                                                   required>

                                                                        </div>


                                                                        <div class="mb-3">

                                                                            <label class="form-label">
                                                                                Mano de obra
                                                                            </label>

                                                                            <input type="number"
                                                                                   name="manoObra"
                                                                                   class="form-control"
                                                                                   min="0"
                                                                                   step="0.01"
                                                                                   required>

                                                                        </div>


                                                                        <div class="mb-3">

                                                                            <label class="form-label">
                                                                                Repuesto
                                                                            </label>

                                                                            <input type="number"
                                                                                   name="repuesto"
                                                                                   class="form-control"
                                                                                   min="0"
                                                                                   step="0.01"
                                                                                   required>

                                                                        </div>

                                                                    </div>


                                                                    <div class="modal-footer">

                                                                        <button type="button"
                                                                                class="btn btn-secondary"
                                                                                data-bs-dismiss="modal">

                                                                            Cancelar

                                                                        </button>

                                                                        <button type="submit"
                                                                                class="btn btn-success">

                                                                            <i class="bi bi-save"></i>

                                                                            Registrar

                                                                        </button>

                                                                    </div>

                                                                </form>

                                                            </div>

                                                        </div>

                                                    </div>

                                                </c:otherwise>

                                            </c:choose>

                                        </div>

                                    </div>

                                </c:otherwise>

                            </c:choose>

                        </div>

                    </div>

                </div>

            </c:if>


            <jsp:include page="/includes/footer.jsp"/>

        </main>
    </body>
</html>
