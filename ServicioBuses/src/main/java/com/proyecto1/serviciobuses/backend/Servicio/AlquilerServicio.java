/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
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
    private CarteraDAO carteraDao;
    private BusDAO busDao;
    private ChoferDAO choferDao;
    
    public AlquilerServicio(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
        this.viajeDao = new ViajeDAO(conexiondb);
        this.carteraDao = new CarteraDAO(conexiondb);
        this.busDao = new BusDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
    }
    
    public boolean solicitarAlquiler(int usuarioId, ViajePrivado alquiler){
        if (usuarioId  <= 0 || alquiler == null || alquiler.getOrigen() == null
                || alquiler.getOrigen().isBlank()
                || alquiler.getDestino() == null
                || alquiler.getDestino().isBlank()
                || alquiler.getFechaSalida() == null
                || alquiler.getHoraSalidaProgramada() == null
                || alquiler.getFechaLlegadaEstimada() == null
                || alquiler.getHoraLlegadaEstimada() == null
                || alquiler.getNumeroPasajeros() <= 0
                || alquiler.getPrecioEstimado() <= 0)) {
            return false;
        }
         if (alquiler.getFechaRetorno() != null && alquiler.getFechaRetorno().isBefore(alquiler.getFechaSalida())) {
            return false;
        }
         Connection con = conexiondb.obtenerConeccion();
         boolean autoCommitAnterior = true;
         
         try {
             autoCommitAnterior = con.getAutoCommit();
             con.setAutoCommit(autoCommitAnterior);
             
             // en la compra de boleto todavia no se le asigna bus ni chofer
             alquiler.setBus(null);
             alquiler.setChofer(null);
             alquiler.setEstado(false);
             alquiler.setPrecioConfirmado(0);
             alquiler.setEstadoAlquiler(false);
             alquiler.setFechaPago(null);
             
             // se construye primero el viaje general
             int viajeId = viajeDao.insertarYObtenerId(alquiler);
             
             if (viajeId <= 0 ) {
                    con.rollback();
                    return false;
             }
             
             alquiler.setId(viajeId);
             
             // inserta datos del alquiler
             boolean privadoInsertado =  viajeDao.insertarPrivado(viajeId,usuarioId,alquiler);
             
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
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(autoCommitAnterior);
            
            Optional<ViajePrivado> resultado = viajeDao.buscarPrivadoPorId(viajeId);
            
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
            
            boolean confirmado = viajeDao.confirmarAlquiler(viajeId , precio);
            
            if (!confirmado) {
                 con.rollback();
                 return false;
            }
            
            con.commit();
            return false;
            
        } catch (SQLException | RuntimeException e) {
            roollback(con);
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
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            // busca alquiler y verifica que sea el usuaroi
           Optional<ViajePrivado> resultado = viajeDao .buscarPrivadoPorIdYUsuario(viajeId,usuarioId);
           
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
              boolean pagoRegistrado = viajeDao.registrarPagoAlquiler(viajeId,usuarioId,fechaPago);
              
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
             
             Optional<ViajePrivado> resultadoAlquiler = viajeDao.buscarPrivadoPorId(viajeId);
             Optional<Viaje> resultadoViaje = viajeDao.buscarPorId(viajeId);
        } catch (Exception e) {
        }
    }
}
