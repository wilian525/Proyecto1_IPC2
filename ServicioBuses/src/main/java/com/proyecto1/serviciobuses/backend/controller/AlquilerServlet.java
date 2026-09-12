/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.Servicio.AlquilerServicio;
import com.proyecto1.serviciobuses.backend.Servicio.ViajeServicio;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajePrivadoDAO;
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
@WebServlet(name = "AlquilerServlet", urlPatterns = {"/AlquilerServlet"})
public class AlquilerServlet extends HttpServlet {

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
             request.setAttribute("sucursales", new SucursalDAO(conexiondb).listar());
             request.setAttribute("alquileres", obtenerAlquileres(request,usuario,conexiondb));
             
             if (servletUtil.esAdministradorSucursal(request ) && usuario.getSucursalId() != null) {
                   request.setAttribute("buses", busesSucursal(conexiondb,usuario.getSucursalId()));
                   request.setAttribute("choferes", choferesSucursal(conexiondb,usuario.getSucursalId())); 
            }
                    request.getRequestDispatcher("/WEB-INF/vistas/alquileres.jsp").forward(request, response);
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
        
        Usuario usuario = servletUtil.obtenerUsuario(request);
        if (usuario == null) {
               response.sendRedirect(request.getContextPath() + "/auth");
               return;
        }
        String accion = servletUtil.accion(request, "");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
                AlquilerServicio servicio = new AlquilerServicio(conexiondb);
                boolean resultado = false;
                
                if ("solicitar".equals(accion)) {
                   resultado = solicitar(request,usuario,servicio,conexiondb);
                   
            }else if ("pagar".equals(accion)) {
                int viajeId =servletUtil.entero( request,"viajeId");

                ViajePrivado privado = new ViajePrivadoDAO( conexiondb ).buscarPorIdYUsuario(  viajeId, usuario.getId()).orElse(null);
                if (privado != null) {
                    resultado = servicio.pagarAlquiler(viajeId, usuario.getId(), servletUtil.fecha( request, "fechaPago" )  );
                }
            } else if ("confirmar".equals(accion)) {
                resultado =confirmar( request,usuario, servicio, conexiondb );

            } else if ("asignar".equals(accion)) {
                resultado = asignar(request, usuario,servicio,conexiondb);

            } else if ("eliminar".equals(  accion)) {
                int viajeId = servletUtil.entero(request, "viajeId");

                if (esAlquilerSucursal(conexiondb, viajeId,usuario)) {
                    resultado = new ViajeServicio( conexiondb ).eliminarViaje( viajeId );
                }
            }

