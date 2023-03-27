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
public class FReporteFacturacionPorCanalHilo implements Runnable {

    FReporteFacturacionPorCanal form;
    public ArrayList<objReporteFacturacionPorCanal> listaDeRegistros;
    boolean xxx = true;
    String sql;
    String rutaArchivo; //= form.ini.getRutaDeApp() + "reportes/reporteFacturacionPorCanal.csv";

    public FReporteFacturacionPorCanalHilo(FReporteFacturacionPorCanal form) {
        this.form = form;

    }

    public FReporteFacturacionPorCanalHilo(FReporteFacturacionPorCanal form, boolean xxx) {
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
             * Esta consulta es sustituidda por la que viene a continuacion sql
             * = "SELECT TTT1.fechaDistribucion,
             * IFNULL(TTT1.total_poblaciones,0) as total_poblaciones,
             * IFNULL(TTT1.total_mayorista,0) AS total_mayorista,
             * IFNULL(TTT2.total_mini_mercados,0) AS total_mini_mercados,
             * IFNULL(TTT2.total_TAT,0) AS total_TAT,
             * (IFNULL(TTT1.total_poblaciones,0) +
             * IFNULL(TTT1.total_mayorista,0) +
             * IFNULL(TTT2.total_mini_mercados,0) + IFNULL(TTT2.total_TAT,0)) AS
             * TOTAL " + "FROM	(SELECT TT1.fechaDistribucion,
             * TT1.total_poblaciones, TT2.total_mayorista " + "FROM	(SELECT
             * T1.fechaDistribucion, T2.total_poblaciones " + "FROM	(SELECT
             * fechaDistribucion FROM vst_defintivofacturaspormanifiesto WHERE
             * fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS
             * DATE) AND CAST('" + form.fechaFinal + "' AS DATE) GROUP BY
             * fechaDistribucion)T1 " + "LEFT JOIN	(SELECT fechaDistribucion,
             * SUM(valorFacturaSinIva) AS total_poblaciones FROM
             * vst_defintivofacturaspormanifiesto WHERE fechaDistribucion
             * BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" +
             * form.fechaFinal + "' AS DATE) AND idCanalDeVenta = '4' GROUP BY
             * fechaDistribucion)T2 " + "ON T1.fechaDistribucion =
             * T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 " + "INNER
             * JOIN	(SELECT T1.fechaDistribucion, T2.total_mayorista " + "FROM
             * (SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto
             * WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'
             * AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) GROUP BY
             * fechaDistribucion)T1 " + "LEFT JOIN	(SELECT fechaDistribucion,
             * SUM(valorFacturaSinIva) AS total_mayorista FROM
             * vst_defintivofacturaspormanifiesto WHERE fechaDistribucion
             * BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" +
             * form.fechaFinal + "' AS DATE) AND idCanalDeVenta = '3' GROUP BY
             * fechaDistribucion)T2 " + "ON T1.fechaDistribucion =
             * T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 " + "ON
             * TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY
             * TT1.fechaDistribucion)TTT1 " + "INNER JOIN	(SELECT
             * TT1.fechaDistribucion, TT1.total_TAT, TT2.total_mini_mercados " +
             * "FROM	(SELECT T1.fechaDistribucion, T2.total_TAT " + "FROM
             * (SELECT fechaDistribucion FROM vst_defintivofacturaspormanifiesto
             * WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'
             * AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) GROUP BY
             * fechaDistribucion)T1 " + "LEFT JOIN	(SELECT fechaDistribucion,
             * SUM(valorFacturaSinIva) AS total_TAT FROM
             * vst_defintivofacturaspormanifiesto WHERE fechaDistribucion
             * BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" +
             * form.fechaFinal + "' AS DATE) AND idCanalDeVenta = '2' GROUP BY
             * fechaDistribucion)T2 " + "ON T1.fechaDistribucion =
             * T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 " + "INNER
             * JOIN	(SELECT T1.fechaDistribucion, T2.total_mini_mercados " +
             * "FROM	(SELECT fechaDistribucion FROM
             * vst_defintivofacturaspormanifiesto WHERE fechaDistribucion
             * BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" +
             * form.fechaFinal + "' AS DATE) GROUP BY fechaDistribucion)T1 " +
             * "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS
             * total_mini_mercados FROM vst_defintivofacturaspormanifiesto WHERE
             * fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS
             * DATE) AND CAST('" + form.fechaFinal + "' AS DATE) AND
             * idCanalDeVenta = '5' GROUP BY fechaDistribucion)T2 " + "ON
             * T1.fechaDistribucion = T2.fechaDistribucion GROUP BY
             * T1.fechaDistribucion)TT2 " + "ON TT1.fechaDistribucion =
             * TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT2 " + "ON
             * TTT1.fechaDistribucion = TTT2.fechaDistribucion GROUP BY
             * TTT1.fechaDistribucion; ";
             */
           rutaArchivo = form.ini.getRutaDeApp() + "reportes/reporteFacturacionPorCanal.csv";
           
            sentenciaSQL();
            con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(this.sql);

                    /*Se exporta el resultado a un archivo plano .CSV */
                    try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo), '\t')) {
                        //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));

                    }

                    rst.last();
                    //int totalRegistros = (rst.getRow() / diffDays);
                    int totalRegistros = rst.getRow();
                    int valor = 0;
                    this.form.barra.setValue(0);

                    rst.beforeFirst();
                    while (rst.next()) {
                        objReporteFacturacionPorCanal registro = new objReporteFacturacionPorCanal();
                        registro.setFecha(rst.getDate("fechaDistribucion"));
                        registro.setPoblaciones(rst.getDouble("total_poblaciones"));
                        registro.setMayoristas(rst.getDouble("total_mayorista"));
                        registro.setMinimercados(rst.getDouble("total_mini_mercados"));
                        registro.setTat(rst.getDouble("total_TAT"));
                        registro.setTotal(rst.getDouble("TOTAL"));
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
                    Desktop.getDesktop().open(new File(form.ini.getRutaDeApp() + "reportes/reporteFacturacionPorCanal.csv"));

                }
                this.form.lblCirculoDeProgreso.setVisible(false);
