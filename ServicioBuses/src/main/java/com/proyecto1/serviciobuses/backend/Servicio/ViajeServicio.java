/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.ConfiguracionDepreciacion;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ConfiguracionDepreciacionDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ViajeServicio {
    
    private ConexionDB conexiondb;
    
    private ViajeDAO viajeDao;
    private BusDAO busDao;
    private ChoferDAO choferDao;
    private ConfiguracionDepreciacionDAO configuracionDao;
    
    public ViajeServicio(ConexionDB conexiondb){
         this.conexiondb = conexiondb;

        this.viajeDao = new ViajeDAO(conexiondb);
        this.busDao= new BusDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
        this.configuracionDao = new ConfiguracionDepreciacionDAO(conexiondb);
    }
            
    public boolean registrarSalida(int viajeId, double kilometraje){
        if (viajeId <= 0 || kilometraje < 0) {
            return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            Optional<Viaje> resultadoviaje = viajeDao.buscarPorId(viajeId);
            
            if (resultadoviaje.isEmpty()) {
                con.rollback();
                return false;
            }
            Viaje viaje = resultadoviaje.get();
            
            // no se puede registrar la salida dos veces
            if (viaje.getHoraLlegadaReal() != null) {
                con.rollback();
                return false;
            }
            
            Optional<Bus> resultadoBus = busDao.buscarPorId(viaje.getBus().getId());
            if (resultadoBus.isEmpty()) {
                con.rollback();
                return false;
            }
            Bus bus = resultadoBus.get();
            // bus activo
            if (!bus.isEstado()) {
                con.rollback();
                return false;
            }
            
            Optional<Chofer> resultadoChofer = choferDao.buscarPorId(viaje.getChofer().getId());
            
            if (resultadoChofer.isEmpty()) {
                con.rollback();
                return false;
            }
            
            Chofer chofer = resultadoChofer.get();
            
            // el chofer debe estar activo
            if (!chofer.isEstado()) {
                con.rollback();
                return false;
            }
            
            if (kilometraje < bus.getKilometrajeActual()) {
                con.rollback();
                return false;
            }
            
            viaje.setHoraSalidaReal(LocalTime.now());
            viaje.setKilometrajeInicial(kilometraje);
            viaje.setEstado(true);
            
            bus.setKilometrajeActual(kilometraje);
            boolean salidaRegistrada = viajeDao.registrarSalida(viaje);
            
            if (!salidaRegistrada) {
                con.rollback();
                return false;
            }
            
            boolean busActualizado = busDao.actualizar(bus);
            
            if (!busActualizado) {
                con.rollback();
                return false;
            }
            
            con.commit();
            return true;
            
        } catch (SQLException |  RuntimeException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean registrarLlegada(int viajeId, double kilometraje, double gastoCombustible){
        if (viajeId <= 0 || kilometraje < 0 || gastoCombustible < 0) {
            return false;
        }
        
        Connection con = conexiondb.obtenerConeccion();
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            Optional<Viaje> resultadoViaje = viajeDao.buscarPorId(viajeId);
             if (resultadoViaje.isEmpty()) {
                con.rollback();
                return false;
            }
             
             Viaje viaje = resultadoViaje.get();
             
             // el viaje tiene que haber iniciado
             if (viaje.getHoraSalidaReal() == null) {
                    con.rollback();
                    return false;
            }
             
             // no se pudo registrar una llegada dos veces
             if (viaje.getHoraLlegadaReal() == null) {
                    con.rollback();
                    return false;
            }
             
             // el kilometraje final nunca puede ser menor al inicial
             if (kilometraje < viaje.getKilometrajeInicial()) {
                con.rollback();
                return false;
            }
             
             Optional<Bus> resultadoBus = busDao.buscarPorId(viaje.getBus().getId());
             
             if (resultadoBus.isEmpty()) {
                 con.rollback();
                 return false;
            }
             
             Bus bus = resultadoBus.get();
             ConfiguracionDepreciacion configuracion = configuracionDao.obtenerActual();
             
             if (configuracion == null) {
                 con.rollback();
                 return false;
            }
             
             double kilometrajeRecorrido = kilometraje - viaje.getKilometrajeInicial();
             double depreciacion = kilometrajeRecorrido * configuracion.getMontoPorKilometraje();
             
             viaje.setHoraLlegadaReal(LocalTime.now());
             viaje.setKilometrajeFinal(kilometraje);
             viaje.setGastoCombustible(gastoCombustible);
             viaje.setMontoDepreciacion(depreciacion);
             
             // viaje termino
             viaje.setEstado(false);
             
             bus.setKilometrajeActual(kilometraje);
             boolean llegadaRegistrada = viajeDao.registrarLlegada(viaje);
             
             if (! llegadaRegistrada) {
                 con.rollback();
                 return false;
            }
             
             boolean busActualizado = busDao.actualizar(bus);
             
             if (!busActualizado) {
                con.rollback();
                return false;
            }
             
             con.commit();
             return false;
             
        } catch (SQLException | RuntimeException  e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
               }
        }
        
    }
}
