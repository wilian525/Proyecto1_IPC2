/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class Cliente extends Usuario{
    
     public Cliente(int id, String nombre, String nit,
                   String dpi, String telefono,
                   String direccion, String username,
                   String password, boolean estado) {

        super(id, nombre, nit, dpi, telefono,
              direccion, username, password, estado);
    }
    
}
