/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Servicio;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSistemas;
import com.proyecto1.serviciobuses.backend.Model.AdministradorSucursal;
import com.proyecto1.serviciobuses.backend.Model.CarteraDigital;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.dao.CarteraDAO;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import com.proyecto1.serviciobuses.backend.dao.UsuarioDAO;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author wilian
 */
public class SucursalServicio {
    
    private ConexionDB conexiondb;
    private SucursalDAO sucursalDao;
    private UsuarioDAO usuarioDao;
    private CarteraDAO carteraDao;

    public SucursalServicio(ConexionDB conexiondb) {
        this.conexiondb = conexiondb;
        this.sucursalDao = new SucursalDAO(conexiondb);
        this.usuarioDao = new UsuarioDAO(conexiondb);
        this.carteraDao = new CarteraDAO(conexiondb);
    }

    public boolean crearSucursal(int administradorSistemaId,Sucursal sucursal,AdministradorSucursal administrador){
        if (administradorSistemaId <= 0 || sucursal == null || administrador == null) {
             return false;
        }
        
        Usuario creador =usuarioDao.buscarPorId( administradorSistemaId);

        if (!(creador  instanceof AdministradorSistemas) || !creador.isEstado()) {
            return false;
        }

        Connection con = conexiondb.obtenerConeccion();
        if (con == null) {
            return false;
        }

        boolean autoCommitAnterior = true;

        try {

            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int sucursalId =sucursalDao.insertarYObtenerId(sucursal);

            if (sucursalId <= 0) {
                con.rollback();
                return false;
            }

            sucursal.setId(sucursalId);

            administrador.setSucursalId( sucursalId );
            administrador.setEstado( true );

            int usuarioId = usuarioDao .insertarYObtenerId( administrador);

            if (usuarioId <= 0) {
                con.rollback();
                return false;
            }

            CarteraDigital cartera = new CarteraDigital(0,0 );

            if (!carteraDao.insertar(usuarioId, cartera)) {
                con.rollback();
                return false;
            }

            administrador.setId(usuarioId );

            con.commit();
            return true;


        } catch (SQLException | RuntimeException e) {

            rollback(con);

            e.printStackTrace();
            return false;

        } finally {
            restaurarAutoCommit(con,autoCommitAnterior);
        }
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
