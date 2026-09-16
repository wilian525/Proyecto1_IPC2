<%-- 
    Document   : viajes
    Created on : 13 sep 2026, 10:08:58 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Gestión de viajes</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main>
            <div class="container-fluid mt-4">


                <!-- ========================================================= -->
                <!-- ENCABEZADO -->
                <!-- ========================================================= -->

                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>

                        <h2>

                            <i class="bi bi-signpost-split"></i>

                            Gestión de viajes

                        </h2>

                        <p class="text-muted mb-0">

                            Administración de rutas y viajes regulares

                        </p>

                    </div>


                    <a href="${pageContext.servletContext.contextPath}/ViajesServlet"
                       class="btn btn-outline-secondary">

                        <i class="bi bi-arrow-clockwise"></i>

                        Actualizar

                    </a>

                </div>



                <!-- ========================================================= -->
                <!-- MENSAJES -->
                <!-- ========================================================= -->

                <c:if test="${param.resultado == 'true'}">

                    <div class="alert alert-success alert-dismissible fade show">

                        <i class="bi bi-check-circle"></i>

                        La operación se realizó correctamente.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>


                <c:if test="${param.resultado == 'false'}">

                    <div class="alert alert-danger alert-dismissible fade show">

                        <i class="bi bi-exclamation-triangle"></i>

                        No fue posible realizar la operación.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>

                    </div>

                </c:if>



                <!-- ========================================================= -->
                <!-- SECCIÓN RUTAS -->
                <!-- ========================================================= -->

                <!-- ==================== RUTAS ==================== -->

                <div class="card mb-4">

                    <div class="card-header">

                        <div class="d-flex justify-content-between align-items-center">

                            <h4 class="mb-0">

                                <i class="bi bi-signpost-2"></i>

                                Rutas

                            </h4>


                            <button type="button"
                                    class="btn btn-success"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalCrearRuta">

                                <i class="bi bi-plus-circle"></i>

                                Crear ruta

                            </button>

                        </div>

                    </div>


                    <div class="card-body">

                        <c:choose>

                            <c:when test="${empty rutas}">

                                <div class="alert alert-info mb-0">

                                    <i class="bi bi-info-circle"></i>

                                    No hay rutas registradas para esta sucursal.

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover align-middle">

                                        <thead class="table-dark">

                                            <tr>

                                                <th>ID</th>

                                                <th>Origen</th>

                                                <th>Destino</th>

                                                <th>Distancia (km)</th>

                                                <th>Precio boleto</th>

                                                <th>Acciones</th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach var="ruta"
                                                       items="${rutas}">

                                                <tr>

                                                    <td>
                                                        ${ruta.id}
                                                    </td>


                                                    <td>
                                                        ${ruta.origen.nombre}
                                                    </td>


                                                    <td>
                                                        ${ruta.destino.nombre}
                                                    </td>


                                                    <td>
                                                        ${ruta.distanciaKilometraje}
                                                    </td>


                                                    <td>

                                                        Q
                                                        <fmt:formatNumber
                                                            value="${ruta.precioBoleto}"
                                                            minFractionDigits="2"
                                                            maxFractionDigits="2"/>

                                                    </td>


                                                    <td>

                                                        <div class="d-flex gap-1">

                                                            <!-- EDITAR RUTA -->

                                                            <button type="button"
                                                                    class="btn btn-sm btn-warning"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#modalEditarRuta${ruta.id}">

                                                                <i class="bi bi-pencil"></i>

                                                            </button>


                                                            <!-- ELIMINAR RUTA -->

                                                            <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                                                  method="POST">

                                                                <input type="hidden"
                                                                       name="accion"
                                                                       value="eliminarRuta">

                                                                <input type="hidden"
                                                                       name="rutaId"
                                                                       value="${ruta.id}">


                                                                <button type="submit"
                                                                        class="btn btn-sm btn-danger"
                                                                        onclick="return confirm('¿Desea eliminar esta ruta?');">

                                                                    <i class="bi bi-trash"></i>

                                                                </button>

                                                            </form>

                                                        </div>

                                                    </td>

                                                </tr>


                                                <!-- ==================== MODAL EDITAR RUTA ==================== -->

                                            <div class="modal fade"
                                                 id="modalEditarRuta${ruta.id}"
                                                 tabindex="-1"
                                                 aria-hidden="true">

                                                <div class="modal-dialog">

                                                    <div class="modal-content">

                                                        <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                                              method="POST">


                                                            <div class="modal-header">

                                                                <h5 class="modal-title">

                                                                    <i class="bi bi-pencil"></i>

                                                                    Editar ruta

                                                                </h5>


                                                                <button type="button"
                                                                        class="btn-close"
                                                                        data-bs-dismiss="modal"
                                                                        aria-label="Cerrar">
                                                                </button>

                                                            </div>


                                                            <div class="modal-body">

                                                                <input type="hidden"
                                                                       name="accion"
                                                                       value="actualizarRuta">


                                                                <input type="hidden"
                                                                       name="rutaId"
                                                                       value="${ruta.id}">


                                                                <!-- ORIGEN -->

                                                                <div class="mb-3">

                                                                    <label class="form-label">

                                                                        Sucursal de origen

                                                                    </label>


                                                                    <select name="origenId"
                                                                            class="form-select"
                                                                            required>

                                                                        <c:forEach var="sucursal"
                                                                                   items="${sucursales}">

                                                                            <option value="${sucursal.id}"
                                                                                    ${sucursal.id == ruta.origen.id ? 'selected' : ''}>

                                                                                ${sucursal.nombre}

                                                                            </option>

                                                                        </c:forEach>

                                                                    </select>

                                                                </div>


                                                                <!-- DESTINO -->

                                                                <div class="mb-3">

                                                                    <label class="form-label">

                                                                        Sucursal de destino

                                                                    </label>


                                                                    <select name="destinoId"
                                                                            class="form-select"
                                                                            required>

                                                                        <c:forEach var="sucursal"
                                                                                   items="${sucursales}">

                                                                            <option value="${sucursal.id}"
                                                                                    ${sucursal.id == ruta.destino.id ? 'selected' : ''}>

                                                                                ${sucursal.nombre}

                                                                            </option>

                                                                        </c:forEach>

                                                                    </select>

                                                                </div>


                                                                <!-- DISTANCIA -->

                                                                <div class="mb-3">

                                                                    <label class="form-label">

                                                                        Distancia aproximada (km)

                                                                    </label>


                                                                    <input type="number"
                                                                           name="distanciaKm"
                                                                           class="form-control"
                                                                           min="0"
                                                                           step="0.01"
                                                                           value="${ruta.distanciaKilometraje}"
                                                                           required>

                                                                </div>


                                                                <!-- PRECIO -->

                                                                <div class="mb-3">

                                                                    <label class="form-label">

                                                                        Precio del boleto

                                                                    </label>


                                                                    <input type="number"
                                                                           name="precioBoleto"
                                                                           class="form-control"
                                                                           min="0"
                                                                           step="0.01"
                                                                           value="${ruta.precioBoleto}"
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


                <!-- ==================== MODAL CREAR RUTA ==================== -->

                <div class="modal fade"
                     id="modalCrearRuta"
                     tabindex="-1"
                     aria-hidden="true">

                    <div class="modal-dialog">

                        <div class="modal-content">


                            <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                  method="POST">


                                <div class="modal-header">

                                    <h5 class="modal-title">

                                        <i class="bi bi-plus-circle"></i>

                                        Crear ruta

                                    </h5>


                                    <button type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal"
                                            aria-label="Cerrar">
                                    </button>

                                </div>


                                <div class="modal-body">

                                    <input type="hidden"
                                           name="accion"
                                           value="crearRuta">


                                    <!-- ORIGEN -->

                                    <div class="mb-3">

                                        <label class="form-label">

                                            Sucursal de origen

                                        </label>


                                        <select name="origenId"
                                                class="form-select"
                                                required>

                                            <option value="">

                                                Seleccione una sucursal

                                            </option>


                                            <c:forEach var="sucursal"
                                                       items="${sucursales}">

                                                <option value="${sucursal.id}">

                                                    ${sucursal.nombre}

                                                </option>

                                            </c:forEach>

                                        </select>

                                    </div>


                                    <!-- DESTINO -->

                                    <div class="mb-3">

                                        <label class="form-label">

                                            Sucursal de destino

                                        </label>


                                        <select name="destinoId"
                                                class="form-select"
                                                required>

                                            <option value="">

                                                Seleccione una sucursal

                                            </option>


                                            <c:forEach var="sucursal"
                                                       items="${sucursales}">

                                                <option value="${sucursal.id}">

                                                    ${sucursal.nombre}

                                                </option>

                                            </c:forEach>

                                        </select>

                                    </div>


                                    <!-- DISTANCIA -->

                                    <div class="mb-3">

                                        <label class="form-label">

                                            Distancia aproximada (km)

                                        </label>


                                        <input type="number"
                                               name="distanciaKm"
                                               class="form-control"
                                               min="0"
                                               step="0.01"
                                               required>

                                    </div>


                                    <!-- PRECIO -->

                                    <div class="mb-3">

                                        <label class="form-label">

                                            Precio del boleto

                                        </label>


                                        <input type="number"
                                               name="precioBoleto"
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

                                        <i class="bi bi-plus-circle"></i>

                                        Crear ruta

                                    </button>

                                </div>


                            </form>

                        </div>

                    </div>

                </div>

                <!-- ==================== FIN RUTAS ==================== -->



                <!-- ========================================================= -->
                <!-- MODAL CREAR RUTA -->
                <!-- ========================================================= -->

                <div class="modal fade"
                     id="modalCrearRuta"
                     tabindex="-1">


                    <div class="modal-dialog">


                        <div class="modal-content">


                            <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                  method="POST">


                                <div class="modal-header">


                                    <h5 class="modal-title">

                                        <i class="bi bi-plus-circle"></i>

                                        Crear ruta

                                    </h5>


                                    <button type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal">
                                    </button>


                                </div>



                                <div class="modal-body">


                                    <input type="hidden"
                                           name="accion"
                                           value="crearRuta">



                                    <div class="mb-3">


                                        <label class="form-label">

                                            Sucursal de origen

                                        </label>


                                        <select name="origenId"
                                                class="form-select"
                                                required>


                                            <option value="">

                                                Seleccione una sucursal

                                            </option>


                                            <c:forEach var="sucursal"
                                                       items="${sucursales}">


                                                <option value="${sucursal.id}">

                                                    ${sucursal.nombre}

                                                </option>


                                            </c:forEach>


                                        </select>


                                    </div>



                                    <div class="mb-3">


                                        <label class="form-label">

                                            Sucursal de destino

                                        </label>


                                        <select name="destinoId"
                                                class="form-select"
                                                required>


                                            <option value="">

                                                Seleccione una sucursal

                                            </option>


                                            <c:forEach var="sucursal"
                                                       items="${sucursales}">


                                                <option value="${sucursal.id}">

                                                    ${sucursal.nombre}

                                                </option>


                                            </c:forEach>


                                        </select>


                                    </div>



                                    <div class="mb-3">


                                        <label class="form-label">

                                            Distancia aproximada (km)

                                        </label>


                                        <input type="number"
                                               name="distanciaKm"
                                               class="form-control"
                                               min="0"
                                               step="0.01"
                                               required>


                                    </div>



                                    <div class="mb-3">


                                        <label class="form-label">

                                            Precio del boleto

                                        </label>


                                        <input type="number"
                                               name="precioBoleto"
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

                                        <i class="bi bi-plus-circle"></i>

                                        Crear ruta

                                    </button>


                                </div>


                            </form>


                        </div>


                    </div>


                </div>



                <!-- ========================================================= -->
                <!-- SECCIÓN VIAJES -->
                <!-- ========================================================= -->

                <div class="card mb-4">


                    <div class="card-header">


                        <div class="d-flex justify-content-between align-items-center">


                            <h4 class="mb-0">

                                <i class="bi bi-bus-front"></i>

                                Viajes programados

                            </h4>


                            <button type="button"
                                    class="btn btn-success"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalProgramarViaje">

                                <i class="bi bi-calendar-plus"></i>

                                Programar viaje

                            </button>


                        </div>


                    </div>



                    <div class="card-body">


                        <c:choose>


                            <c:when test="${empty viajes}">


                                <div class="alert alert-info mb-0">

                                    <i class="bi bi-info-circle"></i>

                                    No hay viajes programados.

                                </div>


                            </c:when>


                            <c:otherwise>


                                <div class="table-responsive">


                                    <table class="table table-bordered table-hover align-middle">


                                        <thead class="table-dark">


                                            <tr>

                                                <th>Bus</th>

                                                <th>Chofer</th>

                                                <th>Ruta</th>

                                                <th>Salida programada</th>

                                                <th>Llegada estimada</th>

                                                <th>Acciones</th>

                                            </tr>


                                        </thead>



                                        <tbody>


                                            <c:forEach var="viaje"
                                                       items="${viajes}">


                                                <tr>


                                                    <td>

                                                        ${viaje.bus.placa}

                                                    </td>


                                                    <td>

                                                        ${viaje.chofer.nombre}

                                                    </td>


                                                    <td>

                                                        ${viaje.ruta.origen.nombre}

                                                        -

                                                        ${viaje.ruta.destino.nombre}

                                                    </td>


                                                    <td>

                                                       ${viaje.fechaSalida} ${viaje.horaSalidaProgramada}

                                                    </td>


                                                    <td>

                                                     ${viaje.fechaLlegadaEstimada} ${viaje.horaLlegadaEstimada}

                                                    </td>


                                                    <td>


                                                        <div class="d-flex gap-1 flex-wrap">


                                                            <!-- ================================= -->
                                                            <!-- SALIDA -->
                                                            <!-- ================================= -->

                                                            <c:if test="${empty viaje.horaSalidaReal}">


                                                                <button type="button"
                                                                        class="btn btn-sm btn-success"
                                                                        data-bs-toggle="modal"
                                                                        data-bs-target="#modalSalida${viaje.id}">

                                                                    <i class="bi bi-play-fill"></i>

                                                                    Salida

                                                                </button>


                                                            </c:if>



                                                            <!-- ================================= -->
                                                            <!-- LLEGADA -->
                                                            <!-- ================================= -->

                                                            <c:if test="${not empty viaje.horaSalidaReal && empty viaje.horaLlegadaReal}">


                                                                <button type="button"
                                                                        class="btn btn-sm btn-primary"
                                                                        data-bs-toggle="modal"
                                                                        data-bs-target="#modalLlegada${viaje.id}">

                                                                    <i class="bi bi-flag-fill"></i>

                                                                    Llegada

                                                                </button>


                                                            </c:if>



                                                            <!-- ================================= -->
                                                            <!-- ELIMINAR -->
                                                            <!-- ================================= -->

                                                            <c:if test="${empty viaje.horaSalidaReal}">


                                                                <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                                                      method="POST">


                                                                    <input type="hidden"
                                                                           name="accion"
                                                                           value="eliminarViaje">


                                                                    <input type="hidden"
                                                                           name="viajeId"
                                                                           value="${viaje.id}">


                                                                    <button type="submit"
                                                                            class="btn btn-sm btn-danger"
                                                                            onclick="return confirm('¿Desea eliminar este viaje?');">

                                                                        <i class="bi bi-trash"></i>

                                                                    </button>


                                                                </form>


                                                            </c:if>


                                                        </div>


                                                    </td>


                                                </tr>



                                                <!-- ================================================= -->
                                                <!-- MODAL SALIDA -->
                                                <!-- ================================================= -->

                                            <div class="modal fade"
                                                 id="modalSalida${viaje.id}"
                                                 tabindex="-1">


                                                <div class="modal-dialog">


                                                    <div class="modal-content">


                                                        <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                                              method="POST">


                                                            <div class="modal-header">


                                                                <h5 class="modal-title">

                                                                    <i class="bi bi-play-circle"></i>

                                                                    Registrar salida

                                                                </h5>


                                                                <button type="button"
                                                                        class="btn-close"
                                                                        data-bs-dismiss="modal">
                                                                </button>


                                                            </div>



                                                            <div class="modal-body">


                                                                <input type="hidden"
                                                                       name="accion"
                                                                       value="salida">


                                                                <input type="hidden"
                                                                       name="viajeId"
                                                                       value="${viaje.id}">



                                                                <div class="mb-3">


                                                                    <label class="form-label">

                                                                        Hora real de salida

                                                                    </label>


                                                                    <input type="datetime-local"
                                                                           name="horaSalida"
                                                                           class="form-control"
                                                                           required>


                                                                </div>



                                                                <div class="mb-3">


                                                                    <label class="form-label">

                                                                        Kilometraje actual del bus

                                                                    </label>


                                                                    <input type="number"
                                                                           name="kilometrajeInicial"
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

                                                                    <i class="bi bi-play-fill"></i>

                                                                    Registrar salida

                                                                </button>


                                                            </div>


                                                        </form>


                                                    </div>


                                                </div>


                                            </div>



                                            <!-- ================================================= -->
                                            <!-- MODAL LLEGADA -->
                                            <!-- ================================================= -->

                                            <div class="modal fade"
                                                 id="modalLlegada${viaje.id}"
                                                 tabindex="-1">


                                                <div class="modal-dialog">


                                                    <div class="modal-content">


                                                        <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                                              method="POST">


                                                            <div class="modal-header">


                                                                <h5 class="modal-title">

                                                                    <i class="bi bi-flag"></i>

                                                                    Registrar llegada

                                                                </h5>


                                                                <button type="button"
                                                                        class="btn-close"
                                                                        data-bs-dismiss="modal">
                                                                </button>


                                                            </div>



                                                            <div class="modal-body">


                                                                <input type="hidden"
                                                                       name="accion"
                                                                       value="llegada">


                                                                <input type="hidden"
                                                                       name="viajeId"
                                                                       value="${viaje.id}">



                                                                <div class="mb-3">


                                                                    <label class="form-label">

                                                                        Hora real de llegada

                                                                    </label>


                                                                    <input type="datetime-local"
                                                                           name="horaLlegada"
                                                                           class="form-control"
                                                                           required>


                                                                </div>



                                                                <div class="mb-3">


                                                                    <label class="form-label">

                                                                        Kilometraje final

                                                                    </label>


                                                                    <input type="number"
                                                                           name="kilometrajeFinal"
                                                                           class="form-control"
                                                                           min="0"
                                                                           step="0.01"
                                                                           required>


                                                                </div>



                                                                <div class="mb-3">


                                                                    <label class="form-label">

                                                                        Gasto total en combustible

                                                                    </label>


                                                                    <input type="number"
                                                                           name="combustible"
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
                                                                        class="btn btn-primary">

                                                                    <i class="bi bi-flag-fill"></i>

                                                                    Registrar llegada

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



                <!-- ========================================================= -->
                <!-- MODAL PROGRAMAR VIAJE -->
                <!-- ========================================================= -->

                <div class="modal fade"
                     id="modalProgramarViaje"
                     tabindex="-1">


                    <div class="modal-dialog modal-lg">


                        <div class="modal-content">


                            <form action="${pageContext.servletContext.contextPath}/ViajesServlet"
                                  method="POST">


                                <div class="modal-header">


                                    <h5 class="modal-title">

                                        <i class="bi bi-calendar-plus"></i>

                                        Programar viaje regular

                                    </h5>


                                    <button type="button"
                                            class="btn-close"
                                            data-bs-dismiss="modal">
                                    </button>


                                </div>



                                <div class="modal-body">


                                    <input type="hidden"
                                           name="accion"
                                           value="programar">



                                    <div class="row">


                                        <!-- BUS -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Bus

                                            </label>


                                            <select name="busId"
                                                    class="form-select"
                                                    required>


                                                <option value="">

                                                    Seleccione un bus

                                                </option>


                                                <c:forEach var="bus"
                                                           items="${buses}">


                                                    <c:if test="${bus.estado}">


                                                        <option value="${bus.id}">

                                                            ${bus.placa}
                                                            -
                                                            ${bus.marca}
                                                            ${bus.modelo}

                                                        </option>


                                                    </c:if>


                                                </c:forEach>


                                            </select>


                                        </div>



                                        <!-- CHOFER -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Chofer

                                            </label>


                                            <select name="choferId"
                                                    class="form-select"
                                                    required>


                                                <option value="">

                                                    Seleccione un chofer

                                                </option>


                                                <c:forEach var="chofer"
                                                           items="${choferes}">


                                                    <c:if test="${chofer.estado}">


                                                        <option value="${chofer.id}">

                                                            ${chofer.nombre}
                                                            -
                                                            ${chofer.numeroLicencia}

                                                        </option>


                                                    </c:if>


                                                </c:forEach>


                                            </select>


                                        </div>



                                        <!-- RUTA -->

                                        <div class="col-md-12 mb-3">


                                            <label class="form-label">

                                                Ruta

                                            </label>


                                            <select name="rutaId"
                                                    class="form-select"
                                                    required>


                                                <option value="">

                                                    Seleccione una ruta

                                                </option>


                                                <c:forEach var="ruta"
                                                           items="${rutas}">


                                                    <option value="${ruta.id}">

                                                        ${ruta.origen.nombre}

                                                        →

                                                        ${ruta.destino.nombre}

                                                        -

                                                        ${ruta.distanciaKilometraje}

                                                    </option>


                                                </c:forEach>


                                            </select>


                                        </div>



                                        <!-- FECHA SALIDA -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Fecha de salida

                                            </label>


                                            <input type="date"
                                                   name="fechaSalida"
                                                   class="form-control"
                                                   required>


                                        </div>



                                        <!-- HORA SALIDA -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Hora de salida

                                            </label>


                                            <input type="time"
                                                   name="horaSalida"
                                                   class="form-control"
                                                   required>


                                        </div>



                                        <!-- FECHA LLEGADA -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Fecha estimada de llegada

                                            </label>


                                            <input type="date"
                                                   name="fechaLlegada"
                                                   class="form-control"
                                                   required>


                                        </div>



                                        <!-- HORA LLEGADA -->

                                        <div class="col-md-6 mb-3">


                                            <label class="form-label">

                                                Hora estimada de llegada

                                            </label>


                                            <input type="time"
                                                   name="horaLlegada"
                                                   class="form-control"
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

                                        <i class="bi bi-calendar-plus"></i>

                                        Programar viaje

                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>
