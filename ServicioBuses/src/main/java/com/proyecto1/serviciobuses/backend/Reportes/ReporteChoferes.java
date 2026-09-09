/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.dao.ChoferDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
public class ReporteChoferes {
    
    private ChoferDAO choferDao;
    private ViajeDAO viajeDao;
    
    public ReporteChoferes(ConexionDB conexiondb){
        this.choferDao = new ChoferDAO(conexiondb);
        this.viajeDao = new ViajeDAO(conexiondb);
    }
    
    public Collection<Chofer> listarChoferes(int sucursalId){
        Collection<Chofer> resultado = new ArrayList<>();
        
        if (sucursalId <= 0) {
             return resultado;
        }
        
        for(Chofer chofer: choferDao.listar()){
            if (chofer.getSucursalId() != null && chofer.getSucursalId() == sucursalId) {
                 resultado.add(chofer);
            }
        }
        return resultado;
    }
    
    public int totalViajesRealizado(int choferId){
        int total = 0;
        
        for(Viaje viaje : viajeDao.listar()){
                 
            if (viaje.getChofer() != null && viaje.getChofer().getId() == choferId && viaje.getHoraLlegadaReal() != null) {
                 total++;
            }
        }
        return total;
    }
    
}
