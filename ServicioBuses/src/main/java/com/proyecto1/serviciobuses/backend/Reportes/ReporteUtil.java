/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import java.time.LocalDate;

/**
 *
 * @author wilian
 */
public class ReporteUtil {
    
    public boolean fechaEnRango(LocalDate fecha, LocalDate fechaInicio,LocalDate fechaFin){
        if (fecha == null) {
             return false;
        }
        
        if (fechaInicio != null && fecha.isBefore(fechaInicio)) {
             return false;
        }
        
        if (fechaFin != null && fecha.isAfter(fechaFin)) {
             return false;
        }
        return true;
    }
    
}
