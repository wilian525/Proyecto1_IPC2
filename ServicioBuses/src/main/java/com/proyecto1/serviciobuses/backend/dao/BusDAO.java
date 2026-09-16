/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
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
public class BusDAO {
    
    private ConexionDB conexiondb;
    
    public static final String CREAR_TABLA = """
                 CREATE TABLE IF NOT EXISTS bus (
                                       bus_id INT AUTO_INCREMENT,
                                       sucursal_id INT NOT NULL,
                                       foto VARCHAR(255) NOT NULL,
                                       placa VARCHAR(20) NOT NULL,
                                       marca VARCHAR(50) NOT NULL,
                                        modelo VARCHAR(50) NOT NULL,
                                        anio_fabricacion SMALLINT NOT NULL,
                                         capacidad INT NOT NULL,
                                         kilometraje_actual DECIMAL(10,2) NOT NULL,
                                        estado BOOLEAN NOT NULL,
                                             
                                        CONSTRAINT pk_bus PRIMARY KEY (bus_id),                 
                                        CONSTRAINT fk_bus_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(sucursal_id),                   
                                         CONSTRAINT ak_bus_placa UNIQUE (placa),              
                                         CONSTRAINT chk_bus_anio CHECK (anio_fabricacion > 0),                              
                                         CONSTRAINT chk_bus_capacidad CHECK (capacidad > 0),                                    
                                          CONSTRAINT chk_bus_kilometraje CHECK (kilometraje_actual >= 0)
                                                     )
                                             """;
    
    public static final String INSERTAR = """
                  INSERT INTO bus (sucursal_id,foto,placa,marca,modelo,anio_fabricacion,capacidad,kilometraje_actual,estado)
                                          VALUES (?,?,?,?,?,?,?,?,?)
                                          """;
    public static final String ACTUALIZAR = """
                       UPDATE bus SET sucursal_id = ?, foto = ?, placa = ?,marca = ?,modelo = ?,anio_fabricacion = ?, capacidad = ?,kilometraje_actual = ?,estado = ?
                               WHERE bus_id = ?
                                            """;
    public static final String BUSCAR_POR_ID = "SELECT * FROM bus WHERE bus_id = ? ";
    public static final String LISTAR = "SELECT * FROM bus ORDER BY placa";
    public static final String DESACTIVAR = "UPDATE bus b SET  estado = FALSE WHERE b.bus_id = ? AND NOT EXISTS(SELECT 1 FROM viaje v WHERE v.bus_id = b.bus_id AND v.hora_llegada_real IS NULL)";
    
    public BusDAO(ConexionDB conexiondb){
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
    
    public boolean insertar(Bus bus){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {

            ps = con.prepareStatement(INSERTAR);

            ps.setInt(1, bus.getSucursalId());
            ps.setString(2, bus.getFoto());
            ps.setString(3, bus.getPlaca());
            ps.setString(4, bus.getMarca());
            ps.setString(5, bus.getModelo());
            ps.setInt(6, bus.getAñoFabricacion());
            ps.setInt(7, bus.getCapacidad());
            ps.setDouble(8, bus.getKilometrajeActual());
            ps.setBoolean(9, bus.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public boolean actualizar(Bus bus){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ACTUALIZAR);
            
            ps.setInt(1, bus.getSucursalId());
            ps.setString(2, bus.getFoto());
            ps.setString(3, bus.getPlaca());
            ps.setString(4, bus.getMarca());
            ps.setString(5, bus.getModelo());
            ps.setInt(6, bus.getAñoFabricacion());
            ps.setInt(7, bus.getCapacidad());
            ps.setDouble(8, bus.getKilometrajeActual());
            ps.setBoolean(9, bus.isEstado());
            ps.setInt(10, bus.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            cerrar(ps);
        }
    }
    
    public Optional<Bus> buscarPorId(int id){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Bus bus = null;
        
        try {

            ps = con.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                bus = construirBus(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(bus);
    }
    
    public Collection<Bus> listar(){
        Collection<Bus> bus = new ArrayList<>();
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps =  null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(LISTAR);
            rs = ps.executeQuery();
            
            while(rs.next()){
                bus.add(construirBus(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cerrar(rs);
            cerrar(ps);
        }
        return bus;
    }
    
    public boolean desactivar(int id){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(DESACTIVAR);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            cerrar(ps);
        }
    }
    
    private Bus construirBus(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        
        
        bus.setId(rs.getInt("bus_id"));
        bus.setSucursalId(rs.getInt("sucursal_id"));
        bus.setFoto(rs.getString("foto"));
        bus.setPlaca(rs.getString("placa"));
        bus.setMarca(rs.getString("marca"));
        bus.setModelo(rs.getString("modelo"));
        bus.setAñoFabricacion(rs.getInt("anio_fabricacion"));
        bus.setCapacidad(rs.getInt("capacidad"));
        bus.setKilometrajeActual(rs.getDouble("kilometraje_actual"));
        bus.setEstado(rs.getBoolean("estado"));
        
        return bus;
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