            response.sendRedirect(request.getContextPath() + "/alquileres?resultado="+ resultado );

        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/alquileres?resultado=false");
        } finally {
            conexiondb.cerrar();
        }
    }
    
    
    private boolean solicitar( HttpServletRequest request,Usuario usuario,AlquilerServicio servicio,ConexionDB conexiondb) {
          int sucrsalId = servletUtil.entero(request, "sucursalId");
          Sucursal sucursal = new SucursalDAO(conexiondb).buscarPorId(sucrsalId);
          if (sucursal == null) {
               return false;
        }
          ViajePrivado alquiler = new ViajePrivado();
         
          alquiler.setOrigen(servletUtil.texto(request, "origen"));
          alquiler.setDestino(servletUtil.texto(request, "destino"));
          alquiler.setFechaSalida(servletUtil.fecha( request,"fechaSalida"));
          alquiler.setHoraSalidaProgramada(servletUtil.hora( request,"horaSalida" ) );
          alquiler.setFechaRetorno(servletUtil.fechaOpcional(request,"fechaRetorno" ));
        alquiler.setNumeroPasajeros(servletUtil.entero( request,"pasajeros" ) );
        alquiler.setFechaLlegadaEstimada( servletUtil.fecha( request, "fechaLlegadaEstimada" ));
        alquiler.setHoraLlegadaEstimada(servletUtil.hora(request,"horaLlegadaEstimada" ) );

        return servicio.solicitarAlquiler( usuario.getId(), alquiler);
    }
    
    private boolean confirmar(HttpServletRequest request,  Usuario usuario, AlquilerServicio servicio,  ConexionDB conexiondb) {
        
          int viajeId = servletUtil.entero(request, "viajeId");
          if (!esAlquilerSucursal(conexiondb,viajeId,usuario)) {
              return false;
        }
           return servicio.confirmarAlquiler(viajeId, servletUtil.decimal(request, "precio"));
    }
    
    private boolean  asignar(HttpServletRequest request, Usuario usuario,AlquilerServicio servicio,ConexionDB conexiondb) {
         int viajeId = servletUtil.entero(request, "viajeId");
         int busId = servletUtil.entero(request, "busId");
         int choferId = servletUtil.entero(request, "choferId");
         
         if (!esAlquilerSucursal(conexiondb,viajeId,usuario)) {
                return false;
        }
         Bus bus = new BusDAO(conexiondb).buscarPorId(busId).orElse(null);
         Chofer chofer = new ChoferDAO(conexiondb).buscarPorId(busId).orElse(null);
         
         if (bus == null || chofer == null || usuario.getSucursalId() == null || bus.getSucursalId() != usuario.getSucursalId()|| chofer.getSucursalId() == null|| chofer.getSucursalId()!= usuario.getSucursalId()) {
            return false;
        }
         return servicio.asignarBusYChofer(viajeId, busId, choferId);
}
    
    private Collection<ViajePrivado> obtenerAlquileres(HttpServletRequest request,Usuario usuario, ConexionDB conexiondb) {
           Collection<ViajePrivado> resultado = new ArrayList<>();
           
           ViajeDAO viajeDao = new ViajeDAO(conexiondb);
           ViajePrivadoDAO privadoDao = new ViajePrivadoDAO(conexiondb);
           
           for(Viaje viaje : viajeDao.listar()){
                  ViajePrivado privado;
                  
                  if (servletUtil.esAdministradorSucursal(request)) {
                        privado = privadoDao.buscarPorId(viaje.getId()).orElse(null);
                        
                        if (privado == null || usuario.getSucursalId() == null ) {
                               continue;
                      }
               } else {
                    privado = privadoDao.buscarPorIdYUsuario(viaje.getId(), usuario.getId()).orElse(null);
                      if (privado == null) {
                             continue;
                      }
                  }
                  copiarDatos(viaje,privado );
                  resultado.add(privado);
           }
           return resultado;
    }
    
    private boolean esAlquilerSucursal(ConexionDB conexiondb,int viajeId,Usuario usuario) {
          if (!(usuario instanceof AdministradorSucursal) || usuario.getSucursalId() == null) {
               return false;
        }
          ViajePrivado privado = new ViajePrivadoDAO(conexiondb).buscarPorId(viajeId).orElse(null);
          
          return privado != null && privado.getSucursalId() == usuario.getSucursalId();
    } 
    
    private Collection<Bus> busesSucursal(ConexionDB conexiondb,int sucursalId){
        Collection<Bus> resultado = new ArrayList<>();
        
        for(Bus bus : new BusDAO(conexiondb).listar()){
                if (bus.getSucursalId() == sucursalId && bus.isEstado()) {
                  resultado.add(bus);
            }
        }
        return resultado;
    }
    
    private Collection<Chofer> choferesSucursal(ConexionDB conexiondb,int sucursalId){
       Collection<Chofer> resultado = new ArrayList<>();
       
       for(Chofer chofer: new ChoferDAO(conexiondb).listar()){
                if (chofer.getSucursalId() != null && chofer.getSucursalId() == sucursalId && chofer.isEstado()) {
                        resultado.add(chofer);
           }
       }
                return resultado;
    }
    
    private void copiarDatos(Viaje viaje, ViajePrivado privado){
            privado.setFechaSalida(viaje.getFechaSalida());
            privado.setHoraSalidaProgramada(viaje.getHoraSalidaProgramada());
            privado.setFechaLlegadaEstimada(viaje.getFechaLlegadaEstimada() );
           privado.setHoraLlegadaEstimada( viaje.getHoraLlegadaEstimada());
        privado.setHoraSalidaReal(viaje.getHoraSalidaReal());
        privado.setHoraLlegadaReal(viaje.getHoraLlegadaReal() );
        privado.setKilometrajeInicial(viaje.getKilometrajeInicial());
        privado.setKilometrajeFinal(viaje.getKilometrajeFinal());
        privado.setGastoCombustible( viaje.getGastoCombustible());
        privado.setMontoDepreciacion(viaje.getMontoDepreciacion());
        privado.setBus( viaje.getBus());
        privado.setChofer(viaje.getChofer() );
    }
}