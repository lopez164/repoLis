/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.NumberToLetterConverter;
import aplicacionlogistica.distribucion.objetos.CFacturasAnuladas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class ReporteFacturasAnuladas {

    String codigoManifiesto;
    Image imgCodigoDeBarras;
    Document document;
    double valorManifiesto=0.0;
    String rutaDeArchivo;
    PdfWriter writer;
    HeaderFooter event;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    
    // various fonts
        Font myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
        Font myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
        Font myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
        Font myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
        Font myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
        Font myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
        Font myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
        Font myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));
        
        String cadena;
        CodigoDeBarras codbar ;

   
    public ReporteFacturasAnuladas(Inicio ini,  ArrayList<CFacturasAnuladas> listaDeFacturasAnuladas, 
            ArrayList<Vst_Factura> listaDeFacturas,String documento) {
                
        /* Declaración de la variables de las celdas en la parte del encabezado del informe*/
        PdfPTable tableEncabezadoInforme;
        PdfPCell cellNumeroManifiesto;
        PdfPCell cellVacia;
        PdfPCell cellFecha;
        PdfPCell cellUsuario;
        
        PdfPCell mCellNumeroManifiesto;
        PdfPCell mCellVacia;
        PdfPCell mCellFecha;
        PdfPCell mCellUsuario;
        
        PdfPTable tablaDatosDelInformeFacturas;        
        PdfPCell celdaNUmeroConsecutivo ;
        PdfPCell celdaNumeroFactura;
        PdfPCell  celdaCliente = null;
        PdfPCell  celdaObservacion = null;
        PdfPCell  celdaValor = null;
        
        PdfPTable tablaDeFirmas;
        PdfPCell celdaRecibido;
        PdfPCell celdaEntregado;
        
        
        rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/facturasAnuladas_" + documento +  ".pdf";
        try {
         
         
            
            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 40);
              
            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream(rutaDeArchivo ));
           
            } catch (DocumentException ex) {
                 System.out.println("Error al crear  documento " + rutaDeArchivo + "tmp/facturasAnuladas_" + documento +  ".pdf");
                Logger.getLogger(ReporteFacturasAnuladas.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));

            event = new HeaderFooter(ini,true);
            writer.setPageEvent(event);

            
            /* se dibuja el código de barras */
            codbar = new CodigoDeBarras();
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            imgCodigoDeBarras = codbar.getBarCodeImage(getStringNumeroDemanifiesto(documento), cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            
            Paragraph prTituloInforme = new Paragraph("MANIFIESTO DE FACTURAS ANULADAS ", myfont5);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);
            
            tableEncabezadoInforme = new PdfPTable(4);
            tableEncabezadoInforme.setTotalWidth(540);
            tableEncabezadoInforme.setTotalWidth(new float[]{80,200,80,200});
            
       
           
            /*Se signan los valores a las celdas del encabezado de informe en el documento */
      
            cellNumeroManifiesto = new PdfPCell(new Phrase("Manifiesto  :", myfont3));
            cellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
            mCellNumeroManifiesto = new PdfPCell(new Phrase("" + documento, myfont1));
            mCellNumeroManifiesto.setBorder(Rectangle.NO_BORDER);
            
            /* se calcula el valor total de la facturas anuladas */
            Double valor = 0.0;
            for (Vst_Factura obj : listaDeFacturas) {
                valor += obj.getValorTotalFactura();

            }

            /* Se asignan los valores a las celdas respectivas */
            cellVacia = new PdfPCell(new Phrase("Val manfto:",myfont3 ));
            cellVacia.setBorder(Rectangle.NO_BORDER);
            mCellVacia = new PdfPCell(new Phrase(nf.format(valor),myfont1));
            mCellVacia.setBorder(Rectangle.NO_BORDER);
            
            cellFecha = new PdfPCell(new Phrase("Fecha : " , myfont3));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            mCellFecha = new PdfPCell(new Phrase("" + new Date(), myfont1));
            mCellFecha.setBorder(Rectangle.NO_BORDER);
            
            cellUsuario = new PdfPCell(new Phrase("Usuario : ", myfont3));
            cellUsuario.setBorder(Rectangle.NO_BORDER);
            cadena=ini.getUser().getNombres() + " " + ini.getUser().getApellidos();
            if(cadena.length()>=26) cadena=cadena.substring(0, 26);
            mCellUsuario = new PdfPCell(new Phrase(cadena , myfont1));
            mCellUsuario.setBorder(Rectangle.NO_BORDER);


            /* se asignan las celdas a la tabla del encabezado en el  documento*/
             
            tableEncabezadoInforme.addCell(cellNumeroManifiesto);
            tableEncabezadoInforme.addCell(mCellNumeroManifiesto);
           
            tableEncabezadoInforme.addCell(cellVacia);
            tableEncabezadoInforme.addCell(mCellVacia);
            
            tableEncabezadoInforme.addCell(cellFecha);
            tableEncabezadoInforme.addCell(mCellFecha);
            
            tableEncabezadoInforme.addCell(cellUsuario);
            tableEncabezadoInforme.addCell(mCellUsuario);
            
            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);
                       
            /* Se instancia la tabla del informe de las facturas */
            tablaDatosDelInformeFacturas = new PdfPTable(5);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            tablaDatosDelInformeFacturas.setTotalWidth(600);
            tablaDatosDelInformeFacturas.setTotalWidth(new float[]{25, 60, 215,200, 90});
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            /* Columna 1 N°. */
            PdfPCell cellNo = new PdfPCell(new Phrase("N° ", myfont3));
            cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);
            
            /* Columna 2 Fct. N°. */
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact. N° ", myfont3));
            cellFact.setPadding(5);
            cellFact.setBorder(Rectangle.NO_BORDER);
            
             /* Columna 3 Cliente */
            PdfPCell cellCliente = new PdfPCell(new Phrase("Cliente ", myfont3));
            cellCliente.setPadding(5);
            cellCliente.setBorder(Rectangle.NO_BORDER);

              /* Columna 4 Observasciones */
            PdfPCell cellObservaciones = new PdfPCell(new Phrase("Obs. ", myfont3));
            cellObservaciones.setPadding(5);
            cellObservaciones.setBorder(Rectangle.NO_BORDER);

              /* Columna 5 valor factura */
            PdfPCell cellValor = new PdfPCell(new Phrase("Valor ", myfont3));
            cellValor.setPadding(5);
            cellValor.setBorder(Rectangle.NO_BORDER);

            
            tablaDatosDelInformeFacturas.addCell(cellNo);
            tablaDatosDelInformeFacturas.addCell(cellFact);
            tablaDatosDelInformeFacturas.addCell(cellCliente);
            tablaDatosDelInformeFacturas.addCell(cellObservaciones);
            tablaDatosDelInformeFacturas.addCell(cellValor);


            int contadorDeFacturas = 0;
           String cadena2;

            /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
            for (CFacturasAnuladas obj : listaDeFacturasAnuladas) {

                // ADHERENCIA
                celdaNUmeroConsecutivo = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont1));
                celdaNUmeroConsecutivo.setBorder(Rectangle.NO_BORDER);

                // NUMERO DE LA FACTURA
                celdaNumeroFactura = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont1));
                celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                for (Vst_Factura fac : listaDeFacturas) {
                    if (obj.getNumeroFactura().equals(fac.getNumeroFactura())) {
                 // NOMBRE DEL CLIENTE
                        cadena2 = fac.getNombreDeCliente();
                        if (cadena2.length() >= 26) {
                            cadena2 = cadena2.substring(0, 26);
                        }
                        celdaCliente = new PdfPCell(new Phrase("" + cadena2, myfont1));
                        celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                 // OBSERVACION
                        cadena2 = obj.getObservacion();
                        if (cadena2.length() >= 26) {
                            cadena2 = cadena2.substring(0, 26);
                        }
                        celdaObservacion = new PdfPCell(new Phrase("" + cadena2, myfont1));
                        celdaNumeroFactura.setBorder(Rectangle.NO_BORDER);

                 // VALOR DE LA FACTURA              
                        celdaValor = new PdfPCell(new Phrase("" + nf.format(fac.getValorTotalFactura()), myfont1));
                        celdaValor.setBorder(Rectangle.NO_BORDER);

                        valorManifiesto += fac.getValorTotalFactura();
                        break;
                    }
                }
  
                tablaDatosDelInformeFacturas.addCell(celdaNUmeroConsecutivo);
                tablaDatosDelInformeFacturas.addCell(celdaNumeroFactura);
                tablaDatosDelInformeFacturas.addCell(celdaCliente);
                tablaDatosDelInformeFacturas.addCell(celdaObservacion);
                tablaDatosDelInformeFacturas.addCell(celdaValor);
                
                contadorDeFacturas++;
              
            }
            
            String valor2;
            valor2 = NumberToLetterConverter.convertNumberToLetter(valorManifiesto);

            Paragraph prValorFacturas = new Paragraph(new Phrase("Valor total anulado   :( " + nf.format(valorManifiesto) + ") \n" + valor2 + "\n\n\n\n", myfont4));
            
            
            tablaDeFirmas = new PdfPTable(2);
            tablaDeFirmas.setSpacingBefore(2);
            tablaDeFirmas.getDefaultCell().setPadding(5);
            tablaDeFirmas.setTotalWidth(500);
            tablaDeFirmas.setTotalWidth(new float[]{250, 250});
            tablaDeFirmas.setLockedWidth(true);

            Phrase lineaRecibe =  new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n", myfont3));
            Phrase lineaEntrega = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont3));
            
             celdaRecibido = new PdfPCell(lineaRecibe);
             celdaRecibido.setBorder(Rectangle.NO_BORDER);
              
             celdaEntregado = new PdfPCell(lineaEntrega);
             celdaEntregado.setBorder(Rectangle.NO_BORDER);
                        
            tablaDeFirmas.addCell(celdaRecibido);
            tablaDeFirmas.addCell(celdaEntregado);
            
 /***********************************************Se arma el documento**************************************************/
           
            document.add(tableEncabezadoInforme);
            document.add(prTituloInforme);
            document.add(imgCodigoDeBarras);
            
           
            /*Se incorpora la tabla de las facturas anuladas al documento*/
            document.add(tablaDatosDelInformeFacturas);
            document.add(new Phrase("\n"));
            document.add(prValorFacturas);

            /*Se incorpora la tabla de las firma al documento*/
            document.add(tablaDeFirmas);
            document.close();

         /* Se abre el documento creado*/
            File path = new File(rutaDeArchivo);
            Desktop.getDesktop().open(path);
            

        } catch (FileNotFoundException ex) {
              System.out.println("Error  no se encontró el archivo ReporteFacturasAnuladas" );
            Logger.getLogger(ReporteFacturasAnuladas.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (DocumentException | IOException ex) {
              System.out.println("Error en la creación del documento ReporteFacturasAnuladas");
            Logger.getLogger(ReporteFacturasAnuladas.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    /**
     * Método que devuelve una cadena con un código que representa el número de mnifiesto
     * y agrega tantos ceros como sea necesario hasta completar una longitud de
     * seis (6) caracteres
     *
     * @return una cadena con 4l código del manifiesto
     */
    private String getStringNumeroDemanifiesto(String  documento) {
        String value = "";
        value = "" + documento;

        switch (value.length()) {
            case 1:
                value = "00000" + documento;
                break;
            case 2:
                value = "0000" + documento;
                break;
            case 3:
                value = "000" + documento;
                break;
            case 4:
                value = "00" + documento;
                break;
            case 5:
                value = "0" + documento;
                break;
            default:
                value = "" + documento;
                break;
        }

        return value;
    }
}
