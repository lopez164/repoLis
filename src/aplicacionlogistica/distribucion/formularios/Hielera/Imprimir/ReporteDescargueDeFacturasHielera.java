/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Imprimir;

//import aplicacionlogistica.distribucion.imprimir.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.distribucion.imprimir.CodigoDeBarras;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Este objeto
 *
 * @author lopez164
 */
public class ReporteDescargueDeFacturasHielera {

    String mfto;
    Inicio ini;
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooterHielera event;
    double valoraConsignar;
    double valorRecaudadoFacturas;
    double valorDescuentoRecogidas;
    boolean conMarcaDeAgua = false;
    // CEmpleados conductor = null;
    CManifiestosDeDistribucion manifiesto = null;
    String fecha = "";
    String horaliquidacion = "";
    String rutaReporte = "";

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    //ArrayList<CFacturasDescargadas> arrFactPorMftoDescargadas;

    public ReporteDescargueDeFacturasHielera(Inicio ini, CManifiestosDeDistribucion manifiesto) {

        /* Este reporte no genera marca de agua como mnifiesto descargado*/
        this.conMarcaDeAgua = false;
        String cadena;
        this.ini = ini;//CUsuarios usuarioQueDescargo = null;
        this.manifiesto = manifiesto; //= null;
        Vst_empleados aux = null; //= null;

        PdfPTable tableEncabezadoInforme;
        PdfPTable tablaDatosDelInformeFacturas;
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
        PdfPCell celdaManifiesto;
        PdfPCell celdaPlaca;
        PdfPCell celdaNombreDelCliente;
        PdfPCell celdaHora;

        PdfPCell celdaMovimiento;
        PdfPCell celdaValorRecaudado;
        PdfPCell celdaUsuario;

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

        double valorTotalRecaudado = 0.0;
        for (CFacturasPorManifiesto fxm : this.manifiesto.getListaFacturasDescargadas()) {
            valorTotalRecaudado += fxm.getValorRecaudado();
            horaliquidacion = fxm.getFechaDistribucion();
        }

        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 40);

            // creation of the different writers
            //rutaReporte = ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.getNumeroManifiesto() + "_" + horaliquidacion.replace("-", "") + ".pdf";
           
            /*Se genera un nombre aleatorio para el archivo*/
            String clave = UUID.randomUUID().toString();
            clave = UUID.randomUUID().toString().substring(clave.length() - 3, clave.length());

            rutaReporte = this.ini.getRutaDeApp() + "manifiestos/" + "Des_" + this.manifiesto.codificarManifiesto() + "-" + clave + ".pdf";

            writer = PdfWriter.getInstance(document, new FileOutputStream(rutaReporte));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            event = new HeaderFooterHielera(this.ini, true, this.manifiesto, writer, conMarcaDeAgua);
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            document.open();

            PdfContentByte cb = writer.getDirectContent();

            /* Se le da formato al número del manifiestos para crear el código de barras*/
            String numManifiesto = getStringNumeroDemanifiesto(this.manifiesto.getNumeroManifiesto());

            /*Se crea el código de barras*/
            imgCodigoDeBarras = codbar.getBarCodeImage("" + numManifiesto, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph prTituloInforme = new Paragraph("INFORME DESCARGUE DE FACTURAS DE DISTRIBUCION", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 250, 93, 240});

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


            /*Se signan los valores a las celdas del encabezado de informe en el documento */
            cellPlaca = new PdfPCell(new Phrase("Placa N° ", myfont3));
            cellPlaca.setBorder(Rectangle.NO_BORDER);
            mCellPlaca = new PdfPCell(new Phrase(": " + this.manifiesto.getVehiculo(), myfont1));
            mCellPlaca.setBorder(Rectangle.NO_BORDER);

            cellKilometros = new PdfPCell(new Phrase("Kilometraje ", myfont3));
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            mCellKilometros = new PdfPCell(new Phrase(": " + " ", myfont1));
            mCellKilometros.setBorder(Rectangle.NO_BORDER);

            cellConductor = new PdfPCell(new Phrase("Conductor  ", myfont3));
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cadena = manifiesto.getNombreConductor() + " " + this.manifiesto.getApellidosConductor();
            if (cadena.length() >= 22) {
                cadena = cadena.substring(0, 22);
            }
            mCellConductor = new PdfPCell(new Phrase(": " + cadena, myfont1));
            mCellConductor.setBorder(Rectangle.NO_BORDER);

