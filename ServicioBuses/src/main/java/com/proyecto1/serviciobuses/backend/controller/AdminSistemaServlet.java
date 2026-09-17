/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.Cliente;
import com.proyecto1.serviciobuses.backend.Model.ConfiguracionDepreciacion;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Servicio.SucursalServicio;
import com.proyecto1.serviciobuses.backend.Servicio.UsuarioServicio;
import com.proyecto1.serviciobuses.backend.dao.ConfiguracionDepreciacionDAO;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import com.proyecto1.serviciobuses.backend.dao.UsuarioDAO;
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
@WebServlet(name = "AdminSistemaServlet", urlPatterns = {"/AdminSistemaServlet"})
public class AdminSistemaServlet extends HttpServlet {

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
                response.sendError(  HttpServletResponse.SC_FORBIDDEN);
                return;
        }
         String accion = servletUtil.accion(request, "sucursales");
         ConexionDB conexiondb = new ConexionDB();
         
         try {
               SucursalDAO sucursalDao = new SucursalDAO(conexiondb);
               UsuarioDAO usuarioDao = new UsuarioDAO(conexiondb);
               request.setAttribute("seccion", accion);
               
               if ("configuracion".equals(accion)) {
                      ConfiguracionDepreciacionDAO dao = new ConfiguracionDepreciacionDAO (conexiondb);
                      request.setAttribute("configuracion", dao.obtenerActual());
             } else if("administradores".equals(accion)){
                    Collection<Usuario> administradores = new ArrayList<>();
                    
                        for(Usuario usuario : usuarioDao.listar()){
                                if (usuario instanceof AdministradorSucursal) {
                                    administradores.add(usuario);
                            }
                        }
                        request.setAttribute("administradores", administradores);
                        request.setAttribute("sucursales", sucursalDao.listar());
             } else if("usuarios".equals(accion)){
                  Collection<Usuario> usuarios = new ArrayList<>();

                 for (Usuario usuario : usuarioDao.listar()) {
                  if (usuario instanceof Cliente) {
                    usuarios.add(usuario);
                   }
                 }
                    request.setAttribute("usuarios", usuarios);
             } else {
                        request.setAttribute("sucursales", sucursalDao.listar());
             }
               
               request.getRequestDispatcher("/WEB-INF/vistas/admin/admin.jsp").forward(request, response);
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
        
        Usuario actual = servletUtil.obtenerUsuario(request);
        
        if (!servletUtil.esAdministradorSistema(request) || actual == null) {
                return;
        }
        String accion = servletUtil.accion(request, "");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
             boolean resultado = false;
             
             if ("crearSucursal".equals(accion)) {
                    resultado = crearSucursal(request,actual,conexiondb);
            } else if ("actualizarSucursal".equals(accion)) {
                  resultado = actualizarSucursal(request,conexiondb);
            } else if ("crearAdministador".equals(accion)) {
                  resultado = crearAdministrador(request,actual,conexiondb);
            } else if ("actualizarAdministrador".equals(accion)) {
                    resultado = actualizarAdministrador(request,conexiondb);
            } else if ("cambiarEstadoUsuario".equals(accion)) {
                 UsuarioServicio servicio = new UsuarioServicio(conexiondb);
                 
                 resultado = servicio.cambiarEstado(servletUtil.entero(request, "usuarioId"), Boolean.parseBoolean(servletUtil.texto(request, "estado")));
                 
            } else if ("depreciacion".equals(accion)) {
                  double monto = servletUtil.decimal(request, "monto");
                  if (monto >= 0) {
                       ConfiguracionDepreciacionDAO  dao= new ConfiguracionDepreciacionDAO(conexiondb);
                       resultado = dao.insertar(new ConfiguracionDepreciacion(0,monto));
                 }
            }
             
             response.sendRedirect(request.getContextPath() + "/AdminSistemaServlet?resultado=" + resultado);
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/AdminSistemaServlet?resultado=false");
        } finally {
            conexiondb.cerrar();
        }
    }
    
    private boolean crearSucursal(HttpServletRequest request,Usuario actual,ConexionDB conexiondb) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(servletUtil.texto(request, "nombre"));
        sucursal.setDireccion(servletUtil.texto(request, "direccion"));
        sucursal.setTelefono(servletUtil.texto(request, "telefono"));
        AdministradorSucursal administrador = new AdministradorSucursal(0, 
                servletUtil.texto(request,"adminNombre"),
                servletUtil.texto( request, "adminNit"),
                servletUtil.texto( request,"adminDpi"),
                 servletUtil.texto(request,"adminTelefono" ),
                 servletUtil.texto( request, "adminDireccion"),
                  servletUtil.texto(request, "adminUsername"),
                  servletUtil.texto(request,"adminPassword"),
                        true
                );
        
        SucursalServicio servicio = new SucursalServicio(conexiondb);
        
        return servicio.crearSucursal(actual.getId(), sucursal, administrador);
        
    }

    private boolean actualizarSucursal(HttpServletRequest request,ConexionDB conexiondb) {
            Sucursal sucursal = new Sucursal();
            sucursal.setId(servletUtil.entero(request, "sucursalId"));
            sucursal.setDireccion(servletUtil.texto(request, "direccion"));
            sucursal.setTelefono(servletUtil.texto(request, "telefono"));
            
            return new SucursalDAO(conexiondb).actualizar(sucursal);
    }
    
    private boolean crearAdministrador( HttpServletRequest request, Usuario actual,ConexionDB conexiondb) {
          AdministradorSucursal administrador = new AdministradorSucursal(0,
                  servletUtil.texto(request,"nombre" ),
                   servletUtil.texto(request, "nit"),
                   servletUtil.texto(request, "dpi" ),
                   servletUtil.texto(request, "telefono" ),
                    servletUtil.texto( request, "direccion" ),
                    servletUtil.texto(request,"username"),
                     servletUtil.texto(request, "password"),
                        true
                );
          administrador.setSucursalId(servletUtil.entero(request, "sucursalId"));
          UsuarioServicio servicio = new UsuarioServicio(conexiondb);
          
          return servicio.registrarAdministradorSucursal(actual.getId(), administrador);
    }
    
    private boolean actualizarAdministrador(HttpServletRequest request,ConexionDB conexiondb) {
        UsuarioDAO usuarioDao = new UsuarioDAO(conexiondb);
        Usuario usuario = usuarioDao.buscarPorId(servletUtil.entero(request, "usuarioId"));
        
        if (!(usuario instanceof AdministradorSucursal)) {
                return false;
        }
        
        usuario.actualizarPerfil( servletUtil.texto( request, "nombre"), servletUtil.texto(request,"nit"),
                servletUtil.texto( request,"dpi"),
                servletUtil.texto(request, "telefono" ),
                servletUtil.texto(request, "direccion"));

        return new UsuarioServicio(conexiondb).actualizarPerfil(usuario);
    }
   

}
