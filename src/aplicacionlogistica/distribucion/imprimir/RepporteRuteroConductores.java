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
 *
 * @author lopez164
 */
public class RepporteRuteroConductores {

    String mfto;
    //String rutaDeArchivo;
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    double valoraConsignar;
    double valorRecaudadoFacturas;
    double valorDescuentoRecogidas;
    public boolean dibujarLinea = true;
    boolean conMarcaDeAgua=false;
    CManifiestosDeDistribucion manifiestoActual;

    String codigoManifiesto;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public RepporteRuteroConductores(Inicio ini, CManifiestosDeDistribucion manifiestoActual) throws Exception {

        this.manifiestoActual = manifiestoActual;

        /*Se crea el usuario que creo el manifiesto*/
        CUsuarios user = new CUsuarios(ini, manifiestoActual.getUsuarioManifiesto(), 0);
        CUsuarios usuarioQueReporta = null;

        codigoManifiesto = this.manifiestoActual.codificarManifiesto();

        String cadena;
        // rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        CEmpleados conductor = null; //= null;
        CEmpleados aux = null; //= null;
        CEmpleados despachador = null;

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
        PdfPCell cellNumeroManifiesto;
        PdfPCell cellCanalDeDistribucion;
        PdfPCell cellVacia;
        PdfPCell cellFecha;
        PdfPCell cellUsuario;

        PdfPCell mCellPlaca;
        PdfPCell mCellKilometros;
        PdfPCell mCellConductor;
        PdfPCell mCellAuxiliar1 = null;
        PdfPCell mCellAuxiliar2 = null;
        PdfPCell mCellAuxiliar3 = null;

        PdfPCell mCellRuta;
        PdfPCell mCellNumeroManifiesto;
        PdfPCell mCellCanalDeDistribucion;
        PdfPCell mCellVacia;
        PdfPCell mCellFecha;
        PdfPCell mCellUsuario;

        /* Declaración de la variables de las celdas en la parte de las firmas */
        PdfPCell celdaRecibido;
        PdfPCell celdaEntregado;
        PdfPCell celdaDespachador;

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
            document = new Document(PageSize.LETTER, 40, 20, 95, 45);

            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream(ini.getRutaDeApp() + "manifiestos/" + this.codigoManifiesto + ".pdf"));

            } catch (DocumentException ex) {
                Logger.getLogger(RepporteRuteroConductores.class.getName()).log(Level.SEVERE, null, ex);
            }
            //writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            document.open();

            // event = new HeaderFooter(ini, dibujarLinea, this.codigoManifiesto);
            event = new HeaderFooter(ini, dibujarLinea, manifiestoActual, writer,conMarcaDeAgua);
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            //document.open();
            PdfContentByte cb = writer.getDirectContent();

            String val = getStringNumeroDemanifiesto(this.manifiestoActual.getNumeroManifiesto());

            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph prTituloInforme = new Paragraph("RUTERO DE SALIDA A DISTRIBUCION", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(697);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 280, 87, 250});

            /* Se inician la variables del encabezado del documento*/
 /* Se crea el objeto conductor*/
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getCedula().equals(this.manifiestoActual.getConductor())) {
                    conductor = new CEmpleados(ini);
                    conductor = obj;
                    break;
                }

            }

            /* Se crea el objeto Despachador*/
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getCedula().equals(this.manifiestoActual.getDespachador())) {
                    despachador = new CEmpleados(ini);
                    despachador = obj;
                    break;
                }

            }

            /*Se crea el objeto Auxiliar*/
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

            int i = 1;

            for (CEmpleados auxiliar : manifiestoActual.getListaDeAuxiliares()) {

                switch (i) {
                    case 1:
                        if (auxiliar.getCedula().equals("0")) {
                            mCellAuxiliar1 = new PdfPCell(new Phrase(": Sin auxiliar asignado", myfont1));
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
                            mCellAuxiliar3.setBorder(Rectangle.NO_BORDER);
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

            /* SE CREA EL OBJETO RUTAS DE DISTRIBUCION */
            CRutasDeDistribucion rutaObj = new CRutasDeDistribucion(ini);
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getIdRutasDeDistribucion() == this.manifiestoActual.getIdRuta()) {
                    rutaObj = obj;
                }
            }

            /* LLAMA AL OBJETO CANALES DE VENTA */
            CCanalesDeVenta canal = new CCanalesDeVenta(ini);
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getIdCanalDeVenta() == this.manifiestoActual.getIdCanal()) {
                    canal = obj;
                }
            }

            /*Se signan los valores a las celdas del encabezado de informe en el documento */
            cellPlaca = new PdfPCell(new Phrase("Placa N° ", myfont3));
            cellPlaca.setBorder(Rectangle.NO_BORDER);
            mCellPlaca = new PdfPCell(new Phrase(": " + this.manifiestoActual.getVehiculo(), myfont1));
            mCellPlaca.setBorder(Rectangle.NO_BORDER);

            cellKilometros = new PdfPCell(new Phrase("Kilometraje ", myfont3));
            cellKilometros.setBorder(Rectangle.NO_BORDER);
            mCellKilometros = new PdfPCell(new Phrase(": " + this.manifiestoActual.getKmSalida(), myfont1));
            mCellKilometros.setBorder(Rectangle.NO_BORDER);

            cellConductor = new PdfPCell(new Phrase("Conductor  ", myfont3));
            cellConductor.setBorder(Rectangle.NO_BORDER);
            cadena = manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor();
            if (cadena.length() >= 22) {
                cadena = cadena.substring(0, 22);
            }
            mCellConductor = new PdfPCell(new Phrase(": " + cadena, myfont1));
            mCellConductor.setBorder(Rectangle.NO_BORDER);

            cellRuta = new PdfPCell(new Phrase("Ruta  ", myfont3));
            cellRuta.setBorder(Rectangle.NO_BORDER);
            mCellRuta = new PdfPCell(new Phrase(": " + rutaObj.getNombreRutasDeDistribucion(), myfont1));
            mCellRuta.setBorder(Rectangle.NO_BORDER);

            cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  ", myfont3));
            cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
            mCellNumeroManifiesto = new PdfPCell(new Phrase(": " + this.manifiestoActual.getNumeroManifiesto(), myfont1));
            mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);

            cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
            cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
            mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + canal.getNombreCanalDeVenta(), myfont1));
            mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);

            cellVacia = new PdfPCell(new Phrase("Val manfto", myfont3));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(": " + nf.format(this.manifiestoActual.getValorTotalManifiesto()), myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);

            cellFecha = new PdfPCell(new Phrase("Fecha ", myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            mCellFecha = new PdfPCell(new Phrase(": " + this.manifiestoActual.getHoraDeLiquidacion(), myfont1));
            mCellFecha.setBorder(Rectangle.NO_BORDER);

            cellUsuario = new PdfPCell(new Phrase("Usuario ", myfont3));
            cellUsuario.setBorder(Rectangle.NO_BORDER);
            cadena = user.getNombres() + " " + user.getApellidos();
            if (cadena.length() >= 26) {
                cadena = cadena.substring(0, 25);
            }

            mCellUsuario = new PdfPCell(new Phrase(": " + cadena, myfont1));
            mCellUsuario.setBorder(Rectangle.NO_BORDER);

//            /* se asignan las celdas a la tabla del encabezado en el  documento*/
//            tableEncabezadoInforme.addCell(cellPlaca);
//            tableEncabezadoInforme.addCell(mCellPlaca);
//
//            tableEncabezadoInforme.addCell(cellKilometros);
//            tableEncabezadoInforme.addCell(mCellKilometros);
//
//            tableEncabezadoInforme.addCell(cellConductor);
//            tableEncabezadoInforme.addCell(mCellConductor);
//
//            tableEncabezadoInforme.addCell(cellAuxiliar1);
//            tableEncabezadoInforme.addCell(mCellAuxiliar1);
//            
//            tableEncabezadoInforme.addCell(cellAuxiliar2);
//            tableEncabezadoInforme.addCell(mCellAuxiliar2);
//            
//            tableEncabezadoInforme.addCell(cellAuxiliar3);
//            tableEncabezadoInforme.addCell(mCellAuxiliar3);
//
//            tableEncabezadoInforme.addCell(cellRuta);
//            tableEncabezadoInforme.addCell(mCellRuta);
//
//            tableEncabezadoInforme.addCell(cellNumeroManifiesto);
//            tableEncabezadoInforme.addCell(mCellNumeroManifiesto);
//
//            tableEncabezadoInforme.addCell(cellCanalDeDistribucion);
//            tableEncabezadoInforme.addCell(mCellCanalDeDistribucion);
//
//            tableEncabezadoInforme.addCell(cellVacia);
//            tableEncabezadoInforme.addCell(mCellVacia);
//            tableEncabezadoInforme.addCell(cellFecha);
//            tableEncabezadoInforme.addCell(mCellFecha);
//
//            tableEncabezadoInforme.addCell(cellUsuario);
//            tableEncabezadoInforme.addCell(mCellUsuario);
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
            //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, write

            //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            // Encabezado de la Tabla del cuerpo del rutero
            tablaDatosDelInformeFacturas = new PdfPTable(6);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{30, 55, 30, 175, 185, 90});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            PdfPCell cellNo = new PdfPCell(new Phrase("N° ", myfont3));
            cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellFactura = new PdfPCell(new Phrase("N° Fac.", myfont3));
            cellFactura.setPadding(5);
            cellFactura.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellSalidasDistribucion = new PdfPCell(new Phrase("S.D.", myfont3));
            cellSalidasDistribucion.setPadding(5);
            cellSalidasDistribucion.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellNombreClientet = new PdfPCell(new Phrase("Cliente ", myfont3));
            cellNombreClientet.setPadding(5);
            cellNombreClientet.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellDireccion = new PdfPCell(new Phrase("Direccción ", myfont3));
            cellDireccion.setPadding(5);
            cellDireccion.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellValor = new PdfPCell(new Phrase("Valor Fact. ", myfont3));
            cellValor.setPadding(5);
            cellValor.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFactura);
            tablaDatosDelInformeFacturas.addCell(cellSalidasDistribucion);
            tablaDatosDelInformeFacturas.addCell(cellNombreClientet);
            tablaDatosDelInformeFacturas.addCell(cellDireccion);
            tablaDatosDelInformeFacturas.addCell(cellValor);

            int contadorDeFacturas = 0;
            double valorManifiesto = 0.0;

            /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
            for (CFacturasPorManifiesto obj : this.manifiestoActual.getListaFacturasPorManifiesto()) {

                if (contadorDeFacturas == 0) {
                    for (CUsuarios usu : ini.getListaDeUsuarios()) {
                        if (obj.getUsuario()==null) {
                            if (usu.getNombreUsuario().equals(Inicio.cifrar(this.manifiestoActual.getUsuarioManifiesto()))) {
                                usuarioQueReporta = usu;
                                break;
                            }

                        } else {
                            if (usu.getNombreUsuario().equals(Inicio.cifrar(obj.getUsuario()))) {
                                usuarioQueReporta = usu;
                                break;
                            }
                        }
                    }
                }

                // consecutivo
                PdfPCell celda1 = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
                celda1.setBorder(Rectangle.NO_BORDER);

                // NUMERO DE LA FACTURA
                PdfPCell celda2 = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
                celda2.setBorder(Rectangle.NO_BORDER);

                // CANTIDAD DE SALIDAS A DISTRIBUCION
                PdfPCell celda3;
                if (obj.getSalidasDistribucion() > 1) {
                    celda3 = new PdfPCell(new Phrase("R" + obj.getSalidasDistribucion(), myfont));
                } else {
                    celda3 = new PdfPCell(new Phrase("" + obj.getSalidasDistribucion(), myfont));
                }
                celda3.setBorder(Rectangle.NO_BORDER);

                // Nombre del Cliente             
                cadena = obj.getNombreDeCliente();
                if (cadena.length() >= 36) {
                    cadena = cadena.substring(0, 35);
                }
                PdfPCell celda4 = new PdfPCell(new Phrase("" + (cadena), myfont));
                celda4.setBorder(Rectangle.NO_BORDER);

                // Direcccion del Cliente  
                cadena = obj.getDireccionDeCliente();
                if (cadena.length() >= 36) {
                    cadena = cadena.substring(0, 35);
                }
                PdfPCell celda5 = new PdfPCell(new Phrase("" + (cadena), myfont));
                celda5.setBorder(Rectangle.NO_BORDER);

                // Valor de la factura             
                PdfPCell celda6 = new PdfPCell(new Phrase("" + nf.format(obj.getValorTotalFactura()), myfont));
                celda6.setBorder(Rectangle.NO_BORDER);

                tablaDatosDelInformeFacturas.addCell(celda1);
                tablaDatosDelInformeFacturas.addCell(celda2);
                tablaDatosDelInformeFacturas.addCell(celda3);
                tablaDatosDelInformeFacturas.addCell(celda4);
                tablaDatosDelInformeFacturas.addCell(celda5);
                tablaDatosDelInformeFacturas.addCell(celda6);

                contadorDeFacturas++;
                valorManifiesto += obj.getValorTotalFactura();

            }

            String valor = NumberToLetterConverter.convertNumberToLetter(valorManifiesto);

            Paragraph prValorManifiesto = new Paragraph(new Phrase("Valor total éste manifiesto  :( " + nf.format(valorManifiesto) + ") \n" + valor + "\n\n\n", myfont1));

            tablaDeFirmas = new PdfPTable(3);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(600);
            tablaDeFirmas.setTotalWidth(new float[]{200, 200, 200});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("________________________________\n" + "Recibe : \n" + manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor(), myfont1));
            Phrase lineaEntrega = new Phrase(new Phrase("________________________________\n" + "Entrega : \n" + usuarioQueReporta.getNombres() + " " + usuarioQueReporta.getApellidos(), myfont1));
            Phrase lineaDespachador = new Phrase(new Phrase("________________________________\n" + "Despacha : \n" + manifiestoActual.getNombreDespachador() + " " + manifiestoActual.getApellidosDespachador(), myfont1));

            celdaRecibido = new PdfPCell(lineaRecibe);
            celdaRecibido.setBorder(Rectangle.NO_BORDER);

            celdaEntregado = new PdfPCell(lineaEntrega);
            celdaEntregado.setBorder(Rectangle.NO_BORDER);

            celdaDespachador = new PdfPCell(lineaDespachador);
            celdaDespachador.setBorder(Rectangle.NO_BORDER);

            tablaDeFirmas.addCell(celdaEntregado);
            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaDespachador);

            /* ACA SE ARMA EL REPORTE DESDE EL ENCABEZADO, CUERPO DEL INFORME
            Y EL PIE DEL DOCUMENTO*/
            document.add(tableEncabezadoInforme);
            document.add(prTituloInforme);

            document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            /*Se incorpora la tabla de las facturas al documento*/
            document.add(tablaDatosDelInformeFacturas);
            document.add(new Phrase("\n"));
            //document.add( prValorManifiest);
            document.add(prValorManifiesto);


            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);

            /*Se cierra el documento documento*/
            document.close();

            File path = new File(ini.getRutaDeApp() + "manifiestos/" + this.codigoManifiesto + ".pdf");

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
