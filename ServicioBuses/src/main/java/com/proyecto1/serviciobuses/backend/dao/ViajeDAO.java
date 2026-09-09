/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.Model.ViajeRegular;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
/**
 *
 * @author wilian
 */
public class ViajeDAO {
    
    private ConexionDB conexiondb;
    
    public static final String CREAR_TABLA = """
        CREATE TABLE IF NOT EXISTS viaje (
            viaje_id INT AUTO_INCREMENT,
            bus_id INT,
            chofer_id INT,
            fecha_salida DATE NOT NULL,
            hora_salida_programada TIME NOT NULL,
            fecha_llegada_estimada DATE NOT NULL,
            hora_llegada_estimada TIME NOT NULL,
            hora_salida_real TIME,
            kilometraje_inicial DECIMAL(10,2),
            hora_llegada_real TIME,
            kilometraje_final DECIMAL(10,2),
            gasto_combustible DECIMAL(10,2),
            monto_depreciacion DECIMAL(10,2),
            estado BOOLEAN NOT NULL,
            tipo VARCHAR(20) NOT NULL,

            CONSTRAINT pk_viaje PRIMARY KEY (viaje_id),
            CONSTRAINT fk_viaje_bus FOREIGN KEY (bus_id) REFERENCES bus(bus_id),
            CONSTRAINT fk_viaje_chofer FOREIGN KEY (chofer_id) REFERENCES chofer(usuario_id),
            CONSTRAINT chk_viaje_tipo CHECK (tipo IN ('REGULAR', 'PRIVADO')),
            CONSTRAINT chk_viaje_km_inicial CHECK ( kilometraje_inicial IS NULL OR kilometraje_inicial >= 0 ),
            CONSTRAINT chk_viaje_km_final CHECK (kilometraje_final IS NULL OR kilometraje_final >= 0),
            CONSTRAINT chk_viaje_combustible CHECK (gasto_combustible IS NULL OR gasto_combustible >= 0 ),
            CONSTRAINT chk_viaje_depreciacion  CHECK (monto_depreciacion IS NULL OR monto_depreciacion >= 0 )
        )
        """;
    
    public static final String INSERTAR = """
                  INSERT INTO viaje  (bus_id, chofer_id, fecha_salida, hora_salida_programada,
                      fecha_llegada_estimada, hora_llegada_estimada, estado, tipo)
                       VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                                          """;
    public static final String ACTUALIZAR = """
                                            UPDATE viaje SET bus_id = ?, chofer_id = ? , fecha_salida = ? , hora_salida_programada = ?,
                                            fecha_llegada_estimada = ?, hora_llegada_estimada = ?, estado = ? WHERE viaje_id = ? AND hora_salida_real IS NULL
                                            """;
    public static final String BUSCAR_POR_ID = "SELECT * FROM viaje WHERE viaje_id = ? ";
    public static final String LISTAR =  " SELECT * FROM viaje ORDER BY fecha_salida, hora_salida_programada ";
    public static final String  REGISTRAR_SALIDA = "UPDATE viaje  SET hora_salida_real = ?, kilometraje_inicial = ?, estado = ? WHERE viaje_id = ? AND hora_salida_real IS NULL AND hora_llegada_real IS NULL";
    public static final String REGISTRAR_LLEGADA = """
                                                   UPDATE viaje SET hora_llegada_real = ? , kilometraje_final = ? , gasto_combustible = ?, 
                                                               monto_depreciacion = ? , estado = ?   WHERE viaje_id = ? AND hora_salida_real IS NOT NULL AND hora_llegada_real IS NULL
                                                   """;
    public static final String ASIGNAR_RECURSOS = "UPDATE viaje SET bus_id = ?, chofer_id = ?  WHERE viaje_id = ? AND tipo = 'PRIVADO' AND hora_salida_real IS NULL ";
    public static final String ELIMINAR = "DELETE FROM viaje WHERE viaje_id = ? AND hora_salida_real IS NULL";
    
    public ViajeDAO(ConexionDB conexiondb){
        this.conexiondb = conexiondb;
    }
    
    public void crearTabla(){
        Connection con = conexiondb.obtenerConeccion();
        Statement statement = null;
        
        try {
            statement = con.createStatement();
            statement.execute(CREAR_TABLA);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(statement);
        }
    }
    
    public boolean insertar(Viaje viaje){
        if (viaje == null) {
             return false;
        }
        return insertarYObtenerId(viaje) > 0;
    }
    
    public boolean actualizar(Viaje viaje){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ACTUALIZAR);

            if (viaje.getBus() != null) {
                    ps.setInt(1, viaje.getBus().getId());
            } else {
                  ps.setNull(1, Types.INTEGER);
            }
            if (viaje.getChofer() != null) {
                 ps.setInt(2, viaje.getChofer().getId());
            } else {
                  ps.setNull(2, Types.INTEGER);
            }
            
