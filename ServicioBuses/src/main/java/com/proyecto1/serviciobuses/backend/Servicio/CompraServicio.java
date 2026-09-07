/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Boleto;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import com.proyecto1.serviciobuses.backend.dao.BoletoDAO;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import com.proyecto1.serviciobuses.backend.dao.RutaDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeRegularDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
/**
 *
 * @author wilian
 */
public class CompraServicio {
    
   private ConexionDB conexiondb;
   
   private ViajeDAO viajeDao;
   private RutaDAO rutaDao;
   private BoletoDAO boletoDao;
   private CarteraDAO carteraDao;
   private ViajeRegularDAO viajeRegularDAO;
   
   public CompraServicio(ConexionDB conexiondb){
       this.conexiondb = conexiondb;
       
       this.viajeDao = new ViajeDAO(conexiondb);
       this.viajeRegularDAO = new ViajeRegularDAO(conexiondb);
       this.rutaDao = new RutaDAO(conexiondb);
       this.boletoDao = new BoletoDAO(conexiondb);
       this.carteraDao = new CarteraDAO(conexiondb);
   }
   
   public boolean compraBoletos(int usuarioId, int viajeId, Collection<Integer> asientos, LocalDate fechaPago){
       if (usuarioId <= 0 || viajeId <= 0 ||asientos == null ||  asientos.isEmpty() || fechaPago == null) {
                return false;
       }
       
       Collection<Integer> asientosCompra = new ArrayList<>();
        
       for(Integer asiento : asientos){
                if (asiento == null || asiento <= 0 || asientosCompra.contains(asiento)) {
                    return false;
           }
                asientosCompra.add(asiento);
       }
       
       Connection con = conexiondb.obtenerConeccion();
       if (con == null) {
           return false;
       }
       boolean autoCommitAnterior = true;
       
       try {
           autoCommitAnterior  = con.getAutoCommit();
           con.setAutoCommit(false);
           
           //verifica viaje exista
           Optional<ViajeRegular> resultadoRegular = viajeRegularDAO.buscarPorViaje(viajeId);
           if (resultadoRegular.isEmpty() || resultadoRegular.get().getRuta() == null) {
                    con.rollback();
                    return false;
           }
             // obtener ruta y valida si existe viaje regular
                    Optional<Ruta> resultadoRuta = rutaDao.buscarPorId(resultadoRegular.get().getRuta().getId());
                    
                    if (resultadoRuta.isEmpty()) {
                        con.rollback();
                        return false;
               } 
                         
                    Ruta ruta = resultadoRuta.get();
                    double precioBoleto = ruta.getPrecioBoleto();
                    double totalCompra = precioBoleto * asientosCompra.size();
                    
                    for(Integer asiento : asientosCompra){
                        Boleto boleto = new Boleto();
                        
                        boleto.setViajeId(viajeId);
                        boleto.setUsuarioId(usuarioId);
                        boleto.setAsiento(asiento);
                        boleto.setFechaPago(fechaPago);
                        boleto.setPrecio(precioBoleto);
                        
                        boolean insertado = boletoDao.insertar(boleto);
                        
                        if (!insertado) {
                             con.rollback();
                             return false;
                        }
                    }
                    
                    // descontar el precio total de la cartera
                    boolean pagoRealizado = carteraDao.descontarSaldo(usuarioId,totalCompra);
                    if (!pagoRealizado) {
                            con.rollback();
                            return false;
           }
                    
                    //  todos boletos fueron aceptado y saldo descontado se acepta 
                    con.commit();
                    return true;
           
       } catch (SQLException | RuntimeException ex) {
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
    
}
