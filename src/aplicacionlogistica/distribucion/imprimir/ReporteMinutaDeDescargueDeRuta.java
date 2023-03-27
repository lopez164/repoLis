/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lopez164
 */
public class ReporteMinutaDeDescargueDeRuta {

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

     boolean conMarcaDeAgua=false;
    public boolean dibujarLinea = true;
    boolean hayDeveoluciones = false;
    boolean hayReenvios = false;
    double valorManifiesto = 0.0;
    double pesoKgManifiesto = 0.0;
    CManifiestosDeDistribucion manifiestoActual;

    Phrase prValorDescuentos;
    Phrase prValorRecogidas;

    String codigoManifiesto;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    NumberFormat formatoNumero = NumberFormat.getNumberInstance();

    // DecimalFormat formatoNumero = new DecimalFormat("#,###.##");
    // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public ReporteMinutaDeDescargueDeRuta(Inicio ini, CManifiestosDeDistribucion manifiestoActual) throws Exception {

        this.manifiestoActual = manifiestoActual;

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
        PdfPTable tablaDatosDelInformeFacturas2;
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
                Logger.getLogger(ReporteMinutaDeDescargueDeRuta.class.getName()).log(Level.SEVERE, null, ex);
            }
            //writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            document.open();

            // event = new HeaderFooter(ini, dibujarLinea, this.codigoManifiesto);
            event = new HeaderFooter(ini, dibujarLinea,manifiestoActual, writer,conMarcaDeAgua); 
            writer.setPageEvent(event);

            codbar = new CodigoDeBarras();

            //document.open();
            PdfContentByte cb = writer.getDirectContent();

            String val = getStringNumeroDemanifiesto(this.manifiestoActual.getNumeroManifiesto());

            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            /**
             * *********************************************************************************************************************************
             */
            Paragraph prTituloInforme = new Paragraph("RELACION DE MERCANCIA DEVUELTA DE RUTA", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);

            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(697);
            tableEncabezadoInforme.setTotalWidth(new float[]{80, 280, 87, 250});

            for (CUsuarios usu : ini.getListaDeUsuarios()) {

                if (usu.getNombreUsuario().equals(Inicio.cifrar(this.manifiestoActual.getUsuarioManifiesto()))) {
                    usuarioQueReporta = usu;
                    break;
                }

            }
            /*
             * ******************************************* Tabla  encabezado del informe  *****************************************************
             */

 /*costruye el encabezado del informe*/
            tablaEncabezadoDeInforme(myfont3, myfont1, manifiestoActual, ini, user, tableEncabezadoInforme);

            /*
             * ******************************************* Tabla cuerpo del informe************************************************************
             */
            tablaDatosDelInformeFacturas = new PdfPTable(5);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{50, 60, 270, 70, 50});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            PdfPCell cellManifiesto = new PdfPCell(new Phrase("N° Mfto ", myfont3));
            cellManifiesto.setPadding(5);
            //cellManifiesto.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellCodigoProducot = new PdfPCell(new Phrase("Cod. Prod.", myfont3));
            cellCodigoProducot.setPadding(5);
            // cellCodigoProducot.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellDescripcionProducto = new PdfPCell(new Phrase("Descripcion de Producto", myfont3));
            cellDescripcionProducto.setPadding(5);
            // cellDescripcionProducto.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellcantidadDevuelta = new PdfPCell(new Phrase("Cant. Dev. ", myfont3));
            cellcantidadDevuelta.setPadding(5);
            //cellcantidadDevuelta.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellverificacion = new PdfPCell(new Phrase("Cantidad ", myfont3));
            cellverificacion.setPadding(5);
            // cellverificacion.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInformeFacturas.addCell(cellManifiesto);
            tablaDatosDelInformeFacturas.addCell(cellCodigoProducot);
            tablaDatosDelInformeFacturas.addCell(cellDescripcionProducto);
            tablaDatosDelInformeFacturas.addCell(cellcantidadDevuelta);
            tablaDatosDelInformeFacturas.addCell(cellverificacion);

            int contadorDeFacturas = 0;

            Connection con = null;
            Statement st = null;
            ResultSet rst = null;

            String sql = "select  fm.numeroManifiesto, pf.codigoProducto,p.descripcionProducto,sum(pfd.cantidadRechazada) as cantidadDevuelta "
                    + "from productosporfacturadescargados pfd "
                    + "join facturaspormanifiesto fm on fm.consecutivo=pfd.consecutivoFacturasDescargadas "
                    + "join productosporfactura pf on pf.consecutivo=pfd.consecutivoProductosPorFactura "
                    + "join productoscamdun p on p.codigoProducto=pf.codigoProducto "
                    + "where fm.numeroManifiesto='" + this.manifiestoActual.getNumeroManifiesto() + "' and fm.activo=1 and pfd.cantidadRechazada > 0 "
                    + "and (pfd.entregado = 0 or pfd.entregado = 4)"
                    + "group by pf.codigoProducto "
                    + "order by pf.codigoProducto asc ;";

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "ReporteMinutaDeDescargueDeRuta");

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    hayDeveoluciones = true;

                    /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
                    //for (CFacturasPorManifiesto obj : this.manifiestoActual.getListaFacturasPorManifiesto()) {
                    // consecutivo
                    PdfPCell celda1 = new PdfPCell(new Phrase("" + rst.getString("numeroManifiesto"), myfont));
                    //celda1.setBorder(Rectangle.NO_BORDER);

                    // NUMERO DE LA FACTURA
                    PdfPCell celda2 = new PdfPCell(new Phrase("" + rst.getString("codigoProducto"), myfont));
                    //celda2.setBorder(Rectangle.NO_BORDER);

                    // CANTIDAD DE SALIDAS A DISTRIBUCION
                    PdfPCell celda3;
                    celda3 = new PdfPCell(new Phrase("" + rst.getString("descripcionProducto"), myfont));

                    //celda3.setBorder(Rectangle.NO_BORDER);
                    PdfPCell celda4 = new PdfPCell(new Phrase("" + rst.getString("cantidadDevuelta"), myfont));
                    //celda4.setBorder(Rectangle.NO_BORDER);

                    PdfPCell celda5 = new PdfPCell(new Phrase(" ", myfont));
                    //celda5.setBorder(Rectangle.NO_BORDER);

                    tablaDatosDelInformeFacturas.addCell(celda1);
                    tablaDatosDelInformeFacturas.addCell(celda2);
                    tablaDatosDelInformeFacturas.addCell(celda3);
                    tablaDatosDelInformeFacturas.addCell(celda4);
                    tablaDatosDelInformeFacturas.addCell(celda5);

                }
            }

            rst.close();
            st.close();
            con.close();

            /**
             * *************************************************************************************************************************************************************
             */
            Paragraph prTituloInforme2 = new Paragraph("RELACION DE MERCANCIA PARA RE ENVIO", myfont5);
            prTituloInforme2.setAlignment(Element.ALIGN_CENTER);

            /*
             * ******************************************* Tabla cuerpo del informe************************************************************
             */
            tablaDatosDelInformeFacturas2 = new PdfPTable(5);
            tablaDatosDelInformeFacturas2.setSpacingBefore(2);
            tablaDatosDelInformeFacturas2.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas2.setTotalWidth(600);
            tablaDatosDelInformeFacturas2.setTotalWidth(new float[]{50, 60, 270, 70, 50});
            tablaDatosDelInformeFacturas2.setLockedWidth(true);

            PdfPCell cellManifiesto2 = new PdfPCell(new Phrase("N° Mfto ", myfont3));
            cellManifiesto.setPadding(5);
            //cellManifiesto.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellCodigoProducot2 = new PdfPCell(new Phrase("Cod. Prod.", myfont3));
            cellCodigoProducot.setPadding(5);
            // cellCodigoProducot.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellDescripcionProducto2 = new PdfPCell(new Phrase("Descripcion de Producto", myfont3));
            cellDescripcionProducto.setPadding(5);
            // cellDescripcionProducto.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellcantidadDevuelta2 = new PdfPCell(new Phrase("Cant. Dev. ", myfont3));
            cellcantidadDevuelta.setPadding(5);
            //cellcantidadDevuelta.setBorder(Rectangle.NO_BORDER);

            PdfPCell cellverificacion2 = new PdfPCell(new Phrase("Cantidad ", myfont3));
            cellverificacion.setPadding(5);
            // cellverificacion.setBorder(Rectangle.NO_BORDER);

            tablaDatosDelInformeFacturas2.addCell(cellManifiesto2);
            tablaDatosDelInformeFacturas2.addCell(cellCodigoProducot2);
            tablaDatosDelInformeFacturas2.addCell(cellDescripcionProducto2);
            tablaDatosDelInformeFacturas2.addCell(cellcantidadDevuelta2);
            tablaDatosDelInformeFacturas2.addCell(cellverificacion2);

            con = null;
            st = null;
            rst = null;

            sql = "select  fm.numeroManifiesto, pf.codigoProducto,p.descripcionProducto,sum(pfd.cantidadRechazada) as cantidadDevuelta "
                    + "from productosporfacturadescargados pfd "
                    + "join facturaspormanifiesto fm on fm.consecutivo=pfd.consecutivoFacturasDescargadas "
                    + "join productosporfactura pf on pf.consecutivo=pfd.consecutivoProductosPorFactura "
                    + "join productoscamdun p on p.codigoProducto=pf.codigoProducto "
                    + "where fm.numeroManifiesto='" + this.manifiestoActual.getNumeroManifiesto() + "' and fm.activo=1 and pfd.cantidadRechazada > 0 "
                    + "and (pfd.entregado = 6 or pfd.entregado = 7)"
                    + "group by pf.codigoProducto "
                    + "order by pf.codigoProducto asc ;";

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "ReporteMinutaDeDescargueDeRuta");

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    hayReenvios = true;

                    /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
                    //for (CFacturasPorManifiesto obj : this.manifiestoActual.getListaFacturasPorManifiesto()) {
                    // consecutivo
                    PdfPCell celda1 = new PdfPCell(new Phrase("" + rst.getString("numeroManifiesto"), myfont));
                    //celda1.setBorder(Rectangle.NO_BORDER);

                    // NUMERO DE LA FACTURA
                    PdfPCell celda2 = new PdfPCell(new Phrase("" + rst.getString("codigoProducto"), myfont));
                    //celda2.setBorder(Rectangle.NO_BORDER);

                    // CANTIDAD DE SALIDAS A DISTRIBUCION
                    PdfPCell celda3;
                    celda3 = new PdfPCell(new Phrase("" + rst.getString("descripcionProducto"), myfont));

                    //celda3.setBorder(Rectangle.NO_BORDER);
                    PdfPCell celda4 = new PdfPCell(new Phrase("" + rst.getString("cantidadDevuelta"), myfont));
                    //celda4.setBorder(Rectangle.NO_BORDER);

                    PdfPCell celda5 = new PdfPCell(new Phrase(" ", myfont));
                    //celda5.setBorder(Rectangle.NO_BORDER);

                    tablaDatosDelInformeFacturas2.addCell(celda1);
                    tablaDatosDelInformeFacturas2.addCell(celda2);
                    tablaDatosDelInformeFacturas2.addCell(celda3);
                    tablaDatosDelInformeFacturas2.addCell(celda4);
                    tablaDatosDelInformeFacturas2.addCell(celda5);

                }
            }

            rst.close();
            st.close();
            con.close();

            /**
             * *************************************************************************************************************************************************************
             */
            tablaDeFirmas = new PdfPTable(3);

            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(600);
            tablaDeFirmas.setTotalWidth(new float[]{200, 200, 200});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe = new Phrase(new Phrase("________________________________\n" + "Entrega : \n" + manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor(), myfont1));
            Phrase lineaEntrega = new Phrase(new Phrase("________________________________\n" + "Recibe : \n" + usuarioQueReporta.getNombres() + " " + usuarioQueReporta.getApellidos(), myfont1));
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
            //document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            if (hayDeveoluciones) {
                /*Se incorpora la tabla de las devoluciones*/
                document.add(tablaDatosDelInformeFacturas);
                document.add(new Phrase("\n"));
            }

            if (hayReenvios) {
                /*Se incorpora la tabla de Re envios*/
                document.add(prTituloInforme2);
                document.add(tablaDatosDelInformeFacturas2);
                document.add(new Phrase("\n"));
            }

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
            Logger.getLogger(ReporteMinutaDeDescargueDeRuta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteMinutaDeDescargueDeRuta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReporteMinutaDeDescargueDeRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que construye la tabla del encabezado del informe de salida a
     * Distribucion
     *
     * @param manifiestoActual1
     *
     */
    private void tablaEncabezadoDeInforme(Font myfont3, Font myfont1, CManifiestosDeDistribucion manifiestoActual1, Inicio ini, CUsuarios user, PdfPTable tableEncabezadoInforme) {
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

        for (CEmpleados auxiliar : manifiestoActual1.getListaDeAuxiliares()) {
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
        cadena = manifiestoActual1.getNombreConductor() + " " + manifiestoActual1.getApellidosConductor();
        if (cadena.length() >= 22) {
            cadena = cadena.substring(0, 22);
        }
        mCellConductor = new PdfPCell(new Phrase(": " + cadena, myfont1));
        mCellConductor.setBorder(Rectangle.NO_BORDER);
        cellRuta = new PdfPCell(new Phrase("Ruta  ", myfont3));
        cellRuta.setBorder(Rectangle.NO_BORDER);
        mCellRuta = new PdfPCell(new Phrase(": " + manifiestoActual1.getNombreDeRuta(), myfont1));
        mCellRuta.setBorder(Rectangle.NO_BORDER);
        cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  ", myfont3));
        cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
        mCellNumeroManifiesto = new PdfPCell(new Phrase(": " + this.manifiestoActual.getNumeroManifiesto(), myfont1));
        mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
        cellCanalDeDistribucion = new PdfPCell(new Phrase("Canal ", myfont3));
        cellCanalDeDistribucion.setBorder(Rectangle.NO_BORDER);
        mCellCanalDeDistribucion = new PdfPCell(new Phrase(": " + manifiestoActual1.getNombreCanal(), myfont1));
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
        mCellPesoKgMfto = new PdfPCell(new Phrase("" + formatoNumero.format(manifiestoActual1.getPesoKgManifiesto()) + " Kg.", myfont1));
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
     * Método que construye la tabla con los descuentos asignadoos a las
     * facturas que se encuentra en el manifiesto actual
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

        /*Asignción de valores  las celdas */
 /*Calcula si en la fila quedan celdas vacias y los acomoda en el informe
        con un colspan uniendo las celdas desocupadas*/
        int residuo = this.manifiestoActual.getListaDeDescuentos().size() % 3;
        residuo = 3 - residuo;
        PdfPCell celda4 = new PdfPCell(new Phrase(""));
        celda4.setBorder(Rectangle.NO_BORDER);
        celda4.setColspan(residuo * 4);

        prValorDescuentos = new Phrase(new Phrase("Valorde los Descuentos :( " + nf.format(valorDescuentoFacturas) + ") \n", myfont3));
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
