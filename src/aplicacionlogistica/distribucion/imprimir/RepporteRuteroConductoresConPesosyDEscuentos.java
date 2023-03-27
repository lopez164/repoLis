/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.costumerService.objetos.CDescuentosPorFactura;
import aplicacionlogistica.costumerService.objetos.CRecogidasPorFactura;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorMinuta;
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
public class RepporteRuteroConductoresConPesosyDEscuentos {

    String mfto;
    //String rutaDeArchivo;
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    double valoraConsignar;
    double valorRecaudadoFacturas;
    double valorDescuentoFacturas;
    double valorRecogidasFacturas;
    
    public boolean dibujarLinea = true;
    boolean conMarcaDeAgua=true;
    //double valorManifiesto = 0.0;
    //double pesoKgManifiesto = 0.0;
    CManifiestosDeDistribucion manifiestoActual;

    PdfPTable tablaDatosDelInformeDescuentos;
    PdfPTable tablaDatosDelInformeRecogidas;
    PdfPTable tablaMinuta;
    
    Phrase prValorDescuentos;
    Phrase prValorRecogidas;

    String codigoManifiesto;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    NumberFormat formatoNumero = NumberFormat.getNumberInstance();

    // DecimalFormat formatoNumero = new DecimalFormat("#,###.##");
    // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public RepporteRuteroConductoresConPesosyDEscuentos(Inicio ini, CManifiestosDeDistribucion manifiestoActual) throws Exception {

        this.manifiestoActual = manifiestoActual;
        
        if(manifiestoActual.getEstadoManifiesto()==3){
            conMarcaDeAgua=false;
        }
        

        /*Se crea el usuario que creo el manifiesto*/
        CUsuarios user = new CUsuarios(ini, manifiestoActual.getUsuarioManifiesto(), 0);
        CUsuarios usuarioQueReporta = null;

        codigoManifiesto = this.manifiestoActual.codificarManifiesto();

        String cadena;
        // rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        
       // CEmpleados conductor = null; //= null;
        //CEmpleados aux = null; //= null;
        //CEmpleados despachado = null;

        PdfPTable tableEncabezadoInforme;
        PdfPTable tablaDatosDelInformeFacturas;
        PdfPTable tablaDeFirmas;

        /* Declaración de la variables de las celdas en la parte del encabezado del informe*/
        PdfPCell cellPlaca;
        PdfPCell cellKilometros;
        PdfPCell cellConductor;
        PdfPCell cellAuxiliar1 = null;
        PdfPCell cellAuxiliar2 = null;
        PdfPCell cellPesoEnKgMfto = null;
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
        PdfPCell mCellPesoKgMfto = null;

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

        PdfPCell celdaNUmeroConsecutivo;
        PdfPCell celdaNumeroFactura;
        PdfPCell celdaDocumentoSoporte;
        PdfPCell celdaValorDescuento;

        // various fonts
        Font myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
        Font myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
        Font myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
        Font myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
        Font myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
        Font myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
        Font myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
        Font myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));

        formatoNumero.setMaximumFractionDigits(2);

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
                Logger.getLogger(RepporteRuteroConductoresConPesosyDEscuentos.class.getName()).log(Level.SEVERE, null, ex);
            }
            //writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            document.open();

            // event = new HeaderFooter(ini, dibujarLinea, this.codigoManifiesto);
            event = new HeaderFooter(ini, dibujarLinea, this.manifiestoActual, writer,conMarcaDeAgua);
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
 
