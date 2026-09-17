/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteBuses;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteChoferes;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteDepreciacion;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteIngresoBoletos;
import com.proyecto1.serviciobuses.backend.Reportes.ReporteIngresosAlquileres;
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
              response.sendError( HttpServletResponse.SC_FORBIDDEN);
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
                    ReporteIngresoBoletos reporte =new ReporteIngresoBoletos(conexiondb );
                    request.setAttribute("datos", reporte.listarViajes(sucursalId, fechaInicio,fechaFin,servletUtil.enteroOpcional(request, "rutaId" ),
                                    servletUtil.enteroOpcional(request,"busId" )) );
                    request.setAttribute("reporte",reporte );
                    
                 } else if ("alquileres".equals(tipo)) {
                       ReporteIngresosAlquileres reporte = new ReporteIngresosAlquileres(conexiondb);
                       request.setAttribute("datos", reporte.listarAlquileres(sucursalId, fechaInicio, fechaFin));
                       request.setAttribute("reporte", reporte);
                       
                 } else if ("depreciacion".equals(tipo)) {
                       ReporteDepreciacion reporte = new ReporteDepreciacion( conexiondb);
                    request.setAttribute("datos", reporte.listarBuses( sucursalId ) );
                    request.setAttribute("reporte",reporte);
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

  

}
