/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.sql.Connection;
import java.sql.ResultSet;
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
public class HiloListadoDeFacturasPorManifiestoDescargadas implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini;
    DescargarFacturas form;
     CManifiestosDeDistribucion manifiesto;
     List<CFacturasPorManifiesto> listaFacturasPorManifiestoDescargadas=null;
   String numeroDeManifiesto;
    ResultSet rst = null;

    /**
     * Constructor de clase
     */
    public HiloListadoDeFacturasPorManifiestoDescargadas(Inicio ini, int tiempo, CManifiestosDeDistribucion manifiesto) {
        this.tiempo = tiempo;
        this.ini = ini;
       this.manifiesto=manifiesto;
       this.numeroDeManifiesto=manifiesto.getNumeroManifiesto();
    }
    
      public HiloListadoDeFacturasPorManifiestoDescargadas(Inicio ini, int tiempo, DescargarFacturas form) {
        this.tiempo = tiempo;
        this.ini = ini;
       this.form=form;
       this.manifiesto=form.manifiestoActual;
       
    }

    @Override
    public void run() {
        String sql=null;
        ResultSet rst = null;
        Statement st;
         Connection con ;
        try {
           
                    
                  // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeFacturasPorManifiestoDescargadas");
            
            con = ini.getConnRemota();
            
            CManifiestosDeDistribucion man= new CManifiestosDeDistribucion(ini);
            
           sql = " SELECT facturasdescargadas.consecutivo, facturasdescargadas.numeroManifiesto, facturasdescargadas.numeroFactura,"
                + "facturasdescargadas. valorRechazo,facturasdescargadas. valorDescuento, facturasdescargadas.valorRecaudado, "
                + "facturasdescargadas.movimientoFactura,facturasdescargadas. motivoRechazo,facturasdescargadas. activo, "
                + "facturasdescargadas.fechaIng, facturasdescargadas.usuario, facturasdescargadas.flag,facturasdescargadas.usuario "
                + " FROM facturasdescargadas "
                + "WHERE "
                + "facturasdescargadas.numeroManifiesto='" + manifiesto + "'  order by fechaIng ASC; ";
            
            manifiesto=man;
            listaFacturasPorManifiestoDescargadas = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                   System.out.println("Cargando  -> " + new Date());
                   CFacturasPorManifiesto fd = new CFacturasPorManifiesto(ini);
                   
                    fd.setConsecutivo(rst.getInt("consecutivo"));
                    fd.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fd.setNumeroFactura(rst.getString("numeroFactura"));
                    fd.setValorRechazo(rst.getDouble("valorRechazo"));
                    fd.setValorDescuento(rst.getDouble("valorDescuento"));
                    fd.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    fd.setIdTipoDeMovimiento(rst.getInt("movimientoFactura"));
                    fd.setCausalDeRechazo(rst.getInt("motivoRechazo"));
                    fd.setActivo(rst.getInt("activo"));
                    fd.setUsuario(rst.getString("usuario"));

                    System.out.println("Cargando factura descargada número -> " + fd.getNumeroFactura());

                    listaFacturasPorManifiestoDescargadas.add(fd);

                    System.out.println("tiempo 2 " + new Date());
                      Thread.sleep(10);

                }
                rst.close();
                st.close();
               // con.close();
                 if(this.manifiesto != null){
                      manifiesto.setListaFacturasDescargadas(listaFacturasPorManifiestoDescargadas);
                }
                           
             
               Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeFacturasPorManifiestoDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
