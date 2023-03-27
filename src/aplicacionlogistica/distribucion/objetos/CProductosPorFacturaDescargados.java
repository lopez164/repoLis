/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

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
 * @author VLI_488
 */
public class CProductosPorFacturaDescargados extends CProductosPorFactura {

    int consecutivoFacturasDescargadas;
    int consecutivoProductosPorFactura;
    double valorDescuento;
    double cantidadRechazada;
    double valorRechazo;
    double cantidadEntregada;
    double totalRecaudadoProducto;
    int entregado;
    int causalDeRechazo;
    double porcentajeDescuento;

    Inicio ini;
    
    CFacturasPorManifiesto facPorMfto;

    /**
     * Método que devuelve el consecutivo cel producto descargado de la factura
     *
     * @return una entero con el numerode consecutivo asignado en la BBCD
     */
    public int getConsecutivoFacturasDescargadas() {
        return consecutivoFacturasDescargadas;
    }

    public synchronized void setConsecutivoFacturasDescargadas(int consecutivoFacturasDescargadas) {
        this.consecutivoFacturasDescargadas = consecutivoFacturasDescargadas;
    }

    /**
     * Método que devuelve el consecutivo cel producto descargado de la factura
     *
     * @return una entero con el numerode consecutivo asignado en la BBCD
     */
    public int getConsecutivoProductosPorFactura() {
        return consecutivoProductosPorFactura;
    }

    public synchronized void setConsecutivoProductosPorFactura(int consecutivoProductosPorFactura) {
        this.consecutivoProductosPorFactura = consecutivoProductosPorFactura;
    }

    /**
     * Método que devuelve el porcentaje de descuento del producto descargado de
     * la factura
     *
     * @return porcentaje de descuento practicado al producto
     */
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Método que devuelve el valor del descuento del producto descargado de la
     * factura
     *
     * @return valor del descuento
     */
    public double getValorDescuentoFactura() {
        return valorDescuento;
    }

    public void setValorDescuentoFactura(double descuento) {
        this.valorDescuento = descuento;
    }

    /**
     * Método que devuelve el valor del rechazo del producto descargado de la
     * factura
     *
     * @return valor del rechazo
     */
    public double getValorRechazoFactura() {
        return valorRechazo;
    }

