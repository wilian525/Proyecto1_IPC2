/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public abstract class  Usuario {
    
    private int id;
    private String nombre;
    private String nit;
    private String dpi;
    private String telefono;
    private String direccion;
    private String userName;
    private String password;
    private boolean estado;
    private Integer sucursalId;
    
    public Usuario(){
    
    }

    public Usuario(int id, String nombre, String nit, String dpi, String telefono, String direccion, String userName, String password, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.dpi = dpi;
        this.telefono = telefono;
        this.direccion = direccion;
        this.userName = userName;
        this.password = password;
        this.estado = estado;
    }



   public boolean iniciarSesion(String username, String password){
       if (!estado) {
           return false;
       }
       return this.userName.equals(username) && this.password.equals(password);
   
   }
   
   public void actualizarPerfil(String nombre, String nit , String dpi, String telefono,String direccion){
       this.nombre = nombre;
       this.nit = nit;
       this.dpi = dpi;
       this.telefono = telefono;
       this.direccion = direccion;
   }
   
   public void activar(){
       this.estado = true;
   }
   
   public void desactivar(){
       this.estado = false;
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    
   
   
}
