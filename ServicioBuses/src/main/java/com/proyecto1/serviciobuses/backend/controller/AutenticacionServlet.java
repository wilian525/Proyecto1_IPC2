/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Servicio.UsuarioServicio;
import java.io.IOException;
import java.io.PrintWriter;
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

    private ServletUtil servletUtil;
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
            out.println("<title>Servlet AutenticacionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AutenticacionServlet at " + request.getContextPath() + "</h1>");
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
        
        String accion = servletUtil.accion(request, "login");
        
        if ("logout".equals(accion)) {
              HttpSession sesion = request.getSession(false);
                if (sesion != null) {
                    sesion.invalidate();
            }
                response.sendRedirect(request.getContextPath() + "/auth");
                return;
        }
        
        if (servletUtil.obtenerUsuario(request) != null) {
                response.sendRedirect(request.getContextPath() + "/usuario?accion=inicio");
                return ;
        }
        
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
        processRequest(request, response);
        
        request.setCharacterEncoding("UFT-8");
        
        String usename = servletUtil.texto(request, "username");
        String password = servletUtil.texto(request, "password");
        ConexionDB conexiondb = new ConexionDB();
        
        try {
            UsuarioServicio servicio = new UsuarioServicio(conexiondb);
            Usuario usuario = servicio.iniciarSesion(usename, password);
            
            if (usuario == null) {
                    request.setAttribute("error", "Usuario o constraseña incorrecos");
                    request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
                    return;
            }
            request.getSession(true).setAttribute("usuario", usuario);
            response.sendRedirect(request.getContextPath() + "/usuario?accion=inicio");
            
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
