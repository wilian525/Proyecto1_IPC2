<%-- 
    Document   : alquileres
    Created on : 13 sep 2026, 10:05:12 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alquileres de buses</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <main>

            <jsp:include page="/includes/header.jsp"/>

            <div class="container py-4">

                <!-- TÍTULO -->
                <div class="text-center mb-4">
                    <h1 class="fw-bold">
                        <i class="bi bi-bus-front"></i>
                        Alquiler de buses
                    </h1>

                    <p class="text-muted">
                        Solicita y administra alquileres de buses para viajes privados.
                    </p>
                </div>


                <!-- MENSAJES -->
                <c:if test="${param.resultado == 'true'}">
                    <div class="alert alert-success alert-dismissible fade show"
                         role="alert">

                        <i class="bi bi-check-circle"></i>
                        La operación se realizó correctamente.

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
                        No fue posible realizar la operación revise que cuente con saldo disponible.

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="alert">
                        </button>
                    </div>
                </c:if>

                <!-- SOLICITAR ALQUILER - SOLO USUARIO -->
                <c:if test="${not esAdministradorSucursal}">

                    <div class="card shadow-sm mb-5">

                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="bi bi-plus-circle"></i>
                                Solicitar alquiler
                            </h5>
                        </div>

                        <div class="card-body">

                            <form action="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                  method="POST">

                                <input type="hidden"
                                       name="accion"
                                       value="solicitar">


                                <div class="row">

                                    <!-- SUCURSAL -->
                                    <div class="col-md-6 mb-3">

                                        <label for="sucursalId" class="form-label">
                                            Sucursal
                                        </label>

                                        <select class="form-select"
                                                id="sucursalId"
                                                name="sucursalId"
                                                required>

                                            <option value="">
                                                Seleccione una sucursal
                                            </option>

                                            <c:forEach items="${sucursales}"
                                                       var="sucursal">

                                                <option value="${sucursal.id}">
                                                    ${sucursal.nombre}
                                                </option>

                                            </c:forEach>

                                        </select>

                                    </div>


                                    <!-- PASAJEROS -->
                                    <div class="col-md-6 mb-3">

                                        <label for="pasajeros" class="form-label">
                                            Número de pasajeros
                                        </label>

                                        <input type="number"
                                               class="form-control"
                                               id="pasajeros"
                                               name="pasajeros"
                                               min="1"
                                               required>

                                    </div>

                                </div>


                                <div class="row">

                                    <!-- ORIGEN -->
                                    <div class="col-md-6 mb-3">

                                        <label for="origen" class="form-label">
                                            Origen
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="origen"
                                               name="origen"
                                               maxlength="150"
                                               required>

                                    </div>


                                    <!-- DESTINO -->
                                    <div class="col-md-6 mb-3">

                                        <label for="destino" class="form-label">
                                            Destino
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               id="destino"
                                               name="destino"
                                               maxlength="150"
                                               required>

                                    </div>

                                </div>


                                <div class="row">

                                    <!-- FECHA SALIDA -->
                                    <div class="col-md-6 mb-3">

                                        <label for="fechaSalida" class="form-label">
                                            Fecha de salida
                                        </label>

                                        <input type="date"
                                               class="form-control"
                                               id="fechaSalida"
                                               name="fechaSalida"
                                               required>

                                    </div>


                                    <!-- HORA SALIDA -->
                                    <div class="col-md-6 mb-3">

                                        <label for="horaSalida" class="form-label">
                                            Hora de salida
                                        </label>

                                        <input type="time"
                                               class="form-control"
                                               id="horaSalida"
                                               name="horaSalida"
                                               required>

                                    </div>

                                </div>


                                <div class="row">

                                    <!-- FECHA RETORNO -->
                                    <div class="col-md-6 mb-3">

                                        <label for="fechaRetorno" class="form-label">
                                            Fecha de retorno
                                        </label>

                                        <input type="date"
                                               class="form-control"
                                               id="fechaRetorno"
                                               name="fechaRetorno">

                                        <small class="text-muted">
                                            Opcional para viajes de ida y vuelta.
                                        </small>

                                    </div>


                                    <!-- FECHA LLEGADA -->
                                    <div class="col-md-6 mb-3">

                                        <label for="fechaLlegadaEstimada" class="form-label">
                                            Fecha estimada de llegada
                                        </label>

                                        <input type="date"
                                               class="form-control"
                                               id="fechaLlegadaEstimada"
                                               name="fechaLlegadaEstimada"
                                               required>

                                    </div>

                                </div>


                                <div class="row">

                                    <!-- HORA LLEGADA -->
                                    <div class="col-md-6 mb-3">

                                        <label for="horaLlegadaEstimada" class="form-label">
                                            Hora estimada de llegada
                                        </label>

                                        <input type="time"
                                               class="form-control"
                                               id="horaLlegadaEstimada"
                                               name="horaLlegadaEstimada"
                                               required>

                                    </div>

                                </div>


                                <div class="alert alert-info">

                                    <i class="bi bi-info-circle"></i>

                                    El sistema calculará un precio estimado.
                                    El administrador de sucursal confirmará el precio final.

                                </div>


                                <div class="d-grid">

                                    <button type="submit"
                                            class="btn btn-primary">

                                        <i class="bi bi-send"></i>
                                        Solicitar alquiler

                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </c:if>


                <!-- LISTADO -->
                <h3 class="mb-4">

                    <i class="bi bi-list-ul"></i>
                    Alquileres

                </h3>


                <c:choose>

                    <c:when test="${not empty alquileres}">

                        <div class="row g-4">

                            <c:forEach items="${alquileres}"
                                       var="alquiler">

                                <div class="col-12">

                                    <div class="card shadow-sm">

                                        <!-- ENCABEZADO -->
                                        <div class="card-header">

                                            <div class="d-flex justify-content-between align-items-center">

                                                <h5 class="mb-0">

                                                    <i class="bi bi-bus-front"></i>

                                                    Alquiler #${alquiler.id}

                                                </h5>


                                                <c:choose>

                                                    <c:when test="${alquiler.estadoAlquiler}">

                                                        <span class="badge bg-success">
                                                            Pagado
                                                        </span>

                                                    </c:when>

                                                    <c:otherwise>

                                                        <span class="badge bg-warning text-dark">
                                                            Pendiente de pago
                                                        </span>

                                                    </c:otherwise>

                                                </c:choose>

                                            </div>

                                        </div>


                                        <div class="card-body">

                                            <!-- INFORMACIÓN -->
                                            <div class="row">

                                                <div class="col-md-6 mb-3">

                                                    <h6 class="text-muted">
                                                        Ruta
                                                    </h6>

                                                    <p class="mb-0">

                                                        ${alquiler.origen}

                                                        <i class="bi bi-arrow-right"></i>

                                                        ${alquiler.destino}

                                                    </p>

                                                </div>


                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Pasajeros
                                                    </h6>

                                                    <p class="mb-0">
                                                        ${alquiler.numeroPasajeros}
                                                    </p>

                                                </div>


                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Fecha de salida
                                                    </h6>

                                                    <p class="mb-0">
                                                        ${alquiler.fechaSalida}
                                                    </p>

                                                </div>

                                            </div>


                                            <!-- FECHAS -->
                                            <div class="row">

                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Hora de salida
                                                    </h6>

                                                    <p>
                                                        ${alquiler.horaSalidaProgramada}
                                                    </p>

                                                </div>


                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Fecha retorno
                                                    </h6>

                                                    <p>

                                                        <c:choose>

                                                            <c:when test="${alquiler.fechaRetorno != null}">
                                                                ${alquiler.fechaRetorno}
                                                            </c:when>

                                                            <c:otherwise>
                                                                No aplica
                                                            </c:otherwise>

                                                        </c:choose>

                                                    </p>

                                                </div>


                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Llegada estimada
                                                    </h6>

                                                    <p>
                                                        ${alquiler.fechaLlegadaEstimada}
                                                    </p>

                                                </div>


                                                <div class="col-md-3 mb-3">

                                                    <h6 class="text-muted">
                                                        Hora llegada
                                                    </h6>

                                                    <p>
                                                        ${alquiler.horaLlegadaEstimada}
                                                    </p>

                                                </div>

                                            </div>


                                            <hr>


                                            <!-- PRECIOS -->
                                            <div class="row">

                                                <div class="col-md-4 mb-3">

                                                    <h6 class="text-muted">
                                                        Precio estimado
                                                    </h6>

                                                    <p class="fs-5 fw-bold">
                                                        Q ${alquiler.precioEstimado}
                                                    </p>

                                                </div>


                                                <div class="col-md-4 mb-3">

                                                    <h6 class="text-muted">
                                                        Precio confirmado
                                                    </h6>

                                                    <p class="fs-5 fw-bold">

                                                        <c:choose>

                                                            <c:when test="${alquiler.precioConfirmado > 0}">
                                                                Q ${alquiler.precioConfirmado}
                                                            </c:when>

                                                            <c:otherwise>
                                                                Pendiente
                                                            </c:otherwise>

                                                        </c:choose>

                                                    </p>

                                                </div>


                                                <div class="col-md-4 mb-3">

                                                    <h6 class="text-muted">
                                                        Fecha de pago
                                                    </h6>

                                                    <p>

                                                        <c:choose>

                                                            <c:when test="${alquiler.fechaPago != null}">
                                                                ${alquiler.fechaPago}
                                                            </c:when>

                                                            <c:otherwise>
                                                                Pendiente
                                                            </c:otherwise>

                                                        </c:choose>

                                                    </p>

                                                </div>

                                            </div>


                                            <!-- BUS Y CHOFER -->
                                            <div class="row">

                                                <div class="col-md-6 mb-3">

                                                    <h6 class="text-muted">
                                                        Bus asignado
                                                    </h6>

                                                    <c:choose>

                                                        <c:when test="${alquiler.bus != null}">

                                                            <p class="mb-0">
                                                                ${alquiler.bus.placa}
                                                            </p>

                                                            <small class="text-muted">

                                                                ${alquiler.bus.marca}
                                                                ${alquiler.bus.modelo}

                                                            </small>

                                                        </c:when>

                                                        <c:otherwise>

                                                            <span class="text-muted">
                                                                Sin bus asignado
                                                            </span>

                                                        </c:otherwise>

                                                    </c:choose>

                                                </div>


                                                <div class="col-md-6 mb-3">

                                                    <h6 class="text-muted">
                                                        Chofer asignado
                                                    </h6>

                                                    <c:choose>

                                                        <c:when test="${alquiler.chofer != null}">

                                                            <p class="mb-0">
                                                                ${alquiler.chofer.nombre}
                                                            </p>

                                                            <small class="text-muted">

                                                                Licencia:
                                                                ${alquiler.chofer.numeroLicencia}

                                                            </small>

                                                        </c:when>

                                                        <c:otherwise>

                                                            <span class="text-muted">
                                                                Sin chofer asignado
                                                            </span>

                                                        </c:otherwise>

                                                    </c:choose>

                                                </div>

                                            </div>


                                            <!-- ACCIONES -->
                                            <div class="mt-3">


                                                <!-- PAGAR - SOLO USUARIO -->
                                                <c:if test="${not esAdministradorSucursal
                                                              and not alquiler.estadoAlquiler
                                                              and alquiler.precioConfirmado > 0}">

                                                      <form action="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                                            method="POST"
                                                            class="mb-3">

                                                          <input type="hidden"
                                                                 name="accion"
                                                                 value="pagar">

                                                          <input type="hidden"
                                                                 name="viajeId"
                                                                 value="${alquiler.id}">


                                                          <div class="row align-items-end">

                                                              <div class="col-md-4">

                                                                  <label class="form-label">
                                                                      Fecha de pago
                                                                  </label>

                                                                  <input type="date"
                                                                         class="form-control"
                                                                         name="fechaPago"
                                                                         required>

                                                              </div>


                                                              <div class="col-md-4">

                                                                  <button type="submit"
                                                                          class="btn btn-success">

                                                                      <i class="bi bi-wallet2"></i>
                                                                      Pagar alquiler

                                                                  </button>

                                                              </div>

                                                          </div>

                                                      </form>

                                                </c:if>


                                                <!-- CONFIRMAR PRECIO - SOLO ADMINISTRADOR DE SUCURSAL -->
                                                <c:if test="${esAdministradorSucursal
                                                              and alquiler.precioConfirmado <= 0}">

                                                      <form action="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                                            method="POST"
                                                            class="mb-3">

                                                          <input type="hidden"
                                                                 name="accion"
                                                                 value="confirmar">

                                                          <input type="hidden"
                                                                 name="viajeId"
                                                                 value="${alquiler.id}">


                                                          <div class="row align-items-end">

                                                              <div class="col-md-4">

                                                                  <label class="form-label">
                                                                      Confirmar precio
                                                                  </label>

                                                                  <div class="input-group">

                                                                      <span class="input-group-text">
                                                                          Q
                                                                      </span>

                                                                      <input type="number"
                                                                             class="form-control"
                                                                             name="precio"
                                                                             min="0.01"
                                                                             step="0.01"
                                                                             value="${alquiler.precioEstimado}"
                                                                             required>

                                                                  </div>

                                                              </div>


                                                              <div class="col-md-4">

                                                                  <button type="submit"
                                                                          class="btn btn-primary">

                                                                      <i class="bi bi-check-circle"></i>
                                                                      Confirmar precio

                                                                  </button>

                                                              </div>

                                                          </div>

                                                      </form>

                                                </c:if>


                                                <!-- ASIGNAR BUS Y CHOFER - SOLO ADMINISTRADOR DE SUCURSAL -->
                                                <c:if test="${esAdministradorSucursal
                                                              and alquiler.estadoAlquiler
                                                              and alquiler.bus == null}">

                                                      <form action="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                                            method="POST"
                                                            class="mb-3">

                                                          <input type="hidden"
                                                                 name="accion"
                                                                 value="asignar">

                                                          <input type="hidden"
                                                                 name="viajeId"
                                                                 value="${alquiler.id}">


                                                          <div class="row align-items-end">

                                                              <div class="col-md-4">

                                                                  <label class="form-label">
                                                                      Bus
                                                                  </label>

                                                                  <select class="form-select"
                                                                          name="busId"
                                                                          required>

                                                                      <option value="">
                                                                          Seleccione un bus
                                                                      </option>

                                                                      <c:forEach items="${buses}"
                                                                                 var="bus">

                                                                          <option value="${bus.id}">

                                                                              ${bus.placa}
                                                                              -
                                                                              ${bus.marca}
                                                                              ${bus.modelo}

                                                                          </option>

                                                                      </c:forEach>

                                                                  </select>

                                                              </div>


                                                              <div class="col-md-4">

                                                                  <label class="form-label">
                                                                      Chofer
                                                                  </label>

                                                                  <select class="form-select"
                                                                          name="choferId"
                                                                          required>

                                                                      <option value="">
                                                                          Seleccione un chofer
                                                                      </option>

                                                                      <c:forEach items="${choferes}"
                                                                                 var="chofer">

                                                                          <option value="${chofer.id}">

                                                                              ${chofer.nombre}
                                                                              -
                                                                              ${chofer.numeroLicencia}

                                                                          </option>

                                                                      </c:forEach>

                                                                  </select>

                                                              </div>


                                                              <div class="col-md-4">

                                                                  <button type="submit"
                                                                          class="btn btn-warning">

                                                                      <i class="bi bi-person-check"></i>
                                                                      Asignar recursos

                                                                  </button>

                                                              </div>

                                                          </div>

                                                      </form>

                                                </c:if>


                                                <!-- ELIMINAR -->
                                                <c:if test="${not alquiler.estadoAlquiler}">

                                                    <form action="${pageContext.servletContext.contextPath}/AlquilerServlet"
                                                          method="POST"
                                                          class="d-inline">

                                                        <input type="hidden"
                                                               name="accion"
                                                               value="eliminar">

                                                        <input type="hidden"
                                                               name="viajeId"
                                                               value="${alquiler.id}">


                                                        <button type="submit"
                                                                class="btn btn-danger"
                                                                onclick="return confirm('¿Está seguro de eliminar este alquiler?');">

                                                            <i class="bi bi-trash"></i>
                                                            Eliminar

                                                        </button>

                                                    </form>

                                                </c:if>

                                            </div>

                                        </div>

                                    </div>

                                </div>

                            </c:forEach>

                        </div>

                    </c:when>


                    <c:otherwise>

                        <div class="alert alert-secondary text-center">

                            <i class="bi bi-bus-front fs-1"></i>

                            <h4 class="mt-3">
                                No hay alquileres
                            </h4>

                            <p class="mb-0">
                                Todavía no existen solicitudes de alquiler.
                            </p>

                        </div>

                    </c:otherwise>

                </c:choose>


                <!-- REGRESAR -->
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
