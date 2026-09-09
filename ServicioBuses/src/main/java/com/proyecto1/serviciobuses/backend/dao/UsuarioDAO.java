/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSistemas;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Cliente;
import com.proyecto1.serviciobuses.backend.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;
/**
 *
 * @author wilian
 */
public class UsuarioDAO {
    
    private ConexionDB conexiondb ;
    
    private static final String CREAR_TABLA = """
          CREATE TABLE IF NOT EXISTS usuario (
                    usuario_id INT AUTO_INCREMENT,
                    nombre VARCHAR(100) NOT NULL,
                    nit VARCHAR(20) NOT NULL,
                    dpi VARCHAR(20) NOT NULL,
                    telefono VARCHAR(20) NOT NULL,
                     direccion VARCHAR(200) NOT NULL,
                    username VARCHAR(50) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    estado BOOLEAN NOT NULL,
                   rol VARCHAR(30) NOT NULL,
                   sucursal_id INT,
                                              
                    CONSTRAINT pk_usuario PRIMARY KEY (usuario_id),                                          
                    CONSTRAINT ak_usuario_dpi UNIQUE (dpi),                                            
                     CONSTRAINT ak_usuario_username UNIQUE (username),                                        
                     CONSTRAINT fk_usuario_sucursal FOREIGN KEY (sucursal_id)  REFERENCES sucursal(sucursal_id)
                                                              )
                                               """;
     private static final String INSERTAR = "INSERT INTO usuario(nombre,nit,dpi,telefono,direccion,username,password,estado,rol,sucursal_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
     private static final String ACTUALIZAR = "UPDATE usuario SET nombre = ?, nit = ?, dpi = ? , telefono = ?, direccion = ?, username = ?, password = ?, estado = ?, rol = ?, sucursal_id = ? WHERE usuario_id = ?";
     private static final String BUSCAR_POR_ID = "SELECT * FROM usuario WHERE usuario_id = ?";
     private static final String BUSCAR_POR_USERNAME = "SELECT * FROM usuario WHERE username = ?";
     private static final String LISTAR = "SELECT * FROM usuario ORDER BY nombre";
     private static final String CAMBIAR_ESTADO = "UPDATE usuario SET estado = ? WHERE usuario_id = ?";
     private static final String CONTAR_ADMIN_SISTEMA_ACTIVO = "SELECT COUNT(*) AS total FROM usuario WHERE rol = 'ADMIN_SISTEMA' AND estado = TRUE";
     private static final String CONTAR_ADMIN_SUCURSAL_ACTIVOS = "SELECT COUNT(*) AS total FROM usuario WHERE rol = 'ADMIN_SUCURSAL' AND sucursal_id = ? AND estado = TRUE";
     private static final String ACTUALIZAR_PERFIL ="UPDATE usuario SET nombre = ?, nit = ? , dpi = ? , telefono = ? , direccion = ? WHERE usuario_id = ? ";
     
     public UsuarioDAO(ConexionDB conexiondb){
         this.conexiondb = conexiondb;
     }
     
     public void crearTabla(){
         Connection con = conexiondb.obtenerConeccion();
         Statement statement = null;
         
         try {
             statement = con.createStatement();
             statement.execute(CREAR_TABLA);
             
         } catch (SQLException e) {
             e.printStackTrace();;
         } finally {
             cerrar(statement);
         }
     }
     
     public boolean insertar(Usuario usuario){
          return insertarYObtenerId(usuario) > 0;
     }
     
     public boolean actualizar(Usuario usuario){
            if (usuario == null || usuario.getId() < 0) {
             return false;
         }
            
            String rol = obtenerRol(usuario);
            
            if (rol == null) {
             return false;
         }
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         
         try {
              ps = con.prepareStatement(ACTUALIZAR);

         ps.setString(1, usuario.getNombre());
        ps.setString(2, usuario.getNit());
        ps.setString(3, usuario.getDpi());
        ps.setString(4, usuario.getTelefono());
        ps.setString(5, usuario.getDireccion());
        ps.setString(6, usuario.getUserName());
        ps.setString(7, usuario.getPassword());
        ps.setBoolean(8, usuario.isEstado());
        ps.setString(9, rol);

            
             if (usuario.getSucursalId() != null) {
                 ps.setInt(10, usuario.getSucursalId());
             } else {
                 ps.setNull(10, Types.INTEGER);
             }
             
             ps.setInt(11, usuario.getId());
             
             return ps.executeUpdate() > 0;
             
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         } finally {
             cerrar(ps);
         }
     }
     
     public Usuario buscarPorId(int id){
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(BUSCAR_POR_ID);
             ps.setInt(1, id);
             rs = ps.executeQuery();
             
             if (rs.next()) {
                 return construirUsuario(rs);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             cerrar(rs);
             cerrar(ps);
         }
                 return null;
     }
     
