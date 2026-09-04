/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.CarteraDigital;
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
public class CarteraDAO {
    
    private ConexionDB conexiondb;
    
    public static final String CREAR_TABLA = """
             CREATE TABLE IF NOT EXISTS cartera_digital (
                           cartera_id INT AUTO_INCREMENT,
                           usuario_id INT NOT NULL,
                           saldo DECIMAL(10,2) NOT NULL DEFAULT 0,
                                             
                           CONSTRAINT pk_cartera  PRIMARY KEY (cartera_id),
                                             
                            CONSTRAINT fk_cartera_usuario FOREIGN KEY (usuario_id)
                               REFERENCES usuario(usuario_id),
                                             
                              CONSTRAINT ak_cartera_usuario UNIQUE (usuario_id),
                                             
                              CONSTRAINT chk_cartera_saldo CHECK (saldo >= 0)
                                )                                
                              """;
    public static final String INSERTAR = " INSERT INTO cartera_digital (usuario_id, saldo) VALUES (?, ? ) ";
    public static final String BUSCAR_POR_USUARIO = "SELECT * FROM cartera_digital WHERE usuario_id = ? ";
    public static final String ACTUALIZAR_SALDO = "UPDATE cartera_digital SET saldo = ? WHERE usuario_id = ? ";
    public static final String DESCONTAR_SALDO = "UPDATE cartera_digital SET saldo = saldo - ? WHERE usuario_id = ? AND saldo >= ? ";
    
    public CarteraDAO(ConexionDB conexiondb){
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
        } finally{
            cerrar(statement);
        }
    }
    
    public boolean insertar(int usuarioId, CarteraDigital cartera){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(INSERTAR);
            ps.setInt(1, usuarioId);
            ps.setDouble(2, cartera.getSaldo());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public boolean descontarSaldo(int usuarioId, double monto){
        if (usuarioId <= 0 || monto < 0) {
                return false;
        }
        
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null; 
        
        try {
            ps = con.prepareStatement(DESCONTAR_SALDO);
            ps.setDouble(1, monto);
            ps.setInt(2, usuarioId);
            ps.setDouble(3, monto);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public Optional<CarteraDigital> buscarPorUsuario(int usuarioId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        CarteraDigital cartera = null;
        
        try {
            ps = con.prepareStatement(BUSCAR_POR_USUARIO);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                cartera = new CarteraDigital(rs.getInt("cartera_id"), rs.getDouble("saldo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(cartera);
    }
    
    public boolean actualizarSaldo(int usuarioId, double saldo){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
          
        try {
            ps = con.prepareStatement(ACTUALIZAR_SALDO);
            ps.setDouble(1, saldo);
            ps.setInt(2, usuarioId);
            
            return ps.executeUpdate() > 0 ;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    private void cerrar(Statement statement){
        if (statement != null)  {
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
