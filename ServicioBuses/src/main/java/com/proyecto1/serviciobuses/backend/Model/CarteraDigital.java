/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Model;

/**
 *
 * @author wilian
 */
public class CarteraDigital {
    
    private int id;
    private double saldo;

    public CarteraDigital(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }
    
    public void recargar(double monto){
        saldo +=  monto;
    }
    
    public boolean descontar( double monto){
            if (tieneSaldo(monto)) {
                   saldo -= monto;
                   return true;
            }
            return false;
}
    
    public boolean tieneSaldo(double monto){
        return saldo >= monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    
}
