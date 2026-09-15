/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ViajeRegularDAO {
    
    private ConexionDB conexiondb;
    
      private static final String CREAR_TABLA = """
            CREATE TABLE IF NOT EXISTS viaje_regular (
                viaje_id INT,
                ruta_id INT NOT NULL,
                CONSTRAINT pk_viaje_regular PRIMARY KEY (viaje_id),
                CONSTRAINT fk_viaje_regular_viaje  FOREIGN KEY (viaje_id) REFERENCES viaje(viaje_id),
                CONSTRAINT fk_viaje_regular_ruta FOREIGN KEY (ruta_id) REFERENCES ruta(ruta_id)
            )
            """;
    private static final String INSERTAR = "INSERT INTO viaje_regular (viaje_id, ruta_id) VALUES (?, ?)";
    private static final String BUSCAR_POR_VIAJE = "SELECT * FROM viaje_regular WHERE viaje_id = ? ";
    private static final String ELIMINAR_POR_VIAJE = "DELETE FROM viaje_regular  WHERE viaje_id = ?";
           
    public ViajeRegularDAO(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
    }
    
    public void crearTabla(){
        Connection con = conexiondb.obtenerConeccion();
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.execute(CREAR_TABLA);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(statement);
        }
    }
    
    public boolean insertar(int viajeId,int rutaId){
        if (viajeId <= 0 || rutaId <= 0) {
             return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(INSERTAR);
            ps.setInt(1, viajeId);
            ps.setInt(2, rutaId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public Optional<ViajeRegular> buscarPorViaje(int viajeId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ViajeRegular viajeRegular = null;
        
        try {
            ps = con.prepareStatement(BUSCAR_POR_VIAJE);
            ps.setInt(1, viajeId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                 viajeRegular = construirViajeRegular(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(viajeRegular);
    }
    
    public boolean eliminarPorViaje(int viajeId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ELIMINAR_POR_VIAJE);
            ps.setInt(1, viajeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    private ViajeRegular construirViajeRegular(ResultSet rs) throws SQLException{
        ViajeRegular viajeRegular = new ViajeRegular();
        viajeRegular.setId(rs.getInt("viaje_id"));
        Ruta ruta = new Ruta();
        ruta.setId(rs.getInt("ruta_id"));
        
        viajeRegular.setRuta(ruta);
        return viajeRegular;
    }
    
     private void cerrar(Statement statement) {

        if (statement != null) {

            try {

                statement.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    private void cerrar(ResultSet resultSet) {

        if (resultSet != null) {

            try {

                resultSet.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
}
