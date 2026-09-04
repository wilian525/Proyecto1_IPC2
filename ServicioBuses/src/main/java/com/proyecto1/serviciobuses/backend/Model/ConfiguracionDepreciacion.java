/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class ConfiguracionDepreciacion {
    
    private int id;
    private double montoPorKilometraje;
    
    public ConfiguracionDepreciacion(int id, double montoPorKilometraje){
        this.id = id;
        this.montoPorKilometraje = montoPorKilometraje;
    }
    
    public void actualizarMonto(double monto){
        this.montoPorKilometraje = monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontoPorKilometraje() {
        return montoPorKilometraje;
    }

    public void setMontoPorKilometraje(double montoPorKilometraje) {
        this.montoPorKilometraje = montoPorKilometraje;
    }
    
    
}
