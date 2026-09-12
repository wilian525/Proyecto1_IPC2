/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.Servicio.CompraServicio;
import com.proyecto1.serviciobuses.backend.dao.BoletoDAO;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.RutaDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeRegularDAO;
import java.io.IOException;
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
@WebServlet(name = "BoletoServlet", urlPatterns = {"/BoletoServlet"})
public class BoletoServlet extends HttpServlet {

    private ServletUtil servletUtil;

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
        if (usuario == null) {
              response.sendRedirect(request.getContextPath() + "/auth");
              return;
        }
        ConexionDB conexiondb = new ConexionDB();
        
        try {
              request.setAttribute("viajes", viajesDisponible(conexiondb));
              BoletoDAO boletoDao = new BoletoDAO(conexiondb);
              request.setAttribute("misBoletos", boletoDao.buscarPorUsuario(usuario.getId()));
              Integer viajeId = servletUtil.enteroOpcional(request, "viajeId");
                if (viajeId != null) {
                     request.setAttribute("asientosOcupados", boletoDao.buscarPorViaje(viajeId));
                     request.setAttribute("viajeId", viajeId);
            }
                request.getRequestDispatcher("/WEB-INF/vistas/boletos.jsp").forward(request, response);
        } finally{
            conexiondb.cerrar();
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
        
        Usuario usuario = servletUtil.obtenerUsuario(request);
        if (usuario == null) {
               response.sendRedirect(request.getContextPath() + "/auth");
               return;
        }
         try {
               int viajeId = servletUtil.entero(request, "viajeId");
               String[] seleccionado = request.getParameterValues("asientos");
               Collection<Integer> asientos = new ArrayList<>();
               if (seleccionado != null) {
                     
                   for(String asiento: seleccionado){
                        asientos.add(Integer.valueOf(asiento));
                   }
             }
               ConexionDB conexiondb = new ConexionDB();
               
               try {
                   boolean resultado = new CompraServicio(conexiondb).compraBoletos(usuario.getId(), viajeId, asientos, servletUtil.fecha(request, "fechaPago"));
                   response.sendRedirect(request.getContextPath() +"/boletos?viajeId=" + viajeId + "&resultado=" + resultado);
             } finally {
                   conexiondb.cerrar();
             }
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/boleto?resultado=false");
        }
    }

 private Collection<ViajeRegular> viajesDisponible(ConexionDB conexiondb){
            Collection<ViajeRegular> resultado = new ArrayList<>();
            ViajeDAO viajeDao = new ViajeDAO(conexiondb);
            ViajeRegularDAO regularDao = new ViajeRegularDAO(conexiondb);
            RutaDAO rutaDao = new RutaDAO(conexiondb);
            BusDAO busDao = new BusDAO(conexiondb);
            
            for(Viaje viaje : viajeDao.listar()){
                        if (viaje.getHoraSalidaReal() != null || viaje.getBus() == null) {
                                continue;
                }
                        ViajeRegular relacion = regularDao.buscarPorViaje(viaje.getId()).orElse(null);
                        if (relacion == null || relacion.getRuta() == null) {
                                continue;
                }
                        Ruta ruta = rutaDao.buscarPorId(relacion.getRuta().getId()).orElse(null);
                        Bus bus = busDao.buscarPorId(viaje.getBus().getId()).orElse(null);
                        if (ruta == null || bus == null || !bus.isEstado()) {
                                continue;
                }
                        
            ViajeRegular regular = new ViajeRegular();

            regular.setId(viaje.getId());
            regular.setFechaSalida(viaje.getFechaSalida());
            regular.setHoraSalidaProgramada(viaje.getHoraSalidaProgramada() );
            regular.setFechaLlegadaEstimada( viaje.getFechaLlegadaEstimada() );
            regular.setHoraLlegadaEstimada( viaje.getHoraLlegadaEstimada());
            regular.setBus(bus);
            regular.setChofer( viaje.getChofer());
            regular.setRuta(ruta);
            resultado.add(regular);
       
            }
            return resultado;
 }
}
