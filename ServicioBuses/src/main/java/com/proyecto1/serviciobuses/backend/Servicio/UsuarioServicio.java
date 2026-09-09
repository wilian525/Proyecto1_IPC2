/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSistemas;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.CarteraDigital;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Cliente;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.UsuarioDAO;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author wilian
 */
public class UsuarioServicio {
    
    private UsuarioDAO usuarioDao;
    private ChoferDAO choferDao;
    private ConexionDB conexiondb;
    private CarteraDAO carteraDao;
    
    public UsuarioServicio(ConexionDB conexiondb){
        this.conexiondb = conexiondb; 
        this.usuarioDao = new UsuarioDAO(conexiondb);
        this.choferDao = new ChoferDAO(conexiondb);
    }
    
    public boolean cambiarEstado(int usuarioId, boolean estado){
        if (usuarioId <= 0) {
             return false;
        }
        
        Usuario usuario = usuarioDao.buscarPorId(usuarioId);
        if (usuario == null) {
              return false;
        }
        
        if (estado) {
             return usuarioDao.cambiarEstado(usuarioId, true);
        }
        
        if (usuario instanceof Chofer) {
             return choferDao.cambiarEstado(usuarioId, estado);
        }
        
        // siempre debe quedar un administrador de sistemas activo
        if (usuario instanceof AdministradorSistemas) {
             int activos = usuarioDao.contarAdministradoresSistemasActivos();
             if (activos <= 1) {
                  return false;
            }
        }
        
        if (usuario instanceof AdministradorSucursal) {
             if (usuario.getSucursalId() == null) {
                  return false;
            }
             int activos = usuarioDao.contarAdministradorSucursalActivos(usuario.getSucursalId());
             if (activos <= 1) {
                  return false;
            }
        }
        return usuarioDao.cambiarEstado(usuarioId, false);
    }
    
    private boolean registrarUsuarioConCartera(Usuario usuario) {

    if (usuario == null) {
        return false;
    }

    Connection con = conexiondb.obtenerConeccion();
    if (con == null) {
        return false;
    }

    boolean autoCommitAnterior = true;

    try {

        autoCommitAnterior =  con.getAutoCommit();

        con.setAutoCommit(false);
        int usuarioId =usuarioDao.insertarYObtenerId(usuario );

        if (usuarioId <= 0) {
            con.rollback();
            return false;
        }

        CarteraDigital cartera = new CarteraDigital(0,  0);

        if (!carteraDao.insertar(usuarioId,cartera)) {
            con.rollback();
            return false;
        }

        usuario.setId( usuarioId);

        con.commit();
        return true;


    } catch (SQLException | RuntimeException e) {

        rollback(con);
        e.printStackTrace();
        return false;

    } finally {
        restaurarAutoCommit(con, autoCommitAnterior );
    }
}
    
    public boolean registrarCliente(Cliente cliente) {

    if (cliente == null) {
        return false;
    }

    cliente.setEstado(true);
    cliente.setSucursalId(null);

    return registrarUsuarioConCartera(cliente);
}
    
    public boolean registrarAdministradorSucursal(int administradorSistemaId, AdministradorSucursal nuevoAdministrador) {

    if (administradorSistemaId <= 0 || nuevoAdministrador == null || nuevoAdministrador.getSucursalId() == null|| nuevoAdministrador.getSucursalId() <= 0) {
        return false;
    }

    Usuario creador =usuarioDao.buscarPorId(administradorSistemaId );
    if (!(creador instanceof AdministradorSistemas)) {
        return false;
    }

    if (!creador.isEstado()) {
        return false;
    }

    nuevoAdministrador.setEstado(true );

    return registrarUsuarioConCartera( nuevoAdministrador);
}
    
    public boolean actualizarPerfil(Usuario usuario){
            return usuarioDao.actualizarPerfil(usuario);
    }
    
    public Usuario iniciarSesion(String username,String password){
        if (username == null|| username.isBlank()|| password == null || password.isBlank()) {
        return null;
    }

    Usuario usuario =usuarioDao.buscarPorUsername( username );

    if (usuario == null) {
        return null;
    }

    if (!usuario.iniciarSesion(username, password)) {
        return null;
    }

    return usuario;
    }
    
     private void rollback( Connection con) {
        if (con != null) {

            try {

                con.rollback();

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    private void restaurarAutoCommit( Connection con, boolean autoCommitAnterior) {
        if (con != null) {

            try {
                con.setAutoCommit(autoCommitAnterior);

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
}
