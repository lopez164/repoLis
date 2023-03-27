/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.formularios.FArqueodeManifiesto;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
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
import java.util.List;
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
public class HiloListadoDeFacturasPorManifiesto implements Runnable {
    
    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini;
    CManifiestosDeDistribucion manifiesto = null;
    DescargarFacturas form = null;
    FConsultarManifiestos form1 = null;
    FArqueodeManifiesto fArqueodeManifiesto;
    List<CFacturasPorManifiesto> listaFacturasPorManifiesto;
    Object ob;
    ResultSet rst = null;
    String numeroDeManifiesto;
    boolean exportar = false;

    /**
     * Constructor de clase
     */
    public HiloListadoDeFacturasPorManifiesto(Inicio ini, int tiempo, CManifiestosDeDistribucion manifiesto) {
        this.tiempo = tiempo;
        this.ini = ini;
        this.manifiesto = manifiesto;
        numeroDeManifiesto = manifiesto.getNumeroManifiesto();
    }
    
    public HiloListadoDeFacturasPorManifiesto(Inicio ini, int tiempo, DescargarFacturas form) {
        this.tiempo = tiempo;
        this.ini = ini;
        this.form = form;
        numeroDeManifiesto = form.manifiestoActual.getNumeroManifiesto();
        
    }
    
    public HiloListadoDeFacturasPorManifiesto(Inicio ini, FConsultarManifiestos form1, boolean exportar) {
        this.tiempo = tiempo;
        this.ini = ini;
        this.form1 = form1;
        numeroDeManifiesto = form1.manifiestoActual.getNumeroManifiesto();
        this.exportar = exportar;
        
    }
    
     public HiloListadoDeFacturasPorManifiesto(FArqueodeManifiesto fArqueodeManifiesto) {
       
        this.ini = fArqueodeManifiesto.ini;
        this.fArqueodeManifiesto = fArqueodeManifiesto;
        numeroDeManifiesto = fArqueodeManifiesto.manifiestoActual.getNumeroManifiesto();
        
        
    }
    
    @Override
    public void run() {
        if (exportar) {
            try {
                crearListaDeFacturasporManifiesto();
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloListadoDeFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            
            crearListaDeFacturasporManifiesto();
        }
        
    }
    
    private void crearListaDeFacturasporManifiesto() throws HeadlessException {
        ResultSet rst = null;
        Statement st;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeFacturasPorManifiesto");
        
        con = ini.getConnRemota();
        
        String sql = null;
        
        listaFacturasPorManifiesto = new ArrayList();
        // manifiesto.listadoDeFacturas(manifiesto.getNumeroManifiesto());
        sql = "SELECT * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "WHERE "
                + "numeroManifiesto='" + numeroDeManifiesto + "' ORDER BY adherencia ASC ";
        
        try {
            
            if (con != null) {
                
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));
                    
                    System.out.println("Cargando vst_factura en manifiesto -> " + fxm.getNumeroFactura());
                    
                    listaFacturasPorManifiesto.add(fxm);
                }
                rst.close();
                st.close();
               // con.close();
                if (this.manifiesto != null) {
                    this.manifiesto.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
                }
                if(fArqueodeManifiesto != null){
                    fArqueodeManifiesto.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
                }
                
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "facturasPormanifiesto_" + form1.manifiestoActual.getNumeroManifiesto() + ".xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form1.barra.setValue(0);

        //cabecera de la hoja de excel
        String[] header = new String[]{"consecutivo", "adherencia", "numeroManifiesto",
            "fechaDistribucion", "vehiculo", "conductor",
            "nombreConductor", "despachador", "NombreDespachador", "ruta", "nombreRuta",
            "numeroFactura", "fechaDeVenta", "cliente", "nombreDeCliente", "direccionCliente", 
            "vendedor","nombreCanal", "valorFacturaSinIva", "valorIvaFactura",
            "valorTotalFactura", "salidasDistribucion","latitud","longitud"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);
        
        int valor = form1.manifiestoActual.getListaFacturasPorManifiesto().size() / 100;
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
        
        for (CFacturasPorManifiesto obj : form1.manifiestoActual.getListaFacturasPorManifiesto()) {
            if (k > valor) {
                avance++;
                this.form1.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getConsecutivo());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue("" + obj.getAdherencia()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(2);
            cell.setCellValue(obj.getNumeroManifiesto()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(3);
            cell.setCellValue(obj.getFechaDistribucion()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(4);
            cell.setCellValue(obj.getVehiculo()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(5);
            cell.setCellValue("" + obj.getConductor()); // nombre del conductor
            //hoja1.autoSizeColumn(5);
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getNombreConductor()); // nombre del conductor
            //hoja1.autoSizeColumn(6);
            cell = row.createCell(7);
            cell.setCellValue(obj.getDespachador()); // nombre del conductor
            //hoja1.autoSizeColumn(7);
            cell = row.createCell(8);
            cell.setCellValue(obj.getNombreDespachador()); // nombre del conductor
            //hoja1.autoSizeColumn(8);
            cell = row.createCell(9);
            cell.setCellValue(obj.getRuta()); // nombre del conductor
            //hoja1.autoSizeColumn(9);
            cell = row.createCell(10);
            cell.setCellValue(obj.getNombreDeRuta()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(11);
            cell.setCellValue(obj.getNumeroFactura()); // nombre del conductor
            //hoja1.autoSizeColumn(10);

            cell = row.createCell(12);
            cell.setCellValue("" + obj.getFechaDeVenta()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(13);
            cell.setCellValue(obj.getCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(14);
            cell.setCellValue(obj.getNombreDeCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(15);
            cell.setCellValue(obj.getDireccionDeCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(10);

            cell = row.createCell(16);
            cell.setCellValue(obj.getVendedor()); // nombre del conductor
            //hoja1.autoSizeColumn(10);

            cell = row.createCell(17);
            cell.setCellValue(obj.getNombreCanal()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            cell = row.createCell(18);
            cell.setCellValue(obj.getValorFacturaSinIva()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            
             cell = row.createCell(19);
            cell.setCellValue(obj.getValorIvaFactura()); // nombre del conductor
            
            cell = row.createCell(20);
            cell.setCellValue(obj.getValorTotalFactura()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
           
            //hoja1.autoSizeColumn(10);

            cell = row.createCell(21);
            cell.setCellValue(obj.getSalidasDistribucion()); // nombre del conductor
            //hoja1.autoSizeColumn(10);
            
            cell = row.createCell(22);
            cell.setCellValue(obj.getLatitud()); // nombre del conductor

            cell = row.createCell(23);
            cell.setCellValue(obj.getLongitud()); // nombre del conductor

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
            
            fileOuS.flush();
            fileOuS.close();
            
            System.out.println("Archivo Creado");
            
            Desktop.getDesktop().open(new File(rutaArchivo));
            this.form1.barra.setValue(100);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
