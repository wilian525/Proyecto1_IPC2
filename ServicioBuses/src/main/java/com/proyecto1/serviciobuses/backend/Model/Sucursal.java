/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class Sucursal {
    
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    
    public Sucursal(){}
    
    public Sucursal(int id,String nombre, String direccion,String telefono){
        this.nombre = nombre;
        this.id = id;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    public void agregarBus(){
      
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
        
        
}
