<%-- 
    Document   : sistema
    Created on : 13 sep 2026, 10:09:20 p.m.
    Author     : wilian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reportes - Administrador del Sistema</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
        <div class="container-fluid mt-4">

            <!-- ===================================================== -->
            <!-- TITULO -->
            <!-- ===================================================== -->

            <div class="d-flex justify-content-between align-items-center mb-4">

                <div>

                    <h1>
                        <i class="bi bi-bar-chart"></i>
                        Reportes del sistema
                    </h1>

                    <p class="text-muted">
                        Información general de las sucursales
                    </p>

                </div>

                <a href="${pageContext.request.contextPath}/AdminSistemaServlet"
                   class="btn btn-secondary">

                    <i class="bi bi-arrow-left"></i>
                    Regresar

                </a>

            </div>


            <!-- ===================================================== -->
            <!-- MENSAJE DE ERROR -->
            <!-- ===================================================== -->

            <c:if test="${not empty error}">

                <div class="alert alert-danger">

                    ${error}

                </div>

            </c:if>


            <!-- ===================================================== -->
            <!-- FILTROS -->
            <!-- ===================================================== -->

            <div class="card mb-4">

                <div class="card-header">

                    <h5 class="mb-0">

                        <i class="bi bi-funnel"></i>
                        Filtros

                    </h5>

                </div>


                <div class="card-body">

                    <form method="GET"
                          action="${pageContext.request.contextPath}/ReporteSistemaServlet">

                        <!-- TIPO DE REPORTE -->

                        <div class="row">

                            <div class="col-md-3 mb-3">

                                <label class="form-label">
                                    Tipo de reporte
                                </label>

                                <select name="accion"
                                        class="form-select"
                                        required>

                                    <option value="ganancia"
                                            ${tipo == 'ganancia' ? 'selected' : ''}>

                                        Ganancias

                                    </option>

                                    <option value="rutas"
                                            ${tipo == 'rutas' ? 'selected' : ''}>

                                        Rutas más demandadas

                                    </option>

                                    <option value="costo"
                                            ${tipo == 'costo' ? 'selected' : ''}>

                                        Costos operativos

                                    </option>

                                    <option value="mapa"
                                            ${tipo == 'mapa' ? 'selected' : ''}>

                                        Mapa de rutas

                                    </option>

                                </select>

                            </div>


                            <!-- SUCURSAL -->

                            <div class="col-md-3 mb-3">

                                <label class="form-label">

                                    Sucursal

                                </label>

                                <select name="sucursalId"
                                        class="form-select">

                                    <option value="">

                                        Todas las sucursales

                                    </option>

                                    <c:forEach var="sucursal"
                                               items="${sucursales}">

                                        <option value="${sucursal.id}"
                                                ${sucursal.id == sucursalId ? 'selected' : ''}>

                                            ${sucursal.nombre}

                                        </option>

                                    </c:forEach>

                                </select>

                            </div>


                            <!-- FECHA INICIO -->

                            <div class="col-md-3 mb-3">

                                <label class="form-label">

                                    Fecha inicio

                                </label>

                                <input type="date"
                                       name="fechaInicio"
                                       class="form-control"
                                       value="${fechaInicio}">

                            </div>


                            <!-- FECHA FIN -->

                            <div class="col-md-3 mb-3">

                                <label class="form-label">

                                    Fecha fin

                                </label>

                                <input type="date"
                                       name="fechaFin"
                                       class="form-control"
                                       value="${fechaFin}">

                            </div>

                        </div>


                        <!-- BOTON -->

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

                    </form>

                </div>

            </div>


            <!-- ===================================================== -->
            <!-- REPORTE GANANCIAS -->
            <!-- ===================================================== -->

            <c:if test="${tipo == 'ganancia'}">

                <div class="card">

                    <div class="card-header">

                        <h4 class="mb-0">

                            <i class="bi bi-cash-stack"></i>

                            Reporte de ganancias

                        </h4>

                    </div>


                    <div class="card-body">

                        <c:choose>

                            <c:when test="${empty dato}">

                                <div class="alert alert-info">

                                    No hay sucursales para mostrar.

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-striped">

                                        <thead>

                                            <tr>

                                                <th>
                                                    Sucursal
                                                </th>

                                                <th>
                                                    Ingresos
                                                </th>

                                                <th>
                                                    Costos
                                                </th>

                                                <th>
                                                    Ganancia neta
                                                </th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach var="sucursal"
                                                       items="${dato}">

                                                <tr>

                                                    <td>

                                                        ${sucursal.nombre}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalIngreso(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalCostos(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.gananciaNeta(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

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
            <!-- RUTAS MÁS DEMANDADAS -->
            <!-- ===================================================== -->

            <c:if test="${tipo == 'rutas'}">

                <div class="card">

                    <div class="card-header">

                        <h4 class="mb-0">

                            <i class="bi bi-signpost-split"></i>

                            Rutas más demandadas

                        </h4>

                    </div>


                    <div class="card-body">

                        <c:choose>

                            <c:when test="${empty dato}">

                                <div class="alert alert-info">

                                    No hay rutas para mostrar.

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-striped">

                                        <thead>

                                            <tr>

                                                <th>
                                                    #
                                                </th>

                                                <th>
                                                    Origen
                                                </th>

                                                <th>
                                                    Destino
                                                </th>

                                                <th>
                                                    Distancia
                                                </th>

                                                <th>
                                                    Precio
                                                </th>

                                                <th>
                                                    Boletos vendidos
                                                </th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach var="ruta"
                                                       items="${dato}"
                                                       varStatus="estado">

                                                <tr>

                                                    <td>

                                                        ${estado.count}

                                                    </td>


                                                    <td>

                                                        ${ruta.origen.nombre}

                                                    </td>


                                                    <td>

                                                        ${ruta.destino.nombre}

                                                    </td>


                                                    <td>

                                                        ${ruta.distanciaKilometraje} km

                                                    </td>


                                                    <td>

                                                        Q ${ruta.precioBoleto}

                                                    </td>


                                                    <td>

                                                        ${reporte.totalBoletoVendidos(
                                                          ruta.id,
                                                          fechaInicio,
                                                          fechaFin
                                                          )}

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
            <!-- COSTOS OPERATIVOS -->
            <!-- ===================================================== -->

            <c:if test="${tipo == 'costo'}">

                <div class="card">

                    <div class="card-header">

                        <h4 class="mb-0">

                            <i class="bi bi-tools"></i>

                            Costos operativos

                        </h4>

                    </div>


                    <div class="card-body">

                        <c:choose>

                            <c:when test="${empty datos}">

                                <div class="alert alert-info">

                                    No hay información para mostrar.

                                </div>

                            </c:when>


                            <c:otherwise>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-striped">

                                        <thead>

                                            <tr>

                                                <th>
                                                    Sucursal
                                                </th>

                                                <th>
                                                    Combustible
                                                </th>

                                                <th>
                                                    Mano de obra
                                                </th>

                                                <th>
                                                    Repuestos
                                                </th>

                                                <th>
                                                    Depreciación
                                                </th>

                                                <th>
                                                    Total
                                                </th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            <c:forEach var="sucursal"
                                                       items="${datos}">

                                                <tr>

                                                    <td>

                                                        ${sucursal.nombre}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalCombustible(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalManoObra(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalRepuesto(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.totalDepreciacion(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

                                                    </td>


                                                    <td>

                                                        Q ${reporte.granTotal(
                                                            sucursal.id,
                                                            fechaInicio,
                                                            fechaFin
                                                            )}

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
            <!-- MAPA DE RUTAS -->
            <!-- ===================================================== -->

            <c:if test="${tipo == 'mapa'}">

                <div class="card shadow-sm">

                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="bi bi-map"></i>
                            Mapa de rutas
                        </h4>
                    </div>

                    <div class="card-body">

                        <!-- FILTRO POR SUCURSAL -->
                        <form method="get"
                              action="${pageContext.request.contextPath}/ReporteSistemaServlet"
                              class="row g-3 mb-4">

                            <input type="hidden"
                                   name="accion"
                                   value="mapa">

                            <div class="col-md-8">

                                <label class="form-label fw-bold">
                                    Sucursal de origen
                                </label>

                                <select name="sucursalId"
                                        class="form-select"
                                        required>

                                    <option value="">
                                        Seleccione una sucursal
                                    </option>

                                    <c:forEach var="sucursal"
                                               items="${sucursales}">

                                        <option value="${sucursal.id}"
                                                <c:if test="${sucursalId == sucursal.id}">
                                                    selected
                                                </c:if>>
                                            ${sucursal.nombre} - ${sucursal.direccion}
                                        </option>

                                    </c:forEach>

                                </select>

                            </div>

                            <div class="col-md-4 d-flex align-items-end">

                                <button type="submit"
                                        class="btn btn-primary w-100">

                                    <i class="bi bi-search"></i>
                                    Mostrar rutas

                                </button>

                            </div>

                        </form>


                        <!-- MAPA -->
                        <div id="mapaRutas"
                             style="height: 550px; width: 100%; border-radius: 10px;">
                        </div>


                        <!-- INFORMACIÓN DE LAS RUTAS -->
                        <div class="mt-4">

                            <h5>
                                <i class="bi bi-signpost-2"></i>
                                Rutas encontradas
                            </h5>

                            <c:choose>

                                <c:when test="${empty datos}">

                                    <div class="alert alert-info">
                                        No hay rutas cuyo origen sea la sucursal seleccionada.
                                    </div>

                                </c:when>

                                <c:otherwise>

                                    <div class="table-responsive">

                                        <table class="table table-striped table-hover">

                                            <thead class="table-dark">

                                                <tr>
                                                    <th>Ruta</th>
                                                    <th>Origen</th>
                                                    <th>Destino</th>
                                                    <th>Distancia</th>
                                                    <th>Precio boleto</th>
                                                </tr>

                                            </thead>

                                            <tbody>

                                                <c:forEach var="ruta"
                                                           items="${datos}">

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
                                                            ${ruta.distanciaKilometraje} km
                                                        </td>

                                                        <td>
                                                            Q ${ruta.precioBoleto}
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


                <!-- ================================================= -->
                <!-- LEAFLET -->
                <!-- ================================================= -->

                <link rel="stylesheet"
                      href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
                      integrity="sha256-p4NxAoJBhIINfQ3iZ9nN3W2b8Jw5j3sV5LJ5N9M9P9M="
                      crossorigin=""/>

                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
                        integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
                        crossorigin="">
                </script>


                <script>

                    /*
                     * Coordenadas aproximadas de las localidades indicadas
                     * en las direcciones de las sucursales de la BD.
                     *
                     * El ID corresponde al sucursal_id.
                     */
                    const coordenadasSucursales = {

                        1: {
                            nombre: "Zona 10",
                            lat: 14.5995,
                            lng: -90.5133
                        },

                        2: {
                            nombre: "zona 18",
                            lat: 14.8680,
                            lng: -90.5180
                        },

                        3: {
                            nombre: "zona 21",
                            lat: 14.5380,
                            lng: -90.5500
                        },

                        4: {
                            nombre: "Zona 1",
                            lat: 14.6349,
                            lng: -90.5069
                        },

                        6: {
                            nombre: "Zona 24",
                            lat: 14.6800,
                            lng: -90.4700
                        },

                        7: {
                            nombre: "zona 2",
                            lat: 14.6450,
                            lng: -90.5100
                        },

                        8: {
                            nombre: "zona 6",
                            lat: 14.6500,
                            lng: -90.5000
                        },

                        9: {
                            nombre: "zona 7",
                            lat: 14.6500,
                            lng: -90.5500
                        },

                        12: {
                            nombre: "Zona 13",
                            lat: 14.5830,
                            lng: -90.5230
                        }

                    };

                    /*
                     * Crear mapa.
                     */
                    const mapa = L.map('mapaRutas').setView(
                            [14.6349, -90.5069],
                            7
                            );
                    /*
                     * Mapa base de OpenStreetMap.
                     */
                    L.tileLayer(
                            'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                            {
                                maxZoom: 19,
                                attribution: '&copy; OpenStreetMap contributors'
                            }
                    ).addTo(mapa);
                    /*
                     * Datos enviados por el backend.
                     */
                    const rutas = [

                    <c:forEach var="ruta"
                               items="${datos}"
                               varStatus="status">

                    {
                    id: ${ruta.id},
                            origenId: ${ruta.origen.id},
                            destinoId: ${ruta.destino.id},
                            origenNombre: "${ruta.origen.nombre}",
                    destinoNombre: "${ruta.destino.nombre}",
                            distancia: ${ruta.distanciaKilometraje},
                            precio: ${ruta.precioBoleto}
                    }

                        <c:if test="${!status.last}">
                    ,
                        </c:if>

                    </c:forEach>

                    ];
                    /*
                     * Dibujar cada ruta.
                     */
                    const limites = [];
                    rutas.forEach(function (ruta) {

                        const origen =
                                coordenadasSucursales[ruta.origenId];
                        const destino =
                                coordenadasSucursales[ruta.destinoId];
                        if (!origen || !destino) {
                            return;
                        }


                        /*
                         * Marcador del origen.
                         */
                        L.marker([
                            origen.lat,
                            origen.lng
                        ])
                                .addTo(mapa)
                                .bindPopup(
                                        "<strong>" +
                                        ruta.origenNombre +
                                        "</strong><br>" +
                                        "Origen de la ruta"
                                        );
                        /*
                         * Marcador del destino.
                         */
                        L.marker([
                            destino.lat,
                            destino.lng
                        ])
                                .addTo(mapa)
                                .bindPopup(
                                        "<strong>" +
                                        ruta.destinoNombre +
                                        "</strong><br>" +
                                        "Destino"
                                        );
                        /*
                         * Línea que representa la ruta.
                         */
                        const linea = L.polyline(
                                [
                                    [origen.lat, origen.lng],
                                    [destino.lat, destino.lng]
                                ]
                                ).addTo(mapa);
                        /*
                         * Información al hacer clic
                         * sobre la ruta.
                         */
                        linea.bindPopup(
                                "<strong>" +
                                ruta.origenNombre +
                                " → " +
                                ruta.destinoNombre +
                                "</strong><br><br>" +
                                "Distancia: " +
                                ruta.distancia +
                                " km<br>" +
                                "Precio boleto: Q " +
                                ruta.precio
                                );
                        limites.push([
                            origen.lat,
                            origen.lng
                        ]);
                        limites.push([
                            destino.lat,
                            destino.lng
                        ]);
                    });
                    /*
                     * Ajustar el mapa para mostrar
                     * todas las rutas encontradas.
                     */
                    if (limites.length > 0) {

                        mapa.fitBounds(limites, {
                            padding: [40, 40]
                        });
                    }

                </script>

            </c:if>

        </div>
    </body>
</html>
