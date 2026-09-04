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
public class Boleto {
    
    private int id;
    private int asiento;
    private LocalDate fechaPago;
    private double precio;
    private int viajeId;
    private int usuarioId;
    
    public Boleto(){
    
    }

    public Boleto(int id, int asiento, LocalDate fechaPago, double precio) {
        this.id = id;
        this.asiento = asiento;
        this.fechaPago = fechaPago;
        this.precio = precio;
    }
    
    public void confirmarCompra(){
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getViajeId() {
        return viajeId;
    }

    public void setViajeId(int viajeId) {
        this.viajeId = viajeId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    
}
