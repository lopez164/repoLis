/*
 * To change this license header; String choose License Headers in Project Properties.
 * To change this template file; String choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.rutero;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class rutero {

    Inicio ini;
    String consecutivo;
    String adherencia;
    String numeroManifiesto;
    String estadoManifiesto;
    String fechaDistribucion;
    String vehiculo;
    String conductor;
    String nombresConductor;
    String idRuta;
    String nombreDeRuta;
    String idtipoDeMovimiento;
    String nombreTipoDeMovimiento;
    String numeroFactura;
    String fechaDeVenta;
    String codigoCliente;
    String nitCliente;
    String nombreDeCliente;
    String nombreEstablecimiento;
    String direccion;
    String barrio;
    String ciudad;
    String telefono;
    String latitud;
    String longitud;
    String vendedor;
    String formaDePago;
    String idCanalDeVenta;
    String nombreCanalDeVenta;
    String valorFacturaSinIva;
    String valorTotalFactura;
    String valorRechazo;
    String valorDescuento;
    String valorRecaudado;
    String idcausalesDeRechazo;
    String nombreCausalDeRechazo;
    String usuario;
    String flag;
    String fechaActualizacion;

    public rutero() {

    }

    public rutero(String manifiesto, String numeroFactura) {
        try {
            ResultSet rst;
            Statement st;
            Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            String sql = "select * "
                    + "FROM "
                    + "vst_rutero "
                    + "WHERE "
                    + "numeroManifiesto='" + manifiesto + "' AND "
                    + "numeroFactura='" + numeroFactura + "';";
            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {

                this.adherencia = "" + rst.getInt("adherencia");
                this.numeroManifiesto = "" + rst.getInt("numeroManifiesto");
                this.estadoManifiesto = "" + rst.getInt("estadoManifiesto");
                this.fechaDistribucion = "" + rst.getDate("fechaDistribucion");
                this.vehiculo = rst.getString("vehiculo");
                this.conductor = rst.getString("conductor");
                this.nombresConductor = rst.getString("nombresConductor");
                this.idRuta = "" + rst.getInt("idRuta");
                this.nombreDeRuta = rst.getString("nombreDeRuta");
                this.idtipoDeMovimiento = "" + rst.getInt("idtipoDeMovimiento");
                this.nombreTipoDeMovimiento = rst.getString("nombreTipoDeMovimiento");
                this.numeroFactura = rst.getString("numeroFactura");
                this.fechaDeVenta = "" +  rst.getDate("fechaDeVenta");
                this.codigoCliente = rst.getString("codigoCliente");
                this.nitCliente = rst.getString("nitCliente");
                this.nombreDeCliente = rst.getString("nombreDeCliente");
                this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
                this.direccion = rst.getString("direccion");
                this.barrio = rst.getString("barrio");
                this.ciudad = rst.getString("ciudad");
                this.telefono = rst.getString("telefono");
                this.latitud = "" + rst.getDouble("latitud");
                this.longitud = "" + rst.getDouble("longitud");
                this.vendedor = rst.getString("vendedor");
                this.formaDePago = "" + rst.getInt("formaDePago");
                this.idCanalDeVenta = "" + rst.getInt("idCanalDeVenta");
                this.nombreCanalDeVenta = rst.getString("nombreCanalDeVenta");
                this.valorFacturaSinIva = rst.getString("valorFacturaSinIva");
                this.valorTotalFactura = rst.getString("valorTotalFactura");
                this.valorRechazo = rst.getString("valorRechazo");
                this.valorDescuento = rst.getString("valorDescuento");
                this.valorRecaudado = rst.getString("valorRecaudado");
                this.idcausalesDeRechazo = "" + rst.getInt("idcausalesDeRechazo");
                this.nombreCausalDeRechazo = rst.getString("nombreCausalDeRechazo");
                this.flag = rst.getString("flag");
                this.fechaActualizacion = "" + rst.getDate("fechaActualizacion");

            }
        } catch (SQLException ex) {
            Logger.getLogger(rutero.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getNombresConductor() {
        return nombresConductor;
    }

    public void setNombresConductor(String nombresConductor) {
        this.nombresConductor = nombresConductor;
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

    public String getIdtipoDeMovimiento() {
        return idtipoDeMovimiento;
    }

    public void setIdtipoDeMovimiento(String idtipoDeMovimiento) {
        this.idtipoDeMovimiento = idtipoDeMovimiento;
    }

    public String getNombreTipoDeMovimiento() {
        return nombreTipoDeMovimiento;
    }

    public void setNombreTipoDeMovimiento(String nombreTipoDeMovimiento) {
        this.nombreTipoDeMovimiento = nombreTipoDeMovimiento;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(String fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
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

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
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

    public String getIdCanalDeVenta() {
        return idCanalDeVenta;
    }

    public void setIdCanalDeVenta(String idCanalDeVenta) {
        this.idCanalDeVenta = idCanalDeVenta;
    }

    public String getNombreCanalDeVenta() {
        return nombreCanalDeVenta;
    }

    public void setNombreCanalDeVenta(String nombreCanalDeVenta) {
        this.nombreCanalDeVenta = nombreCanalDeVenta;
    }

    public String getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public void setValorFacturaSinIva(String valorFacturaSinIva) {
        this.valorFacturaSinIva = valorFacturaSinIva;
    }

    public String getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(String valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }

    public String getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(String valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public String getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(String valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public String getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(String valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public String getIdcausalesDeRechazo() {
        return idcausalesDeRechazo;
    }

    public void setIdcausalesDeRechazo(String idcausalesDeRechazo) {
        this.idcausalesDeRechazo = idcausalesDeRechazo;
    }

    public String getNombreCausalDeRechazo() {
        return nombreCausalDeRechazo;
    }

    public void setNombreCausalDeRechazo(String nombreCausalDeRechazo) {
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String insertar() {
        String sql = "INSERT INTO `rutero`(`adherencia`,`numeroManifiesto`,`fechaDistribucion`,"
                + "`vehiculo`,`conductor`,`nombresConductor`,`idRuta`,`nombreDeRuta`,`idtipoDeMovimiento`,"
                + "`nombreTipoDeMovimiento`,`numeroFactura`,`fechaDeVenta`,`codigoCliente`,"
                + "`nitCliente`,`nombreDeCliente`,`nombreEstablecimiento`,`direccion`,`barrio`,`ciudad`,"
                + "`telefono`,`latitud`,`longitud`,`vendedor`,`formaDePago`,`idCanalDeVenta`,`nombreCanalDeVenta`,"
                + "`valorFacturaSinIva`,`valorTotalFactura`,`valorRechazo`,`valorDescuento`,`valorRecaudado`,"
                + "`idcausalesDeRechazo`,`nombreCausalDeRechazo`,`fechaIng`,`usuario`,`flag`,`fechaActualizacion`) "
                + "VALUES ('"
                + this.adherencia + "','"
                + this.numeroManifiesto + "','"
                + this.fechaDistribucion + "','"
                + this.vehiculo + "','"
                + this.conductor + "','"
                + this.nombresConductor + "','"
                + this.idRuta + "','"
                + this.nombreDeRuta + "','"
                + this.idtipoDeMovimiento + "','"
                + this.nombreTipoDeMovimiento + "','"
                + this.numeroFactura + "','"
                + this.fechaDeVenta + "','"
                + this.codigoCliente + "','"
                + this.nitCliente + "','"
                + this.nombreDeCliente + "','"
                + this.nombreEstablecimiento + "','"
                + this.direccion + "','"
                + this.barrio + "','"
                + this.ciudad + "','"
                + this.telefono + "','"
                + this.latitud + "','"
                + this.longitud + "','"
                + this.vendedor + "','"
                + this.formaDePago + "','"
                + this.idCanalDeVenta + "','"
                + this.nombreCanalDeVenta + "','"
                + this.valorFacturaSinIva + "','"
                + this.valorTotalFactura + "','"
                + this.valorRechazo + "','"
                + this.valorDescuento + "','"
                + this.valorRecaudado + "','"
                + this.idcausalesDeRechazo + "','"
                + this.nombreCausalDeRechazo + "','"
                + this.usuario + "','"
                + this.flag + "','"
                + this.fechaActualizacion + "') "
                + "ON DUPLICATE KEY UPDATE flag=1;";

        return sql;
    }

}
