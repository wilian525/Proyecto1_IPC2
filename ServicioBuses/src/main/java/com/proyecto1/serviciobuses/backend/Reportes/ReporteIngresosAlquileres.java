/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Bus;
import com.proyecto1.serviciobuses.backend.Model.Usuario;
import com.proyecto1.serviciobuses.backend.Model.Viaje;
import com.proyecto1.serviciobuses.backend.Model.ViajePrivado;
import com.proyecto1.serviciobuses.backend.dao.BusDAO;
import com.proyecto1.serviciobuses.backend.dao.UsuarioDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajeDAO;
import com.proyecto1.serviciobuses.backend.dao.ViajePrivadoDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class ReporteIngresosAlquileres {
    
    private ViajeDAO viajeDao;
    private ViajePrivadoDAO viajePrivadoDao;
    private BusDAO busDao;
    private UsuarioDAO usuarioDao;
    private ReporteUtil reporteUtil;


    public ReporteIngresosAlquileres(  ConexionDB conexiondb) {
        this.viajeDao = new ViajeDAO(conexiondb);
        this.viajePrivadoDao =new ViajePrivadoDAO( conexiondb);
        this.busDao =new BusDAO(conexiondb);
        this.usuarioDao =new UsuarioDAO(conexiondb);
        this.reporteUtil = new ReporteUtil();
    }


    public Collection<ViajePrivado>listarAlquileres(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin) {
        Collection<ViajePrivado> resultado =  new ArrayList<>();

        for (Viaje viaje :viajeDao.listar()) {

            Optional<ViajePrivado> privado =viajePrivadoDao .buscarPorId(viaje.getId());

            if (privado.isEmpty()) {
                continue;
            }

            ViajePrivado alquiler =privado.get();
          
            // validar alquileres pagados
            if (!alquiler.isEstadoAlquiler() || alquiler.getFechaPago()== null || !reporteUtil.fechaEnRango(  alquiler.getFechaPago(), fechaInicio,fechaFin)|| viaje.getBus() == null) {
                continue;
            }
            Bus bus = busDao.buscarPorId( viaje .getBus().getId() ).orElse(null);

            if (bus == null || bus.getSucursalId()!= sucursalId) {
                continue;
            }

            copiarDatosViaje( viaje, alquiler);
            alquiler.setBus(bus);
            resultado.add(alquiler);
        }
        return resultado;
    }

    public Usuario obtenerCliente( ViajePrivado alquiler) {

        if (alquiler == null || alquiler.getUsuarioId() <= 0) {

            return null;
        }

        return usuarioDao.buscarPorId( alquiler.getUsuarioId());
    }

    public double ingresoTotalSucursal(int sucursalId, LocalDate fechaInicio, LocalDate fechaFin) {

        double total = 0;

        for (ViajePrivado alquiler : listarAlquileres(sucursalId,fechaInicio, fechaFin)) {

            total +=alquiler .getPrecioConfirmado();
        }
        return total;
    }


    private void copiarDatosViaje( Viaje viaje,ViajePrivado alquiler) {

        alquiler.setFechaSalida(viaje.getFechaSalida());

        alquiler.setHoraSalidaProgramada( viaje.getHoraSalidaProgramada() );

        alquiler.setHoraSalidaReal( viaje.getHoraSalidaReal() );

        alquiler.setFechaLlegadaEstimada(viaje.getFechaLlegadaEstimada());

        alquiler.setHoraLlegadaEstimada(viaje.getHoraLlegadaEstimada() );

        alquiler.setHoraLlegadaReal(viaje.getHoraLlegadaReal());

        alquiler.setKilometrajeInicial(viaje.getKilometrajeInicial());

        alquiler.setKilometrajeFinal(viaje.getKilometrajeFinal() );

        alquiler.setGastoCombustible( viaje.getGastoCombustible());

        alquiler.setMontoDepreciacion(viaje.getMontoDepreciacion());

        alquiler.setEstado( viaje.isEstado());
    
    }
}
