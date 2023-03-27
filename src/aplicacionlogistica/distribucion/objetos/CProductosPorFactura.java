/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CProductosPorFactura {//extends Inicio {

    int consecutivoFactXMfto;
    int consecutivo;
    String numeroFactura;
    String idCliente;
    int agencia;
    String nombreDeCliente;
    String nombreVendedor;
    String fechaDeVenta;
    String formaDePago;
    int canal;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazoFactura;
    double valorDescuentoFactura;
    double valorRecaudadoFactura;
    double pesoTotalFactura;
    String codigoProducto;
    String descripcionProducto;
    String linea;
    double pesoProducto;
    double valorTotalItemConIva;
    double valorUnitarioSinIva;
    double valorUnitarioConIva;
    int consecutivoProductoPorFactura;
    double cantidad;
    double valorProductoXCantidad;
    int idZona;
    int idRegional;


    /*variables productos descargados*/
    //int consecutivoFacturasDescargadas;
    double valorDescuentoItem = 0.0;
    double cantidadRechazadaItem = 0.0;
    double valorRechazoItem = 0.0;
    double cantidadEntregadaItem = 0.0;
    double valorTotalLiquidacionItem = 0.0;
    int entregado = 1;
    int causalDeRechazo = 1;
    String nombreCausalDeRechazo;
    double porcentajeDescuento = 0.0;
    String codigoDeBarras;

    int activo;
    private Inicio ini;

    public double getPesoTotalFactura() {
        return pesoTotalFactura;
    }

    public void setPesoTotalFactura(double pesoTotalFactura) {
        this.pesoTotalFactura = pesoTotalFactura;
    }

    public double getValorTotalItemConIva() {
        return valorTotalItemConIva;
    }

    public void setValorTotalItemConIva(double valorTotalItemConIva) {
        this.valorTotalItemConIva = valorTotalItemConIva;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getIdRegional() {
        return idRegional;
    }

    public void setIdRegional(int idRegional) {
        this.idRegional = idRegional;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(String barcode) {
        this.codigoDeBarras = barcode;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public int getConsecutivoFactXMfto() {
        return consecutivoFactXMfto;
    }

    public void setConsecutivoFactXMfto(int consecutivoFactXMfto) {
        this.consecutivoFactXMfto = consecutivoFactXMfto;
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

    public String getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(String fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(String FormaDePago) {
        this.formaDePago = FormaDePago;
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

    public double getValorRechazoFactura() {
        return valorRechazoFactura;
    }

    public void setValorRechazoFactura(double valorRechazoFactura) {
        this.valorRechazoFactura = valorRechazoFactura;
    }

    public double getValorDescuentoFactura() {
        return valorDescuentoFactura;
    }

    public void setValorDescuentoFactura(double valorDescuentoFactura) {
        this.valorDescuentoFactura = valorDescuentoFactura;
    }

    public double getValorRecaudadoFactura() {
        return valorRecaudadoFactura;
    }

    public void setValorRecaudadoFactura(double valorRecaudadoFactura) {
        this.valorRecaudadoFactura = valorRecaudadoFactura;
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

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public double getPesoProducto() {
        return pesoProducto;
    }

    public void setPesoProducto(double pesoProducto) {
        this.pesoProducto = pesoProducto;
    }

    public double getValorUnitarioSinIva() {
        return valorUnitarioSinIva;
    }

    public void setValorUnitarioSinIva(double valorUnitario) {
        this.valorUnitarioSinIva = valorUnitario;
    }

    public double getValorUnitarioConIva() {
        return valorUnitarioConIva;
    }

    public void setValorUnitarioConIva(double valorUnitarioConIva) {
        this.valorUnitarioConIva = valorUnitarioConIva;
    }

    public int getConsecutivoProductoPorFactura() {
        return consecutivoProductoPorFactura;
    }

    public void setConsecutivoProductoPorFactura(int consecutivoProductoPorFactura) {
        this.consecutivoProductoPorFactura = consecutivoProductoPorFactura;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorProductoXCantidad() {
        return valorProductoXCantidad;
    }

    public void setValorProductoXCantidad(double valorProductoXCantidad) {
        this.valorProductoXCantidad = valorProductoXCantidad;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public double getValorDescuentoItem() {
        return valorDescuentoItem;
    }

    public void setValorDescuentoItem(double valorDescuento) {
        this.valorDescuentoItem = valorDescuento;
    }

    public double getCantidadRechazadaItem() {
        return cantidadRechazadaItem;
    }

    public void setCantidadRechazadaItem(double cantidadRechazada) {
        this.cantidadRechazadaItem = cantidadRechazada;
    }

    public double getValorRechazoItem() {
        return valorRechazoItem;
    }

    public void setValorRechazoItem(double valorRechazo) {
        this.valorRechazoItem = valorRechazo;
    }

    public double getCantidadEntregadaItem() {
        return cantidadEntregadaItem;
    }

    public void setCantidadEntregadaItem(double cantidadEntregada) {
        this.cantidadEntregadaItem = cantidadEntregada;
    }

    public double getValorTotalLiquidacionItem() {
        return valorTotalLiquidacionItem;
    }

    public void setValorTotalLiquidacionItem(double totalRecaudadoProducto) {
        this.valorTotalLiquidacionItem = totalRecaudadoProducto;
    }

    public int getEntregado() {
        return entregado;
    }

    public void setEntregado(int entregado) {
        this.entregado = entregado;
    }

    public int getCausalDeRechazo() {
        return causalDeRechazo;
    }

    public void setCausalDeRechazo(int causalDeRechazo) {
        this.causalDeRechazo = causalDeRechazo;
    }

    public String getNombreCausalDeRechazo() {
        return nombreCausalDeRechazo;
    }

    public void setNombreCausalDeRechazo(String nombreCausalDeRechazo) {
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public CProductosPorFactura() {

    }

    public CProductosPorFactura(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CProductosPorFactura(Inicio ini, String prefijo, String numeroFactura, String codigo) throws Exception {
        this.ini = ini;
        String sql = "SELECT DISTINCT r.MATID,"
                + "m.CODIGO as codigoProducto,"
                + " m.DESCRIP as descripcionProducto,"
                + " lm.DESCRIP as linea,"
                + " '0' as valorUnitario,"
                + " '0' as valorUnitarioConIva,"
                + " '1' as isFree,"
                + " COALESCE(m.PESO,'0') as pesoProducto,"
                + " '0' as largoProducto,"
                + " '0' as anchoProducto,"
                + " '0' as altoProducto,"
                + " '1' as activo,"
                + " 'CURRENT_TIMESTAMP()' as fechaIng,"
                + " 'AUTOMATICO' AS usuario,"
                + " '1' as flag,"
                + " COALESCE(m.CODBARRA,'') as barcode,"
                + "  b.CODBARRA as barcode2 "
                + " FROM DEKARDEX r "
                + " join MATERIAL m on m.MATID=r.MATID"
                + " left outer join DEMATBARRA b on b.MATID=r.MATID "
                + " join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                + " join KARDEX k on k.KARDEXID=r.KARDEXID "
                + " AND k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO ='" + numeroFactura + "' and "
                + " b.CODBARRA='" + codigo + "' or  m.codigo='" + codigo + "' );";
        // + " m.GRUPMATID <> '64';";

    }

    public boolean grabarProductosPorFactura(ArrayList<String> facturas) {
        boolean grabado = false;
        String sql = null;
        try {
            for (String obj : facturas) {
                try {
                    sql = "INSERT INTO productosporfactura (factura, codigoProducto, "
                            + "cantidad, valorUnitario, valorTotal, valorUnitarioConIva,"
                            + "valorTotalConIva,activo,usuario, flag) VALUES ('"
                            + this.numeroFactura + "','"
                            + this.codigoProducto + "','"
                            + this.cantidad + "','"
                            + this.valorUnitarioSinIva + "','"
                            + this.valorUnitarioConIva + "','"
                            + this.valorUnitarioConIva + "','"
                            // + this.valorUnitarioTotalConIva + "','"
                            + this.activo + "','"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1')"
                            + "ON DUPLICATE KEY UPDATE "
                            + "factura='" + this.numeroFactura + "',"
                            + "codigoProducto='" + this.codigoProducto + "',"
                            + "cantidad='" + this.cantidad + "',"
                            + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                            + "valorTotal='" + this.valorUnitarioConIva + "',"
                            + "valorUnitarioConIva='" + this.valorUnitarioConIva + "',"
                            //+ "valorTotalConIva='" + this.valorUnitarioTotalConIva + "',"
                            + "activo='" + this.activo + "',"
                            + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                            + " flag='-1';";
                    if (grabado = ini.insertarDatosLocalmente(sql)) {
                        //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
                    }
                } catch (Exception ex) {
                    System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
                }

            }

        } catch (Exception ex) {
            System.out.println("Error en grabarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    public boolean grabarProductosPorFactura() {
        boolean grabado = false;
        String sql = null;
        try {

            try {
                sql = "INSERT INTO productosporfactura (factura, codigoProducto, "
                        + "cantidad, valorUnitario, valorTotal, valorUnitarioConIva,"
                        + "valorTotalConIva,activo,usuario, flag) VALUES ('"
                        + this.numeroFactura + "','"
                        + this.codigoProducto + "','"
                        + this.cantidad + "','"
                        + this.valorUnitarioSinIva + "','"
                        + this.valorUnitarioConIva + "','"
                        + this.valorUnitarioConIva + "','"
                        // + this.valorUnitarioTotalConIva + "','"
                        + this.activo + "','"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','2')  "
                        + "ON DUPLICATE KEY UPDATE "
                        + "factura='" + this.numeroFactura + "',"
                        + "codigoProducto='" + this.codigoProducto + "',"
                        + "cantidad='" + this.cantidad + "',"
                        + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                        + "valorTotal='" + this.valorUnitarioConIva + "',"
                        + "valorUnitarioConIva='" + this.valorUnitarioConIva + "',"
                        // + "valorTotalConIva='" + this.valorUnitarioTotalConIva + "',"
                        + "activo='" + this.activo + "',"
                        + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                        + " flag='-1';";

                if (grabado = ini.insertarDatosLocalmente(sql)) {
                    //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
                }
            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            System.out.println("Error en grabarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    public boolean actualizarProductosPorFactura(int consecutivo) {
        boolean grabado = false;
        String sql = null;
        try {

            sql = "UPDATE  productosporfactura SET "
                    + "factura='" + this.numeroFactura + "',"
                    + "codigoProducto='" + this.codigoProducto + "',"
                    + "cantidad='" + this.cantidad + "',"
                    + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                    + "valorTotal='" + this.valorUnitarioConIva + "',"
                    + "valorUnitarioConIva='" + this.valorUnitarioConIva + "','"
                    //  + "valorTotalConIva='" + this.valorUnitarioTotalConIva + "','"
                    + "activo='" + this.activo + "',"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + " flag='-1' "
                    + "WHERE  "
                    + "consecutivo='" + consecutivo + "';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
            }

        } catch (SQLException ex) {
            System.out.println("Error en actualizarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error en actualizarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarProductosPorFactura(String sql) {
        boolean grabado = false;

        try {

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
                grabado = ini.insertarDatosRemotamente(sql);
            }

        } catch (SQLException ex) {
            System.out.println("Error en actualizarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error en actualizarProductosPorFactura sql " + ex.getMessage() + "\n " + sql);
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public String getCadenaConLosCampos() {
        String cadena;
        cadena = this.consecutivoProductoPorFactura + ","
                + this.numeroFactura + ","
                + this.codigoProducto + ","
                + this.cantidad + ","
                + this.valorUnitarioSinIva + ","
                + this.valorUnitarioConIva + ","
                + this.valorUnitarioConIva + ","
                // + this.valorUnitarioTotalConIva + ","
                + this.activo;

        return cadena;
    }

    public String getCadenaConLosCamposProductoDescargado() {
        String cadena = null;

        cadena = this.consecutivoFactXMfto + ","
                + this.consecutivoProductoPorFactura + ","
                + this.valorDescuentoItem + ","
                + this.cantidadRechazadaItem + ","
                + this.valorRechazoItem + ","
                + this.cantidadEntregadaItem + ","
                + this.valorTotalLiquidacionItem + ","
                + this.entregado + ","
                + this.causalDeRechazo + ","
                + this.activo;
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
            sql = "INSERT INTO productosporfactura ("
                    + "factura, codigoProducto,cantidad, valorUnitario, valorTotal,"
                    + "valorUnitarioConIva,valorTotalConIva,pesoProducto,activo,usuario, flag,"
                    + "descripcionProducto) VALUES ('"
                    + this.numeroFactura + "','"
                    + this.codigoProducto + "','"
                    + this.cantidad + "','"
                    + this.valorUnitarioSinIva + "','"
                    + (this.valorUnitarioSinIva * this.cantidad) + "','"
                    + this.valorUnitarioConIva + "','"
                    + (this.valorUnitarioConIva * this.cantidad) + "','"
                    + this.pesoProducto + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "'-1','"
                    + this.descripcionProducto + "') ON DUPLICATE KEY UPDATE "
                    + "factura='" + this.numeroFactura + "',"
                    + "codigoProducto='" + this.codigoProducto + "',"
                    + "descripcionProducto='" + this.descripcionProducto + "',"
                    + "cantidad='" + this.cantidad + "',"
                    + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                    + "valorTotal='" + (this.valorUnitarioSinIva * this.cantidad) + "',"
                    + "valorUnitarioConIva='" + this.valorUnitarioConIva + "',"
                    + "valorTotalConIva='" + (this.valorUnitarioConIva * this.cantidad) + "',"
                    + "activo='" + this.activo + "',"
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + " flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public boolean actualizarProductoDeLaFactura(String numeroFactura, String codigoProducto, String valorTotalConIva, String cantidad) {
        boolean grabado = false;
        String sql;

        try {

            sql = "update productosporfactura set cantidad='" + cantidad + "', "
                    + "valorTotalConIva='" + valorTotalConIva + "' "
                    + "where  factura='" + numeroFactura + "' "
                    + " and codigoProducto='" + codigoProducto + "';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                if (grabado = ini.insertarDatosRemotamente(sql)) {
                    grabado = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

        return grabado;
    }

    public void asignarValores(Connection con, String sql) throws SQLException {
        Statement st;
        ResultSet rst;
        if (con != null) {

            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {

                this.consecutivoProductoPorFactura = rst.getInt("consecutivoProductoPorFactura");
                this.codigoProducto = rst.getString("codigoProducto");
                this.linea = rst.getString("linea");
                this.descripcionProducto = rst.getString("descripcionProducto");
                this.pesoProducto = rst.getDouble("pesoProducto");
                this.cantidad = rst.getInt("cantidad");
                this.valorUnitarioSinIva = rst.getDouble("valorUnitario");
                this.valorUnitarioConIva = rst.getDouble("valorUnitarioTotal");
                this.valorProductoXCantidad = rst.getDouble("valorProductoXCantidad");

                this.numeroFactura = rst.getString("factura");
                this.idCliente = rst.getString("idCliente");
                this.agencia = rst.getInt("agencia");
                this.nombreDeCliente = rst.getString("nombreDeCliente");
                this.nombreVendedor = rst.getString("nombreVendedor");
                this.fechaDeVenta = rst.getString("fechaDeVenta");
                this.formaDePago = rst.getString("FormaDePago");
                this.canal = rst.getInt("canal");
                this.valorFacturaSinIva = rst.getDouble("valorFacturaSinIva");
                this.valorIvaFactura = rst.getDouble("valorIvaFactura");
                this.valorTotalFactura = rst.getDouble("valorTotalFactura");
                this.valorRechazoFactura = rst.getDouble("valorRechazo");
                this.valorDescuentoFactura = rst.getDouble("valorDescuento");
                this.valorTotalLiquidacionItem = rst.getDouble("valorTotalRecaudado");
                this.activo = rst.getInt("activo");
                this.valorRechazoFactura = 0.0;
                this.valorDescuentoFactura = 0.0;
                this.valorRecaudadoFactura = 0.0;

            }
            rst.close();
            st.close();
            //con.close();
        }
    }

    public String getSentenciaInsertSQLDescargar() {
        String sql = null;
        try {
            sql = "INSERT INTO productosporfacturadescargados (consecutivoFacturasDescargadas, consecutivoProductosPorFactura, descuento,"
                    + " cantidadRechazada, valorRechazo, cantidadEntregada, totalRecaudado, entregado, causalDeRechazo, activo,  usuario, flag) VALUES ('"
                    + this.consecutivoFactXMfto + "','"
                    + this.consecutivoProductoPorFactura + "','"
                    + this.valorDescuentoItem + "','"
                    + this.cantidadRechazadaItem + "','"
                    + this.valorRechazoItem + "','"
                    + this.cantidadEntregadaItem + "','"
                    + this.valorTotalLiquidacionItem + "','"
                    + this.entregado + "','"
                    + this.causalDeRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.getIni().getUser().getNombreUsuario()) + "','1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + " flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

}