            cellRuta = new PdfPCell(new Phrase("Ruta  ", myfont3));
            cellRuta.setBorder(Rectangle.NO_BORDER);
            mCellRuta = new PdfPCell(new Phrase(": " + this.manifiesto.getNombreDeRuta(), myfont1));
            mCellRuta.setBorder(Rectangle.NO_BORDER);

            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + this.manifiesto.getNombreCanal(), myfont1));
            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);

            cellVacia = new PdfPCell(new Phrase("Val. Recaudo", myfont3));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(": " + nf.format(valorTotalRecaudado), myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            mCellFecha = new PdfPCell(new Phrase(": " + horaliquidacion, myfont1));
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
            tablaDatosDelInformeFacturas = new PdfPTable(9);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{23, 50, 40, 40, 140, 80, 40, 60, 55});
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

            PdfPCell cellmanifiesto = new PdfPCell(new Phrase("Mfto.", myfont3));
            //cellFact.setPadding(5);
            cellmanifiesto.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 Fact. N° */
            PdfPCell cellVehiculo = new PdfPCell(new Phrase("Placa", myfont3));
            //cellFact.setPadding(5);
            cellVehiculo.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 Fact. N° */
            PdfPCell nomBreCliente = new PdfPCell(new Phrase("Nombre Cliente", myfont3));
            //cellFact.setPadding(5);
            nomBreCliente.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellHora = new PdfPCell(new Phrase("Hora", myfont3));
            //cellFact.setPadding(5);
            cellHora.setBorder(Rectangle.NO_BORDER);


            /* Columna 3 Mov. */
            PdfPCell cellMov = new PdfPCell(new Phrase("Mov", myfont3));
            //cellMov.setPadding(5);
            cellMov.setBorder(Rectangle.NO_BORDER);

            /* Columna 4 $$ */
            PdfPCell cellVal = new PdfPCell(new Phrase("$$", myfont3));
            //cellVal.setPadding(5);
            cellVal.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellusuario = new PdfPCell(new Phrase("User", myfont3));
            //cellVal.setPadding(5);
            cellusuario.setBorder(Rectangle.NO_BORDER);

            /* Se anexan las celdas al documento */
            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellmanifiesto);
            tablaDatosDelInformeFacturas.addCell(cellVehiculo);
            tablaDatosDelInformeFacturas.addCell(nomBreCliente);
            tablaDatosDelInformeFacturas.addCell(cellHora);
            tablaDatosDelInformeFacturas.addCell(cellMov);
            tablaDatosDelInformeFacturas.addCell(cellVal);
            tablaDatosDelInformeFacturas.addCell(cellusuario);

            /**
             * **************************se inicia el proceso en la parte del
             * informe de las facturas********************************
             */
            int contadorDeFacturas = 0;
            if (this.manifiesto.getListaFacturasDescargadas() != null) {

                for (CFacturasPorManifiesto obj : this.manifiesto.getListaFacturasDescargadas()) {
                    /*Asignción de valores  las celdas */
                    celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                    celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                    celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
                    celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                    celdaManifiesto = new PdfPCell(new Phrase("" + obj.getNumeroManifiesto(), myfont));
                    celdaManifiesto.setBorder(Rectangle.NO_BORDER);

                    celdaPlaca = new PdfPCell(new Phrase("" + obj.getVehiculo(), myfont));
                    celdaPlaca.setBorder(Rectangle.NO_BORDER);

                    celdaNombreDelCliente = new PdfPCell(new Phrase("" + obj.getNombreDeCliente(), myfont));
                    celdaNombreDelCliente.setBorder(Rectangle.NO_BORDER);

                    celdaHora = new PdfPCell(new Phrase("" + obj.getFechaIng(), myfont));
                    celdaHora.setBorder(Rectangle.NO_BORDER);

                    obj.setTipoDeDEscargue();
                    celdaMovimiento = new PdfPCell(new Phrase(obj.getTipoDeDEscargue(), myfont));
                    celdaMovimiento.setBorder(Rectangle.NO_BORDER);

                    celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecaudado()), myfont));
                    celdaValorRecaudado.setBorder(Rectangle.NO_BORDER);

                    celdaUsuario = new PdfPCell(new Phrase(obj.getUsuario(), myfont));
                    celdaUsuario.setBorder(Rectangle.NO_BORDER);

                    /*Incoporación de las celdas a la tabla */
                    tablaDatosDelInformeFacturas.addCell(celdaNUmeroConsecutivo);
                    tablaDatosDelInformeFacturas.addCell(celdaNumeroFactura);
                    tablaDatosDelInformeFacturas.addCell(celdaManifiesto);
                    tablaDatosDelInformeFacturas.addCell(celdaPlaca);
                    tablaDatosDelInformeFacturas.addCell(celdaNombreDelCliente);
                    tablaDatosDelInformeFacturas.addCell(celdaHora);
                    tablaDatosDelInformeFacturas.addCell(celdaMovimiento);
                    tablaDatosDelInformeFacturas.addCell(celdaValorRecaudado);
                    tablaDatosDelInformeFacturas.addCell(celdaUsuario);

                    contadorDeFacturas++;
                    Thread.sleep(10);

                    valorRecaudadoFacturas += obj.getValorRecaudado();

                }
            }

            //String valor = NumberToLetterConverter.convertNumberToLetter(valoraConsignar);
            Phrase prValorFacturas = new Phrase(new Phrase("Valor total Recaudado en facturas  :( " + nf.format(valorRecaudadoFacturas) + ") \n ", myfont3));
            Phrase valorDescargueFacturas = new Phrase(new Phrase("" + NumberToLetterConverter.convertNumberToLetter(valorRecaudadoFacturas), myfont3));

            /**
             * *****************************************************************************************************************
             */
            tablaDeFirmas = new PdfPTable(2);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos(), myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + this.manifiesto.getNombreConductor() + " " + this.manifiesto.getApellidosConductor(), myfont3));

            celdaRecibido = new PdfPCell(lineaRecibe);
            celdaRecibido.setBorder(Rectangle.NO_BORDER);

            celdaEntregado = new PdfPCell(lineaEntrega);
            celdaEntregado.setBorder(Rectangle.NO_BORDER);

            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaEntregado);

            /**
             * *********************************************Se arma el
             * documento *************************************************
             */
            /*Se inserta el encababezdo y título del informe  */
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(tableEncabezadoInforme);
            document.add(prTituloInforme);

            // document.add(new Phrase("\n"));
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
                    //document.add(tablaDatosDelInformeRecogidas);
                    //document.add(new Phrase("\n"));
                    //document.add(prValorRecogidas);
                    document.add(new Phrase("\n"));

                    //document.add(prValorManifiesto);
                }

            }

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);

            /*Se cierra el documento documento*/
            document.close();

            File file = new File(rutaReporte);

            try {

                Desktop.getDesktop().open(file);

            } catch (IOException ex) {
                Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error 1 catch  " + ex, "Error", 0);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeadlessException | InterruptedException ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ReporteDescargueDeFacturasHielera(Inicio ini, CManifiestosDeDistribucion manifiesto, boolean conMarcaDeAgua) {

        /* Este reporte no genera marca de agua como mnifiesto descargado*/
        this.conMarcaDeAgua = true;
        this.ini = ini;
        String cadena;
        CUsuarios usuarioQueDescargo = null;
        this.manifiesto = manifiesto; //= null;
        Vst_empleados aux = null; //= null;

        PdfPTable tableEncabezadoInforme;
        PdfPTable tablaDatosDelInformeFacturas;
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
        PdfPCell celdaManifiesto;
        PdfPCell celdaPlaca;
        PdfPCell celdaRuta;
        PdfPCell celdaCanal;
        PdfPCell celdaHora;

        PdfPCell celdaMovimiento;
        PdfPCell celdaValorRecaudado;
        PdfPCell celdaUsuario;
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

        double valorTotalRecaudado = 0.0;
        for (CFacturasPorManifiesto fxm : this.manifiesto.getListaFacturasDescargadas()) {
            valorTotalRecaudado += fxm.getValorRecaudado();
            horaliquidacion = fxm.getFechaDistribucion();
        }

        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 40);

            // creation of the different writers
            //rutaReporte = ini.getRutaDeApp() + "manifiestos/" + "Des_" + manifiesto.get + "_" + horaliquidacion.replace("-", "") + ".pdf";
            
              /*Se genera un nombre aleatorio para el archivo*/
            String clave = UUID.randomUUID().toString();
            clave = UUID.randomUUID().toString().substring(clave.length() - 3, clave.length());
            
            rutaReporte = this.ini.getRutaDeApp() + "manifiestos/" + "Des_" + this.manifiesto.codificarManifiesto() + "-" + clave + ".pdf";

            writer = PdfWriter.getInstance(document, new FileOutputStream(rutaReporte));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            codbar = new CodigoDeBarras();

            document.open();
            event = new HeaderFooterHielera(this.ini, true, this.manifiesto, writer, this.conMarcaDeAgua);
            writer.setPageEvent(event);

            PdfContentByte cb = writer.getDirectContent();

            /* Se le da formato al número del manifiestos para crear el código de barras*/
            //String numManifiesto = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());

            /*Se crea el código de barras*/
            imgCodigoDeBarras = codbar.getBarCodeImage("" + getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto()), cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph prTituloInforme = new Paragraph("INFORME DESCARGUE DE FACTURAS DE DISTRIBUCION", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 250, 93, 240});

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


            /*Se signan los valores a las celdas del encabezado de informe en el documento */
            cellPlaca = new PdfPCell(new Phrase("Placa N° ", myfont3));
            cellPlaca.setBorder(Rectangle.NO_BORDER);
            mCellPlaca = new PdfPCell(new Phrase(": " + " ", myfont1));
            mCellPlaca.setBorder(Rectangle.NO_BORDER);

            cellKilometros = new PdfPCell(new Phrase("Kilometraje ", myfont3));
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            mCellKilometros = new PdfPCell(new Phrase(": " + " ", myfont1));
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
            mCellRuta = new PdfPCell(new Phrase(": " + " ", myfont1));
            mCellRuta.setBorder(Rectangle.NO_BORDER);

            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + " ", myfont1));
            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);

            cellVacia = new PdfPCell(new Phrase("Val. Recaudo", myfont3));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(": " + nf.format(valorTotalRecaudado), myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            mCellFecha = new PdfPCell(new Phrase(": " + horaliquidacion, myfont1));
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
            tablaDatosDelInformeFacturas = new PdfPTable(10);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{23, 50, 40, 40, 70, 70, 80, 40, 60, 55});
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

            PdfPCell cellmanifiesto = new PdfPCell(new Phrase("Mfto.", myfont3));
            //cellFact.setPadding(5);
            cellmanifiesto.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 Fact. N° */
            PdfPCell cellVehiculo = new PdfPCell(new Phrase("Placa", myfont3));
            //cellFact.setPadding(5);
            cellVehiculo.setBorder(Rectangle.NO_BORDER);

            /*Columna 2 Fact. N° */
            PdfPCell cellRutas = new PdfPCell(new Phrase("Ruta", myfont3));
            //cellFact.setPadding(5);
            cellRutas.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellCanal = new PdfPCell(new Phrase("Canal", myfont3));
            //cellFact.setPadding(5);
            cellCanal.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellHora = new PdfPCell(new Phrase("Hora", myfont3));
            //cellFact.setPadding(5);
            cellHora.setBorder(Rectangle.NO_BORDER);


            /* Columna 3 Mov. */
            PdfPCell cellMov = new PdfPCell(new Phrase("Mov", myfont3));
            //cellMov.setPadding(5);
            cellMov.setBorder(Rectangle.NO_BORDER);

            /* Columna 4 $$ */
            PdfPCell cellVal = new PdfPCell(new Phrase("$$", myfont3));
            //cellVal.setPadding(5);
            cellVal.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellusuario = new PdfPCell(new Phrase("User", myfont3));
            //cellVal.setPadding(5);
            cellusuario.setBorder(Rectangle.NO_BORDER);

            /* Se anexan las celdas al documento */
            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellmanifiesto);
            tablaDatosDelInformeFacturas.addCell(cellVehiculo);
            tablaDatosDelInformeFacturas.addCell(cellRutas);
            tablaDatosDelInformeFacturas.addCell(cellCanal);
            tablaDatosDelInformeFacturas.addCell(cellHora);
            tablaDatosDelInformeFacturas.addCell(cellMov);
            tablaDatosDelInformeFacturas.addCell(cellVal);
            tablaDatosDelInformeFacturas.addCell(cellusuario);

            /**
             * **************************se inicia el proceso en la parte del
             * informe de las facturas********************************
             */
            int contadorDeFacturas = 0;
            if (this.manifiesto.getListaFacturasDescargadas() != null) {

                for (CFacturasPorManifiesto obj : this.manifiesto.getListaFacturasDescargadas()) {
                    /*Asignción de valores  las celdas */
                    celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                    celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                    celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
                    celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                    celdaManifiesto = new PdfPCell(new Phrase("" + obj.getNumeroManifiesto(), myfont));
                    celdaManifiesto.setBorder(Rectangle.NO_BORDER);

                    celdaPlaca = new PdfPCell(new Phrase("" + obj.getVehiculo(), myfont));
                    celdaPlaca.setBorder(Rectangle.NO_BORDER);

                    if (obj.getNombreDeRuta().length() >= 16) {
                        celdaRuta = new PdfPCell(new Phrase("" + obj.getNombreDeRuta().substring(0, 15), myfont));
                    } else {
                        celdaRuta = new PdfPCell(new Phrase("" + obj.getNombreDeRuta(), myfont));
                    }
                    celdaRuta.setBorder(Rectangle.NO_BORDER);

                    celdaCanal = new PdfPCell(new Phrase("" + obj.getNombreCanal(), myfont));
                    celdaCanal.setBorder(Rectangle.NO_BORDER);

                    celdaHora = new PdfPCell(new Phrase("" + obj.getFechaIng(), myfont));
                    celdaHora.setBorder(Rectangle.NO_BORDER);

                    obj.setTipoDeDEscargue();
                    celdaMovimiento = new PdfPCell(new Phrase(obj.getTipoDeDEscargue(), myfont));
                    celdaMovimiento.setBorder(Rectangle.NO_BORDER);

                    //celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecaudado()), myfont));
                    celdaValorRecaudado = new PdfPCell(new Phrase("" + nf.format("0.0"), myfont));
                    celdaValorRecaudado.setBorder(Rectangle.NO_BORDER);

                    celdaUsuario = new PdfPCell(new Phrase(obj.getUsuario(), myfont));
                    celdaUsuario.setBorder(Rectangle.NO_BORDER);

                    /*Incoporación de las celdas a la tabla */
                    tablaDatosDelInformeFacturas.addCell(celdaNUmeroConsecutivo);
                    tablaDatosDelInformeFacturas.addCell(celdaNumeroFactura);
                    tablaDatosDelInformeFacturas.addCell(celdaManifiesto);
                    tablaDatosDelInformeFacturas.addCell(celdaPlaca);
                    tablaDatosDelInformeFacturas.addCell(celdaRuta);
                    tablaDatosDelInformeFacturas.addCell(celdaCanal);
                    tablaDatosDelInformeFacturas.addCell(celdaHora);
                    tablaDatosDelInformeFacturas.addCell(celdaMovimiento);
                    tablaDatosDelInformeFacturas.addCell(celdaValorRecaudado);
                    tablaDatosDelInformeFacturas.addCell(celdaUsuario);

                    contadorDeFacturas++;
                    Thread.sleep(10);

                    valorRecaudadoFacturas += obj.getValorRecaudado();

                }
            }

            //String valor = NumberToLetterConverter.convertNumberToLetter(valoraConsignar);
            Phrase prValorFacturas = new Phrase(new Phrase("Valor total Recaudado en facturas  :( " + nf.format(valorRecaudadoFacturas) + ") \n ", myfont3));
            Phrase valorDescargueFacturas = new Phrase(new Phrase("" + NumberToLetterConverter.convertNumberToLetter(valorRecaudadoFacturas), myfont3));

            /**
             * *****************************************************************************************************************
             */
            tablaDeFirmas = new PdfPTable(2);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos(), myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor(), myfont3));

            celdaRecibido = new PdfPCell(lineaRecibe);
            celdaRecibido.setBorder(Rectangle.NO_BORDER);

            celdaEntregado = new PdfPCell(lineaEntrega);
            celdaEntregado.setBorder(Rectangle.NO_BORDER);

            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaEntregado);

            /**
             * *********************************************Se arma el
             * documento *************************************************
             */
            /*Se inserta el encababezdo y título del informe  */
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(tableEncabezadoInforme);
            document.add(prTituloInforme);

            // document.add(new Phrase("\n"));
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
                    //document.add(tablaDatosDelInformeRecogidas);
                    //document.add(new Phrase("\n"));
                    //document.add(prValorRecogidas);
                    document.add(new Phrase("\n"));

                    //document.add(prValorManifiesto);
                }

            }

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);

            /*Se cierra el documento documento*/
            document.close();

            File file = new File(rutaReporte);

            try {

                Desktop.getDesktop().open(file);

            } catch (IOException ex) {
                Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error 1 catch  " + ex, "Error", 0);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReporteDescargueDeFacturasHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getStringNumeroDemanifiesto(String manifiesto) {
        String valor = "" + manifiesto;

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
