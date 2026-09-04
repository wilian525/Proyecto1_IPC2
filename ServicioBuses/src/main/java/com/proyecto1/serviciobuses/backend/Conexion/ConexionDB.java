/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author wilian
 */
public class ConexionDB {
    
    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String SCHEMA = "sistema_bus";
    public static final String USER_NAME = "wilian";
    public static final String PASSWORD = "Clasic.Mysql";
    
    public static final String URL = "jdbc:mysql://"
            + IP + ":" + PUERTO + "/" + SCHEMA ;
    
    private Connection conexion;
    
    public ConexionDB(){
        conectar();
    }
    
    private void conectar(){
        try{
                conexion = DriverManager.getConnection(URL,USER_NAME,PASSWORD);
                System.out.println("conexion establecida correctamente");
                System.out.println("esquema" + conexion.getSchema());
        }catch(SQLException ex){
            System.out.println("no fue posible la conexion a la base de datos");
            ex.printStackTrace();
        }
    }
    
    public Connection obtenerConeccion(){
    return conexion;
    }
    
    public void cerrar(){
        try{
                if(conexion != null && !conexion.isClosed()){
                        conexion.close();
                        System.out.println("conexion cerrada");
            }
        }catch(SQLException ex){
                ex.printStackTrace();
        }
    }
}

