/*
 * To change this license header; String  choose License Headers in Project Properties.
 * To change this template file; String  choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class GastosPorVehiculo {

    String idLineaFactura;
    String numeroDeOrden;
    String idProveedor;
    String nombreProveedor;
    String iditemGasto;
    String idConsecutivoGasto;
    int codigoCuentaPpal;
    String nombreCuentaPrincipal;
    int codigoSubcuenta;
    String nombreCuentaSecundaria;
    String descripcionProductoServicio;
    String cantidad;
    String valorUnitario;
    String valorTotal;
    String activo;
    Date fechaIng;
    String usuario;
    int flag;
    String kilometraje;
    String nombreSucursal;
    String placa;
    String conductor;
    String nombreConductor;
    String numeroRecibo;
    String fechaRecibo;
    String ciudad;
    int agencia;
    String nombreAgencia;
    int idRegional;
    String nombreRegional;
    int idZona;
    String nombreZona;
    double recorrido;
    double consumoPorGalon;

    Inicio ini;

    public String getIdLineaFactura() {
        return idLineaFactura;
    }

    public void setIdLineaFactura(String idLineaFactura) {
        this.idLineaFactura = idLineaFactura;
    }

    
    
    public double getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(double recorrido) {
        this.recorrido = recorrido;
    }

    
    
    public double getConsumoPorGalon() {
        return consumoPorGalon;
    }

    public void setConsumoPorGalon(double consumoPorGalon) {
        this.consumoPorGalon = consumoPorGalon;
    }
    
    

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    
    
    public String getNumeroDeOrden() {
        return numeroDeOrden;
    }

    public void setNumeroDeOrden(String numeroDeOrden) {
        this.numeroDeOrden = numeroDeOrden;
    }

    public String getIditemGasto() {
        return iditemGasto;
    }

    public void setIditemGasto(String iditemGasto) {
        this.iditemGasto = iditemGasto;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public String getIdConsecutivoGasto() {
        return idConsecutivoGasto;
    }

    public void setIdConsecutivoGasto(String idConsecutivoGasto) {
        this.idConsecutivoGasto = idConsecutivoGasto;
    }

    public int getCodigoSubcuenta() {
        return codigoSubcuenta;
    }

    public void setCodigoSubcuenta(int codigoSubcuenta) {
        this.codigoSubcuenta = codigoSubcuenta;
    }

    public String getDescripcionProductoServicio() {
        return descripcionProductoServicio;
    }

    public void setDescripcionProductoServicio(String descripcionProductoServicio) {
        this.descripcionProductoServicio = descripcionProductoServicio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(String valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public GastosPorVehiculo(Inicio ini) {
        this.ini = ini;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getCodigoCuentaPpal() {
        return codigoCuentaPpal;
    }

    public void setCodigoCuentaPpal(int codigoCuentaPpal) {
        this.codigoCuentaPpal = codigoCuentaPpal;
    }

    public String getNombreCuentaPrincipal() {
        return nombreCuentaPrincipal;
    }

    public void setNombreCuentaPrincipal(String nombreCuentaPrincipal) {
        this.nombreCuentaPrincipal = nombreCuentaPrincipal;
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

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public String getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(String fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public int getIdRegional() {
        return idRegional;
    }

    public void setIdRegional(int idRegional) {
        this.idRegional = idRegional;
    }

    public String getNombreRegional() {
        return nombreRegional;
    }

    public void setNombreRegional(String nombreRegional) {
        this.nombreRegional = nombreRegional;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    
    
    public String getNombreCuentaSecundaria() {
        return nombreCuentaSecundaria;
    }

    public void setNombreCuentaSecundaria(String nombreCuentaSecundaria) {
        this.nombreCuentaSecundaria = nombreCuentaSecundaria;
    }

    public String getSentenciaInsertSQL() {
        String cadena = null;
        try {

            cadena = " INSERT INTO itemsgastosflota("
                    + "codigoSubcuenta,"
                    + "descripcionProductoServicio,"
                    + "cantidad,"
                    + "valorUnitario,"
                    + "valorTotal,"
                    + "activo,"
                    + "fechaIng,"
                    + "usuario,"
                    + "flag,"
                    + "kilometraje,"
                    + "proveedor,"
                    + "placa,"
                    + "numeroRecibo,"
                    + "fechaRecibo,"
                    + "ciudad,"
                    + "agencia) "
                    + "VALUES ('"
                    + this.codigoSubcuenta + "','"
                    + this.descripcionProductoServicio + "','"
                    + this.cantidad + "','"
                    + this.valorUnitario + "','"
                    + this.valorTotal + "','"
                    + this.activo + "',"
                    + "CURRENT_TIMESTAMP,'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1','" //this.flag + "','"
                    + this.kilometraje + "','"
                    + this.nombreSucursal + "','"
                    + this.placa + "','"
                    + this.numeroRecibo + "','"
                    + this.fechaRecibo + "','"
                    + this.ciudad + "','"
                    + this.agencia + "')"
                    + "ON DUPLICATE KEY UPDATE flag=2;";

        } catch (Exception ex) {
            Logger.getLogger(GastosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cadena;
    }

}
