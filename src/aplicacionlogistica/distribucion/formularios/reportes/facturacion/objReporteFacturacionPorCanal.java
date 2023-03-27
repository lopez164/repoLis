/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.facturacion;

import java.sql.Date;

/**
 *
 * @author lelopez
 */
public class objReporteFacturacionPorCanal {
    Date fecha;
    Double poblaciones;
    Double mayoristas;
    Double minimercados;
    Double tat;
    Double total;

    public objReporteFacturacionPorCanal() {
        
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getPoblaciones() {
        return poblaciones;
    }

    public void setPoblaciones(Double poblaciones) {
        this.poblaciones = poblaciones;
    }

    public Double getMayoristas() {
        return mayoristas;
    }

    public void setMayoristas(Double mayoristas) {
        this.mayoristas = mayoristas;
    }

    public Double getMinimercados() {
        return minimercados;
    }

    public void setMinimercados(Double minimercados) {
        this.minimercados = minimercados;
    }

    public Double getTat() {
        return tat;
    }

    public void setTat(Double tat) {
        this.tat = tat;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
    
    
}

