/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.Servicio.ViajeServicio;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.RutaDAO;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
@WebServlet(name = "ViajesServlet", urlPatterns = {"/ViajesServlet"})
public class ViajesServlet extends HttpServlet {

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
         
         if (!servletUtil.esAdministradorSucursal(request ) || usuario == null || usuario.getSucursalId() == null) {
              response.sendError(HttpServletResponse.SC_FORBIDDEN);
              return;
        }
         String accion = servletUtil.accion(request, "viaje");
         ConexionDB conexiondb = new ConexionDB();
         
         try {
                request.setAttribute("seccion", accion);
                request.setAttribute("buses", busesSucursal(conexiondb,usuario.getSucursalId()));
                request.setAttribute("choferes", choferesSucursal(conexiondb,usuario.getSucursalId()));
                request.setAttribute("rutas", rutasSucursal(conexiondb,usuario.getSucursalId()));
                request.setAttribute("sucursales", new SucursalDAO(conexiondb).listar());
                
                if ("viaje".equals(accion)) {
                     request.setAttribute("viajes", viajesSucursal(conexiondb,usuario.getSucursalId()));
             }
                request.getRequestDispatcher("/WEB-INF/visitas/sucursal/viajes.jsp").forward(request, response);
        } finally {
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
        
        request.setCharacterEncoding("UFT-8");
        
        Usuario usuario = servletUtil.obtenerUsuario(request);
        
        if (!servletUtil.esAdministradorSucursal(request) || usuario == null || usuario.getSucursalId() == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
        }
        String accion = servletUtil.accion(request, "");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
             boolean resultado = false;
             if ("crearRuta".equals(accion)) {
                 resultado = crearRuta(request,usuario,conexiondb);
                 
            } else if ("actualizarRuta".equals(accion)) {
                  resultado = actualizarRuta(request,usuario,conexiondb);
                  
            } else if ("eliminarRuta".equals(accion)) {
                   resultado = eliminarRuta(request,usuario,conexiondb);
                   
            } else if ("programar".equals(accion)) {
                 resultado = programarViaje(request,usuario,conexiondb);
                 
            } else if ("salida".equals(accion)) {
                   int viajeId = servletUtil.entero(request, "viajeId");
                   if (viajePerteneceSucursal(conexiondb,viajeId,usuario.getSucursalId())) {
                         resultado = new ViajeServicio(conexiondb).registrarSalida(viajeId, servletUtil.decimal(request, "kilometraje"), servletUtil.hora(request, "horaReal"));
                 }
            } else if ("llegada".equals(accion)) {
                    int viajeId = servletUtil.entero(request,"viajeId");
                    if (viajePerteneceSucursal(conexiondb,viajeId,usuario.getSucursalId())) {
                          resultado = new ViajeServicio(conexiondb).registrarLlegada(viajeId, servletUtil.decimal(request, "kilometraje"), servletUtil.decimal(request, "combustible"), servletUtil.hora(request, "horaReal"));
                 }
            } else if ("eliminarViaje".equals(accion)) {
                   int viajeId = servletUtil.entero(request, "viajeId");
                    if (viajePerteneceSucursal(conexiondb,viajeId,usuario.getSucursalId())) {
                        resultado = new ViajeServicio(conexiondb).eliminarViaje(viajeId);
                 }
            }
             response.sendRedirect(request.getContextPath() + "/viajes?resultado=" + resultado);
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/viajes?resultado=false");
        } finally {
             conexiondb.cerrar();
        }
    }

    
    private boolean crearRuta(HttpServletRequest request, Usuario usuario, ConexionDB conexiondb) {
         SucursalDAO sucursalDao = new SucursalDAO(conexiondb);
         Sucursal origen = sucursalDao.buscarPorId(usuario.getSucursalId());
         Sucursal destino = sucursalDao.buscarPorId(servletUtil.entero(request, "destinoId"));
         
         if (origen == null || destino == null || origen.getId() == destino.getId()) {
                return false;
        }
         Ruta ruta = new Ruta();
          ruta.setOrigen(origen);
          ruta.setDestino(destino);
          ruta.setDistanciaKilometraje(servletUtil.decimal(request, "distancia"));
          ruta.setPrecioBoleto(servletUtil.decimal(request, "precio"));
          
          return new RutaDAO(conexiondb).insertar(ruta);
    }
    
    private boolean actualizarRuta(HttpServletRequest request, Usuario usuario, ConexionDB conexiondb) {
         RutaDAO rutaDao = new RutaDAO(conexiondb);
         Ruta ruta = rutaDao.buscarPorId(servletUtil.entero(request, "rutaId")).orElse(null);
         
         if (ruta == null || ruta.getOrigen() == null || ruta.getOrigen().getId() != usuario.getSucursalId()) {
                    return false;
        }
         Sucursal destino = new SucursalDAO(conexiondb).buscarPorId(servletUtil.entero(request, "destinoId"));
         if (destino == null || destino.getId() == usuario.getSucursalId()) {
               return false;
        }
         ruta.setDestino(destino);
         ruta.setDistanciaKilometraje(servletUtil.decimal(request, "distancia"));
         ruta.setPrecioBoleto(servletUtil.decimal(request, "precio"));
         
         return rutaDao.actualizar(ruta);
    }
    
    private boolean eliminarRuta( HttpServletRequest request, Usuario usuario, ConexionDB conexiondb) {
         RutaDAO rutaDao = new RutaDAO(conexiondb);
         Ruta ruta = rutaDao.buscarPorId(servletUtil.entero(request, "rutaId")).orElse(null);
         if (ruta == null || ruta.getOrigen() == null || ruta.getOrigen().getId() != usuario.getSucursalId()) {
                return false;
        }
         return rutaDao.eliminar(ruta.getId());
    }
    
    
    private boolean programarViaje(HttpServletRequest request,Usuario usuario,ConexionDB conexiondb) {
         int busId = servletUtil.entero(request, "busId");
         int choferId = servletUtil.entero(request, "choferId");
         int rutaId = servletUtil.entero(request, "rutaId");
         
         Bus bus = new BusDAO(conexiondb).buscarPorId(busId).orElse(null);
         Chofer chofer = new ChoferDAO(conexiondb).buscarPorId(busId).orElse(null);
         Ruta ruta = new RutaDAO(conexiondb).buscarPorId(busId).orElse(null);
         
         if (bus == null || chofer == null || ruta == null || ruta.getOrigen() == null || bus.getSucursalId() != usuario.getSucursalId()  || chofer.getSucursalId() == null || chofer.getSucursalId() != usuario.getSucursalId()) {
               return false;
        }
         ViajeRegular viaje = new ViajeRegular();
         viaje.setBus(bus);
         viaje.setChofer(chofer);
         viaje.setRuta(ruta);
         viaje.setFechaSalida(servletUtil.fecha(request, "fechaSalida"));
         viaje.setHoraSalidaProgramada(servletUtil.hora(request, "horaSalida"));
         viaje.setFechaLlegadaEstimada(servletUtil.fecha(request, "fechaLlegada"));
         viaje.setHoraLlegadaEstimada(servletUtil.hora(request, "horaLlegada"));
         
         return new ViajeServicio(conexiondb).programaViajeRegular(viaje);
    }
    
    private boolean viajePerteneceSucursal(ConexionDB conexiondb,int viajeId,int sucursalId){
            Viaje viaje = new ViajeDAO(conexiondb).buscarPorId(viajeId).orElse(null);
            if (viaje == null || viaje.getBus() == null) {
                return false;
        }
            Bus bus = new BusDAO(conexiondb).buscarPorId(viajeId).orElse(null);
            return bus != null && bus.getSucursalId() == sucursalId;
    }
    
    private Collection<Viaje> viajesSucursal(ConexionDB conexiondb,int sucursalId){
          Collection<Viaje> resultado = new ArrayList<>();
          
          for(Viaje viaje : new ViajeDAO(conexiondb).listar()){
                 if (viaje.getBus() == null) {
                      continue;
              }
                 Bus bus = new BusDAO(conexiondb).buscarPorId(viaje.getBus().getId()).orElse(null);
                 if (bus != null && bus.getSucursalId() == sucursalId) {
                     viaje.setBus(bus);
                     resultado.add(viaje);
              }
          }
          return resultado;
    }
    
    private Collection<Bus> busesSucursal(ConexionDB conexionDb,int sucursalId){
          Collection<Bus> resultado = new ArrayList<>();
          for(Bus bus : new BusDAO(conexionDb).listar()){
                    if (bus.getSucursalId() == sucursalId) {
                            resultado.add(bus);
              }
          }
          return resultado;
    }
    
    private Collection<Chofer> choferesSucursal(ConexionDB conexiondb,int sucursalId){
            Collection<Chofer> resultado = new ArrayList<>();
             for(Chofer chofer: new ChoferDAO(conexiondb).listar()){
                        if (chofer.getSucursalId() != null && chofer.getSucursalId() == sucursalId) {
                                resultado.add(chofer);
                 }
             }
             return resultado;
    }
            
    private Collection<Ruta> rutasSucursal(ConexionDB conexiondb, int sucursalId){
        Collection<Ruta> resultado = new ArrayList<>();
         for(Ruta ruta : new RutaDAO(conexiondb).listar()){
                    if (ruta.getOrigen() != null && ruta.getOrigen().getId() == sucursalId) {
                            resultado.add(ruta);
             }
         }
         return resultado;
    }
}