/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.threads;

import aplicacionlogistica.distribucion.Threads.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDePorteria;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import com.opencsv.CSVWriter;
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
import java.util.Date;
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
 * @author Usuario
 */
public class HiloFReporteDePorteria implements Runnable {

    public static boolean band = false;

    Inicio ini;
    ArrayList<Vst_Factura> listaDeFacturasSinMovimiento = null;
    boolean xxx;

    FReporteDePorteria form;

    public HiloFReporteDePorteria(FReporteDePorteria form) {
        this.ini = form.ini;
        this.form = form;

    }
        public HiloFReporteDePorteria(FReporteDePorteria form, boolean xxx) {
        this.form = form;
        this.xxx = xxx;
        this.ini = form.ini;
    }

    @Override
    public void run() {
        
        this.form.lblCirculoDeProgreso.setVisible(true);
        if(xxx){
        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeFacturasSinMovimiento");

        String sql = null;
        this.form.fechaFinal = ini.getFechaSql(this.form.jFechaFinal);

        
        sql = "SELECT * "
                + "FROM vst_defintivofacturaspormanifiesto "
                + "where "
                + "fechaDistribucion='" + this.form.fechaFinal + "' "
                + "order by fechaDespachado";
                        
           /* sql = "SELECT  numeroFactura,fechaDeVenta,idCliente, nitCliente,"
                    + "nombreDeCliente,direccionDeCliente,barrio,ciudad,telefonoCliente,emailCliente,"
                    + "latitud,longitud,nombreEstablecimiento,vendedor,telefonoVendedor,formaDePago,"
                    + "pago,canal,nombreCanalDeVenta,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                    + "valorRechazo, valorDescuento,valorTotalRecaudado,zona,regional,agencia,isFree,"
                    + "estadoFactura,if(estadoFactura=1,'SIN  MOVIMIENTO',nombreEstadoFactura) AS nombreEstadoFactura,"
                    + "activo,salidasDistribucion,trasmitido,"
                    + "numeroDescuento,numeroRecogida, pesofactura "
                    + "FROM vst_fcturas "
                    + "where fechaDeVenta>='" + this.form.fechaIncial + "' "
                    + "and fechaDeVenta<='" + this.form.fechaFinal + "' "
                    + "and direccionDeCliente<>'ANULADA' "
                    + "and salidasDistribucion < 1 or (fechaDeVenta>='" + this.form.fechaIncial + "' "
                    + "and fechaDeVenta<='" + this.form.fechaFinal + "' "
                    + "and salidasDistribucion > 0 and "
                    + "( estadoFactura=6 or estadoFactura=7)) "
                    + "or((estadoFactura = 1 and isFree = 0) and fechaDeVenta>='" + this.form.fechaIncial + "' and fechaDeVenta<='" + this.form.fechaFinal + "') "
                    + "order by isFree;"; **/


        try {
            if (con != null) {
                this.form.listaDeFacturasPorManifiesto=new ArrayList<>();
                st = con.createStatement();
                rst = st.executeQuery(sql);
                rst.last();
                int valor = rst.getRow();
                this.form.barra.setValue(0);
                rst.first();

                valor = valor / 100;
                int i = 0;
                int avance = 0;
                rst.beforeFirst();
                
                 /*Se genera un nombre aleatorio para el archivo*/
                    String clave = UUID.randomUUID().toString().substring(0, 8);
                   

                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/" + clave +".csv"),'\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));
                        
                    }
                
                    this.form.reporteFinal=new File("tmp/" + clave +".csv");
                    
                   // Desktop.getDesktop().open(new File("tmp/" + clave +".csv"));
                
                                
                
                /**************************************************************/
                  rst.beforeFirst();
                  
                while (rst.next()) {
                    if (i > valor) {
                        avance++;
                        this.form.barra.setValue((int) avance);
                        i = 0;
                    }
                    
                    llenarRegistro(rst);

                   // this.form.listaDeFacturasPorManifiesto.add(registro);
                    i++;
                    Thread.sleep(10);

                }
                this.form.barra.setValue(100);
                 this.form.jBtnExcel1.setEnabled(true);
                 
                this.form.llenarTablaListaDeFacturas();
                
                this.form.btnExportarExcel.setEnabled(true);
                this.form.btnAceptar.setEnabled(true);        
                this.form.lblCirculoDeProgreso.setVisible(false);
            }
            rst.close();
            st.close();
            con.close();
            if (this.form.listaDeFacturasPorManifiesto.isEmpty()) {
                JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            }

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloFReporteDePorteria.class.getName()).log(Level.SEVERE, null, ex);
        }  
        }else{
           try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFReporteDePorteria.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }

        

    }

    private void llenarRegistro(ResultSet rst) throws InterruptedException {

        try {
            System.out.println("Cargando  -> " + new Date());
            CFacturasPorManifiesto factura = new CFacturasPorManifiesto(ini);

            factura.setNumeroFactura(rst.getString("numeroFactura"));
            factura.setNumeroManifiesto(rst.getString("numeroManifiesto"));
            factura.setFechaDistribucion("" + rst.getDate("fechaDistribucion"));
            factura.setCliente(rst.getString("cliente"));
            factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
            factura.setDireccionDeCliente(rst.getString("direccionDeCliente"));
            factura.setBarrio(rst.getString("barrio"));
            factura.setTelefonoCliente(rst.getString("telefonoCliente"));
            factura.setVendedor(rst.getString("vendedor"));
            factura.setFormaDePago(rst.getString("formaDePago"));
            factura.setIdCanalDeVenta(rst.getInt("idCanal"));
            factura.setNombreCanalDeVenta(rst.getString("nombreCanalDeVenta"));
            factura.setIsFree(rst.getInt("isFree"));
            factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
            factura.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
            factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
            factura.setValorRechazo(rst.getDouble("valorRechazo"));
            factura.setValorDescuento(rst.getDouble("valorDescuento"));
            factura.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
            factura.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
            factura.setDespachado(rst.getInt("despachado"));
            factura.setNombreConductor(rst.getString("nombreConductor"));
            factura.setFechaDeVenta( rst.getDate("fechaDeVenta"));
            factura.setUsuariodespachador(rst.getString("usuariodespachador"));
            factura.setFechaDespachado(rst.getString("fechaDespachado") );
            factura.setMinutos(rst.getInt("minutos"));
            factura.setFechaIng(rst.getString("fechaIng"));
          //  factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
           

            this.form.listaDeFacturasPorManifiesto.add(factura);

            System.out.println("Cargando facturaPor Manifiesto número -> " + factura.getNumeroFactura());

            //arrFacturasPorManifiesto.add(fxm);
            System.out.println("tiempo 2 " + new Date());
            Thread.sleep(10);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloFReporteDePorteria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloFReporteDePorteria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "Inventario.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
         this.form.barra.setValue(0);
         this.form.jLabel1.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);
      
          new Thread(new HiloAux(form.barra,form.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header = new String[]{ "numeroFactura", "fechaDeVenta","nombreCanalDeVenta", "cliente",
    "nombreDeCliente","direccionDeCliente","barrio","ciudad","telefonoCliente",
    "vendedor","valorFacturaSinIva","valorTotalFactura"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);

        int valor = form.listaDeFacturasPorManifiesto.size() / 100;
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
            Thread.sleep(10);
        }
        i++;
        /*RESTO DE INFORME*/

        for (CFacturasPorManifiesto obj : form.listaDeFacturasPorManifiesto) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
                k = 0;
                 Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getNumeroFactura());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue("" + obj.getFechaDeVenta()); // numero de manifiesto
            
            cell = row.createCell(2);
            cell.setCellValue("" + obj.getNombreCanalDeVenta()); // numero de manifiesto
             //hoja1.autoSizeColumn(1);
            cell = row.createCell(3);
            cell.setCellValue(obj.getCliente()); // fecha de distribucion
             //hoja1.autoSizeColumn(2);
            cell = row.createCell(4);
            cell.setCellValue(obj.getNombreDeCliente()); // placa del vehiculo
             //hoja1.autoSizeColumn(3);
            cell = row.createCell(5);
            cell.setCellValue(obj.getDireccionDeCliente()); // nombre del conductor
             //hoja1.autoSizeColumn(4);
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getBarrio()); // nombre del conductor
             //hoja1.autoSizeColumn(5);
            cell = row.createCell(7);
          //  cell.setCellValue("" + obj.getCiudadCliente()); // nombre del conductor
             //hoja1.autoSizeColumn(6);
            cell = row.createCell(8);
            cell.setCellValue(obj.getTelefonoCliente()); // nombre del conductor
             //hoja1.autoSizeColumn(7);
            cell = row.createCell(9);
            cell.setCellValue(obj.getVendedor()); // nombre del conductor
             //hoja1.autoSizeColumn(8);
            cell = row.createCell(10);
            cell.setCellValue(obj.getValorFacturaSinIva()); // nombre del conductor
             //hoja1.autoSizeColumn(9);
            cell = row.createCell(11);
            cell.setCellValue(obj.getValorTotalFactura()); // nombre del conductor
             //hoja1.autoSizeColumn(10);
            
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
