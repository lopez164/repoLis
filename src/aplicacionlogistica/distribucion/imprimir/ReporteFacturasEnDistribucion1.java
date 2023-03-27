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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class ReporteFacturasEnDistribucion1 {

    String codigoManifiesto;
    Image imgCodigoDeBarras;
    Document document;
    double valorManifiesto = 0.0;
    String rutaDeArchivo;
    PdfWriter writer;
    HeaderFooter event;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public ReporteFacturasEnDistribucion1(Inicio ini, CManifiestosDeDistribucion manifiesto) {

        CEmpleados conductor = null;
        CEmpleados aux = null;
        CCanalesDeVenta canal;
        CRutasDeDistribucion rutaObj;

        String cadena;

        // various fonts
        Font myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
        Font myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
        Font myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
        Font myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
        Font myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
        Font myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
        Font myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
        Font myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));

        CodigoDeBarras codbar;
        PdfPTable tableEncabezadoInforme;

        /* Declaración de la variables de las celdas en la parte del encabezado del informe*/
        PdfPCell cellPlaca;
        PdfPCell cellKilometros;
        PdfPCell cellConductor;
        PdfPCell cellAuxiliar;
        PdfPCell cellRuta;
        PdfPCell cellNumeroManifiesto;
        PdfPCell cellCanalDeDistribucion;
        PdfPCell cellVacia;
        PdfPCell cellFecha;
        PdfPCell cellUsuario;

        PdfPCell mCellPlaca;
        PdfPCell mCellKilometros;
        PdfPCell mCellConductor;
        PdfPCell mCellAuxiliar;
        PdfPCell mCellRuta;
        PdfPCell mCellNumeroManifiesto;
        PdfPCell mCellCanalDeDistribucion;
        PdfPCell mCellVacia;
        PdfPCell mCellFecha;
        PdfPCell mCellUsuario;

        PdfPTable tablaDatosDelInformeFacturas;
        PdfPCell celdaNUmeroConsecutivo;
        PdfPCell celdaNumeroFactura;
        PdfPCell celdaValorFactura;

        PdfPTable tablaDeFirmas;
        PdfPCell celdaRecibido;
        PdfPCell celdaEntregado;

        List<CFacturasPorManifiesto> listaFacturasPorManifiesto;

        listaFacturasPorManifiesto = new ArrayList();
        rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 30);

            listaFacturasPorManifiesto = manifiesto.getListaFacturasPorManifiesto();

            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream(rutaDeArchivo + "manifiestos/" + manifiesto.codificarManifiesto() + ".pdf"));
            } catch (DocumentException ex) {
                Logger.getLogger(ReporteFacturasEnDistribucion1.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            event = new HeaderFooter(ini, true);
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            document.open();
            PdfContentByte cb = writer.getDirectContent();
            String val = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());
            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph prTituloInforme = new Paragraph("MANIFIESTO DE REPARTO PARA SALIR A DISTRIBUCION", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 200, 80, 200});

            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getCedula().equals(manifiesto.getConductor())) {
                    conductor = new CEmpleados(ini);
                    conductor = obj;
                    break;
                }

            }

            if (conductor != null) {
                cadena = conductor.getNombres() + " " + conductor.getApellidos();
                if (cadena.length() >= 30) {
                    cadena = cadena.substring(0, 30);
                }
                cellConductor = new PdfPCell(new Phrase(cadena, myfont4));
            }