            ps.setDate(3,Date.valueOf(viaje.getFechaSalida()) );
            ps.setTime(4, Time.valueOf(viaje.getHoraSalidaProgramada()));
            ps.setDate( 5,Date.valueOf(viaje.getFechaLlegadaEstimada()) );
            ps.setTime(6, Time.valueOf(viaje.getHoraLlegadaEstimada()) );
            ps.setBoolean(7, viaje.isEstado());
            ps.setInt(8, viaje.getId());

            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            cerrar(ps);
        }
    }
    
    public boolean eliminar(int viajeId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ELIMINAR);
            ps.setInt(1, viajeId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    public Optional<Viaje> buscarPorId(int id){
        
        Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
        ResultSet rs = null;

        Viaje viaje = null;

        try {
            ps = con.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                viaje = construirViaje(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }

        return Optional.ofNullable(viaje);
    }
    
    public Collection<Viaje> listar(){  
        Collection<Viaje> viajes = new ArrayList();
         
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet  rs = null;
        
        try {
            ps = con.prepareStatement(LISTAR);
            rs = ps.executeQuery();
            
            while(rs.next()){
                viajes.add(construirViaje(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cerrar(rs);
            cerrar(ps);
        }
        return viajes;
    }
    
    public boolean registrarSalida(Viaje viaje){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(REGISTRAR_SALIDA);

            ps.setTime(1,Time.valueOf(viaje.getHoraSalidaReal()));
            ps.setDouble(2,viaje.getKilometrajeInicial());
            ps.setBoolean(3,viaje.isEstado());
            ps.setInt(4,viaje.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            cerrar(ps);
        }
    }
    
    public boolean registrarLlegada(Viaje viaje){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
                ps = con.prepareStatement(REGISTRAR_LLEGADA);

            ps.setTime( 1,Time.valueOf(viaje.getHoraLlegadaReal()) );
            ps.setDouble(2,viaje.getKilometrajeFinal() );
            ps.setDouble(3,viaje.getGastoCombustible() );
            ps.setDouble(4,viaje.getMontoDepreciacion());
            ps.setBoolean(5,viaje.isEstado() );
            ps.setInt(6,viaje.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }  finally{
            cerrar(ps);
        }
    }
    
    private Viaje construirViaje(ResultSet rs) throws SQLException{
        Viaje viaje = new Viaje();
        viaje.setId(rs.getInt("viaje_id"));
        int busId = rs.getInt("bus_id");
        
        if (!rs.wasNull()) {
              Bus bus = new Bus();
              bus.setId(rs.getInt("bus_id"));
               viaje.setBus(bus);
        }
        
        int choferId = rs.getInt("chofer_id");
        if (!rs.wasNull()) {
             Chofer chofer = new Chofer();
             chofer.setId(rs.getInt("chofer_id"));
              viaje.setChofer(chofer);
        }
        

        viaje.setFechaSalida( rs.getDate("fecha_salida").toLocalDate());
        viaje.setHoraSalidaProgramada(rs.getTime( "hora_salida_programada" ).toLocalTime() );
        viaje.setFechaLlegadaEstimada(  rs.getDate( "fecha_llegada_estimada").toLocalDate()  );
        viaje.setHoraLlegadaEstimada(rs.getTime( "hora_llegada_estimada" ).toLocalTime());

        if (rs.getTime("hora_salida_real") != null) {
            viaje.setHoraSalidaReal( rs.getTime( "hora_salida_real" ).toLocalTime()  );
        }

        if (rs.getTime("hora_llegada_real") != null) {
            viaje.setHoraLlegadaReal( rs.getTime( "hora_llegada_real").toLocalTime());
        }

        viaje.setKilometrajeInicial( rs.getDouble("kilometraje_inicial") );
        viaje.setKilometrajeFinal( rs.getDouble("kilometraje_final") );
        viaje.setGastoCombustible(   rs.getDouble("gasto_combustible") );
        viaje.setMontoDepreciacion( rs.getDouble("monto_depreciacion"));
        viaje.setEstado( rs.getBoolean("estado") );

        return viaje;
    }
    
    private String obtenerTipo(Viaje viaje){
        if (viaje instanceof ViajeRegular) {
            return "REGULAR";
        }
        if (viaje instanceof ViajePrivado) {
            return "PRIVADO";
        }
        throw new IllegalArgumentException("El viaje debe ser regular  o privado");
    }
    
    public int insertarYObtenerId(Viaje viaje){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(INSERTAR,Statement.RETURN_GENERATED_KEYS);
            if (viaje.getBus() != null) {
                    ps.setInt(1, viaje.getBus().getId());
            } else {
                  ps.setNull(1, Types.INTEGER);
            }
            
            if (viaje.getChofer() != null) {
                 ps.setInt(2, viaje.getChofer().getId());
            } else {
                ps.setNull(2,Types.INTEGER);
            }
            ps.setDate(3, Date.valueOf(viaje.getFechaSalida()));
            ps.setTime(4,Time.valueOf( viaje.getHoraSalidaProgramada()));
            ps.setDate(5, Date.valueOf(viaje.getFechaLlegadaEstimada()));
            ps.setTime(6, Time.valueOf(viaje.getHoraLlegadaEstimada()));
            ps.setBoolean(7, viaje.isEstado());
            ps.setString(8, obtenerTipo(viaje));
            
            if (ps.executeUpdate() ==0) {
                 return -1;
            }
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                 return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return -1;
    }
    
    public boolean asignarRecursos(int viajeId, int busId,int choferId){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(ASIGNAR_RECURSOS);
            ps.setInt(1, busId);
            ps.setInt(2, choferId);
            ps.setInt(3, viajeId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
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
