/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Reportes;

import aplicacionlogistica.distribucion.Threads.HiloAux;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.DocumentosPorVehiculo;
import mtto.proveedores.HiloIngresarProveedores;
import mtto.vehiculos.CCarros;
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
public class HiloReporteListadoCarrosPropios implements Runnable {

    FReporteListadoCarrosPropios fReporteListadoCarrosPropios = null;
    public List<DocumentosPorVehiculo> listaDeRegistros;
    String caso = "";

    public HiloReporteListadoCarrosPropios(FReporteDocumentosVehiculos form) {
        this.fReporteListadoCarrosPropios = fReporteListadoCarrosPropios;

    }

    public HiloReporteListadoCarrosPropios(FReporteListadoCarrosPropios fReporteListadoCarrosPropios, String comando) {
        this.fReporteListadoCarrosPropios = fReporteListadoCarrosPropios;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "llenarJtable":
                        llenarJtable();
                        break;

                    case "exportarExcel":
                        exportarExcel();

                        break;

                    case "cargarFormulario":
                        cargarFormulario();

                        break;
                    default:
                        JOptionPane.showInternalMessageDialog(fReporteListadoCarrosPropios, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }

                /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarJtable() {
        int valorBarra = 0;
        int valorIncre = fReporteListadoCarrosPropios.ini.getListaDeCarrosPropios().size() / 100;

        this.fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(true);
        this.fReporteListadoCarrosPropios.listaDeRegistros = new ArrayList();

        /*Se genera un nombre aleatorio para el archivo*/
        String clave = UUID.randomUUID().toString().substring(0, 8);

        fReporteListadoCarrosPropios.rutaInforme = "tmp/ListaDeCarros" + clave + ".xls";
        DefaultTableModel modelo = (DefaultTableModel) fReporteListadoCarrosPropios.tblListaDeCarrosPropios.getModel();

        int i = 0;
        for (CCarros car : fReporteListadoCarrosPropios.ini.getListaDeCarrosPropios()) {

            if (i > valorIncre) {
                valorBarra += valorIncre;
                i = 0;
            }
            int filaTabla1 = fReporteListadoCarrosPropios.tblListaDeCarrosPropios.getRowCount();

            modelo.addRow(new Object[fReporteListadoCarrosPropios.tblListaDeCarrosPropios.getRowCount()]);

            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(filaTabla1 + 1, filaTabla1, 0);  // item
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getPlaca(), filaTabla1, 1);  // item
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getNombreLineaVehiculo(), filaTabla1, 2); // numero de manifiesto
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getNombreMarcaDeVehiculo(), filaTabla1, 3); // numero de manifiesto
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getNombreConductor() + " " + car.getApellidosConductor(), filaTabla1, 4); // numero de manifiesto
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getModelo(), filaTabla1, 5); // numero de manifiesto
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getNombreTipoVehiculo(), filaTabla1, 6); // numero de manifiesto
            fReporteListadoCarrosPropios.tblListaDeCarrosPropios.setValueAt(car.getNombreTipoServicio(), filaTabla1, 7); // numero de manifiesto
            i++;
        }

        this.fReporteListadoCarrosPropios.barra.setValue(100);
        this.fReporteListadoCarrosPropios.btnExportarExcel.setEnabled(true);

        this.fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(false);
