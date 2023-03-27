/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.ReporteovilizadoPorTodosLosConductores;

import aplicacionlogistica.distribucion.Threads.HiloAux;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReporteMovilizadoPorConductorModelo;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FReporteMovilzadoPorTodosLosConductoresHilo implements Runnable {

    FReportemovilizadoPorTodosLosConductores form;
    public ArrayList<FReporteMovilizadoPorConductorModelo> listaDeRegistros;
    boolean xxx = true;
    String[] header;

    public FReporteMovilzadoPorTodosLosConductoresHilo(FReportemovilizadoPorTodosLosConductores form) {
        this.form = form;

    }

    public FReporteMovilzadoPorTodosLosConductoresHilo(FReportemovilizadoPorTodosLosConductores form, boolean xxx) {
        this.form = form;
        this.xxx = xxx;
    }

    @Override
    public void run() {
        if (xxx) {
            Connection con = null;
            Statement st = null;
            ResultSet rst = null;
            this.form.listaDeRegistros = new ArrayList();

            
            //String SQL_CONSULTA_CONDUCTOR_TODOS_CANALES;
            String sql = "SELECT TTTT1.fechaDistribucion,TTTT1.numeroManifiesto, TTTT2.nombreConductor, TTTT1.vehiculo, IFNULL(TTTT1.entrega_total,0) AS ET, IFNULL(TTTT1.entrega_parcial,0) AS EP, IFNULL(TTTT1.rechazo_total,0) AS RT, IFNULL(TTTT1.volver_zonificar,0) AS VZ, IFNULL(TTTT2.programado,0) AS PG, (IFNULL(TTTT1.entrega_total,0) + IFNULL(TTTT1.entrega_parcial,0)) AS cant_entregada, TTTT1.nombreCanal, IFNULL(TTTT2.total_facturas,0) AS subtotal_facturas "
                    + "FROM	(SELECT TTT1.fechaDistribucion,TTT1.numeroManifiesto, TTT1.vehiculo, TTT1.nombreCanal, TTT1.entrega_total, TTT1.entrega_parcial, TTT2.rechazo_total, TTT2.volver_zonificar "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal, TT1.entrega_total, TT2.entrega_parcial "
                    + "FROM	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.entrega_total "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'    AS DATE)    GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, count(idtipoDeMovimiento) AS entrega_total FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   AND idtipoDeMovimiento = 2 GROUP BY  nombreConductor)T2 "
                    + "	ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.entrega_parcial "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'   AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)   GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, count(idtipoDeMovimiento) AS entrega_parcial FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   AND idtipoDeMovimiento = 4 GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion AND TT1.numeroManifiesto = TT2.numeroManifiesto AND TT1.vehiculo = TT2.vehiculo AND TT1.nombreCanal = TT2.nombreCanal GROUP BY TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal)TTT1 "
                    + "INNER JOIN	(SELECT TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal, TT1.rechazo_total, TT2.volver_zonificar "
                    + "FROM	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.rechazo_total "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)   GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, count(idtipoDeMovimiento) AS rechazo_total FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   AND idtipoDeMovimiento = 3 GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.volver_zonificar "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)    GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, count(idtipoDeMovimiento) AS volver_zonificar FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   AND idtipoDeMovimiento = 6 GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion AND TT1.numeroManifiesto = TT2.numeroManifiesto AND TT1.vehiculo = TT2.vehiculo AND TT1.nombreCanal = TT2.nombreCanal GROUP BY TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal)TTT2 "
                    + "ON TTT1.fechaDistribucion = TTT2.fechaDistribucion AND TTT1.numeroManifiesto = TTT2.numeroManifiesto AND TTT1.vehiculo = TTT2.vehiculo AND TTT1.nombreCanal = TTT2.nombreCanal GROUP BY TTT1.fechaDistribucion, TTT1.numeroManifiesto, TTT1.vehiculo, TTT1.nombreCanal)TTTT1 "
                    + "INNER JOIN	(SELECT TTT1.fechaDistribucion, TTT1.numeroManifiesto, TTT1.vehiculo, TTT1.nombreCanal,TTT1.programado, TTT1.total_facturas, TTT2.nombreConductor "
                    + "FROM	(SELECT TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal, TT1.programado, TT2.total_facturas "
                    + "FROM	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.programado "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "' AS DATE)   GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, count(idtipoDeMovimiento) AS programado FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.total_facturas "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)   GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, SUM(valorFacturaSinIva) AS total_facturas FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'  AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TT2 "
                    + "ON TT1.fechaDistribucion = TT2.fechaDistribucion AND TT1.numeroManifiesto = TT2.numeroManifiesto AND TT1.vehiculo = TT2.vehiculo AND TT1.nombreCanal = TT2.nombreCanal GROUP BY TT1.fechaDistribucion, TT1.numeroManifiesto, TT1.vehiculo, TT1.nombreCanal)TTT1 "
                    + "INNER JOIN	(SELECT T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal, T2.nombreConductor "
                    + "FROM	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "'  AS DATE)   GROUP BY  nombreConductor)T1 "
                    + "LEFT JOIN	(SELECT fechaDistribucion, numeroManifiesto, vehiculo, nombreCanal, nombreConductor FROM vst_movilizacionfacturasdescargadas WHERE fechaDistribucion BETWEEN CAST('" + form.fechaIncial + "'     AS DATE) AND CAST('" + form.fechaFinal + "'      AS DATE)   GROUP BY  nombreConductor)T2 "
                    + "ON T1.fechaDistribucion = T2.fechaDistribucion AND T1.numeroManifiesto = T2.numeroManifiesto AND T1.vehiculo = T2.vehiculo AND T1.nombreCanal = T2.nombreCanal GROUP BY T1.fechaDistribucion, T1.numeroManifiesto, T1.vehiculo, T1.nombreCanal)TTT2 "
                    + "ON TTT1.fechaDistribucion = TTT2.fechaDistribucion AND TTT1.numeroManifiesto = TTT2.numeroManifiesto AND TTT1.vehiculo = TTT2.vehiculo AND TTT1.nombreCanal = TTT2.nombreCanal GROUP BY TTT1.fechaDistribucion, TTT1.numeroManifiesto, TTT1.vehiculo, TTT1.nombreCanal)TTTT2 "
                    + "ON TTTT1.fechaDistribucion = TTTT2.fechaDistribucion AND TTTT1.numeroManifiesto = TTTT2.numeroManifiesto AND TTTT1.vehiculo = TTTT2.vehiculo AND TTTT1.nombreCanal = TTTT2.nombreCanal GROUP BY TTTT1.fechaDistribucion, TTTT1.numeroManifiesto, TTTT1.vehiculo, TTTT1.nombreCanal";

            con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    /*variable para obtener el nombre de la columna*/
                    ResultSetMetaData rsmd = rst.getMetaData();
                    int numeroColumnas = rsmd.getColumnCount();

                    form.header = new String[numeroColumnas];
                    
                    for (int i = 0; i < numeroColumnas; i++) {
                        form.header[i] = rsmd.getColumnName(i + 1);
                        System.out.println("columna=" + rsmd.getTableName(i + 1) + "." + rsmd.getColumnName(i + 1) + " --> " + rsmd.getColumnTypeName(i + 1));
                    }

                    rst.last();
                    int valor = rst.getRow();
                    this.form.barra.setValue(0);
                    rst.first();

                    valor = valor / 100;
                    int i = 0;
                    int avance = 0;
                    FReporteMovilizadoPorConductorModelo registro = new FReporteMovilizadoPorConductorModelo();
                    registro = llenarRegistro(rst);
                    this.form.listaDeRegistros.add(registro);

                    while (rst.next()) {
                        if (i > valor) {
                            avance++;
                            this.form.barra.setValue((int) avance);
                            i = 0;
                        }
                        registro = llenarRegistro(rst);

                        this.form.listaDeRegistros.add(registro);
                        i++;
                        Thread.sleep(10);

                    }
                    this.form.barra.setValue(100);
                    this.form.llenarTablabitacora();
                    this.form.btnExportarExcel.setEnabled(true);
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FReporteMovilzadoPorTodosLosConductoresHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FReporteMovilzadoPorTodosLosConductoresHilo.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(FReporteMovilzadoPorTodosLosConductoresHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private FReporteMovilizadoPorConductorModelo llenarRegistro(ResultSet rst) throws SQLException {

        FReporteMovilizadoPorConductorModelo registro = new FReporteMovilizadoPorConductorModelo();
        registro.setFechaDistribucion(rst.getString("fechaDistribucion"));
        registro.setNumeroManifiesto(rst.getString("numeroManifiesto"));
        registro.setNombreConductor(rst.getString("nombreConductor"));
        registro.setVehiculo(rst.getString("vehiculo"));
        registro.setEntregaTotal(rst.getString("ET"));
        registro.setEntregaParcial(rst.getString("EP"));
        registro.setDevolucionTotal(rst.getString("RT"));
        registro.setReenvios(rst.getString("VZ"));
        registro.setProgramados(rst.getString("PG"));
        registro.setCantidadEntregada(rst.getString("cant_entregada"));
        registro.setNombreCanal(rst.getString("nombreCanal"));
        registro.setSubtotalFacturas(rst.getString("subtotal_facturas"));

        return registro;
    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "Inventario.xlsx";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form.barra.setValue(0);
        this.form.jLabel1.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(form.barra, form.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header222 = new String[]{"idCanal", "nombreCanal", "numeroManifiesto", "adherencia", "numeroFactura",
            "fechaDistribucion", "fechaDeVenta", "vehiculo", "tipoContrato", "conductor", "nombreConductor",
            "nombreDeRuta", "tipoRuta", "nombreDeCliente", "direccion", "valorFacturaSinIva", "valorTotalFactura",
            "valorRechazo", "valorDescuento", "valorRecaudado", "idTipoDeMovimiento", "nombreTipoDeMovimiento",
            "causalDeRechazo", "nombreCausalDeRechazo", "vendedor", "salidasDistribucion"};

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
        for (int j = 0; j < form.header.length; j++) {
            //para la cabecera
            HSSFCell cell = row.createCell(j);//se crea las celdas para la cabecera, junto con la posición
            cell.setCellStyle(style); // se añade el style crea anteriormente 
            cell.setCellValue(form.header[j]);//se añade el contenido
            hoja1.autoSizeColumn(j);

        }
        i++;
        /*RESTO DE INFORME*/

        for (FReporteMovilizadoPorConductorModelo obj : form.listaDeRegistros) {
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
            cell.setCellValue(obj.getNumeroManifiesto()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(2);
            cell.setCellValue(obj.getVehiculo()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(3);
            cell.setCellValue(obj.getEntregaTotal()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(4);
            cell.setCellValue(obj.getEntregaParcial()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(5);
            cell.setCellValue("" + obj.getDevolucionTotal()); // nombre del conductor
            //hoja1.autoSizeColumn(5);
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getReenvios()); // nombre del conductor
            //hoja1.autoSizeColumn(6);
            cell = row.createCell(7);
            cell.setCellValue(obj.getProgramados()); // nombre del conductor
            //hoja1.autoSizeColumn(7);
            cell = row.createCell(8);
            cell.setCellValue(obj.getCant_entregada()); // nombre del conductor
            //hoja1.autoSizeColumn(8);
            cell = row.createCell(9);
            cell.setCellValue(obj.getNombreCanal()); // nombre del conductor
            //hoja1.autoSizeColumn(9);
            cell = row.createCell(10);
            cell.setCellValue(obj.getSubtotal_facturas()); // nombre del conductor
            //hoja1.autoSizeColumn(10);

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
