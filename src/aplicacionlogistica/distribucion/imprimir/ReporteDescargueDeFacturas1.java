/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
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
 * Este objeto
 *
 * @author lopez164
 */
public class ReporteDescargueDeFacturas1 {

    String mfto;
   
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    double valoraConsignar;
    double valorRecaudadoFacturas;
    double valorDescuentoRecogidas;
    boolean conMarcaDeAgua=false;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    //ArrayList<CFacturasDescargadas> arrFactPorMftoDescargadas;

    public ReporteDescargueDeFacturas1(Inicio ini, CManifiestosDeDistribucion manifiesto) {

        /* Este reporte no genera marca de agua como mnifiesto descargado*/
        this.conMarcaDeAgua=false;
        String cadena;
        CUsuarios usuarioQueDescargo = null;
        //rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        CEmpleados conductor = null; //= null;
        CEmpleados aux = null; //= null;

        PdfPTable tableEncabezadoInforme;
        PdfPTable tablaDatosDelInformeFacturas;
        PdfPTable tablaDatosDelInformeRecogidas;
        PdfPTable tablaConsignaciones;
        PdfPTable tablaDeFirmas;

        /* Declaración de la variables de las celdas en la parte del encabezado del informe*/
        PdfPCell cellPlaca;
        PdfPCell cellKilometros;
        PdfPCell cellConductor;
        PdfPCell cellAuxiliar1 = null;
        PdfPCell cellAuxiliar2 = null;
        PdfPCell cellAuxiliar3 = null;

        PdfPCell cellRuta;

        PdfPCell cellCanalDeDistribucion;
        PdfPCell cellVacia;
        PdfPCell cellFecha;

        PdfPCell mCellPlaca;
        PdfPCell mCellKilometros;
        PdfPCell mCellConductor;
        PdfPCell mCellAuxiliar1 = null;
        PdfPCell mCellAuxiliar2 = null;
        PdfPCell mCellAuxiliar3 = null;

        PdfPCell mCellRuta;
        PdfPCell mCellCanalDeDistribucion;
        PdfPCell mCellVacia;
        PdfPCell mCellFecha;


        /* Declaración de la variables de las celdas en la parte del informe*/
        PdfPCell celdaNUmeroConsecutivo;
        PdfPCell celdaNumeroFactura;
        PdfPCell celdaMovimiento;
        PdfPCell celdaValorRecaudado;

        PdfPCell celdaSoporteConsignacion;
        PdfPCell celdaBanco;
        PdfPCell celdaNumeroDeCuenta;
        PdfPCell celdaValorConsignacion;

        /* Declaración de la variables de las celdas en la parte de las firmas */
        PdfPCell celdaRecibido;
        PdfPCell celdaEntregado;

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

        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 40);

            // creation of the different writers
            writer = PdfWriter.getInstance(document, new FileOutputStream(ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.codificarManifiesto() + ".pdf"));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            event = new HeaderFooter(ini, true, manifiesto,writer,conMarcaDeAgua);
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            /* Se le da formato al número del manifiestos para crear el código de barras*/
            String numManifiesto = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());

            /*Se crea el código de barras*/
            imgCodigoDeBarras = codbar.getBarCodeImage("" + numManifiesto, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph prTituloInforme = new Paragraph("INFORME DESCARGUE DE FACTURAS DE DISTRIBUCION", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 250, 83, 250});

