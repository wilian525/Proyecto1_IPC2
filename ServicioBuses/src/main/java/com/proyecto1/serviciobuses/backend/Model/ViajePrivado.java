/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author wilian
 */
public class ViajePrivado extends Viaje{
    
    public static final double TARIFA_POR_PASAJERO = 50.00;
    
    private String origen;
    private int usuarioId;
    private String destino;
    private LocalDate fechaRetorno;
    private int numeroPasajeros;
    private double precioEstimado;
    private double precioConfirmado;
    private boolean estadoAlquiler;
    private LocalDate fechaPago;
    
    public ViajePrivado() {
        super();
    }

    public ViajePrivado(String origen, String destino, LocalDate fechaRetorno, int numeroPasajeros, double precioEstimado, double precioConfirmado, boolean estadoAlquiler, LocalDate fechaPago, int id, LocalDate fechaSalida, LocalTime horaSalidaProgramada, LocalTime horaLlegadaReal, double kilometrajeInicial, double kilometrajeFinal, double gastoCombustible, boolean estado) {
        super(id, fechaSalida, horaSalidaProgramada, horaLlegadaReal, kilometrajeInicial, kilometrajeFinal, gastoCombustible, estado);
        this.origen = origen;
        this.destino = destino;
        this.fechaRetorno = fechaRetorno;
        this.numeroPasajeros = numeroPasajeros;
        this.precioEstimado = precioEstimado;
        this.precioConfirmado = precioConfirmado;
        this.estadoAlquiler = estadoAlquiler;
        this.fechaPago = fechaPago;
    }
    
    public double calcularSalarioChofer() {
          if (getChofer() == null) {
        return 0;
    }
    return getChofer().getSalarioBasePorViaje() * 1.15;
    }

    public double calcularCosto() {
        if (numeroPasajeros <= 0) {
            return 0;
        }

    double costo = numeroPasajeros * TARIFA_POR_PASAJERO;
    if (fechaRetorno != null) {
        costo = costo * 2;
    }

    return costo;
}
    
    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFechaRetorno() {
        return fechaRetorno;
    }

    public void setFechaRetorno(LocalDate fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public int getNumeroPasajeros() {
        return numeroPasajeros;
    }

    public void setNumeroPasajeros(int numeroPasajeros) {
        this.numeroPasajeros = numeroPasajeros;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    public double getPrecioConfirmado() {
        return precioConfirmado;
    }

    public void setPrecioConfirmado(double precioConfirmado) {
        this.precioConfirmado = precioConfirmado;
    }

    public boolean isEstadoAlquiler() {
        return estadoAlquiler;
    }

    public void setEstadoAlquiler(boolean estadoAlquiler) {
        this.estadoAlquiler = estadoAlquiler;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    
}
