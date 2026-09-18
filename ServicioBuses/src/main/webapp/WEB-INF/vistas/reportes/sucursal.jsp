<%-- 
    Document   : sucursal
    Created on : 13 sep 2026, 10:09:36 p.m.
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
            <div class="container py-4">

                <!-- ENCABEZADO -->
                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>
                        <h1 class="fw-bold">
                            <i class="bi bi-building"></i>
                            Reportes de sucursal
                        </h1>

                        <p class="text-muted mb-0">
                            Información de buses, choferes, boletos, alquileres y depreciación.
                        </p>
                    </div>

                    <a href="${pageContext.request.contextPath}/"
                       class="btn btn-outline-secondary">
                        <i class="bi bi-house"></i>
                        Inicio
                    </a>

                </div>


                <!-- MENSAJE DE ERROR -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="bi bi-exclamation-triangle"></i>
                        ${error}
                    </div>
                </c:if>


                <!-- SELECTOR DE REPORTE -->
                <div class="card shadow-sm mb-4">

                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="bi bi-funnel"></i>
                            Seleccionar reporte
                        </h5>
                    </div>

                    <div class="card-body">

                        <form method="get"
                              action="${pageContext.request.contextPath}/ReporteSucursalServlet">

                            <div class="row g-3">

                                <!-- TIPO DE REPORTE -->
                                <div class="col-md-4">

                                    <label class="form-label fw-bold">
                                        Tipo de reporte
                                    </label>

                                    <select name="accion"
                                            class="form-select"
                                            required>

                                        <option value="buses"
                                                <c:if test="${tipo == 'buses'}">selected</c:if>>
                                                    Buses
                                                </option>

                                                <option value="choferes"
                                                <c:if test="${tipo == 'choferes'}">selected</c:if>>
                                                    Choferes
                                                </option>

                                                <option value="boletos"
                                                <c:if test="${tipo == 'boletos'}">selected</c:if>>
                                                    Ingresos por boletos
                                                </option>

                                                <option value="alquileres"
                                                <c:if test="${tipo == 'alquileres'}">selected</c:if>>
                                                    Ingresos por alquileres
                                                </option>

                                                <option value="depreciacion"
                                                <c:if test="${tipo == 'depreciacion'}">selected</c:if>>
                                                    Depreciación por bus
                                                </option>

                                        </select>

                                    </div>


                                    <!-- FECHA INICIO -->
                                    <div class="col-md-4">

                                        <label class="form-label fw-bold">
                                            Fecha inicio
                                        </label>

                                        <input type="date"
                                               name="fechaInicio"
                                               class="form-control"
                                               value="${fechaInicio}">

                                </div>


                                <!-- FECHA FIN -->
                                <div class="col-md-4">

                                    <label class="form-label fw-bold">
                                        Fecha fin
                                    </label>

                                    <input type="date"
                                           name="fechaFin"
                                           class="form-control"
                                           value="${fechaFin}">

                                </div>


                                <!-- ESTADO DEL BUS -->
                                <div class="col-md-4">

                                    <label class="form-label fw-bold">
                                        Estado del bus
                                    </label>

                                    <select name="estado"
                                            class="form-select">

                                        <option value="">
                                            Seleccione estado
                                        </option>

                                        <option value="true">
                                            Activos
                                        </option>

                                        <option value="false">
                                            Inactivos
                                        </option>

                                    </select>

                                </div>


                                <!-- FILTRO RUTA -->
                                <div class="col-md-4">

                                    <label class="form-label fw-bold">
                                        ID de ruta
                                    </label>

                                    <input type="number"
                                           name="rutaId"
                                           class="form-control"
                                           min="1"
                                           placeholder="Opcional">

                                </div>


                                <!-- FILTRO BUS -->
                                <div class="col-md-4">

                                    <label class="form-label fw-bold">
                                        ID de bus
                                    </label>

                                    <input type="number"
                                           name="busId"
                                           class="form-control"
                                           min="1"
                                           placeholder="Opcional">

                                </div>

                            </div>


                            <div class="mt-4">

                                <button type="submit"
                                        class="btn btn-primary">

                                    <i class="bi bi-search"></i>
                                    Generar reporte

                                </button>

                                <button type="submit"
                                        name="exportar"
                                        value="true"
                                        class="btn btn-success ms-2">

                                    <i class="bi bi-file-earmark-code"></i>
                                    Exportar HTML

                                </button>

                            </div>

                        </form>

                    </div>
                </div>


                <!-- ===================================================== -->
                <!-- REPORTE DE BUSES -->
                <!-- ===================================================== -->

                <c:if test="${tipo == 'buses'}">

                    <div class="card shadow-sm">

                        <div class="card-header">
                            <h4 class="mb-0">
                                <i class="bi bi-bus-front"></i>
                                Buses de la sucursal
                            </h4>
                        </div>

                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay buses para mostrar.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover align-middle">

                                            <thead class="table-dark">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Placa</th>
                                                    <th>Marca</th>
                                                    <th>Modelo</th>
                                                    <th>Año</th>
                                                    <th>Capacidad</th>
                                                    <th>Kilometraje</th>
                                                    <th>Estado</th>
                                                    <th>Chofer actual</th>
                                                    <th>Viajes realizados</th>
                                                </tr>
                                            </thead>

                                            <tbody>

                                                <c:forEach var="bus" items="${datos}">

                                                    <tr>

                                                        <td>${bus.id}</td>

                                                        <td>${bus.placa}</td>

                                                        <td>${bus.marca}</td>

                                                        <td>${bus.modelo}</td>

                                                        <td>${bus.añoFabricacion}</td>

                                                        <td>${bus.capacidad}</td>

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
                                                                    <span class="badge bg-secondary">
                                                                        Inactivo
                                                                    </span>
                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>

                                                        <td>

                                                            <c:set var="choferActual"
                                                                   value="${reporte.obtenerChoferActual(bus.id)}"/>

                                                            <c:choose>

                                                                <c:when test="${not empty choferActual}">
                                                                    ${choferActual.nombre}
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <span class="text-muted">
                                                                        Sin chofer asignado
                                                                    </span>
                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>

                                                        <td>
                                                            ${reporte.totalViajesRealizados(bus.id)}
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

                </c:if>


                <!-- ===================================================== -->
                <!-- REPORTE DE CHOFERES -->
                <!-- ===================================================== -->

                <c:if test="${tipo == 'choferes'}">

                    <div class="card shadow-sm">

                        <div class="card-header">
                            <h4 class="mb-0">
                                <i class="bi bi-person-badge"></i>
                                Choferes de la sucursal
                            </h4>
                        </div>

                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay choferes registrados en la sucursal.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>
                                                    <th>ID</th>
                                                    <th>Nombre</th>
                                                    <th>NIT</th>
                                                    <th>DPI</th>
                                                    <th>Teléfono</th>
                                                    <th>Licencia</th>
                                                    <th>Tipo</th>
                                                    <th>Vencimiento</th>
                                                    <th>Salario por viaje</th>
                                                    <th>Viajes realizados</th>
                                                </tr>

                                            </thead>

                                            <tbody>

                                                <c:forEach var="chofer" items="${datos}">

                                                    <tr>

                                                        <td>${chofer.id}</td>

                                                        <td>${chofer.nombre}</td>

                                                        <td>${chofer.nit}</td>

                                                        <td>${chofer.dpi}</td>

                                                        <td>${chofer.telefono}</td>

                                                        <td>${chofer.numeroLicencia}</td>

                                                        <td>${chofer.tipoLicencia}</td>

                                                        <td>${chofer.fechaVencimiento}</td>

                                                        <td>
                                                            Q ${chofer.salarioBasePorViaje}
                                                        </td>

                                                        <td>
                                                            ${reporte.totalViajesRealizado(chofer.id)}
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

                </c:if>


                <!-- ===================================================== -->
                <!-- INGRESOS POR BOLETOS -->
                <!-- ===================================================== -->

                <c:if test="${tipo == 'boletos'}">

                    <div class="card shadow-sm">

                        <div class="card-header">

                            <h4 class="mb-0">
                                <i class="bi bi-ticket-perforated"></i>
                                Ingresos por boletos
                            </h4>

                        </div>

                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay viajes con boletos vendidos para los filtros seleccionados.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>
                                                    <th>Viaje</th>
                                                    <th>Fecha</th>
                                                    <th>Bus</th>
                                                    <th>Ruta</th>
                                                    <th>Boletos vendidos</th>
                                                    <th>Ingreso total</th>
                                                </tr>

                                            </thead>

                                            <tbody>

                                                <c:forEach var="viaje" items="${datos}">

                                                    <c:set var="ruta"
                                                           value="${reporte.obtenerRuta(viaje.id)}"/>

                                                    <tr>

                                                        <td>
                                                            ${viaje.id}
                                                        </td>

                                                        <td>
                                                            ${viaje.fechaSalida}
                                                        </td>

                                                        <td>
                                                            ${viaje.bus.placa}
                                                        </td>

                                                        <td>

                                                            <c:choose>

                                                                <c:when test="${not empty ruta}">
                                                                    ${ruta.origen.nombre}
                                                                    →
                                                                    ${ruta.destino.nombre}
                                                                </c:when>

                                                                <c:otherwise>
                                                                    Sin ruta
                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>

                                                        <td>
                                                            ${reporte.cantidadBoletos(viaje.id, fechaInicio, fechaFin)}
                                                        </td>

                                                        <td>
                                                            Q ${reporte.ingresoTotal(viaje.id, fechaInicio, fechaFin)}
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

                </c:if>


                <!-- ===================================================== -->
                <!-- INGRESOS POR ALQUILERES -->
                <!-- ===================================================== -->

                <c:if test="${tipo == 'alquileres'}">

                    <div class="card shadow-sm">

                        <div class="card-header">

                            <h4 class="mb-0">
                                <i class="bi bi-calendar-check"></i>
                                Ingresos por alquileres
                            </h4>

                        </div>

                        <div class="card-body">

                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay alquileres pagados para los filtros seleccionados.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>
                                                    <th>Viaje</th>
                                                    <th>Cliente</th>
                                                    <th>Origen</th>
                                                    <th>Destino</th>
                                                    <th>Fecha salida</th>
                                                    <th>Pasajeros</th>
                                                    <th>Bus</th>
                                                    <th>Precio confirmado</th>
                                                    <th>Fecha pago</th>
                                                </tr>

                                            </thead>

                                            <tbody>

                                                <c:forEach var="alquiler" items="${datos}">

                                                    <c:set var="cliente"
                                                           value="${reporte.obtenerCliente(alquiler)}"/>

                                                    <tr>

                                                        <td>
                                                            ${alquiler.id}
                                                        </td>

                                                        <td>

                                                            <c:choose>

                                                                <c:when test="${not empty cliente}">
                                                                    ${cliente.nombre}
                                                                </c:when>

                                                                <c:otherwise>
                                                                    Cliente no encontrado
                                                                </c:otherwise>

                                                            </c:choose>

                                                        </td>

                                                        <td>
                                                            ${alquiler.origen}
                                                        </td>

                                                        <td>
                                                            ${alquiler.destino}
                                                        </td>

                                                        <td>
                                                            ${alquiler.fechaSalida}
                                                        </td>

                                                        <td>
                                                            ${alquiler.numeroPasajeros}
                                                        </td>

                                                        <td>
                                                            ${alquiler.bus.placa}
                                                        </td>

                                                        <td>
                                                            Q ${alquiler.precioConfirmado}
                                                        </td>

                                                        <td>
                                                            ${alquiler.fechaPago}
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

                </c:if>


                <!-- ===================================================== -->
                <!-- DEPRECIACIÓN -->
                <!-- ===================================================== -->

                <c:if test="${tipo == 'depreciacion'}">

                    <div class="card shadow-sm">

                        <div class="card-header">

                            <h4 class="mb-0">
                                <i class="bi bi-graph-down"></i>
                                Depreciación por bus
                            </h4>

                        </div>

                        <div class="card-body">

                            <div class="alert alert-secondary">

                                <i class="bi bi-info-circle"></i>

                                Monto actual de depreciación por kilómetro:
                                <strong>
                                    Q ${reporte.montoPorKilometroActual()}
                                </strong>

                            </div>


                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay buses registrados en la sucursal.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover align-middle">

                                            <thead class="table-dark">

                                                <tr>
                                                    <th>ID</th>
                                                    <th>Placa</th>
                                                    <th>Marca</th>
                                                    <th>Modelo</th>
                                                    <th>Kilometraje recorrido</th>
                                                    <th>Depreciación acumulada</th>
                                                </tr>

                                            </thead>

                                            <tbody>

                                                <c:forEach var="bus" items="${datos}">

                                                    <tr>

                                                        <td>
                                                            ${bus.id}
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
                                                            ${reporte.totalKilometraje(bus.id)}
                                                        </td>

                                                        <td>
                                                            Q ${reporte.depreciacionAcumulada(bus.id)}
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

                </c:if>


            </div>
        </main>
    </body>
</html>
