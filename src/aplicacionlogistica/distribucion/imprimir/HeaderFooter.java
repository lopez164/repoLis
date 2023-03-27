/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.TiffImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author lopez164
 */
public class HeaderFooter extends PdfPageEventHelper {

    PdfPCell celda1;
    PdfPCell celda2, celda3, celda4;
    Image imgLogoCliente;
    Image imglLogoPropio;
    PdfPTable tablaDelEncabezado = new PdfPTable(1);
    Rectangle rect;
    Font myfont;
    Font myfont0;
    Font myfont1;
    Font myfont2;
    Font myfont3;
    Font myfont4;
    Font myfont5;
    Font myfont6;
    String ruta;
    boolean dibujarLinea = false;
    boolean conMarcaDeAgua=false;
    CManifiestosDeDistribucion manifiesto = null;
    PdfWriter pdw = null;
    //int estadoManifiesto;
    Properties propiedades;
    Date fecha;
    Inicio ini;

    public HeaderFooter(Inicio ini, boolean dibujarLinea) {
        this.ini = ini;
        propiedades = ini.getPropiedades();
        this.dibujarLinea = dibujarLinea;

        try {
            ruta = "" + (new File(".").getAbsolutePath()).replace(".", "");

            // various fonts
            myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
            myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
            myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
            myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
            myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
            myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
            myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
            myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));

            /*Encabezado del Informe (manifiesto)*/
            celda1 = new PdfPCell(new Phrase(propiedades.getProperty("nombreCliente")));
            celda2 = new PdfPCell(new Phrase("Nit. " + propiedades.getProperty("nitCliente")));
            celda3 = new PdfPCell(new Phrase(propiedades.getProperty("direccionCliente"), myfont3));
            celda4 = new PdfPCell(new Phrase(propiedades.getProperty("ciudadCliente"), myfont3));

            celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda2.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda3.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda4.setHorizontalAlignment(Element.ALIGN_CENTER);

            ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoCliente");
            java.awt.Image   img = new ImageIcon(ruta).getImage();
            //java.awt.Image img = new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logos/" + propiedades.getProperty("logoCliente"))).getImage();

            imgLogoCliente = Image.getInstance(img, Color.yellow);

            celda1.setBorder(Rectangle.NO_BORDER);
            celda2.setBorder(Rectangle.NO_BORDER);
            celda3.setBorder(Rectangle.NO_BORDER);
            celda4.setBorder(Rectangle.NO_BORDER);

            // NOMBRE DE LA EMPRESA
            tablaDelEncabezado.addCell(celda1);
            // NIT DE LA EMPRESA
            tablaDelEncabezado.addCell(celda2);
            // DIRECCION DE LA EMPRESA
            tablaDelEncabezado.addCell(celda3);
            // CIUDAD DE LA EMPRESA
            tablaDelEncabezado.addCell(celda4);

            ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoPropio");
            img = new ImageIcon(ruta).getImage();
            imglLogoPropio = Image.getInstance(img, Color.yellow);

            int x, y, heigh, width;

            x = Integer.parseInt(propiedades.getProperty("logoCliente_x"));
            y = Integer.parseInt(propiedades.getProperty("logoCliente_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoClienteHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoClientewidth"));

            imgLogoCliente.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            //imgLogoCliente.scaleAbsolute(90, 70); //fitHeight);//(90, 200);
            imgLogoCliente.setAbsolutePosition(x, y);
            //imgLogoCliente.setAbsolutePosition(20, 710);

            x = Integer.parseInt(propiedades.getProperty("logoPropio_x"));
            y = Integer.parseInt(propiedades.getProperty("logoPropio_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoPropioHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoPropioWidth"));

            imglLogoPropio.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            imglLogoPropio.setAbsolutePosition(x, y);
            //imglogo2.setAbsolutePosition(630, 510);

            tablaDelEncabezado.setTotalWidth(350f);
          
