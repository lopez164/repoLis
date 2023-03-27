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
public class objReporteFacturacionTAT {

    Date fechaDistribucion;
    Double total_TAT_mayor30;
    int menor_diez;
    int diez_veinte;
    int veinte_treinta;
    int mayor_treinta;
    int cantidad_total;
    Double total_TAT_general;

    public objReporteFacturacionTAT() {

    }

    public Date getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(Date fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public Double getTotal_TAT_mayor30() {
        return total_TAT_mayor30;
    }

    public void setTotal_TAT_mayor30(Double total_TAT_mayor30) {
        this.total_TAT_mayor30 = total_TAT_mayor30;
    }

    public int getMenor_diez() {
        return menor_diez;
    }

    public void setMenor_diez(int menor_diez) {
        this.menor_diez = menor_diez;
    }

    public int getDiez_veinte() {
        return diez_veinte;
    }

    public void setDiez_veinte(int diez_veinte) {
        this.diez_veinte = diez_veinte;
    }

    public int getVeinte_treinta() {
        return veinte_treinta;
    }

    public void setVeinte_treinta(int veinte_treinta) {
        this.veinte_treinta = veinte_treinta;
    }

    public int getMayor_treinta() {
        return mayor_treinta;
    }

    public void setMayor_treinta(int mayor_treinta) {
        this.mayor_treinta = mayor_treinta;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public Double getTotal_TAT_general() {
        return total_TAT_general;
    }

    public void setTotal_TAT_general(Double total_TAT_general) {
        this.total_TAT_general = total_TAT_general;
    }

}