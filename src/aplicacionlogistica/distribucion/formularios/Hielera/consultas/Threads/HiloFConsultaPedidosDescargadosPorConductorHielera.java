/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.consultas.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
public class HiloFConsultaPedidosDescargadosPorConductorHielera implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FConsultaPedidosDescargadosPorConductorHielera fConsultaPedidosDescargadosPorConductorHielera = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFConsultaPedidosDescargadosPorConductorHielera(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fConsultarPedidosConductorHielera
     * @param comando
     */
    public HiloFConsultaPedidosDescargadosPorConductorHielera(Inicio ini, FConsultaPedidosDescargadosPorConductorHielera fConsultaPedidosDescargadosPorConductorHielera, String comando) {
        this.ini = ini;
        this.fConsultaPedidosDescargadosPorConductorHielera = fConsultaPedidosDescargadosPorConductorHielera;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "consultarConductor":
                        consultarConductor();
                        break;
                    case "exportarExcel":
                        exportarExcel();
                        break;

                    case "sacarMinuta":
                        sacarMinuta();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fConsultaPedidosDescargadosPorConductorHielera, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFConsultaPedidosDescargadosPorConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void consultarConductor() {

        fConsultaPedidosDescargadosPorConductorHielera.lblCirculoDeProgreso.setVisible(true);
        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEnabled(false);
        fConsultaPedidosDescargadosPorConductorHielera.dateManifiesto.setEnabled(false);
         Date fecha = new Date();
        // fecha = Inicio.getFechaSql(fConsultaPedidosDescargadosPorConductorHielera.dateManifiesto);
           
            try {
               
                if (fConsultaPedidosDescargadosPorConductorHielera.conductor != null) {
                    fConsultaPedidosDescargadosPorConductorHielera.conductor.setListaFacturasDescargadas(fConsultaPedidosDescargadosPorConductorHielera.manifiesto.getNumeroManifiesto(),true); 
                    
                    llenarDatosManifiestoCerrado();
                    fConsultaPedidosDescargadosPorConductorHielera.jBtnMinuta.setEnabled(true);

                }
                 fConsultaPedidosDescargadosPorConductorHielera.lblCirculoDeProgreso.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(HiloFConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

       // fConsultaPedidosDescargadosPorConductorHielera.txtCedulaConductor.setEnabled(true);
       // fConsultaPedidosDescargadosPorConductorHielera.txtCedulaConductor.setEditable(false);
        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEnabled(true);
        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEditable(false);
      //   fConsultaPedidosDescargadosPorConductorHielera.txtPlaca.setEnabled(true);
       // fConsultaPedidosDescargadosPorConductorHielera.txtPlaca.setEditable(false);
        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEnabled(true);
        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEditable(false);

        //fConsultaPedidosDescargadosPorConductorHielera.btnNuevo.setEnabled(false);
        fConsultaPedidosDescargadosPorConductorHielera.btnNuevo.setEnabled(true);
        fConsultaPedidosDescargadosPorConductorHielera.jBtnNuevo.setEnabled(true);
        
        fConsultaPedidosDescargadosPorConductorHielera.btnImprimir.setEnabled(false);
        fConsultaPedidosDescargadosPorConductorHielera.jBtnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        int cantidadFacturasEnManifiesto = 0;

       // fConsultaPedidosDescargadosPorConductorHielera.modelo2 = (DefaultTableModel) fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.getModel();

        int promedio = fConsultaPedidosDescargadosPorConductorHielera.conductor.getListaFacturasDescargadas().size() / 100;
        int i = 0;
       

        double valor = 0.0;
        String cadenaFacturas = "('";
        /* SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO , y se armauna cadena con
        los numeros de las facturas del manifiesto*/

         llenarjTableFacturasPorVehiculo();
        

       

        /*Con la cadena se busca la lista de descuentos que tenga en el mnifiesto*/
       // fConsultaPedidosDescargadosPorConductorHielera.manifiestoActual.setListaDeDescuentos(cadenaFacturas);

        // SE BUSCA EL conductor Y SE LLENA EL CAMPO
       // fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setText(fConsultaPedidosDescargadosPorConductorHielera.manifiestoActual.getNombreConductor() + " "
        //        + fConsultarManifiestos.manifiestoActual.getApellidosConductor());

        /* Se llenan os campos de texto de los auiliares*/
       // this.fConsultarManifiestos.llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
       // this.fConsultarManifiestos.txtNombreDespachador.setText(fConsultarManifiestos.manifiestoActual.getNombreDespachador() + " "
        //        + fConsultarManifiestos.manifiestoActual.getApellidosDespachador());

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
       // this.fConsultarManifiestos.txtCanalVentas.setText(fConsultarManifiestos.manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        //this.fConsultarManifiestos.txtRuta.setText(fConsultarManifiestos.manifiestoActual.getNombreDeRuta());

       // this.fConsultarManifiestos.txtCodigoManifiesto.setText(this.fConsultarManifiestos.manifiestoActual.codificarManifiesto());

       // fConsultaPedidosDescargadosPorConductorHielera.barra.setValue(100);
        fConsultaPedidosDescargadosPorConductorHielera.lblCirculoDeProgreso.setVisible(false);
      //  fConsultaPedidosDescargadosPorConductorHielera.txtManifiesto.setEnabled(false);
 //       fConsultaPedidosDescargadosPorConductorHielera.btnExportar.setEnabled(true);
  //       fConsultaPedidosDescargadosPorConductorHielera.jBtnExportar.setEnabled(true);
        fConsultaPedidosDescargadosPorConductorHielera.btnImprimir.setEnabled(true);
         fConsultaPedidosDescargadosPorConductorHielera.jBtnImprimir.setEnabled(true);
        
        fConsultaPedidosDescargadosPorConductorHielera.btnImprimir.requestFocus();

    }


    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "facturasPormanifiestoDescargadas_" + fConsultaPedidosDescargadosPorConductorHielera.conductor.getCedula() + ".xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
       // fConsultaPedidosDescargadosPorConductorHielera.barra.setValue(0);

        //cabecera de la hoja de excel
        String[] header = new String[]{"consecutivo", "adherencia", "numeroManifiesto",
            "fechaDistribucion", "vehiculo", "conductor",
            "nombreConductor", "despachador", "NombreDespachador", "ruta", "nombreRuta",
            "numeroFactura", "fechaDeVenta", "cliente", "nombreDeCliente", "direccionCliente",
            "vendedor", "nombreCanal", "valorFacturaSinIva", "valorIvaFactura",
            "valorTotalFactura", "salidasDistribucion", "latitud", "longitud"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);

        int valor = fConsultaPedidosDescargadosPorConductorHielera.conductor.getListaFacturasDescargadas().size() / 100;
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

        for (CFacturasPorManifiesto obj : fConsultaPedidosDescargadosPorConductorHielera.conductor.getListaFacturasDescargadas()) {
            
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
//            fConsultaPedidosDescargadosPorConductorHielera.barra.setValue(100);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sacarMinuta() throws HeadlessException {
        String listadeproductos = null;
        int seleccion = JOptionPane.showOptionDialog(fConsultaPedidosDescargadosPorConductorHielera, "Seleccione Tipo de Minuta",
                "Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,// null para icono por defecto.
                new Object[]{"Completa", "Por Codigos", "Cancelar"}, "opcion 1");

        if (seleccion != -1) {
            System.out.println("seleccionada opcion " + (seleccion + 1));
            //fConsultaPedidosDescargadosPorConductorHielera.conductor.sacarMinutaManifiesto(seleccion);
        }
    }
    
     public void llenarjTableFacturasPorVehiculo() throws Exception {

        fConsultaPedidosDescargadosPorConductorHielera.modelo2 = (DefaultTableModel) fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.getModel();
        if (fConsultaPedidosDescargadosPorConductorHielera.modelo2.getRowCount() > 0) {
            int a = fConsultaPedidosDescargadosPorConductorHielera.modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                fConsultaPedidosDescargadosPorConductorHielera.modelo2.removeRow(i);
            }
        }

        fConsultaPedidosDescargadosPorConductorHielera.txtNombreConductor.setEditable(false);

        if (fConsultaPedidosDescargadosPorConductorHielera.conductor.getListaFacturasDescargadas() != null) {

            for (CFacturasPorManifiesto obj : fConsultaPedidosDescargadosPorConductorHielera.conductor.getListaFacturasDescargadas()) {
                obj.setTipoDeDEscargue();

                int fila = fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.getRowCount();
                fConsultaPedidosDescargadosPorConductorHielera.modelo2.addRow(new Object[fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.getRowCount()]);
                fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fila + 1), fila, 0);  // item
                fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de factura
                fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNombreDeCliente(), fila, 2); // nombre del nombreDelCliente
                fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getTipoDeDEscargue(), fila, 3); // nombre del nombreDelCliente
                fConsultaPedidosDescargadosPorConductorHielera.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(obj.getValorRecaudado()), fila, 4);

                fConsultaPedidosDescargadosPorConductorHielera.valorTotalRecaudado += obj.getValorRecaudado();

            }
        }

    }
}
