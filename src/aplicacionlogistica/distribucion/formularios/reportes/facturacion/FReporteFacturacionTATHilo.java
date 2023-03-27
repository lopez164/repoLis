/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.facturacion;

import aplicacionlogistica.distribucion.Threads.HiloAux;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
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
public class FReporteFacturacionTATHilo implements Runnable {

    FReporteFacturacionTAT form;
    public ArrayList<objReporteFacturacionPorCanal> listaDeRegistros;
    boolean xxx = true;

    public FReporteFacturacionTATHilo(FReporteFacturacionTAT form) {
        this.form = form;

    }

    public FReporteFacturacionTATHilo(FReporteFacturacionTAT form, boolean xxx) {
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

            /*Sentncia sql para crear los objetos */
            String sql;

            long startTime = form.fechaIncial.getTime();
            long endTime = form.fechaFinal.getTime();
            long diffTime = endTime - startTime;
            int diffDays = (int) (diffTime / (1000 * 60 * 60 * 24));

            //  String x = "CAST('" + form.fechaIncial + "' AS";
            //x="AND CAST('" + form.fechaFinal + "'";
           
            /**
             * 
             * sql = "SELECT TTTT1.fechaDistribucion, IFNULL(TTTT1.total_TAT_mayor30,0) AS total_TAT_mayor30, IFNULL(TTTT2.menor_diez,0) AS menor_diez, IFNULL(TTTT2.diez_veinte,0) AS diez_veinte, IFNULL(TTTT2.veinte_treinta,0) AS veinte_treinta, IFNULL(TTTT2.mayor_treinta,0) AS mayor_treinta, (IFNULL(TTTT2.menor_diez,0) + IFNULL(TTTT2.diez_veinte,0) + IFNULL(TTTT2.veinte_treinta,0) + IFNULL(TTTT2.mayor_treinta,0)) AS cantidad_total, IFNULL(TTTT1.total_TAT_general,0) AS total_TAT_general "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.total_TAT_mayor30, TT2.total_TAT_general "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.total_TAT_mayor30 "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_TAT_mayor30 FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva > '30000' AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion )T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.total_TAT_general "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_TAT_general FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTTT1  "
                    + "INNER JOIN	(SELECT TTT1.fechaDistribucion, TTT1.menor_diez, TTT1.diez_veinte, TTT2.veinte_treinta, TTT2.mayor_treinta "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.menor_diez, TT2.diez_veinte "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.menor_diez "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS menor_diez FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva <= '10000' AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.diez_veinte "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS diez_veinte FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)  AND valorFacturaSinIva >'10000' AND valorFacturaSinIva < '20000' AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT1 "
                    + "INNER JOIN	(SELECT TT1.fechaDistribucion, TT1.veinte_treinta, TT2.mayor_treinta "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.veinte_treinta "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS veinte_treinta FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)  AND valorFacturaSinIva >='20000' AND valorFacturaSinIva <= '30000' AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.mayor_treinta "
                    + "FROM	(SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS mayor_treinta FROM vst_defintivofacturaspormanifiesto WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva > '30000' AND idCanalDeVenta = '2'  GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT2 "
                    + "ON TTT1.fechaDistribucion = TTT2.fechaDistribucion GROUP BY TTT1.fechaDistribucion)TTTT2 "
                    + "ON TTTT1.fechaDistribucion = TTTT2.fechaDistribucion GROUP BY TTTT1.fechaDistribucion;";
                    * 
                    * 
                    */
            
            sql = "SELECT TTTT1.fechaDistribucion, IFNULL(TTTT1.total_TAT_mayor30,0) AS total_TAT_mayor30, IFNULL(TTTT2.menor_diez,0) AS menor_diez, IFNULL(TTTT2.diez_veinte,0) AS diez_veinte, IFNULL(TTTT2.veinte_treinta,0) AS veinte_treinta, IFNULL(TTTT2.mayor_treinta,0) AS mayor_treinta, (IFNULL(TTTT2.menor_diez,0) + IFNULL(TTTT2.diez_veinte,0) + IFNULL(TTTT2.veinte_treinta,0) + IFNULL(TTTT2.mayor_treinta,0)) AS cantidad_total, IFNULL(TTTT1.total_TAT_general,0) AS total_TAT_general "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.total_TAT_mayor30, TT2.total_TAT_general "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.total_TAT_mayor30 "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_TAT_mayor30 FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva > '30000' AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion )T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.total_TAT_general "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_TAT_general FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTTT1  "
                    + "INNER JOIN	(SELECT TTT1.fechaDistribucion, TTT1.menor_diez, TTT1.diez_veinte, TTT2.veinte_treinta, TTT2.mayor_treinta "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.menor_diez, TT2.diez_veinte "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.menor_diez "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS menor_diez FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva <= '10000' AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.diez_veinte "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS diez_veinte FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)  AND valorFacturaSinIva >'10000' AND valorFacturaSinIva < '20000' AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT1 "
                    + "INNER JOIN	(SELECT TT1.fechaDistribucion, TT1.veinte_treinta, TT2.mayor_treinta "
                    + "FROM	(SELECT T1.fechaDistribucion, T2.veinte_treinta "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS veinte_treinta FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)  AND valorFacturaSinIva >='20000' AND valorFacturaSinIva <= '30000' AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.mayor_treinta "
                    + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, count(valorFacturaSinIva) AS mayor_treinta FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE) AND valorFacturaSinIva > '30000' AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT2 "
                    + "ON TTT1.fechaDistribucion = TTT2.fechaDistribucion GROUP BY TTT1.fechaDistribucion)TTTT2 "
                    + "ON TTTT1.fechaDistribucion = TTTT2.fechaDistribucion GROUP BY TTTT1.fechaDistribucion;";

            con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);

                    /*Se exporta el resultado a un archivo plano .CSV */
                    try (CSVWriter writer = new CSVWriter(new FileWriter(form.ini.getRutaDeApp() + "reportes/reporteFacturacionTAT.csv"), '\t')) {
                        //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));

                    }

                    rst.last();
                    int totalRegistros = (rst.getRow() / diffDays);
                    int valor = 0;
                    this.form.barra.setValue(0);

                    rst.beforeFirst();
                    while (rst.next()) {
                        objReporteFacturacionTAT registro = new objReporteFacturacionTAT();
                        registro.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                        registro.setTotal_TAT_mayor30(rst.getDouble("total_TAT_mayor30"));
                        registro.setMenor_diez(rst.getInt("menor_diez"));
                        registro.setDiez_veinte(rst.getInt("diez_veinte"));
                        registro.setVeinte_treinta(rst.getInt("veinte_treinta"));
                        registro.setMayor_treinta(rst.getInt("mayor_treinta"));
                        registro.setCantidad_total(rst.getInt("cantidad_total"));
                        registro.setTotal_TAT_general(rst.getDouble("total_TAT_general"));

                        form.listaDeRegistros.add(registro);

                    }
                    rst.close();
                    st.close();
                    con.close();

                    valor = totalRegistros / 100;
                    int i = 0;
                    int avance = 0;
                    int x = 0;

                    for (i = 0; i <= (totalRegistros / 200); i++) {
                        if (x > valor) {
                            avance++;
                            this.form.barra.setValue((int) avance);
                            x = 0;
                        } else {
                            x++;
                        }

//                         registro = llenarRegistro(rst);
//                        this.form.listaDeRegistros.add(registro);
                        Thread.sleep(10);

                    }
                    this.form.barra.setValue(100);
                    this.form.llenarTablabitacora();
                    //Desktop.getDesktop().open(new File("yourfile.csv"));
                    Desktop.getDesktop().open(new File(form.ini.getRutaDeApp() + "reportes/reporteFacturacionTAT.csv"));

                }
                this.form.lblCirculoDeProgreso.setVisible(false);
