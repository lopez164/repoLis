/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Reportes;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.DocumentosPorVehiculo;
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
public class HiloReporteDocumentosFlota implements Runnable {

    FReporteDocumentosVehiculos form;
    public List<DocumentosPorVehiculo> listaDeRegistros;
    boolean xxx = true;

    public HiloReporteDocumentosFlota(FReporteDocumentosVehiculos form) {
        this.form = form;

    }

    public HiloReporteDocumentosFlota(FReporteDocumentosVehiculos form, boolean xxx) {
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

            /* Sentencia sql para crear los objetos */
            String sql = "select v.placa, v.tipoVehiculo,dtv.idTipoDocumento,td.nombreTipoDocumento,dpv.numeroDocumento, "
                    + "ifnull(dpv.fechaVencimiento,'PENDIENTE') AS fechaVencimiento, dpv.lugarExpedicion,dpv.entidadEmisora "
                    + "from vehiculos v "
                    + "join documentosportipodevehiculo dtv on dtv.idTipoVehiculo=v.tipoVehiculo "
                    + "join tiposdocumentos td on td.idtiposDocumentos=dtv.idTipoDocumento "
                    + "left outer join documentosporvehiculo dpv on dpv.idTipoDocumento=dtv.idTipoDocumento and dpv.placa=v.placa and dpv.activo=1 "
                    + "where v.tipocontrato=1 "
                    + "order by fechaVencimiento asc; ";

            con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());

            try {
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);

                    /*Se genera un nombre aleatorio para el archivo*/
                    String clave = UUID.randomUUID().toString().substring(0, 8);

                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/documentosFlota_" + clave + ".csv"), '\t')) {
                        //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));
                        form.informe = new  File("tmp/documentosFlota_" + clave + ".csv");

                    }

                    rst.last();
                    int totalRegistros = (rst.getRow() / 1);

                    rst.beforeFirst();
                    DefaultTableModel modelo = (DefaultTableModel) form.tblDocumentosPorVehiculo.getModel();

                    String respuesta = "VIGENTE";
                    int valor = 0;
                    this.form.barra.setValue(0);
                    valor = totalRegistros / 100;
                    int i = 0;
                    int avance = 0;
                    int x = 0;
                    while (rst.next()) {
                        if (x > valor) {
                            avance++;
                            this.form.barra.setValue((int) avance);
                            x = 0;
                        } else {
                            x++;
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        int filaTabla1 = form.tblDocumentosPorVehiculo.getRowCount();

                        if (rst.getString("fechaVencimiento").equals("PENDIENTE")) {
                            respuesta = "PEDIENTE";

                        } else {
                            Date fechaActual = new Date();
                            String fechaSistema = dateFormat.format(fechaActual);
                            respuesta = "VIGENTE";

                            Date date1 = dateFormat.parse(rst.getString("fechaVencimiento"));
                            Date date2 = dateFormat.parse(fechaSistema);

                            if (date1.before(date2)) {
                                respuesta = "VENCIDO";
                            }
                        }

                        modelo.addRow(new Object[form.tblDocumentosPorVehiculo.getRowCount()]);

                        form.tblDocumentosPorVehiculo.setValueAt(filaTabla1 + 1, filaTabla1, 0);  // item
                        form.tblDocumentosPorVehiculo.setValueAt(rst.getString("placa"), filaTabla1, 1);  // item
                        form.tblDocumentosPorVehiculo.setValueAt(rst.getString("nombreTipoDocumento"), filaTabla1, 2); // numero de manifiesto
                         form.tblDocumentosPorVehiculo.setValueAt(rst.getString("numeroDocumento"), filaTabla1, 3); // numero de manifiesto
                        form.tblDocumentosPorVehiculo.setValueAt(rst.getString("entidadEmisora"), filaTabla1, 4); // numero de manifiesto
                        form.tblDocumentosPorVehiculo.setValueAt(rst.getString("lugarExpedicion"), filaTabla1, 5); // numero de manifiesto
                        form.tblDocumentosPorVehiculo.setValueAt(rst.getString("fechaVencimiento"), filaTabla1, 6); // numero de manifiesto
                        form.tblDocumentosPorVehiculo.setValueAt(respuesta, filaTabla1, 7); // numero de manifiesto
                    }

                    rst.close();
                    st.close();
                    con.close();

                    this.form.barra.setValue(100);
                    //this.form.llenarTablabitacora();
                   // Desktop.getDesktop().open(new File("tmp/documentosFlota" + clave + ".csv"));
                   this.form.btnExportarExcel.setEnabled(true);

                }
                this.form.lblCirculoDeProgreso.setVisible(false);
//                if (this.form.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(HiloReporteDocumentosFlota.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloReporteDocumentosFlota.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private Vst_FacturasDescargadas llenarRegistro(ResultSet rst) {
        Vst_FacturasDescargadas registro = new Vst_FacturasDescargadas();
        try {

            registro.setIdCanal(rst.getInt("idCanal"));
            registro.setNombreCanal(rst.getString("nombreCanal"));
//            registro.setNumeroManifiesto(rst.getString("numeroManifiesto"));
//            registro.setAdherencia(rst.getInt("adherencia"));
//            registro.setNumeroFactura(rst.getString("numeroFactura"));
//            registro.setFechaDistribucion(rst.getDate("fechaDistribucion"));
//            registro.setFechaDeVenta(rst.getDate("fechaDeVenta"));
//            registro.setVehiculo(rst.getString("vehiculo"));
//            registro.setTipoContrato(rst.getString("tipoContrato"));
//            registro.setConductor(rst.getString("conductor"));
//            registro.setNombreConductor(rst.getString("nombreConductor"));
//            registro.setNombreDespachador(rst.getString("nombreDespachador"));
//            registro.setNombreDeRuta(rst.getString("nombreDeRuta"));
//            registro.setTipoRuta(rst.getString("tipoRuta"));
//            registro.setNombreDeCliente(rst.getString("nombreDeCliente"));
//            registro.setDireccion(rst.getString("direccion"));
//            registro.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
//            registro.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
//            registro.setValorRechazo(rst.getDouble("valorRechazo"));
//            registro.setValorDescuento(rst.getDouble("valorDescuento"));
//            registro.setValorRecaudado(rst.getDouble("valorRecaudado"));
//            registro.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
//            registro.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
//            registro.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
//            registro.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
//            registro.setCompetencia(rst.getString("competencia"));
//            registro.setVendedor(rst.getString("vendedor"));
//            registro.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
//            registro.setFechaReal(rst.getDate("fechaReal"));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteDocumentosFlota.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registro;
    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "Inventario.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form.barra.setValue(0);
        this.form.jLabel1.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(form.barra, form.jLabel1)).start();

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

        for (Vst_FacturasDescargadas obj : form.listaDeRegistros) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
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
