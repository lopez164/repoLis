/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.distribucion.Threads.HiloAux;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import com.opencsv.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
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
 * @author lelopez
 */
public class HiloReporteGeneralDeDescargueDeRutas implements Runnable {

    FReporteGeneralDeDescargueDeRutas fReporteGeneralDeDescargueDeRutas;
    public ArrayList<Vst_FacturasDescargadas> listaDeRegistros;
    boolean xxx = true;

    public HiloReporteGeneralDeDescargueDeRutas(FReporteGeneralDeDescargueDeRutas form) {
        this.fReporteGeneralDeDescargueDeRutas = form;

    }

    public HiloReporteGeneralDeDescargueDeRutas(FReporteGeneralDeDescargueDeRutas form, boolean xxx) {
        this.fReporteGeneralDeDescargueDeRutas = form;
        this.xxx = xxx;
    }

    @Override
    public void run() {
        if (xxx) {
            Connection con = null;
            Statement st = null;
            ResultSet rst = null;
            this.fReporteGeneralDeDescargueDeRutas.lblCirculoDeProgreso.setVisible(true);
            this.fReporteGeneralDeDescargueDeRutas.listaDeRegistros = new ArrayList();

            /*Sentncia sql para crear los objetos */
            String sql = "SELECT * FROM descargueDeRutas "
                    + "where "
                    + "fechaDistribucion>='" + fReporteGeneralDeDescargueDeRutas.fechaIncial + "'and "
                    + "fechaDistribucion<='" + fReporteGeneralDeDescargueDeRutas.fechaFinal + "' order by "
                    + "numeroManifiesto,adherencia ;";

            long startTime = fReporteGeneralDeDescargueDeRutas.fechaIncial.getTime();
            long endTime = fReporteGeneralDeDescargueDeRutas.fechaFinal.getTime();
            long diffTime = (endTime - startTime) + 1;
            
          
            
            int diffDays = (int)(diffTime / (1000 * 60 * 60 * 24));

              if(diffDays<=0)  diffDays=1 ;
               String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
               String noUsarSSL = "&useSSL=false";
               String cadenaRemota = String.format("jdbc:mysql://%s/%s?%s%s", fReporteGeneralDeDescargueDeRutas.ini.getServerRemota(),
                    "rutero", husoHorario, noUsarSSL);
              
              
            con = CConexiones.GetConnection(cadenaRemota, 
                  this.fReporteGeneralDeDescargueDeRutas.ini.getUsuarioBDRemota(), this.fReporteGeneralDeDescargueDeRutas.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    
                    
                    /*Se genera un nombre aleatorio para el archivo*/
                    String clave = UUID.randomUUID().toString().substring(0, 8);
                   

                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/" + clave +".csv"),'\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));
                        
                    }

                    rst.last();
                    int totalRegistros = (rst.getRow() / diffDays);
                    int valor = 0;
                    this.fReporteGeneralDeDescargueDeRutas.barra.setValue(0);
                    rst.close();
                    st.close();
                    con.close();

                    
                    valor = totalRegistros / 100;
                    int i = 0;
                    int avance = 0;
                    int x = 0;

                    for (i = 0; i <= (totalRegistros/200); i++) {
                        if (x > valor) {
                            avance++;
                            this.fReporteGeneralDeDescargueDeRutas.barra.setValue((int) avance);
                            x = 0;
                        } else {
                            x++;
                        }

                        // registro = llenarRegistro(rst);
                        //this.fReporteGeneralDeDescargueDeRutas.listaDeRegistros.add(registro);
                        Thread.sleep(10);

                    }
                     Desktop.getDesktop().open(new File("tmp/" + clave +".csv"));
                    this.fReporteGeneralDeDescargueDeRutas.barra.setValue(100);
                    this.fReporteGeneralDeDescargueDeRutas.llenarTablabitacora();
                    
                   
                    
                }
                this.fReporteGeneralDeDescargueDeRutas.lblCirculoDeProgreso.setVisible(false);
//                if (this.fReporteGeneralDeDescargueDeRutas.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(fReporteGeneralDeDescargueDeRutas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(HiloReporteGeneralDeDescargueDeRutas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fReporteGeneralDeDescargueDeRutas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(HiloReporteGeneralDeDescargueDeRutas.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloReporteGeneralDeDescargueDeRutas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private Vst_FacturasDescargadas llenarRegistro(ResultSet rst) {
        Vst_FacturasDescargadas registro = new Vst_FacturasDescargadas();
        try {

            registro.setIdCanal(rst.getInt("idCanal"));
            registro.setNombreCanal(rst.getString("nombreCanal"));
            registro.setNumeroManifiesto(rst.getString("numeroManifiesto"));
            registro.setAdherencia(rst.getInt("adherencia"));
            registro.setNumeroFactura(rst.getString("numeroFactura"));
            registro.setFechaDistribucion(rst.getDate("fechaDistribucion"));
            registro.setFechaDeVenta(rst.getDate("fechaDeVenta"));
            registro.setVehiculo(rst.getString("vehiculo"));
            registro.setTipoContrato(rst.getString("tipoContrato"));
            registro.setConductor(rst.getString("conductor"));
            registro.setNombreConductor(rst.getString("nombreConductor"));
            registro.setNombreDespachador(rst.getString("nombreDespachador"));
            registro.setNombreDeRuta(rst.getString("nombreDeRuta"));
            registro.setTipoRuta(rst.getString("tipoRuta"));
            registro.setNombreDeCliente(rst.getString("nombreDeCliente"));
            registro.setDireccion(rst.getString("direccion"));
            registro.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
            registro.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
            registro.setValorRechazo(rst.getDouble("valorRechazo"));
            registro.setValorDescuento(rst.getDouble("valorDescuento"));
            registro.setValorRecaudado(rst.getDouble("valorRecaudado"));
            registro.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
            registro.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
            registro.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
            registro.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
            registro.setCompetencia(rst.getString("competencia"));
            registro.setVendedor(rst.getString("vendedor"));
            registro.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
            registro.setFechaReal(rst.getDate("fechaReal"));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(fReporteGeneralDeDescargueDeRutas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteGeneralDeDescargueDeRutas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registro;
    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "Inventario.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.fReporteGeneralDeDescargueDeRutas.barra.setValue(0);
        this.fReporteGeneralDeDescargueDeRutas.jLabel1.setVisible(true);
        this.fReporteGeneralDeDescargueDeRutas.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(fReporteGeneralDeDescargueDeRutas.barra, fReporteGeneralDeDescargueDeRutas.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header = new String[]{"idCanal", "nombreCanal", "numeroManifiesto", "adherencia", "numeroFactura",
            "fechaDistribucion", "fechaDeVenta", "vehiculo", "tipoContrato", "conductor", "nombreConductor", "nombreDespachador",
            "nombreDeRuta", "tipoRuta", "nombreDeCliente", "direccion", "valorFacturaSinIva", "valorTotalFactura",
            "valorRechazo", "valorDescuento", "valorRecaudado", "idTipoDeMovimiento", "nombreTipoDeMovimiento",
            "causalDeRechazo", "nombreCausalDeRechazo", "competencia", "vendedor", "salidasDistribucion", "fechaReal"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);

        int valor = fReporteGeneralDeDescargueDeRutas.listaDeRegistros.size() / 100;
        int k = 0;
        int avance = 0;

        //poner negrita a la cabecera
        int i = 0;
        CellStyle style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        HSSFRow row = hoja1.createRow(i);//se crea las filas
        for (int j = 0; j < header.length; j++) {
            //para la cabecera
            HSSFCell cell = row.createCell(j);//se crea las celdas para la cabecera, junto con la posición
            cell.setCellStyle(style); // se añade el style crea anteriormente 
            cell.setCellValue(header[j]);//se añade el contenido
            hoja1.autoSizeColumn(j);

        }
        i++;
        /*RESTO DE INFORME*/

        for (Vst_FacturasDescargadas obj : fReporteGeneralDeDescargueDeRutas.listaDeRegistros) {
            if (k > valor) {
                avance++;
                this.fReporteGeneralDeDescargueDeRutas.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getIdCanal());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue(obj.getNombreCanal()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(2);
            cell.setCellValue(obj.getNumeroManifiesto()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(3);
            cell.setCellValue(obj.getAdherencia()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(4);
            cell.setCellValue(obj.getNumeroFactura()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(5);
            cell.setCellValue("" + obj.getFechaDistribucion()); // nombre del conductor
            //hoja1.autoSizeColumn(5);
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getFechaDeVenta()); // nombre del conductor
            //hoja1.autoSizeColumn(6);
            cell = row.createCell(7);
            cell.setCellValue(obj.getVehiculo()); // nombre del conductor
            //hoja1.autoSizeColumn(7);
            cell = row.createCell(8);
            cell.setCellValue(obj.getTipoContrato()); // nombre del conductor
            //hoja1.autoSizeColumn(8);
            cell = row.createCell(9);
            cell.setCellValue(obj.getConductor()); // nombre del conductor
            //hoja1.autoSizeColumn(9);
            cell = row.createCell(10);
            cell.setCellValue(obj.getNombreConductor()); // nombre del conductor

            cell = row.createCell(11);
            cell.setCellValue(obj.getNombreDespachador()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(12);
            cell.setCellValue(obj.getNombreDeRuta()); // nombre del conductor
            //hoja1.autoSizeColumn(11);
            cell = row.createCell(13);
            cell.setCellValue(obj.getTipoRuta()); // nombre del conductor
            //hoja1.autoSizeColumn(12);
            cell = row.createCell(14);
            cell.setCellValue(obj.getNombreDeCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(13);
            cell = row.createCell(15);
            cell.setCellValue(obj.getDireccion()); // nombre del conductor
            //hoja1.autoSizeColumn(14);
            cell = row.createCell(16);
            cell.setCellValue(obj.getValorFacturaSinIva());
            //hoja1.autoSizeColumn(15);
            cell = row.createCell(17);
            cell.setCellValue(obj.getValorTotalFactura()); // nombre del conductor
            //hoja1.autoSizeColumn(16);
            cell = row.createCell(18);
            cell.setCellValue(obj.getValorRechazo()); // nombre del conductor
            //hoja1.autoSizeColumn(17);
            cell = row.createCell(19);
            cell.setCellValue(obj.getValorDescuento()); // nombre del conductor
            //hoja1.autoSizeColumn(18);
            cell = row.createCell(20);
            cell.setCellValue(obj.getValorRecaudado()); // nombre del conductor
            //hoja1.autoSizeColumn(19);
            cell = row.createCell(21);
            cell.setCellValue(obj.getIdTipoDeMovimiento()); // nombre del conductor
            //hoja1.autoSizeColumn(20);
            cell = row.createCell(22);
            cell.setCellValue(obj.getNombreTipoDeMovimiento()); // nombre del conductor
            //hoja1.autoSizeColumn(21);
            cell = row.createCell(23);
            cell.setCellValue(obj.getCausalDeRechazo()); // nombre del conductor
            //hoja1.autoSizeColumn(22);
            cell = row.createCell(24);
            cell.setCellValue(obj.getNombreCausalDeRechazo()); // nombre del conductor
            //hoja1.autoSizeColumn(23);

            cell = row.createCell(25);
            cell.setCellValue(obj.getCompetencia()); // nombre del conductor
            //hoja1.autoSizeColumn(23);
            cell = row.createCell(26);
            cell.setCellValue(obj.getVendedor()); // nombre del conductor
            //hoja1.autoSizeColumn(24);
            cell = row.createCell(27);
            cell.setCellValue(obj.getSalidasDistribucion()); // nombre 

            cell = row.createCell(28);
            cell.setCellValue("" + obj.getFechaReal()); // nombre 
            //hoja1.autoSizeColumn(25);
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
            this.fReporteGeneralDeDescargueDeRutas.barra.setValue(100);
            this.fReporteGeneralDeDescargueDeRutas.lblCirculoDeProgreso.setVisible(false);
            this.fReporteGeneralDeDescargueDeRutas.jLabel1.setText("Archivo exportado satisfactoriamente");
            this.fReporteGeneralDeDescargueDeRutas.jLabel1.setText("Archivo exportado satisfactoriamente");
            fileOuS.flush();
            fileOuS.close();

            System.out.println("Archivo Creado");

            Desktop.getDesktop().open(new File(rutaArchivo));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