    public void setValorRechazoFactura(double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    /**
     * Método que devuelve el valor recaudado del producto descargado de la
     * factura
     *
     * @return valor del descuento
     */
    public double getValorTotalRecaudadoProducto() {
        return totalRecaudadoProducto;
    }

    public void setValorTotalRecaudadoProducto(double totalRecaudadoProducto) {
        this.totalRecaudadoProducto = totalRecaudadoProducto;
    }

    /**
     * Método que devuelve 0= no entregado, 1= entregado
     *
     * @return entero, devuelve 0= no entregado, 1= entregado
     */
    public int getEntregado() {
        return entregado;
    }

    public void setEntregado(int entregado) {
        this.entregado = entregado;
    }

    /**
     * Método que devuelve un entero con la causal de rechazo
     *
     * @return entero con la causal de rechazo
     */
    public int getCausalDeRechazo() {
        return causalDeRechazo;
    }

    public void setCausalDeRechazo(int causalDeRechazo) {
        this.causalDeRechazo = causalDeRechazo;
    }

    /**
     * Método que devuelve cantidad rechaza de producto
     *
     * @return entero con la cantidad de producto rechazado
     */
    public double getCantidadRechazadaItem() {
        return cantidadRechazada;
    }

    public void setCantidadRechazadaItem(double cantidadRechazada) {
        this.cantidadRechazada = cantidadRechazada;
    }

    /**
     * Método que devuelve cantidad entregada de producto
     *
     * @return entero con la cantidad de producto entregada
     */
    public double getCantidadEntregadaItem() {
        return cantidadEntregada;
    }

    public void setCantidadEntregadaItem(double cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public CProductosPorFacturaDescargados() {

    }

    public CProductosPorFacturaDescargados(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CProductosPorFacturaDescargados(Inicio ini, CFacturasPorManifiesto facPorMfto) throws Exception {
        this.ini = ini;
        this.facPorMfto = facPorMfto;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
          // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(),"CProductosPorFacturaDescargados");

             con = this.ini.getConnLocal();

            
            sql = "SELECT productosporfactura.consecutivo, productosporfactura.factura, productosporfactura.codigoProducto, productosporfactura.cantidad,"
                    + "productosporfactura. valorUnitario, productosporfactura.valorTotal, "
                    + "productosporfacturadescargados.descuento, productosporfacturadescargados.valorRechazo, productosporfacturadescargados.totalRecaudado, "
                    + "productosporfacturadescargados.entregado, productosporfacturadescargados.causalDeRechazo,productosporfacturadescargados.activo,"
                    + "FROM productosporfactura, productosporfacturadescargados,facturasdescargadas "
                    + "WHERE"
                    + " productosporfactura.consecutivo = productosporfacturadescargados. consecutivo AND "
                    + " productosporfacturadescargados.manifiesto=facturasdescargadas.manifiesto AND "
                    + "productosporfacturadescargados.factura=facturasdescargadas.factura AND "
                    + " and activo=1;";

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {
                    this.consecutivoProductosPorFactura = rst.getInt("consecutivo");
                    this.numeroFactura = rst.getString("factura");
                    this.codigoProducto = rst.getString("codigoProducto");
                    this.cantidad = rst.getDouble("cantidad");
                    this.valorUnitarioSinIva = rst.getDouble("valorUnitario");
                    this.valorUnitarioConIva = rst.getDouble("valorTotal");
                    this.valorDescuento = rst.getDouble("descuento");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.totalRecaudadoProducto = rst.getDouble("totalRecaudado");
                    this.entregado = rst.getInt("entregado");
                    this.causalDeRechazo = rst.getInt("causalDeRechazo");
                    this.activo = rst.getInt("activo");
                } else {
                    this.consecutivoProductosPorFactura = 0;
                    this.numeroFactura = null;
                    this.codigoProducto = null;
                    this.cantidad = 0.0;
                    this.valorUnitarioSinIva = 0.0;
                    this.valorUnitarioConIva = 0.0;
                    this.valorDescuento = 0.0;
                    this.valorRechazo = 0.0;
                    this.totalRecaudadoProducto = 0.0;
                    this.entregado = 0;
                    this.causalDeRechazo = 0;
                    this.activo = 0;
                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage());
            Logger.getLogger(CProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarProductosPorFacturaDescargada() {
        boolean grabado = false;
        String sql = null;
        try {

            try {
                sql = "INSERT INTO `productosporfacturadescargados` (consecutivoFacturasDescargadas, consecutivoProductosPorFactura, descuento,"
                        + " cantidadRechazada, valorRechazo, cantidadEntregada, totalRecaudado, entregado, causalDeRechazo, activo,  usuario, flag) VALUES ('"
                        + this.consecutivoFacturasDescargadas + "','"
                        + this.consecutivoProductosPorFactura + "','"
                        + this.valorDescuento + "','"
                        + this.cantidadRechazada + "','"
                        + this.valorRechazo + "','"
                        + this.cantidadEntregada + "','"
                        + this.totalRecaudadoProducto + "','"
                        + this.entregado + "','"
                        + this.causalDeRechazo + "','"
                        + this.activo + "','"
                        + Inicio.deCifrar(this.getIni().getUser().getNombreUsuario()) + "','1') "
                        + " ON DUPLICATE KEY UPDATE "
                        + "consecutivoFacturasDescargadas='" + this.consecutivoFacturasDescargadas + "',"
                        + "consecutivoProductosPorFactura='" + this.consecutivoProductosPorFactura + "',"
                        + "descuento='" + this.valorDescuento + "',"
                        + "cantidadRechazada='" + this.cantidadRechazada + "',"
                        + "valorRechazo='" + this.valorRechazo + "',"
                        + "cantidadEntregada='" + this.cantidadEntregada + "',"
                        + "totalRecaudado='" + this.totalRecaudadoProducto + "',"
                        + "entregado='" + this.entregado + "',"
                        + "causalDeRechazo='" + this.causalDeRechazo + "',"
                        + "activo='" + this.activo + "',"
                        + "usuario='" + Inicio.deCifrar(this.getIni().getUser().getNombreUsuario()) + "',"
                        + " flag='-1';";
                grabado = getIni().insertarDatosLocalmente(sql);
            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            Logger.getLogger(CProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    public boolean actualizarProductosPorFacturaDescargada(String sql) {
        boolean grabado = false;

        try {

            try {

                grabado = getIni().insertarDatosLocalmente(sql);
                grabado = getIni().insertarDatosRemotamente(sql);

            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            Logger.getLogger(CProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    public String getCadenaConLosCampos() {
        String cadena = null;
        cadena = this.consecutivoFacturasDescargadas + ","
                + this.consecutivoProductosPorFactura + ","
                + this.valorDescuento + ","
                + this.cantidadRechazada + ","
                + this.valorRechazo + ","
                + this.cantidadEntregada + ","
                + this.totalRecaudadoProducto + ","
                + this.entregado + ","
                + this.causalDeRechazo + ","
                + this.activo;
        return cadena;
    }

    @Override
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {
            sql = "INSERT INTO `productosporfacturadescargados` (consecutivoFacturasDescargadas, consecutivoProductosPorFactura, descuento,"
                    + " cantidadRechazada, valorRechazo, cantidadEntregada, totalRecaudado, entregado, causalDeRechazo, activo,  usuario, flag) VALUES ('"
                    + this.consecutivoFacturasDescargadas + "','"
                    + this.consecutivoProductosPorFactura + "','"
                    + this.valorDescuento + "','"
                    + this.cantidadRechazada + "','"
                    + this.valorRechazo + "','"
                    + this.cantidadEntregada + "','"
                    + this.totalRecaudadoProducto + "','"
                    + this.entregado + "','"
                    + this.causalDeRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.getIni().getUser().getNombreUsuario()) + "','1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "consecutivoFacturasDescargadas='" + this.consecutivoFacturasDescargadas + "',"
                    + "consecutivoProductosPorFactura='" + this.consecutivoProductosPorFactura + "',"
                    + "descuento='" + this.valorDescuento + "',"
                    + "cantidadRechazada='" + this.cantidadRechazada + "',"
                    + "valorRechazo='" + this.valorRechazo + "',"
                    + "cantidadEntregada='" + this.cantidadEntregada + "',"
                    + "totalRecaudado='" + this.totalRecaudadoProducto + "',"
                    + "entregado='" + this.entregado + "',"
                    + "causalDeRechazo='" + this.causalDeRechazo + "',"
                    + "activo='" + this.activo + "',"
                    + "usuario='" + Inicio.deCifrar(this.getIni().getUser().getNombreUsuario()) + "',"
                    + " flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

}
