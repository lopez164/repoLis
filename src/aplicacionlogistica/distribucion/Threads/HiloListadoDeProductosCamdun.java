/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeProductosCamdun implements Runnable {

    Inicio ini;

    List<CProductosPorFactura> listaProductoscamdun = null;

    ResultSet rst = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloListadoDeProductosCamdun(Inicio ini) {

        this.ini = ini;

    }

    @Override
    public void run() {

        listaVacio();

    }

    private void listaVacio() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadeProductosCamdun");

            CProductos producto = new CProductos(ini);
            List arrProductosCamdun = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(producto.arrListaDeProductos());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    producto = new CProductos(ini);

                    producto.setActivo(rst.getInt("activo"));
                    producto.setAltoProducto(rst.getDouble("altoProducto"));
                    producto.setAnchoProducto(rst.getDouble("anchoProducto"));
                    producto.setCodigoProducto(rst.getString("codigoProducto"));
                    producto.setDescripcionProducto(rst.getString("descripcionProducto"));
                    producto.setIsFree(rst.getInt("isFree"));
                    producto.setLargoProducto(rst.getDouble("largoProducto"));
                    producto.setLinea(rst.getString("linea"));
                    producto.setPesoProducto(rst.getDouble("pesoProducto"));
                    producto.setValorUnitarioConIva(rst.getDouble("valorUnitarioConIva"));
                    producto.setValorUnitarioSinIva(rst.getDouble("valorUnitario"));

                    System.out.println("Cargando productos -> " + producto.getDescripcionProducto());

                    arrProductosCamdun.add(producto);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeProductosCamdun(arrProductosCamdun);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
