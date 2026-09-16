/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Mantenimiento;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Servicio.UsuarioServicio;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.MantenimientoDAO;
import java.io.IOException;
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
@WebServlet(name = "FlotaServlet", urlPatterns = {"/FlotaServlet"})
public class FlotaServlet extends HttpServlet {

    private ServletUtil servletUtil =  new ServletUtil();
  
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
        
        if (!esAdministradorValido(usuario)) {
                  response.sendError(HttpServletResponse.SC_FORBIDDEN);
                  return;
        }
        String accion = servletUtil.accion(request, "buses");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
             request.setAttribute("seccion", accion);
                if ("choferes".equals(accion)) {
                    request.setAttribute("choferes", listarChoferes(conexiondb,usuario.getSucursalId()));
            } else if ("mantenimiento".equals(accion)) {
                Collection<Bus> buses = listarBuses(conexiondb,usuario.getSucursalId());
                request.setAttribute("buses", buses);
                Integer busId = servletUtil.enteroOpcional(request, "busId");
                
                    if (busId != null && busPerteneceSucursal(conexiondb,busId,usuario.getSucursalId())) {
                         request.setAttribute("mantenimiento", new MantenimientoDAO(conexiondb).listarPorBus(busId));
                         request.setAttribute("busId", busId);
                    }
            } else {
                request.setAttribute("buses", listarBuses(conexiondb,usuario.getSucursalId()));
            }
                System.out.println("ANTES DEL FORWARD - seccion: " + accion);
                request.getRequestDispatcher("/WEB-INF/vistas/sucursal/flota.jsp").forward(request, response);
                System.out.println("DESPUES DEL FORWARD");
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
        
        request.setCharacterEncoding("UTF-8");
        
        Usuario usuario = servletUtil.obtenerUsuario(request);
            if (!esAdministradorValido(usuario)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return ;
        }
            String accion = servletUtil.accion(request, "");
             ConexionDB conexiondb = new ConexionDB();
             