//                if (this.form.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(FReporteFacturacionTATHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(FReporteFacturacionTATHilo.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(FReporteFacturacionTATHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "reporteFacturacionTAT.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "reportes/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form.barra.setValue(0);
        this.form.jLabel1.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(form.barra, form.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header = new String[]{"Fecha", "Poblaciones", "Mayoristas", "Minimercados", "TAT", "Total"};

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
        for (int j = 0; j < header.length; j++) {
            //para la cabecera
            HSSFCell cell = row.createCell(j);//se crea las celdas para la cabecera, junto con la posición
            cell.setCellStyle(style); // se añade el style crea anteriormente 
            cell.setCellValue(header[j]);//se añade el contenido
            hoja1.autoSizeColumn(j);

        }
        i++;
        /*RESTO DE INFORME*/

        for (objReporteFacturacionTAT obj : form.listaDeRegistros) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getFechaDistribucion());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue(obj.getTotal_TAT_mayor30()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(2);
            cell.setCellValue(obj.getMenor_diez()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(3);
            cell.setCellValue(obj.getDiez_veinte()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(4);
            cell.setCellValue(obj.getVeinte_treinta()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(5);
            cell.setCellValue("" + obj.getTotal_TAT_mayor30()); // nombre del conductor
            //hoja1.autoSizeColumn(5);

            cell = row.createCell(6);
            cell.setCellValue("" + obj.getCantidad_total()); // nombre del conductor
            //hoja1.autoSizeColumn(5);

            cell = row.createCell(7);
            cell.setCellValue("" + obj.getTotal_TAT_general()); // nombre del conductor
            //hoja1.autoSizeColumn(5);

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
            this.form.jLabel1.setText("Archivo exportado satisfactoriamente");
            this.form.jLabel1.setText("Archivo exportado satisfactoriamente");
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
