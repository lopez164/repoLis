/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;

/**
 *
 * @author lelopez
 */
public class MantenimientosAsignados {
    String placa; 
    int idMantenimiento; 
    String nombreMantenimiento;
    String frecuencia; 
    int activo;
    
    Inicio ini;

    public MantenimientosAsignados(Inicio ini) {
        this.ini = ini;
    }

    public String getNombreMantenimiento() {
        return nombreMantenimiento;
    }

    public void setNombreMantenimiento(String nombreMantenimiento) {
        this.nombreMantenimiento = nombreMantenimiento;
    }

    
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
    
}
