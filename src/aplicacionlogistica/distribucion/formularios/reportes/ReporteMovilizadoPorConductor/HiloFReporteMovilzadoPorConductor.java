/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.Threads.HiloFConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lelopez
 */
public class HiloFReporteMovilzadoPorConductor implements Runnable {

    FReportemovilizadoPorConductor fReportemovilizadoPorConductor;
    public ArrayList<FReporteMovilizadoPorConductorModelo> listaDeRegistros;
    String caso = null;
    Inicio ini;
    boolean xxx = true;
    String[] header;

    public HiloFReporteMovilzadoPorConductor(Inicio ini, FReportemovilizadoPorConductor form, String caso) {
        this.fReportemovilizadoPorConductor = form;
        this.caso = caso;
        this.ini = ini;

    }

    @Override
    public void run() {

        try {
            if (caso != null) {
                switch (caso) {
                    case "consultarDatos":
                        consultarDatos();
                        break;
                    case "exportarExcel":
                        //exportarExcel();
                        break;

                    case "sacarMinuta":
                        //sacarMinuta();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fReportemovilizadoPorConductor, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (xxx) {

        }

    }

    public void consultarDatos() {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        this.fReportemovilizadoPorConductor.lblCirculoDeProgreso.setVisible(true);

        this.fReportemovilizadoPorConductor.listaDeRegistros = new ArrayList();

        /*Sentncia sql para crear los objetos */
        String sql = "select fm.numeroManifiesto, fm.numeroFactura,m.vehiculo, "
                + "fm.valorARecaudarFactura, mv.nombreTipoDeMovimiento , fm.fechaIng, fm.usuario ,"
                + "fd.valorRecaudado,fd.fechaIng,fd.usuario,"
                + "TIMESTAMPDIFF(minute, fm.fechaIng, fd.fechaIng) AS minutos "
                + "FROM facturaspormanifiesto fm "
                + "left outer join facturasdescargadas fd on fm.numeroManifiesto=fd.numeroManifiesto and fm.numeroFactura=fd.numeroFactura "
                + "join manifiestosdedistribucion m on m.consecutivo=fm.numeroManifiesto "
                + "left outer join tiposdemovimientosmanifiestosfacturas mv on fd.movimientoFactura=mv.idtipoDeMovimiento "
                + "where "
                + "fm.fechaIng>='" + fReportemovilizadoPorConductor.fechaIncial + "'and "
                + "fm.fechaIng<='" + fReportemovilizadoPorConductor.fechaFinal + "' and "
                + "m.conductor='" + fReportemovilizadoPorConductor.conductor.getCedula() + "' "
                + "order by  fm.fechaIng;";

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                /*Se genera un nombre aleatorio para el archivo*/
                String clave = UUID.randomUUID().toString().substring(0, 8);

                fReportemovilizadoPorConductor.rutaArchivo = "tmp/" + clave + ".csv";

                try (CSVWriter writer = new CSVWriter(new FileWriter(fReportemovilizadoPorConductor.rutaArchivo), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);
                    // Desktop.getDesktop().open(new File("yourfile.csv"));

                }

                rst.close();
                st.close();
                con.close();

                llenarJtable();

            }
            this.fReportemovilizadoPorConductor.lblCirculoDeProgreso.setVisible(false);

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloFReporteMovilzadoPorConductor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarJtable() {
        File archivo = new File(fReportemovilizadoPorConductor.rutaArchivo);
        int filaTabla2;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

        try {
            if (archivo != null) {
                FileReader fr = new FileReader(archivo);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                DefaultTableModel modelo = (DefaultTableModel) fReportemovilizadoPorConductor.tablaRegistros.getModel();
                int i = 0;
                int x;
                
                while ((linea = br.readLine()) != null) {
                    if (i != 0) {
                        String cadena = linea.replaceAll("\"", "");

                        // linea.replaceAll("\"","");
                        String strMain = cadena;
                        String[] arrSplit = strMain.split("\t");
                        
                        
                        filaTabla2 = fReportemovilizadoPorConductor.tablaRegistros.getRowCount();

                        modelo.addRow(new Object[fReportemovilizadoPorConductor.tablaRegistros.getRowCount()]);

                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[0], filaTabla2, 1);  // numero manifiesto
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[1], filaTabla2, 2); // numero de factura
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[2], filaTabla2, 3); // placa
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(nf.format(Double.parseDouble(arrSplit[3])), filaTabla2, 4); // valor de la factura
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[4], filaTabla2, 5); // tipo de Movimiento
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[5], filaTabla2, 6); // Hora de salida
                        fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[6], filaTabla2, 7); // usuario de salida 
                        try{
                        if (arrSplit[7] == null) {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt(nf.format(Double.parseDouble("0.0")), filaTabla2, 8); // valor recaudado       
                        } else {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt(nf.format(Double.parseDouble(arrSplit[7])), filaTabla2, 8); // valor recaudado       
                        }

                        if (arrSplit[8] == null) {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt("", filaTabla2, 9); // Usuario de entrada

                        } else {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[8], filaTabla2, 9); // Usuario de entrada

                        }

                        if (arrSplit[9] == null) {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt("0", filaTabla2, 10); //tiempo de entrega(minutos)
                        } else {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[9], filaTabla2, 10); //tiempo de entrega(minutos)
                        }

                         if (arrSplit[10] == null) {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt("0", filaTabla2, 11); //tiempo de entrega(minutos)
                        } else {
                            fReportemovilizadoPorConductor.tablaRegistros.setValueAt(arrSplit[10], filaTabla2, 11); //tiempo de entrega(minutos)
                        }
                        }catch(Exception  ex){
                            
                        }
                        
                    }
                    i++;
                }
                if (i == 1) {
                    JOptionPane.showInternalMessageDialog(fReportemovilizadoPorConductor, "No hay registros en ese periodo ", "Sin Registros", JOptionPane.WARNING_MESSAGE);

                }
                br.close();
                fr.close();
                fReportemovilizadoPorConductor.btnExportarExcel.setEnabled(true);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

}
