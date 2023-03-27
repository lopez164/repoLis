/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloAsignarFacturas_ProductoAmanifiesto implements Runnable {

    CManifiestosDeDistribucion manifiesto;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param manifiesto
     */
    public HiloAsignarFacturas_ProductoAmanifiesto(Inicio ini) {
        this.ini = ini;
    }

    @Override
    public void run() {

        asignarProductosAFacturas();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAsignarFacturas_ProductoAmanifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void asignarProductosAFacturas() {
        for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {
              
            
            if (manifiesto.getListaFacturasPorManifiesto() == null
                    || manifiesto.getListaFacturasPorManifiesto().isEmpty()) {
                /*Comienza a cargar los productos de las facturas del manifiesto*/
                manifiesto.setListaFacturasPorManifiesto();
                actualizarProductos(manifiesto);

            }
        }
    }

    public void actualizarProductos(CManifiestosDeDistribucion manifiesto) throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        int i=0;
        
        try {
            System.out.print("Manifiesto : # " + manifiesto.getNumeroManifiesto() + "\n");
            
            System.out.print("Aca verifica la conexion \n");
            if (!ini.verificarConexion()) {
                //JOptionPane.showMessageDialog(null, "Error en la conexion a Internet ", " Alerta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.print("entra conexion \n");
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloCargarListaDeProductosFacturasPoManifiesto");
            System.out.print("empieza a Asignar las facturas \n");

            for (CFacturasPorManifiesto factura : manifiesto.getListaFacturasPorManifiesto()) {

                if (factura.getListaProductosPorFactura() == null) {

                    String sql = "SELECT * "
                            + "FROM view_productosPorFactura "
                            + "WHERE "
                            + "numeroFactura='" + factura.getNumeroFactura() + "';";

                    List<CProductosPorFactura> listaproductosPorFactura = new ArrayList();

                    if (con != null) {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);

                        while (rst.next()) {
                            CProductosPorFactura pxf = new CProductosPorFactura(ini);

                            pxf.setConsecutivoFactXMfto(factura.getConsecutivo());
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

                            /*  */
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
                            pxf.setValorRechazoFactura(rst.getDouble("valorRechazoFactura"));
                            pxf.setValorDescuentoFactura(rst.getDouble("valorDescuentoFactura"));
                            pxf.setValorRecaudadoFactura(rst.getDouble("valorRecaudadoFactura"));
                            pxf.setActivo(1);

                            listaproductosPorFactura.add(pxf);
                            Thread.sleep(10);

                        }
                        
                        i++;

                        System.out.print("factura : # " + factura.getNumeroFactura() + "-->  " + i + "\n");

                        factura.setListaProductosPorFactura(listaproductosPorFactura);

                        Thread.sleep(1);
                    }

                    Thread.sleep(10);
                    rst.close();
                    st.close();

                } 
            }
            System.out.print("Finaliza la Asignacion las facturas \n");
            con.close();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            //JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {

            //JOptionPane.showMessageDialog(null, "Error parametrizar  manifiesto " + manifiesto.getNumeroManifiesto() + " " + ex, " Alerta", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(HiloCargarListaDeProductosFacturasPoManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print("Aca sale del ciclo de las facturas");

    }

}
