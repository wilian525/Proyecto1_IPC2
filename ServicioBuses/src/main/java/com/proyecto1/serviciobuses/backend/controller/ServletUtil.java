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
import java.time.LocalDate;
import java.time.LocalTime;

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
    
    public int entero(HttpServletRequest request, String parametro) {
        return Integer.parseInt(texto(request,parametro));
    }
    
    public Integer enteroOpcional(  HttpServletRequest request,String parametro){
            String valor = texto(request,parametro);
            if (valor.isBlank()) {
                    return null;
        }
            return Integer.valueOf(valor);
    }
    
    public double decimal(HttpServletRequest request,String parametro){
        return Double.parseDouble(texto(request,parametro));
    }
    
    public LocalDate fecha(HttpServletRequest request, String parametro){
            return LocalDate.parse(texto(request,parametro));
    }
    
    public LocalDate fechaOpcional(HttpServletRequest request,String parametro){
            String valor = texto(request,parametro);
                if (valor.isBlank()) {
                     return null;
        }
                return LocalDate.parse(valor);
        }
    
     public  LocalTime hora( HttpServletRequest request, String parametro) {
        return LocalTime.parse( texto(request, parametro));
    }

     
    public  String accion(HttpServletRequest request,String accionDefault) {
        String accion =texto(request, "accion");
        if (accion.isBlank()) {
            return accionDefault;
        }
        return accion;
    }
}
