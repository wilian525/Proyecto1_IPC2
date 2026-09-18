/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Reportes.ExportadorReporteHTML;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteCosto;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteGanancias;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteRutas;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
@WebServlet(name = "ReporteSistemaServlet", urlPatterns = {"/ReporteSistemaServlet"})
public class ReporteSistemaServlet extends HttpServlet {

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

        if (!servletUtil.esAdministradorSistema(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String tipo = servletUtil.accion(request, "ganancia");
            LocalDate fechaInicio = servletUtil.fechaOpcional(request, "fechaInicio");
            LocalDate fechaFin = servletUtil.fechaOpcional(request, "fechaFin");

            if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
                request.setAttribute("error", "el Intervalo de fechas no es valida");
            }
            Integer sucursalId = servletUtil.enteroOpcional(request, "sucursalId");
            ConexionDB conexiondb = new ConexionDB();

            String exportar = request.getParameter("exportar");

            if ("true".equals(exportar)) {
                exportarReporte(request, response, conexiondb, tipo, sucursalId, fechaInicio, fechaFin);
                return;
            }

            try {
                SucursalDAO sucursalDao = new SucursalDAO(conexiondb);
                request.setAttribute("tipo", tipo);
                request.setAttribute("fechaInicio", fechaInicio);
                request.setAttribute("fechaFin", fechaFin);
                request.setAttribute("sucursalId", sucursalId);
                request.setAttribute("sucursales", sucursalDao.listar());

                if ("ganancia".equals(tipo)) {
                    ReporteGanancias reporte = new ReporteGanancias(conexiondb);
                    request.setAttribute("dato", reporte.listarSucursal(sucursalId));
                    request.setAttribute("reporte", reporte);

                } else if ("rutas".equals(tipo)) {
                    ReporteRutas reporte = new ReporteRutas(conexiondb);
                    request.setAttribute("dato", reporte.rutasMasDemandadas(fechaInicio, fechaFin));
                    request.setAttribute("reporte", reporte);

                } else if ("costo".equals(tipo)) {
                    ReporteGanancias ganancias = new ReporteGanancias(conexiondb);
                    request.setAttribute("datos", ganancias.listarSucursal(sucursalId));
                    request.setAttribute("reporte", new ReporteCosto(conexiondb));

                } else if ("mapa".equals(tipo) && sucursalId != null) {
                    ReporteRutas reporte = new ReporteRutas(conexiondb);
                    request.setAttribute("datos", reporte.rutasPorSucursal(sucursalId));
                }
                request.getRequestDispatcher("/WEB-INF/vistas/reportes/sistema.jsp").forward(request, response);

            } finally {
                conexiondb.cerrar();
            }
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/ReporteSistemaServlet?error=true");
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

    private void exportarReporte(HttpServletRequest request,
            HttpServletResponse response,
            ConexionDB conexiondb,
            String tipo,
            Integer sucursalId,
            LocalDate fechaInicio,
            LocalDate fechaFin) throws IOException {

        ExportadorReporteHTML exportador = new ExportadorReporteHTML();

        Collection<String[]> filas = new ArrayList<>();

        String titulo;

        if ("ganancia".equals(tipo)) {

            ReporteGanancias reporte = new ReporteGanancias(conexiondb);

            SucursalDAO sucursalDao = new SucursalDAO(conexiondb);

            Collection<Sucursal> sucursales = reporte.listarSucursal(sucursalId);

            titulo = "Reporte de ganancias";

            String[] encabezados = {
                "Sucursal",
                "Ingresos",
                "Costos",
                "Ganancia neta"
            };

            for (Sucursal sucursal : sucursales) {

                filas.add(new String[]{
                    sucursal.getNombre(),
                    "Q " + reporte.totalIngreso(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.totalCostos(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.gananciaNeta(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    )
                });
            }

            enviarHTML(response,
                    exportador.genera(titulo, encabezados, filas),
                    "reporte_ganancias.html");

        } else if ("rutas".equals(tipo)) {

            ReporteRutas reporte = new ReporteRutas(conexiondb);

            Collection<Ruta> rutas
                    = reporte.rutasMasDemandadas(fechaInicio, fechaFin);

            titulo = "Rutas más demandadas";

            String[] encabezados = {
                "#",
                "Origen",
                "Destino",
                "Distancia",
                "Precio",
                "Boletos vendidos"
            };

            int contador = 1;

            for (Ruta ruta : rutas) {

                filas.add(new String[]{
                    String.valueOf(contador++),
                    ruta.getOrigen() != null
                    ? ruta.getOrigen().getNombre()
                    : "",
                    ruta.getDestino() != null
                    ? ruta.getDestino().getNombre()
                    : "",
                    ruta.getDistanciaKilometraje() + " km",
                    "Q " + ruta.getPrecioBoleto(),
                    String.valueOf(
                    reporte.totalBoletoVendidos(
                    ruta.getId(),
                    fechaInicio,
                    fechaFin
                    )
                    )
                });
            }

            enviarHTML(response,
                    exportador.genera(titulo, encabezados, filas),
                    "reporte_rutas_demandadas.html");

        } else if ("costo".equals(tipo)) {

            ReporteGanancias ganancias
                    = new ReporteGanancias(conexiondb);

            ReporteCosto reporte
                    = new ReporteCosto(conexiondb);

            Collection<Sucursal> sucursales
                    = ganancias.listarSucursal(sucursalId);

            titulo = "Costos operativos";

            String[] encabezados = {
                "Sucursal",
                "Combustible",
                "Mano de obra",
                "Repuestos",
                "Depreciación",
                "Total"
            };

            for (Sucursal sucursal : sucursales) {

                filas.add(new String[]{
                    sucursal.getNombre(),
                    "Q " + reporte.totalCombustible(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.totalManoObra(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.totalRepuesto(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.totalDepreciacion(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    ),
                    "Q " + reporte.granTotal(
                    sucursal.getId(),
                    fechaInicio,
                    fechaFin
                    )
                });
            }

            enviarHTML(response,
                    exportador.genera(titulo, encabezados, filas),
                    "reporte_costos_operativos.html");

        } else if ("mapa".equals(tipo)) {

            if (sucursalId == null) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Debe seleccionar una sucursal."
                );
                return;
            }

            ReporteRutas reporte
                    = new ReporteRutas(conexiondb);

            Collection<Ruta> rutas
                    = reporte.rutasPorSucursal(sucursalId);

            titulo = "Rutas por sucursal";

            String[] encabezados = {
                "Origen",
                "Destino",
                "Distancia",
                "Precio"
            };

            for (Ruta ruta : rutas) {

                filas.add(new String[]{
                    ruta.getOrigen() != null
                    ? ruta.getOrigen().getNombre()
                    : "",
                    ruta.getDestino() != null
                    ? ruta.getDestino().getNombre()
                    : "",
                    ruta.getDistanciaKilometraje() + " km",
                    "Q " + ruta.getPrecioBoleto()
                });
            }

            enviarHTML(response,
                    exportador.genera(titulo, encabezados, filas),
                    "reporte_mapa_rutas.html");
        }
    }

    private void enviarHTML(HttpServletResponse response,
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
