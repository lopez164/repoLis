/*
 * To change this license header; String  choose License Headers in Project Properties.
 * To change this template file; String  choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mtto.documentos.objetos.GastosPorVehiculo;

/**
 *
 * @author lelopez
 */
public class GastosFlota {

    String idGastoFlota;
    String idProveedor;
    String nombreProveedor;
    String sucursalProveedor;
    String nombreSucursalProveedor;
    String numeroDeOrden;
    String numeroFactura;
    String fechaFactura;
    String vehiculo;
    String conductor;
    String nombreConductor;
    String kilometraje;
    String valorfactura;
    String zona;
    String regional;
    String agencia;
    String fotografiaFactura;
    String formatoFotografia;
    String activo;
    String fechaIng;
    String usuario;
    String flag;

    Inicio ini;
    List<GastosPorVehiculo> listaGastosPorVehiculo;

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getNombreSucursalProveedor() {
        return nombreSucursalProveedor;
    }

    public void setNombreSucursalProveedor(String nombreSucursalProveedor) {
        this.nombreSucursalProveedor = nombreSucursalProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public List<GastosPorVehiculo> getListaGastosPorVehiculo() {
        return listaGastosPorVehiculo;
    }

    public void setListaGastosPorVehiculo(List<GastosPorVehiculo> listaGastosPorVehiculo) {
        this.listaGastosPorVehiculo = listaGastosPorVehiculo;
    }

    public String getIdGastoFlota() {
        return idGastoFlota;
    }

    public void setIdGastoFlota(String idConsecutivo) {
        this.idGastoFlota = idConsecutivo;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getSucursalProveedor() {
        return sucursalProveedor;
    }

    public void setSucursalProveedor(String sucursalProveedor) {
        this.sucursalProveedor = sucursalProveedor;
    }

    public String getNumeroDeOrden() {
        return numeroDeOrden;
    }

    public void setNumeroDeOrden(String numeroDeOrden) {
        this.numeroDeOrden = numeroDeOrden;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
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

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getValorfactura() {
        return valorfactura;
    }

    public void setValorfactura(String valorfactura) {
        this.valorfactura = valorfactura;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getFotografiaFactura() {
        return fotografiaFactura;
    }

    public void setFotografiaFactura(String fotografiaFactura) {
        this.fotografiaFactura = fotografiaFactura;
    }

    public String getFormatoFotografia() {
        return formatoFotografia;
    }

    public void setFormatoFotografia(String formatoFotografia) {
        this.formatoFotografia = formatoFotografia;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
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

    public GastosFlota(Inicio ini) {
        this.ini = ini;
    }

    public boolean guardar() {
        boolean grabado = false;
        List<String> listaDeSentencias = new ArrayList<>();

        ResultSet rst = null;
        PreparedStatement sentencia = null;
        Connection con;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = ini.getConnRemota();

            // listaDeProductos = new ArrayList();
            String sql = "INSERT INTO gastosflota"
                    + "(numeroDeOrden,"
                    + "idProveedor,"
                    + "sucursalProveedor,"
                    + "numeroFactura,"
                    + "fechaFactura,"
                    + "vehiculo,"
                    + "conductor,"
                    + "kilometraje,"
                    + "valorfactura,"
                    + "zona,"
                    + "regional,"
                    + "agencia,"
                    + "usuario) "
                    + "VALUES ('"
                    + this.numeroDeOrden + "','"
                    + this.idProveedor + "','"
                    + this.sucursalProveedor + "','"
                    + this.numeroFactura + "','"
                    + this.fechaFactura + "','"
                    + this.vehiculo + "','"
                    + this.conductor + "','"
                    + this.kilometraje + "','"
                    + this.valorfactura + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE flag=-1;";
            System.out.print(sql + "\n");

            if (con != null) {
                sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                int afectedRows = sentencia.executeUpdate();
                if (afectedRows == 0) {
                    throw new SQLException("No se pudo guardar en la Sentencia =" + sql + "\n");
                }

                rst = sentencia.getGeneratedKeys();
                if (rst.next()) {
                    this.setIdGastoFlota("" + rst.getInt(1));
                    System.out.print("consecutivo de la factura " + this.numeroFactura + "\n");

                } else {
                    System.out.print("Factura ya existe " + this.getIdGastoFlota() + "\n");

                    return true;
                }

                rst.close();
                sentencia.close();
                // con.close();

                int i = 1;
                for (GastosPorVehiculo item : listaGastosPorVehiculo) {

                    String cadena = " INSERT INTO itemsgastosflota("
                            + "idLineaFactura,"
                            + "idConsecutivoGasto,"
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
                            + i++ + "','"
                            + this.getIdGastoFlota() + "','"
                            + item.getCodigoSubcuenta() + "','"
                            + item.getDescripcionProductoServicio() + "','"
                            + item.getCantidad() + "','"
                            + item.getValorUnitario() + "','"
                            + item.getValorTotal() + "','"
                            + item.getActivo() + "',"
                            + "CURRENT_TIMESTAMP,'"
                            + item.getUsuario() + "','"
                            + item.getFlag() + "','"
                            + item.getKilometraje() + "','"
                            + item.getNombreSucursal() + "','"
                            + item.getPlaca() + "','"
                            + item.getNumeroRecibo() + "','"
                            + item.getFechaRecibo() + "','"
                            + item.getCiudad() + "','"
                            + item.getAgencia() + "')"
                            + "ON DUPLICATE KEY UPDATE flag=2;";

                    listaDeSentencias.add(cadena);

                }
                grabado = ini.insertarDatosRemotamente(listaDeSentencias, "FacturasLogisticas");

            } // fin try
        } catch (Exception ex) {

        }

        return grabado;
    }

    public String guardar(int k) {
        String msg = "";
        boolean grabado = false;
        List<String> listaDeSentencias = new ArrayList<>();

        ResultSet rst = null;
        PreparedStatement sentencia = null;
        Connection con;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = ini.getConnRemota();

            // listaDeProductos = new ArrayList();
            String sql = "INSERT INTO gastosflota"
                    + "(numeroDeOrden,"
                    + "idProveedor,"
                    + "sucursalProveedor,"
                    + "numeroFactura,"
                    + "fechaFactura,"
                    + "vehiculo,"
                    + "conductor,"
                    + "kilometraje,"
                    + "valorfactura,"
                    + "zona,"
                    + "regional,"
                    + "agencia,"
                    + "usuario) "
                    + "VALUES ('"
                    + this.numeroDeOrden + "','"
                    + this.idProveedor + "','"
                    + this.sucursalProveedor + "','"
                    + this.numeroFactura + "','"
                    + this.fechaFactura + "','"
                    + this.vehiculo + "','"
                    + this.conductor + "','"
                    + this.kilometraje + "','"
                    + this.valorfactura + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE flag=-1;";
            System.out.print(sql + "\n");

            if (con != null) {
                sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                int afectedRows = sentencia.executeUpdate();
                if (afectedRows == 0) {
                    throw new SQLException("No se pudo guardar en la Sentencia =" + sql + "\n");
                }

                rst = sentencia.getGeneratedKeys();
                if (rst.next()) {
                    this.setIdGastoFlota("" + rst.getInt(1));
                    System.out.print("consecutivo de la factura " + this.numeroFactura + "\n");

                } else {
                    System.out.print("Factura ya existe " + this.numeroFactura + "\n");

                    return "Factura ya existe " + this.numeroFactura + "\n";
                }

                rst.close();
                sentencia.close();
                // con.close();

                int i = 1;
                for (GastosPorVehiculo item : listaGastosPorVehiculo) {

                    String cadena = " INSERT INTO itemsgastosflota("
                            + "idLineaFactura,"
                            + "idConsecutivoGasto,"
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
                            + i++ + "','"
                            + this.getIdGastoFlota() + "','"
                            + item.getCodigoSubcuenta() + "','"
                            + item.getDescripcionProductoServicio() + "','"
                            + item.getCantidad() + "','"
                            + item.getValorUnitario() + "','"
                            + item.getValorTotal() + "','"
                            + item.getActivo() + "',"
                            + "CURRENT_TIMESTAMP,'"
                            + item.getUsuario() + "','"
                            + item.getFlag() + "','"
                            + item.getKilometraje() + "','"
                            + item.getNombreSucursal() + "','"
                            + item.getPlaca() + "','"
                            + item.getNumeroRecibo() + "','"
                            + item.getFechaRecibo() + "','"
                            + item.getCiudad() + "','"
                            + item.getAgencia() + "')"
                            + "ON DUPLICATE KEY UPDATE flag=2;";

                    listaDeSentencias.add(cadena);

                }
                grabado = ini.insertarDatosRemotamente(listaDeSentencias, "FacturasLogisticas");
               
                if (grabado) {
                    msg = "Factura # " + this.numeroFactura + " guardada en la BBDD \n";
                }

            } // fin try
        } catch (Exception ex) {
            msg += "Error al guardar dato en el sistema \n";

        }

        return msg;
    }

}
