/*
 * To change this license header;choose License Headers in Project Properties.
 * To change this template file;choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.Inicio;

/**
 *
 * @author lopez164
 */
public class Vst_ProductosPorFacturaPorManifiesto {
    Inicio ini;
    int numeroDeManifiesto;
    String fechaDistribucion;
    String vehiculo;
    String numeroFactura;
    String idCliente;
    String nombreDeCliente;
    String nombreVendedor;
    String fechaDeVenta;
    int FormaDePago;
    int canal;
    int zona;
    int regional;
    int agencia;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazo;
    double valorDescuento;
    double valorTotalRecaudado;
    String codigoProducto;
    String descripcionProducto;
    int cantidad;
    double valorUnitario;
    double valorTotal;
    double valorUnitarioConIva;
    double valorTotalConIva;

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

    public int getNumeroDeManifiesto() {
        return numeroDeManifiesto;
    }

    public void setNumeroDeManifiesto(int numeroDeManifiesto) {
        this.numeroDeManifiesto = numeroDeManifiesto;
    }

    public String getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(String fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
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

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String vendedor) {
        this.nombreVendedor = vendedor;
    }

    public String getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(String fechaDeVenta) {
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

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorUnitarioConIva() {
        return valorUnitarioConIva;
    }

    public void setValorUnitarioConIva(double valorUnitarioConIva) {
        this.valorUnitarioConIva = valorUnitarioConIva;
    }

    public double getValorTotalConIva() {
        return valorTotalConIva;
    }

    public void setValorTotalConIva(double valorTotalConIva) {
        this.valorTotalConIva = valorTotalConIva;
    }

    public Vst_ProductosPorFacturaPorManifiesto(Inicio ini){
        this.ini=ini;
    }
}