//                if (this.form.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

            } catch (SQLException ex) {
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "reporteFacturacionPorCanal.xls";
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

        for (objReporteFacturacionPorCanal obj : form.listaDeRegistros) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getFecha());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue(obj.getPoblaciones()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(2);
            cell.setCellValue(obj.getMayoristas()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(3);
            cell.setCellValue(obj.getMinimercados()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(4);
            cell.setCellValue(obj.getTat()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(5);
            cell.setCellValue("" + obj.getTotal()); // nombre del conductor
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

    public void exportarCSV() {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;

        rutaArchivo = form.ini.getRutaDeApp() + "reportes/reporteFacturacionPorCanal.csv";
        
        con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());
        if (con != null) {
            sentenciaSQL();
            try {
                st = con.createStatement();
                rst = st.executeQuery(this.sql);

                /*Se exporta el resultado a un archivo plano .CSV */
                try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);
                    // Desktop.getDesktop().open(new File("yourfile.csv"));

                    Desktop.getDesktop().open(new File(rutaArchivo));

                } catch (Exception ex) {
                    Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }

    private void sentenciaSQL() {
        sql = "SELECT TTT1.fechaDistribucion, IFNULL(TTT1.total_poblaciones,0) as total_poblaciones, IFNULL(TTT1.total_mayorista,0) AS total_mayorista, IFNULL(TTT2.total_mini_mercados,0) AS total_mini_mercados, IFNULL(TTT2.total_TAT,0) AS total_TAT, (IFNULL(TTT1.total_poblaciones,0) + IFNULL(TTT1.total_mayorista,0) + IFNULL(TTT2.total_mini_mercados,0) + IFNULL(TTT2.total_TAT,0)) AS TOTAL "
                + "FROM	(SELECT TT1.fechaDistribucion, TT1.total_poblaciones, TT2.total_mayorista "
                + "FROM	(SELECT T1.fechaDistribucion, T2.total_poblaciones "
                + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE)  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_poblaciones FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) AND idCanalVenta = '4'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.total_mayorista "
                + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE)  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_mayorista FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) AND idCanalVenta = '3'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT1 "
                + "INNER JOIN	(SELECT TT1.fechaDistribucion, TT1.total_TAT, TT2.total_mini_mercados "
                + "FROM	(SELECT T1.fechaDistribucion, T2.total_TAT "
                + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE)  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_TAT FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) AND idCanalVenta = '2'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT1 "
                + "INNER JOIN	(SELECT T1.fechaDistribucion, T2.total_mini_mercados "
                + "FROM	(SELECT fechaDistribucion FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE)  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T1 "
                + "LEFT JOIN	(SELECT fechaDistribucion, SUM(valorFacturaSinIva) AS total_mini_mercados FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "' AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE) AND idCanalVenta = '5'  and idTipoMovimiento <> 7 GROUP BY  fechaDistribucion)T2 "
                + "ON T1.fechaDistribucion = T2.fechaDistribucion GROUP BY T1.fechaDistribucion)TT2 "
                + "ON TT1.fechaDistribucion = TT2.fechaDistribucion GROUP BY TT1.fechaDistribucion)TTT2 "
                + "ON TTT1.fechaDistribucion = TTT2.fechaDistribucion GROUP BY TTT1.fechaDistribucion; ";

    }

}
