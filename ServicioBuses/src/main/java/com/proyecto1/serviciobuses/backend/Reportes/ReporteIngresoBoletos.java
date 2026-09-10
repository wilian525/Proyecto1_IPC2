/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Boleto;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.dao.BoletoDAO;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.RutaDAO;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeRegularDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ReporteIngresoBoletos {
    
   private ViajeDAO viajeDao;
    private ViajeRegularDAO viajeRegularDao;
    private BoletoDAO boletoDao;
    private BusDAO busDao;
    private RutaDAO rutaDao;
    private SucursalDAO sucursalDao;
    private ReporteUtil reporteUtil;
    
     public ReporteIngresoBoletos(ConexionDB conexiondb) {
        this.viajeDao =new ViajeDAO(conexiondb);
        this.viajeRegularDao =new ViajeRegularDAO(conexiondb);
        this.boletoDao =new BoletoDAO(conexiondb);
        this.busDao = new BusDAO(conexiondb);
        this.rutaDao =new RutaDAO(conexiondb);
        this.sucursalDao =new SucursalDAO(conexiondb);
    }
     
     public Collection<Viaje> listarViajes(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin, Integer rutaId, Integer busId){
            Collection<Viaje> resultado = new ArrayList<>();
     
            for(Viaje viaje: viajeDao.listar()){
                        if (viaje.getBus() == null) {
                                continue;
                }
                        Bus bus = busDao.buscarPorId(viaje.getBus().getId()).orElse(null);
                        
                        if (bus == null ||  bus.getSucursalId() != null) {
                                continue;
                }
                        if (busId != null && bus.getId() != busId) {
                                continue;
                }
                        Optional<ViajeRegular> regular = viajeRegularDao.buscarPorViaje(viaje.getId());
                        
                        if (regular.isEmpty() || regular.get().getRuta() == null) {
                                  continue;
                }
                        int idRuta = regular.get().getRuta().getId();
                        
                        if (rutaId != null && idRuta != rutaId) {
                            continue;
                }
                        if (cantidadBoletos(viaje.getId(), fechaInicio, fechaFin) == 0) {
                              continue;
                }
                        viaje.setBus(bus);
                        resultado.add(viaje);
            }
            return resultado;
     }
     
     public Collection<Boleto> boletosVendidos(int viajeId, LocalDate fechaInicio, LocalDate fechaFin){
            Collection<Boleto> resultado = new ArrayList<>();
     
            for(Boleto boleto : boletoDao.buscarPorViaje(viajeId)){
                    
                if (reporteUtil.fechaEnRango(fechaFin, fechaInicio, fechaFin)) {
                         resultado.add(boleto);
                }
            }
            return resultado;
     }
     
     public int cantidadBoletos(int viajeId, LocalDate fechaInicio , LocalDate fechaFin){
            return boletosVendidos(viajeId, fechaInicio,fechaFin).size();
     }
     
     public double ingresoTotal(int viajeId, LocalDate fechaInicio, LocalDate fechaFin){
            double total = 0;
            
            for(Boleto boleto : boletosVendidos(viajeId,fechaInicio,fechaFin)){
                     total += boleto.getPrecio();
            }
                return total;
     }
     
     public double ingresoTotalSucursal(int sucursalId , LocalDate fechaInicio, LocalDate fechaFin){
            double total = 0;
            
            for(Viaje viaje : listarViajes(sucursalId, fechaInicio,fechaFin,null,null)){
                    total += ingresoTotal(viaje.getId(),fechaInicio,fechaFin);
            }
            return total;
     }
     
     public Ruta obtenerRuta(int viajeId){
            Optional<ViajeRegular> regular = viajeRegularDao.buscarPorViaje(viajeId);
            
            if (regular.isEmpty() || regular.get().getRuta() == null) {
                    return null;
         }
            Ruta ruta = rutaDao.buscarPorId(regular.get().getRuta().getId()).orElse(null);
            
            if (ruta == null ) {
              return null;
         }
            completarSucursales(ruta);
            return ruta;
     }
     
     private void completarSucursales(Ruta ruta){
          if (ruta.getOrigen() != null) {
                Sucursal origen = sucursalDao.buscarPorId(ruta.getOrigen().getId());
                
                if (origen != null) {
                    ruta.setOrigen(origen);
              }
         }
          
          if (ruta.getDestino() != null) {
                Sucursal destino = sucursalDao.buscarPorId(ruta.getDestino().getId());
                
                if (destino != null) {
                        ruta.setDestino(destino);
              }
         }
     }
}