//                if (this.fReporteListadoCarrosPropios.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }
    }

    public void exportarExcel() throws InterruptedException {
        //String nombreArchivo = "Inventario.xls";
        String rutaArchivo = fReporteListadoCarrosPropios.rutaInforme;
        String hoja = "Hoja1";
        this.fReporteListadoCarrosPropios.barra.setValue(0);
        this.fReporteListadoCarrosPropios.jLabel1.setVisible(true);
        this.fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(fReporteListadoCarrosPropios.barra, fReporteListadoCarrosPropios.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header = new String[]{"Placa", "LineaVehiculo", "MarcaVehiculo", "conductor", "Modelo",
            "TipoVehiculo", "TipoServicio","serialChasis","serialMotor","tipoCombustible","tipoCarroceria","C.I.","Max P.A."};

        //contenido de la hoja de excel
        //String[][] document = new String[fReporteListadoCarrosPropios.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);

        int valor = fReporteListadoCarrosPropios.ini.getListaDeCarrosPropios().size() / 100;
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

        
        for (CCarros car : fReporteListadoCarrosPropios.ini.getListaDeCarrosPropios()) {
            if (k > valor) {
                avance++;
                this.fReporteListadoCarrosPropios.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(car.getPlaca());  // item
             hoja1.autoSizeColumn(0);
             
            cell = row.createCell(1);
            cell.setCellValue(car.getNombreLineaVehiculo()); // numero de manifiesto
            hoja1.autoSizeColumn(1);
            
            cell = row.createCell(2);
            cell.setCellValue(car.getNombreMarcaDeVehiculo()); // fecha de distribucion
            hoja1.autoSizeColumn(2);
            
            cell = row.createCell(3);
            cell.setCellValue(car.getNombreConductor() + " " + car.getApellidosConductor()); // placa del vehiculo
            hoja1.autoSizeColumn(3);
            
            cell = row.createCell(4);
            cell.setCellValue(car.getModelo()); // nombre del conductor
            hoja1.autoSizeColumn(4);
            
            cell = row.createCell(5);
            cell.setCellValue(car.getNombreTipoVehiculo()); // nombre del conductor
            hoja1.autoSizeColumn(5);
            
            cell = row.createCell(6);
            cell.setCellValue(car.getNombreTipoServicio()); // nombre del conductor
            hoja1.autoSizeColumn(6);
            
            cell = row.createCell(7);
            cell.setCellValue(car.getSerialChasis()); // nombre del conductor
            hoja1.autoSizeColumn(7);  // nnx78f cuadrante 12 ospina perez patrullero Correa

             cell = row.createCell(8);
            cell.setCellValue(car.getSerialMotor()); // nombre del conductor
            hoja1.autoSizeColumn(8); 
            
             cell = row.createCell(9);
            cell.setCellValue(car.getNombreTipoCombustible()); // nombre del conductor
            hoja1.autoSizeColumn(9); 
            
            cell = row.createCell(10);
            cell.setCellValue(car.getNombreTipoCarroceria()); // nombre del conductor
            hoja1.autoSizeColumn(10); 
            
            cell = row.createCell(11);
            cell.setCellValue(car.getCapacidadInstalada()); // nombre del conductor
            hoja1.autoSizeColumn(11); 
            
             cell = row.createCell(12);
            cell.setCellValue(car.getPesoTotalAutorizado()); // nombre del conductor
            hoja1.autoSizeColumn(12); 
            

            
            
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
            this.fReporteListadoCarrosPropios.barra.setValue(100);
            this.fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(false);
            this.fReporteListadoCarrosPropios.jLabel1.setText("Archivo exportado satisfactoriamente");
            fileOuS.flush();
            fileOuS.close();

            System.out.println("Archivo Creado");

            Desktop.getDesktop().open(new File(rutaArchivo));

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private void cargarFormulario() {
        fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(true);
        fReporteListadoCarrosPropios.jLabel1.setVisible(false);

        if (fReporteListadoCarrosPropios.ini.getListaDeCarrosPropios() == null) {
            fReporteListadoCarrosPropios.ini.setListaDeCarrosPropios();
        }

        fReporteListadoCarrosPropios.cuadrarFechaJDateChooser();
        fReporteListadoCarrosPropios.lblCirculoDeProgreso.setVisible(false);
        fReporteListadoCarrosPropios.jBtnNuevo.setEnabled(true);
        fReporteListadoCarrosPropios.jbtnExportarExcel.setEnabled(true);
        fReporteListadoCarrosPropios.jbtnInforme.setEnabled(true);
        fReporteListadoCarrosPropios.btnAceptar.setEnabled(true);

    }

}
