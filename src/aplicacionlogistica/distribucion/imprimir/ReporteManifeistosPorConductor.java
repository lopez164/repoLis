/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosMovilizadosPorConductor;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class ReporteManifeistosPorConductor {

    String codigoManifiesto;
    //Image imgCodigoDeBarras;
    Document document;
    double valorManifiestos = 0.0;
    double valorRecaudado = 0.0;
            
    FReporteManifiestosMovilizadosPorConductor form ;
    String rutaDeArchivo;
    PdfWriter writer;
    HeaderFooter event;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini;

    // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public ReporteManifeistosPorConductor(FReporteManifiestosMovilizadosPorConductor form ) {

        this.form = form;
        this.ini = form.ini;
        Date fecha1 = Inicio.getFechaSql(form.jFechaInicial);
        Vst_empleados conductor = null;
        Vst_empleados aux = null;
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
        PdfPCell celdaNUmeroManifiesto;
        PdfPCell celdaVehiculo;
        PdfPCell celdaconductor;
        PdfPCell celdaRuta;
        PdfPCell celdaEstdado;
        PdfPCell celdaValoMfto;
        PdfPCell celdaValoRecaudo;
        

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

//            listaFacturasPorManifiesto = manifiesto.getListaFacturasPorManifiesto();

            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream(rutaDeArchivo + "reportes/" + 
                        fecha1 + "_" + form.conductor.getCedula() + ".pdf"));
            } catch (DocumentException ex) {
                Logger.getLogger(ReporteManifeistosPorConductor.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            event = new HeaderFooter(this.ini, false);
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            document.open();
            PdfContentByte cb = writer.getDirectContent();
           

            Paragraph prTituloInforme = new Paragraph("REPORTE DE MANIFIESTOS POR REPARTIDOR  \n\n", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 200, 80, 200});
           

            if (this.form.conductor != null) {
                cadena = this.form.conductor.getNombres() + " " + this.form.conductor.getApellidos();
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
            
//         
//            /*Se signan los valores a las celdas del encabezado de informe en el documento */
//            cellPlaca = new PdfPCell(new Phrase("Placa N° :", myfont3));
//            cellPlaca.setBorder(Rectangle.NO_BORDER);
//
//            mCellPlaca = new PdfPCell(new Phrase(manifiesto.getVehiculo(), myfont1));
//            mCellPlaca.setBorder(Rectangle.NO_BORDER);
//
//            cellKilometros = new PdfPCell(new Phrase("Kilometraje :", myfont3));
//            cellKilometros.setBorder(Rectangle.NO_BORDER);
//            mCellKilometros = new PdfPCell(new Phrase("" + manifiesto.getKmSalida(), myfont1));
//            mCellKilometros.setBorder(Rectangle.NO_BORDER);

            cellConductor = new PdfPCell(new Phrase("Conductor  :", myfont3));
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cadena = this.form.conductor.getNombres() + " " + this.form.conductor.getApellidos();
            if (cadena.length() >= 26) {
                cadena = cadena.substring(0, 26);
            }
            mCellConductor = new PdfPCell(new Phrase(cadena, myfont1));
            mCellConductor.setBorder(Rectangle.NO_BORDER);

//            cellRuta = new PdfPCell(new Phrase("Ruta  :", myfont3));
//            cellRuta.setBorder(Rectangle.NO_BORDER);
//            mCellRuta = new PdfPCell(new Phrase(rutaObj.getNombreRutasDeDistribucion(), myfont1));
//            mCellRuta.setBorder(Rectangle.NO_BORDER);
//
//            cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  :", myfont3));
//            cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
//            mCellNumeroManifiesto = new PdfPCell(new Phrase("" + manifiesto.getNumeroManifiesto(), myfont1));
//            mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);

//            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal :", myfont3));
//            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
//            mCellCanalDeDistribucion = new PdfPCell(new Phrase(canal.getNombreCanal(), myfont1));
//            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
//
//            cellVacia = new PdfPCell(new Phrase("Val manfto:", myfont3));
//            cellVacia.setBorder(Rectangle.NO_BORDER);
//            mCellVacia = new PdfPCell(new Phrase(nf.format(manifiesto.getValorTotalManifiesto()), myfont1));
//            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha : ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);

           mCellFecha = new PdfPCell(new Phrase("" + this.form.fechaIncial, myfont1));
           mCellFecha.setBorder(Rectangle.NO_BORDER);

            cellUsuario = new PdfPCell(new Phrase("Usuario : ", myfont3));
            cellUsuario.setBorder(Rectangle.NO_BORDER);
            cadena = this.form.ini.getUser().getNombres() + " " + this.form.ini.getUser().getApellidos();
            if (cadena.length() >= 26) {
                cadena = cadena.substring(0, 26);
            }
            mCellUsuario = new PdfPCell(new Phrase(cadena, myfont1));
            mCellUsuario.setBorder(Rectangle.NO_BORDER);


            /* se asignan las celdas a la tabla del encabezado en el  documento*/
//            tableEncabezadoInforme.addCell(cellPlaca);
//            tableEncabezadoInforme.addCell(mCellPlaca);

//            tableEncabezadoInforme.addCell(cellKilometros);
//            tableEncabezadoInforme.addCell(mCellKilometros);

            tableEncabezadoInforme.addCell(cellConductor);
            tableEncabezadoInforme.addCell(mCellConductor);

//            tableEncabezadoInforme.addCell(cellAuxiliar);
//            tableEncabezadoInforme.addCell(mCellAuxiliar);
//            
//            tableEncabezadoInforme.addCell(cellRuta);
//            tableEncabezadoInforme.addCell(mCellRuta);

//            tableEncabezadoInforme.addCell(cellNumeroManifiesto);
//            tableEncabezadoInforme.addCell(mCellNumeroManifiesto);

//            tableEncabezadoInforme.addCell(cellCanalDeDistribucion);
//            tableEncabezadoInforme.addCell(mCellCanalDeDistribucion);

//            tableEncabezadoInforme.addCell(cellVacia);
//            tableEncabezadoInforme.addCell(mCellVacia);

            tableEncabezadoInforme.addCell(cellFecha);
            tableEncabezadoInforme.addCell(mCellFecha);

            tableEncabezadoInforme.addCell(cellUsuario);
            tableEncabezadoInforme.addCell(mCellUsuario);

            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);

            //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            /* Se instancia la tabla del informe de las facturas */
            tablaDatosDelInformeFacturas = new PdfPTable(6);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{50, 50, 150, 90, 80, 80});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            /* Columna 1 Manifiesto. */
            PdfPCell cellManifiesto = new PdfPCell(new Phrase("N° Mfto.", myfont3));
            cellManifiesto.setPadding(1);
            //  cellNo.setBorder(Rectangle.NO_BORDER);

            /* Columna 2 Vehiculo */
            PdfPCell cellVehiculo = new PdfPCell(new Phrase("Vehiculo ", myfont3));
            cellVehiculo.setPadding(1);
           // cellVehiculo.setBorder(Rectangle.NO_BORDER);

            /* Columna 3 Conductr manifiesto */
           // PdfPCell cellConductr = new PdfPCell(new Phrase("Conductor", myfont3));
            // cellConductor.setPadding(5);
            //cellVal.setBorder(Rectangle.NO_BORDER);
            
             /* Columna 4 ruta manifiesto */
            PdfPCell cellRut = new PdfPCell(new Phrase("Ruta", myfont3));
            cellRut.setPadding(1);
            //cellVal.setBorder(Rectangle.NO_BORDER);
            
              /* Columna 5 Estado Manifiesto */
            PdfPCell cellEstadoMan = new PdfPCell(new Phrase("Estado", myfont3));
            cellEstadoMan.setPadding(1);
            //cellVal.setBorder(Rectangle.NO_BORDER);
            
              /* Columna 6 valor manifiesto */
            PdfPCell cellValorMan = new PdfPCell(new Phrase("Valor", myfont3));
            cellValorMan.setPadding(1);
            //cellVal.setBorder(Rectangle.NO_BORDER);
            
               /* Columna 7 valor Recaudo */
            PdfPCell cellValorRecaudad = new PdfPCell(new Phrase("Recaudo", myfont3));
            cellValorRecaudad.setPadding(1);
            //cellVal.setBorder(Rectangle.NO_BORDER);
           
            tablaDatosDelInformeFacturas.addCell(cellManifiesto);
            tablaDatosDelInformeFacturas.addCell(cellVehiculo);
           // tablaDatosDelInformeFacturas.addCell(cellConductr);
            tablaDatosDelInformeFacturas.addCell(cellRut);
            tablaDatosDelInformeFacturas.addCell(cellEstadoMan);
            tablaDatosDelInformeFacturas.addCell(cellValorMan);
            tablaDatosDelInformeFacturas.addCell(cellValorRecaudad);


            /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
            for (String  obj : form.listaDeRegistros) {
                String texto = obj;
                String txtArray[] = texto.split(",");

                // ADHERENCIA
                celdaNUmeroManifiesto = new PdfPCell(new Phrase(txtArray[0], myfont1));
               

                // NUMERO DE LA FACTURA
                celdaVehiculo = new PdfPCell(new Phrase(txtArray[1], myfont1));
               

                // VALOR A RECAUDAR DE LA FACTURA              
                //celdaconductor = new PdfPCell(new Phrase(txtArray[2], myfont1));
                
                 // VALOR A RECAUDAR DE LA FACTURA              
                celdaRuta = new PdfPCell(new Phrase(txtArray[3], myfont1));
               
                 // VALOR A RECAUDAR DE LA FACTURA              
                celdaEstdado = new PdfPCell(new Phrase(txtArray[4], myfont1));
               
                 // VALOR A RECAUDAR DE LA FACTURA              
                celdaValoMfto = new PdfPCell(new Phrase("" + nf.format(Double.parseDouble(txtArray[5])), myfont1));
                 valorManifiestos += Double.parseDouble(txtArray[5]);
               
                 // VALOR A RECAUDAR DE LA FACTURA              
                celdaValoRecaudo = new PdfPCell(new Phrase("" + nf.format(Double.parseDouble(txtArray[6])), myfont1));
                valorRecaudado += Double.parseDouble(txtArray[6]);

                tablaDatosDelInformeFacturas.addCell(celdaNUmeroManifiesto);    // 
                tablaDatosDelInformeFacturas.addCell(celdaVehiculo);
                //tablaDatosDelInformeFacturas.addCell(celdaconductor);
                tablaDatosDelInformeFacturas.addCell(celdaRuta);
                tablaDatosDelInformeFacturas.addCell(celdaEstdado);
                tablaDatosDelInformeFacturas.addCell(celdaValoMfto);
                tablaDatosDelInformeFacturas.addCell(celdaValoRecaudo);
                

               // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <3 && >=1
//                if (contadorDeFacturas == listaFacturasPorManifiesto.size()) {
//
//                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
//                    int residuo = contadorDeFacturas % 3;
//                    residuo = 3 - residuo;
//                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
//                    celda4.setBorder(Rectangle.NO_BORDER);;
//                    celda4.setColspan(residuo * 3);
//                    tablaDatosDelInformeFacturas.addCell(celda4);
//                }
               
            }

            String valorManifiesto;
            valorManifiesto = NumberToLetterConverter.convertNumberToLetter(valorManifiestos);
            Paragraph prValorManifiesto = new Paragraph(new Phrase("Valor total Manifiestos  :( " + nf.format(valorManifiestos) + ") \n" + valorManifiesto + "\n\n", myfont1));

            
            String valoRecaudo;
            valoRecaudo = NumberToLetterConverter.convertNumberToLetter(valorRecaudado);
            Paragraph prValorRecaudado = new Paragraph(new Phrase("Valor total Recaudado  :( " + nf.format(valorRecaudado) + ") \n" + valoRecaudo + "\n\n", myfont1));

            tablaDeFirmas = new PdfPTable(2);
            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + this.form.conductor.getNombres() + " " + this.form.conductor.getApellidos(), myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos(), myfont3));

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
//            document.add(imgCodigoDeBarras);

            /*Se incorpora la tabla de las facturas al documento*/
            document.add(tablaDatosDelInformeFacturas);
           
            document.add(new Phrase("\n"));
            document.add(prValorManifiesto);
            
            document.add(new Phrase("\n"));
            document.add(prValorRecaudado);

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);
            document.close();

            File path = new File(rutaDeArchivo + "reportes/" + fecha1 + "_" + form.conductor.getCedula() + ".pdf");

            Desktop.getDesktop().open(path);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteManifeistosPorConductor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (DocumentException ex) {
            Logger.getLogger(ReporteManifeistosPorConductor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            Logger.getLogger(ReporteManifeistosPorConductor.class.getName()).log(Level.SEVERE, null, ex);

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
