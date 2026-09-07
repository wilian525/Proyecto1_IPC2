/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Mantenimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
public class MantenimientoDAO {
    
    private ConexionDB conexiondb;
    
  private static final String CREAR_TABLA = """ 
                                                   CREATE TABLE IF NOT EXISTS mantenimiento(
                                                    mantenimiento_id INT AUTO_INCREMENT,
                                                    bus_id INT NOT NULL,
                                                    fecha DATE NOT NULL,
                                                    monto_mano_obra DECIMAL(10,2) NOT NULL,
                                                    monto_repuesto DECIMAL(10,2) NOT NULL,
                                                    CONSTRAINT pk_mantenimiento PRIMARY KEY (mantenimiento_id),
                                                    CONSTRAINT fk_mantenimiento_bus FOREIGN KEY (bus_id) REFERENCES bus(bus_id),
                                                    CONSTRAINT fk_mantenimiento_mano_obra CHECK (monto_mano_obra >= 0 ),
                                                    CONSTRAINT chk_mantenimiento_repuestos CHECK (monto_repuesto >= 0)
                                               )
                                              """ ;
    
    private static final String INSERTAR = """
        INSERT INTO mantenimiento
        (bus_id, fecha, monto_mano_obra, monto_repuestos) VALUES (?, ?, ?, ?)
        """;
    
     private static final String LISTAR_POR_BUS = """
        SELECT * FROM mantenimiento WHERE bus_id = ? ORDER BY fecha DESC
        """;
     
     public MantenimientoDAO(ConexionDB conexiondb){
         this.conexiondb = conexiondb;
     }
     
     public void crearTabla(){
         Connection conexion = conexiondb.obtenerConeccion();
         Statement statement = null;
         
         try {
             statement = conexion.createStatement();
             statement.execute(CREAR_TABLA);
         } catch (SQLException e) {
             e.printStackTrace();
         } finally{
             cerrar(statement);
         }
     }
     
     public boolean insertar(Mantenimiento mantenimiento){
         Connection conexion = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         
         try {
             ps = conexion.prepareStatement(INSERTAR);
             ps.setInt(1, mantenimiento.getBusId());
             ps.setDate(2, java.sql.Date.valueOf(mantenimiento.getFecha()));
            ps.setDouble(3, mantenimiento.getMontoManoObra());
            ps.setDouble(4, mantenimiento.getMontoRepuesto());
            
            return ps.executeUpdate() > 0;
            
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         } finally {
             cerrar(ps);
         }
     }
     
     public Collection<Mantenimiento> listarPorBus(int busId){
         Collection<Mantenimiento> mantenimientos = new ArrayList<>();
         Connection conexion = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = conexion.prepareStatement(LISTAR_POR_BUS);
             ps.setInt(1, busId);
             
             rs = ps.executeQuery();
             
             while(rs.next()){
                 Mantenimiento mantenimiento = new Mantenimiento();
                 mantenimiento.setId(rs.getInt("mantenimiento_id"));
                 mantenimiento.setBusId(rs.getInt("bus_id"));
                 mantenimiento.setFecha(rs.getDate("fecha").toLocalDate());
                 mantenimiento.setMontoManoObra(rs.getDouble("monto_mano_obra"));
                 mantenimiento.setMontoRepuesto(rs.getDouble("monto_repuestos"));
                 mantenimientos.add(mantenimiento);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally{
             cerrar(rs);
             cerrar(ps);
         }
         return mantenimientos;
     }
     
     private void cerrar(Statement statement){
         if (statement != null) {
             try {
                 statement.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
     
     private void cerrar(ResultSet resultSet){
         if (resultSet != null) {
             try {
                 resultSet.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
}
