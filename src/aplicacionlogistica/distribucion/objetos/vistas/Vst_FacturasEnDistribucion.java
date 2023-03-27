/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.Inicio;
import java.util.Date;

/**
 *
 * @author lelopez
 */
public class Vst_FacturasEnDistribucion {
String consecutivo; 
String adherencia; 
String numeroManifiesto; 
Date fechaDistribucion; 
String vehiculo; 
String conductor; 
String nombreConductor; 
String despachador; 
String nombreDespachador; 
String idRuta; 
String nombreDeRuta; 
String tipoRuta; 
String numeroFactura; 
String valorARecaudarFactura; 
Date fechaIng; 
Date fechaDeVenta; 
String cliente; 
String rutaDeCliente; 
String nombreRutaDeCliente; 
String nombreDeCliente; 
String direccionDeCliente; 
String barrio; 
String telefonoCliente; 
String vendedor; 
String formaDePago; 
String idCanal; 
String nombreCanal; 
Double valorFacturaSinIva; 
Double valorIvaFactura; 
Double valorTotalFactura; 
Double valorRechazo; 
Double valorDescuento; 
Double valorTotalRecaudado; 
int salidasDistribucion; 
Date fechaReal; 
String tipoContrato; 
String tipoVehiculo; 
String usuario;  
Inicio ini;

    public Vst_FacturasEnDistribucion(Inicio ini) {
        this.ini=ini;
    }



    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getAdherencia() {
        return adherencia;
    }

    public void setAdherencia(String adherencia) {
        this.adherencia = adherencia;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public Date getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(Date fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public String getNombreDespachador() {
        return nombreDespachador;
    }

    public void setNombreDespachador(String nombreDespachador) {
        this.nombreDespachador = nombreDespachador;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombreDeRuta() {
        return nombreDeRuta;
    }

    public void setNombreDeRuta(String nombreDeRuta) {
        this.nombreDeRuta = nombreDeRuta;
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(String tipoRuta) {
        this.tipoRuta = tipoRuta;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getValorARecaudarFactura() {
        return valorARecaudarFactura;
    }

    public void setValorARecaudarFactura(String valorARecaudarFactura) {
        this.valorARecaudarFactura = valorARecaudarFactura;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getRutaDeCliente() {
        return rutaDeCliente;
    }

    public void setRutaDeCliente(String rutaDeCliente) {
        this.rutaDeCliente = rutaDeCliente;
    }

    public String getNombreRutaDeCliente() {
        return nombreRutaDeCliente;
    }

    public void setNombreRutaDeCliente(String nombreRutaDeCliente) {
        this.nombreRutaDeCliente = nombreRutaDeCliente;
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

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(String formaDePago) {
        this.formaDePago = formaDePago;
    }

    public String getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
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

    public int getSalidasDistribucion() {
        return salidasDistribucion;
    }

    public void setSalidasDistribucion(int salidasDistribucion) {
        this.salidasDistribucion = salidasDistribucion;
    }

    public Date getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(Date fechaReal) {
        this.fechaReal = fechaReal;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    
}
