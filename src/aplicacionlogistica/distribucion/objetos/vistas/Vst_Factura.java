/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class Vst_Factura {

    Inicio ini;
    String numeroFactura = null;
    Date fechaDeVenta;
    String idCliente;
    String nitCliente;
    String nombreDeCliente;
    String direccionDeCliente;
    String nombreEstablecimiento;
    String barrio;
    String ciudadCliente;
    String telefonoCliente;
    String emailCliente;
    String vendedor;
    String telefonoVendedor;
    String latitud;
    String longitud;
    String formaDePago;
    String pago;
    int canal;
    String nombreCanalDeVenta;
    int isFree;
    int idEstadoFactura;
    String nombreEstadoFactura;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazo;
    double valorDescuento;
    double valorRecaudado;
    double pesofactura;
    int salidasDistribucion;
    String numeroDescuento;
    String numeroRecogida;
    String trasmitido;
    String nombreMovimiento;
    int plazoDiaz;
    List<CBitacoraFacturas> listaDeMovimientosBitacora;
    List<Vst_ProductosPorFactura> listaDetalleFactura;
    List<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    List<Vst_ProductosPorFacturaDescargados> listaDeProductosEntregados;
    List<Vst_ProductosPorFacturaDescargados> listaDeProductosConDesccuento;
    List<Vst_FacturasPorManifiesto> listaDeMovimientosfactura;

    boolean remoto = false;

    public int getPlazoDiaz() {
        return plazoDiaz;
    }

    public void setPlazoDiaz(int plazoDiaz) {
        this.plazoDiaz = plazoDiaz;
    }

    
    public String getTelefonoVendedor() {
        return telefonoVendedor;
    }

    public void setTelefonoVendedor(String telefonoVendedor) {
        this.telefonoVendedor = telefonoVendedor;
    }

    
    
    public int getSalidasDistribucion() {
        return salidasDistribucion;
    }

    public void setSalidasDistribucion(int salidasDistribucion) {
        this.salidasDistribucion = salidasDistribucion;
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

//    public String getCliente() {
//        return idCliente;
//    }
//    public void setCliente(String cliente) {
//        this.idCliente = cliente;
//    }
    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
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

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public String getNombreCanalDeVenta() {
        return nombreCanalDeVenta;
    }

    public void setNombreCanalDeVenta(String nombreCanalDeVenta) {
        this.nombreCanalDeVenta = nombreCanalDeVenta;
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

    public double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(double valorTotalRecaudado) {
        this.valorRecaudado = valorTotalRecaudado;
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

    public String getCiudadCliente() {
        return ciudadCliente;
    }

    public void setCiudadCliente(String ciudadCliente) {
        this.ciudadCliente = ciudadCliente;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;

    }

    public int getEstadoFactura() {
        return idEstadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.idEstadoFactura = estadoFactura;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public double getPesofactura() {
        return pesofactura;
    }

    public void setPesofactura(double pesofactura) {
        this.pesofactura = pesofactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEstadoFactura() {
        return idEstadoFactura;
    }

    public void setIdEstadoFactura(int idEstadoFactura) {
        this.idEstadoFactura = idEstadoFactura;
    }

    public String getNombreEstadoFactura() {
        return nombreEstadoFactura;
    }

    public void setNombreEstadoFactura(String nombreEstadoFactura) {
        this.nombreEstadoFactura = nombreEstadoFactura;
    }

    public String getNumeroDescuento() {
        return numeroDescuento;
    }

    public void setNumeroDescuento(String numeroDevolucion) {
        this.numeroDescuento = numeroDevolucion;
    }

    public String getNumeroRecogida() {
        return numeroRecogida;
    }

    public void setNumeroRecogida(String numeroRecogida) {
        this.numeroRecogida = numeroRecogida;
    }

    public String getTrasmitido() {
        return trasmitido;
    }

    public void setTrasmitido(String trasmitido) {
        this.trasmitido = trasmitido;
    }

    public List<Vst_ProductosPorFactura> getListaDetalleFactura() {
        return listaDetalleFactura;
    }

    public void setListaDetalleFactura(List<Vst_ProductosPorFactura> listaDetalleFactura) {
        this.listaDetalleFactura = listaDetalleFactura;
    }

    public String getNombreMovimiento() {
        return nombreMovimiento;
    }

    public void setNombreMovimiento(String nombreMovimiento) {
        this.nombreMovimiento = nombreMovimiento;
    }

    public List<CBitacoraFacturas> getListaDeMovimientosBitacora() {
        return listaDeMovimientosBitacora;
    }

    public void setListaDeMovimientosBitacora(List<CBitacoraFacturas> listaDeMovimientosBitacora) {
        this.listaDeMovimientosBitacora = listaDeMovimientosBitacora;
    }

    public List<Vst_ProductosPorFacturaDescargados> getListaDeProductosRechazados() {
        return listaDeProductosRechazados;
    }

    public void setListaDeProductosRechazados(List<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados) {
        this.listaDeProductosRechazados = listaDeProductosRechazados;
    }

    public List<Vst_ProductosPorFacturaDescargados> getListaDeProductosEntregados() {
        return listaDeProductosEntregados;
    }

    public void setListaDeProductosEntregados(List<Vst_ProductosPorFacturaDescargados> listaDeProductosEntregados) {
        this.listaDeProductosEntregados = listaDeProductosEntregados;
    }

    public List<Vst_ProductosPorFacturaDescargados> getListaDeProductosConDesccuento() {
        return listaDeProductosConDesccuento;
    }

    public void setListaDeProductosConDesccuento(List<Vst_ProductosPorFacturaDescargados> listaDeProductosConDesccuento) {
        this.listaDeProductosConDesccuento = listaDeProductosConDesccuento;
    }

    public List<Vst_FacturasPorManifiesto> getListaDeMovimientosfactura() {
        return listaDeMovimientosfactura;
    }

    public void setListaDeMovimientosfactura(List<Vst_FacturasPorManifiesto> listaDeMovimientosfactura) {
        this.listaDeMovimientosfactura = listaDeMovimientosfactura;
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

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public Vst_Factura() {

    }

    public Vst_Factura(Inicio ini) {
        this.ini = ini;
    }

    public Vst_Factura(Inicio ini, String numeroFactura) {
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        this.ini = ini;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT * "
                    + "FROM  view_facturas "
                    + "where numeroFactura='" + numeroFactura + "';";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                llenarDatos(rst);
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            try {
                System.out.println("Error en consultar factura " + ex.getMessage().toString());
                Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public Vst_Factura(Inicio ini, String numeroFactura, boolean remoto) {
        this.remoto = remoto;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        this.ini = ini;
        try {

            if (remoto) {
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            } else {
                con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            }

            sql = "SELECT * "
                    + "FROM  view_facturas "
                    + "where numeroFactura='" + numeroFactura + "';";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                llenarDatos(rst);

                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            try {
                System.out.println("Error en consultar vst_factura consulta sql " + ex.getMessage().toString());
                Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    private void llenarDatos(ResultSet rst) throws SQLException {
        if (rst.next()) {
            this.numeroFactura = rst.getString("numeroFactura");
            this.fechaDeVenta = rst.getDate("fechaDeVenta");
            this.idCliente = rst.getString("idCliente");
            this.nitCliente = rst.getString("nitCliente");
            this.nombreDeCliente = rst.getString("nombreDeCliente");
            this.direccionDeCliente = rst.getString("direccionDeCliente");
            this.barrio = rst.getString("barrio");
            this.ciudadCliente = rst.getString("ciudad");
            this.telefonoCliente = rst.getString("telefonoCliente");
            this.emailCliente = rst.getString("emailCliente");
            this.latitud = rst.getString("latitud");
            this.longitud = rst.getString("longitud");
            this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
            this.vendedor = rst.getString("vendedor");
            this.telefonoVendedor = rst.getString("telefonoVendedor"); 
            this.formaDePago = rst.getString("formaDePago");
            //this.pago = rst.getString("pago");
            this.canal = rst.getInt("canal");
            this.nombreCanalDeVenta = rst.getString("nombreCanalDeVenta");
            this.isFree = rst.getInt("isFree");
            this.idEstadoFactura = rst.getInt("estadoFactura");
            this.nombreEstadoFactura = rst.getString("nombreEstadoFactura");
            this.valorFacturaSinIva = rst.getDouble("valorFacturaSinIva");
            this.valorIvaFactura = rst.getDouble("valorIvaFactura");
            this.valorTotalFactura = rst.getDouble("valorTotalFactura");
            this.valorRechazo = rst.getDouble("valorRechazo");
            this.valorDescuento = rst.getDouble("valorDescuento");
            this.valorRecaudado = rst.getDouble("valorTotalRecaudado");
            this.salidasDistribucion = rst.getInt("salidasDistribucion");
            this.pesofactura = rst.getDouble("pesofactura");
            this.numeroDescuento = rst.getString("numeroDescuento");
            this.numeroRecogida = rst.getString("numeroRecogida");
            this.trasmitido = rst.getString("trasmitido");
            this.nombreMovimiento = rst.getString("nombreEstadoFactura");
        }
    }

    /**
     * Método que permite asignr la vista de BBDD con los datos de los productos
     * que tiene la factura dicho manifiesto
     *
     * @param remoto
     */
    public void setListaDetalleFactura(boolean remoto) {

        listaDetalleFactura = new ArrayList<>();
        Connection con = null;
        ResultSet rst = null;
        Statement st = null;

        if (remoto) {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        } else {
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        }

        String sql = " SELECT * "
                + "FROM vst_productosporfactura "
                + "WHERE "
                + "vst_productosporfactura.numeroFactura='" + this.numeroFactura + "' ";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFactura factura N° " + numeroFactura);
                    Vst_ProductosPorFactura pxf = new Vst_ProductosPorFactura();

                    pxf.setConsecutivo(rst.getInt("consecutivo"));
                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setIdCliente(rst.getString("idCliente"));
                    pxf.setZona(rst.getInt("zona"));
                    pxf.setRegional(rst.getInt("regional"));
                    pxf.setAgencia(rst.getInt("agencia"));
                    pxf.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    pxf.setNombreVendedor(rst.getString("nombreVendedor"));
                    pxf.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    pxf.setFormaDePago(rst.getString("FormaDePago"));
                    pxf.setCanal(rst.getInt("canal"));
                    pxf.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    pxf.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    pxf.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    pxf.setValorRechazo(rst.getDouble("valorRechazo"));
                    pxf.setValorDescuento(rst.getDouble("valorDescuento"));
                    pxf.setValorTotalRecaudado(rst.getDouble("valorTotalRecaudado"));
                    pxf.setCodigoProducto(rst.getString("codigoProducto"));
                    pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxf.setCantidad(rst.getInt("cantidad"));
                    pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));
                    pxf.setValorTotalItem(rst.getDouble("valorTotal"));
                    pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));
                    pxf.setValorTotalConIva(rst.getDouble("valorTotalConIva"));

                    listaDetalleFactura.add(pxf);
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void setListaDeMovimientosfactura() {
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            listaDeMovimientosfactura = new ArrayList();
//                sql = "SELECT numeroFactura,fechaDeVenta,cliente,nombreDeCliente,"
//                 + "vendedor,formaDePago,pago,canal,nombreCanalDeVenta,valorFacturaSinIva,"
//                 + "valorIvaFactura,valorTotalFactura,valorRechazo,valorDescuento,valorRecaudado"
//                 + "FROM vst_fcturas"
//                 + "where numeroFactura='" + this.numeroDeFactura + "';";
            sql = "SELECT * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "where numeroFactura='" + this.numeroFactura + "';";

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    Vst_FacturasPorManifiesto fxm = new Vst_FacturasPorManifiesto();

                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setRuta(rst.getInt("idRuta"));
                    fxm.setNombreRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));

                    listaDeMovimientosfactura.add(fxm);

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            try {
                rst.close();
                st.close();
                con.close();
                System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage());
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public void setListaDeProductosRechazados() {

        listaDeProductosRechazados = new ArrayList<>();
        ResultSet rst = null;
        Statement st = null;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        String sql = "SELECT * "
                + "FROM vst_productosporfacturaDescargada "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' AND "
                + "cantidadRechazada > 0 "
                + " order by codigoProducto;";
        if (con != null) {
            try {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFacturaDescargados factura N° " + numeroFactura);
                    Vst_ProductosPorFacturaDescargados pxfd = new Vst_ProductosPorFacturaDescargados();

                    pxfd.setConsecutivoFacturasDescargadas(rst.getInt("ConsecutivoFacturasDescargadas"));
                    pxfd.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    pxfd.setConsecutivoProductosPorFactura(rst.getInt("consecutivoProductosPorFactura"));
                    pxfd.setNumeroFactura(rst.getString("numeroFactura"));
                    pxfd.setValorDescuento(rst.getDouble("descuento"));
                    pxfd.setCantidadRechazada(rst.getDouble("cantidadRechazada"));
                    pxfd.setValorRechazo(rst.getDouble("valorRechazo"));
                    pxfd.setCantidadEntregada(rst.getDouble("cantidadEntregada"));
                    pxfd.setValoTotalRecaudadoItem(rst.getDouble("totalRecaudado"));
                    pxfd.setEntregado(rst.getInt("entregado"));
                    pxfd.setCausalDerechazo(rst.getInt("causalDerechazo"));
                    pxfd.setCodigoProducto(rst.getString("codigoProducto"));
                    pxfd.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxfd.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));

                    listaDeProductosRechazados.add(pxfd);

                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
                }

            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

  

    public boolean anularFacturaSinMovimiento(String observaciones) {
        boolean grabado = false;
        String sql = null;
        try {

            /*grabar en tabla facturas anuladas*/
            sql = "INSERT INTO facturasanuladas "
                    + "(numeroManifiesto,numeroFactura,observacion,activo,usuario,flag)"
                    + "VALUES('0','"
                    + this.numeroFactura + "','"
                    + observaciones + "','"
                    + "1','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "1') "
                    + "on duplicate key update flag='-1';";
            ini.insertarDatosRemotamente(sql);

            /*grabar datos en tabla bitacora facturas -> se creo un  trigger en la BBDD en  la tabla facturas anuladas
                para que dispare la sentencia de la inserccion  en la bitacora de la factura*/
 /*Actualizar tabla remota  facturas en estadofactura=8 */
            sql = "update facturas set estadoFactura='" + this.idEstadoFactura + "',flag ='" + this.salidasDistribucion + "' where numeroFactura='" + this.numeroFactura + "';";
            ini.insertarDatosRemotamente(sql);

            /*Actualizar tabla local  facturas en estadofactura=8*/
            //sql= "update facturas set estadoFactura='" + this.idEstadoFactura + "' where numeroFactura='" + this.numeroFactura + "'';";
            ini.insertarDatosLocalmente(sql);

            grabado = true;

        } catch (Exception ex) {
            Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, sql, ex);
        }

        return grabado;

    }

       
      /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     */
    public void setListaDeMovimientosBitacora() {
        listaDeMovimientosBitacora = new ArrayList<>();

        ResultSet rst = null;
        Statement st = null;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        String sql = " SELECT * "
                + "FROM bitacorafacturas "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' "
                + "order by documento,fechaIng asc";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println(new java.util.Date() + "Cargando  ->  bitacora factura N° " + numeroFactura);

                    CBitacoraFacturas bitacora = new CBitacoraFacturas();

                    bitacora.setIdbitacorafacturas(rst.getInt("idbitacorafacturas"));
                    bitacora.setNumeroDocumento(rst.getString("documento"));
                    bitacora.setNumeroFactura(rst.getString("numeroFactura"));
                    bitacora.setObservacion(rst.getString("movimiento"));
                    bitacora.setFechaIng(rst.getString("fechaIng"));
                    bitacora.setFecha(rst.getDate("fecha"));

                    listaDeMovimientosBitacora.add(bitacora);
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    
      /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     * @param fecha
     * @return 
     */
    public String getListaFacturasBitacora(String fecha) {
 String lista="";
  ResultSet rst = null;
            Statement st = null;
 
        try {
            Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            
            String sql = "SELECT distinct(numeroFactura) "
                    + "FROM bitacoraSalidasFacturas "
                    + "where usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "'"
                    + "and fechaIng>='" + fecha + " 00:00:00 ' "
                    + "and fechaIng<='" + fecha + " 23:59:59 '; ";
            
            if (con != null) {
                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    int i=0;
                    while (rst.next()) {
                        if (i == 0) {
                            lista += rst.getString("numeroFactura");
                        } else {
                            lista += "," + rst.getString("numeroFactura");
                        }
                        i++;
                        
                    }
                    rst.close();
                    st.close();
                    con.close();
                    
                } catch (SQLException ex) {
                    try {
                        Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                        rst.close();
                        st.close();
                        con.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            
        } catch (Exception ex) {
            Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
