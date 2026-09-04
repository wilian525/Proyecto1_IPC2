/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
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
public class SucursalDAO {
    
    private ConexionDB conexiondb;
    private static final String CREAR_TABLA = """ 
                                              CREATE TABLE IF NOT EXISTS sucursal(
                                                    sucursal_id INT AUTO_INCREMENT,
                                                    nombre VARCHAR(100) NOT NULL,
                                                    direccion VARCHAR(200) NOT NULL,
                                                    telefono VARCHAR(20) NOT NULL,
                                                    CONSTRAINT pk_sucursal PRYMARY KEY (sucursal_id)
                                              )
                                              """;
    
    public static final String INSERTAR = " INSERTAR INTO sucursal (nombre,direccion,telefono) VALUES (?,?,?) ";
    public static final String ACTUALIZAR = "UPDATE sucursal SET nombre = ?, direccion = ?, telefono = ? WHERE sucursal_id = ? ";
    public static final String BUSCAR_POR_ID = "SELECT * FROM sucursal WHERE sucursal_id = ? ";
    public static final String LISTAR = "SELECT * FROM sucursal ORDER BY nombre";
    
    public SucursalDAO(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
    }
    
    public void crearTabla() {

        Connection conexion = conexiondb.obtenerConeccion();
        Statement statement = null;

        try {
            statement = conexion.createStatement();
            statement.execute(CREAR_TABLA);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(statement);
        }
    }

    public boolean insertar(Sucursal sucursal) {
        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;

        try {
            ps = conexion.prepareStatement(INSERTAR);
            
            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getDireccion());
            ps.setString(3, sucursal.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }

    public boolean actualizar(Sucursal sucursal) {
        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;

        try {
            ps = conexion.prepareStatement(ACTUALIZAR);

            ps.setString(1, sucursal.getNombre());
            ps.setString(2, sucursal.getDireccion());
            ps.setString(3, sucursal.getTelefono());
            ps.setInt(4, sucursal.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }

    public Sucursal buscarPorId(int id) {
        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return construirSucursal(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }

        return null;
    }

    public Collection<Sucursal> listar() {
        Collection<Sucursal> sucursales = new ArrayList<>();

        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(LISTAR);
            rs = ps.executeQuery();

            while (rs.next()) {
                sucursales.add(  construirSucursal(rs) );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return sucursales;
    }

    private Sucursal construirSucursal(ResultSet rs)throws SQLException {
        return new Sucursal( rs.getInt("sucursal_id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"));
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
  

