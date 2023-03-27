/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarRegistro;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CFacturasPorManifiesto {//extends Inicio {

    int consecutivo;
    String numeroManifiesto;
    String numeroFactura;
    double valorARecaudarFactura;
    double pesoFactura;
    int adherencia;
    int zona;
    int regional;
    int agencia;
    Inicio ini;

    String fechaDistribucion;
    String vehiculo;
    String conductor;
    String nombreConductor;
    String despachador;
    String nombreDespachador;
    int idRuta;
    String nombreDeRuta;
    String tipoRuta;
    String fechaIng;
    Date fechaDeVenta;
    String cliente;
    String ruta;
    String nombreDeCliente;
    String direccionDeCliente;
    String barrio;
    String telefonoCliente;
    String vendedor;
    String formaDePago;
    int idCanal;
    String nombreCanal;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazo;
    double valorDescuento;
    double valorRecaudado;
    int salidasDistribucion;
    String fechaReal;
    int tipoContrato;
    int tipoVehiculo;
    int idCanalDeVenta;
    String nombreCanalDeVenta;
    String usuario;
    int activo;//double valorRecaudar;

    int adherenciaDescargue;
    int idMovimientoFactura;
    String nombreIdmovimiento;
    int idMotivoRechazo;
    String nombreCausalDeDevolucion;

    String tipoDeDEscargue;

    int trasmitido;
    String numeroDescuento;
    String numeroRecogida;
    String latitud;
    String longitud;
    String telefonoVendedor;
    int plazoDias;
    int despachado;
    int flag;
    
    String fechaDespachado;
    String usuariodespachador;
    String vehiculoAsignado;
    int estadoFactura;
    int isFree;
    /*variable que mide el tiempo desde el alistamiento hasta el despacho de la facturas*/
    int minutos;
    Double fpContado;

    Vst_Factura objFactura = null;
    CManifiestosDeDistribucion objManifiesto = null;

    ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    
    
    
    public Double getFpContado() {
        return fpContado;
    }

    public void setFpContado(Double fpContado) {
        this.fpContado = fpContado;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getUsuariodespachador() {
        return usuariodespachador;
    }

    public void setUsuariodespachador(String usuariodespachador) {
        this.usuariodespachador = usuariodespachador;
    }

    public int getDespachado() {
        return despachado;
    }

    public void setDespachado(int despachado) {
        this.despachado = despachado;
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

    public Vst_Factura getObjFactura() {
        return objFactura;
    }

    public void setObjFactura(Vst_Factura objFactura) {
        this.objFactura = objFactura;
    }

    public CManifiestosDeDistribucion getObjManifiesto() {
        return objManifiesto;
    }

    public void setObjManifiesto(CManifiestosDeDistribucion objManifiesto) {
        this.objManifiesto = objManifiesto;
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

    List<CProductosPorFactura> listaProductosPorFactura;

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getValorARecaudarFactura() {
        return valorARecaudarFactura;
    }

    public void setValorARecaudarFactura(Double valorARecaudarFactura) {
        this.valorARecaudarFactura = valorARecaudarFactura;
    }

    public int getAdherencia() {
        return adherencia;
    }

    public void setAdherencia(int adherencia) {
        this.adherencia = adherencia;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public int getIdZona() {
        return zona;
    }

    public void setIdZona(int zona) {
        this.zona = zona;
    }

    public int getIdRegional() {
        return regional;
    }

    public void setIdRegional(int regional) {
        this.regional = regional;
    }

    public int getIdAgencia() {
        return agencia;
    }

    public void setIdAgencia(int agencia) {
        this.agencia = agencia;
    }

    public double getPesoFactura() {
        return pesoFactura;
    }

    public void setPesoFactura(double pesoFactura) {
        this.pesoFactura = pesoFactura;
    }

    public CFacturasPorManifiesto() {

    }

    public CFacturasPorManifiesto(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CFacturasPorManifiesto(Inicio ini, String numeroFactura, String fecha, boolean xx) throws Exception {
        this.ini = ini;
        //String numeroManifiesto = "0";

        ResultSet rst = null;
        Statement st;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasPorManifiesto");

        con = this.ini.getConnRemota();
        String sql = null;

        sql = "SELECT * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' and fechaDistribucion='" + fecha + "' "
                + "ORDER BY fechaIng desc "
                + "limit 1";

        try {

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    asignarPropiedades(rst);

                } else {

                }

                rst.close();
                st.close();
               // con.close();

            }

            grabarBitacoraSalidaFacturas(numeroManifiesto, numeroFactura);
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(HiloListadoDeFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void asignarPropiedades(ResultSet rst) throws SQLException {

        this.consecutivo = rst.getInt("consecutivo");
        this.adherencia = rst.getInt("adherencia");
        this.numeroManifiesto = rst.getString("numeroManifiesto");
        // numeroManifiesto = rst.getString("numeroManifiesto");
        this.fechaDistribucion = rst.getString("fechaDistribucion");
        this.vehiculo = rst.getString("vehiculo"); // correspponde al vehiculo del manifiesto
        this.conductor = rst.getString("conductor");
        this.nombreConductor = rst.getString("nombreConductor");
        this.despachador = rst.getString("despachador");
        this.nombreDespachador = rst.getString("nombreDespachador");
        this.idRuta = rst.getInt("idRuta");
        this.nombreDeRuta = rst.getString("nombreDeRuta");
        this.numeroFactura = rst.getString("numeroFactura");
        this.valorARecaudarFactura = rst.getDouble("valorARecaudarFactura");
        this.fechaIng = rst.getString("fechaIng");
        this.fechaDeVenta = rst.getDate("fechaDeVenta");
        this.cliente = rst.getString("cliente");
        this.nombreDeCliente = rst.getString("nombreDeCliente");
        this.direccionDeCliente = rst.getString("direccionDeCliente");
        this.vendedor = rst.getString("vendedor");
        this.formaDePago = rst.getString("formaDePago");
        this.idCanal = rst.getInt("idCanal");
        this.nombreCanal = rst.getString("nombreCanal");
        this.valorFacturaSinIva = rst.getDouble("valorFacturaSinIva");
        this.valorIvaFactura = rst.getDouble("valorIvaFactura");
        this.valorTotalFactura = rst.getDouble("valorTotalFactura");
        this.valorRechazo = rst.getDouble("valorRechazo");
        this.valorDescuento = rst.getDouble("valorDescuento");
        this.valorRecaudado = rst.getDouble("valorTotalRecaudado");
        this.salidasDistribucion = rst.getInt("salidasDistribucion");
        this.trasmitido = rst.getInt("trasmitido");
        this.pesoFactura = rst.getDouble("pesofactura");
        this.idMovimientoFactura = (rst.getInt("idMovimientoFactura"));
        this.setTipoDeDEscargue();
        this.adherenciaDescargue = 0;
        //this.idTipoDeMovimiento = 1;
        this.nombreIdmovimiento = "NINGUNO";
        //this.causalDeRechazo(1);
        this.nombreCausalDeDevolucion = "NINGUNO";
        this.latitud = rst.getString("latitud");
        this.longitud = rst.getString("longitud");
        this.telefonoVendedor = rst.getString("telefonoVendedor");
        this.plazoDias = rst.getInt("plazoDias");
        this.despachado = rst.getInt("despachado");
        this.despachador = rst.getString("despachador");
        this.fechaDespachado = rst.getString("fechaDespachado");
        this.vehiculoAsignado = rst.getString("telefonoVendedor");
        this.estadoFactura = rst.getInt("estadoFactura");
        this.isFree = rst.getInt("isFree");
        this.fpContado = rst.getDouble("fpContado");

    }

    public CFacturasPorManifiesto(Inicio ini, String numeroFactura) throws Exception {
        this.ini = ini;
        String numeroManifiesto = "0";

        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasPorManifiesto");

         con = this.ini.getConnRemota();
        
        String sql = null;

        sql = "SELECT * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' "
                + "ORDER BY fechaIng desc "
                + "limit 1";

        try {

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    asignarPropiedades(rst);

                } else {
                    this.numeroFactura = null;
                }

            }

            rst.close();
            st.close();
            // con.close();

            if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
                grabarBitacoraSalidaFacturas(numeroManifiesto, numeroFactura);
            }

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public CFacturasPorManifiesto(Inicio ini, String numeroFactura, String numeroManifiesto) throws Exception {
        this.ini = ini;

        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasPorManifiesto");

         con = this.ini.getConnRemota();
         
        String sql = null;

        sql = "SELECT * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' and numeroManifiesto='" + numeroManifiesto + "' "
                + "ORDER BY fechaIng desc "
                + "limit 1";

        try {

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    asignarPropiedades(rst);

                } else {
                    this.numeroFactura = null;
                }

            }

            rst.close();
            st.close();
            // con.close();

            if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
                //grabarBitacoraSalidaFacturas(numeroManifiesto, numeroFactura);
            }

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean grabarFacturasPoManifiesto(ArrayList<String> listadoDeFacturas) {
        boolean grabado = false;
        String sql = null;
        try {
            for (String obj : listadoDeFacturas) {
                try {
                    sql = "INSERT INTO facturaspormanifiesto (numeroManifiesto,"
                            + " numeroFactura,valorARecaudarFactura,pesoFactura,zona, regional, agencia, activo, usuario,"
                            + " flag,despachado,vehiculoAsignado ) VALUES ('"
                            + this.numeroManifiesto + "','"
                            + obj.toString() + "','"
                            + this.fpContado + "','" // valor a recaudar en efectivo
                            + this.pesoFactura + "','"
                            + ini.getUser().getZona() + "','"
                            + ini.getUser().getRegional() + "','"
                            + ini.getUser().getAgencia() + "','1','"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1','"
                            + this.despachado + "','"
                            + this.vehiculoAsignado + "') "
                            + " ON DUPLICATE KEY UPDATE "
                            + "numeroManifiesto='" + this.numeroManifiesto + "',"
                            + "numeroFactura='" + obj.toString() + "',"
                            + "zona='" + ini.getUser().getZona() + "',"
                            + "regional='" + ini.getUser().getRegional() + "',"
                            + "agencia='" + ini.getUser().getAgencia() + "',"
                            + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                            + " flag='-1';";
                    if (ini.insertarDatosRemotamente(sql)) {
                        grabado = true;
                    }
                } catch (Exception ex) {
                    System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en actualizaTMPFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

    }

    public boolean grabarFacturasPoManifiesto() {
        boolean grabado = false;
        String sql = null;
        try {

            try {
                sql = "INSERT INTO facturaspormanifiesto (numeroManifiesto,"
                        + " numeroFactura,valorARecaudarFactura,pesoFactura,adherencia,zona, regional, agencia, activo, usuario,"
                        + " flag,despachado,vehiculoAsignado) VALUES ('"
                        + this.numeroManifiesto + "','"
                        + this.numeroFactura + "','"
                        + this.fpContado + "','" // valor a recaudar de contado
                        + this.pesoFactura + "','"
                        + this.adherencia + "','"
                        + ini.getUser().getZona() + "','"
                        + ini.getUser().getRegional() + "','"
                        + ini.getUser().getAgencia() + "','1','"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1','"
                        + this.despachado + "','"
                        + this.vehiculoAsignado + "') "
                        + " ON DUPLICATE KEY UPDATE "
                        + "numeroManifiesto='" + this.numeroManifiesto + "',"
                        + "numeroFactura='" + this.numeroFactura + "',"
                        + "adherencia='" + this.adherencia + "',"
                        + "zona='" + ini.getUser().getZona() + "',"
                        + "regional='" + ini.getUser().getRegional() + "',"
                        + "agencia='" + ini.getUser().getAgencia() + "',"
                        + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                        + "despachado = '" + this.despachado + "',"
                        + "vehiculoAsignado = '" + this.vehiculoAsignado + "',"
                        + " flag='-1';";

                grabado = ini.insertarDatosRemotamente(sql);

                if (grabado) {
                    sql = "update facturas set flag="
                            + "(select count(numeroFactura) from facturaspormanifiesto "
                            + "where numeroFactura='" + this.numeroFactura + "'), isFree=0,  estadoFactura=1 "
                            + "where "
                            + "numeroFactura='" + this.numeroFactura + "';";
                    ini.insertarDatosLocalmente(sql);
                }

            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

    }

    public boolean grabarFacturasPoManifiestoDescargada() {
        boolean grabado = false;
        String sql = null;
        try {

            sql = "INSERT INTO  facturasdescargadas(consecutivo,numeroManifiesto,adherencia,numeroFactura,valorRechazo,valorDescuento,valorRecaudado,"
                    + "movimientoFactura,motivoRechazo,activo,usuario,flag) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.adherenciaDescargue + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idMovimientoFactura + "','"
                    + this.idMotivoRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + "flag=-1,"
                    + "valorRechazo='" + this.valorRechazo + "',"
                    + "valorDescuento='" + this.valorDescuento + "',"
                    + "valorRecaudado='" + this.valorRecaudado + "',"
                    + "movimientoFactura='" + this.idMovimientoFactura + "',"
                    + "motivoRechazo='" + this.idMotivoRechazo + "',"
                    + "fechaIng= CURRENT_TIMESTAMP, "
                    + "usuario='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "XXX';";

            if (ini.insertarDatosRemotamente(sql)) {

                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                sql = "update facturas set estadoFactura='" + this.idMovimientoFactura + "' where numeroFactura ='" + this.numeroFactura + "';";
                grabado = ini.insertarDatosLocalmente(sql);

                switch (this.idMovimientoFactura) {

                    case 2:

                        grabado = true;

                        break;
                    case 3: // devolucion total
                        for (CProductosPorFactura pxf : listaProductosPorFactura) {
                            pxf.setConsecutivoFactXMfto(consecutivo);
                            // pxf.setConsecutivoProductoPorFactura(pxf.getc)
                            pxf.setValorDescuentoItem(0.0);
                            pxf.setCantidadRechazadaItem(pxf.getCantidad());
                            pxf.setValorRechazoItem(pxf.getValorProductoXCantidad());
                            pxf.setCantidadEntregadaItem(0.0);
                            pxf.setValorTotalLiquidacionItem(0.0);
                            pxf.setEntregado(0);
                            pxf.setCausalDeRechazo(idMotivoRechazo);
                            pxf.setActivo(1);

                       }
                        grabado = grabarDevolucionDeProducto();
                        break;

                    case 4: // devolucion parcial
                        grabado = grabarDevolucionDeProducto();
                        break;

                    case 5:
                        grabado = true;

                        break;

                    case 6:
                        grabado = true;

                        break;
                    case 7:
                        grabado = true;

                        break;
                    case 8:
                        grabado = true;

                        break;

                }

            }

        } catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

    }

    private boolean grabarDevolucionDeProducto() {
        boolean grabado = false;
        String sql = null;
        List<String> listaDetalleFactura = new ArrayList<>();
        try {

            for (CProductosPorFactura pxf : listaProductosPorFactura) {

                if (pxf.cantidadRechazadaItem > 0) {
                    sql = "INSERT INTO productosporfacturadescargados (consecutivoFacturasDescargadas, consecutivoProductosPorFactura, descuento,"
                            + " cantidadRechazada, valorRechazo, cantidadEntregada, totalRecaudado, entregado, causalDeRechazo, activo,  usuario, flag) VALUES ('"
                            + pxf.consecutivoFactXMfto + "','"
                            + pxf.consecutivoProductoPorFactura + "','"
                            + pxf.valorDescuentoItem + "','"
                            + pxf.cantidadRechazadaItem + "','"
                            + pxf.valorRechazoItem + "','"
                            + pxf.cantidadEntregadaItem + "','"
                            + pxf.valorTotalLiquidacionItem + "','"
                            + pxf.entregado + "','"
                            + pxf.causalDeRechazo + "','"
                            + pxf.activo + "','"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1') "
                            + " ON DUPLICATE KEY UPDATE "
                            + " flag='-1';";
                    listaDetalleFactura.add(sql);

                }

            }

            if (listaDetalleFactura.size() > 0) {
                grabado = ini.insertarDatosRemotamente(listaDetalleFactura, "CfacturasPorManifiesto");
            }

        } catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

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

            sql = "INSERT INTO facturaspormanifiesto ("
                    + "numeroManifiesto, numeroFactura, valorARecaudarFactura, pesoFactura,"
                    + "adherencia, zona, regional, agencia, activo,"
                    + " usuario, flag, despachado, fechaDespachado, usuariodespachador,"
                    + "vehiculoAsignado"
                    + ") VALUES ('"
                    + this.numeroManifiesto + "','"
                    + this.numeroFactura + "','"
                    + this.fpContado + "','" // vaalor a recaudar de contado
                    + this.pesoFactura + "','"
                    + this.adherencia + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + "1','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "'-1','"
                    + this.despachado + "','"
                    + this.fechaDespachado + "','"
                    + this.usuariodespachador + "','"
                    + this.vehiculoAsignado + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "numeroManifiesto='" + this.numeroManifiesto + "',"
                    + "numeroFactura='" + this.numeroFactura + "',"
                    + "adherencia='" + this.adherencia + "',"
                    + "zona='" + ini.getUser().getZona() + "',"
                    + "regional='" + ini.getUser().getRegional() + "',"
                    + "agencia='" + ini.getUser().getAgencia() + "',"
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en
     * la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertFcaturasDescargadasSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO  facturasdescargadas(consecutivo,numeroManifiesto,adherencia,numeroFactura,valorRechazo,valorDescuento,valorRecaudado,"
                    + "movimientoFactura,motivoRechazo,activo,usuario,flag) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.adherenciaDescargue + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idMovimientoFactura + "','"
                    + this.idMotivoRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + "flag=-1; ";

            /* si la factura es un rechazo total, se libera para tener la
             posibilidad de sacarla a ruta nuevamente  */
//            if (this.idMovimientoFactura == 2 || this.idMovimientoFactura == 4 || this.idMovimientoFactura == 2 ) {
//                sql += "update facturas set isFree=0, where numeroFactura='" + this.numeroFactura + "';";
//                ini.insertarDatosLocalmente(sql);
//            }
//            
//            if (this.idMovimientoFactura == 3) {
//                sql += "update facturas set isFree=1 where numeroFactura='" + this.numeroFactura + "';";
//                ini.insertarDatosLocalmente(sql);
//            }
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
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

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
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

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
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

    public int getSalidasDistribucion() {
        return salidasDistribucion;
    }

    public void setSalidasDistribucion(int salidasDistribucion) {
        this.salidasDistribucion = salidasDistribucion;
    }

    public String getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(String fechaReal) {
        this.fechaReal = fechaReal;
    }

    public int getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(int tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public int getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(int tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public int getIdCanalDeVenta() {
        return idCanalDeVenta;
    }

    public void setIdCanalDeVenta(int idCanalDeVenta) {
        this.idCanalDeVenta = idCanalDeVenta;
    }

    public String getNombreCanalDeVenta() {
        return nombreCanalDeVenta;
    }

    public void setNombreCanalDeVenta(String nombreCanalDeVenta) {
        this.nombreCanalDeVenta = nombreCanalDeVenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getAdherenciaDescargue() {
        return adherenciaDescargue;
    }

    public void setAdherenciaDescargue(int adherenciaDescargue) {
        this.adherenciaDescargue = adherenciaDescargue;
    }

    public int getIdTipoDeMovimiento() {
        return idMovimientoFactura;
    }

    public void setIdTipoDeMovimiento(int idMovimientoFactura) {
        this.idMovimientoFactura = idMovimientoFactura;
    }

    public int getCausalDeRechazo() {
        return idMotivoRechazo;
    }

    public void setCausalDeRechazo(int idMotivoRechazo) {
        this.idMotivoRechazo = idMotivoRechazo;
    }

    public String getNombreIdmovimiento() {
        return nombreIdmovimiento;
    }

    public void setNombreIdmovimiento(String nombreIdmovimiento) {
        this.nombreIdmovimiento = nombreIdmovimiento;
    }

    public String getNombreCausalDeDevolucion() {
        return nombreCausalDeDevolucion;
    }

    public void setNombreCausalDeDevolucion(String nombreCausalDeDevolucion) {
        this.nombreCausalDeDevolucion = nombreCausalDeDevolucion;
    }

    /**
     * Método que devuelve una cadena con l lista de los campos de la tabla
     * facturas por manifiesto
     *
     * @return una cadena con la lista de los campos
     */
    public String getCadenaConLosCampos() {
        String cadena = null;

        cadena = this.adherencia + ","
                + this.numeroManifiesto + ","
                + this.numeroFactura + ","
                + this.valorARecaudarFactura + ","
                + this.pesoFactura;
        /*
        numeroManifiesto, numeroFactura, valorARecaudarFactura, pesoFactura, 
        adherencia, 
        zona, 
        regional, 
        agencia, 
        activo, 
        fechaIng, 
        usuario, 
        flag, 
        despachado, 
        fechaDespachado, 
        usuariodespachador, 
        vehiculoAsignado
        **/

        return cadena;
    }

    public List<CProductosPorFactura> getListaProductosPorFactura() {
        return listaProductosPorFactura;
    }

    public List<CProductosPorFactura> setListaProductosPorFactura(boolean remoto) {

        listaProductosPorFactura = new ArrayList<>();
        String numerofactura;

        ResultSet rst = null;
        Statement st = null;
         Connection con;

        if (remoto) {
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
             con = this.ini.getConnRemota();
        } else {
           // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = ini.getConnLocal();
        }

        String sql = " SELECT * "
                + "FROM view_productosporfactura "
                + "WHERE "
                + "numeroFactura='" + this.numeroFactura + "' ";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFactura factura N° " + this.numeroFactura);

                    CProductosPorFactura pxf = new CProductosPorFactura();
                    pxf.setConsecutivoFactXMfto(consecutivo); //consecutivo de manifiesto
                    pxf.setConsecutivoProductoPorFactura(rst.getInt("consecutivoProductoPorFactura"));
                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setCodigoProducto(rst.getString("codigoProducto"));
                    pxf.setLinea(rst.getString("linea"));
                    pxf.setPesoProducto(rst.getDouble("pesoProducto"));
                    pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxf.setCantidad(rst.getInt("cantidad"));
                    pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));// sin iva
                    pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));
                    pxf.setValorProductoXCantidad(rst.getDouble("valorProductoXCantidad"));

                    pxf.setIdCliente(rst.getString("idCliente"));
                    pxf.setAgencia(rst.getInt("agencia"));
                    pxf.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    pxf.setNombreVendedor(rst.getString("nombreVendedor"));
                    pxf.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    pxf.setFormaDePago(rst.getString("formaDePago"));
                    pxf.setCanal(rst.getInt("canal"));
                    pxf.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    pxf.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    pxf.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    pxf.setValorRechazoFactura(rst.getDouble("valorRechazoFactura"));
                    pxf.setValorDescuentoFactura(rst.getDouble("valorDescuentoFactura"));
                    pxf.setValorRecaudadoFactura(rst.getDouble("valorRecaudadoFactura"));
                    pxf.setActivo(1);

                    listaProductosPorFactura.add(pxf);
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
        return listaProductosPorFactura;
    }

    public void setListaProductosPorFactura(List<CProductosPorFactura> listaProductosPorFactura) {
        this.listaProductosPorFactura = listaProductosPorFactura;
    }

    public String getTipoDeDEscargue() {
        return tipoDeDEscargue;
    }

    public void setTipoDeDEscargue(String tipoDeDEscargue) {
        this.tipoDeDEscargue = tipoDeDEscargue;
    }

    public void setTipoDeDEscargue() {

        switch (this.getIdTipoDeMovimiento()) {
            case 2:
                if (this.getValorRecaudado() == 0.0) {
                    this.setTipoDeDEscargue("E. T. Cr");

                } else {
                    if (this.getValorDescuento() == 0.0) {
                        this.setTipoDeDEscargue("E. T. Cn");
                    } else {

                        this.setTipoDeDEscargue("E. T. %");
                    }

                }
                break;
            case 3:
                this.setTipoDeDEscargue("D. T.");
                break;
            case 4:
                if (this.getValorRecaudado() == 0.0) {
                    this.setTipoDeDEscargue("E. N. Cr");
                } else {
                    this.setTipoDeDEscargue("E. N. Cn");
                }
                break;
            case 5:
                break;
            case 6:
                this.setTipoDeDEscargue("R. E.");
                break;

            case 7:
                this.setTipoDeDEscargue("N. V.");
                break;

        }
    }

    /**
     * Método que devuelve una cadena con la lista de todos los campos de la
     * tabla.
     *
     * @return una cadena con la lista de los campos
     */
    //@Override
    public String getCadenaConLosCamposParaDescargue() {
        String cadena;

        cadena = this.consecutivo + ","
                + this.numeroManifiesto + ","
                + this.adherenciaDescargue + ","
                + this.numeroFactura + ","
                + this.valorRechazo + ","
                + this.valorDescuento + ","
                + this.valorRecaudado + ","
                + this.idMovimientoFactura + ","
                + this.idMotivoRechazo + ","
                + this.activo;
        return cadena;
    }

    public int getIdMovimientoFactura() {
        return idMovimientoFactura;
    }

    public void setIdMovimientoFactura(int idMovimientoFactura) {
        this.idMovimientoFactura = idMovimientoFactura;
    }

    public int getIdCausalDeRechazo() {
        return idMotivoRechazo;
    }

    public void setIdMotivoRechazo(int idMotivoRechazo) {
        this.idMotivoRechazo = idMotivoRechazo;
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

    public String getFechaDespachado() {
        return fechaDespachado;
    }

    public void setFechaDespachado(String fechaDespachado) {
        this.fechaDespachado = fechaDespachado;
    }

    public String getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(String vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }

    public final void grabarBitacoraSalidaFacturas(String numeroManifiesto, String numeroFactura) {
        try {
            String sql = "INSERT INTO bitacorasalidasfacturas"
                    + "(numeroManifiesto,numeroFactura,verificado,activo,fechaIng,usuario,flag) VALUES "
                    + "('" + numeroManifiesto + "','"
                    + numeroFactura + "','"
                    + "1','"
                    + "1',"
                    + "CURRENT_TIMESTAMP(),'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "1') on duplicate key update flag='-1';";

            new Thread(new HiloGuardarRegistro(ini, sql)).start();

        } catch (Exception ex) {
            Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void actualizarSalidaFacturas() {
        try {
            String sql = "update salidasFacturas "
                    + "set verificado='1', "
                    + "horaDeSalida=current_timestamp(), "
                    + "usuario='prueba'"
                    + "where numeroFactura='" + this.numeroFactura + "' and fecha='2021-06-09';";

            new Thread(new HiloGuardarRegistro(ini, sql)).start();

        } catch (Exception ex) {
            Logger.getLogger(Vst_Factura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void eliminarFacturaDelManifiesto() {
       
        try {
            String sql = "delete from facturaspormanifiesto "
                    + "where  "
                    + "numeroFactura='" + this.numeroFactura + "' and "
                    + "numeroManifiesto='" + this.numeroManifiesto + "' ;";

            ini.insertarDatosRemotamente(sql);

            sql = "update facturas set isFree=1, estadoFactura=1 "
                    + "where numeroFactura='" + this.numeroFactura + "';";

            ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
