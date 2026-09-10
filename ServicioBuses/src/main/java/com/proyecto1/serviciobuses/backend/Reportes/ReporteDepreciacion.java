/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.ConfiguracionDepreciacion;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ConfiguracionDepreciacionDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
public class ReporteDepreciacion {
    
    private BusDAO busDao;
    private ViajeDAO viajeDao;
    private ConfiguracionDepreciacionDAO configuracionDao;
    
    public ReporteDepreciacion(ConexionDB conexiondb){
            this.busDao = new BusDAO(conexiondb);
            this.viajeDao = new ViajeDAO(conexiondb);
            this.configuracionDao = new ConfiguracionDepreciacionDAO(conexiondb);
    }
    public Collection<Bus> listarBuses(int sucursalId){
          Collection<Bus> resultado = new ArrayList<>();
        
          for(Bus bus: busDao.listar()){
               
              if (bus.getSucursalId() == sucursalId) {
                    resultado.add(bus);
              }
          }
          return resultado;
}
    
    public double totalKilometraje(int busId){
         double total = 0;
         
         for(Viaje viaje :  viajeDao.listar()){
             
             if (viaje.getBus() != null && viaje.getBus().getId() == busId && viaje.getHoraLlegadaReal() != null) {
                  total += viaje.getKilometrajeFinal() - viaje.getKilometrajeInicial();
             }
         }
         return total;
    }
    
    public double depreciacionAcumulada(int busId){
        double total = 0;
        
        for(Viaje viaje : viajeDao.listar()){
            
            if (viaje.getBus() != null && viaje.getBus().getId() == busId && viaje.getHoraLlegadaReal() != null) {
                 total += viaje.getMontoDepreciacion();
            }
        }
        return total;
    }
    
    public double montoPorKilometroActual(){
            ConfiguracionDepreciacion configuracion = configuracionDao.obtenerActual();
            
            if (configuracion == null) {
                return 0;
        }
            return configuracion.getMontoPorKilometraje();
    }
}
