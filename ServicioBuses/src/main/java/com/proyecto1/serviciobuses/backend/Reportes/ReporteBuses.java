/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ReporteBuses { 
    
    private BusDAO busDao;
    private ChoferDAO choferDao;
    private ViajeDAO viajeDao;
    
    public ReporteBuses(ConexionDB conexiondb){
        this.busDao = new BusDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
        this.viajeDao = new ViajeDAO(conexiondb);
    }
    
    public Collection<Bus> listarBuses(int sucursalId , Boolean estado){
         Collection<Bus> resultado = new ArrayList<>();
         
          if (sucursalId <= 0) {
              return resultado;
        }
          for(Bus bus : busDao.listar()){
                if (bus.getSucursalId() != sucursalId) {
                    continue;
              }
                if ( estado != null && bus.isEstado() != estado) {
                    continue;
              }
                resultado.add(bus);
          }
          return resultado;
    }
    
    public Chofer obtenerChoferActual(int busId){
        if (busId <= 0) {
             return null;
        }
        Collection<Viaje> viajes = viajeDao.listar();
        
        // busca un viaje que este en transito
        for(Viaje viaje : viajes){
            if (!esViajeDelBus(viaje,busId)) {
                 continue;
            }
            
            if (viaje.getHoraSalidaReal() != null && viaje.getHoraLlegadaReal() == null && viaje.getChofer() != null) {
                  Optional<Chofer> chofer = choferDao.buscarPorId(viaje.getChofer().getId());
                  if (chofer.isPresent()) {
                      return chofer.get();
                }
            }
        }
        
        // si no esta viajando busca viaje programado 
        for(Viaje viaje : viajes){
            if (!esViajeDelBus(viaje,busId)) {
                  continue;
            }
            
            if (viaje.getHoraSalidaReal() == null && viaje.getHoraLlegadaReal() == null  && viaje.getChofer() != null) {
                 Optional<Chofer> chofer = choferDao.buscarPorId(viaje.getChofer().getId());
                  if (chofer.isPresent()) {
                     return chofer.get();
                }
            }
        }
        return null;
    }
    
    public int totalViajesRealizados(int busId){
        int total = 0;
        
        for(Viaje viaje : viajeDao.listar()){
        
            if (esViajeDelBus(viaje,busId) && viaje.getHoraLlegadaReal() != null) {
                  total ++;
            }
        }
        return total;
    }
    
    private boolean esViajeDelBus(Viaje viaje, int busId){
        return viaje != null && viaje.getBus() != null && viaje.getBus().getId() == busId;
    }
    
}
