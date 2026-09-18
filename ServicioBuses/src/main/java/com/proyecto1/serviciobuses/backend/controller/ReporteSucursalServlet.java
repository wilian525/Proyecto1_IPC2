/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteBuses;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteChoferes;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteDepreciacion;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteIngresoBoletos;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteIngresosAlquileres;
import java.io.IOException;
import java.io.PrintWriter;
import com.proyecto1.serviciobuses.backend.Reportes.ExportadorReporteHTML;
import java.util.ArrayList;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 *
 * @author wilian
 */
@WebServlet(name = "ReporteSucursalServlet", urlPatterns = {"/ReporteSucursalServlet"})
public class ReporteSucursalServlet extends HttpServlet {

    private ServletUtil servletUtil = new ServletUtil();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = servletUtil.obtenerUsuario(request);
        if (!servletUtil.esAdministradorSucursal(request) || usuario == null || usuario.getSucursalId() == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String tipo = servletUtil.accion(request, "buses");
            LocalDate fechaInicio = servletUtil.fechaOpcional(request, "fechaInicio");
            LocalDate fechaFin = servletUtil.fechaOpcional(request, "fechaFin");

            if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
                request.setAttribute("error", "EL intervalo de fechas no es valida");
            }
            int sucursalId = usuario.getSucursalId();
            ConexionDB conexiondb = new ConexionDB();

