/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasParaAnular;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeRechasosParaDescargar implements Runnable {

   FAnularFacturas formAnular=null;
   NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini;
    

     ArrayList<CFacturasParaAnular> listaDeRechasosParaAnular= null;


    /**
     * Constructor de clase
     * @param ini
     */
    public HiloListadoDeRechasosParaDescargar(Inicio ini) {
        this.ini = ini;
    }

     public HiloListadoDeRechasosParaDescargar(Inicio ini,FAnularFacturas formAnular) {
        this.ini = ini;
        this.formAnular=formAnular;
    }
    @Override
    public void run() {

        ResultSet rst = null;
        Statement st;
        Connection con;
        
        try {
             // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeRechasosParaDescargar");
            
            listaDeRechasosParaAnular = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                String sql="SELECT * FROM facturasparaanular ;";
                rst = st.executeQuery(sql);
                                
                while (rst.next()) {
                    System.out.println("Cargando  facturas para anular -> " + new Date());
                    CFacturasParaAnular rpa = new CFacturasParaAnular(ini);

                    rpa.setNumeroFactura(rst.getString("numeroFactura"));
                    rpa.setCausalDeRechazo(rst.getString("causalDeRechazo"));
                    

                    System.out.println("Cargando rechazos para anular -> " + rpa.getNumeroFactura());

                    listaDeRechasosParaAnular.add(rpa);
                    
                    this.formAnular.agregarFacturaAlArchivo(rpa);
                   
                    Thread.sleep(2);
                
                    System.out.println("saliendo del ciclo facturas para anular " + new Date());

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeRechasosParaAnular(listaDeRechasosParaAnular);
                Thread.sleep(1);
            }
        }catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(HiloListadoDeRechasosParaDescargar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (formAnular != null) {
            
            this.formAnular.listaDeFacturasParaAnular=listaDeRechasosParaAnular;
            
             this.formAnular.lblCirculoDeProgreso.setVisible(true);
             
             double valor=0;
            for (CFacturasParaAnular obj : listaDeRechasosParaAnular) {
                try {
                    Vst_Factura fac = new Vst_Factura(ini, obj.getNumeroFactura());
                    
                    DefaultTableModel modelo = (DefaultTableModel) this.formAnular.jTableFacturasParaAnular.getModel();
                    
                    int filaTabla2 = this.formAnular.jTableFacturasParaAnular.getRowCount();
                    
                    modelo.addRow(new Object[this.formAnular.jTableFacturasParaAnular.getRowCount()]);
                    
                    this.formAnular.jTableFacturasParaAnular.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                    this.formAnular.jTableFacturasParaAnular.setValueAt(fac.getNumeroFactura(), filaTabla2, 1); // numero de facturaActual
                    this.formAnular.jTableFacturasParaAnular.setValueAt(fac.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
                    this.formAnular.jTableFacturasParaAnular.setValueAt(nf.format(fac.getValorTotalFactura()), filaTabla2, 3);
                    valor+=fac.getValorTotalFactura();
                    Thread.sleep(2);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloListadoDeRechasosParaDescargar.class.getName()).log(Level.SEVERE, null, ex);
                   this.formAnular.lblCirculoDeProgreso.setVisible(false); 
                }

            }
            this.formAnular.lblValorTotalRechaso.setText(nf.format(valor));
            this.formAnular.lblCirculoDeProgreso.setVisible(false);
            this.formAnular.txtNumeroDeFactura.setEditable(true);
            this.formAnular.txtNumeroDeFactura.setEnabled(true);
            this.formAnular.txtNumeroDeFactura.requestFocus();
            this.formAnular.btnGrabar.setEnabled(true);
        }
    }
}
