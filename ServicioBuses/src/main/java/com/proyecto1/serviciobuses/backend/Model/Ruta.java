/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class Ruta {
    
    private int id;
    private Sucursal origen;
    private Sucursal destino;
    private double distanciaKilometraje;
    private double precioBoleto;
    
    public Ruta (){}
    
    public Ruta(int id, Sucursal origen,Sucursal destino, double distanciaKilomatraje,double precioBoleto){
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.distanciaKilometraje = distanciaKilomatraje;
        this.precioBoleto = precioBoleto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sucursal getOrigen() {
        return origen;
    }

    public void setOrigen(Sucursal origen) {
        this.origen = origen;
    }

    public Sucursal getDestino() {
        return destino;
    }

    public void setDestino(Sucursal destino) {
        this.destino = destino;
    }

    public double getDistanciaKilometraje() {
        return distanciaKilometraje;
    }

    public void setDistanciaKilometraje(double distanciaKilometraje) {
        this.distanciaKilometraje = distanciaKilometraje;
    }

    public double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }
    
    
    
}