//            /* Se inician la variables del encabezado del documento*/
// /* Se crea el objeto conductor*/
//            for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                if (obj.getCedula().equals(manifiesto.getConductor())) {
//                    conductor = new CEmpleados(ini);
//                    conductor = obj;
//                    break;
//                }
//
//            }

            /*Se crea el objeto de la lista de Auxiliares */
            cellAuxiliar1 = new PdfPCell(new Phrase("Auxiliar1 ", myfont3));
            cellAuxiliar2 = new PdfPCell(new Phrase("Auxiliar2 ", myfont3));
            cellAuxiliar3 = new PdfPCell(new Phrase("Auxiliar3 ", myfont3));
            cellAuxiliar1.setBorder(Rectangle.NO_BORDER);
            cellAuxiliar2.setBorder(Rectangle.NO_BORDER);
            cellAuxiliar3.setBorder(Rectangle.NO_BORDER);

            mCellAuxiliar1 = new PdfPCell(new Phrase(": Sin auxiliar1 asignado", myfont1));
            mCellAuxiliar2 = new PdfPCell(new Phrase(": Sin auxiliar2 asignado", myfont1));
            mCellAuxiliar3 = new PdfPCell(new Phrase(": Sin auxiliar3 asignado", myfont1));
            mCellAuxiliar1.setBorder(Rectangle.NO_BORDER);
            mCellAuxiliar2.setBorder(Rectangle.NO_BORDER);
            mCellAuxiliar3.setBorder(Rectangle.NO_BORDER);

            manifiesto.setListaDeAuxiliares();
            
            int i = 1;

            for (CEmpleados auxiliar : manifiesto.getListaDeAuxiliares()) {

                switch (i) {
                    case 1:
                        if (auxiliar.getCedula().equals("0")) {
                            mCellAuxiliar1 = new PdfPCell(new Phrase(": Sin auxiliar1 asignado", myfont1));
                            mCellAuxiliar1.setBorder(Rectangle.NO_BORDER);
                        } else {
                            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                                if (auxiliar.getCedula().equals(obj.getCedula())) {
                                    cadena = auxiliar.getNombres() + " " + auxiliar.getApellidos();
                                    if (cadena.length() >= 26) {
                                        cadena = cadena.substring(0, 25);
                                    }
                                    mCellAuxiliar1 = new PdfPCell(new Phrase(": " + cadena, myfont1));
                                    mCellAuxiliar1.setBorder(Rectangle.NO_BORDER);
                                }
                            }
                        }
                        break;
                    case 2:
                        if (auxiliar.getCedula().equals("0")) {
                            mCellAuxiliar2 = new PdfPCell(new Phrase(": Sin auxiliar2 asignado", myfont1));
                            mCellAuxiliar2.setBorder(Rectangle.NO_BORDER);
                        } else {
                            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                                if (auxiliar.getCedula().equals(obj.getCedula())) {
                                    cadena = auxiliar.getNombres() + " " + auxiliar.getApellidos();
                                    if (cadena.length() >= 26) {
                                        cadena = cadena.substring(0, 25);
                                    }
                                    mCellAuxiliar2 = new PdfPCell(new Phrase(": " + cadena, myfont1));
                                    mCellAuxiliar2.setBorder(Rectangle.NO_BORDER);

                                }
                            }
                        }

                        break;

                    case 3:
                        if (auxiliar.getCedula().equals("0")) {
                            mCellAuxiliar3 = new PdfPCell(new Phrase(": Sin auxiliar3 asignado", myfont1));
                            mCellAuxiliar3.setBorder(Rectangle.NO_BORDER);;
                        } else {
                            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                                if (auxiliar.getCedula().equals(obj.getCedula())) {
                                    cadena = auxiliar.getNombres() + " " + auxiliar.getApellidos();
                                    if (cadena.length() >= 26) {
                                        cadena = cadena.substring(0, 25);
                                    }
                                    mCellAuxiliar3 = new PdfPCell(new Phrase(": " + cadena, myfont1));
                                    mCellAuxiliar3.setBorder(Rectangle.NO_BORDER);

                                }
                            }
                        }
                        break;
                }

                i++;

            }

           

            /*Se signan los valores a las celdas del encabezado de informe en el documento */
            cellPlaca = new PdfPCell(new Phrase("Placa N° ", myfont3));
            cellPlaca.setBorder(Rectangle.NO_BORDER);
            mCellPlaca = new PdfPCell(new Phrase(": " + manifiesto.getVehiculo(), myfont1));
            mCellPlaca.setBorder(Rectangle.NO_BORDER);

            cellKilometros = new PdfPCell(new Phrase("Kilometraje ", myfont3));
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            mCellKilometros = new PdfPCell(new Phrase(": " + manifiesto.getKmEntrada(), myfont1));
            mCellKilometros.setBorder(Rectangle.NO_BORDER);

            cellConductor = new PdfPCell(new Phrase("Conductor  ", myfont3));
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cadena = manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor();
            if (cadena.length() >= 22) {
                cadena = cadena.substring(0, 22);
            }
            mCellConductor = new PdfPCell(new Phrase(": " + cadena, myfont1));
            mCellConductor.setBorder(Rectangle.NO_BORDER);

            cellRuta = new PdfPCell(new Phrase("Ruta  ", myfont3));
            cellRuta.setBorder(Rectangle.NO_BORDER);
            mCellRuta = new PdfPCell(new Phrase(": " + manifiesto.getNombreDeRuta(), myfont1));
            mCellRuta.setBorder(Rectangle.NO_BORDER);

            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + manifiesto.getNombreCanal(), myfont1));
            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);

            cellVacia = new PdfPCell(new Phrase("Val manfto", myfont3));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(": " + nf.format(manifiesto.getValorTotalManifiesto()), myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            mCellFecha = new PdfPCell(new Phrase(": " + manifiesto.getHoraDeLiquidacion(), myfont1));
            mCellFecha.setBorder(Rectangle.NO_BORDER);

            /* se asignan las celdas a la tabla del encabezado en el  documento*/
            tableEncabezadoInforme.addCell(cellPlaca);
            tableEncabezadoInforme.addCell(mCellPlaca);

            tableEncabezadoInforme.addCell(cellKilometros);
            tableEncabezadoInforme.addCell(mCellKilometros);

            tableEncabezadoInforme.addCell(cellConductor);
            tableEncabezadoInforme.addCell(mCellConductor);

            tableEncabezadoInforme.addCell(cellRuta);
            tableEncabezadoInforme.addCell(mCellRuta);

            tableEncabezadoInforme.addCell(cellAuxiliar1);
            tableEncabezadoInforme.addCell(mCellAuxiliar1);

            tableEncabezadoInforme.addCell(cellCanalDeDistribucion);
            tableEncabezadoInforme.addCell(mCellCanalDeDistribucion);

            tableEncabezadoInforme.addCell(cellAuxiliar2);
            tableEncabezadoInforme.addCell(mCellAuxiliar2);

            tableEncabezadoInforme.addCell(cellFecha);
            tableEncabezadoInforme.addCell(mCellFecha);

            tableEncabezadoInforme.addCell(cellAuxiliar3);
            tableEncabezadoInforme.addCell(mCellAuxiliar3);

            tableEncabezadoInforme.addCell(cellVacia);
            tableEncabezadoInforme.addCell(mCellVacia);

            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);
            //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());

            /* Se instancia la tabla del informe de las facturas */
            tablaDatosDelInformeFacturas = new PdfPTable(12);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{23, 40, 35, 75, 23, 40, 35, 75, 23, 40, 35, 65});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            tablaDatosDelInformeFacturas.setHorizontalAlignment(Element.ALIGN_LEFT);

            /*Columna 1 N°*/
            PdfPCell cellNo = new PdfPCell(new Phrase("N°", myfont3));
            // cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 Fact. N° */
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact #", myfont3));
            //cellFact.setPadding(5);
            cellFact.setBorder(Rectangle.NO_BORDER);

            /* Columna 3 Mov. */
            PdfPCell cellMov = new PdfPCell(new Phrase("Mov", myfont3));
            //cellMov.setPadding(5);
            cellMov.setBorder(Rectangle.NO_BORDER);

            /* Columna 4 $$ */
            PdfPCell cellVal = new PdfPCell(new Phrase("$$", myfont3));
            //cellVal.setPadding(5);
            cellVal.setBorder(Rectangle.NO_BORDER);

            /* Se anexan las celdas al documento */
            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellMov);
            tablaDatosDelInformeFacturas.addCell(cellVal);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellMov);
            tablaDatosDelInformeFacturas.addCell(cellVal);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellMov);
            tablaDatosDelInformeFacturas.addCell(cellVal);

          
            
            /**
             * **************************se inicia el proceso en la parte del  informe de las facturas********************************
             */
           manifiesto.setListaFacturasDescargadas();
            
            int contadorDeFacturas = 0;
            if(manifiesto.getListaFacturasDescargadas() != null){

            for (CFacturasPorManifiesto obj : manifiesto.getListaFacturasDescargadas()) {
                if(contadorDeFacturas==0){
                    for(CUsuarios usu : ini.getListaDeUsuarios()){
                        if(usu.getNombreUsuario().equals(Inicio.cifrar(obj.getUsuario()))){
                            usuarioQueDescargo=usu;
                            break;
                        }
                    }
                    
                }

                /*Asignción de valores  las celdas */
                celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
                celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);
                
                 switch (obj.getIdTipoDeMovimiento()) {
                                case 2:
                                    if (obj.getValorRecaudado() == 0.0) {
                                        obj.setTipoDeDEscargue("E. T. Cr");

                                    } else {
                                        if (obj.getValorDescuento() == 0.0) {
                                            obj.setTipoDeDEscargue("E. T. Cn");
                                        } else {

                                            obj.setTipoDeDEscargue("E. T. %");
                                        }

                                    }
                                    break;
                                case 3:
                                    obj.setTipoDeDEscargue("D. T.");
                                    break;
                                case 4:
                                    if (obj.getValorRecaudado() == 0.0) {
                                        obj.setTipoDeDEscargue("E. N. Cr");
                                    } else {
                                        obj.setTipoDeDEscargue("E. N. Cn");
                                    }
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    obj.setTipoDeDEscargue("R. E.");
                                    break;
                                    
                                case 7:
                                    obj.setTipoDeDEscargue("N. V.");
                                    break;

                            }
                
                celdaMovimiento = new PdfPCell(new Phrase(obj.getTipoDeDEscargue(), myfont));
                celdaMovimiento.setBorder(Rectangle.NO_BORDER);

                celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecaudado()), myfont));
                celdaValorRecaudado.setBorder(Rectangle.NO_BORDER);

                /*Incoporación de las celdas a la tabla */
                tablaDatosDelInformeFacturas.addCell(celdaNUmeroConsecutivo);
                tablaDatosDelInformeFacturas.addCell(celdaNumeroFactura);
                tablaDatosDelInformeFacturas.addCell(celdaMovimiento);
                tablaDatosDelInformeFacturas.addCell(celdaValorRecaudado);

                contadorDeFacturas++;

                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <4 && >=1
                if (contadorDeFacturas == manifiesto.getListaFacturasDescargadas().size()) {

//                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
//                    int residuo = contadorDeFacturas % 4;
//                    residuo = 4 - residuo;
//                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
//                    celda4.setBorder(Rectangle.NO_BORDER);
//                    celda4.setColspan(residuo * 4);
//                    tablaDatosDelInformeFacturas.addCell(celda4);
                }

                valorRecaudadoFacturas += obj.getValorRecaudado();
                //valoraConsignar += obj.getValorRecaudado();q
            }
            }

            // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
            int residuo = manifiesto.getListaFacturasDescargadas().size() % 3;
            residuo = 3 - residuo;
            PdfPCell celda4 = new PdfPCell(new Phrase(""));
            celda4.setBorder(Rectangle.NO_BORDER);
            celda4.setColspan(residuo * 4);
            tablaDatosDelInformeFacturas.addCell(celda4);

            //String valor = NumberToLetterConverter.convertNumberToLetter(valoraConsignar);
            Phrase prValorFacturas = new Phrase(new Phrase("Valor total Recaudado en facturas  :( " + nf.format(valorRecaudadoFacturas) + ") \n " , myfont3));
            Phrase valorDescargueFacturas = new Phrase(new Phrase("" +  NumberToLetterConverter.convertNumberToLetter(valorRecaudadoFacturas),myfont3));

            /**
             * ******************************************* Tabla de las recogidas ***************************************************************
             */
            /* Se instancia la tabla del informe de las recogidas */
            tablaDatosDelInformeRecogidas = new PdfPTable(12);
            tablaDatosDelInformeRecogidas.setSpacingBefore(2);
            tablaDatosDelInformeRecogidas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeRecogidas.setTotalWidth(600);
            tablaDatosDelInformeRecogidas.setTotalWidth(new float[]{23, 57, 30, 70, 23, 57, 30, 70, 23, 57, 30, 70});
            tablaDatosDelInformeRecogidas.setLockedWidth(true);

            contadorDeFacturas = 0;
              if(manifiesto.getListaDeRecogidasPorManifiesto() != null){

            for (CRecogidasPorManifiesto obj : manifiesto.getListaDeRecogidasPorManifiesto()) {

                /*Asignción de valores  las celdas */
                celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
                celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                celdaMovimiento = new PdfPCell(new Phrase("FR", myfont));
                celdaMovimiento.setBorder(Rectangle.NO_BORDER);

                celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecaudadoRecogida()), myfont));
                celdaValorRecaudado.setBorder(Rectangle.NO_BORDER);
                /* se le da formato a las celdas del cuerpo del informe */

 /*Incoporación de las celdas al documento*/
                tablaDatosDelInformeRecogidas.addCell(celdaNUmeroConsecutivo);
                tablaDatosDelInformeRecogidas.addCell(celdaNumeroFactura);
                tablaDatosDelInformeRecogidas.addCell(celdaMovimiento);
                tablaDatosDelInformeRecogidas.addCell(celdaValorRecaudado);

                contadorDeFacturas++;

                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <4 && >=1
                if (contadorDeFacturas == manifiesto.getListaDeRecogidasPorManifiesto().size()) {

//                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
//                     int residuo = contadorDeFacturas % 4;
//                    residuo = 4 - residuo;
//                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
//                    celda4.setBorder(Rectangle.NO_BORDER);
//                    celda4.setColspan(residuo * 4);
//                    tablaDatosDelInformeRecogidas.addCell(celda4);
                }

                valorDescuentoRecogidas += obj.getValorRecaudadoRecogida();

            }
            }

            residuo = manifiesto.getListaFacturasDescargadas().size() % 3;
            residuo = 3 - residuo;
            celda4 = new PdfPCell(new Phrase(""));
            celda4.setBorder(Rectangle.NO_BORDER);
            celda4.setColspan(residuo * 4);
            tablaDatosDelInformeFacturas.addCell(celda4);

            Phrase prValorRecogidas = new Phrase(new Phrase("Valor a descontar por Recogidas  :( " + nf.format(valorDescuentoRecogidas) + ") \n", myfont3));

            String valor = NumberToLetterConverter.convertNumberToLetter(valorRecaudadoFacturas - valorDescuentoRecogidas);
            Phrase prValorManifiesto = new Phrase(new Phrase("Valor total a consignar en éste manifiesto  :( " + nf.format(valorRecaudadoFacturas - valorDescuentoRecogidas) + ") \n" + valor + "\n\n\n", myfont3));

            /**
             * ********************************************************** Tabla  Consignaciones  *****************************************************
             */
            Double valorConsignado = 0.0;
            tablaConsignaciones = new PdfPTable(5);
            tablaConsignaciones.setSpacingBefore(2);
            tablaConsignaciones.getDefaultCell().setPadding(5);
            //tablaConsignaciones.setTotalWidth(435);
            tablaConsignaciones.setTotalWidth(new float[]{35, 90, 120, 120, 100});
            tablaConsignaciones.setLockedWidth(true);

            tablaConsignaciones.setHorizontalAlignment(Element.ALIGN_LEFT);

            /*Columna 1 N°*/
            PdfPCell celdaItem = new PdfPCell(new Phrase("N°", myfont3));
            celdaItem.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 N°*/
            celdaSoporteConsignacion = new PdfPCell(new Phrase("N° Soporte", myfont3));
            celdaSoporteConsignacion.setBorder(Rectangle.NO_BORDER);

            /*Columna 3 Fact. N° */
            celdaBanco = new PdfPCell(new Phrase("Nombre Banco", myfont3));
            celdaBanco.setBorder(Rectangle.NO_BORDER);

            /* Columna 4 Mov. */
            celdaNumeroDeCuenta = new PdfPCell(new Phrase("N° de Cuenta", myfont3));
            celdaNumeroDeCuenta.setBorder(Rectangle.NO_BORDER);

            /* Columna 5 $$ */
            celdaValorConsignacion = new PdfPCell(new Phrase("Valor Consignacion", myfont3));
            celdaValorConsignacion.setBorder(Rectangle.NO_BORDER);

            tablaConsignaciones.addCell(celdaItem);
            tablaConsignaciones.addCell(celdaSoporteConsignacion);
            tablaConsignaciones.addCell(celdaBanco);
            tablaConsignaciones.addCell(celdaNumeroDeCuenta);
            tablaConsignaciones.addCell(celdaValorConsignacion);

            contadorDeFacturas = 0;

            if(manifiesto.getListaDeSoportesConsignaciones() != null){
            
            for (CSoportesConsignaciones obj : manifiesto.getListaDeSoportesConsignaciones()) {

                /*Asignción de valores  las celdas */
                celdaItem = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                celdaItem.setBorder(Rectangle.NO_BORDER);

                celdaSoporteConsignacion = new PdfPCell(new Phrase("" + obj.getNumeroSoporte(), myfont));
                celdaSoporteConsignacion.setBorder(Rectangle.NO_BORDER);

                celdaBanco = new PdfPCell(new Phrase("" + obj.getNombreDelBanco(), myfont));
                celdaBanco.setBorder(Rectangle.NO_BORDER);

                celdaNumeroDeCuenta = new PdfPCell(new Phrase("" + obj.getNumeroDeCuenta(), myfont));
                celdaNumeroDeCuenta.setBorder(Rectangle.NO_BORDER);

                celdaValorConsignacion = new PdfPCell(new Phrase("" + nf.format(obj.getValor()), myfont));
                celdaValorConsignacion.setBorder(Rectangle.NO_BORDER);
               
                /* se le da formato a las celdas del cuerpo del informe */
                /*Incoporación de las celdas a la tabla*/
               
                tablaConsignaciones.addCell(celdaItem);
                tablaConsignaciones.addCell(celdaSoporteConsignacion);
                tablaConsignaciones.addCell(celdaBanco);
                tablaConsignaciones.addCell(celdaNumeroDeCuenta);
                tablaConsignaciones.addCell(celdaValorConsignacion);

                contadorDeFacturas++;
                valorConsignado += obj.getValor();

            }
            }
            contadorDeFacturas = 0;

            Phrase prValorConsignaciones = new Phrase(new Phrase("Valor consignaciones  :( " + nf.format(valorConsignado) + ") \n", myfont3));

            String strValorConsignado = NumberToLetterConverter.convertNumberToLetter(valorConsignado);
            Phrase prValorConsignadoManifiesto = new Phrase(new Phrase("Valor total consignado manifiesto  :( " + nf.format(valorConsignado) + ") \n" + strValorConsignado + "\n\n\n", myfont3));

            /**
             * *****************************************************************************************************************
             */
            tablaDeFirmas = new PdfPTable(2);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + usuarioQueDescargo.getNombres() + " " + usuarioQueDescargo.getApellidos(), myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + manifiesto.getNombreConductor()+ " " + manifiesto.getApellidosConductor(), myfont3));

            celdaRecibido = new PdfPCell(lineaRecibe);
            celdaRecibido.setBorder(Rectangle.NO_BORDER);

            celdaEntregado = new PdfPCell(lineaEntrega);
            celdaEntregado.setBorder(Rectangle.NO_BORDER);

            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaEntregado);

            /**
             * *********************************************Se arma el documento *************************************************
             */
            /*Se inserta el encababezdo y título del informe  */
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(tableEncabezadoInforme);
            document.add(prTituloInforme);

            document.add(new Phrase("\n"));
            document.add(imgCodigoDeBarras);

            /*Se incorpora la tabla de las facturas al documento*/
            document.add(tablaDatosDelInformeFacturas);
            //document.add(new Phrase("\n"));
            document.add(prValorFacturas);
            document.add(new Phrase(valorDescargueFacturas));
            document.add(new Phrase("\n"));

            if (valorRecaudadoFacturas > 0.0) {

                if (valorDescuentoRecogidas > 0.0) {
                    /*Se incorpora la tabla de las Recogidas al documento*/
                    document.add(tablaDatosDelInformeRecogidas);
                    //document.add(new Phrase("\n"));
                    document.add(prValorRecogidas);
                    document.add(new Phrase("\n"));

                    document.add(prValorManifiesto);
                }

                if (manifiesto.getListaDeSoportesConsignaciones().size() > 0) {
                    /*Se incorpora la tabla de las consignaciones al documento*/
                    document.add(tablaConsignaciones);
                    //document.add(new Phrase("\n"));
                    document.add(prValorConsignadoManifiesto);
                    document.add(new Phrase("\n"));
                }

            }

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);

            /*Se cierra el documento documento*/
            document.close();

            File path = new File(ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.codificarManifiesto() + ".pdf");

            try {

                Desktop.getDesktop().open(path);

            } catch (IOException ex) {
                Logger.getLogger(ReporteDescargueDeFacturas1.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error 1 catch  " + ex, "Error", 0);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteDescargueDeFacturas1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteDescargueDeFacturas1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReporteDescargueDeFacturas1.class.getName()).log(Level.SEVERE, null, ex);
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
        String valor;
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
