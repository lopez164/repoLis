/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.util.Date;

/**
 *
 * @author lelopez
 */
public class CFacturasAutorizadasDescuentos {

    String numeroFactura;
    Date fechaDeVenta;
    String idCliente;
    String nitCliente;
    String nombreDeCliente;
    String direccionDeCliente;
    String barrio;
    int formaDePago;
    int canal;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazo;
    double valorDescuento;
    double valorTotalRecaudado;
    int isFree;
    int estadoFactura;
    String numeroDescuento;
    double porcentajeDesuento;
    String condicion ;  

    public CFacturasAutorizadasDescuentos() {
        
    }
    
     public CFacturasAutorizadasDescuentos(Inicio ini) {
        
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getDireccionDeCliente() {
        return direccionDeCliente;
    }

    public void setDireccionDeCliente(String direccionDeCliente) {
        this.direccionDeCliente = direccionDeCliente;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public int getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(int formaDePago) {
        this.formaDePago = formaDePago;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public double getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public void setValorFacturaSinIva(double valorFacturaSinIva) {
        this.valorFacturaSinIva = valorFacturaSinIva;
    }

    public double getValorIvaFactura() {
        return valorIvaFactura;
    }

    public void setValorIvaFactura(double valorIvaFactura) {
        this.valorIvaFactura = valorIvaFactura;
    }

    public double getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(double valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }

    public double getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public double getValorTotalRecaudado() {
        return valorTotalRecaudado;
    }

    public void setValorTotalRecaudado(double valorTotalRecaudado) {
        this.valorTotalRecaudado = valorTotalRecaudado;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public String getNumeroDescuento() {
        return numeroDescuento;
    }

    public void setNumeroDescuento(String numeroDescuento) {
        this.numeroDescuento = numeroDescuento;
    }

    public double getPorcentajeDesuento() {
        return porcentajeDesuento;
    }

    public void setPorcentajeDesuento(double porcentajeDesuento) {
        this.porcentajeDesuento = porcentajeDesuento;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
     
     
}