//            if (manifiesto.getAuxiliarDeReparto1().equals("0")) {
//                cellAuxiliar = new PdfPCell(new Phrase("Auxiliar  : ", myfont3));
//                cellAuxiliar.setBorder(Rectangle.NO_BORDER);
//                
//                mCellAuxiliar = new PdfPCell(new Phrase("Sin auxiliar asignado", myfont1));
//                mCellAuxiliar.setBorder(Rectangle.NO_BORDER);
//            } else {
//
//                for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                    if (obj.getCedula().equals(manifiesto.getAuxiliarDeReparto1())) {
//                        aux = new CEmpleados(ini);
//                        aux = obj;
//                        break;
//                    }
//
//                }
//                cellAuxiliar = new PdfPCell(new Phrase("Auxiliar  : ", myfont3));
//                cellAuxiliar.setBorder(Rectangle.NO_BORDER);
//                cadena= aux.getNombres() + " " + aux.getApellidos();
//                 if (cadena.length() >= 26)     cadena = cadena.substring(0,26);
//                
//                mCellAuxiliar = new PdfPCell(new Phrase(cadena, myfont1));
//                mCellAuxiliar.setBorder(Rectangle.NO_BORDER);
//            }
            // LLAMA AL OBJETO RUTAS DE DISTRIBUCION
            rutaObj = new CRutasDeDistribucion(ini);
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getIdRutasDeDistribucion() == manifiesto.getIdRuta()) {
                    rutaObj = obj;
                    break;
                }
            }

            // LLAMA AL OBJETO CANALES DE VENTA
            canal = new CCanalesDeVenta(ini);
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getIdCanalDeVenta() == manifiesto.getIdCanal()) {
                    canal = obj;
                    break;
                }
            }

            /*Se signan los valores a las celdas del encabezado de informe en el documento */
            cellPlaca = new PdfPCell(new Phrase("Placa N° :", myfont3));
            cellPlaca.setBorder(Rectangle.NO_BORDER);

            mCellPlaca = new PdfPCell(new Phrase(manifiesto.getVehiculo(), myfont1));
            mCellPlaca.setBorder(Rectangle.NO_BORDER);

            cellKilometros = new PdfPCell(new Phrase("Kilometraje :", myfont3));
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            mCellKilometros = new PdfPCell(new Phrase("" + manifiesto.getKmSalida(), myfont1));
            mCellKilometros.setBorder(Rectangle.NO_BORDER);

            cellConductor = new PdfPCell(new Phrase("Conductor  :", myfont3));
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cadena = conductor.getNombres() + " " + conductor.getApellidos();
            if (cadena.length() >= 26) {
                cadena = cadena.substring(0, 26);
            }
            mCellConductor = new PdfPCell(new Phrase(cadena, myfont1));
            mCellConductor.setBorder(Rectangle.NO_BORDER);

            cellRuta = new PdfPCell(new Phrase("Ruta  :", myfont3));
            cellRuta.setBorder(Rectangle.NO_BORDER);
            mCellRuta = new PdfPCell(new Phrase(rutaObj.getNombreRutasDeDistribucion(), myfont1));
            mCellRuta.setBorder(Rectangle.NO_BORDER);

            cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  :", myfont3));
            cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
            mCellNumeroManifiesto = new PdfPCell(new Phrase("" + manifiesto.getNumeroManifiesto(), myfont1));
            mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);

            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal :", myfont3));
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            mCellCanalDeDistribucion = new PdfPCell(new Phrase(canal.getNombreCanalDeVenta(), myfont1));
            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);

            cellVacia = new PdfPCell(new Phrase("Val manfto:", myfont3));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(nf.format(manifiesto.getValorTotalManifiesto()), myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha : ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);

            mCellFecha = new PdfPCell(new Phrase("" + manifiesto.getHoraDeLiquidacion(), myfont1));
            mCellFecha.setBorder(Rectangle.NO_BORDER);

            cellUsuario = new PdfPCell(new Phrase("Usuario : ", myfont3));
            cellUsuario.setBorder(Rectangle.NO_BORDER);
            cadena = ini.getUser().getNombres() + " " + ini.getUser().getApellidos();
            if (cadena.length() >= 26) {
                cadena = cadena.substring(0, 26);
            }
            mCellUsuario = new PdfPCell(new Phrase(cadena, myfont1));
            mCellUsuario.setBorder(Rectangle.NO_BORDER);


            /* se asignan las celdas a la tabla del encabezado en el  documento*/
            tableEncabezadoInforme.addCell(cellPlaca);
            tableEncabezadoInforme.addCell(mCellPlaca);

            tableEncabezadoInforme.addCell(cellKilometros);
            tableEncabezadoInforme.addCell(mCellKilometros);

            tableEncabezadoInforme.addCell(cellConductor);
            tableEncabezadoInforme.addCell(mCellConductor);

