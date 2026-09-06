/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ViajePrivadoDAO {
    
    private ConexionDB conexiondb;
    
    public static final String CREAR_TABLA = """
         CREATE TABLE IF NOT EXISTS viaje_privado (
                    viaje_id INT,
                    usuario_id INT NOT NULL,
                     origen VARCHAR(200) NOT NULL,
                     destino VARCHAR(200) NOT NULL,
                    fecha_retorno DATE,
                    numero_pasajeros INT NOT NULL,
                    precio_estimado DECIMAL(10,2) NOT NULL,
                    precio_confirmado DECIMAL(10,2),
                     estado_alquiler BOOLEAN NOT NULL DEFAULT FALSE,
                   fecha_pago DATE,
                                             
                   CONSTRAINT pk_viaje_privado PRIMARY KEY (viaje_id),
                                             
                   CONSTRAINT fk_viaje_privado_viaje FOREIGN KEY (viaje_id)  REFERENCES viaje(viaje_id),
                                             
                    CONSTRAINT fk_viaje_privado_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id),
                                             
                    CONSTRAINT chk_viaje_privado_pasajeros CHECK (numero_pasajeros > 0),
                                             
                     CONSTRAINT chk_viaje_privado_estimado CHECK (precio_estimado >= 0),
                                             
                     CONSTRAINT chk_viaje_privado_confirmado CHECK ( precio_confirmado IS NULL OR precio_confirmado >= 0 )
                                                         )                                    
                                             """;
    public static final String INSERTAR = """
                                          INSERT INTO viaje_privado (viaje_id, usuario_id, origen, destino, fecha_retorno, numero_pasajeros, precio_estimado, precio_confirmado, estado_alquiler , fecha_pago)
                                          VALUES (?,?,?,?,?,?,?, NULL, FALSE, NULL)
                                          """;
    public static final String BUSCAR_POR_ID = "SELECT * FROM viaje_privado WHERE viaje_id = ?";
    public static final String BUSCAR_POR_ID_USUARIO = "SELECT * FROM viaje_privado WHERE viaje_id = ? AND usuario_id = ? ";
    public static final String CONFIRMAR_ALQUILER = "UPDATE viaje_privado SET precio_confirmado = ? WHERE viaje_id = ? AND estado_alquiler = FALSE" ;
    public static final String REGISTRAR_PAGO = "UPDATE viaje_privado SET estado_alquiler = TRUE, fecha_pago = ? WHERE viaje_id = ? AND usuario_id = ? AND precio_confirmar IS NOT NULL AND estado_alquiler = FALSE";
    
    public ViajePrivadoDAO(ConexionDB conexiondb){
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
    
    public boolean insertar(int viajeId,int usuarioId,ViajePrivado viajePrivado){
          if (viajePrivado == null) {
                return false;
        }
          Connection con = conexiondb.obtenerConeccion();
          PreparedStatement ps = null;
          
          try {
            ps = con.prepareStatement(INSERTAR);
            ps.setInt(1, viajeId);
            ps.setInt(2, usuarioId);
            ps.setString(3, viajePrivado.getOrigen());
            ps.setString(4, viajePrivado.getDestino());
            
              if (viajePrivado.getFechaRetorno() != null) {
                   ps.setDate(5, Date.valueOf(viajePrivado.getFechaRetorno()));
              } else {
                  ps.setNull(5, Types.DATE);
              }
              ps.setInt( 6, viajePrivado.getNumeroPasajeros());
             ps.setDouble(7,viajePrivado.getPrecioEstimado());

            return ps.executeUpdate() > 0;
      
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
              cerrar(ps);
          }
    }
    
    public Optional<ViajePrivado> buscarPorId(int viajeId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ViajePrivado viajePrivado = null;
        
        try {
            ps = con.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, viajeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                viajePrivado = construirViajePrivado(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(viajePrivado);
    }
    
    public Optional<ViajePrivado> buscarPorIdYUsuario(int viajeId, int usuarioId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ViajePrivado viajePrivado = null;
        
        try {
            ps = con.prepareStatement(BUSCAR_POR_ID_USUARIO);
            ps.setInt(1, viajeId);
            ps.setInt(2, usuarioId);
            
            if (rs.next()) {
                 viajePrivado = construirViajePrivado(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(viajePrivado);
    }
    
    public boolean confirmarAlquiler(int viajeId, double precio){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(CONFIRMAR_ALQUILER);
            ps.setDouble(1, precio);
            ps.setInt(2, viajeId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public boolean gistrarPago(int viajeId,int usuarioId, LocalDate fechaPago){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(REGISTRAR_PAGO);
            ps.setDate(1, Date.valueOf(fechaPago));
            ps.setInt(2, viajeId);
            ps.setInt(3, usuarioId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    private ViajePrivado construirViajePrivado(ResultSet rs) throws SQLException {
        ViajePrivado viajePrivado = new ViajePrivado();
        viajePrivado.setId(rs.getInt("viaje_id"));
         viajePrivado.setOrigen(rs.getString("origen"));
        viajePrivado.setDestino( rs.getString("destino"));
        Date fechaRetorno = rs.getDate("fecha_retorno");
        
        if (fechaRetorno != null) {
             viajePrivado.setFechaRetorno(fechaRetorno.toLocalDate());
        }
        viajePrivado.setNumeroPasajeros(rs.getInt("numero_pasajeros"));
        viajePrivado.setPrecioEstimado(rs.getDouble("precio_estimado"));
        double precioConfirmado = rs.getDouble("precio_confirmado");
        
        if (!rs.wasNull()) {
             viajePrivado.setPrecioConfirmado(precioConfirmado);
        }
        
        viajePrivado.setEstadoAlquiler(rs.getBoolean("estado_alquiler"));
        Date fechaPago = rs.getDate("fecha_pago");
        
        if (fechaPago != null)  {
              viajePrivado.setFechaPago(fechaPago.toLocalDate());
        }
        return viajePrivado;
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
