/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lopez164
 */
public class ReporteDescargueDeFacturas {

    String mfto;
   Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    double valoraConsignar;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
     //ArrayList<CFacturasDescargadas> arrFactPorMftoDescargadas;

    public ReporteDescargueDeFacturas(Inicio ini, CManifiestosDeDistribucion manifiesto) {

       // rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");

        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 30);

            // creation of the different writers
            writer = PdfWriter.getInstance(document, new FileOutputStream(ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.codificarManifiesto() + ".pdf"));
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

            event = new HeaderFooter(ini,true);
            writer.setPageEvent(event);

            // various fonts
            Font myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
            Font myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
            Font myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
            Font myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
            Font myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
            Font myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
            Font myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
            Font myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));

            CodigoDeBarras codbar = new CodigoDeBarras();

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            String val = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());

            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

           // Paragraph par = new Paragraph("\n\n\n\n\n\n\n\n\n\n");
            Paragraph par1 = new Paragraph("\n\n" +"INFORME DE INGRESO Y DESCARGUE DE DISTRIBUCION", myfont5);
            par1.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tableEncabezadoInforme = new PdfPTable(2);
            tableEncabezadoInforme.setTotalWidth(500);
            tableEncabezadoInforme.setTotalWidth(new float[]{200, 250});

            PdfPCell cellPlaca = new PdfPCell(new Phrase("Placa N° " + manifiesto.getVehiculo(), myfont4));
            PdfPCell cellKilometros = new PdfPCell(new Phrase("Kilometraje " + manifiesto.getKmSalida(), myfont4));

            CEmpleados conductor = null;
            CEmpleados aux = null;

            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getCedula().equals(manifiesto.getConductor())) {
                    conductor = new CEmpleados(ini);
                    conductor = obj;
                    break;
                }

            }

            PdfPCell cellConductor = new PdfPCell(new Phrase("Conductor  " + conductor.getNombres() + " " + conductor.getApellidos(), myfont4));
            PdfPCell cellAuxiliar1 = null;
           

