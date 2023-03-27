/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.ReporteovilizadoPorTodosLosConductores;

/**
 *
 * @author lelopez
 */
// ReporteTotalVstMovilizacionFacturasDescargadas vstMFD = new ReporteTotalVstMovilizacionFacturasDescargadas(); 
public class FReporteMovilizadoPorTodosLosConductoresModelo {

    String fechaDistribucion;
    String numeroManifiesto;
    String nombreConductor;
    String vehiculo;
    String ET;
    String EP;
    String DT;
    String RE;
    String PG;
    String cant_entregada;
    String nombreCanal;
    String subtotal_facturas;

    public String getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(String fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getEntregaTotal() {
        return ET;
    }

    public void setEntregaTotal(String ET) {
        this.ET = ET;
    }

    public String getEntregaParcial() {
        return EP;
    }

    public void setEntregaParcial(String EP) {
        this.EP = EP;
    }

    public String getDevolucionTotal() {
        return DT;
    }

    public void setDevolucionTotal(String RT) {
        this.DT = RT;
    }

    public String getReenvios() {
        return RE;
    }

    public void setReenvios(String RE) {
        this.RE = RE;
    }

    public String getProgramados() {
        return PG;
    }

    public void setProgramados(String PG) {
        this.PG = PG;
    }

    public String getCant_entregada() {
        return cant_entregada;
    }

    public void setCantidadEntregada(String cant_entregada) {
        this.cant_entregada = cant_entregada;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getSubtotal_facturas() {
        return subtotal_facturas;
    }

    public void setSubtotalFacturas(String subtotal_facturas) {
        this.subtotal_facturas = subtotal_facturas;
    }

}
