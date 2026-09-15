/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Servicio.UsuarioServicio;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author wilian
 */
@WebServlet(name = "AutenticacionServlet", urlPatterns = {"/AutenticacionServlet"})
public class AutenticacionServlet extends HttpServlet {

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

        String accion = servletUtil.accion(request, "login");
        // 1. Caso de Logout
        if ("logout".equals(accion)) {
            HttpSession sesion = request.getSession(false);
            if (sesion != null) {
                sesion.invalidate();
            }
            
            response.sendRedirect(request.getContextPath() + "/AutenticacionServlet");
            return; // Detiene la ejecución para no llegar al forward
        }
        
          // 2. Registro realizado correctamente
    if ("ok".equals(request.getParameter("registro"))) {
        request.setAttribute("mensaje","Cuenta creada correctamente. Ahora puede iniciar sesión.");
    }

        // 3. Si el usuario ya inició sesión, redirigir al inicio
        if (servletUtil.obtenerUsuario(request) != null) {
            response.sendRedirect(request.getContextPath() + "/UsuarioServlet?accion=inicio");
            return; 
        }

        // 4. Si no hay sesión ni logout, mostrar la vista del login
        request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
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
        
        String username = servletUtil.texto(request, "username");
        String password = servletUtil.texto(request, "password");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
            UsuarioServicio servicio = new UsuarioServicio(conexiondb);
            Usuario usuario = servicio.iniciarSesion(username, password);
            System.out.println("Usuario recibido: " + usuario);
            
            if (usuario == null) {
                    request.setAttribute("error", "Usuario o constraseña incorrecos");
                    request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
                    return;
            }
            request.getSession(true).setAttribute("usuario", usuario);
            System.out.println("Sesion creada con usuario: " + usuario.getUserName());
            response.sendRedirect(request.getContextPath() + "/UsuarioServlet?accion=inicio");
            
        } finally {
            conexiondb.cerrar();
        }
    }

  

}
