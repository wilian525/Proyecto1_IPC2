/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.ConfiguracionDepreciacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author wilian
 */

public class ConfiguracionDepreciacionDAO {
    
   private ConexionDB conexiondb;
   
   public static final String CREAR_TABLA  = """
        CREATE TABLE IF NOT EXISTS configuracion_depreciacion (
            configuracion_id INT AUTO_INCREMENT,
            monto_por_kilometro DECIMAL(10,2) NOT NULL,
            CONSTRAINT pk_configuracion_depreciacion PRIMARY KEY (configuracion_id),
            CONSTRAINT chk_configuracion_monto CHECK (monto_por_kilometro >= 0)
        )
        """;
   
    private static final String INSERTAR = " INSERT INTO configuracion_depreciacion  (monto_por_kilometro)  VALUES (?) ";
    private static final String ACTUALIZAR = "UPDATE configuracion_depreciacion SET monto_por_kilometro = ? WHERE configuracion_id = ? ";
    private static final String OBTENER_ACTUAL = "SELECT * FROM configuracion_depreciacion ORDER BY configuracion_id DESC LIMIT 1";
    
    public ConfiguracionDepreciacionDAO(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
    }
    
    public void craerTabla(){
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

   public boolean insertar(ConfiguracionDepreciacion configuracion){
       Connection conexion = conexiondb.obtenerConeccion();
       PreparedStatement ps = null;
       
       try {
           ps = conexion.prepareStatement(INSERTAR);
           ps.setDouble(1,configuracion.getMontoPorKilometraje());
           return ps.executeUpdate() > 0;
           
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       } finally{
           cerrar(ps);
       }
   }
   
   public boolean actualizar( ConfiguracionDepreciacion configuracion) {

        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;

        try {

            ps = conexion.prepareStatement(ACTUALIZAR);

            ps.setDouble(1, configuracion.getMontoPorKilometraje());
            ps.setInt(2, configuracion.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
   
   public ConfiguracionDepreciacion obtenerActual(){
       Connection conexion = conexiondb.obtenerConeccion();
       PreparedStatement ps = null;
       ResultSet rs = null;
       
       try {
           ps = conexion.prepareStatement(OBTENER_ACTUAL);
           rs = ps.executeQuery();
           
           if (rs.next()) {
               return new ConfiguracionDepreciacion(rs.getInt("configuracion_id"), rs.getDouble("monto_por_kilometro"));
           }
       } catch (SQLException e) {
           e.printStackTrace();
       } finally{
             cerrar(rs);        
           cerrar(ps);
       }
       return null;
   }
   
   private void cerrar (Statement statement){
       if (statement != null) {
           try {
           statement.close();
          } catch (SQLException e) {
              e.printStackTrace();
            }
       }
   }
   
   private void cerrar (ResultSet resulSet){
       if (resulSet != null) {
           try {
               resulSet.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }
                                         

}
