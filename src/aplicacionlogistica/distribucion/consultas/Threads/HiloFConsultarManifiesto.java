/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.vehiculos.CCarros;
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
public class HiloFConsultarManifiesto implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FConsultarManifiestos fConsultarManifiestos = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFConsultarManifiesto(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param comando
     */
    public HiloFConsultarManifiesto(Inicio ini, FConsultarManifiestos fConsultarManifiesto, String comando) {
        this.ini = ini;
        this.fConsultarManifiestos = fConsultarManifiesto;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "manifiestoParaReimprimir":
                        manifiestoParaReimprimir();
                        break;
                    case "exportarExcel":
                        exportarExcel();
                        break;

                    case "sacarMinuta":
                        sacarMinuta();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fConsultarManifiestos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void manifiestoParaReimprimir() {

        try {

            // manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            this.fConsultarManifiestos.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(this.fConsultarManifiestos.txtManifiesto.getText().trim()));
            this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(true);

            // se valida que el manifiesto exista
            if (this.fConsultarManifiestos.manifiestoActual.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "Ese manifiesto no Existe en la BB DD"
                        + "\n La placa del vehiculo no esta asignada ", "Error", JOptionPane.WARNING_MESSAGE);

                this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                this.fConsultarManifiestos.txtManifiesto.requestFocus();
                this.fConsultarManifiestos.txtManifiesto.requestFocus();
            } else { //  si existe el vehículo, existe el manifiesto

                this.fConsultarManifiestos.manifiestoActual.setListaFacturasPorManifiesto();

                this.fConsultarManifiestos.manifiestoActual.setListaDeAuxiliares();
                //this.fConsultarManifiestos.manifiestoActual.setListaDeDescuentos();

                this.fConsultarManifiestos.txtManifiesto.setEditable(false);
                CCarros car = new CCarros(ini, this.fConsultarManifiestos.manifiestoActual.getVehiculo());
                this.fConsultarManifiestos.txtPlaca.setText(this.fConsultarManifiestos.manifiestoActual.getVehiculo());
                this.fConsultarManifiestos.txtCedulaConductor.setText(this.fConsultarManifiestos.manifiestoActual.getConductor());

                this.fConsultarManifiestos.txtCodigoManifiesto.setText(this.fConsultarManifiestos.manifiestoActual.codificarManifiesto());

                if (fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() < 1) {
                    JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "Ese manifiesto no Tiene Facturas asignadas "
                            + "", "Error", JOptionPane.WARNING_MESSAGE);
                    this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                    return;
                }

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (this.fConsultarManifiestos.manifiestoActual.getEstadoManifiesto()) {
                    case 0:
                        this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);

                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                        break;

                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
                        this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        if (fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() < 1) {
                            JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "Ese manifiesto no Tiene Facturas asignadas "
                                    + "", "Error", JOptionPane.WARNING_MESSAGE);
                            this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                            return;
                        } else {
                            llenarDatosManifiestoCerrado();
                        }

                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        if (fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() < 1) {
                            JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "Ese manifiesto no Tiene Facturas asignadas "
                                    + "", "Error", JOptionPane.WARNING_MESSAGE);
                            this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                            return;
                        } else {

                            llenarDatosManifiestoCerrado();
                        }
                        break;
                    case 5:// MANIFIESTO YA DESCARGADO
                        if (fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() < 1) {
                            JOptionPane.showInternalMessageDialog(this.fConsultarManifiestos, "Ese manifiesto no Tiene Facturas asignadas,\n"
                                    + "Manifiesto se encuentra anulado", "Error", JOptionPane.WARNING_MESSAGE);
                            this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
                            return;
                        }

                }

            }

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FConsultarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        this.fConsultarManifiestos.txtCanalVentas.setEnabled(true);
        this.fConsultarManifiestos.txtCanalVentas.setEditable(false);
        this.fConsultarManifiestos.txtCedulaConductor.setEnabled(true);
        this.fConsultarManifiestos.txtCedulaConductor.setEditable(false);
        this.fConsultarManifiestos.txtCodigoManifiesto.setEnabled(true);
        this.fConsultarManifiestos.txtCodigoManifiesto.setEditable(false);
        this.fConsultarManifiestos.txtNombreAuxiliar1.setEnabled(true);
        this.fConsultarManifiestos.txtNombreAuxiliar1.setEditable(false);
        this.fConsultarManifiestos.txtNombreAuxiliar2.setEnabled(true);
        this.fConsultarManifiestos.txtNombreAuxiliar2.setEditable(false);
        this.fConsultarManifiestos.txtNombreAuxiliar3.setEnabled(true);
        this.fConsultarManifiestos.txtNombreAuxiliar3.setEditable(false);
        this.fConsultarManifiestos.txtNombreConductor.setEnabled(true);
        this.fConsultarManifiestos.txtNombreConductor.setEditable(false);
        this.fConsultarManifiestos.txtNombreDespachador.setEnabled(true);
        this.fConsultarManifiestos.txtNombreDespachador.setEditable(false);
        this.fConsultarManifiestos.txtPlaca.setEnabled(true);
        this.fConsultarManifiestos.txtPlaca.setEditable(false);
        this.fConsultarManifiestos.txtNombreConductor.setEnabled(true);
        this.fConsultarManifiestos.txtNombreConductor.setEditable(false);

        this.fConsultarManifiestos.txtRuta.setEnabled(true);
        this.fConsultarManifiestos.txtRuta.setEditable(false);

        this.fConsultarManifiestos.btnNuevo.setEnabled(false);
        this.fConsultarManifiestos.btnNuevo.setEnabled(true);
        this.fConsultarManifiestos.btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        int cantidadFacturasEnManifiesto = 0;

        this.fConsultarManifiestos.modelo2 = (DefaultTableModel) this.fConsultarManifiestos.tblFacturasPorManifiesto.getModel();

        int promedio = this.fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() / 100;
        int i = 0;
        int valorBarra = 0;

        double valor = 0.0;
        String cadenaFacturas = "('";
        /* SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO , y se armauna cadena con
        los numeros de las facturas del manifiesto*/

        for (CFacturasPorManifiesto obj
                : this.fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto()) {
            cadenaFacturas += obj.getNumeroFactura() + "','";

            if (i > promedio) {
                i = 0;
                valorBarra++;
                this.fConsultarManifiestos.barra.setValue(valorBarra);

                Thread.sleep(10);

            }

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            int filaTabla2 = this.fConsultarManifiestos.tblFacturasPorManifiesto.getRowCount();
            this.fConsultarManifiestos.modelo2.addRow(new Object[this.fConsultarManifiestos.tblFacturasPorManifiesto.getRowCount()]);

            this.fConsultarManifiestos.tblFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            this.fConsultarManifiestos.tblFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            this.fConsultarManifiestos.tblFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            this.fConsultarManifiestos.tblFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   
            valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            this.fConsultarManifiestos.tblFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;

            i++;
            Thread.sleep(10);
        }

        cadenaFacturas = cadenaFacturas.substring(0, cadenaFacturas.length() - 2);
        cadenaFacturas += ");";

        /*Con la cadena se busca la lista de descuentos que tenga en el mnifiesto*/
        this.fConsultarManifiestos.manifiestoActual.setListaDeDescuentos(cadenaFacturas);

        // SE BUSCA EL conductor Y SE LLENA EL CAMPO
        this.fConsultarManifiestos.txtNombreConductor.setText(fConsultarManifiestos.manifiestoActual.getNombreConductor() + " "
                + fConsultarManifiestos.manifiestoActual.getApellidosConductor());

        /* Se llenan os campos de texto de los auiliares*/
        this.fConsultarManifiestos.llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        this.fConsultarManifiestos.txtNombreDespachador.setText(fConsultarManifiestos.manifiestoActual.getNombreDespachador() + " "
                + fConsultarManifiestos.manifiestoActual.getApellidosDespachador());

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        this.fConsultarManifiestos.txtCanalVentas.setText(fConsultarManifiestos.manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        this.fConsultarManifiestos.txtRuta.setText(fConsultarManifiestos.manifiestoActual.getNombreDeRuta());

        this.fConsultarManifiestos.txtCodigoManifiesto.setText(this.fConsultarManifiestos.manifiestoActual.codificarManifiesto());

        this.fConsultarManifiestos.barra.setValue(100);
        this.fConsultarManifiestos.lblCirculoDeProgreso.setVisible(false);
        this.fConsultarManifiestos.txtManifiesto.setEnabled(false);
        this.fConsultarManifiestos.btnExportar.setEnabled(true);
        this.fConsultarManifiestos.btnImprimir.setEnabled(true);
        this.fConsultarManifiestos.btnImprimir.requestFocus();

    }

    private void crearListaDeFacturasporManifiesto() {

        this.fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto();

    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "facturasPormanifiesto_" + fConsultarManifiestos.manifiestoActual.getNumeroManifiesto() + ".xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.fConsultarManifiestos.barra.setValue(0);

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

        int valor = fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto().size() / 100;
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

        for (CFacturasPorManifiesto obj : fConsultarManifiestos.manifiestoActual.getListaFacturasPorManifiesto()) {
            if (k > valor) {
                avance++;
                this.fConsultarManifiestos.barra.setValue((int) avance);
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
            this.fConsultarManifiestos.barra.setValue(100);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sacarMinuta() throws HeadlessException {
        String listadeproductos = null;
        int seleccion = JOptionPane.showOptionDialog(fConsultarManifiestos, "Seleccione Tipo de Minuta",
                "Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,// null para icono por defecto.
                new Object[]{"Completa", "Por Codigos", "Cancelar"}, "opcion 1");

        if (seleccion != -1) {
            System.out.println("seleccionada opcion " + (seleccion + 1));
            fConsultarManifiestos.manifiestoActual.sacarMinutaManifiesto(seleccion);
        }
    }
}
