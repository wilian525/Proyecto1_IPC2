/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.controller;

import com.proyecto1.serviciobuses.backend.Model.AdministradorSistemas;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author wilian
 */
public class ServletUtil {
    
    public Usuario obtenerUsuario(HttpServletRequest request){
         HttpSession sesion = request.getSession(false);
         
         if (sesion == null) {
               return null;
        }
         Object usuario = sesion.getAttribute("usuario");
         if (usuario instanceof Usuario) {
                return (Usuario) usuario;
        }
         return null;
    }
    
    public boolean esAdministradorSistema(HttpServletRequest request){
            return obtenerUsuario(request) instanceof AdministradorSistemas; 
    }
    
    public boolean esAdministradorSucursal(HttpServletRequest request){
          return obtenerUsuario(request) instanceof AdministradorSucursal;
    }
    
    public String texto( HttpServletRequest request, String parametro) {
            String valor = request.getParameter(parametro);
            
            if (valor == null) {
                return "";
        }
            return valor.trim();
    }
    
    public int entero()
}
