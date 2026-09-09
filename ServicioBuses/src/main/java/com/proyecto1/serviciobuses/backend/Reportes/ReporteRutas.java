/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Boleto;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.dao.BoletoDAO;
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
public class ReporteRutas {
    
    private RutaDAO rutaDao;
    private SucursalDAO sucursalDao;
    private ViajeDAO viajeDao;
    private ViajeRegularDAO viajeRegularDao;
    private BoletoDAO boletoDao;
    private ReporteUtil reporteUtil;
    
    public ReporteRutas(ConexionDB conexiondb) {
        this.rutaDao =new RutaDAO(conexiondb);
        this.sucursalDao = new SucursalDAO(conexiondb);
        this.viajeDao = new ViajeDAO(conexiondb);
        this.viajeRegularDao =new ViajeRegularDAO(conexiondb );
        this.boletoDao = new BoletoDAO(conexiondb);
    }
    
    public Collection<Ruta> rutasMasDemandadas(LocalDate fechaInicio, LocalDate fechaFin){
            ArrayList<Ruta> rutas = new ArrayList<>();
            ArrayList<Integer> totales = new ArrayList<>();
            
            // guarda cada ruta y cantidad boletos
            for(Ruta ruta : rutaDao.listar()){
                completarSucursales(ruta);
                rutas.add(ruta);
                totales.add(totalBoletoVendidos(ruta.getId(),fechaInicio,fechaFin));
            }
            
            // ordenamos de mayor a menor
            for (int i = 0; i < rutas.size() - 1; i++) {
                for (int j = 0; j < rutas.size(); j++) {
                    
                    if (totales.get(j) > totales.get(i)) {
                         Ruta rutaTemporal = rutas.get(i);
                         rutas.set(i, rutas.get(j));
                         rutas.set(j, rutaTemporal);
                         
                         int totalTemporal = totales.get(i);
                         totales.set(i, totales.get(j));
                         totales.set(j, totalTemporal);
                    }
                }
        }
            return rutas;
    }
    
    public int totalBoletoVendidos(int rutaId, LocalDate fechaInicio,LocalDate fechaFin){
            int total = 0;
            
            for(Viaje viaje : viajeDao.listar()){
                Optional<ViajeRegular> regular = viajeRegularDao.buscarPorViaje(viaje.getId());
                
                if (regular.isEmpty() || regular.get().getRuta() == null || regular.get().getRuta().getId() != rutaId) {
                     continue;
                }
                for(Boleto boleto : boletoDao.buscarPorViaje(viaje.getId())){
                    
                        if (reporteUtil.fechaEnRango(boleto.getFechaPago(),fechaInicio,fechaFin)) {
                          total++;
                    }
                }
            }
                return total;
    }
    
    public Collection<Ruta> rutasPorSucursal(int sucursalId){
        Collection<Ruta> resultado = new ArrayList<>();
        
        if (sucursalId <= 0 ) {
             return resultado;
        }
        
        for(Ruta ruta : rutaDao.listar()){
            if (ruta.getOrigen() != null && ruta.getOrigen().getId() == sucursalId) {
                completarSucursales(ruta);
                resultado.add(ruta);
            }
        }
        return resultado;
    }
    
    private void completarSucursales(Ruta ruta){
        if (ruta == null) {
             return ; 
        }
        
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
