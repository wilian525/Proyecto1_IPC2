/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Mantenimiento;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.MantenimientoDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
public class ReporteCosto {
    
    private BusDAO busDao;
    private ViajeDAO viajeDao;
    private MantenimientoDAO mantenimientoDao;
    private ReporteUtil reporteUtil;
    
     public ReporteCosto(ConexionDB conexiondb) {
        this.busDao =new BusDAO(conexiondb);
        this.viajeDao =new ViajeDAO(conexiondb);
        this.mantenimientoDao = new MantenimientoDAO(conexiondb);
         this.reporteUtil = new ReporteUtil();
    }
     
     public Collection<Viaje> listarViajes(int sucursalId, LocalDate fechaInicio,LocalDate fechaFin){
         Collection<Viaje> resultado = new ArrayList<>();
         
         for(Viaje viaje : viajeDao.listar()){
             
             if (viaje.getBus() == null || viaje.getHoraLlegadaReal() == null || !reporteUtil.fechaEnRango(viaje.getFechaSalida(), fechaInicio, fechaFin)) {
                    continue;
             }
             Bus bus = buscarBus(viaje.getBus().getId());
             if (bus != null && bus.getSucursalId() == sucursalId) {
                   viaje.setBus(bus);
                   resultado.add(viaje);
             }
         }
         return resultado;
     }
     
     public Collection<Mantenimiento> listarMantenimiento(int sucursalId, LocalDate fechaInicio,LocalDate fechaFin){
            Collection<Mantenimiento> resultado = new ArrayList<>();
            
            for(Bus bus : busDao.listar()){
                
                if (bus.getSucursalId() != sucursalId) {
                       continue;
                }
                
                for(Mantenimiento mantenimiento : mantenimientoDao.listarPorBus(bus.getId())){
                        
                    if (reporteUtil.fechaEnRango(mantenimiento.getFecha(), fechaInicio, fechaFin)) {
                              resultado.add(mantenimiento);
                    }
                }
            }
            return resultado;
     }
     
     public double totalCombustible(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
         double total = 0;
         
         for(Viaje viaje : listarViajes(sucursalId,fechaInicio,fechaFin)){
                    total += viaje.getGastoCombustible();
         }
         return total;
     }
     
     public double totalManoObra(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
         double total = 0;
         
         for(Mantenimiento mantenimiento : listarMantenimiento(sucursalId,fechaInicio,fechaFin)){
                    total += mantenimiento.getMontoManoObra();
         }
         return total;
     }
     
     public double totalRepuesto(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
            double total = 0;

        for (Mantenimiento mantenimiento :  listarMantenimiento(sucursalId, fechaInicio, fechaFin)) {

            total +=mantenimiento.getMontoRepuesto();
        }
        return total;
     }
     
     public double totalDepreciacion(int sucursalId,LocalDate fechaInicio, LocalDate fechaFin){
         double total = 0;

        for (Viaje viaje :listarViajes(sucursalId, fechaInicio,fechaFin)) {

            total += viaje.getMontoDepreciacion();
        }
        return total;
     }
     
     public double granTotal(int sucursalId,LocalDate fechaInicio, LocalDate fechaFin){
            
          return totalCombustible(
                sucursalId,
                fechaInicio,
                fechaFin
        )
                + totalManoObra(
                        sucursalId,
                        fechaInicio,
                        fechaFin
                )
                + totalRepuesto(
                        sucursalId,
                        fechaInicio,
                        fechaFin
                )
                + totalDepreciacion(
                        sucursalId,
                        fechaInicio,
                        fechaFin
                );
        }
     
     private Bus buscarBus(int busId){
         return busDao.buscarPorId(busId).orElse(null);
     }
}