            try {
                if ("true".equals(request.getParameter("exportar"))) {

                    exportarReporte(
                            request,
                            response,
                            conexiondb,
                            tipo,
                            sucursalId,
                            fechaInicio,
                            fechaFin
                    );

                    return;
                }
                request.setAttribute("tipo", tipo);
                request.setAttribute("fechaInicio", fechaInicio);
                request.setAttribute("fechaFin", fechaFin);

                if ("buses".equals(tipo)) {
                    Boolean estado = null;
                    String valorEstado = servletUtil.texto(request, "estado");

                    if (!valorEstado.isBlank()) {
                        estado = Boolean.valueOf(valorEstado);
                    }
                    ReporteBuses reporte = new ReporteBuses(conexiondb);
                    request.setAttribute("datos", reporte.listarBuses(sucursalId, estado));
                    request.setAttribute("reporte", reporte);

                } else if ("choferes".equals(tipo)) {
                    ReporteChoferes reporte = new ReporteChoferes(conexiondb);
                    request.setAttribute("datos", reporte.listarChoferes(sucursalId));
                    request.setAttribute("reporte", reporte);

                } else if ("boletos".equals(tipo)) {
                    ReporteIngresoBoletos reporte = new ReporteIngresoBoletos(conexiondb);
                    request.setAttribute("datos", reporte.listarViajes(sucursalId, fechaInicio, fechaFin, servletUtil.enteroOpcional(request, "rutaId"),
                            servletUtil.enteroOpcional(request, "busId")));
                    request.setAttribute("reporte", reporte);

                } else if ("alquileres".equals(tipo)) {
                    ReporteIngresosAlquileres reporte = new ReporteIngresosAlquileres(conexiondb);
                    request.setAttribute("datos", reporte.listarAlquileres(sucursalId, fechaInicio, fechaFin));
                    request.setAttribute("reporte", reporte);

                } else if ("depreciacion".equals(tipo)) {
                    ReporteDepreciacion reporte = new ReporteDepreciacion(conexiondb);
                    request.setAttribute("datos", reporte.listarBuses(sucursalId));
                    request.setAttribute("reporte", reporte);
                }
                request.getRequestDispatcher("/WEB-INF/vistas/reportes/sucursal.jsp").forward(request, response);

            } finally {
                conexiondb.cerrar();
            }
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/reportes/sucursal?error=true");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    private void exportarReporte(
            HttpServletRequest request,
            HttpServletResponse response,
            ConexionDB conexiondb,
            String tipo,
            int sucursalId,
            LocalDate fechaInicio,
            LocalDate fechaFin) throws IOException {

        ExportadorReporteHTML exportador = new ExportadorReporteHTML();

        Collection<String[]> filas = new ArrayList<>();

        String titulo;
        String nombreArchivo;

        if ("buses".equals(tipo)) {

            Boolean estado = null;

            String valorEstado = servletUtil.texto(request, "estado");

            if (!valorEstado.isBlank()) {
                estado = Boolean.valueOf(valorEstado);
            }

            ReporteBuses reporte = new ReporteBuses(conexiondb);

            Collection<com.proyecto1.serviciobuses.backend.Model.Bus> buses
                    = reporte.listarBuses(sucursalId, estado);

            titulo = "Reporte de buses de la sucursal";

            nombreArchivo = "reporte_buses.html";

            String[] encabezados = {
                "ID",
                "Placa",
                "Marca",
                "Modelo",
                "Año",
                "Capacidad",
                "Kilometraje",
                "Estado",
                "Chofer actual",
                "Viajes realizados"
            };

            for (com.proyecto1.serviciobuses.backend.Model.Bus bus : buses) {

                String choferActual = "";

                com.proyecto1.serviciobuses.backend.Model.Chofer chofer
                        = reporte.obtenerChoferActual(bus.getId());

                if (chofer != null) {
                    choferActual = chofer.getNombre();
                } else {
                    choferActual = "Sin chofer asignado";
                }

                filas.add(new String[]{
                    String.valueOf(bus.getId()),
                    bus.getPlaca(),
                    bus.getMarca(),
                    bus.getModelo(),
                    String.valueOf(bus.getAñoFabricacion()),
                    String.valueOf(bus.getCapacidad()),
                    String.valueOf(bus.getKilometrajeActual()),
                    bus.isEstado() ? "Activo" : "Inactivo",
                    choferActual,
                    String.valueOf(reporte.totalViajesRealizados(bus.getId()))
                });
            }

        } else if ("choferes".equals(tipo)) {

            ReporteChoferes reporte
                    = new ReporteChoferes(conexiondb);

            Collection<com.proyecto1.serviciobuses.backend.Model.Chofer> choferes
                    = reporte.listarChoferes(sucursalId);

            titulo = "Reporte de choferes de la sucursal";

            nombreArchivo = "reporte_choferes.html";

            String[] encabezados = {
                "ID",
                "Nombre",
                "NIT",
                "DPI",
                "Teléfono",
                "Licencia",
                "Tipo",
                "Vencimiento",
                "Salario por viaje",
                "Viajes realizados"
            };

            for (Chofer chofer : choferes) {

                filas.add(new String[]{
                    String.valueOf(chofer.getId()),
                    chofer.getNombre(),
                    chofer.getNit(),
                    chofer.getDpi(),
                    chofer.getTelefono(),
                    chofer.getNumeroLicencia(),
                    String.valueOf(chofer.getTipoLicencia()),
                    String.valueOf(chofer.getFechaVencimiento()),
                    "Q " + chofer.getSalarioBasePorViaje(),
                    String.valueOf(
                    reporte.totalViajesRealizado(chofer.getId())
                    )
                });
            }

        } else if ("boletos".equals(tipo)) {

            Integer rutaId
                    = servletUtil.enteroOpcional(request, "rutaId");

            Integer busId
                    = servletUtil.enteroOpcional(request, "busId");

            ReporteIngresoBoletos reporte
                    = new ReporteIngresoBoletos(conexiondb);

            Collection<com.proyecto1.serviciobuses.backend.Model.Viaje> viajes
                    = reporte.listarViajes(
                            sucursalId,
                            fechaInicio,
                            fechaFin,
                            rutaId,
                            busId
                    );

            titulo = "Reporte de ingresos por boletos";

            nombreArchivo = "reporte_ingresos_boletos.html";

            String[] encabezados = {
                "Viaje",
                "Fecha",
                "Bus",
                "Ruta",
                "Boletos vendidos",
                "Ingreso total"
            };

            for (com.proyecto1.serviciobuses.backend.Model.Viaje viaje : viajes) {

                com.proyecto1.serviciobuses.backend.Model.Ruta ruta
                        = reporte.obtenerRuta(viaje.getId());

                String nombreRuta = "Sin ruta";

                if (ruta != null) {

                    nombreRuta
                            = ruta.getOrigen().getNombre()
                            + " → "
                            + ruta.getDestino().getNombre();
                }

                filas.add(new String[]{
                    String.valueOf(viaje.getId()),
                    String.valueOf(viaje.getFechaSalida()),
                    viaje.getBus() != null
                    ? viaje.getBus().getPlaca()
                    : "",
                    nombreRuta,
                    String.valueOf(
                    reporte.cantidadBoletos(
                    viaje.getId(),
                    fechaInicio,
                    fechaFin
                    )
                    ),
                    "Q " + reporte.ingresoTotal(
                    viaje.getId(),
                    fechaInicio,
                    fechaFin
                    )
                });
            }

        } else if ("alquileres".equals(tipo)) {

            ReporteIngresosAlquileres reporte
                    = new ReporteIngresosAlquileres(conexiondb);

            Collection<com.proyecto1.serviciobuses.backend.Model.ViajePrivado> alquileres
                    = reporte.listarAlquileres(
                            sucursalId,
                            fechaInicio,
                            fechaFin
                    );

            titulo = "Reporte de ingresos por alquileres";

            nombreArchivo = "reporte_ingresos_alquileres.html";

            String[] encabezados = {
                "Viaje",
                "Cliente",
                "Origen",
                "Destino",
                "Fecha salida",
                "Pasajeros",
                "Bus",
                "Precio confirmado",
                "Fecha pago"
            };

            for (com.proyecto1.serviciobuses.backend.Model.ViajePrivado alquiler
                    : alquileres) {

                com.proyecto1.serviciobuses.backend.Model.Usuario cliente
                        = reporte.obtenerCliente(alquiler);

                String nombreCliente = "Cliente no encontrado";

                if (cliente != null) {
                    nombreCliente = cliente.getNombre();
                }

                filas.add(new String[]{
                    String.valueOf(alquiler.getId()),
                    nombreCliente,
                    alquiler.getOrigen(),
                    alquiler.getDestino(),
                    String.valueOf(alquiler.getFechaSalida()),
                    String.valueOf(alquiler.getNumeroPasajeros()),
                    alquiler.getBus() != null
                    ? alquiler.getBus().getPlaca()
                    : "",
                    "Q " + alquiler.getPrecioConfirmado(),
                    String.valueOf(alquiler.getFechaPago())
                });
            }

        } else if ("depreciacion".equals(tipo)) {

            ReporteDepreciacion reporte
                    = new ReporteDepreciacion(conexiondb);

            Collection<com.proyecto1.serviciobuses.backend.Model.Bus> buses
                    = reporte.listarBuses(sucursalId);

            titulo = "Reporte de depreciación por bus";

            nombreArchivo = "reporte_depreciacion.html";

            String[] encabezados = {
                "ID",
                "Placa",
                "Marca",
                "Modelo",
                "Kilometraje recorrido",
                "Depreciación acumulada"
            };

            for (com.proyecto1.serviciobuses.backend.Model.Bus bus : buses) {

                filas.add(new String[]{
                    String.valueOf(bus.getId()),
                    bus.getPlaca(),
                    bus.getMarca(),
                    bus.getModelo(),
                    String.valueOf(
                    reporte.totalKilometraje(bus.getId())
                    ),
                    "Q " + reporte.depreciacionAcumulada(bus.getId())
                });
            }

        } else {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Tipo de reporte no válido."
            );

            return;
        }

        String html
                = exportador.genera(
                        titulo,
                        obtenerEncabezados(tipo),
                        filas
                );

        enviarHTML(
                response,
                html,
                nombreArchivo
        );
    }

    private String[] obtenerEncabezados(String tipo) {

        if ("buses".equals(tipo)) {

            return new String[]{
                "ID",
                "Placa",
                "Marca",
                "Modelo",
                "Año",
                "Capacidad",
                "Kilometraje",
                "Estado",
                "Chofer actual",
                "Viajes realizados"
            };

        } else if ("choferes".equals(tipo)) {

            return new String[]{
                "ID",
                "Nombre",
                "NIT",
                "DPI",
                "Teléfono",
                "Licencia",
                "Tipo",
                "Vencimiento",
                "Salario por viaje",
                "Viajes realizados"
            };

        } else if ("boletos".equals(tipo)) {

            return new String[]{
                "Viaje",
                "Fecha",
                "Bus",
                "Ruta",
                "Boletos vendidos",
                "Ingreso total"
            };

        } else if ("alquileres".equals(tipo)) {

            return new String[]{
                "Viaje",
                "Cliente",
                "Origen",
                "Destino",
                "Fecha salida",
                "Pasajeros",
                "Bus",
                "Precio confirmado",
                "Fecha pago"
            };

        } else {

            return new String[]{
                "ID",
                "Placa",
                "Marca",
                "Modelo",
                "Kilometraje recorrido",
                "Depreciación acumulada"
            };
        }
    }

    private void enviarHTML(
            HttpServletResponse response,
            String html,
            String nombreArchivo) throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + nombreArchivo + "\""
        );

        try (PrintWriter out = response.getWriter()) {
            out.print(html);
        }
    }

}
