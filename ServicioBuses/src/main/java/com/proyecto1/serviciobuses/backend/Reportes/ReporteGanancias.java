/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Sucursal;
import com.proyecto1.serviciobuses.backend.dao.SucursalDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author wilian
 */
public class ReporteGanancias {
    
    private SucursalDAO sucursalDao;
    private ReporteIngresoBoletos reporteBoletos;
    private ReporteIngresosAlquileres reporteAlquileres;
    private ReporteCosto reporteCostos;
    
    public ReporteGanancias(ConexionDB conexiondb){
          this.sucursalDao = new SucursalDAO(conexiondb);
          this.reporteBoletos = new ReporteIngresoBoletos(conexiondb);
          this.reporteAlquileres = new ReporteIngresosAlquileres( conexiondb );
          this.reporteCostos =new ReporteCosto(conexiondb);    
    }
    
    public Collection<Sucursal> listarSucursal(Integer sucursalId){
            Collection<Sucursal> resultado = new ArrayList<>();
            
            for(Sucursal sucursal : sucursalDao.listar()){
                    
                if (sucursalId == null || sucursal.getId() == sucursalId) {
                            resultado.add(sucursal);
                }
            }
            return resultado;
    }
    
    public double ingresoBoletos(int sucursalId, LocalDate fechaInicio, LocalDate fechaFIn){
            return reporteBoletos.ingresoTotalSucursal(sucursalId, fechaInicio, fechaFIn);
    }
    
    public double IngresosAlquileres(int sucursalId , LocalDate fechaInicio , LocalDate fechaFIn){
            return reporteAlquileres.ingresoTotalSucursal(sucursalId, fechaInicio, fechaFIn);
    }
    
    public double  totalIngreso(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
            return ingresoBoletos(sucursalId, fechaInicio, fechaFin) + IngresosAlquileres(sucursalId,fechaInicio,fechaFin);
    
    }
    
    public double totalCostos(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
          return reporteCostos.granTotal(sucursalId, fechaInicio, fechaFin);
    }
    
    public double gananciaNeta(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin){
            return totalIngreso(sucursalId,fechaInicio,fechaFin) - totalCostos(sucursalId,fechaInicio,fechaFin);
            
    }
    
    public double totalIngresosGeneral(Collection<Sucursal> sucursales, LocalDate fechaInicio, LocalDate fechaFin){
        double total = 0;
        
        for(Sucursal sucursal : sucursales){
                 total += totalIngreso(sucursal.getId(), fechaInicio,fechaFin);
        }
        return total;
    }
    
    public double totalCostoGeneral(Collection<Sucursal> sucursales, LocalDate fechaInicio, LocalDate fechaFin){
            double total = 0;
            
            for(Sucursal sucursal : sucursales){
                    total += totalCostos(sucursal.getId(),fechaInicio,fechaFin);
            }
         return total;
    }
            public double totalGananciaGeneral(Collection<Sucursal> sucursales, LocalDate fechaInicio, LocalDate fechaFin){
                    return totalIngresosGeneral(sucursales, fechaInicio,fechaFin) - totalCostoGeneral(sucursales, fechaInicio,fechaFin );
            
            }
    
}