             try {
                  boolean resultado = false;
                  
                  if ("crearBus".equals(accion)) {
                     Bus bus = construirBus(request);
                     bus.setSucursalId(usuario.getSucursalId());
                     bus.setEstado(true);
                     
                     resultado = new BusDAO(conexiondb).insertar(bus);
                 } else if ("actualizarBus".equals(accion)) {
                     
                       resultado = actualizarBus(request,usuario,conexiondb);
                       
                 } else if ("desactivarBus".equals(accion)) {
                       int busId = servletUtil.entero(request, "busId");
                       
                       if ( busPerteneceSucursal(conexiondb, busId,  usuario.getSucursalId())) {
                            resultado = new BusDAO(conexiondb).desactivar(busId);
                      }
                       
                 } else if ("crearChofer".equals(accion)) {
                      Chofer chofer = construirChofer(request);
                      chofer.setSucursalId(usuario.getSucursalId());
                      chofer.setEstado(true);
                      
                      resultado = new ChoferDAO(conexiondb).insertar(chofer);
                      
                 } else if ("actualizarChofer".equals(accion)) {
                      resultado = actualizarChofer(request,usuario,conexiondb);
                      
                 } else if ("estadoChofer".equals(accion)) {
                      int choferId = servletUtil.entero(request, "choferId");
                      
                      if (choferPerteneceSucursal(conexiondb,choferId,usuario.getSucursalId())) {
                           resultado = new UsuarioServicio(conexiondb).cambiarEstado(choferId, Boolean.parseBoolean(servletUtil.texto(request, "estado")));
                           
                      }
                 } else if ("mantenimiento".equals(accion)) {
                       resultado = registrarMantenimiento(request,usuario,conexiondb);
                 }
                  response.sendRedirect( request.getContextPath()+ "/FlotaServlet?accion=buses&resultado="+ resultado);
        }finally {
                 conexiondb.cerrar();
        }
    }

    private boolean actualizarBus( HttpServletRequest request,Usuario usuario,ConexionDB conexiondb) {
        int busId = servletUtil.entero(request, "busId");
        BusDAO busDao = new BusDAO(conexiondb);
        Bus anterior = busDao.buscarPorId(busId).orElse(null);
        
        if (anterior == null || anterior.getSucursalId() != usuario.getSucursalId()) {
              return false;
        }
        Bus bus = construirBus(request);
        bus.setId(busId);
        bus.setSucursalId(usuario.getSucursalId());
        bus.setEstado(anterior.isEstado());
        
        return busDao.actualizar(bus);
    }
    
    private boolean actualizarChofer( HttpServletRequest request,Usuario usuario,ConexionDB conexiondb) {
         int choferId = servletUtil.entero(request, "chofer");
         ChoferDAO choferDao = new ChoferDAO(conexiondb);
         Chofer chofer = choferDao.buscarPorId(choferId).orElse(null);
         
         if (chofer == null || chofer.getSucursalId() == null || !chofer.getSucursalId().equals(usuario.getSucursalId())) {
                return false;
        }
         chofer.setNombre(servletUtil.texto(request, "nombre"));
         chofer.setNit(servletUtil.texto(request, "nit"));
         chofer.setDpi(servletUtil.texto(request, "dpi"));
         chofer.setTelefono( servletUtil.texto(request,"telefono" ));
        chofer.setDireccion(servletUtil.texto(request,"direccion"));
        chofer.setFoto(servletUtil.texto( request, "foto" ));
        chofer.setNumeroLicencia(servletUtil.texto( request,"numeroLicencia" ));

        String licencia = servletUtil.texto( request, "tipoLicencia" );
        if (!licencia.isBlank()) {
            chofer.setTipoLicencia( licencia.charAt(0));
        }
       
        chofer.setFechaVencimiento(servletUtil.fecha(request, "fechaVencimiento"));
        chofer.setSalarioBasePorViaje(servletUtil.decimal(request, "salario"));
        
        return choferDao.actualizar(chofer);
    }
    
    private boolean registrarMantenimiento(HttpServletRequest request,Usuario usuario,  ConexionDB conexiondb) {
         int busId = servletUtil.entero(request, "busId");
         if (!busPerteneceSucursal(conexiondb,busId,usuario.getSucursalId())) {
                return false;
        }
         Mantenimiento mantenimiento = new Mantenimiento();
         mantenimiento.setBusId(busId);
         mantenimiento.setFecha(servletUtil.fecha(request, "fecha"));
         mantenimiento.setMontoManoObra(servletUtil.decimal(request, "manoObra"));
         mantenimiento.setMontoRepuesto(servletUtil.decimal(request, "repuesto"));
         
         if (mantenimiento.getMontoManoObra() < 0 || mantenimiento.getMontoRepuesto() < 0) {
                return false;
        }
          return new MantenimientoDAO(conexiondb).insertar(mantenimiento);
    }
    
    private Bus construirBus(HttpServletRequest request){
        Bus bus = new Bus();
        
        bus.setFoto(servletUtil.texto(request, "foto"));
        bus.setPlaca(servletUtil.texto(request, "placa"));
        bus.setMarca(servletUtil.texto(request, "marca"));
        bus.setModelo(servletUtil.texto(request, "modelo"));
        bus.setAñoFabricacion(servletUtil.entero(request, "anioFabricacion"));
        bus.setCapacidad(servletUtil.entero(request, "capacidad"));
        bus.setKilometrajeActual(servletUtil.decimal(request, "kilometraje"));
        
        return bus;
    }
    
    private Chofer construirChofer(HttpServletRequest request){
        Chofer chofer = new Chofer();
        
        chofer.setNombre( servletUtil.texto( request,"nombre" ));
        chofer.setNit( servletUtil.texto( request,"nit" ));
        chofer.setDpi(servletUtil.texto( request,"dpi" ));
        chofer.setTelefono(servletUtil.texto(request, "telefono" ) );
        chofer.setDireccion(servletUtil.texto( request,"direccion") );
        chofer.setUserName( servletUtil.texto(request,"username") );
        chofer.setPassword(servletUtil.texto( request,"password"));
        chofer.setFoto( servletUtil.texto(request,"foto"));
        chofer.setNumeroLicencia(servletUtil.texto( request, "numeroLicencia") );

        String licencia = servletUtil.texto(request, "tipoLicencia");
        
        if (!licencia.isBlank()) {
              chofer.setTipoLicencia(licencia.charAt(0));
        }
        chofer.setFechaVencimiento(servletUtil.fecha(request, "fechaVencimiento"));
        chofer.setSalarioBasePorViaje(servletUtil.decimal(request, "salario"));
        return chofer;
    }
    
    private Collection<Bus> listarBuses(ConexionDB conexiondb,int sucursalId){
            Collection<Bus> resultado = new ArrayList<>();
            
            for(Bus bus : new BusDAO(conexiondb).listar()){
                    if (bus.getSucursalId() == sucursalId) {
                            resultado.add(bus);
                }
            }
            return resultado;
    }
    
    private Collection<Chofer> listarChoferes(ConexionDB conexiondb, int sucursalId){
                Collection<Chofer> resultado = new ArrayList<>();
                
                for(Chofer chofer : new ChoferDAO(conexiondb).listar()){
                            if (chofer.getSucursalId() != null  && chofer.getSucursalId() == sucursalId) {
                                   resultado.add(chofer);
                    }
                }
                return resultado;
    }
    
    private boolean busPerteneceSucursal(ConexionDB conexiondb,int busId,int sucursalId){
            Bus bus = new BusDAO(conexiondb).buscarPorId(busId).orElse(null);
            
            return bus != null && bus.getSucursalId() == sucursalId;
    }
    
    private boolean choferPerteneceSucursal(ConexionDB conexiondb,int choferId,int sucursalId){
            Chofer chofer = new ChoferDAO(conexiondb).buscarPorId(choferId).orElse(null);
            
            return  chofer != null && chofer.getSucursalId() != null && chofer.getSucursalId() == sucursalId;
    }
    
    private boolean esAdministradorValido(Usuario usuario){
            return usuario instanceof AdministradorSucursal && usuario.getSucursalId() != null;
    }
   
}
