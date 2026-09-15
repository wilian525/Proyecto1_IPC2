<%-- 
    Document   : boletos
    Created on : 13 sep 2026, 10:04:55 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <div class="container py-4">
                <div class="text-center mb-4">
                    <h1 class="fw-bold">
                        <i class="bi bi-ticket-perforated"></i>
                        Compra de Boletos
                    </h1>
                    <p class="text-muted">
                        Consulta los viajes disponibles y selecciona
                        tus asientos.
                    </p>
                </div>

                <div class="mb-4">
                    <h3>
                        <i class="bi bi-bus-front"></i>
                        Viajes disponibles
                    </h3>
                </div>

                <c:choose>
                    <c:when test="${not empty viajes}">
                        <div class="row g-4">
                            <c:forEach items="${viajes}" var="viaje">
                                <div class="col-12">
                                    <div class="card shadow-sm">
                                        <div class="card-header">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <h5 class="mb-0">
                                                    <i class="bi bi-bus-front"></i>
                                                    Viaje #${viaje.id}
                                                </h5>

                                                <span class="badge bg-success">
                                                    Disponible
                                                </span>

                                            </div>
                                        </div>

                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-6 mb-3">
                                                    <h6 class="text-muted">
                                                        Ruta
                                                    </h6>
                                                    <p class="mb-0">
                                                        ${viaje.ruta.origen.nombre}
                                                        <i class="bi bi-arrow-right"></i>
                                                        ${viaje.ruta.destino.nombre}
                                                    </p>
                                                </div>

                                                <div class="col-md-3 mb-3">
                                                    <h6 class="text-muted">
                                                        Fecha de salida
                                                    </h6>
                                                    <p class="mb-0">
                                                        ${viaje.fechaSalida}
                                                    </p>
                                                </div>

                                                <div class="col-md-3 mb-3">
                                                    <h6 class="text-muted">
                                                        Hora de salida
                                                    </h6>
                                                    <p class="mb-0">
                                                        ${viaje.horaSalidaProgramada}
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-4 mb-3">
                                                    <h6 class="text-muted">
                                                        Precio por boleto
                                                    </h6>
                                                    <p class="fs-5 fw-bold mb-0">
                                                        Q ${viaje.ruta.precioBoleto}
                                                    </p>
                                                </div>

                                                <div class="col-md-4 mb-3">
                                                    <h6 class="text-muted">
                                                        Bus
                                                    </h6>
                                                    <p class="mb-0">
                                                        ${viaje.bus.placa}
                                                    </p>
                                                    <small class="text-muted">
                                                        ${viaje.bus.marca}
                                                        ${viaje.bus.modelo}
                                                    </small>
                                                </div>

                                                <div class="col-md-4 mb-3">
                                                    <h6 class="text-muted">
                                                        Capacidad
                                                    </h6>
                                                    <p class="mb-0">
                                                        ${viaje.bus.capacidad}
                                                        asientos
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="mt-3">
                                                <a href="${pageContext.servletContext.contextPath}/BoletoServlet?viajeId=${viaje.id}"
                                                   class="btn btn-primary">
                                                    <i class="bi bi-person-check"></i>
                                                    Seleccionar asientos
                                                </a>
                                            </div>

                                            <c:if test="${viajeId == viaje.id}">
                                                <hr class="my-4">
                                                <div class="card bg-light">
                                                    <div class="card-body">

                                                        <h5 class="card-title mb-3">
                                                            <i class="bi bi-grid-3x3-gap"></i>
                                                            Selección de asientos
                                                        </h5>

                                                        <p class="text-muted">
                                                            Selecciona los asientos que deseas comprar.
                                                            Los asientos ocupados no pueden ser seleccionados.
                                                        </p>

                                                        <form action="${pageContext.servletContext.contextPath}/BoletoServlet"
                                                              method="POST">

                                                            <!-- Identificador del viaje -->
                                                            <input type="hidden"
                                                                   name="viajeId"
                                                                   value="${viaje.id}">

                                                            <!-- ASIENTOS -->
                                                            <div class="row g-2 mb-4">
                                                                <c:forEach begin="1"
                                                                           end="${viaje.bus.capacidad}"
                                                                           var="numeroAsiento">
                                                                    <c:set var="ocupado" value="false"/>
     
                                                                    <!-- Verificar si el asiento ya está ocupado -->
                                                                    <c:forEach items="${asientosOcupados}"
                                                                               var="boleto">
                                                                        <c:if test="${boleto.asiento == numeroAsiento}">
                                                                            <c:set var="ocupado"
                                                                                   value="true"/>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                                                        <c:choose>

                                                                            <!-- ASIENTO OCUPADO -->
                                                                            <c:when test="${ocupado}">
                                                                                <div class="border rounded p-2 text-center bg-secondary text-white">
                                                                                    <i class="bi bi-person-fill"></i>
                                                                                    <br>
                                                                                    Asiento ${numeroAsiento}
                                                                                    <br>
                                                                                    <small>
                                                                                        Ocupado
                                                                                    </small>
                                                                                </div>
                                                                            </c:when>

                                                                            <!-- ASIENTO DISPONIBLE -->
                                                                            <c:otherwise>

                                                                                <div class="form-check border rounded p-2">
                                                                                    <input class="form-check-input"
                                                                                           type="checkbox"
                                                                                           name="asientos"
                                                                                           value="${numeroAsiento}"
                                                                                           id="asiento${viaje.id}_${numeroAsiento}">
                                                                                    <label class="form-check-label"
                                                                                           for="asiento${viaje.id}_${numeroAsiento}">
                                                                                        <i class="bi bi-person"></i>
                                                                                        Asiento ${numeroAsiento}
                                                                                    </label>
                                                                                </div>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </c:forEach>
                                                            </div>


                                                            <!-- FECHA DE PAGO -->
                                                            <div class="mb-3">
                                                                <label for="fechaPago${viaje.id}"
                                                                       class="form-label">
                                                                    Fecha de pago
                                                                </label>

                                                                <input type="date"
                                                                       class="form-control"
                                                                       id="fechaPago${viaje.id}"
                                                                       name="fechaPago"
                                                                       required>
                                                            </div>


                                                            <!-- INFORMACIÓN DEL PAGO -->
                                                            <div class="alert alert-info">
                                                                <i class="bi bi-wallet2"></i>
                                                                El pago del boleto se realizará utilizando
                                                                el saldo disponible en la cartera digital.
                                                            </div>

                                                            <!-- BOTONES -->
                                                            <div class="d-flex justify-content-between">

                                                                <a href="${pageContext.servletContext.contextPath}/BoletoServlet"
                                                                   class="btn btn-secondary">

                                                                    <i class="bi bi-arrow-left"></i>
                                                                    Cancelar
                                                                </a>
                                                                <button type="submit"
                                                                        class="btn btn-success">
                                                                    <i class="bi bi-ticket-perforated"></i>
                                                                    Comprar boletos

                                                                </button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>

                    <c:otherwise>

                        <div class="alert alert-warning text-center">
                            <i class="bi bi-calendar-x fs-1"></i>
                            <h4 class="mt-3">
                                No hay viajes disponibles
                            </h4>
                            <p class="mb-0">
                                Actualmente no existen viajes regulares
                                disponibles para comprar boletos.
                            </p>
                        </div>
                    </c:otherwise>
                </c:choose>

                <hr class="my-5">

                <div class="mb-4">
                    <h3>
                        <i class="bi bi-ticket"></i>
                        Mis boletos
                    </h3>
                </div>

                <c:choose>
                    <c:when test="${not empty misBoletos}">
                        <div class="row g-3">
                            <c:forEach items="${misBoletos}" var="boleto">
                                <div class="col-12 col-md-6">
                                    <div class="card shadow-sm">
                                        <div class="card-body">
                                            <h5 class="card-title">
                                                <i class="bi bi-ticket-perforated"></i>
                                                Boleto #${boleto.id}
                                            </h5>

                                            <p class="mb-1">
                                                <strong>Asiento:</strong>
                                                ${boleto.asiento}
                                            </p>

                                            <p class="mb-1">
                                                <strong>Fecha de pago:</strong>
                                                ${boleto.fechaPago}
                                            </p>

                                            <p class="mb-0">
                                                <strong>Precio:</strong>
                                                Q ${boleto.precio}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="alert alert-secondary">
                            <i class="bi bi-ticket"></i>
                            Todavía no tienes boletos comprados.
                        </div>
                    </c:otherwise>
                </c:choose>


                <div class="text-center mt-5">
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
