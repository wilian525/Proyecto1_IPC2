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
public class Viaje {
    
    private int id;
    private LocalDate fechaSalida;
    private LocalTime horaSalidaProgramada;
    private LocalTime horaSalidaReal;
    private LocalTime horaLlegadaReal;
    private LocalDate fechaLlegadaEstimada;
    private LocalTime horaLlegadaEstimada;
    private double kilometrajeInicial;
    private double kilometrajeFinal;
    private double gastoCombustible;
    private double montoDepreciacion;
    private boolean estado;
    private Chofer chofer;
    private Bus bus;
    
    public Viaje(){}

    public Viaje(int id, LocalDate fechaSalida, LocalTime horaSalidaProgramada, LocalTime horaLlegadaReal, double kilometrajeInicial, double kilometrajeFinal, double gastoCombustible, boolean estado) {
        this.id = id;
        this.fechaSalida = fechaSalida;
        this.horaSalidaProgramada = horaSalidaProgramada;
        this.horaLlegadaReal = horaLlegadaReal;
        this.kilometrajeInicial = kilometrajeInicial;
        this.kilometrajeFinal = kilometrajeFinal;
        this.gastoCombustible = gastoCombustible;
        this.estado = estado;
    }
   
public double calcularDepreciacion(double montoPorKilometro) {
    if (montoPorKilometro < 0|| kilometrajeFinal < kilometrajeInicial) {
        return 0;
    }
    double kilometrosRecorridos = kilometrajeFinal - kilometrajeInicial;

    return kilometrosRecorridos * montoPorKilometro;
}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalTime getHoraSalidaProgramada() {
        return horaSalidaProgramada;
    }

    public void setHoraSalidaProgramada(LocalTime horaSalidaProgramada) {
        this.horaSalidaProgramada = horaSalidaProgramada;
    }

    public LocalTime getHoraLlegadaReal() {
        return horaLlegadaReal;
    }

    public void setHoraLlegadaReal(LocalTime horaLlegadaReal) {
        this.horaLlegadaReal = horaLlegadaReal;
    }

    public double getKilometrajeInicial() {
        return kilometrajeInicial;
    }

    public void setKilometrajeInicial(double kilometrajeInicial) {
        this.kilometrajeInicial = kilometrajeInicial;
    }

    public double getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(double kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }

    public double getGastoCombustible() {
        return gastoCombustible;
    }

    public void setGastoCombustible(double gastoCombustible) {
        this.gastoCombustible = gastoCombustible;
    }

    public double getMontoDepreciacion() {
        return montoDepreciacion;
    }

    public void setMontoDepreciacion(double montoDepreciacion) {
        this.montoDepreciacion = montoDepreciacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDate getFechaLlegadaEstimada() {
        return fechaLlegadaEstimada;
    }

    public void setFechaLlegadaEstimada(LocalDate fechaLlegadaEstimada) {
        this.fechaLlegadaEstimada = fechaLlegadaEstimada;
    }

    public LocalTime getHoraLlegadaEstimada() {
        return horaLlegadaEstimada;
    }

    public void setHoraLlegadaEstimada(LocalTime horaLlegadaEstimada) {
        this.horaLlegadaEstimada = horaLlegadaEstimada;
    }

    public LocalTime getHoraSalidaReal() {
        return horaSalidaReal;
    }

    public void setHoraSalidaReal(LocalTime horaSalidaReal) {
        this.horaSalidaReal = horaSalidaReal;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
    
    
    
}
