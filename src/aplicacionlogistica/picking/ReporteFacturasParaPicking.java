/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.picking;

import aplicacionlogistica.distribucion.imprimir.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.imprimir.HeaderFooter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class ReporteFacturasParaPicking {

    String codigoManifiesto;
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini=null;
    ArrayList<String> listaDeFacturasPorManifiesto=null;
    String numeroManifiesto=null;
    String destino=null;
    String destinatario=null;
    String ruTaArchivo=null;
   

   // public ReporteFacturasEnDistribucion(Inicio ini, ArrayList<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public ReporteFacturasParaPicking(Inicio ini, ArrayList<String> listaDeFacturasPorManifiesto, String numeroManifiesto,String destino, String destinatario) {
       
        this.ini = ini;
        this.listaDeFacturasPorManifiesto = listaDeFacturasPorManifiesto;
        this.numeroManifiesto = numeroManifiesto;
        this.destino = destino;
        this.destinatario = destinatario;
        try {
         
         
            
            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 5, 30);
             /* aca se dibuja el codigo de barras */
           // String val = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());
          
           String val = getStringNumeroDemanifiesto(numeroManifiesto);
            //facturasPorManifiesto=manifiesto.getListaFacturasPorManifiesto();
            
            ruTaArchivo="manifiestos/picking_" + val + ".pdf";
            
            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream(this.ruTaArchivo));
            } catch (DocumentException ex) {
                Logger.getLogger(ReporteFacturasParaPicking.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

          //  event = new HeaderFooter(ini,true);
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
           
           

            /*Se dibuja el codigo de barras y se ubica en el documento */
            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph par = new Paragraph("\n\n\n\n");
            Paragraph par1 = new Paragraph("\n\n " + "MANIFIESTO DE FACTURAS PARA PROCESO DE PICKING", myfont5);
            par1.setAlignment(Element.ALIGN_CENTER);
            PdfPTable table1 = new PdfPTable(2);
           // table1.setTotalWidth(500);
            table1.setTotalWidth(new float[]{250, 250});

            PdfPCell cell1 = new PdfPCell(new Phrase("Destino :  " + this.destino, myfont4));
            PdfPCell cell2 = new PdfPCell(new Phrase(" ", myfont4));
          
            CEmpleados conductor = null ;
            CEmpleados aux = null ;
            
            for(CEmpleados obj : ini.getListaDeEmpleados()){
//                if(obj.getCedula().equals(manifiesto.getConductor())){
//                    conductor = new CEmpleados(ini);
//                    conductor=obj;
//                    break;
//                }
                
            } 
              PdfPCell cell3 = null ;
            if(this.destinatario != null){
                  cell3 = new PdfPCell(new Phrase("Destinatario :  " + this.destinatario, myfont4));
            }
                    
           
           PdfPCell  cell4 = new PdfPCell(new Phrase(" ",myfont4));
            
            // LLAMA AL OBJETO RUTAS DE DISTRIBUCION
            CRutasDeDistribucion ruta = new CRutasDeDistribucion(ini);
//            for(CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()){
//                if(obj.getIdRutasDeDistribucion()== manifiesto.getIdRuta()){
//                    ruta=obj;
//                    break;
//                }
//            }
            PdfPCell cell5 = new PdfPCell(new Phrase("Manifiesto :  " + val , myfont4));
            PdfPCell cell6 = new PdfPCell(new Phrase(" ", myfont4));
            
            // LLAMA AL OBJETO CANALES DE VENTA
//          CCanales canal = new CCanales(ini);
//            for(CCanales obj : ini.getListaDeCanalesDeVenta()){
//                if(obj.getIdCanal()== manifiesto.getIdCanal()){
//                    canal=obj;
//                    break;
//                }
//            }            
            PdfPCell cell7 = new PdfPCell(new Phrase("Canal :  " , myfont4));
            PdfPCell cell8 = new PdfPCell(new Phrase(" "));
            
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd '-' hh:mm aaa");

            PdfPCell cell9 = new PdfPCell(new Phrase("Fecha : "+ dNow , myfont4));
            PdfPCell cell10 = new PdfPCell(new Phrase("Usuario : " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont4)); // + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont4));

            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell4.setBorder(Rectangle.NO_BORDER);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell7.setBorder(Rectangle.NO_BORDER);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell9.setBorder(Rectangle.NO_BORDER);
            cell10.setBorder(Rectangle.NO_BORDER);
            

            table1.addCell(cell1);
            table1.addCell(cell2);
            table1.addCell(cell3);
            table1.addCell(cell4);
            table1.addCell(cell5);
            table1.addCell(cell6);
            //table1.addCell(cell7);
            //table1.addCell(cell8);
            table1.addCell(cell9);
            table1.addCell(cell10);

            table1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            document.add(new Paragraph("\n\n\n\n\n\n\n"));
            document.add(par1);
            document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            // demonstrate some table features
            PdfPTable table = new PdfPTable(12);
            table.setSpacingBefore(2);
            table.getDefaultCell().setPadding(5);
            table.setTotalWidth(600);
            table.setTotalWidth(new float[]{25, 70, 25, 70, 25, 70, 25, 70, 25, 70,25, 70,});
            table.setLockedWidth(true);

            PdfPCell cellNo = new PdfPCell(new Phrase("N° ",myfont5));
            cellNo.setPadding(5);
            cellNo.setBorder(Rectangle.NO_BORDER);
            cellNo.setBorderColor(new BaseColor(255, 255, 255));
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact. N° ",myfont5));
            cellFact.setPadding(5);
           cellFact.setBorder(Rectangle.NO_BORDER);
            /* EStos son los encabezados de las columnas del informe*/
            table.addCell(cellNo);
            table.addCell(cellFact);
           
            table.addCell(cellNo);
            table.addCell(cellFact);
            
            table.addCell(cellNo);
            table.addCell(cellFact);
           
            table.addCell(cellNo);
            table.addCell(cellFact);
           
            table.addCell(cellNo);
            table.addCell(cellFact);
            
            table.addCell(cellNo);
            table.addCell(cellFact);
           

            int contadorDeFacturas = 0;
           // double valorManifiesto=0.0;

          /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
               for (String obj : this.listaDeFacturasPorManifiesto) {

                // ADHERENCIA
                PdfPCell celda1 = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont1));
                celda1.setBorder(Rectangle.NO_BORDER);

                // NUMERO DE LA FACTURA
                PdfPCell celda2 = new PdfPCell(new Phrase("" + obj, myfont1));
                celda2.setBorder(Rectangle.NO_BORDER);

                table.addCell(celda1);
                table.addCell(celda2);

                contadorDeFacturas++;
                //   valorManifiesto+=obj.getValorARecaudarFactura();

                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <2 && >=1
                if (contadorDeFacturas == listaDeFacturasPorManifiesto.size()) {

                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
                    int residuo = contadorDeFacturas % 12;
                    residuo = 12 - residuo;
                    PdfPCell celda3 = new PdfPCell(new Phrase(""));
                    celda3.setBorderColor(new BaseColor(255, 255, 255));
                    celda3.setColspan(residuo * 12);
                    table.addCell(celda3);
                }

            }
            
            //String valor;
            //valor = NumberToLetterConverter.convertNumberToLetter(valorManifiesto);

           // Paragraph valorTotal = new Paragraph(new Phrase("Valor total a recaudar  :( " + nf.format(valorManifiesto) + ") \n" + valor + "\n\n\n\n", myfont4));
            PdfPTable table3 = new PdfPTable(2);
            table3.setTotalWidth(new float[]{250, 250});

            Phrase ddestinatario = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + this.destinatario, myfont4));
            Phrase usuario = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + ini.getUser().getNombres() + " "
                    + ini.getUser().getApellidos(), myfont4));
            
            PdfPCell cell11 = new PdfPCell(ddestinatario);
            PdfPCell cell12 = new PdfPCell(usuario);
            
            cell10.setBorder(Rectangle.NO_BORDER);
            cell11.setBorder(Rectangle.NO_BORDER);
            cell12.setBorder(Rectangle.NO_BORDER);
               
//            cell10.setBorderColor(new BaseColor(255, 255, 255));
//            cell11.setBorderColor(new BaseColor(255, 255, 255));
//            cell12.setBorderColor(new BaseColor(255, 255, 255));
//            
            table3.addCell(cell11);
            table3.addCell(cell12);

            document.add(table);
            document.add(new Phrase("\n"));
           // document.add(valorTotal);
            document.add(table3);

            document.close();

            File path = new File(this.ruTaArchivo);
            Desktop.getDesktop().open(path);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteFacturasParaPicking.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteFacturasParaPicking.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (Exception ex) {
            Logger.getLogger(ReporteFacturasParaPicking.class.getName()).log(Level.SEVERE, null, ex);
            
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
