/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajePrivadoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class AlquilerServicio {
    
    private ConexionDB conexiondb;
    
    private ViajeDAO viajeDao;
    private ViajePrivadoDAO viajePrivadoDao;
    private CarteraDAO carteraDao;
    private BusDAO busDao;
    private ChoferDAO choferDao;
    
    public AlquilerServicio(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
        this.viajeDao = new ViajeDAO(conexiondb);
        this.viajePrivadoDao = new ViajePrivadoDAO(conexiondb);
        this.carteraDao = new CarteraDAO(conexiondb);
        this.busDao = new BusDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
    }
    
    public boolean solicitarAlquiler(int usuarioId, ViajePrivado alquiler){
        if (usuarioId  <= 0 || alquiler == null ){
            return false;
        }
         if (alquiler.getOrigen() == null || alquiler.getOrigen().isBlank() || alquiler.getDestino() == null|| alquiler.getDestino().isBlank() ) {
                     return false;
          }
                
          if (alquiler.getFechaSalida() == null || alquiler.getHoraSalidaProgramada() == null || alquiler.getFechaLlegadaEstimada() == null || alquiler.getHoraLlegadaEstimada() == null) {
                  return false;      
        }
                     
           if(alquiler.getNumeroPasajeros() <= 0) {
               return false;
           }
 
         if (alquiler.getFechaRetorno() != null && alquiler.getFechaRetorno().isBefore(alquiler.getFechaSalida())) {
            return false;
        }
         
         double precioEstimado = alquiler.calcularCosto();
         if (precioEstimado < 0) {
             return false;
        }
         alquiler.setPrecioEstimado(precioEstimado);
         
              // en la compra de boleto todavia no se le asigna bus ni chofer
             alquiler.setBus(null);
             alquiler.setChofer(null);
             alquiler.setEstado(false);
             alquiler.setPrecioConfirmado(0);
             alquiler.setEstadoAlquiler(false);
             alquiler.setFechaPago(null);
             
         Connection con = conexiondb.obtenerConeccion();
         if (con  == null) {
             return false;
        }
         
         boolean autoCommitAnterior = true;
         
         try {
             autoCommitAnterior = con.getAutoCommit();
             con.setAutoCommit(false);
             
             // se construye primero el viaje general
             int viajeId = viajeDao.insertarYObtenerId(alquiler);
             
             if (viajeId <= 0 ) {
                    con.rollback();
                    return false;
             }
             
             alquiler.setId(viajeId);
             
             // inserta datos del alquiler
             boolean privadoInsertado =  viajePrivadoDao.insertar(viajeId,usuarioId,alquiler);
             
             if (!privadoInsertado) {
                   con.rollback();
                   return false;
             }
             
             // solo si las dos insercciones funcionan 
             con.commit();
             return true;
            
        } catch (SQLException |  RuntimeException e) {
            rollback(con);
            e.printStackTrace();
            return false;
        } finally {
             restaurarAutoCommit(con,autoCommitAnterior);
         }
    }
    
    public boolean confirmarAlquiler(int viajeId,double precio){
        if (viajeId <= 0 || precio <= 0) {
              return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        if (con  == null) {
             return false;
        }
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            Optional<ViajePrivado> resultado = viajePrivadoDao.buscarPorId(viajeId);
            
            if (resultado.isEmpty()) {
                  con.rollback();
                  return false;
            }
            
            ViajePrivado alquiler = resultado.get();
            
            // si fue pagado el precio no puede cambiar
            if (alquiler.isEstadoAlquiler()) {
                 con.rollback();
                 return false;
            }
            
            boolean confirmado = viajePrivadoDao.confirmarAlquiler(viajeId , precio);
            
            if (!confirmado) {
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
    
    public boolean pagarAlquiler(int viajeId, int usuarioId, LocalDate fechaPago){
        if (viajeId <= 0 || usuarioId <= 0 || fechaPago == null) {
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
            
            // busca alquiler y verifica que sea el usuaroi
           Optional<ViajePrivado> resultado = viajePrivadoDao .buscarPorIdYUsuario(viajeId,usuarioId);
           
            if (resultado.isEmpty()) {
                 con.rollback();
                 return false;
            }
            ViajePrivado alquiler = resultado.get();
            
            // verifica que no tenga pago dos veces 
            if (alquiler.isEstadoAlquiler()) {
                con.rollback();
                return false;
            }
            
            if (alquiler.getPrecioConfirmado() <= 0) {
                    con.rollback();
                    return false;
            }
            
            // descuenta de cartera
             boolean saldoDescontado = carteraDao.descontarSaldo(usuarioId, alquiler.getPrecioConfirmado());
             if (!saldoDescontado) {
                   con.rollback();
                   return false;
            }
              // verifica alquiler pagado
              boolean pagoRegistrado = viajePrivadoDao.registrarPago(viajeId,usuarioId,fechaPago);
              
              if (!pagoRegistrado) {
                   con.rollback();
                   return false;
            }
              
              con.commit();
              return true;
              
        } catch (SQLException | RuntimeException e) {
            rollback(con);
            e.printStackTrace();
            return false;
        }  finally {
             restaurarAutoCommit(con,autoCommitAnterior);
            }
    }
    
    public boolean asignarBusYChofer(int viajeId, int busId,int choferId){
         if (viajeId <= 0 || busId <= 0 || choferId  <= 0) {
                return false;
        }
        
         Connection  con = conexiondb.obtenerConeccion();
         boolean autoCommitAnterior = true ;
         
         try {
             autoCommitAnterior = con.getAutoCommit();
             con.setAutoCommit(false);
             
             Optional<ViajePrivado> resultadoAlquiler = viajePrivadoDao.buscarPorId(viajeId);
             Optional<Viaje> resultadoViaje = viajeDao.buscarPorId(viajeId);
             Optional<Bus> resultadoBus = busDao.buscarPorId(busId);
             Optional<Chofer> resutadoChofer = choferDao.buscarPorId(choferId);
             
             if (resultadoAlquiler.isEmpty() || resultadoViaje.isEmpty() || resultadoBus.isEmpty() || resutadoChofer.isEmpty()) {
                 con.rollback();
                 return false;
             }
             
             ViajePrivado alquiler = resultadoAlquiler.get();
             Viaje viaje = resultadoViaje.get();
             Bus bus = resultadoBus.get();
             Chofer chofer = resutadoChofer.get();
             
             if (!bus.isEstado() || !chofer.isEstado()) {
                  con.rollback();
                  return false;
             }
             
             // bus debe terner espacio
             if (bus.getCapacidad() < alquiler.getNumeroPasajeros()) {
                  con.rollback();
                  return false;
             }
             // primero debe estar pagado
             if (!alquiler.isEstadoAlquiler()) {
                  con.rollback();
                  return false;
             }
             
             // bus y chofer deben pertenecer misma socursal
             if (chofer.getSucursalId() == null || bus.getSucursalId() != chofer.getSucursalId()) {
                    con.rollback();
                    return false;
             }
             
             if (!chofer.licenciaVigente(viaje.getFechaSalida())) {
                   con.rollback();
                   return false;
             }
             
             boolean asignado = viajeDao.asignarRecursos(viajeId,busId,choferId);
             
             if (!asignado) {
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
    
    private void rollback(Connection con){
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void restaurarAutoCommit(Connection con , boolean autoCommitAnterior){
        try {
            con.setAutoCommit(autoCommitAnterior);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