//            tableEncabezadoInforme.addCell(cellAuxiliar);
//            tableEncabezadoInforme.addCell(mCellAuxiliar);
//            
            tableEncabezadoInforme.addCell(cellRuta);
            tableEncabezadoInforme.addCell(mCellRuta);

            tableEncabezadoInforme.addCell(cellNumeroManifiesto);
            tableEncabezadoInforme.addCell(mCellNumeroManifiesto);

            tableEncabezadoInforme.addCell(cellCanalDeDistribucion);
            tableEncabezadoInforme.addCell(mCellCanalDeDistribucion);

            tableEncabezadoInforme.addCell(cellVacia);
            tableEncabezadoInforme.addCell(mCellVacia);

            tableEncabezadoInforme.addCell(cellFecha);
            tableEncabezadoInforme.addCell(mCellFecha);

            tableEncabezadoInforme.addCell(cellUsuario);
            tableEncabezadoInforme.addCell(mCellUsuario);

            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);

            //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            /* Se instancia la tabla del informe de las facturas */
            tablaDatosDelInformeFacturas = new PdfPTable(9);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{25, 60, 90, 25, 60, 90, 25, 60, 90});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            /* Columna 1 N°. */
            PdfPCell cellNo = new PdfPCell(new Phrase("N° ", myfont3));
            cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);

            /* Columna 2 Fct. N°. */
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact. N° ", myfont3));
            cellFact.setPadding(5);
            cellFact.setBorder(Rectangle.NO_BORDER);

            /* Columna 3 valor factura */
            PdfPCell cellVal = new PdfPCell(new Phrase("$$ ", myfont3));
            cellVal.setPadding(5);
            cellVal.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellVal);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellVal);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellVal);

            int contadorDeFacturas = 0;


            /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
            for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {

                // ADHERENCIA
                celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont1));
                celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                // NUMERO DE LA FACTURA
                celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont1));
                celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                // VALOR A RECAUDAR DE LA FACTURA              
                celdaValorFactura = new PdfPCell(new Phrase("" + nf.format(obj.getValorARecaudarFactura()), myfont1));
                celdaValorFactura.setBorder(Rectangle.NO_BORDER);

                tablaDatosDelInformeFacturas.addCell(celdaNUmeroConsecutivo);
                tablaDatosDelInformeFacturas.addCell(celdaNumeroFactura);
                tablaDatosDelInformeFacturas.addCell(celdaValorFactura);

                contadorDeFacturas++;

                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <3 && >=1
                if (contadorDeFacturas == listaFacturasPorManifiesto.size()) {

                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
                    int residuo = contadorDeFacturas % 3;
                    residuo = 3 - residuo;
                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
                    celda4.setBorder(Rectangle.NO_BORDER);;
                    celda4.setColspan(residuo * 3);
                    tablaDatosDelInformeFacturas.addCell(celda4);
                }
                valorManifiesto += obj.getValorARecaudarFactura();
            }

            String valor;
            valor = NumberToLetterConverter.convertNumberToLetter(valorManifiesto);

            Paragraph prValorFacturas = new Paragraph(new Phrase("Valor total a recaudar  :( " + nf.format(valorManifiesto) + ") \n" + valor + "\n\n\n\n", myfont4));

            tablaDeFirmas = new PdfPTable(2);
            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + conductor.getNombres() + " " + conductor.getApellidos(), myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont3));

            celdaRecibido = new PdfPCell(lineaRecibe);
            celdaRecibido.setBorder(Rectangle.NO_BORDER);

            celdaEntregado = new PdfPCell(lineaEntrega);
            celdaEntregado.setBorder(Rectangle.NO_BORDER);

            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaEntregado);

            /**
             * *********************************************Se arma el documento*************************************************
             */
            /*Se inserta el encababezdo y título del informe  */
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(tableEncabezadoInforme);
            //document.add(new Paragraph("\n"));
            document.add(prTituloInforme);
            // document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            /*Se incorpora la tabla de las facturas al documento*/
            document.add(tablaDatosDelInformeFacturas);
            document.add(new Phrase("\n"));
            document.add(prValorFacturas);

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);
            document.close();

            File path = new File(rutaDeArchivo + "manifiestos/" + manifiesto.codificarManifiesto() + ".pdf");

            Desktop.getDesktop().open(path);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion1.class.getName()).log(Level.SEVERE, null, ex);

        } catch (DocumentException ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion1.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion1.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Método que devuelve una cadena con un código que representa el número de
     * mnifiesto y agrega tantos ceros como sea necesario hasta completar una
     * longitud de seis (6) caracteres
     *
     * @return una cadena con 4l código del manifiesto
     */
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
