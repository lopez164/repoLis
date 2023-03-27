/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;
import mtto.documentos.Threads.HiloListadoDeSucursales;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtto.documentos.objetos.GastosPorVehiculo;

/**
 *
 * @author lelopez
 */
public class FacturasLogisticas {

    int idfacturacionLogistica;
    int sucursalProveedor;
    String nombreSucursal;
    int numeroDeOrden;
    String numeroFactura;
    String fechaFactura;
    String placaVehiculo;
    String conductor;
    String nombreConductor;
    String apellidosConductor;
    int kilometraje;
    double valorfactura;
    int zona;
    int regional;
    int agencia;
//fotografiaFactura	longblob
    String formatoFotografia;
    int activo;
    Date fechaIng;
    String usuario;
    int flag;

    Inicio ini;

    List<GastosPorVehiculo> listaDeProductos;

    public int getSucursalProveedor() {
        return sucursalProveedor;
    }

    public void setSucursalProveedor(int sucursalProveedor) {
        this.sucursalProveedor = sucursalProveedor;
    }

    public int getIdfacturacionLogistica() {
        return idfacturacionLogistica;
    }

    public void setIdfacturacionLogistica(int idfacturacionLogistica) {
        this.idfacturacionLogistica = idfacturacionLogistica;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public int getNumeroDeOrden() {
        return numeroDeOrden;
    }

    public void setNumeroDeOrden(int numeroDeOrden) {
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

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
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

    public String getApellidosConductor() {
        return apellidosConductor;
    }

    public void setApellidosConductor(String apellidosConductor) {
        this.apellidosConductor = apellidosConductor;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public double getValorfactura() {
        return valorfactura;
    }

    public void setValorfactura(double valorfactura) {
        this.valorfactura = valorfactura;
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

    public String getFormatoFotografia() {
        return formatoFotografia;
    }

    public void setFormatoFotografia(String formatoFotografia) {
        this.formatoFotografia = formatoFotografia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
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

    public List<GastosPorVehiculo> getListaDeProductos() {
        return listaDeProductos;
    }

    public void setListaDeProductos(List<GastosPorVehiculo> listaDeProductos) {
        this.listaDeProductos = listaDeProductos;
    }
/*
    private void setListaDeProductos(Inicio ini) {
        this.ini = ini;
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        try {

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        
            listaDeProductos = new ArrayList();

            String sql = "select * from  itemsgastosflota where activo=1 and idfacturacionLogistica='" + idfacturacionLogistica + "';";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {
                    GastosPorVehiculo item = new GastosPorVehiculo(ini);

                    item.setIditemsPorFactura(rst.getInt("iditemsPorFactura"));
                    item.setIdfacturacionLogistica(rst.getInt("idfacturacionLogistica"));
                    item.setCodigoSubcuenta(rst.getInt("codigoSubcuenta"));
                    item.setDescripcionProductoServicio(rst.getString("descripcionProductoServicio"));
                    item.setCantidad(rst.getDouble("cantidad"));
                    item.setValorUnitario(rst.getDouble("valorUnitario"));
                    item.setValorTotal(rst.getDouble("valorTotal"));
                    item.setActivo(rst.getInt("activo"));
                    item.setFechaIng(rst.getDate("fechaIng"));
                    item.setUsuario(rst.getString("usuario"));
                    item.setFlag(rst.getInt("flag"));

                    listaDeProductos.add(item);

                }
                rst.close();
                st.close();
                //con.close();

            } // fin try
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
        }

    } **/

    public FacturasLogisticas() {

    }

    public FacturasLogisticas(Inicio ini) {
        this.ini = ini;
    }
/*
    public FacturasLogisticas(Inicio ini, String factura, int sucursalProveedor) {
        this.ini = ini;
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        try {

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        
            listaDeProductos = new ArrayList();

            String sql = "select * from  view_facturasLogisticas where activo=1 and numeroFactura='" + factura + "' and sucursalProveedor='" + sucursalProveedor + "';";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {
                    this.idfacturacionLogistica = rst.getInt("idfacturacionLogistica");
                    this.sucursalProveedor = rst.getInt("sucursalProveedor");
                    this.nombreSucursal = rst.getString("nombreSucursal");
                    this.numeroDeOrden = rst.getInt("numeroDeOrden");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.fechaFactura = "" + rst.getDate("fechaFactura");
                    this.placaVehiculo = rst.getString("placaVehiculo");
                    this.conductor = rst.getString("conductor");
                    this.nombreConductor = rst.getString("nombreConductor");
                    this.apellidosConductor = rst.getString("apellidosConductor");
                    this.kilometraje = rst.getInt("kilometraje");;
                    this.valorfactura = rst.getDouble("valorfactura");
                    this.zona = rst.getInt("zona");;
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
//                       fotografiaFactura	longblob
                    this.formatoFotografia = rst.getString("formatoFotografia");
                    this.activo = rst.getInt("activo");
                    this.fechaIng = rst.getDate("fechaIng");
                    this.usuario = rst.getString("usuario");
                    this.flag = rst.getInt("flag");

                }
                rst.close();
                st.close();
                //con.close();

                setListaDeProductos(ini);

            } // fin try
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
        }

    } **/

    public boolean guardarFactura() {
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
                    + this.sucursalProveedor + "','"
                    + this.numeroFactura + "','"
                    + this.fechaFactura + "','"
                    + this.placaVehiculo + "','"
                    + this.conductor + "','"
                    + this.kilometraje + "','"
                    + this.valorfactura + "','"
                    + ini.getUser().getZona() + "','"
                    + ini.getUser().getRegional() + "','"
                    + ini.getUser().getAgencia() + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE flag=-1;";

            if (con != null) {
                sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int afectedRows = sentencia.executeUpdate();
                if (afectedRows == 0) {
                    throw new SQLException("No se pudo guardar en la Sentencia =" + sql);
                }

                rst = sentencia.getGeneratedKeys();
                if (rst.next()) {
                    this.setIdfacturacionLogistica(rst.getInt(1));
                    //this.idMantenimientosPorPlaca = generatedKeys.getInt(1);
                }


                rst.close();
                sentencia.close();
               // con.close();

               int i= 1;
                for (GastosPorVehiculo item : listaDeProductos) {
                    {
//                        String cadena = "INSERT INTO itemsgastosflota "
//                                + "(idfacturacionLogistica,"
//                                + "codigoSubcuenta,"
//                                + "descripcionProductoServicio,"
//                                + "cantidad,"
//                                + "valorUnitario,"
//                                + "valorTotal,"
//                                + "usuario) "
//                                + "VALUES"
//                                + "('"
//                                + this.idfacturacionLogistica + "','"
//                                + item.codigoSubcuenta + "','"
//                                + item.descripcionProductoServicio + "','"
//                                + item.cantidad + "','"
//                                + item.valorUnitario + "','"
//                                + item.valorTotal + "','"
//                                + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') "
//                                + "ON DUPLICATE KEY UPDATE "
//                                + "flag='-1';";
                        
                    String cadena = " INSERT INTO itemsgastosflota("
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
                +  i++ + "','"          
                +  item.getCodigoSubcuenta() + "','"
                +  item.getDescripcionProductoServicio() + "','"
                +  item.getCantidad() + "','"
                +  item.getValorUnitario() + "','"
                +  item.getValorTotal() + "','"
                +  item.getActivo() + "',"
                + "CURRENT_TIMESTAMP,'"
                +  item.getUsuario() + "','"
                +  item.getFlag() + "','"
                +  item.getKilometraje() + "','"
                +  item.getNombreSucursal() + "','"
                +  item.getPlaca() + "','"
                +  item.getNumeroRecibo() + "','"
                +  item.getFechaRecibo() + "','"
                +  item.getCiudad() + "','"
                +  item.getAgencia() + "')"
                + "ON DUPLICATE KEY UPDATE flag=2;";
                        

                 listaDeSentencias.add(cadena);

                    }

                }
                grabado = ini.insertarDatosRemotamente(listaDeSentencias,"FacturasLogisticas");

            } // fin try
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
        }

        return grabado;
    }

}
