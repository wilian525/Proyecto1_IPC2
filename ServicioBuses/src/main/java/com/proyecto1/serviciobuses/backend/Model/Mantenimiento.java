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
public class Mantenimiento {
    
    private int id;
    private LocalDate fecha;
    private double montoManoObra;
    private double montoRepuesto;
    private int busId;
    
    public Mantenimiento(){
    
    }
    
    public Mantenimiento(int id,LocalDate fecha, double montoManoObra,double montoRepuesto){
        this.id = id;
        this.fecha =fecha;
        this.montoManoObra = montoManoObra;
        this.montoRepuesto = montoRepuesto;
    }
    
    public double calcularCostoTotal(){
        return montoManoObra + montoRepuesto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getMontoManoObra() {
        return montoManoObra;
    }

    public void setMontoManoObra(double montoManoObra) {
        this.montoManoObra = montoManoObra;
    }

    public double getMontoRepuesto() {
        return montoRepuesto;
    }

    public void setMontoRepuesto(double montoRepuesto) {
        this.montoRepuesto = montoRepuesto;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }
    
    
}
