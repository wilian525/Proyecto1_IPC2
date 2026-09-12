/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.CarteraDigital;
import com.proyecto1.serviciobuses.backend.Model.Cliente;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Servicio.UsuarioServicio;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 *
 * @author wilian
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {
    
    private ServletUtil ServletUtil;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UsuarioServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsuarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
        
        String accion = ServletUtil.accion(request,"inicio");
        
        if ("registro".equals(accion)) {
             request.getRequestDispatcher("/WEB-INF/vistas/registro.jsp").forward(request, response);
             return;
        }
        Usuario usuario = ServletUtil.obtenerUsuario(request);
        
        if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/auth");
                return ;
        }
        
        if ("perfil".equals(accion)) {
                request.setAttribute("perfil", usuario);
                
                request.getRequestDispatcher("/WEB-INF/vistas/perfil.jsp").forward(request, response);
                return;
        }
        
        if ("cartera".equals(accion)) {
                ConexionDB conexiondb = new ConexionDB();
                
                try {
                        CarteraDAO carteraDao = new CarteraDAO(conexiondb);
                        
                        Optional<CarteraDigital> cartera = carteraDao.buscarPorUsuario(usuario.getId());
                        request.setAttribute("cartera", cartera.orElse(null));
                        
                        request.getRequestDispatcher("/WEB-INF/vistas/cartera.jsp").forward(request, response);
            } finally {
                    conexiondb.cerrar();
            }
                return;
        }
        request.getRequestDispatcher("/WEB-INF/vistas/inicio.jsp");
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
        processRequest(request, response);
        
        request.setCharacterEncoding("UTF-8");
        
        String accion = ServletUtil.accion(request,"");
        
        try {
            
            if ("registrar".equals(accion)) {
                    registrarCliente(request,response);
                    return;
            }
            
            Usuario usuario = ServletUtil.obtenerUsuario(request);
            
            if (usuario == null) {
                    response.sendRedirect(request.getContextPath() + "/auth");
                    return;
            }
            
            if ("actualizarPerfil".equals(accion)) {
                    actualizarPerfil(request,response,usuario);
                    return;
            }
            if ("recarga".equals(accion)) {
                 recargarCartera(request,response,usuario);
                 return;
            }
            
            response.sendRedirect(request.getContextPath() + "/usuario?accion=inicio");
            
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/usuario?accion=inicio" + "&resultado=false");
        }
    }

    private void registrarCliente(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
    
        Cliente cliente = new Cliente(0,ServletUtil.texto(request,"nombre"), ServletUtil.texto(request,"nit"),ServletUtil.texto(request,"dpi" ), ServletUtil.texto(request,"telefono" ),
                        ServletUtil.texto( request,"direccion"),  ServletUtil.texto(request,"username"),ServletUtil.texto( request,"password"), true);
        
        ConexionDB conexiondb = new ConexionDB();
        
        try {
            UsuarioServicio sevicio = new UsuarioServicio(conexiondb);
             boolean resultado = sevicio.registrarCliente(cliente);
             
             if (!resultado) {
                 request.setAttribute("error", "No fue posible registrar la cuenta");
                 request.getRequestDispatcher("/WEB-INF/vistas/registro.jsp").forward(request, response);
                 return;
            }
             response.sendRedirect(request.getContextPath() + "/auth?registro=ok");
             
        } finally {
            conexiondb.cerrar();
        }
    }
    
    private void actualizarPerfil( HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario)
            throws IOException {
        
         String nombreAnterior =usuario.getNombre();
        String nitAnterior = usuario.getNit();
        String dpiAnterior =usuario.getDpi();
        String telefonoAnterior = usuario.getTelefono();
        String direccionAnterior =usuario.getDireccion();
        
        usuario.actualizarPerfil(ServletUtil.texto( request,"nombre" ), ServletUtil.texto( request, "nit" ),ServletUtil.texto(request, "dpi" ),ServletUtil.texto(request, "telefono" ),ServletUtil.texto( request,"direccion"));
        
        ConexionDB conexiondb = new ConexionDB();
        
        try {
            UsuarioServicio sevicio = new UsuarioServicio(conexiondb);
            boolean resultado = sevicio.actualizarPerfil(usuario);
            
            if (!resultado) {
                  usuario.actualizarPerfil(nombreAnterior, nitAnterior, dpiAnterior, telefonoAnterior, direccionAnterior);
            }
            response.sendRedirect(request.getContextPath() + "/usuario?accion=perfin" + "&resultado=" + resultado);
        } finally {
            conexiondb.cerrar();
        }
    }
    
    private void recargarCartera( HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario)
            throws IOException {
        
        double monto = ServletUtil.decimal(request,"monto");
        
        ConexionDB conexiondb = new ConexionDB();
        
        try {
             CarteraDAO carteraDao = new CarteraDAO(conexiondb);
             boolean resultado = carteraDao.recargarSaldo(usuario.getId(),monto);
             response.sendRedirect(request.getContextPath() + "/usuario?accion=cartera" + "&resultado=" + resultado);
        } finally {
            conexiondb.cerrar();
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