     public Usuario buscarPorUsername(String username){
         if (username == null  || username.isBlank()) {
             return null;
         }
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(BUSCAR_POR_USERNAME);
             ps.setString(1, username);
             rs = ps.executeQuery();
             
             if (rs.next()) {
                 return construirUsuario(rs);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally{
             cerrar(rs);
             cerrar(ps);
         }
         return null;
     }
     
     public Collection<Usuario> listar(){
         Collection<Usuario> usuarios = new ArrayList<>();
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(LISTAR);
             rs = ps.executeQuery();
             
             while(rs.next()){
                 Usuario usuario = construirUsuario(rs);
                 if (usuario != null) {
                     usuarios.add(usuario);
                 }      
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             cerrar(rs);
             cerrar(ps);
         }
         return usuarios;
     }
     
     public boolean cambiarEstado(int id, boolean estado){
         if (id <= 0 ) {
              return false;
         }
         
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         
         try {
             ps = con.prepareStatement(CAMBIAR_ESTADO );
             ps.setBoolean(1, estado);
             ps.setInt(2, id);
             return ps.executeUpdate() > 0;
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         } finally {
             cerrar(ps);
         }
         
     }
     
     private String obtenerRol(Usuario usuario){
         if (usuario instanceof AdministradorSistemas) {
        return "ADMIN_SISTEMA";
    }

    if (usuario instanceof AdministradorSucursal) {
        return "ADMIN_SUCURSAL";
    }

    if (usuario instanceof Chofer) {
        return "CHOFER";
    }

    if (usuario instanceof Cliente) {
        return "CLIENTE";
    }

    return null;
     }
     
     private Usuario construirUsuario(ResultSet rs ) throws SQLException{
          int id = rs.getInt("usuario_id");
         String nombre = rs.getString("nombre");
          String nit = rs.getString("nit");
          String dpi = rs.getString("dpi");
         String telefono = rs.getString("telefono");
          String direccion = rs.getString("direccion");
          String username = rs.getString("username");
          String password = rs.getString("password");
          boolean estado = rs.getBoolean("estado");
          
          String rol = rs.getString("rol");
          
          Usuario usuario = null;
          
          if ("ADMIN_SISTEMA".equals(rol)) {
             usuario = new AdministradorSistemas(id,nombre,nit, dpi,telefono,direccion,username,password,estado);
         } else if("ADMIN_SUCURSAL".equals(rol)){
             usuario = new AdministradorSucursal(id,nombre,nit,dpi,telefono,direccion,username,password,estado);  
         } else if("CHOFER".equals(rol)){
         Chofer chofer = new Chofer();
          
         chofer.setId(id);
        chofer.setNombre(nombre);
        chofer.setNit(nit);
        chofer.setDpi(dpi);
        chofer.setTelefono(telefono);
        chofer.setDireccion(direccion);
        chofer.setUserName(username);
        chofer.setPassword(password);
        chofer.setEstado(estado);

        usuario = chofer;
         } else if("CLIENTE".equals(rol)){
             usuario = new Cliente(id,nombre,nit,dpi,telefono,direccion,username,password,estado);
         }
          if (usuario != null) {
              int sucursalId = rs.getInt("sucursal_id");
              
              if (!rs.wasNull()) {
                  usuario.setSucursalId(sucursalId);
              } else{
                  usuario.setSucursalId(null);
              }
         }
          return usuario;
     }
     
     public int contarAdministradoresSistemasActivos(){
         Connection con  = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(CONTAR_ADMIN_SISTEMA_ACTIVO);
             rs = ps.executeQuery();
             if (rs.next()) {
                  return rs.getInt("total");
             }
            
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             cerrar(rs);
             cerrar(ps);
         }
         return 0;
     }
     
     public int contarAdministradorSucursalActivos(int sucursalId){
         if (sucursalId <= 0) {
              return 0;
         }
         Connection con =  conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(CONTAR_ADMIN_SUCURSAL_ACTIVOS);
             ps.setInt(1, sucursalId);
             rs = ps.executeQuery();
             if (rs.next()) {
                  return rs.getInt("total");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             cerrar(rs);
             cerrar(ps);
         }
         return 0;
     }
     
     public int insertarYObtenerId(Usuario usuario){
         if (usuario == null) {
              return -1;
         }
         String rol = obtenerRol(usuario);
         Connection con = conexiondb.obtenerConeccion();
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = con.prepareStatement(INSERTAR,Statement.RETURN_GENERATED_KEYS);
             ps.setString(1, usuario.getNombre());
             ps.setString(2, usuario.getNit());
             ps.setString(3, usuario.getDpi() );
             ps.setString( 4,usuario.getTelefono());
             ps.setString( 5,usuario.getDireccion());
             ps.setString(6, usuario.getUserName());
              ps.setString( 7,usuario.getPassword());
            ps.setBoolean( 8, usuario.isEstado());
            ps.setString(9, rol);
            
             if (usuario.getSucursalId() != null) {
                  ps.setInt(10, usuario.getSucursalId());
             } else {
                 ps.setNull(10, Types.INTEGER);
             }
             
             if (ps.executeUpdate() == 0) {
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
     
     public boolean actualizarPerfil(Usuario usuario){
         if (usuario == null || usuario.getId() <= 0) {
           return false;
    }

    Connection con = conexiondb.obtenerConeccion();
    PreparedStatement ps = null;

    try {
        ps = con.prepareStatement(ACTUALIZAR_PERFIL );
        ps.setString(1,  usuario.getNombre());
        ps.setString(2, usuario.getNit());
        ps.setString(  3, usuario.getDpi());
        ps.setString(4, usuario.getTelefono());
        ps.setString(5, usuario.getDireccion());
        ps.setInt(  6, usuario.getId());

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
