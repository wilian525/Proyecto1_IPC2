/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Ruta;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
/**
 *
 * @author wilian
 */
public class RutaDAO {
    
    private ConexionDB conexiondb;
    
    private static final String CREAR_TABLA = """
           CREATE TABLE IF NOT EXISTS ruta (
                       ruta_id INT AUTO_INCREMENT,
                       origen_id INT NOT NULL,
                       destino_id INT NOT NULL,
                       distancia_km DECIMAL(10,2) NOT NULL,
                       precio_boleto DECIMAL(10,2) NOT NULL,
           
                       CONSTRAINT pk_ruta PRIMARY KEY (ruta_id),         
                       CONSTRAINT fk_ruta_origen FOREIGN KEY (origen_id)  REFERENCES sucursal(sucursal_id),      
                       CONSTRAINT fk_ruta_destinoFOREIGN KEY (destino_id) REFERENCES sucursal(sucursal_id), 
                       CONSTRAINT chk_ruta_distancia  CHECK (distancia_km > 0),         
                       CONSTRAINT chk_ruta_precio CHECK (precio_boleto >= 0),       
                       CONSTRAINT chk_ruta_origen_destino CHECK (origen_id <> destino_id)
                                                      )
                                              """;
   private static final String INSERTAR = "INSERT INTO ruta (origen_id, destino_id, distancia_km, precio_boleto) VALUES (?,?,?,?) ";
    private static final String ACTUALIZAR = "UPDATE ruta SET origen_id = ? , destino = ? , distancia_km = ? , precio_boleto = ? WHERE ruta_id = ? ";
    private static final String ELIMINAR = "DELETE FROM ruta WHERE ruta_id = ? AND NOT EXISTS (SELECT 1 FROM viaje_regular WHERE viaje_regular.ruta_id = ruta.ruta_id) ";
    private static final String BUSCAR_POR_ID = "SELECT * FROM ruta WHERE ruta_id = ? ";
    private static final String LISTAR = "SELECT * FROM ruta ORDER BY ruta_id";
    private static final String BUSCAR_POR_VIAJE_REGULAR = "SELECT r.* FROM ruta r INNER JOIN viaje_regular vr ON r.ruta_id = vr.ruta_id WHERE vr.viaje_id = ?";
    
    public RutaDAO(ConexionDB conexiondb){
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
    
    public boolean insertar(Ruta ruta){
        if (ruta  == null || ruta.getOrigen() == null || ruta.getDestino() == null) {
                return false;
        }
        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        
        try {
            ps = conexion.prepareStatement(INSERTAR);

            ps.setInt(1, ruta.getOrigen().getId());
            ps.setInt( 2, ruta.getDestino().getId() );
            ps.setDouble(3, ruta.getDistanciaKilometraje());
            ps.setDouble(4, ruta.getPrecioBoleto() );

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public boolean eliminar(int id){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ELIMINAR);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            cerrar(ps);
        }
    }
    
    public Optional<Ruta> buscarPorId(int id){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps =  null;
        ResultSet rs = null;
        
        Ruta ruta = null;
        
        try {
             ps = con.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                ruta = construirRuta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(ruta);
    }
    
    public Optional<Ruta> buscarPorViajeRegular(int viajeId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        Ruta ruta = null;
        
        try {
            ps =  con.prepareStatement(BUSCAR_POR_VIAJE_REGULAR);
            ps.setInt(1, viajeId);
            
            if (rs.next()) {
                ruta = construirRuta(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(ruta);
    }
    
    public Collection<Ruta> listar(){
        Collection<Ruta> ruta = new ArrayList<>();
        
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(LISTAR);
            rs = ps.executeQuery();
            
            while(rs.next()){
                ruta.add(construirRuta(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return ruta;
    }
    
    private Ruta construirRuta(ResultSet rs) throws SQLException{
        Sucursal origen = new Sucursal();
        origen.setId(rs.getInt("origen_id"));

        Sucursal destino = new Sucursal();
        destino.setId( rs.getInt("destino_id"));

        Ruta ruta = new Ruta();

        ruta.setId(rs.getInt("ruta_id"));
        ruta.setOrigen(origen);
        ruta.setDestino(destino);

        ruta.setDistanciaKilometraje(rs.getDouble("distancia_km"));
        ruta.setPrecioBoleto(rs.getDouble("precio_boleto"));

        return ruta;
    }
    
     private void cerrar(Statement statement) {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cerrar(ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}


