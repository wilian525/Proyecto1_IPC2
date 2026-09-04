/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

import java.time.LocalDate;

/**
 *
 * @author wilian
 */
public class Chofer extends Usuario {
    
    private String numeroLicencia;
    private String foto;
    private char tipoLicencia;
    private LocalDate fechaVencimiento ;
    private double salarioBasePorViaje ;
    
    
    public Chofer(){
    
    }

    public Chofer(int id, String nombre, String nit, String dpi,
                  String telefono, String direccion,
                  String username, String password, boolean estado,
                  String numeroLicencia, String foto,
                  char tipoLicencia, LocalDate fechaVencimiento,
                  double salarioBasePorViaje) {
            
        super(id,nombre,nit,dpi,telefono,direccion,username,password,estado);
        
        this.numeroLicencia = numeroLicencia;
        this.foto = foto;
        this.tipoLicencia = tipoLicencia;
        this.fechaVencimiento = fechaVencimiento;
        this.salarioBasePorViaje = salarioBasePorViaje;
    }
    
    public boolean licenciaVigente(LocalDate fechaActual){
        return fechaVencimiento != null && !fechaVencimiento.isBefore(fechaActual);
        }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public char getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(char tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getSalarioBasePorViaje() {
        return salarioBasePorViaje;
    }

    public void setSalarioBasePorViaje(double salarioBasePorViaje) {
        this.salarioBasePorViaje = salarioBasePorViaje;
    }
    
    
}
