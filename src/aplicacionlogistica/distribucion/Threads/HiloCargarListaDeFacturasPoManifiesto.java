/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloCargarListaDeFacturasPoManifiesto implements Runnable {

    Inicio ini;

    List<CFacturasPorManifiesto> listaFacturasPorManifiesto = null;

    String sql;

    /**
     * Constructor de clase
     */
    public HiloCargarListaDeFacturasPoManifiesto(Inicio ini) {

        this.ini = ini;

    }

    @Override
    public void run() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        
        for (CManifiestosDeDistribucion mfto : ini.getListaDeManifiestossinDescargar()) {
            
            if (mfto.getListaFacturasPorManifiesto() == null) {
                String sql = "select * from vst_defintivofacturaspormanifiesto where numeroManifiesto=" + mfto.getNumeroManifiesto() + "' "
                        + "ORDER BY adherencia ASC ;";
                try {
                   
                    con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloCargarListaDeFacturasPoManifiesto");

                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    listaFacturasPorManifiesto = new ArrayList();
                    if (con != null) {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        while (rst.next()) {
                            fxm.setConsecutivo(rst.getInt("consecutivo"));
                            fxm.setAdherencia(rst.getInt("adherencia"));
                            fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                            fxm.setVehiculo(rst.getString("vehiculo"));
                            fxm.setConductor(rst.getString("conductor"));
                            fxm.setNombreConductor(rst.getString("nombreConductor"));
                            fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                            fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                            fxm.setNumeroFactura(rst.getString("numeroFactura"));
                            fxm.setValorTotalFactura(rst.getDouble("valorARecaudarFactura"));
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
                            fxm.setPesoFactura(rst.getDouble("pesofactura"));

                            listaFacturasPorManifiesto.add(fxm);

                        }
                        mfto.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
                        rst.close();
                        st.close();
                        con.close();

                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

                } catch (Exception ex) {
                    Logger.getLogger(HiloCargarListaDeFacturasPoManifiesto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}

