/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.ConfiguracionDepreciacion;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.dao.BoletoDAO;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ConfiguracionDepreciacionDAO;
import com.proyecto1.serviciobuses.backend.dao.RutaDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajePrivadoDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeRegularDAO;
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
    private ViajeRegularDAO viajeRegularDao;
    private ViajePrivadoDAO viajePrivadoDao;
    private BusDAO busDao;
    private ChoferDAO choferDao;
    private RutaDAO rutaDao;
    private BoletoDAO boletaDao;
    private ConfiguracionDepreciacionDAO configuracionDao;
    
    public ViajeServicio(ConexionDB conexiondb){
         this.conexiondb = conexiondb;

         this.viajeRegularDao = new ViajeRegularDAO(conexiondb);
         this.viajePrivadoDao = new ViajePrivadoDAO (conexiondb);
         this.rutaDao = new RutaDAO(conexiondb);
         this.boletaDao = new BoletoDAO(conexiondb);
        this.viajeDao = new ViajeDAO(conexiondb);
        this.busDao= new BusDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
        this.configuracionDao = new ConfiguracionDepreciacionDAO(conexiondb);
    }
            
    public boolean registrarSalida(int viajeId, double kilometraje,LocalTime horaSalidaReal){
        if (viajeId <= 0 || kilometraje < 0 || horaSalidaReal == null) {
            return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        if (con == null) {
             return false;
        }
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
            if (viaje.getHoraSalidaReal() != null) {
                con.rollback();
                return false;
            }
            if (viaje.getBus() == null || viaje.getChofer() == null) {
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
            
            viaje.setHoraSalidaReal(horaSalidaReal);
            viaje.setKilometrajeInicial(kilometraje);
            viaje.setEstado(true);
            
            bus.setKilometrajeActual(kilometraje);
            
            if (!viajeDao.registrarSalida(viaje)) {
                con.rollback();
                return false;
            }
            if (!busDao.actualizar(bus)) {
                 con.rollback();
                 return false;
            }
            
            con.commit();
            return true;
            
        } catch (SQLException |  RuntimeException ex) {
            rollback(con);
            ex.printStackTrace();
            return false;
        } finally {
           restaurarAutoCommit(con,autoCommitAnterior);
        }
    }
    
    public boolean registrarLlegada(int viajeId, double kilometraje, double gastoCombustible,LocalTime horaLlegadaReal){
        if (viajeId <= 0 || kilometraje < 0 || gastoCombustible < 0 || horaLlegadaReal == null) {
            return false;
        }
        
        Connection con = conexiondb.obtenerConeccion();
        if (con == null) {
             return false;
        }
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
             if (viaje.getHoraLlegadaReal() != null) {
                    con.rollback();
                    return false;
            }
             
             if (viaje.getBus() == null) {
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
             
            viaje.setKilometrajeFinal(kilometraje);
           double depreciacion = viaje.calcularDepreciacion( configuracion.getMontoPorKilometraje());
             
             viaje.setHoraLlegadaReal(horaLlegadaReal);
             viaje.setGastoCombustible(gastoCombustible);
             viaje.setMontoDepreciacion(depreciacion);
             
             // viaje termino
             viaje.setEstado(false);
             
             bus.setKilometrajeActual(kilometraje);
             
             if (! viajeDao.registrarLlegada(viaje)) {
                 con.rollback();
                 return false;
            }
             
             if (!busDao.actualizar(bus)) {
                con.rollback();
                return false;
            }
             
             con.commit();
             return true;
             
        } catch (SQLException | RuntimeException  e) {
            rollback(con);
            e.printStackTrace();
            return false;
        } finally {
            restaurarAutoCommit(con,autoCommitAnterior);
        }
    }
    
    private void rollback(Connection con){
        if (con != null) {
             try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void restaurarAutoCommit(Connection con,boolean autoCommitAnterior){
        if (con != null) {
             try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean programaViajeRegular(ViajeRegular viaje){
        if (viaje == null || viaje.getBus() == null || viaje.getChofer() == null || viaje.getRuta() == null || viaje.getFechaSalida() == null || viaje.getHoraSalidaProgramada() == null
            || viaje.getFechaLlegadaEstimada() == null || viaje.getHoraLlegadaEstimada() == null ){
            return false;
        }
        
        if (viaje.getFechaLlegadaEstimada().isBefore(viaje.getFechaSalida())) {
              return false;
        }
        
        if (viaje.getFechaLlegadaEstimada().isEqual(viaje.getFechaSalida()) && !viaje.getHoraLlegadaEstimada().isAfter(viaje.getHoraSalidaProgramada())) {
             return false;
        }
          Connection con = conexiondb.obtenerConeccion();
          if (con == null) {
             return false;
        }
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            Optional<Bus> resultadoBus = busDao.buscarPorId(viaje.getBus().getId());
            Optional<Chofer> resultadoChofer = choferDao.buscarPorId(viaje.getChofer().getId());
            Optional<Ruta> resultadoRuta = rutaDao.buscarPorId(viaje.getRuta().getId());
            
            if (resultadoBus.isEmpty() || resultadoChofer.isEmpty() || resultadoRuta.isEmpty()) {
                  con.rollback();
                  return false;
            }
            
            Bus bus = resultadoBus.get();
            Chofer chofer = resultadoChofer.get();
            Ruta ruta = resultadoRuta.get();
            if (!bus.isEstado() || !chofer.isEstado()) {
                  con.rollback();
                  return false;
            }
            
            if (!chofer.licenciaVigente(viaje.getFechaSalida())) {
                  con.rollback();
                  return false;
            }
            if (chofer.getSucursalId() == null || bus.getSucursalId()  != chofer.getSucursalId()) {
            con.rollback();
            return false;
        }

        if (ruta.getOrigen() == null || bus.getSucursalId()  != ruta.getOrigen().getId()) {
            con.rollback();
            return false;
        }
            viaje.setBus(bus);
            viaje.setChofer(chofer);
            viaje.setRuta(ruta);
            
            viaje.setEstado(false);
            
            int viajeId = viajeDao.insertarYObtenerId(viaje);
            
            if (viajeId <= 0) {
                 con.rollback();
                 return false;
            }
            
            boolean regularInsertado = viajeRegularDao.insertar(viajeId, ruta.getId());
            if (!regularInsertado) {
                  con.rollback();
                  return false;
            }
            
            viaje.setId(viajeId);
            
            con.commit();
            return true;

        } catch (SQLException | RuntimeException e) {
            rollback(con);
            e.printStackTrace();
            return false;
        } finally {
            restaurarAutoCommit(con,autoCommitAnterior);
        }
    }
    
    public boolean eliminarViaje(int viajeId){
        if (viajeId <= 0) {
             return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        if (con == null) {
             return false;
        } 
        
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
            
            if (viaje.getHoraSalidaReal() != null) {
                 con.rollback();
                 return false;
            }
            
            Optional<ViajeRegular> regular = viajeRegularDao.buscarPorViaje(viajeId);
            Optional<ViajePrivado> privado = viajePrivadoDao.buscarPorId(viajeId);
            if (regular.isPresent()) {
                 if (!boletaDao.buscarPorViaje(viajeId).isEmpty()) {
                     con.rollback();
                     return false;
                }
                 if (!viajeRegularDao.eliminarPorViaje(viajeId)) {
                     con.rollback();
                     return false;
                }
            } else if (privado.isPresent()){
                if (privado.get().isEstadoAlquiler()) {
                     con.rollback();
                     return false;
                }
                if (!viajePrivadoDao.eliminarPorViaje(viajeId)) {
                     con.rollback();
                     return false;
                }
            } else {
                con.rollback();
                return false;
            }
            
            if (!viajeDao.eliminar(viajeId)) {
                  con.rollback();
                  return false;
            }
            
            con.commit();
            return true;
            
        } catch (SQLException | RuntimeException e) {
             rollback(con);
             e.printStackTrace();
             return false;
        } finally {
            restaurarAutoCommit(con,autoCommitAnterior);
        }
    }
}
