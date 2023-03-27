/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import java.sql.Date;

/**
 *
 * @author lopez164
 */
public class Vst_ProductosPorFacturaEnDistribucion {
    int consecutivo;
    String numeroDeManifiesto;
    Date fechaDistribucion;
    String vehiculo;
    String numeroFactura;
    String idCliente;
    int zona;
    int regional;
    int agencia;
    String nombreDeCliente;
    String nombreVendedor;
    Date fechaDeVenta;
    int FormaDePago;
    int canal;
    Double valorFacturaSinIva;
    Double valorIvaFactura;
    Double valorTotalFactura;
    Double valorRechazo;
    Double valorDescuento;
    Double valorTotalRecaudado;
    String codigoProducto;
    String descripcionProducto;
    int cantidad;
    Double valorUnitario;
    Double valorTotal;
    Double valorUnitarioConIva;
    Double valorTotalConIva;
    Date fechaReal;

    public Date getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(Date fechaReal) {
        this.fechaReal = fechaReal;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    
    public Date getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(Date fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public String getNumeroDeManifiesto() {
        return numeroDeManifiesto;
    }

    public void setNumeroDeManifiesto(String numeroDeManifiesto) {
        this.numeroDeManifiesto = numeroDeManifiesto;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public int getFormaDePago() {
        return FormaDePago;
    }

    public void setFormaDePago(int FormaDePago) {
        this.FormaDePago = FormaDePago;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public Double getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public void setValorFacturaSinIva(Double valorFacturaSinIva) {
        this.valorFacturaSinIva = valorFacturaSinIva;
    }

    public Double getValorIvaFactura() {
        return valorIvaFactura;
    }

    public void setValorIvaFactura(Double valorIvaFactura) {
        this.valorIvaFactura = valorIvaFactura;
    }

    public Double getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(Double valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }

    public Double getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(Double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public Double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(Double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Double getValorTotalRecaudado() {
        return valorTotalRecaudado;
    }

    public void setValorTotalRecaudado(Double valorTotalRecaudado) {
        this.valorTotalRecaudado = valorTotalRecaudado;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorUnitarioConIva() {
        return valorUnitarioConIva;
    }

    public void setValorUnitarioConIva(Double valorUnitarioConIva) {
        this.valorUnitarioConIva = valorUnitarioConIva;
    }

    public Double getValorTotalConIva() {
        return valorTotalConIva;
    }

    public void setValorTotalConIva(Double valorTotalConIva) {
        this.valorTotalConIva = valorTotalConIva;
    }
    
}
