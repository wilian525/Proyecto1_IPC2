/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
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
             LocalDate fechaInicio = servletUtil.fechaOpcional(request, "fechaInico");
             LocalDate fechaFin = servletUtil.fechaOpcional(request, "fechaFin");
             
             if (fechaInicio != null && fechaFin != null  && fechaFin.isBefore(fechaInicio)) {
                   request.setAttribute("error", "el Intervalo de fechas no es valida");
            }
             Integer sucursalId = servletUtil.enteroOpcional(request, "sucursalId");
             ConexionDB conexiondb = new ConexionDB();
             
             try {
                   SucursalDAO sucursalDao = new SucursalDAO(conexiondb);
                   request.setAttribute("tipo", tipo);
                    request.setAttribute("fechaInicio",fechaInicio );
                  request.setAttribute( "fechaFin",fechaFin);
                request.setAttribute( "sucursalId", sucursalId);
                request.setAttribute("sucursales",  sucursalDao.listar() );
                
                 if ("ganancia".equals(tipo)) {
                       ReporteGanancias reporte = new ReporteGanancias(conexiondb);
                        request.setAttribute("dato", reporte.listarSucursal(sucursalId));
                        request.setAttribute("reporte", reporte);
                        
                 } else if ("rutas".equals(tipo)) {
                        ReporteRutas reporte = new ReporteRutas(conexiondb);
                        request.setAttribute("dato", reporte.rutasMasDemandadas(fechaInicio, fechaFin));
                        request.setAttribute("reporte", reporte);
                        
                 } else if ("costo".equals(tipo)) {
                        ReporteGanancias ganancias =new ReporteGanancias( conexiondb );
                       request.setAttribute("datos", ganancias.listarSucursal(sucursalId ));
                        request.setAttribute( "reporte", new ReporteCosto( conexiondb ));
                        
                 } else if ("mapa".equals(tipo) && sucursalId != null) {
                      ReporteRutas reporte = new ReporteRutas(conexiondb);
                      request.setAttribute("datos", reporte.rutasPorSucursal(sucursalId));
                 }
                 request.getRequestDispatcher("/WEB-INF/vistas/reportes/sistema.jsp").forward(request, response);
                 
            } finally{
                 conexiondb.cerrar();
            }
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/reportes/sistema?error=true");
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



}
