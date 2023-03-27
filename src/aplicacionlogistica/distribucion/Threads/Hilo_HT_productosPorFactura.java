/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Hilo_HT_productosPorFactura implements Runnable {

    Inicio ini;
    CFacturas factura;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param factura
     */
    public Hilo_HT_productosPorFactura(Inicio ini, CFacturas factura) {
        this.factura = factura;
        this.ini = ini;

    }

    @Override
    public void run() {
        try {
           List<CProductosPorFactura>  listaProductosPorFactura = new ArrayList<>();
            Statement st;
            ResultSet rst;

            Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"Hilo_HT_productosPorFactura");
            String sql = " SELECT * "
                    + "FROM vst_productosporfactura "
                    + "WHERE "
                    + "vst_productosporfactura.numeroFactura='" + factura.getNumeroDeFactura() + "' ";

            if (con != null) {
               
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  ->  vstproductosPorFactura factura N° " + factura.getNumeroDeFactura());
                    CProductosPorFactura pxf = new CProductosPorFactura(ini);

                    pxf.setNumeroFactura(rst.getString("numeroFactura"));
                    pxf.setIdCliente(rst.getString("idCliente"));
                   // pxf.setZona(rst.getInt("zona"));
                    //pxf.setRegional(rst.getInt("regional"));
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
                    pxf.setConsecutivoProductoPorFactura(rst.getInt("consecutivo"));
                    pxf.setCodigoProducto(rst.getString("codigoProducto"));
                    pxf.setDescripcionProducto(rst.getString("descripcionProducto"));
                    pxf.setCantidad(rst.getDouble("cantidad"));
                    pxf.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));
                    pxf.setValorTotalLiquidacionItem(rst.getDouble("valorTotal"));
                    pxf.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));
                    pxf.setValorTotalLiquidacionItem(rst.getDouble("valorTotalConIva"));

                    listaProductosPorFactura.add(pxf);

                }
                rst.close();
                st.close();
                con.close();
                this.factura.setControl(1);
                this.factura.setListaCProductosPorFactura(listaProductosPorFactura);

                System.out.println("se asignaron los productos a la  factura # " + factura.getNumeroDeFactura());
            }

        } catch (SQLException ex) {
            System.out.println("Error en  consulta sql " + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(Hilo_HT_productosPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
