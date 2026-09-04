/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class Bus {
    
    private int id;
    private String foto;
    private String marca;
    private String placa;
    private String modelo;
    private int añoFabricacion;
    private int capacidad;
    private double kilometrajeActual;
    private boolean estado;
    private int sucursalId;
    
    public Bus(){
    
    }
    
    public Bus(int id, String foto, String placa,
               String marca, String modelo,
               int anioFabricacion, int capacidad,
               double kilometrajeActual, boolean estado){
        
         this.id = id;
        this.foto = foto;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.añoFabricacion = anioFabricacion;
        this.capacidad = capacidad;
        this.kilometrajeActual = kilometrajeActual;
        this.estado = estado;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAñoFabricacion() {
        return añoFabricacion;
    }

    public void setAñoFabricacion(int añoFabricacion) {
        this.añoFabricacion = añoFabricacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(double kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }
    
    
    
}