            if (manifiesto != null) {
                if (manifiesto.getEstadoManifiesto() == 2) {
                    generarMarcaAgua();
                }
            }

  

//        } catch (BadElementException | IOException ex) {
//            Logger.getLogger(HeaderFooter.class.getName()).log(Level.SEVERE, null, ex);
//        }
        } catch (BadElementException | IOException ex) {
            Logger.getLogger(HeaderFooter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public HeaderFooter(Inicio ini, boolean dibujarLinea, CManifiestosDeDistribucion manifiesto) {
        this.ini = ini;
        propiedades = ini.getPropiedades();
        this.dibujarLinea = dibujarLinea;
        this.manifiesto = manifiesto;

        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "");
        // various fonts
        myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
        myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
        myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
        myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
        myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
        myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
        myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
        myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));
        /*Encabezado del Informe (manifiesto)*/
        celda1 = new PdfPCell(new Phrase(propiedades.getProperty("nombreCliente")));
        celda2 = new PdfPCell(new Phrase("Nit. " + propiedades.getProperty("nitCliente")));
        celda3 = new PdfPCell(new Phrase(propiedades.getProperty("direccionCliente"), myfont3));
        celda4 = new PdfPCell(new Phrase(propiedades.getProperty("ciudadCliente"), myfont3));
        celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda2.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda3.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda4.setHorizontalAlignment(Element.ALIGN_CENTER);
        java.awt.Image img = null;
        int x, y, heigh, width;
        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoCliente");
        //img = new javax.swing.ImageIcon(getClass().getResource(ruta)).getImage();
        
        try {
            img = new ImageIcon(ruta).getImage();
            imgLogoCliente = Image.getInstance(img, Color.yellow);
            x = Integer.parseInt(propiedades.getProperty("logoCliente_x"));
            y = Integer.parseInt(propiedades.getProperty("logoCliente_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoClienteHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoClientewidth"));

            imgLogoCliente.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            //imgLogoCliente.scaleAbsolute(90, 70); //fitHeight);//(90, 200);
            imgLogoCliente.setAbsolutePosition(x, y);
            //imgLogoCliente.setAbsolutePosition(20, 710);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ruta + "\n Revisar archivo de configuracion", "Imagen de logoCliente no encontrada", JOptionPane.ERROR_MESSAGE);
        }
        
        celda1.setBorder(Rectangle.NO_BORDER);
        celda2.setBorder(Rectangle.NO_BORDER);
        celda3.setBorder(Rectangle.NO_BORDER);
        celda4.setBorder(Rectangle.NO_BORDER);
        // NOMBRE DE LA EMPRESA
        tablaDelEncabezado.addCell(celda1);
        // NIT DE LA EMPRESA
        tablaDelEncabezado.addCell(celda2);
        // DIRECCION DE LA EMPRESA
        tablaDelEncabezado.addCell(celda3);
        // CIUDAD DE LA EMPRESA
        tablaDelEncabezado.addCell(celda4);
        try{
            
            ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoPropio");
            img = new ImageIcon(ruta).getImage();
            imglLogoPropio = Image.getInstance(img, Color.yellow);

            x = Integer.parseInt(propiedades.getProperty("logoPropio_x"));
            y = Integer.parseInt(propiedades.getProperty("logoPropio_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoPropioHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoPropioWidth"));

            imglLogoPropio.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            imglLogoPropio.setAbsolutePosition(x, y);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ruta + "\n Revisar archivo de configuracion", "Imagen de logoPropio no encontrada", JOptionPane.ERROR_MESSAGE);
        }
        tablaDelEncabezado.setTotalWidth(350f);
        if (manifiesto.getEstadoManifiesto() == 2) {
            generarMarcaAgua();
        }

    }

    public HeaderFooter(Inicio ini, boolean dibujarLinea, CManifiestosDeDistribucion manifiesto, PdfWriter pdw, boolean conMarcaDeAgua) {
        this.ini = ini;
        this.conMarcaDeAgua = conMarcaDeAgua;
        propiedades = ini.getPropiedades();
        this.dibujarLinea = dibujarLinea;
        this.manifiesto = manifiesto;
        this.pdw = pdw;
       // this.estadoManifiesto = estadoManifiesto;
        this.fecha=fecha;

        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "");
        // various fonts
        myfont = new Font(FontFactory.getFont("Verdana", 7, Font.NORMAL));
        myfont0 = new Font(FontFactory.getFont("Verdana", 7, Font.BOLDITALIC));
        myfont1 = new Font(FontFactory.getFont("Verdana", 9, Font.NORMAL));
        myfont2 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD, BaseColor.RED));
        myfont3 = new Font(FontFactory.getFont("Verdana", 9, Font.BOLD));
        myfont4 = new Font(FontFactory.getFont("Verdana", 10, Font.NORMAL));
        myfont5 = new Font(FontFactory.getFont("Verdana", 12, Font.BOLD));
        myfont6 = new Font(FontFactory.getFont("Verdana", 15, Font.BOLD));
        /*Encabezado del Informe (manifiesto)*/
        celda1 = new PdfPCell(new Phrase(propiedades.getProperty("nombreCliente")));
        celda2 = new PdfPCell(new Phrase("Nit. " + propiedades.getProperty("nitCliente")));
        celda3 = new PdfPCell(new Phrase(propiedades.getProperty("direccionCliente"), myfont3));
        celda4 = new PdfPCell(new Phrase(propiedades.getProperty("ciudadCliente"), myfont3));
        celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda2.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda3.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda4.setHorizontalAlignment(Element.ALIGN_CENTER);
        //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoCliente");
        //java.awt.Image img = new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logos/" + propiedades.getProperty("logoCliente"))).getImage();
        java.awt.Image img = null;
        int x = 0, y = 0, heigh = 0, width = 0;
       
        try {
           ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoCliente");
           img = new ImageIcon(ruta).getImage();
            
            imgLogoCliente = Image.getInstance(img, Color.yellow);
            x = Integer.parseInt(propiedades.getProperty("logoCliente_x"));
            y = Integer.parseInt(propiedades.getProperty("logoCliente_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoClienteHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoClientewidth"));
            imgLogoCliente.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            imgLogoCliente.setAbsolutePosition(x, y);
            //home/lelopez/Documentos/proyectos/globalMarcasDistribuciones/imagenes/logos
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ruta + "\n Revisar archivo de configuracion", "Imagen de logoCliente no encontrada", JOptionPane.ERROR_MESSAGE);
        }
        
        celda1.setBorder(Rectangle.NO_BORDER);
        celda2.setBorder(Rectangle.NO_BORDER);
        celda3.setBorder(Rectangle.NO_BORDER);
        celda4.setBorder(Rectangle.NO_BORDER);
        // NOMBRE DE LA EMPRESA
        tablaDelEncabezado.addCell(celda1);
        // NIT DE LA EMPRESA
        tablaDelEncabezado.addCell(celda2);
        // DIRECCION DE LA EMPRESA
        tablaDelEncabezado.addCell(celda3);
        // CIUDAD DE LA EMPRESA
        tablaDelEncabezado.addCell(celda4);
        try {
           ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/logos/" + propiedades.getProperty("logoPropio");
          //  img = new javax.swing.ImageIcon(getClass().getResource("/imagenes/logos/" + propiedades.getProperty("logoPropio"))).getImage();
            img = new ImageIcon(ruta).getImage();
            imglLogoPropio = Image.getInstance(img, Color.yellow);
            x = Integer.parseInt(propiedades.getProperty("logoPropio_x"));
            y = Integer.parseInt(propiedades.getProperty("logoPropio_y"));
            heigh = Integer.parseInt(propiedades.getProperty("logoPropioHeigh"));
            width = Integer.parseInt(propiedades.getProperty("logoPropioWidth"));
            imglLogoPropio.scaleAbsolute(heigh, width); //fitHeight);//(90, 200);
            //imglLogoPropio.setAbsolutePosition(500, 710);
            //imglogo2.setAbsolutePosition(630, 510);
            imglLogoPropio.setAbsolutePosition(x, y);
            
            if (this.conMarcaDeAgua) {
                    generarMarcaAgua();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ruta + "\n Revisar archivo de configuracion", "Imagen de logoPropio no encontrada", JOptionPane.ERROR_MESSAGE);
        }
        tablaDelEncabezado.setTotalWidth(350f);
        
