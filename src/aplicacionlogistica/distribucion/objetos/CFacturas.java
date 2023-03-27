/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFacturaDescargados;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public class CFacturas {//extends Inicio {

    private int consecutivo;
    private String numeroDeFactura;
    String fechaDeVenta;
    String codigoDeCliente;
    private String nitCliente;
    private String nombreDeCliente;
    private String clasificacionCliente;
    String direccion;
    String barrio;
    String ciudad;
    String telefono;
    private String emailCliente;
    private String latitud;
    private String longitud;
    private String nombreEstablecimiento;
    String vendedor;
    String formaDePago;
    private String pago;
    int canal;
    private String nombreCanalDeVenta;
    Double valorFacturaSinIva;
    Double valorIvaFactura;
    Double valorTotalFactura;
    Double fpContado; // valor a cobrar  de contado
    Double valorRechazo;
    Double valorDescuento;
    Double valorTotalRecaudado;
    Double valorRecogida;
    ImageIcon imagenFactura;
    String formato;
    int estadoFactura;
    String nombreEstadoFactura;
    int salidasDistribucion;
    int trasmitido;
    int activoFactura;
    int zona;
    int regional;
    int agencia;
    int isFree;
    int flag;
    int control = 0;
    String numeroDescuento;
    String numeroRecogida;
    Double pesofactura;
    String ruta;
    int trasmitio;
    int plazoDias;
    String telefonoVendedor;
    String nombreMovimiento;
    String prefijo;
    String numero;
    String observaciones;
    String fechaIng;
    String nombreDeConductor;

    Inicio ini;
    private File fotografia;

    List<CProductosPorFactura> listaDetalleFactura;

    List<CProductosPorFactura> listaProductosPorFactura;
    List<CFacturasPorManifiesto> listaDeMovimientosfactura;
    List<CBitacoraFacturas> listaDeMovimientosBitacora;

    List<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    List<Vst_ProductosPorFacturaDescargados> listaDeProductosEntregados;

    // List<Vst_ProductosPorFacturaDescargados> vstlistaDeProductosRechazados;
    //List<Vst_ProductosPorFacturaDescargados> vstlistaDeProductosEntregados;
    //List<Vst_ProductosPorFacturaDescargados> vstlistaDeProductosConDesccuento;
    CClientes cliente;

    public String getNombreDeConductor() {
        return nombreDeConductor;
    }

    public void setNombreDeConductor(String nombreDeComductor) {
        this.nombreDeConductor = nombreDeComductor;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getFpContado() {
        return fpContado;
    }

    public void setFpContado(Double fpContado) {
        this.fpContado = fpContado;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;

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

    public String getNombreMovimiento() {
        return nombreMovimiento;
    }

    public void setNombreMovimiento(String nombreMovimiento) {
        this.nombreMovimiento = nombreMovimiento;
    }

    public String getTelefonoVendedor() {
        return telefonoVendedor;
    }

    public void setTelefonoVendedor(String telefonoVendedor) {
        this.telefonoVendedor = telefonoVendedor;
    }

    public int getPlazoDias() {
        return plazoDias;
    }

    public void setPlazoDias(int plazoDias) {
        this.plazoDias = plazoDias;
    }

    /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     * @param numeroFactura
     * @return listado de los movimientos de la factura
     */
    public List<CBitacoraFacturas> getListaDeMovimientosBitacora(String numeroFactura) {
        listaDeMovimientosBitacora = new ArrayList<>();

        ResultSet rst;
        Statement st;
        Connection con; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = this.ini.getConnRemota();

        String sql = " SELECT * "
                + "FROM bitacorafacturas "
                + "WHERE "
                + "bitacorafacturas.numeroFactura='" + numeroFactura + "' "
                + "order by documento,fechaIng asc";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println(new Date() + "Cargando  ->  bitacora factura N° " + numeroDeFactura);

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
                // con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return listaDeMovimientosBitacora;
    }

    /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     * @return listado de los movimientos de la factura
     */
    public List<CBitacoraFacturas> getListaDeMovimientosBitacora() {
        return listaDeMovimientosBitacora;
    }

    public List<CProductosPorFactura> getListaDetalleFactura() {
        return listaDetalleFactura;
    }

    public void setListaDetalleFactura(List<CProductosPorFactura> listaDetalleFactura) {
        this.listaDetalleFactura = listaDetalleFactura;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public String getClasificacionCliente() {
        return clasificacionCliente;
    }

    public void setClasificacionCliente(String clasificacionCliente) {
        this.clasificacionCliente = clasificacionCliente;
    }

    public int getTrasmitio() {
        return trasmitio;
    }

    public void setTrasmitio(int trasmitio) {
        this.trasmitio = trasmitio;
    }

    public void setEstadoFactura(int estadoFactura) {
        try {
            this.estadoFactura = estadoFactura;
            String sql = "Update facturas set estadoFactura='" + estadoFactura + "' where numeroFactura='" + this.numeroDeFactura + "'";
            //ini.insertarDatosRemotamente(sql);
            ini.insertarDatosLocalmente(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    /**
     * Metodo que asigna los movimientos en la bitacora de la factura
     *
     * @param listaDeMovimientosBitacora array con la lista de los movimentos de
     * la factura
     */
    public void setListaDeMovimientosBitacora(List<CBitacoraFacturas> listaDeMovimientosBitacora) {
        this.listaDeMovimientosBitacora = listaDeMovimientosBitacora;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CClientes getCliente() {
        return cliente;
    }

    public void setCliente(CClientes cliente) {
        this.cliente = cliente;
    }

    public void setCliente() {
        try {
            cliente = new CClientes(ini, codigoDeCliente);

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Double getValorRecogida() {
        return valorRecogida;
    }

    public void setValorRecogida(Double valorRecogida) {
        this.valorRecogida = valorRecogida;
    }

    public List<CFacturasPorManifiesto> getListaDeMovimientosfactura() {
        return listaDeMovimientosfactura;
    }

    public void setListaDeMovimientosfactura(List<CFacturasPorManifiesto> listaDeMovimientosfactura) {
        this.listaDeMovimientosfactura = listaDeMovimientosfactura;
    }

    public void setListaDeMovimientosfactura() {

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = this.ini.getConnRemota();

            listaDeMovimientosfactura = new ArrayList<>();
            sql = "SELECT * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "where numeroFactura='" + this.numeroDeFactura + "';";

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto();

                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));

                    listaDeMovimientosfactura.add(fxm);

                }
                rst.close();
                st.close();
                // con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage());
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(String fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getCodigoDeCliente() {
        return codigoDeCliente;
    }

    public void setCodigoDeCliente(String cliente) {
        this.codigoDeCliente = cliente;
    }

    public String getDireccionDeCliente() {
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

    public String getTelefonoCliente() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Double getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
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

    public ImageIcon getImagenFactura() {
        return imagenFactura;
    }

    public void setImagenFactura(ImageIcon imagenFactura) {
        this.imagenFactura = imagenFactura;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public int getActivoFactura() {
        return activoFactura;
    }

    public void setActivoFactura(int activoFactura) {
        this.activoFactura = activoFactura;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
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

    public List<CProductosPorFactura> getListaCProductosPorFactura() {
        return listaProductosPorFactura;
    }

    public void setListaCProductosPorFactura(List<CProductosPorFactura> arrProductos) {
        this.listaProductosPorFactura = arrProductos;
    }

    /**
     * Metodo trae el detalle de la factura, los productos de la factura
     *
     * la factura
     *
     * @param remoto define con un booleano si trae los datos del servidor
     * remoto o del servidor local
     */
    public void setListaCProductosPorFactura(boolean remoto) {
        listaProductosPorFactura = new ArrayList<>();
        ResultSet rst;
        Statement st;
        Connection con;

        if (remoto) {
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = this.ini.getConnRemota();
        } else {
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = this.ini.getConnLocal();
        }

        String sql = " SELECT * "
                + "FROM vst_productosporfactura "
                + "WHERE "
                + "numeroFactura='" + this.numeroDeFactura + "' ";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    CProductosPorFactura pxf = new CProductosPorFactura(ini);
                    Double pesoFactura = 0.0;

                    pxf.setConsecutivoProductoPorFactura(rst.getInt("consecutivo"));
                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setCodigoProducto(rst.getString("codigoProducto"));
                    pxf.setLinea(rst.getString("linea"));
                    pxf.setPesoProducto(rst.getDouble("pesoProducto"));
                    pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxf.setCantidad(rst.getInt("cantidad"));
                    pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));// sin iva
                    pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));

                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setIdCliente(rst.getString("idCliente"));
                    pxf.setAgencia(rst.getInt("agencia"));
                    pxf.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    pxf.setNombreVendedor(rst.getString("nombreVendedor"));
                    pxf.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    pxf.setFormaDePago(rst.getString("FormaDePago"));
                    pxf.setCanal(rst.getInt("canal"));
                    pxf.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    pxf.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    pxf.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    pxf.setValorRechazoFactura(rst.getDouble("valorRechazo"));
                    pxf.setValorDescuentoFactura(rst.getDouble("valorDescuento"));
                    pxf.setValorRecaudadoFactura(rst.getDouble("valorTotalRecaudado"));
                    pxf.setCodigoDeBarras(rst.getString("barcode"));
                    pxf.setActivo(1);

                    this.listaProductosPorFactura.add(pxf);
                }
                rst.close();
                st.close();
                // con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Metodo trae el detalle de la factura, los productos de la factura con la
     * salvedad que trae los datos del servidor de TNS
     *
     * @param prefijo se refiere al prefijo asignaod por TNS
     * @param factura Ee refiere al campo NUMERO de TNS
     */
    public void setListaCProductosPorFactura(String prefijo, String factura) {
        List<CProductosPorFactura> arrPxF = new ArrayList<>();
        ResultSet rstTNS;
        Statement stTNS;
        Connection conTNS;

        try {
            System.out.println("Inicio de conexion  productos" + new Date());
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            System.out.println("conexion  a servidor TNS exitosa" + new Date());

            String sql = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||'-'||k.NUMERO as numeroFactura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "lm.DESCRIP as linea,"
                    + "SUM(d.CANLISTA) AS cantidad, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANLISTA)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioTotal,"
                    + "(d.PRECIOVTA * SUM(d.CANLISTA)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANLISTA)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID  "
                    + "and k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO='" + factura + "' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                    + "GROUP BY numeroFactura,codigoProducto,descripcionProducto,linea,d.CANLISTA,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by numeroFactura,codigoProducto";

            if (conTNS != null) {
                listaProductosPorFactura = new ArrayList<>();
                try {
                    stTNS = conTNS.createStatement();
                    rstTNS = stTNS.executeQuery(sql);
                    System.out.println("iniciando  -> " + new Date());
                    while (rstTNS.next()) {

                        CProductosPorFactura pxf = new CProductosPorFactura(ini);
                        pesofactura = 0.0;

                        pxf.setConsecutivoProductoPorFactura(0);
                        pxf.setNumeroFactura(rstTNS.getString("numeroFactura"));
                        pxf.setCodigoProducto(rstTNS.getString("codigoProducto"));
                        pxf.setLinea(rstTNS.getString("linea"));
                        pxf.setPesoProducto(rstTNS.getDouble("pesoProducto"));
                        pxf.setDescripcionProducto(rstTNS.getString("descripcionProducto"));
                        pxf.setCantidad(rstTNS.getInt("cantidad"));
                        pxf.setValorUnitarioSinIva(rstTNS.getDouble("valorUnitario"));// sin iva
                        pxf.setValorUnitarioConIva(rstTNS.getDouble("valorUnitarioTotal"));

                        /*pxf.setIdCliente(rstTNS.getString("idCliente"));
                        pxf.setAgencia(rstTNS.getInt("agencia"));
                        pxf.setNombreDeCliente(rstTNS.getString("nombreDeCliente"));
                        pxf.setNombreVendedor(rstTNS.getString("nombreVendedor"));
                        pxf.setFechaDeVenta(rstTNS.getDate("fechaDeVenta"));
                        pxf.setFormaDePago(rstTNS.getInt("FormaDePago"));
                        pxf.setCanal(rstTNS.getInt("canal"));
                        pxf.setValorFacturaSinIva(rstTNS.getDouble("valorFacturaSinIva"));
                        pxf.setValorIvaFactura(rstTNS.getDouble("valorIvaFactura"));
                        pxf.setValorTotalFactura(rstTNS.getDouble("valorTotalFactura"));
                        pxf.setValorRechazoFactura(rstTNS.getDouble("valorRechazo"));
                        pxf.setValorDescuentoFactura(rstTNS.getDouble("valorDescuento"));
                        pxf.setValorRecaudadoFactura(rstTNS.getDouble("valorTotalRecaudado"));
                        pxf.setActivo(1);
                        **/
                        pesofactura += pxf.getPesoProducto();
                        this.listaProductosPorFactura.add(pxf);
                        System.out.println("ciclo " + new Date());
                    }
                    rstTNS.close();
                    stTNS.close();
                    conTNS.close();

                } catch (SQLException ex) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo Constructor vacio
     *
     */
    public CFacturas() {
    }

    /**
     * Metodo Constructor
     *
     * @param ini se refiere a la clase de configuracio Inicio
     */
    public CFacturas(Inicio ini) throws Exception {
        this.ini = ini;

    }

    /**
     * Metodo Constructor
     *
     * @param ini objeto de configuracion del sistema
     * @param numeroFactura
     * @throws java.lang.Exception
     */
    public CFacturas(Inicio ini, String numeroFactura) throws Exception {
        this.ini = ini;

        /* Se ajusta el numero de factura en el caso de que contenga la palabra
        POSC en el prefijo **/
        numeroFactura = numeroFactura.replace("POSC", "PO");

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

            con = this.ini.getConnLocal();
            sql = "SELECT *  "
                    + "FROM view_facturas "
                    + "WHERE  numeroFactura='" + numeroFactura + "';";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                asignarPropiedades(rst);
                rst.close();
                st.close();
                // con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage());
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void asignarPropiedades(ResultSet rst) throws SQLException {
        if (rst.next()) {

            this.numeroDeFactura = rst.getString("numeroFactura");
            this.fechaDeVenta = rst.getString("fechaDeVenta");
            this.codigoDeCliente = rst.getString("idCliente");
            this.nitCliente = rst.getString("nitCliente");
            this.nombreDeCliente = rst.getString("nombreDeCliente");
            this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
            this.direccion = rst.getString("direccionDeCliente");
            this.clasificacionCliente = rst.getString("clasificacionCliente");
            this.barrio = rst.getString("barrio");
            this.ciudad = rst.getString("ciudad");
            this.telefono = rst.getString("telefonoCliente");
            this.vendedor = rst.getString("vendedor");
            this.formaDePago = rst.getString("formaDePago");
            this.canal = rst.getInt("canal");
            this.valorFacturaSinIva = rst.getDouble("valorFacturaSinIva");
            //this.valorIvaFactura = rst.getDouble("valorIvaFactura");
            this.valorTotalFactura = rst.getDouble("valorTotalFactura");
            this.valorRechazo = rst.getDouble("valorRechazo");
            this.valorDescuento = rst.getDouble("valorDescuento");
            this.valorTotalRecaudado = rst.getDouble("valorTotalRecaudado");
            this.telefonoVendedor = rst.getString("telefonoVendedor");
            this.zona = rst.getInt("zona");
            this.regional = rst.getInt("regional");
            this.agencia = rst.getInt("agencia");
            this.isFree = rst.getInt("isFree");
            this.activoFactura = rst.getInt("activo");
            this.estadoFactura = rst.getInt("estadoFactura");
            this.nombreEstadoFactura = rst.getString("nombreEstadoFactura");
            this.numeroDescuento = rst.getString("numeroDescuento");
            this.numeroRecogida = rst.getString("numeroRecogida");
            this.pesofactura = rst.getDouble("pesofactura");
            this.trasmitido = rst.getInt("trasmitido");
            this.nombreMovimiento = rst.getString("nombreEstadoFactura");
            this.plazoDias = rst.getInt("plazoDias");
            this.prefijo = rst.getString("prefijo");
            this.numero = rst.getString("numero");
            this.fpContado = rst.getDouble("fpContado");
            this.observaciones = rst.getString("observaciones");

        } else {
            this.numeroDeFactura = null;

        }
    }

    /**
     * Metodo Constructor con parametros del servidor TNS
     *
     * @param ini objeto de configuracion del sistema
     * @param prefijo se refiere al campo prefijo de la tabla Kardex en TNS
     * @param numeroFactura se refiere al campo numero de la tabla Kardex en TNS
     * @throws java.lang.Exception
     */
    public CFacturas(Inicio ini, String prefijo, String numeroFactura) throws Exception {
        this.ini = ini;
        try {
            Connection conTNS = null;
            Statement statementTNS = null;
            String sql;
            ResultSet rstTNS = null;
            System.out.println("Inicio de conexion " + new Date());
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());

            System.out.println("conexion exitosa " + new Date());

            sql = getSelectFacturasTNS()
                    + "join TERCEROS t on  r.cliente = t.terid "
                    + "join ZONAS Z on Z.ZONAID = t.ZONA1 "
                    + "join TERCEROS t2 on r.CODVEN=t2.nit "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "where  "
                    + "r.CODPREFIJO ='" + prefijo + "' AND r.CODCOMP='FV' and r.NUMERO ='" + numeroFactura + "';";

            if (conTNS != null) {
                statementTNS = conTNS.createStatement();
                rstTNS = statementTNS.executeQuery(sql);
                if (rstTNS.next()) {

                    this.numeroDeFactura = rstTNS.getString("numeroFactura");
                    this.fechaDeVenta = rstTNS.getString("fechaDeVenta");
                    this.codigoDeCliente = rstTNS.getString("idCliente");
                    this.nitCliente = rstTNS.getString("nitCliente");
                    this.nombreDeCliente = rstTNS.getString("nombreDeCliente");
                    this.nombreEstablecimiento = rstTNS.getString("nombreEstablecimiento");

                    /*Se evalua la direccion de la entrega del pedido */
                    if (rstTNS.getString("observaciones") != null) {
                        if (rstTNS.getString("observaciones").contains("ENTREGAR EN:")) {
                            String cadena[] = rstTNS.getString("observaciones").split(":");
                            this.direccion = cadena[1].trim();

                        } else {
                            this.direccion = rstTNS.getString("direccionDeCliente");

                        }
                    } else {
                        this.direccion = rstTNS.getString("direccionDeCliente");
                    }
                    this.clasificacionCliente = "1";
                    this.barrio = rstTNS.getString("barrio");
                    this.ciudad = rstTNS.getString("ciudad");
                    this.telefono = rstTNS.getString("telefonoCliente");
                    if (rstTNS.getInt("plazoDias") > 1) {
                        this.formaDePago = "CREDITO";
                    } else {
                        this.formaDePago = "CONTADO','";
                    }
                    this.vendedor = rstTNS.getString("vendedor");
                    //this.formaDePago = 1;
                    this.canal = 1;
                    this.valorFacturaSinIva = rstTNS.getDouble("valorFacturaSinIva");
                    this.valorIvaFactura = rstTNS.getDouble("valorIvaFactura");
                    this.valorTotalFactura = rstTNS.getDouble("valorTotalFactura");
                    this.valorRechazo = rstTNS.getDouble("valorRechazo");
                    this.valorTotalRecaudado = 0.0;
                    this.valorDescuento = rstTNS.getDouble("valorDescuento");
                    this.zona = rstTNS.getInt("zona");
                    this.regional = rstTNS.getInt("regional");
                    this.agencia = rstTNS.getInt("agencia");
                    this.isFree = rstTNS.getInt("isFree");
                    this.activoFactura = rstTNS.getInt("activo");
                    this.estadoFactura = rstTNS.getInt("estadoFactura");
                    this.nombreEstadoFactura = rstTNS.getString("nombreEstadoFactura");
                    this.numeroDescuento = rstTNS.getString("numeroDescuento");
                    this.numeroRecogida = rstTNS.getString("numeroRecogida");
                    this.pesofactura = rstTNS.getDouble("pesofactura");
                    this.trasmitido = rstTNS.getInt("trasmitido");
                    this.plazoDias = rstTNS.getInt("plazoDias");
                    this.prefijo = rstTNS.getString("prefijo");
                    this.numero = rstTNS.getString("numero");
                    this.fpContado = rstTNS.getDouble("fpContado");
                    this.observaciones = rstTNS.getString("observaciones");
                    this.fechaIng = rstTNS.getString("fechaIng");

                } else {
                    this.numeroDeFactura = null;
                }
                rstTNS.close();
                statementTNS.close();
                conTNS.close();

            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar CFacturas consulta sql " + ex.getMessage());
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo Constructor
     *
     * @param ini objeto de configuracion del sistema
     * @param numeroFactura
     * @param esRemoto booena que define si la data viene del servidor remoto o
     * del servidor local
     * @throws java.lang.Exception
     */
    public CFacturas(Inicio ini, String numeroFactura, boolean esRemoto) throws Exception {
        this.ini = ini;
        /* Se ajusta el numero de factura en el caso de que contenga la palabra
        POSC en el prefijo **/
        numeroFactura = numeroFactura.replace("POSC", "PO");

        if (esRemoto) {
            try {
                Connection con;
                Statement st;
                String sql;
                ResultSet rst;
                // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                con = this.ini.getConnRemota();

                sql = "SELECT *  "
                        + "FROM view_facturas "
                        + "WHERE  numeroFactura='" + numeroFactura + "';";

                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    asignarPropiedades(rst);
                    rst.close();
                    st.close();
                    // con.close();
                }

            } catch (SQLException ex) {
                System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage());
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Metodo que permite grabar las factura en el servidor
     *
     * @return devuelve true si graba los datos, false si hubo errr al guardar
     */
    public boolean grabarFactura() {
        boolean grabado = false;
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String fechaVenta;
            fechaVenta = fecha.format(this.fechaDeVenta);

            String sql = "INSERT INTO facturas (numeroFactura, fechaDeVenta, cliente,direccion,barrio,ciudad,telefono, vendedor,canal, valorFacturaSinIva,"
                    + " valorIvaFactura, valorTotalFactura, valorRechazo, valorDescuento, valorTotalRecaudado,"
                    + " zona,regional,agencia,activo, usuario, flag,observaciones) VALUES ('"
                    + this.numeroDeFactura + "','"
                    + fechaVenta + "','" /*------------------------------------->corresponde a la fecha de venta*/
                    + this.codigoDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.telefono + "','"
                    + this.vendedor + "','"
                    + this.canal + "','"
                    + this.valorFacturaSinIva + "','"
                    + this.valorIvaFactura + "','"
                    + this.valorTotalFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorTotalRecaudado + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + ini.getCodigoAgencia() + "','"
                    + this.activoFactura + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','0','"
                    + this.observaciones + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "cliente='" + this.codigoDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    + "telefono='" + this.telefono + "',"
                    + "vendedor='" + this.vendedor + "',"
                    + "canal='" + this.canal + "',"
                    + " valorFacturaSinIva='" + this.valorFacturaSinIva + "',"
                    + " valorIvaFactura='" + this.valorIvaFactura + "',"
                    + "valorRechazo='" + this.valorRechazo + "',"
                    + " valorDescuento='" + this.valorDescuento + "',"
                    + " valorTotalRecaudado='" + this.valorTotalRecaudado + "',"
                    + " formato='" + this.formato + "',"
                    + " activo='" + this.activoFactura + "',"
                    + " zona='" + this.zona + "',"
                    + " regional='" + this.regional + "',"
                    + " agencia='" + ini.getCodigoAgencia() + "',"
                    + " activo='" + this.activoFactura + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                grabado = ini.insertarDatosRemotamente(sql);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarFacturas 1 sql " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarFacturas 2 sql " + ex.getMessage());
        }
        return grabado;

    }

    /**
     * Método que graba registro cuando es importado de un archivo, para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una true si es grabado, false cuando no es grabado
     */
    public boolean grabarFacturasDesdeArchivo_() {
        boolean grabado = false;
        try {

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String fechax;
            fechax = fecha.format(this.fechaDeVenta);

            String sql = "INSERT INTO facturas (numeroFactura, fechaDeVenta, cliente,direccion,barrio,ciudad,telefono, vendedor,canal, valorFacturaSinIva,"
                    + " valorIvaFactura, valorTotalFactura, valorRechazo, valorDescuento, valorTotalRecaudado,"
                    + " zona,regional,agencia,activo, usuario, flag) VALUES ('"
                    + this.numeroDeFactura + "','"
                    + fechax + "','" /*--------------------------------------------------------------> fechadeVenta */
                    + this.codigoDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.telefono + "','"
                    + this.vendedor + "','"
                    + this.canal + "','"
                    + this.valorFacturaSinIva + "','"
                    + this.valorIvaFactura + "','"
                    + this.valorTotalFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorTotalRecaudado + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + ini.getCodigoAgencia() + "','"
                    + this.activoFactura + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','0') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "activo='1';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarFacturas 1 sql " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarFacturas 2 sql " + ex.getMessage());
        }
        return grabado;

    }

    /**
     * Método que actualiza la base de datos en la tabla CFacturas
     *
     * @return devuelve true si graba los datos, y falso en el caso contrario
     */
    public boolean actualizarFactura() {
        boolean grabado = false;
        try {

            String sql = "UPDATE  facturas SET "
                    + "cliente='" + this.codigoDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    + "telefono='" + this.telefono + "',"
                    + "vendedor='" + this.vendedor + "',"
                    + "formaDePago='" + this.formaDePago + "',"
                    + "canal='" + this.canal + "',"
                    + " valorFacturaSinIva='" + this.valorFacturaSinIva + "',"
                    + " valorIvaFactura='" + this.valorIvaFactura + "',"
                    + "valorRechazo='" + this.valorRechazo + "',"
                    + " valorDescuento='" + this.valorDescuento + "',"
                    + " valorTotalRecaudado='" + this.valorTotalRecaudado + "',"
                    + " formato='" + this.formato + "',"
                    + " activo='" + this.activoFactura + "',"
                    + " isFree='" + this.isFree + "',"
                    + " zona='" + ini.getUser().getZona() + "',"
                    + " regional='" + ini.getUser().getRegional() + "',"
                    + " agencia='" + ini.getCodigoAgencia() + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',activo='1' "
                    + "WHERE  "
                    + "numeroFactura='" + this.numeroDeFactura + "';";
            if (grabado = ini.insertarDatosLocalmente(sql)) {

                if (grabado = ini.insertarDatosRemotamente(sql)) {

                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en actualizarFactura 1 sql " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en actualizarFactura 2 sql " + ex.getMessage());
        }
        return grabado;
    }

    public String arrListadoDeProductosEnFactura() {
        String sql = " SELECT consecutivo, factura, codigoProducto, cantidad, valorUnitario, valorTotal, valorUnitarioConIva, valorTotalConIva, activo, fechaIng, usuario, flag "
                + "FROM productosporfactura,facturas "
                + "WHERE "
                + "productosporfactura.factura = facturas.numeroFactura and "
                + "productosporfactura.factura='" + this.numeroDeFactura + "'";

        return sql;

    }

    private boolean insertarFofografia(File foto) {
        boolean grab;
        this.fotografia = foto;
        try {
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = this.ini.getConnLocal();

            if (fotografia != null) {
                String extension = fotografia.getAbsolutePath().substring(fotografia.getAbsolutePath().lastIndexOf("."));
                Statement sentencia = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "select * from facturas  where numeroFactura='" + this.numeroDeFactura + "';";
                ResultSet rs;
                rs = sentencia.executeQuery(sql2);
                con.setAutoCommit(false);
                PreparedStatement pst;
                pst = con.prepareStatement("Update facturas set imagenFactura=?, formato=? where numeroFactura=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                while (rs.next()) {
                    //File file = new File(fotografia.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(fotografia);
                    pst.setBinaryStream(1, fis, fotografia.length());
                    pst.setString(2, extension);
                    pst.setString(3, rs.getString(1));
                    pst.executeUpdate();
                    con.commit();
                }

                pst.close();
                rs.close();
                //con.close();

            }

            grab = true;

            // con.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }

    public void liberarFactura(boolean liberar) {
        try {
            String sql;
            if (liberar) {
                sql = "UPDATE facturas set isFree=1  WHERE facturas.numeroFactura='" + this.numeroDeFactura + "'";
                this.isFree = 1;
            } else {
                sql = "UPDATE facturas set isFree=0 WHERE facturas.numeroFactura='" + this.numeroDeFactura + "' ";
                this.isFree = 0;
            }

            ini.insertarDatosLocalmente(sql);
            //ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en  setIsFree(int isFree)  sql " + ex.getMessage());
        }
    }

    public boolean liberarFacturas(List<String> list) {
        boolean ok = false;
        try {
            String sql, cadena = "";
            sql = "UPDATE facturas set isFree=1  WHERE numeroFactura in('";
            for (String obj : list) {
                cadena += obj + "','";
            }
            cadena = cadena.substring(0, cadena.length() - 2) + ");";
            sql = sql + cadena;

            ok = ini.insertarDatosLocalmente(sql);
            //ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en  setIsFree(int isFree)  sql " + ex.getMessage());
        }
        return ok;
    }

    /**
     * Método que devuelve una cadena con l lista de los campos de la tabla
     * facturas por manifiesto
     *
     * @return una cadena con la lista de los campos
     */
    public String getCadenaConLosCampos() {
        String cadena = this.numeroDeFactura + ","
                + this.fechaDeVenta + ","
                + this.codigoDeCliente + ","
                + this.direccion + ","
                + this.barrio + ","
                + this.ciudad + ","
                + this.telefono + ","
                + this.vendedor + ","
                + this.formaDePago + ","
                + this.canal + ","
                + this.valorFacturaSinIva + ","
                + this.valorIvaFactura + ","
                + this.valorTotalFactura + ","
                + this.valorRechazo + ","
                + this.valorDescuento + ","
                + this.valorTotalRecaudado + ","
                + this.imagenFactura + ","
                + this.formato + ","
                + this.zona + ","
                + this.regional + ","
                + this.agencia + ","
                + this.isFree + ","
                + this.activoFactura + ","
                + this.observaciones + ","
                + this.fechaIng;

        return cadena;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {
            sql = "INSERT INTO facturas (numeroFactura,fechaDeVenta,cliente,direccion,barrio,"
                    + "ciudad,telefono,vendedor,formaDePago,canal,"
                    + "valorFacturaSinIva,valorIvaFactura,valorTotalFactura,valorRechazo,valorDescuento,"
                    + "valorTotalRecaudado,zona,regional,agencia,isFree,"
                    + "estadoFactura,activo,usuario,flag,pesofactura,"
                    + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,"
                    + "plazoDias,prefijo,numero,fpContado,observaciones) "
                    + "VALUES ('"
                    + this.numeroDeFactura + "','"
                    + this.fechaDeVenta + "','" /*---------------------------------------------------------> fechaDeVenta*/
                    + this.codigoDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.telefono + "','"
                    + this.vendedor + "','"
                    + this.formaDePago + "','"
                    + this.canal + "','"
                    + this.valorFacturaSinIva + "','"
                    + this.valorIvaFactura + "','"
                    + this.valorTotalFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorTotalRecaudado + "','"
                    + ini.getIdZona() + "','"
                    + ini.getIdRegional() + "','"
                    + ini.getIdAgencia() + "','"
                    + this.isFree + "','"
                    + this.estadoFactura + "','"
                    + this.activoFactura + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario())
                    + "','0','"
                    + this.pesofactura + "','"
                    + this.ruta + "','"
                    + this.trasmitido + "','"
                    + this.numeroDescuento + "','"
                    + this.numeroRecogida + "','"
                    + this.telefonoVendedor + "','"
                    + this.plazoDias + "','"
                    + this.prefijo + "','"
                    + this.numero + "','"
                    + this.fpContado + "','"
                    + this.observaciones + "') "
                    + "ON DUPLICATE KEY UPDATE "
                    + "cliente='" + this.codigoDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    + "telefono='" + this.telefono + "',"
                    + "valorFacturaSinIva='" + this.valorFacturaSinIva + "',"
                    + "valorIvaFactura='" + this.valorIvaFactura + "',"
                    + "valorTotalFactura='" + this.valorTotalFactura + "', "
                    + "pesofactura='" + this.pesofactura + "', "
                    + "ruta='" + this.ruta + "', "
                    + "activo='1';";

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNumeroDeFactura() {
        return numeroDeFactura;
    }

    public void setNumeroDeFactura(String numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
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

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
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

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getNombreCanalDeVenta() {
        return nombreCanalDeVenta;
    }

    public void setNombreCanalDeVenta(String nombreCanalDeVenta) {
        this.nombreCanalDeVenta = nombreCanalDeVenta;
    }

    public String getNombreEstadoFactura() {
        return nombreEstadoFactura;
    }

    public void setNombreEstadoFactura(String nombreEstadoFactura) {
        this.nombreEstadoFactura = nombreEstadoFactura;
    }

    public int getSalidasDistribucion() {
        return salidasDistribucion;
    }

    public void setSalidasDistribucion(int salidasDistribucion) {
        this.salidasDistribucion = salidasDistribucion;
    }

    public int getTrasmitido() {
        return trasmitido;
    }

    public void setTrasmitido(int trasmitido) {
        this.trasmitido = trasmitido;
    }

    public String getNumeroDescuento() {
        return numeroDescuento;
    }

    public void setNumeroDescuento(String numeroDescuento) {
        this.numeroDescuento = numeroDescuento;
    }

    public String getNumeroRecogida() {
        return numeroRecogida;
    }

    public void setNumeroRecogida(String numeroRecogida) {
        this.numeroRecogida = numeroRecogida;
    }

    public Double getPesofactura() {
        return pesofactura;
    }

    public void setPesofactura(Double pesofactura) {
        this.pesofactura = pesofactura;
    }

    public File getFotografia() {
        return fotografia;
    }

    public void setFotografia(File fotografia) {
        this.fotografia = fotografia;
    }

    /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     * @param fecha
     * @return
     */
    public String getListaFacturasBitacora(String fecha) {
        String lista = "";
        ResultSet rst = null;
        Statement st = null;

        try {
            Connection con; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = this.ini.getConnRemota();

            String sql = "SELECT distinct(numeroFactura) "
                    + "FROM bitacorasalidasfacturas "
                    + "where usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "'"
                    + "and fechaIng>='" + fecha + " 00:00:00 ' "
                    + "and fechaIng<='" + fecha + " 23:59:59 '; ";

            if (con != null) {
                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    int i = 0;
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
                    //bcon.close();

                } catch (SQLException ex) {
                    try {
                        Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                        rst.close();
                        st.close();
                        //con.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
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
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = this.ini.getConnRemota();
        } else {
            //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = this.ini.getConnLocal();
        }

        String sql = " SELECT * "
                + "FROM vst_productosporfactura "
                + "WHERE "
                + "numeroFactura='" + this.numeroDeFactura + "' ";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFactura factura N° " + numeroDeFactura);
                    CProductosPorFactura pxf = new CProductosPorFactura();

                    pxf.setConsecutivoProductoPorFactura(rst.getInt("consecutivo"));
                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setCodigoProducto(rst.getString("codigoProducto"));
                    pxf.setLinea(rst.getString("linea"));
                    pxf.setPesoProducto(rst.getDouble("pesoProducto"));
                    pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxf.setCantidad(rst.getInt("cantidad"));
                    pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));// sin iva
                    pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));

                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setIdCliente(rst.getString("idCliente"));
                    pxf.setAgencia(rst.getInt("agencia"));
                    pxf.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    pxf.setNombreVendedor(rst.getString("nombreVendedor"));
                    pxf.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    pxf.setFormaDePago(rst.getString("FormaDePago"));
                    pxf.setCanal(rst.getInt("canal"));
                    pxf.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    pxf.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    pxf.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    pxf.setValorRechazoFactura(rst.getDouble("valorRechazo"));
                    pxf.setValorDescuentoFactura(rst.getDouble("valorDescuento"));
                    pxf.setValorRecaudadoFactura(rst.getDouble("valorTotalRecaudado"));
                    pxf.setCodigoDeBarras(rst.getString("barcode"));
                    pxf.setValorTotalItemConIva(rst.getDouble("valorTotalConIva"));
                    pxf.setActivo(1);

                    listaDetalleFactura.add(pxf);
                }
                rst.close();
                st.close();
                // con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Método que permite asignr la vista de BBDD Firebird con los datos de los
     * productos que tiene la factura
     *
     * @param prefijo campo de la tabla kardex de la bbdd firebird
     * @param numeroFactura campo de la tabla kardex de la bbdd firebird
     */
    public void setListaDetalleFactura(String prefijo, String numeroFactura) {
        listaDetalleFactura = new ArrayList<>();
        Connection conTNS = null;
        ResultSet rst = null;
        Statement st = null;
        try {

            String sqlListaDetalleFactura = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||'-'||k.NUMERO as factura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "SUM(d.CANMAT) AS CANTIDAD, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANMAT)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioConIva,"
                    + "(d.PRECIOVTA * SUM(d.CANMAT)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANMAT)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID  and "
                    + "k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO ='" + numeroFactura + "' "
                    // + "and k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and  "a
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY factura,codigoProducto,descripcionProducto,d.CANMAT,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by factura,codigoProducto";

            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());

            if (conTNS != null) {
                try {
                    st = conTNS.createStatement();
                    rst = st.executeQuery(sqlListaDetalleFactura);
                    double pesoFactura = 0.0;
                    while (rst.next()) {

                        System.out.println("Cargando  ->  vstproductosPorFactura factura N° " + numeroDeFactura);
                        CProductosPorFactura pxf = new CProductosPorFactura();

                        pxf.setConsecutivo(0);
                        pxf.setNumeroFactura(this.numeroDeFactura);
                        pxf.setIdCliente(this.codigoDeCliente);
                        pxf.setIdZona(this.zona);
                        pxf.setIdRegional(this.regional);
                        pxf.setAgencia(this.agencia);
                        pxf.setNombreDeCliente(this.nombreDeCliente);
                        pxf.setNombreVendedor(this.vendedor);
                        pxf.setFechaDeVenta(this.fechaDeVenta);
                        pxf.setFormaDePago(this.formaDePago);
                        pxf.setCanal(this.canal);
                        pxf.setValorFacturaSinIva(this.valorFacturaSinIva);
                        pxf.setValorIvaFactura(this.valorIvaFactura);
                        pxf.setValorTotalFactura(this.valorTotalFactura);
                        pxf.setValorRechazoFactura(this.valorRechazo);
                        pxf.setValorDescuentoFactura(this.valorDescuento);
                        pxf.setValorRecaudadoFactura(this.valorTotalRecaudado);
                        pxf.setCodigoProducto(rst.getString("codigoProducto"));
                        pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                        pxf.setCantidad(rst.getInt("cantidad"));
                        pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));
                        pxf.setValorTotalItemConIva(rst.getDouble("valorTotal"));
                        pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));
                        pxf.setPesoProducto(rst.getDouble("pesoProducto"));
                        pesoFactura += pxf.getPesoProducto();
                        pxf.setPesoTotalFactura(pesoFactura);
                        listaDetalleFactura.add(pxf);
                    }
                    this.pesofactura = pesoFactura;
                    rst.close();
                    st.close();
                    conTNS.close();

                } catch (SQLException ex) {
                    try {
                        Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                        rst.close();
                        st.close();
                        conTNS.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo que devuelve los movimientos en la bitacora de la factura
     *
     */
    public void setListaDeMovimientosBitacora() {
        listaDeMovimientosBitacora = new ArrayList<>();

        ResultSet rst = null;
        Statement st = null;
        Connection con; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = this.ini.getConnRemota();

        String sql = " SELECT * "
                + "FROM bitacorafacturas "
                + "WHERE "
                + "numeroFactura='" + numeroDeFactura + "' "
                + "order by documento,fechaIng asc";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println(new java.util.Date() + "Cargando  ->  bitacora factura N° " + numeroDeFactura);

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
                //con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void setListaDeProductosRechazados() {

        listaDeProductosRechazados = new ArrayList<>();
        ResultSet rst = null;
        Statement st = null;
        Connection con; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = this.ini.getConnRemota();

        String sql = "SELECT * "
                + "FROM vst_productosporfacturadescargada "
                + "WHERE "
                + "numeroFactura='" + numeroDeFactura + "' AND "
                + "cantidadRechazada > 0 "
                + " order by codigoProducto;";
        if (con != null) {
            try {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFacturaDescargados factura N° " + numeroDeFactura);
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
                //con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex1);
                }

            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static String getSelectFacturasTNS() {
        String select = "";
        select = "SELECT r.KARDEXID,r.numero,"
                // + "r.CODPREFIJO||'-'||r.NUMERO as numeroFactura,"
                + "r.CODPREFIJO as prefijo,"
                + "r.NUMERO as numero,"
                + "r.CODPREFIJO||r.NUMERO as numeroFactura,"
                + "r.FECHA as fechaDeVenta,"
                + "t.NIT as nitCliente,"
                + "r.CLIENTE as idCliente,"
                + "t.NOMBRE as nombreDeCliente,"
                + "COALESCE(t.ESTABLECIMIENTO,'NA') as nombreEstablecimiento,"
                + "t.DIRECC1 as  direccionDeCliente,"
                + "r.ZONATERCERO as barrio,"
                + "c.NOMBRE as ciudad,"
                + "SUBSTRING(t.TELEF1 FROM 1 FOR 12) as telefonoCliente,"
                + "r.NOMVENDEDOR as vendedor,"
                + "SUBSTRING(t2.TELEF1 FROM 1 FOR 12) as telefonoVendedor,"
                + "'1' as formaDePago,"
                + "'1' as canal,"
                + "r.OBSERV as observaciones,"
                + "r.VRBASE AS valorFacturaSinIva,"
                + "r.VRIVA AS valorIvaFactura,"
                + "r.TOTAL AS valorTotalFactura,"
                + "r.PLAZODIAS AS plazoDias,"
                + "r.FPCONTADO AS fpContado,"
                + "'0' as valorRechazo,"
                + "'0' as valorDescuento,"
                + "'0' as valorTotalRecaudado,"
                //  + "-- imagenFactura,"
                + "'PNG' as formato,"
                + "'1' as zona,"
                + "'1' as regional,"
                + "'1' as agencia,"
                + "'1' as isFree,"
                + "'1' as estadoFactura,"
                + "'TRASMITIDO' as nombreEstadoFactura,"
                + "'1' as activo,'"
                + "CURRENT_TIMESTAMP()' as fechaIng,"
                + "'AUTOMATICO' AS usuario,"
                + "'0' AS flag," // tiene que ser cero por las salidas a distribucion
                + "'0' AS pesofactura,"
                + "r.CODVEN AS ruta,"
                + "'0' AS trasmitido,"
                + "'0' AS numeroDescuento,"
                + "'0' AS numeroRecogida "
                + "FROM KARDEX r ";
        return select;

    }

    public static boolean existeFactura(Connection con, String numeroFactura) {
        boolean encontrado = false;
        ResultSet rst = null;
        Statement st = null;

        String sql = "select numeroFactura from facturas where numeroFactura='" + numeroFactura + "';";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    encontrado = true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return encontrado;
    }

}