//            if (manifiesto.getAuxiliarDeReparto1().equals("0")) {
//                cellAuxiliar1 = new PdfPCell(new Phrase("Auxiliar  : Sin auxiliar asignado", myfont4));
//            } else {
//
//                for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                    if (obj.getCedula().equals(manifiesto.getAuxiliarDeReparto1())) {
//                        aux = new CEmpleados(ini);
//                        aux = obj;
//                    }
//                    cellAuxiliar1 = new PdfPCell(new Phrase("Auxiliar :  " + aux.getNombres() + " " + aux.getApellidos(), myfont4));
//                }
//            }
            
          
            
            

            // LLAMA AL OBJETO RUTAS DE DISTRIBUCION
            CRutasDeDistribucion rutaObj = new CRutasDeDistribucion(ini);
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getIdRutasDeDistribucion() == manifiesto.getIdRuta()) {
                    rutaObj = obj;
                }
            }

            PdfPCell cellRuta = new PdfPCell(new Phrase("Ruta  " + rutaObj.getNombreRutasDeDistribucion(), myfont4));

            // LLAMA AL OBJETO CANALES DE VENTA
            CCanalesDeVenta canal = new CCanalesDeVenta(ini);
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getIdCanalDeVenta() == manifiesto.getIdCanal()) {
                    canal = obj;
                }
            }

            PdfPCell cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  " + val, myfont4));
            PdfPCell cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal :  " + canal.getNombreCanalDeVenta(), myfont4));
            PdfPCell cell8 = new PdfPCell(new Phrase(" "));

            //Date dNow = new Date( );
            //  SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd '-' hh:mm aaa");
            PdfPCell cellFecha = new PdfPCell(new Phrase("Fecha : " + manifiesto.getHoraDeLiquidacion(), myfont4));
            PdfPCell cellUsuario = new PdfPCell(new Phrase("Usuario : " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont4));
            cellPlaca.setBorder(Rectangle.NO_BORDER);
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cellAuxiliar1.setBorder(Rectangle.NO_BORDER);
           
            cellRuta.setBorder(Rectangle.NO_BORDER);
            cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            cell8.setBorder(Rectangle.NO_BORDER);
            cellFecha.setBorder(Rectangle.NO_BORDER);
            cellUsuario.setBorder(Rectangle.NO_BORDER);

            tableEncabezadoInforme.addCell(cellPlaca);
            tableEncabezadoInforme.addCell(cellKilometros);
            tableEncabezadoInforme.addCell(cellConductor);
            tableEncabezadoInforme.addCell(cellAuxiliar1);
            
            tableEncabezadoInforme.addCell(cellRuta);
            tableEncabezadoInforme.addCell(cellNumeroManifiesto);
            tableEncabezadoInforme.addCell(cellCanalDeDistribucion);
            tableEncabezadoInforme.addCell(cell8);
            tableEncabezadoInforme.addCell(cellFecha);
            tableEncabezadoInforme.addCell(cellUsuario);
            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            
            document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(par1);
            document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            // demonstrate some table features
            PdfPTable tablaDatosDelInforme = new PdfPTable(9);
            tablaDatosDelInforme.setSpacingBefore(2);
            tablaDatosDelInforme.getDefaultCell().setPadding(5);
            tablaDatosDelInforme.setTotalWidth(600);
            tablaDatosDelInforme.setTotalWidth(new float[]{25, 60, 90, 25, 60, 90, 25, 60, 90});
            tablaDatosDelInforme.setLockedWidth(true);

            PdfPCell cellNo = new PdfPCell(new Phrase("N° "));
            cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact. N° "));
            cellFact.setPadding(5);
            cellFact.setBorder(Rectangle.NO_BORDER);
            PdfPCell cellVal = new PdfPCell(new Phrase("$$ "));
            cellVal.setPadding(5);
            cellVal.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInforme.addCell(cellNo);
            tablaDatosDelInforme.addCell(cellFact);
            tablaDatosDelInforme.addCell(cellVal);

            tablaDatosDelInforme.addCell(cellNo);
            tablaDatosDelInforme.addCell(cellFact);
            tablaDatosDelInforme.addCell(cellVal);

            tablaDatosDelInforme.addCell(cellNo);
            tablaDatosDelInforme.addCell(cellFact);
            tablaDatosDelInforme.addCell(cellVal);

            int contadorDeFacturas = 0;

            for (CFacturasPorManifiesto obj : manifiesto.getListaFacturasDescargadas()) {

                PdfPCell celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont1));
                PdfPCell celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont1));
                PdfPCell celdaValorRecaudado = null;

                celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecaudado()), myfont1));

                celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);
                celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);
                celdaValorRecaudado.setBorder(Rectangle.NO_BORDER);
                tablaDatosDelInforme.addCell(celdaNUmeroConsecutivo);
                tablaDatosDelInforme.addCell(celdaNumeroFactura);
                tablaDatosDelInforme.addCell(celdaValorRecaudado);

                contadorDeFacturas++;

                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <3 && >=1
                if (contadorDeFacturas == manifiesto.getListaFacturasDescargadas().size()) {

                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
                    int residuo = contadorDeFacturas % 3;
                    residuo = 3 - residuo;
                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
                    celda4.setBorder(Rectangle.NO_BORDER);
                    celda4.setColspan(residuo * 3);
                    tablaDatosDelInforme.addCell(celda4);
                }
                valoraConsignar += obj.getValorRecaudado();
            }

            String valor = NumberToLetterConverter.convertNumberToLetter(valoraConsignar);
            Paragraph valorTotal = new Paragraph(new Phrase("Valor total a recaudar  :( " + nf.format(valoraConsignar) + ") \n" + valor + "\n\n\n\n", myfont4));
            PdfPTable table3 = new PdfPTable(2);

            Phrase conducto = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont4));
            Phrase usuario = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + conductor.getNombres() + " " + conductor.getApellidos(), myfont4));

            PdfPCell cell11 = new PdfPCell(conducto);
            PdfPCell cell12 = new PdfPCell(usuario);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell11);
            table3.addCell(cell12);

            document.add(tablaDatosDelInforme);
            document.add(new Phrase("\n"));
            document.add(valorTotal);
            document.add(table3);

            document.close();

            File path = new File(ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.codificarManifiesto() + ".pdf");

            try {

                Desktop.getDesktop().open(path);

            } catch (IOException ex) {
                Logger.getLogger(ReporteDescargueDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error 1 catch  " + ex, "Error", 0);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteDescargueDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteDescargueDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReporteDescargueDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getStringNumeroDemanifiesto(String manifiesto) {
        String valor = null;
        valor = "" + manifiesto;

        switch (valor.length()) {
            case 1:
                valor = "00000" + manifiesto;
                break;
            case 2:
                valor = "0000" + manifiesto;
                break;
            case 3:
                valor = "000" + manifiesto;
                break;
            case 4:
                valor = "00" + manifiesto;
                break;
            case 5:
                valor = "0" + manifiesto;
                break;
            default:
                valor = "" + manifiesto;
                break;
        }

        return valor;
    }

}