//        } catch (BadElementException | IOException ex) {
//            Logger.getLogger(HeaderFooter.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
           
            if (this.conMarcaDeAgua) {
                /* Si el manifiesto no se ha guardado en la BBDD se imprime uno provisional */
                if (manifiesto.getEstadoManifiesto() == 2) {
                    generarMarcaAgua();
                }
                if (manifiesto.getEstadoManifiesto() == 4) {
                    generarMarcaAgua();
                }
            }
            rect = writer.getBoxSize("art");

            /*Se dibujan las linesas del titulo del informe*/
            PdfContentByte cb = writer.getDirectContent();

            cb.moveTo(40, 700);
            cb.lineTo(500, 700);
            cb.stroke();
            
            try{
            document.add(imgLogoCliente);
            document.add(imglLogoPropio);
            }catch(Exception ex){
                
            }

            tablaDelEncabezado.writeSelectedRows(0, -1, 120f, 770f, writer.getDirectContent());
            //tablaDelEncabezado.writeSelectedRows(0, 1, 120f, 770f, writer.getDirectContent());

            if (this.dibujarLinea) {
                cb.moveTo(40, 630);
                cb.lineTo(500, 630);
                cb.stroke();
                dibujarLinea = false;
            }

            cb.moveTo(40, 40);
            cb.lineTo(500, 40);
            cb.stroke();

            /*Se agrega el número de la página al pie de página*/
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("página %d", writer.getPageNumber()), myfont),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 5, 0);

            /* Se agrega la fecha de impresión del documento al pie de página */
            String strDateFormat = "yyyy-MM-dd hh: mm: ss" ;//hh: mm: ss a dd-MMM-aaaa"; // El formato de fecha está especificado  
            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
            
            if (manifiesto != null){
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Fecha de Despacho: " + manifiesto.getHoraDeDespacho(), myfont), 550, rect.getBottom(), 0);
            
            /* Se agrega la fecha de impresión del documento al pie de página */
           
              ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Manifiesto N° : " + manifiesto.codificarManifiesto(), myfont), 45, rect.getBottom(), 0);

            }else{
             ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Fecha de Impresion : " + new Date(), myfont), 45, rect.getBottom(), 0);
 
            }
            

            //doc.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(HeaderFooter.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error encabezado del documento " + ex, "Error", 0);
        }
    }

    public void generarMarcaAgua() {
        //Indicamos la ruta de la imagen
        // String rutaImagen = "c:\\imagen.tif";

        //Se crea el documento
        Document document = new Document();

        try {
            //Se crea un writer de la clase PdfWriter para generar el PDF
            // PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:\\imagen.pdf"));

            document.open();

            document.setPageSize(PageSize.LETTER);

            //Se manda llamar el método que coloca la imágen en el PDF
            // poneImagen(document, rutaImagen); //Se manda llamar el método que coloca el texto "marca de agua", en el PDF
           
            poneSelloAgua();

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void poneImagen(Document document, String archivo) {
        RandomAccessFileOrArray ra = null;
        boolean resultado = false;
        try {
            //ra = new RandomAccessFileOrArray(archivo, true);
            ra = new RandomAccessFileOrArray(archivo);
            Image img = TiffImage.getTiffImage(ra, 1);
            if (img != null) {
                //Se valida la escala de la imágen
                if (img.getScaledWidth() > 600 || img.getScaledWidth() > 800) {
                    //Se reduce la escala de la imágen    
                    img.scaleToFit(615, 825);
                }
                //Se indica la posicion donde se colocara la imágen en el PDF
                img.setAbsolutePosition(0, 0);
                //Se inserta la imágen en el PDF
                document.add(img);
            }
            ra.close();
        } catch (Exception e) {
            System.out.println("Error al agregar imagen al PDF " + e.getMessage());
        }
    }

    private void poneSelloAgua() {
        try {

            PdfContentByte cb = pdw.getDirectContent();
            //Se crea un templete para asignar la marca de agua
            PdfTemplate template = cb.createTemplate(700, 300);

            template.beginText();
            //Inicializamos los valores para el templete
            //Se define el tipo de letra, color y tamaño
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            template.setColorFill(new BaseColor(220, 220, 220));
            template.setFontAndSize(bf, 30);

            template.setTextMatrix(0, 0);

            //Se define el texto que se agregara como marca de agua
            if (manifiesto.getEstadoManifiesto() == 2) {
                template.showText("MANIFIESTO PROVISIONAL");
            }

            if (manifiesto.getEstadoManifiesto() == 4) {
                template.showText("MANIFIESTO DESCARGADO");
            }

            template.endText();

            //Se asigna el templete
            //Se asignan los valores para el texto de marca de agua
            // Se asigna el grado de inclinacion y la posicion donde aparecerá el texto
            //                                                     x    y
            cb.addTemplate(template, 1, 1, -1, 1, 150, 200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