//            /* Se crea el objeto Despachador*/
//            for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                if (obj.getCedula().equals(this.manifiestoActual.getDespachador())) {
//                    despachador = new CEmpleados(ini);
//                    despachador = obj;
//                    break;
//                }
//
//            }

             /*
             * ******************************************* Tabla  encabezado del informe  *****************************************************
             */

            /*costruye el encabezado del informe*/
            tablaEncabezadoDeInforme(myfont3, myfont1, ini, user, tableEncabezadoInforme);
            
            
             /*
             * ******************************************* Tabla cuerpo del informe************************************************************
             */

            tablaDatosDelInformeFacturas = new PdfPTable(7);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{25, 45, 35, 175, 165, 60, 60});
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

            PdfPCell cellPesoKg = new PdfPCell(new Phrase("Kg. Fact. ", myfont3));
            cellPesoKg.setPadding(5);
            cellPesoKg.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFactura);
            tablaDatosDelInformeFacturas.addCell(cellSalidasDistribucion);
            tablaDatosDelInformeFacturas.addCell(cellNombreClientet);
            tablaDatosDelInformeFacturas.addCell(cellDireccion);
            tablaDatosDelInformeFacturas.addCell(cellValor);
            tablaDatosDelInformeFacturas.addCell(cellPesoKg);

            int contadorDeFacturas = 0;


            /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
            for (CFacturasPorManifiesto obj : this.manifiestoActual.getListaFacturasPorManifiesto()) {

                if (contadorDeFacturas == 0) {
                    for (CUsuarios usu : ini.getListaDeUsuarios()) {
                        if (obj.getUsuario() == null) {
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
                PdfPCell celda2;
                if(obj.getFormaDePago().equals("CREDITO")){
                    celda2 = new PdfPCell(new Phrase("*" + obj.getNumeroFactura(), myfont)); 
                }else{
                    celda2 = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont)); 
                }
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

                // Peso en Kg de la factura         
                PdfPCell celda7 = new PdfPCell(new Phrase("" + formatoNumero.format(obj.getPesoFactura()/1000) + " Kg.", myfont));
                celda7.setBorder(Rectangle.NO_BORDER);

                tablaDatosDelInformeFacturas.addCell(celda1);
                tablaDatosDelInformeFacturas.addCell(celda2);
                tablaDatosDelInformeFacturas.addCell(celda3);
                tablaDatosDelInformeFacturas.addCell(celda4);
                tablaDatosDelInformeFacturas.addCell(celda5);
                tablaDatosDelInformeFacturas.addCell(celda6);
                tablaDatosDelInformeFacturas.addCell(celda7);

                contadorDeFacturas++;
                //valorManifiesto += obj.getValorTotalFactura();
                //pesoKgManifiesto += obj.getPesoFactura();

            }

            /**
             * ******************************************* Tabla de los descuentos  ***************************************************************
             *
             * /* Se instancia la tabla del informe de los Descuentos
             */
            Paragraph prTitulolistaDescuentos = new Paragraph("Lista de Facturas con Descuento", myfont5);
            prTitulolistaDescuentos.setAlignment(Element.ALIGN_CENTER);
            
            if (this.manifiestoActual.getListaDeDescuentos()!=null && this.manifiestoActual.getListaDeDescuentos().size() > 0) {

                tablaDescuentosPorFactura(myfont3, contadorDeFacturas, myfont);

            }
            /**
             * *********************************************************** FIN DESCUENTOS ***********************************************************
             */
           
            
             /**
             * ******************************************* Tabla de las Recogidas  ***************************************************************
             *
             * /* Se instancia la tabla del informe de las recogidas
             */
            Paragraph prTitulolistaRecogidas = new Paragraph("Lista de Facturas con Recogidas", myfont5);
            prTitulolistaRecogidas.setAlignment(Element.ALIGN_CENTER);
            
            if (this.manifiestoActual.getListaDeRecogidas()!=null && this.manifiestoActual.getListaDeRecogidas().size() > 0 ) {

                tablaRecogidasPorFactura(myfont3, contadorDeFacturas, myfont);

            }
            /**
             * *********************************************************** FIN Recogidas ***********************************************************
             */
           
            
             /*************************************************************TABLA DE MINUTA DEL MANIFIESTO**********************************************/
            
             if(ini.isImprimirMinuta()){
            this.manifiestoActual.setListaDeProductosMinuta();
            
             tablaMinuta(myfont3, contadorDeFacturas, myfont);
            
             }
            /***************************************************************FIN DE LA MINUTA**********************************************************/

            
            
           String valor = NumberToLetterConverter.convertNumberToLetter(this.manifiestoActual.getValorTotalManifiesto(false));

           Paragraph prValorManifiesto = new Paragraph(new Phrase("Valor total éste manifiesto  :( " + nf.format(this.manifiestoActual.getValorTotalManifiesto(false)) + ") \n" + valor + "\n\n\n", myfont1));

            tablaDeFirmas = new PdfPTable(3);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(600);
            tablaDeFirmas.setTotalWidth(new float[]{200, 200, 200});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("________________________________\n" + "Recibe Conductor  : \n" + manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor(), myfont1));
            Phrase lineaEntrega = new Phrase(new Phrase("________________________________\n" + "Entrega : \n" + usuarioQueReporta.getNombres() + " " + usuarioQueReporta.getApellidos(), myfont1));
            Phrase lineaDespachador = new Phrase(new Phrase("________________________________\n" + "Despacha : \n" + manifiestoActual.getNombreDespachador() + " " + manifiestoActual.getApellidosDespachador(), myfont1));

            Phrase lineaObservaciones = new Phrase(new Phrase("\n\n" + "OBSERVACIONES : \n",myfont6));
            Phrase Observaciones = new Phrase(new Phrase(manifiestoActual.getObservaciones() + "\n",myfont4));
            
                    
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

            /*Se valida si hay facturas con descuento*/
            if (manifiestoActual.getListaDeDescuentos()!=null && this.manifiestoActual.getListaDeDescuentos().size() > 0 ) {
                document.add(prTitulolistaDescuentos);
                document.add(tablaDatosDelInformeDescuentos);
                document.add(prValorDescuentos);
            }
            
             /*Se valida si hay facturas con descuento*/
            if (manifiestoActual.getListaDeRecogidas()!=null && this.manifiestoActual.getListaDeRecogidas().size() > 0  ) {
                document.add(prTitulolistaRecogidas);
               document.add(tablaDatosDelInformeRecogidas);
                document.add(prValorRecogidas);
            }
            
            if(ini.isImprimirMinuta()){
            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaMinuta);
             document.add(new Phrase("\n\n"));
            }

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);
            
             /*Se incorpora las observaciones del manifiesto*/
            document.add(lineaObservaciones);
            document.add(Observaciones);
            

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
    
     /**
     * Método que construye la tabla del encabezado del informe de salida a 
     * Distribucion
     *
     * @param manifiestoActual1
     * 
     */
    

    private void tablaEncabezadoDeInforme(Font myfont3, Font myfont1, Inicio ini, CUsuarios user, PdfPTable tableEncabezadoInforme) {
        PdfPCell cellAuxiliar1;
        PdfPCell cellAuxiliar2;
        PdfPCell cellPesoEnKgMfto;
        PdfPCell mCellAuxiliar1;
        PdfPCell mCellAuxiliar2;
        PdfPCell mCellPesoKgMfto;
        String cadena;
        PdfPCell cellPlaca;
        PdfPCell mCellPlaca;
        PdfPCell cellKilometros;
        PdfPCell mCellKilometros;
        PdfPCell cellConductor;
        PdfPCell mCellConductor;
        PdfPCell cellRuta;
        PdfPCell mCellRuta;
        PdfPCell cellNumeroManifiesto;
        PdfPCell mCellNumeroManifiesto;
        PdfPCell cellCanalDeDistribucion;
        PdfPCell mCellCanalDeDistribucion;
        PdfPCell cellVacia;
        PdfPCell mCellVacia;
        PdfPCell cellFecha;
        PdfPCell mCellFecha;
        PdfPCell cellUsuario;
        PdfPCell mCellUsuario;
        /*Se crea el objeto Auxiliar*/
        cellAuxiliar1 = new PdfPCell(new Phrase("Auxiliar1 ", myfont3));
        cellAuxiliar2 = new PdfPCell(new Phrase("Auxiliar2 ", myfont3));
        cellPesoEnKgMfto = new PdfPCell(new Phrase("Peso Kg.", myfont3));
        cellAuxiliar1.setBorder(Rectangle.NO_BORDER);
        cellAuxiliar2.setBorder(Rectangle.NO_BORDER);
        cellPesoEnKgMfto.setBorder(Rectangle.NO_BORDER);
        mCellAuxiliar1 = new PdfPCell(new Phrase(": Sin auxiliar1 asignado", myfont1));
        mCellAuxiliar2 = new PdfPCell(new Phrase(": Sin auxiliar2 asignado", myfont1));
        mCellPesoKgMfto = new PdfPCell(new Phrase("" + 0.0 + " Kg.", myfont1));
        mCellAuxiliar1.setBorder(Rectangle.NO_BORDER);
        mCellAuxiliar2.setBorder(Rectangle.NO_BORDER);
        mCellPesoKgMfto.setBorder(Rectangle.NO_BORDER);
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
                    
            }
            
            i++;
                   
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
        mCellRuta = new PdfPCell(new Phrase(": " + manifiestoActual.getNombreDeRuta(), myfont1));
        mCellRuta.setBorder(Rectangle.NO_BORDER);
        cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  ", myfont3));
        cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
        mCellNumeroManifiesto = new PdfPCell(new Phrase(": " + this.manifiestoActual.getNumeroManifiesto(), myfont1));
        mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
        cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
        cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
        mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + manifiestoActual.getNombreCanal(), myfont1));
        mCellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
        cellVacia = new PdfPCell(new Phrase("Val manfto", myfont3));
        cellVacia.setBorder(Rectangle.NO_BORDER);
        mCellVacia = new PdfPCell(new Phrase(": " + nf.format(manifiestoActual.getValorTotalManifiesto(false)), myfont1));
        mCellVacia.setBorder(Rectangle.NO_BORDER);
        cellFecha = new PdfPCell(new Phrase("Fecha ", myfont3));
        cellFecha.setBorder(Rectangle.NO_BORDER);
        mCellFecha = new PdfPCell(new Phrase(": " + this.manifiestoActual.getFechaDistribucion(), myfont1));
        mCellFecha.setBorder(Rectangle.NO_BORDER);
        cellUsuario = new PdfPCell(new Phrase("Usuario ", myfont3));
        cellUsuario.setBorder(Rectangle.NO_BORDER);
        cadena = user.getNombres() + " " + user.getApellidos();
        if (cadena.length() >= 26) {
            cadena = cadena.substring(0, 25);
        }
        mCellUsuario = new PdfPCell(new Phrase(": " + cadena, myfont1));
        mCellUsuario.setBorder(Rectangle.NO_BORDER);
        mCellPesoKgMfto = new PdfPCell(new Phrase("" + formatoNumero.format(manifiestoActual.getPesoKgManifiesto()) + " Kg.", myfont1));
        mCellPesoKgMfto.setBorder(Rectangle.NO_BORDER);
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
        tableEncabezadoInforme.addCell(cellPesoEnKgMfto);
        tableEncabezadoInforme.addCell(mCellPesoKgMfto);
        tableEncabezadoInforme.addCell(cellVacia);
        tableEncabezadoInforme.addCell(mCellVacia);
        tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);
        //tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, write
    }
   
    
    
     /**
     * Método que construye la tabla con los descuentos asignadoos  a las facturas
     * que se encuentra en el manifiesto actual
     *
     * @param manifiestoOrigen
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    private void tablaDescuentosPorFactura(Font myfont3, int contadorDeFacturas, Font myfont) throws DocumentException {
        
        /*Declaracion de variables para la construcion de la tabla*/
        PdfPCell celdaNUmeroConsecutivo;
        PdfPCell celdaNumeroFactura;
        PdfPCell celdaDocumentoSoporte;
        PdfPCell celdaValorDescuento;
        
        /*Costruye la tabla*/
        tablaDatosDelInformeDescuentos = new PdfPTable(12);
        tablaDatosDelInformeDescuentos.setSpacingBefore(2);
        tablaDatosDelInformeDescuentos.getDefaultCell().setPadding(5);
        tablaDatosDelInformeDescuentos.setTotalWidth(600);
        tablaDatosDelInformeDescuentos.setTotalWidth(new float[]{23, 50, 50, 70, 23, 50, 50, 70, 23, 50, 50, 70});
        tablaDatosDelInformeDescuentos.setLockedWidth(true);
        
        /*cosntruye las celdas del encabezado*/
        PdfPCell cellItem = new PdfPCell(new Phrase("N° ", myfont3));
        cellItem.setPadding(5);
        cellItem.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellNumFactura = new PdfPCell(new Phrase("N° Fac.", myfont3));
        cellNumFactura.setPadding(5);
        cellNumFactura.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSoporte = new PdfPCell(new Phrase("Soporte.", myfont3));
        cellSoporte.setPadding(5);
        cellSoporte.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellvalor = new PdfPCell(new Phrase("Valor $ ", myfont3));
        cellvalor.setPadding(5);
        cellvalor.setBorder(Rectangle.NO_BORDER);
        contadorDeFacturas = 0;
        
        
        /* Costruye la fila de encabezado de la tabla */
        for (int w = 0; w < 3; w++) {
            tablaDatosDelInformeDescuentos.addCell(cellItem);
            tablaDatosDelInformeDescuentos.addCell(cellNumFactura);
            tablaDatosDelInformeDescuentos.addCell(cellSoporte);
            tablaDatosDelInformeDescuentos.addCell(cellvalor);
        }
        /*Asignción de valores  las celdas */
        for (CDescuentosPorFactura obj : this.manifiestoActual.getListaDeDescuentos()) {
            
            /*Asigna el numero del consecutivo de la factura*/
            celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
            celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el numero de la Factura*/
            celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
            celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);
            
            
            /*Asign el numero del soporte del descuento*/
            celdaDocumentoSoporte = new PdfPCell(new Phrase(obj.getNumeroDocumento(), myfont));
            celdaDocumentoSoporte.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el valor del descuento*/
            celdaValorDescuento = new PdfPCell(new Phrase("" + nf.format(obj.getValorDescuento()), myfont));
            celdaValorDescuento.setBorder(Rectangle.NO_BORDER);
            /* se le da formato a las celdas del cuerpo del informe */
            
            /*Incoporación de las celdas al documento*/
            tablaDatosDelInformeDescuentos.addCell(celdaNUmeroConsecutivo);
            tablaDatosDelInformeDescuentos.addCell(celdaNumeroFactura);
            tablaDatosDelInformeDescuentos.addCell(celdaDocumentoSoporte);
            tablaDatosDelInformeDescuentos.addCell(celdaValorDescuento);
            
            contadorDeFacturas++;
            
            
            
            valorDescuentoFacturas += obj.getValorDescuento();
        }
        
        /*Calcula si en la fila quedan celdas vacias y los acomoda en el informe
        con un colspan uniendo las celdas desocupadas*/
        int residuo = this.manifiestoActual.getListaDeDescuentos().size() % 3;
        residuo = 3 - residuo;
        PdfPCell celda4 = new PdfPCell(new Phrase(""));
        celda4.setBorder(Rectangle.NO_BORDER);
        celda4.setColspan(residuo * 4);
        
        tablaDatosDelInformeDescuentos.addCell(celda4);
        
        prValorDescuentos = new Phrase(new Phrase("Valorde los Descuentos :( " + nf.format(valorDescuentoFacturas) + ") \n", myfont3));
    }

    
    
     /**
     * Método que construye la tabla con los descuentos asignadoos  a las facturas
     * que se encuentra en el manifiesto actual
     *
     * @param manifiestoOrigen
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    private void tablaRecogidasPorFactura(Font myfont3, int contadorDeFacturas, Font myfont) throws DocumentException {
        
        /*Declaracion de variables para la construcion de la tabla*/
        PdfPCell celdaNUmeroConsecutivo;
        PdfPCell celdaNumeroFactura;
        PdfPCell celdaDocumentoSoporte;
        PdfPCell celdaValorDescuento;
        
        /*Costruye la tabla*/
        tablaDatosDelInformeRecogidas = new PdfPTable(12);
        tablaDatosDelInformeRecogidas.setSpacingBefore(2);
        tablaDatosDelInformeRecogidas.getDefaultCell().setPadding(5);
        tablaDatosDelInformeRecogidas.setTotalWidth(600);
        tablaDatosDelInformeRecogidas.setTotalWidth(new float[]{23, 50, 50, 70, 23, 50, 50, 70, 23, 50, 50, 70});
        tablaDatosDelInformeRecogidas.setLockedWidth(true);
        
        /*cosntruye las celdas del encabezado*/
        PdfPCell cellItem = new PdfPCell(new Phrase("N° ", myfont3));
        cellItem.setPadding(5);
        cellItem.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellNumFactura = new PdfPCell(new Phrase("N° Fac.", myfont3));
        cellNumFactura.setPadding(5);
        cellNumFactura.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSoporte = new PdfPCell(new Phrase("Soporte.", myfont3));
        cellSoporte.setPadding(5);
        cellSoporte.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellvalor = new PdfPCell(new Phrase("Valor $ ", myfont3));
        cellvalor.setPadding(5);
        cellvalor.setBorder(Rectangle.NO_BORDER);
        contadorDeFacturas = 0;
        
        
        /* Costruye la fila de encabezado de la tabla */
        for (int w = 0; w < 3; w++) {
            tablaDatosDelInformeRecogidas.addCell(cellItem);
            tablaDatosDelInformeRecogidas.addCell(cellNumFactura);
            tablaDatosDelInformeRecogidas.addCell(cellSoporte);
            tablaDatosDelInformeRecogidas.addCell(cellvalor);
        }
        /*Asignción de valores  las celdas */
        for (CRecogidasPorFactura obj : this.manifiestoActual.getListaDeRecogidas()) {
            
            /*Asigna el numero del consecutivo de la factura*/
            celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont));
            celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el numero de la Factura*/
            celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont));
            celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);
            
            
            /*Asign el numero del soporte del descuento*/
            celdaDocumentoSoporte = new PdfPCell(new Phrase(obj.getNumeroDocumento(), myfont));
            celdaDocumentoSoporte.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el valor del descuento*/
            celdaValorDescuento = new PdfPCell(new Phrase("" + nf.format(obj.getValorRecogida()), myfont));
            celdaValorDescuento.setBorder(Rectangle.NO_BORDER);
            /* se le da formato a las celdas del cuerpo del informe */
            
            /*Incoporación de las celdas al documento*/
            tablaDatosDelInformeRecogidas.addCell(celdaNUmeroConsecutivo);
            tablaDatosDelInformeRecogidas.addCell(celdaNumeroFactura);
            tablaDatosDelInformeRecogidas.addCell(celdaDocumentoSoporte);
            tablaDatosDelInformeRecogidas.addCell(celdaValorDescuento);
            
            contadorDeFacturas++;
            
            
            
            valorRecogidasFacturas += obj.getValorRecogida();
        }
        
        /*Calcula si en la fila quedan celdas vacias y los acomoda en el informe
        con un colspan uniendo las celdas desocupadas*/
        int residuo = this.manifiestoActual.getListaDeRecogidas().size() % 3;
        residuo = 3 - residuo;
        PdfPCell celda4 = new PdfPCell(new Phrase(""));
        celda4.setBorder(Rectangle.NO_BORDER);
        celda4.setColspan(residuo * 4);
        
        tablaDatosDelInformeRecogidas.addCell(celda4);
        
        prValorRecogidas = new Phrase(new Phrase("Valor de las Recogidas :( " + nf.format(valorRecogidasFacturas) + ") \n", myfont3));
    }
    
      private void tablaMinuta(Font myfont3, int contadorDeFacturas, Font myfont) throws DocumentException {
    //private void tablaMinuta(Font myfont3) throws DocumentException {
   
        /*Declaracion de variables para la construcion de la tabla*/
        
       
        PdfPCell celdacodigoProducto;
        PdfPCell celdadescripcionProducto;
        PdfPCell celdacantidad;
        PdfPCell celdapeso;
        
        /*Costruye la tabla*/
        tablaMinuta = new PdfPTable(4);
        tablaMinuta.setSpacingBefore(2);
        tablaMinuta.getDefaultCell().setPadding(5);
        tablaMinuta.setTotalWidth(500);
        tablaMinuta.setTotalWidth(new float[]{70, 250, 70, 70});
        tablaMinuta.setLockedWidth(true);
        
        /*construye las celdas del encabezado*/
        PdfPCell cellCodProd = new PdfPCell(new Phrase("Cod. Prod. ", myfont3));
        cellCodProd.setPadding(5);
        cellCodProd.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellDescripcion = new PdfPCell(new Phrase("Descripcion", myfont3));
        cellDescripcion.setPadding(5);
        cellDescripcion.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad", myfont3));
        cellCantidad.setPadding(5);
        cellCantidad.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellPeso = new PdfPCell(new Phrase("Peso", myfont3));
        cellPeso.setPadding(5);
        cellPeso.setBorder(Rectangle.NO_BORDER);
        //contadorDeFacturas = 0;
        
        
       
            tablaMinuta.addCell(cellCodProd);
            tablaMinuta.addCell(cellDescripcion);
            tablaMinuta.addCell(cellCantidad);
            tablaMinuta.addCell(cellPeso);
        
        /*Asignción de valores  las celdas */
        for (CProductosPorMinuta obj : this.manifiestoActual.getListaDeProductosMinuta()) {
            
            /*Asigna el numero del consecutivo de la factura*/
            celdacodigoProducto = new PdfPCell(new Phrase("" + obj.getCodigoProducto(), myfont));
            celdacodigoProducto.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el numero de la Factura*/
            celdadescripcionProducto = new PdfPCell(new Phrase("" + obj.getDescripcionProducto(), myfont));
            celdadescripcionProducto.setBorder(Rectangle.NO_BORDER);
            
            
            /*Asign el numero del soporte del descuento*/
            celdacantidad = new PdfPCell(new Phrase("" + obj.getCantidad(), myfont));
            celdacantidad.setBorder(Rectangle.NO_BORDER);
            
            /*Asigna el valor del descuento*/
            celdapeso = new PdfPCell(new Phrase("" +obj.getPeso(), myfont));
            celdapeso.setBorder(Rectangle.NO_BORDER);
            /* se le da formato a las celdas del cuerpo del informe */
            
            /*Incoporación de las celdas al documento*/
            tablaMinuta.addCell(celdacodigoProducto);
            tablaMinuta.addCell(celdadescripcionProducto);
            tablaMinuta.addCell(celdacantidad);
            tablaMinuta.addCell(celdapeso);
            
            
            
            
        }
        
        
        prValorRecogidas = new Phrase(new Phrase("Valor de las Recogidas :( " + nf.format(valorRecogidasFacturas) + ") \n", myfont3));
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
