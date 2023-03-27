/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.consultas.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
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
public class HiloFConsultarPedidosConductorHielera implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FConsultarPedidosConductorHielera fConsultarPedidosConductorHielera = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFConsultarPedidosConductorHielera(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fConsultarPedidosConductorHielera
     * @param comando
     */
    public HiloFConsultarPedidosConductorHielera(Inicio ini, FConsultarPedidosConductorHielera fConsultarPedidosConductorHielera, String comando) {
        this.ini = ini;
        this.fConsultarPedidosConductorHielera = fConsultarPedidosConductorHielera;
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
                        JOptionPane.showInternalMessageDialog(fConsultarPedidosConductorHielera, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void consultarConductor() {

        fConsultarPedidosConductorHielera.lblCirculoDeProgreso.setVisible(true);
        fConsultarPedidosConductorHielera.txtNombreConductor.setEnabled(false);
        fConsultarPedidosConductorHielera.dateManifiesto.setEnabled(false);
         Date fecha = new Date();
            fecha = ini.getFechaSql(fConsultarPedidosConductorHielera.dateManifiesto);
           
            try {
                for (CEmpleados cond : ini.getListaDeEmpleados()) {
                    String nombres = cond.getNombres() + " " + cond.getApellidos();
                    if (nombres.equals(fConsultarPedidosConductorHielera.txtNombreConductor.getText().trim())) {

                        fConsultarPedidosConductorHielera.conductor = new CEmpleados(ini, cond.getCedula());

                        break;
                    }
                    Thread.sleep(10);
                }
                if (fConsultarPedidosConductorHielera.conductor != null) {
                    fConsultarPedidosConductorHielera.conductor.setListaDeFacturaPorConductor("" + fecha);
                   
                    llenarDatosManifiestoCerrado();
                     fConsultarPedidosConductorHielera.jBtnMinuta.setEnabled(true);

                }
                 fConsultarPedidosConductorHielera.lblCirculoDeProgreso.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(FConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        

    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        this.fConsultarPedidosConductorHielera.txtCedulaConductor.setEnabled(true);
        this.fConsultarPedidosConductorHielera.txtCedulaConductor.setEditable(false);
        this.fConsultarPedidosConductorHielera.txtNombreConductor.setEnabled(true);
        this.fConsultarPedidosConductorHielera.txtNombreConductor.setEditable(false);
         this.fConsultarPedidosConductorHielera.txtPlaca.setEnabled(true);
        this.fConsultarPedidosConductorHielera.txtPlaca.setEditable(false);
        this.fConsultarPedidosConductorHielera.txtNombreConductor.setEnabled(true);
        this.fConsultarPedidosConductorHielera.txtNombreConductor.setEditable(false);

        this.fConsultarPedidosConductorHielera.btnNuevo.setEnabled(false);
        this.fConsultarPedidosConductorHielera.btnNuevo.setEnabled(true);
        this.fConsultarPedidosConductorHielera.btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        int cantidadFacturasEnManifiesto = 0;

        this.fConsultarPedidosConductorHielera.modelo2 = (DefaultTableModel) this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.getModel();

        int promedio = this.fConsultarPedidosConductorHielera.conductor.getListaDeFacturaPorConductor().size() / 100;
        int i = 0;
        int valorBarra = 0;

        double valor = 0.0;
        String cadenaFacturas = "('";
        /* SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO , y se armauna cadena con
        los numeros de las facturas del manifiesto*/

        for (CFacturasPorManifiesto obj
                : this.fConsultarPedidosConductorHielera.conductor.getListaDeFacturaPorConductor()) {
            cadenaFacturas += obj.getNumeroFactura() + "','";

            if (i > promedio) {
                i = 0;
                valorBarra++;
                this.fConsultarPedidosConductorHielera.barra.setValue(valorBarra);

                Thread.sleep(10);

            }

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            int filaTabla2 = this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.getRowCount();
            this.fConsultarPedidosConductorHielera.modelo2.addRow(new Object[this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.getRowCount()]);

            this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   
            valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            this.fConsultarPedidosConductorHielera.tblFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;

            i++;
            Thread.sleep(10);
        }

        cadenaFacturas = cadenaFacturas.substring(0, cadenaFacturas.length() - 2);
        cadenaFacturas += ");";

        /*Con la cadena se busca la lista de descuentos que tenga en el mnifiesto*/
       // this.fConsultarPedidosConductorHielera.manifiestoActual.setListaDeDescuentos(cadenaFacturas);

        // SE BUSCA EL conductor Y SE LLENA EL CAMPO
       // this.fConsultarPedidosConductorHielera.txtNombreConductor.setText(fConsultarPedidosConductorHielera.manifiestoActual.getNombreConductor() + " "
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

        this.fConsultarPedidosConductorHielera.barra.setValue(100);
        this.fConsultarPedidosConductorHielera.lblCirculoDeProgreso.setVisible(false);
      //  this.fConsultarPedidosConductorHielera.txtManifiesto.setEnabled(false);
        this.fConsultarPedidosConductorHielera.btnExportar.setEnabled(true);
         this.fConsultarPedidosConductorHielera.jBtnExportar.setEnabled(true);
        this.fConsultarPedidosConductorHielera.btnImprimir.setEnabled(true);
         this.fConsultarPedidosConductorHielera.jBtnImprimir.setEnabled(true);
        
        this.fConsultarPedidosConductorHielera.btnImprimir.requestFocus();

    }


    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "facturasPormanifiesto_" + fConsultarPedidosConductorHielera.conductor.getCedula() + ".xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.fConsultarPedidosConductorHielera.barra.setValue(0);

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

        int valor = fConsultarPedidosConductorHielera.conductor.getListaDeFacturaPorConductor().size() / 100;
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

        for (CFacturasPorManifiesto obj : fConsultarPedidosConductorHielera.conductor.getListaDeFacturaPorConductor()) {
            if (k > valor) {
                avance++;
                this.fConsultarPedidosConductorHielera.barra.setValue((int) avance);
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
            this.fConsultarPedidosConductorHielera.barra.setValue(100);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sacarMinuta() throws HeadlessException {
        String listadeproductos = null;
        int seleccion = JOptionPane.showOptionDialog(fConsultarPedidosConductorHielera, "Seleccione Tipo de Minuta",
                "Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,// null para icono por defecto.
                new Object[]{"Completa", "Por Codigos", "Cancelar"}, "opcion 1");

        if (seleccion != -1) {
            System.out.println("seleccionada opcion " + (seleccion + 1));
           // fConsultarPedidosConductorHielera.conductor.sacarMinutaPorConductor(seleccion);
        }
    }
}
