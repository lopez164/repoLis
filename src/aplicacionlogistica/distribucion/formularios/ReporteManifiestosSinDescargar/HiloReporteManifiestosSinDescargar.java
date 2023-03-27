/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar;

import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloAux;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ManifiestosDeDistribucion;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author Usuario
 */
public class HiloReporteManifiestosSinDescargar implements Runnable {

   
   
    Inicio ini;
    FReporteManifiestosSinDescargar form;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param tiempo
     * @param ini
     */
    public HiloReporteManifiestosSinDescargar(FReporteManifiestosSinDescargar form) {
        this.form=form;       
        this.ini = form.ini;

    }

   

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        switch(this.form.xxx){
            case 0:
            break;
            case 1:
                 traerDatos();
                break;
            case 2:
        {
            try {
                exportarExcel();
                
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloReporteManifiestosSinDescargar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                break;
        }
       
        
    }

    private void traerDatos() throws HeadlessException {
        ResultSet rst = null;
        Statement st;
        Connection con;
        int i=0;
        try {

            this.form.limpiarTablaManifiestos();

            ArrayList<CManifiestosDeDistribucion> listaDeManifestosPendientesPorDescargar = null;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloReporteManifiestosSinDescargar");

            CManifiestosDeDistribucion vista = new CManifiestosDeDistribucion(ini);
            
            //java.sql.Date fecha = (java.sql.Date) ini.getFechaSql(this.form.jFechaInicial);

            listaDeManifestosPendientesPorDescargar = new ArrayList();
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(ini.arrListadoDeManifiestosSinDescargar());
               
                int rstSize=rst.getFetchSize()/100;
                
                while (rst.next()) {
                    if(i>=rstSize){
                        form.barra.setValue(form.barra.getValue() + i);
                        i=0;
                    }
                    System.out.println("Cargando  -> " + new Date());
                    vista = new CManifiestosDeDistribucion(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    vista.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    vista.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    vista.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                    vista.setVehiculo(rst.getString("vehiculo"));
                    vista.setTipoVehiculo(rst.getString("tipoVehiculo"));
                    vista.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    vista.setConductor(rst.getString("conductor"));
                    vista.setNombreConductor(rst.getString("nombreConductor") );
                    vista.setApellidosConductor(rst.getString("apellidosConductor"));
                    vista.setNombreCanal(rst.getString("nombreCanal"));
                    vista.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    //vista.setValorTotalManifiesto(Double.POSITIVE_INFINITY);
                    
                    
                    
                    System.out.println("Cargando facturas # -> " + vista.getNumeroManifiesto());

                    listaDeManifestosPendientesPorDescargar.add(vista);
                   // vista.setListaFacturasPorManifiesto();

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(10);

                    i++;
                }
                form.barra.setValue(100);
                i=0;
                rst.close();
                st.close();
                con.close();
                ini.setListaDeManifiestossinDescargar(listaDeManifestosPendientesPorDescargar);
                form.listaDeRegistros=listaDeManifestosPendientesPorDescargar;
                Thread.sleep(1);
                form.llenarTablabitacora();
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloReporteManifiestosSinDescargar.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + ex, " Alerta, cerrar ventana", 1, null);
        }
    }
    
     public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "manifiestosEnDistribucion.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "reportes/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form.barra.setValue(0);
        this.form.lblExportandoExcel.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(form.barra, form.lblExportandoExcel)).start();

        //cabecera de la hoja de excel
        String[] header222 = new String[]{"id", "fechaDistribucion","numeroManifiesto", "nombreRuta","nombreCanal","vehiculo",  "tipoVehiculo", "nombreConductor", "cant Pedidos", "Valor Ruta"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja); 

        int valor = form.listaDeRegistros.size() / 100;
        int k = 0;
        int avance = 0;

        //poner negrita a la cabecera
        int i = 0;
        CellStyle style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        HSSFRow row = hoja1.createRow(i);//se crea las filas
        for (int j = 0; j < header222.length; j++) {
            //para la cabecera
            HSSFCell cell = row.createCell(j);//se crea las celdas para la cabecera, junto con la posición
            cell.setCellStyle(style); // se añade el style crea anteriormente 
            cell.setCellValue(header222[j]);//se añade el contenido
            hoja1.autoSizeColumn(j);

        }
        i++;
        /*RESTO DE INFORME*/
        

        for (CManifiestosDeDistribucion obj : form.listaDeRegistros) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(""+ i);  // item
             hoja1.autoSizeColumn(0);
            
             cell = row.createCell(1);
            cell.setCellValue("" + obj.getFechaDistribucion()); // numero de manifiesto
            hoja1.autoSizeColumn(1);
            
            cell = row.createCell(2);
            cell.setCellValue(obj.getNumeroManifiesto()); // fecha de distribucion
            hoja1.autoSizeColumn(2);
            
            cell = row.createCell(3);
            cell.setCellValue(obj.getNombreDeRuta()); // placa del vehiculo
            hoja1.autoSizeColumn(3);
            
            cell = row.createCell(4);
            cell.setCellValue(obj.getNombreCanal()); // placa del vehiculo
            hoja1.autoSizeColumn(4);
            
            cell = row.createCell(5);
            cell.setCellValue(obj.getVehiculo()); // nombre del conductor
            hoja1.autoSizeColumn(5);
            
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getTipoVehiculo()); // nombre del conductor
            hoja1.autoSizeColumn(6);
            
            cell = row.createCell(7);
            cell.setCellValue("" + obj.getConductor()); // nombre del conductor
            hoja1.autoSizeColumn(7);
            
            cell = row.createCell(8);
            cell.setCellValue(obj.getCantidadPedidos()); // nombre del conductor
            hoja1.autoSizeColumn(8);
            
            cell = row.createCell(9);
            cell.setCellValue("" + form.nf.format(obj.getValorTotalManifiesto())); // nombre del conductor
            hoja1.autoSizeColumn(9);
            
            
            i++;
            k++;
            Thread.sleep(10);
        }

        File file;
        file = new File(rutaArchivo);

        try {

            if (file.exists()) {// si el archivo existe se elimina
                file.delete();
                System.out.println("Archivo eliminado");

            }
            FileOutputStream fileOuS = new FileOutputStream(new File(rutaArchivo));
            libro.write(fileOuS);
            this.form.barra.setValue(100);
            this.form.lblCirculoDeProgreso.setVisible(false);
            this.form.lblExportandoExcel.setText("Archivo exportado satisfactoriamente");
            this.form.lblExportandoExcel.setText("Archivo exportado satisfactoriamente");
            fileOuS.flush();
            fileOuS.close();

            System.out.println("Archivo Creado");

            Desktop.getDesktop().open(new File(rutaArchivo));
            form.btnEjecutar.setEnabled(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
