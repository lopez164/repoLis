/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosMovilizadosPorConductor;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import com.opencsv.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lelopez
 */
public class HiloReporteManifiestosMovilizadosPorConductor implements Runnable {

    FReporteManifiestosMovilizadosPorConductor form;
    public List<String> listaDeRegistros;
    boolean xxx = true;

    public HiloReporteManifiestosMovilizadosPorConductor(FReporteManifiestosMovilizadosPorConductor form) {
        this.form = form;

    }

    public HiloReporteManifiestosMovilizadosPorConductor(FReporteManifiestosMovilizadosPorConductor form, boolean xxx) {
        this.form = form;
        this.xxx = xxx;
    }

    @Override
    public void run() {
        if (xxx) {
            Connection con = null;
            Statement st = null;
            ResultSet rst = null;
            this.form.lblCirculoDeProgreso.setVisible(true);
            this.form.listaDeRegistros = new ArrayList();
            Date fecha1 = Inicio.getFechaSql(form.jFechaInicial);
            
            /*Sentncia sql para crear los objetos */
            String sql = "SELECT numeroManifiesto,vehiculo,concat(nombreConductor,' ',apellidosConductor) as conductor,nombreDeRuta,"
                    + "if(estadoManifiesto=2,'CREADO',IF(estadoManifiesto=3,'DISTRIBUCION',IF(estadoManifiesto=4,'DESCARGADO','ANULADO'))) as estadoManifiesto,"
                    + "valorTotalManifiesto,valorRecaudado "
                    + "from vst_manifiestoDeDistribucion "
                    + "where "
                    + "fechaDistribucion='"  + fecha1 + "'; ";
                   // + "and conductor='" + form.conductor.getCedula() + "' "
                   // + "order by numeroManifiesto; ";

                      
            con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if(rst.next()){
                     rst.beforeFirst();    
                    /*Se genera un nombre aleatorio para el archivo*/
                    String clave = UUID.randomUUID().toString().substring(0, 8);
           
                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/" + clave +".csv"),'\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));
                        
                    }
                   listaDeRegistros = new ArrayList<>();
                    rst.beforeFirst();
                  
                    while (rst.next()) {
                        String cadena;
                        cadena = rst.getString("numeroManifiesto") + ","
                                + rst.getString("vehiculo") + ","
                                + rst.getString("conductor") + ","
                                + rst.getString("nombreDeRuta") + ","
                                + rst.getString("estadoManifiesto") + ","
                                + rst.getString("valorTotalManifiesto") + ","
                                + rst.getString("valorRecaudado") + "";

                        listaDeRegistros.add(cadena);
                    }

                   form.listaDeRegistros=listaDeRegistros;
                   rst.close();
                   st.close();
                   con.close();
                   form.llenarTablabitacora();
                    this.form.barra.setValue(100);
                    //this.form.llenarTablabitacora();
                    
                    Desktop.getDesktop().open(new File("tmp/" + clave +".csv"));                        
                    }else{
                        JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE); 
                    }
                    

                    
                }
                this.form.lblCirculoDeProgreso.setVisible(false);
//                if (this.form.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(HiloReporteManifiestosMovilizadosPorConductor.class.getName()).log(Level.SEVERE, null, ex);

            }

        } 
        }

    }

    

   


