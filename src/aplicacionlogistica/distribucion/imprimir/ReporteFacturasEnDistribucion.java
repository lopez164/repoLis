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
public class ReporteFacturasEnDistribucion {

    String codigoManifiesto;
    Image imgCodigoDeBarras;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
   

   // public ReporteFacturasEnDistribucion(Inicio ini, List<CFacturasPorManifiesto> arrFactPorMfto, CManifiestosDeDistribucion manifiesto, double valoraRecaudar, String codigoManifiesto) {
    public ReporteFacturasEnDistribucion(Inicio ini,  CManifiestosDeDistribucion manifiesto) {
       
        List<CFacturasPorManifiesto> listaFacturasPorManifiesto= new  ArrayList();
        try {
         
         
            
            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 5, 30);
            
            listaFacturasPorManifiesto=manifiesto.getListaFacturasPorManifiesto();
            
            
            System.out.println("Final del documento " + new File(".").getAbsolutePath());
            try {
                // creation of the different writers
                writer = PdfWriter.getInstance(document, new FileOutputStream("manifiestos/" + this.codigoManifiesto + ".pdf"));
            } catch (DocumentException ex) {
                Logger.getLogger(ReporteFacturasEnDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
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
           
            /* aca se dibuja el codigo de barras */
            String val = getStringNumeroDemanifiesto(manifiesto.getNumeroManifiesto());
            imgCodigoDeBarras = codbar.getBarCodeImage(val, cb);
            imgCodigoDeBarras.setAbsolutePosition(530, 640);

            Paragraph par = new Paragraph("\n\n\n\n\n\n");
            Paragraph par1 = new Paragraph("\n\n " + "MANIFIESTO DE REPARTO PARA SALIR A DISTRIBUCION", myfont5);
            par1.setAlignment(Element.ALIGN_CENTER);
            PdfPTable table1 = new PdfPTable(2);
            table1.setTotalWidth(500);
            table1.setTotalWidth(new float[]{200, 250});

            PdfPCell cell1 = new PdfPCell(new Phrase("Placa N° " + manifiesto.getVehiculo(), myfont4));
            PdfPCell cell2 = new PdfPCell(new Phrase("Kilometraje " + manifiesto.getKmSalida(), myfont4));
          
            CEmpleados conductor = null ;
            CEmpleados aux = null ;
            
            for(CEmpleados obj : ini.getListaDeEmpleados()){
                if(obj.getCedula().equals(manifiesto.getConductor())){
                    conductor = new CEmpleados(ini);
                    conductor=obj;
                    break;
                }
                
            } 
              PdfPCell cell3 = null ;
            if(conductor != null){
                  cell3 = new PdfPCell(new Phrase("Conductor  " + conductor.getNombres() + " " + conductor.getApellidos(), myfont4));
            }
           
            PdfPCell cell4;
            
            
//            if (manifiesto.getAuxiliarDeReparto1().equals("0")) {
//                cell4 = new PdfPCell(new Phrase("Auxiliar  : Sin auxiliar asignado", myfont4));
//            } else {
//              
//                for(CEmpleados obj : ini.getListaDeEmpleados()){
//                if(obj.getCedula().equals(manifiesto.getAuxiliarDeReparto1())){
//                    aux = new CEmpleados(ini);
//                    aux=obj;
//                    break;
//                }
//                
//            }
//                cell4 = new PdfPCell(new Phrase("Auxiliar :  " + aux.getNombres() + " " + aux.getApellidos(), myfont4));
//            }
            
            // LLAMA AL OBJETO RUTAS DE DISTRIBUCION
            CRutasDeDistribucion ruta = new CRutasDeDistribucion(ini);
            for(CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()){
                if(obj.getIdRutasDeDistribucion()== manifiesto.getIdRuta()){
                    ruta=obj;
                    break;
                }
            }
            PdfPCell cell5 = new PdfPCell(new Phrase("Ruta  " + ruta.getNombreRutasDeDistribucion(), myfont4));
            PdfPCell cell6 = new PdfPCell(new Phrase("Manifiesto :  " + val, myfont4));
            
            // LLAMA AL OBJETO CANALES DE VENTA
            CCanalesDeVenta canal = new CCanalesDeVenta(ini);
            for(CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()){
                if(obj.getIdCanalDeVenta()== manifiesto.getIdCanal()){
                    canal=obj;
                    break;
                }
            }            
            PdfPCell cell7 = new PdfPCell(new Phrase("Canal :  " + canal.getNombreCanalDeVenta(), myfont4));
            PdfPCell cell8 = new PdfPCell(new Phrase(" "));
            
           // Date dNow = new Date();
            //SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd '-' hh:mm aaa");

            PdfPCell cell9 = new PdfPCell(new Phrase("Fecha : " + manifiesto.getHoraDeDespacho(), myfont4));
            PdfPCell cell10 = new PdfPCell(new Phrase("Usuario : ", myfont4)); // + ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont4));

            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell3.setBorder(Rectangle.NO_BORDER);
//            cell4.setBorder(Rectangle.NO_BORDER);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell6.setBorder(Rectangle.NO_BORDER);
            cell7.setBorder(Rectangle.NO_BORDER);
            cell8.setBorder(Rectangle.NO_BORDER);
            cell9.setBorder(Rectangle.NO_BORDER);
            cell10.setBorder(Rectangle.NO_BORDER);

            table1.addCell(cell1);
            table1.addCell(cell2);
            table1.addCell(cell3);
//            table1.addCell(cell4);
            table1.addCell(cell5);
            table1.addCell(cell6);
            table1.addCell(cell7);
            table1.addCell(cell8);
            table1.addCell(cell9);
            table1.addCell(cell10);

            table1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
            document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            document.add(par1);
            document.add(new Paragraph("\n"));
            document.add(imgCodigoDeBarras);

            // demonstrate some table features
            PdfPTable table = new PdfPTable(9);
            table.setSpacingBefore(2);
            table.getDefaultCell().setPadding(5);
            table.setTotalWidth(600);
            table.setTotalWidth(new float[]{25, 60, 90, 25, 60, 90, 25, 60, 90});
            table.setLockedWidth(true);

            PdfPCell cellNo = new PdfPCell(new Phrase("N° "));
            cellNo.setPadding(5);
            cellNo.setBorderColor(new BaseColor(255, 255, 255));
            PdfPCell cellFact = new PdfPCell(new Phrase("Fact. N° "));
            cellFact.setPadding(5);
            cellFact.setBorderColor(new BaseColor(255, 255, 255));
            PdfPCell cellVal = new PdfPCell(new Phrase("$$ "));
            cellVal.setPadding(5);
            cellVal.setBorderColor(new BaseColor(255, 255, 255));

            table.addCell(cellNo);
            table.addCell(cellFact);
            table.addCell(cellVal);

            table.addCell(cellNo);
            table.addCell(cellFact);
            table.addCell(cellVal);

            table.addCell(cellNo);
            table.addCell(cellFact);
            table.addCell(cellVal);

            int contadorDeFacturas = 0;
            double valorManifiesto=0.0;

          /* se recorre en un bucle las facturas asignadas al manifiesto de distribucion*/
               for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {
             
                // ADHERENCIA
                 PdfPCell celda1 = new PdfPCell(new Phrase("" + (contadorDeFacturas + 1) + ".", myfont1));
                  celda1.setBorder(Rectangle.NO_BORDER);

                // NUMERO DE LA FACTURA
                PdfPCell celda2 = new PdfPCell(new Phrase("" + obj.getNumeroFactura(), myfont1));
              celda2.setBorder(Rectangle.NO_BORDER);
              
               // VALOR A RECAUDAR DE LA FACTURA              
                 PdfPCell  celda3 = new PdfPCell(new Phrase("" + nf.format(obj.getValorARecaudarFactura()), myfont1));
                 celda3.setBorder(Rectangle.NO_BORDER);
                 
  
                table.addCell(celda1);
                table.addCell(celda2);
                table.addCell(celda3);
                
                contadorDeFacturas++;
                valorManifiesto+=obj.getValorARecaudarFactura();
                
                // AJUSTA LA ULTIMA FILA DEL INFORME CUANDO LOS ITEM SON <3 && >=1
                if (contadorDeFacturas == listaFacturasPorManifiesto.size()) {
                    
                    // SE DECLARA VARIABLE ENTERA PARA SABER CUANTAS FACTURA QUEDAN Y AJUSTARLAS 
                    int residuo = contadorDeFacturas % 3;
                    residuo = 3 - residuo;
                    PdfPCell celda4 = new PdfPCell(new Phrase(""));
                    celda4.setBorderColor(new BaseColor(255, 255, 255));
                    celda4.setColspan(residuo * 3);
                    table.addCell(celda4);
                }
              
            }
            
            String valor;
            valor = NumberToLetterConverter.convertNumberToLetter(valorManifiesto);

                    Paragraph valorTotal = new Paragraph(new Phrase("Valor total a recaudar  :( " + nf.format(valorManifiesto) + ") \n" + valor + "\n\n\n\n", myfont4));
            PdfPTable table3 = new PdfPTable(2);

            Phrase conducto = new Phrase(new Phrase("_____________________________________\n" + "Recibe : \n" + conductor.getNombres() + " " + conductor.getApellidos(), myfont4));
            Phrase usuario = new Phrase(new Phrase("_____________________________________\n" + "Entrega : \n" + ini.getUser().getNombres() + " "
                    + ini.getUser().getApellidos(), myfont4));
            PdfPCell cell11 = new PdfPCell(conducto);
            PdfPCell cell12 = new PdfPCell(usuario);
            
            cell10.setBorderColor(new BaseColor(255, 255, 255));
            cell11.setBorderColor(new BaseColor(255, 255, 255));
            cell12.setBorderColor(new BaseColor(255, 255, 255));
            table3.addCell(cell11);
            table3.addCell(cell12);

            document.add(table);
            document.add(new Phrase("\n"));
            document.add(valorTotal);
            document.add(table3);

            document.close();

            File path = new File("manifiestos/" + this.codigoManifiesto + ".pdf");
            Desktop.getDesktop().open(path);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (Exception ex) {
            Logger.getLogger(ReporteFacturasEnDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            
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
