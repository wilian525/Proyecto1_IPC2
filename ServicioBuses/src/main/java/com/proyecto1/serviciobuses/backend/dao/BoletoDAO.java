/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Boleto;
import java.sql.Connection;
import java.sql.Date;
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
public class BoletoDAO {
    
    private ConexionDB conexiondb;
    
    private static final String CREAR_TABLA = """
          CREATE TABLE IF NOT EXISTS boleto (
                    boleto_id INT AUTO_INCREMENT,
                    viaje_id INT NOT NULL,
                     usuario_id INT NOT NULL,
                    asiento INT NOT NULL,
                  fecha_pago DATE NOT NULL,
                    precio DECIMAL(10,2) NOT NULL,
                                                                                              
                  CONSTRAINT pk_boleto  PRIMARY KEY (boleto_id),
                   CONSTRAINT fk_boleto_viaje FOREIGN KEY (viaje_id) REFERENCES viaje(viaje_id),
                     CONSTRAINT fk_boleto_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id),
                    CONSTRAINT ak_boleto_viaje_asiento UNIQUE (viaje_id, asiento),
                      CONSTRAINT chk_boleto_asiento CHECK (asiento > 0),
                    CONSTRAINT chk_boleto_precio CHECK (precio >= 0)             
                                                )                     
                 """;
    private static final String INSERTAR = "INSERT INTO boleto (viaje_id, usuario_id, asiento,fecha_pago, precio) VALUES (?,?,?,?,?) ";
    private static final String BUSCAR_POR_VIAJE = "SELECT * FROM boleto WHERE viaje_id = ? ORDER BY asiento";
    private static final String BUSCAR_POR_USUARIO = "SELECT * FROM boleto WHERE usuario_id = ? ORDER BY fecha_pago DESC , boleto_id DESC";
    private static final String VALIDAR_ASIENTO = "SELECT b.capacidad, v.tipo FROM viaje v INNER JOIN bus b ON v.bus_id = b.bus_id WHERE v.viaje_id = ? ";
    
    public BoletoDAO (ConexionDB conexiondb){
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
     
     public boolean insertar(Boleto boleto){
         if (boleto == null || boleto.getFechaPago() == null) {
             return false;
         }
         if (!asientoValido(boleto)) {
             return false;
         }
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         
         try {
             ps = con.prepareStatement(INSERTAR);
            ps.setInt(1, boleto.getViajeId());
            ps.setInt(2, boleto.getUsuarioId());
            ps.setInt(3, boleto.getAsiento());

            ps.setDate(4,Date.valueOf(boleto.getFechaPago()));
            ps.setDouble( 5, boleto.getPrecio() );

            return ps.executeUpdate() > 0;
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         } finally{
             cerrar(ps);
         }
     }
     
     public Collection<Boleto> buscarPorViaje(int viajeId){
         Collection<Boleto> boletos = new ArrayList<>();
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(BUSCAR_POR_VIAJE);
             ps.setInt(1, viajeId);
             rs = ps.executeQuery();
             
             while(rs.next()){
                 boletos.add(construirBoleto(rs));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally{
             cerrar(rs);
             cerrar(ps);
         }
         return boletos;
     }
     
     public Collection<Boleto> buscarPorUsuario(int usuarioId){
         Collection<Boleto> boletos = new ArrayList<>();
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(BUSCAR_POR_USUARIO);
             ps.setInt(1, usuarioId);
             rs = ps.executeQuery();
             
             while(rs.next()){
                 boletos.add(construirBoleto(rs));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally{
             cerrar(rs);
             cerrar(ps);
         }
         return boletos;
     }
     
     private boolean asientoValido(Boleto boleto){
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(VALIDAR_ASIENTO);
            ps.setInt(1, boleto.getViajeId());
            rs = ps.executeQuery();

            if (rs.next()) {
                int capacidad = rs.getInt("capacidad");
                String tipo = rs.getString("tipo");

                return "REGULAR".equals(tipo) && boleto.getAsiento() > 0 && boleto.getAsiento() <= capacidad;
            }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             cerrar(rs);
             cerrar(ps);
         }
         return false;
     }
     
     private Boleto construirBoleto(ResultSet rs) throws SQLException{
            Boleto boleto = new Boleto();
            
            boleto.setId(rs.getInt("boleto_id"));
            boleto.setViajeId(rs.getInt("viaje_id") );
           boleto.setUsuarioId(rs.getInt("usuario_id") );
           boleto.setAsiento( rs.getInt("asiento"));
           boleto.setFechaPago( rs.getDate("fecha_pago").toLocalDate() );
           boleto.setPrecio( rs.getDouble("precio"));

        return boleto;
     }
     
     private void cerrar(Statement statement){
         if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
     }
     
     private void cerrar(ResultSet resultSet){
             if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
     }
}
