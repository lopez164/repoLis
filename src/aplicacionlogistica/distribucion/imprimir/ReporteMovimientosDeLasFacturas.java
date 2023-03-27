/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.awt.HeadlessException;
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
import javax.swing.JOptionPane;

/**
 * Este objeto
 *
 * @author lopez164
 */
public class ReporteMovimientosDeLasFacturas {

   
    String rutaDeArchivo;
    Document document;
    PdfWriter writer;
    HeaderFooter event;
    float[] medidaTabla ;
     
    ArrayList<Vst_FacturasDescargadas> listaDeFacturasRechazadas;
    
    
     PdfPTable tableEncabezadoInforme;
        PdfPTable tablaDatosDelInformeFacturas;
       
         /* Declaración de la variables de las celdas en la parte del informe*/
        PdfPCell cellFactura;
        PdfPCell cellCliente;
        PdfPCell cellVendedor;
        PdfPCell cellConductor;
        PdfPCell cellPlaca;
        PdfPCell cellValor;
        PdfPCell cellCusalDeRechazo;
       

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

         Paragraph responsableTabla;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    //ArrayList<CFacturasDescargadas> arrFactPorMftoDescargadas;

    public ReporteMovimientosDeLasFacturas(Inicio ini, ArrayList<Vst_FacturasDescargadas> listaDeFacturasRechazadas, int orderBy,int tipoMovimiento,Date fechaIni, Date fechaFin) {

        this.listaDeFacturasRechazadas=listaDeFacturasRechazadas;
       rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
       medidaTabla= new float[]{65, 150, 150, 150};
        try {

            // creation of the document with a certain size and certain margins
            // may want to use PageSize.LETTER instead
            document = new Document(PageSize.LETTER, 40, 20, 95, 40);

            // creation of the different writers
            writer = PdfWriter.getInstance(document, new FileOutputStream(rutaDeArchivo + "tmp/" + "ReporteRechazosTotales" + ".pdf"));
            writer.setBoxSize("art", new Rectangle(40, 20, 559, 788));
            

            event = new HeaderFooter(ini,false);
            writer.setPageEvent(event);

            document.open();

            PdfContentByte cb = writer.getDirectContent();
            Paragraph prTituloInforme = null;
            switch(tipoMovimiento){
                case 1:
                     prTituloInforme = new Paragraph("NO HAY MOVIMIENTO 1 ", myfont5);
                    break;
                case 2:
                     prTituloInforme = new Paragraph("INFORME ENTREGAS TOTALES ", myfont5);
                    break;
                case 3:
                     prTituloInforme = new Paragraph("INFORME RECHAZOS TOTALES ", myfont5);
                    break;
                case 4:
                     prTituloInforme = new Paragraph("INFORME RECHAZOS PARCIALES ", myfont5);
                    break;
                case 5:
                     prTituloInforme = new Paragraph("INFORME ENTREGAS PARCIALES ", myfont5);
                    break;
                
            }

           
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);
            document.add(prTituloInforme);
            
            
            prTituloInforme = new Paragraph("Desde : " + fechaIni + "    Hasta : " + fechaFin, myfont1);
            prTituloInforme.setAlignment(Element.ALIGN_CENTER);
            document.add(prTituloInforme);
            
            /*Se signan los valores a las celdas del encabezado de informe en el documento */
                 
//            PdfPCell cellUsuario = new PdfPCell(new Phrase("Usuario : ", myfont3));
//            cellUsuario.setBorder(Rectangle.NO_BORDER);
//            PdfPCell mCellUsuario = new PdfPCell(new Phrase(ini.getUser().getNombres() + " " + ini.getUser().getApellidos(), myfont1));
//            mCellUsuario.setBorder(Rectangle.NO_BORDER);
//
//            /* se asignan las celdas a la tabla del encabezado en el  documento*/
//            tableEncabezadoInforme= new PdfPTable(2);
//            tableEncabezadoInforme.setSpacingBefore(2);
//            tableEncabezadoInforme.getDefaultCell().setPadding(5);
//            tableEncabezadoInforme.setTotalWidth(600);
//            tableEncabezadoInforme.setTotalWidth(new float[]{300,300});
//            tableEncabezadoInforme.setLockedWidth(true);
//            
//            
//            tableEncabezadoInforme.addCell(cellUsuario);
//            tableEncabezadoInforme.addCell(mCellUsuario);
//            
//                       
//            tableEncabezadoInforme.setHorizontalAlignment(Element.ALIGN_LEFT);
//            tableEncabezadoInforme.writeSelectedRows(0, -1, 60f, 690f, writer.getDirectContent());
//            
            //document.add(tableEncabezadoInforme);

            /* Se instancia la tabla del informe de las facturas */
            tablaDatosDelInformeFacturas = new PdfPTable(4);
            tablaDatosDelInformeFacturas.setSpacingBefore(2);
            tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
            
            tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
            tablaDatosDelInformeFacturas.setLockedWidth(true);

            
            /**
             * **************************se inicia el proceso en la parte del informe de las facturas********************************
             */
      
            switch(orderBy){
                case 1: // ordenado por Cliente
                    break;
                case 2: // ordenado por vendedor
                    imprimirPorVendedor();
                    break;
                case 3: // ordenado por Conductor
                    imprimirPorConductor();
                    break;
                case 4:// ordenado por causal de Rechazo
                    imprimirPorCausalDeRechazo();
                    break;
                    
                    case 5:// ordenado por causal de Rechazo
                    imprimirPorFechaDeDistribucion();
                    break;
                
            }

      
            /*Se cierra el documento documento*/
            document.close();

            File path = new File(rutaDeArchivo  + "tmp/" + "ReporteRechazosTotales" + ".pdf");

            try {

                Desktop.getDesktop().open(path);

            } catch (IOException ex) {
                Logger.getLogger(ReporteMovimientosDeLasFacturas.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error 1 catch  " + ex, "Error", 0);
            }

        } catch (FileNotFoundException | DocumentException | HeadlessException ex) {
            Logger.getLogger(ReporteMovimientosDeLasFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    private void imprimirPorCliente(){
        
    }
    private void imprimirPorVendedor() throws DocumentException {
      
        String vendedorAux = "";
        for (Vst_FacturasDescargadas obj : this.listaDeFacturasRechazadas) {

            if (vendedorAux.equals("")) {
                vendedorAux = obj.getVendedor();
                
                tablaDatosDelInformeFacturas = new PdfPTable(4);
                tablaDatosDelInformeFacturas.setSpacingBefore(2);
                tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                
                tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                tablaDatosDelInformeFacturas.setLockedWidth(true);

                responsableTabla = new Paragraph("\nVendedor : " + obj.getVendedor(), myfont);
                
                insertarEncabezadoTablaVendedor();
                insertarFilaVendedor(obj);

            } else {

                if (vendedorAux.equals(obj.getVendedor())) {
                    insertarFilaVendedor(obj);

                } else {
                   
                    
                    document.add(responsableTabla);
                    document.add(tablaDatosDelInformeFacturas);
                    

                    tablaDatosDelInformeFacturas = new PdfPTable(4);
                    tablaDatosDelInformeFacturas.setSpacingBefore(2);
                    tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                    
                    tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                    tablaDatosDelInformeFacturas.setLockedWidth(true);
                    
                    
                    vendedorAux = obj.getVendedor();

                    responsableTabla = new Paragraph("\nVendedor : " + vendedorAux, myfont);

                   
                    insertarEncabezadoTablaVendedor();
                    insertarFilaVendedor(obj);
                    
                }
                 
            }

        }
        document.add(responsableTabla);
        document.add(tablaDatosDelInformeFacturas);

    }
    private void imprimirPorConductor() throws DocumentException{
       String conductorAux = "";
        for (Vst_FacturasDescargadas obj : this.listaDeFacturasRechazadas) {

            if (conductorAux.equals("")) {
                conductorAux = obj.getNombreConductor();
                
                tablaDatosDelInformeFacturas = new PdfPTable(4);
                tablaDatosDelInformeFacturas.setSpacingBefore(2);
                tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                
                tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                tablaDatosDelInformeFacturas.setLockedWidth(true);

                responsableTabla = new Paragraph("\nConductor : " + obj.getNombreConductor(), myfont);
                
                insertarEncabezadoTablaConductor();
                insertarFilaConductor(obj);

            } else {

                if (conductorAux.equals(obj.getNombreConductor())) {
                    insertarFilaConductor(obj);

                } else {
                   
                    
                    document.add(responsableTabla);
                    document.add(tablaDatosDelInformeFacturas);
                    

                    tablaDatosDelInformeFacturas = new PdfPTable(4);
                    tablaDatosDelInformeFacturas.setSpacingBefore(2);
                    tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                    
                    tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                    tablaDatosDelInformeFacturas.setLockedWidth(true);
                    
                    
                    conductorAux = obj.getNombreConductor();

                    responsableTabla = new Paragraph("\nConductor : " + conductorAux, myfont);

                   
                    insertarEncabezadoTablaConductor();
                    insertarFilaConductor(obj);
                    
                }
                 
            }

        }
        document.add(responsableTabla);
        document.add(tablaDatosDelInformeFacturas);  
    }
    private void imprimirPorCausalDeRechazo() throws DocumentException{
        String causalRechazoAux = "";
        for (Vst_FacturasDescargadas obj : this.listaDeFacturasRechazadas) {

            if (causalRechazoAux.equals("")) {
                causalRechazoAux = obj.getNombreCausalDeRechazo();
                
                tablaDatosDelInformeFacturas = new PdfPTable(4);
                tablaDatosDelInformeFacturas.setSpacingBefore(2);
                tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                
                tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                tablaDatosDelInformeFacturas.setLockedWidth(true);

                responsableTabla = new Paragraph("\n Causal de Rechazo : " + obj.getNombreCausalDeRechazo(), myfont);
                
                insertarEncabezadoTablaCausalDeRechazo();
                insertarFilaCausalDeRechazo(obj);

            } else {

                if (causalRechazoAux.equals(obj.getNombreCausalDeRechazo())) {
                    insertarFilaCausalDeRechazo(obj);

                } else {
                   
                    
                    document.add(responsableTabla);
                    document.add(tablaDatosDelInformeFacturas);
                    

                    tablaDatosDelInformeFacturas = new PdfPTable(4);
                    tablaDatosDelInformeFacturas.setSpacingBefore(2);
                    tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                    
                    tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                    tablaDatosDelInformeFacturas.setLockedWidth(true);
                    
                    
                    causalRechazoAux = obj.getNombreCausalDeRechazo();

                    responsableTabla = new Paragraph("\n Causal de Rechazo : " + causalRechazoAux, myfont);

                   
                    insertarEncabezadoTablaCausalDeRechazo();
                    insertarFilaCausalDeRechazo(obj);
                    
                }
                 
            }

        }
        document.add(responsableTabla);
        document.add(tablaDatosDelInformeFacturas);  
    } 
    private void imprimirPorFechaDeDistribucion() throws DocumentException{
        String fechaDistribucionAux = "";
        for (Vst_FacturasDescargadas obj : this.listaDeFacturasRechazadas) {

            if (fechaDistribucionAux.equals("")) {
                fechaDistribucionAux =obj.getFechaDistribucion().toString();
                
                tablaDatosDelInformeFacturas = new PdfPTable(4);
                tablaDatosDelInformeFacturas.setSpacingBefore(2);
                tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                
                tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                tablaDatosDelInformeFacturas.setLockedWidth(true);

                responsableTabla = new Paragraph("\n Fecha de Distribución : " + obj.getFechaDistribucion(), myfont);
                
                insertarEncabezadoFechaDeDistribucion();
                insertarFilaFechaDeDistribucion(obj);

            } else {

                if (fechaDistribucionAux.equals(obj.getFechaDistribucion().toString())) {
                    insertarFilaFechaDeDistribucion(obj);

                } else {
                   
                    
                    document.add(responsableTabla);
                    document.add(tablaDatosDelInformeFacturas);
                    

                    tablaDatosDelInformeFacturas = new PdfPTable(4);
                    tablaDatosDelInformeFacturas.setSpacingBefore(2);
                    tablaDatosDelInformeFacturas.getDefaultCell().setPadding(5);
                    
                    tablaDatosDelInformeFacturas.setTotalWidth(medidaTabla);
                    tablaDatosDelInformeFacturas.setLockedWidth(true);
                    
                    
                    fechaDistribucionAux = obj.getFechaDistribucion().toString();

                    responsableTabla = new Paragraph("\n Causal Fecha de Distribución : " + fechaDistribucionAux, myfont);

                   
                    insertarEncabezadoFechaDeDistribucion();
                    insertarFilaFechaDeDistribucion(obj);
                    
                }
                 
            }

        }
        document.add(responsableTabla);
        document.add(tablaDatosDelInformeFacturas);  
    } 


    private void insertarEncabezadoTablaVendedor() {
        /*Columna 1 número de la Factura*/
        PdfPCell cellFactura_ = new PdfPCell(new Phrase("Factura N°", myfont3));
        cellFactura_.setPadding(5);
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
        
        /*Columna 2 Nombre del Cliente */
        PdfPCell cellCliente_ = new PdfPCell(new Phrase("Cliente", myfont3));
        cellCliente_.setPadding(5);
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 3  nombre dl conductor */
        PdfPCell cellConductor_ = new PdfPCell(new Phrase("Conductor", myfont3));
        cellConductor_.setPadding(5);
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 4 Causal de rechazo */
        PdfPCell cellCausal_ = new PdfPCell(new Phrase("Causal", myfont3));
        cellCausal_.setPadding(5);
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /* Se anexan las celdas al documento */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellConductor_);
        tablaDatosDelInformeFacturas.addCell(cellCausal_);
    }
    private void insertarEncabezadoTablaConductor() {
        /*Columna 1 número de la Factura*/
        PdfPCell cellFactura_ = new PdfPCell(new Phrase("Factura N°", myfont3));
        cellFactura_.setPadding(5);
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
        
        /*Columna 2 Nombre del Cliente */
        PdfPCell cellCliente_ = new PdfPCell(new Phrase("Cliente", myfont3));
        cellCliente_.setPadding(5);
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 3  nombre dl conductor */
        PdfPCell cellVendedor_ = new PdfPCell(new Phrase("Vendedor", myfont3));
        cellVendedor_.setPadding(5);
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 4 Causal de rechazo */
        PdfPCell cellCausal_ = new PdfPCell(new Phrase("Causal", myfont3));
        cellCausal_.setPadding(5);
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /* Se anexan las celdas al documento */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellVendedor_);
        tablaDatosDelInformeFacturas.addCell(cellCausal_);
    }
    private void  insertarEncabezadoTablaCausalDeRechazo() {
        /*Columna 1 número de la Factura*/
        PdfPCell cellFactura_ = new PdfPCell(new Phrase("Factura N°", myfont3));
        cellFactura_.setPadding(5);
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
        
        /*Columna 2 Nombre del Cliente */
        PdfPCell cellCliente_ = new PdfPCell(new Phrase("Cliente", myfont3));
        cellCliente_.setPadding(5);
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 3  nombre dl conductor */
        PdfPCell cellVendedor_ = new PdfPCell(new Phrase("Vendedor", myfont3));
        cellVendedor_.setPadding(5);
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 4 Causal de rechazo */
        PdfPCell cellConductor_ = new PdfPCell(new Phrase("Conductor", myfont3));
        cellConductor_.setPadding(5);
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /* Se anexan las celdas al documento */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellVendedor_);
        tablaDatosDelInformeFacturas.addCell(cellConductor_);
    } 
    private void  insertarEncabezadoFechaDeDistribucion() {
        /*Columna 1 número de la Factura*/
        PdfPCell cellFactura_ = new PdfPCell(new Phrase("Factura N°", myfont3));
        cellFactura_.setPadding(5);
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
        
        /*Columna 2 Nombre del Cliente */
        PdfPCell cellCliente_ = new PdfPCell(new Phrase("Cliente", myfont3));
        cellCliente_.setPadding(5);
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 3  nombre dl conductor */
        PdfPCell cellVendedor_ = new PdfPCell(new Phrase("Vendedor", myfont3));
        cellVendedor_.setPadding(5);
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        /* Columna 4 Causal de rechazo */
        PdfPCell cellConductor_ = new PdfPCell(new Phrase("Conductor", myfont3));
        cellConductor_.setPadding(5);
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /* Se anexan las celdas al documento */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellVendedor_);
        tablaDatosDelInformeFacturas.addCell(cellConductor_);
    } 

    private void insertarFilaVendedor(Vst_FacturasDescargadas obj) {
        PdfPCell cellFactura_;
        PdfPCell cellCliente_;
        PdfPCell cellConductor_;
        PdfPCell cellCausal_;
         String cadena="";
       
        cellFactura_ = new PdfPCell(new Phrase(obj.getNumeroFactura(), myfont1));
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
       
        cadena=obj.getNombreDeCliente();
         if(obj.getNombreDeCliente().length()>= 24)         cadena=obj.getNombreDeCliente().substring(0, 24);
        cellCliente_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getNombreConductor();
         if(obj.getNombreConductor().length()>= 24)         cadena=obj.getNombreConductor().substring(0, 24);
        cellConductor_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getNombreCausalDeRechazo();
         if(obj.getNombreCausalDeRechazo().length()>= 24)         cadena=obj.getNombreCausalDeRechazo().substring(0, 24);
        cellCausal_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /*Incoporación de las celdas a la tabla */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellConductor_);
        tablaDatosDelInformeFacturas.addCell(cellCausal_);
    }
    private void insertarFilaConductor(Vst_FacturasDescargadas obj) {
        PdfPCell cellFactura_;
        PdfPCell cellCliente_;
        PdfPCell cellConductor_;
        PdfPCell cellCausal_;
         String cadena="";
       
        cellFactura_ = new PdfPCell(new Phrase(obj.getNumeroFactura(), myfont1));
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
       
        cadena=obj.getNombreDeCliente();
         if(obj.getNombreDeCliente().length()>= 24)         cadena=obj.getNombreDeCliente().substring(0, 24);
        cellCliente_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getVendedor();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellConductor_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getNombreCausalDeRechazo();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellCausal_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /*Incoporación de las celdas a la tabla */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellConductor_);
        tablaDatosDelInformeFacturas.addCell(cellCausal_);
    }               
    private void  insertarFilaCausalDeRechazo(Vst_FacturasDescargadas obj) {
        PdfPCell cellFactura_;
        PdfPCell cellCliente_;
        PdfPCell cellvendedor_;
        PdfPCell cellConductorl_;
         String cadena="";
       
        cellFactura_ = new PdfPCell(new Phrase(obj.getNumeroFactura(), myfont1));
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
       
        cadena=obj.getNombreDeCliente();
         if(obj.getNombreDeCliente().length()>= 24)         cadena=obj.getNombreDeCliente().substring(0, 24);
        cellCliente_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getVendedor();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellvendedor_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getNombreConductor();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellConductorl_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /*Incoporación de las celdas a la tabla */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellvendedor_);
        tablaDatosDelInformeFacturas.addCell(cellConductorl_);
    } 
    private void  insertarFilaFechaDeDistribucion(Vst_FacturasDescargadas obj) {
        PdfPCell cellFactura_;
        PdfPCell cellCliente_;
        PdfPCell cellvendedor_;
        PdfPCell cellConductorl_;
         String cadena="";
       
        cellFactura_ = new PdfPCell(new Phrase(obj.getNumeroFactura(), myfont1));
        //cellFactura_.setBorder(Rectangle.NO_BORDER);
       
        cadena=obj.getNombreDeCliente();
         if(obj.getNombreDeCliente().length()>= 24)         cadena=obj.getNombreDeCliente().substring(0, 24);
        cellCliente_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCliente_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getVendedor();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellvendedor_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellConductor_.setBorder(Rectangle.NO_BORDER);
        
        cadena=obj.getNombreConductor();
         if(cadena.length()>= 24)         cadena=cadena.substring(0, 24);
        cellConductorl_ = new PdfPCell(new Phrase(cadena, myfont1));
        //cellCausal_.setBorder(Rectangle.NO_BORDER);
        
        /*Incoporación de las celdas a la tabla */
        tablaDatosDelInformeFacturas.addCell(cellFactura_);
        tablaDatosDelInformeFacturas.addCell(cellCliente_);
        tablaDatosDelInformeFacturas.addCell(cellvendedor_);
        tablaDatosDelInformeFacturas.addCell(cellConductorl_);
    } 
   }
