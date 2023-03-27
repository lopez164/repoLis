/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.imprimir;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;



/**
 *
 * @author lopez164
 */
public class CodigoDeBarras {
    Image imgShipBarCode;
    public CodigoDeBarras(){
        
    }
    public Image getBarCodeImage(String data, PdfContentByte cb){
        
      Barcode128 shipBarCode = new Barcode128();
      shipBarCode.setX(0.75f);
      shipBarCode.setN(1.5f);
      shipBarCode.setChecksumText(true);
      shipBarCode.setGenerateChecksum(true);
      shipBarCode.setSize(10f);
      shipBarCode.setTextAlignment(Element.ALIGN_CENTER);
      shipBarCode.setBaseline(10f);
      shipBarCode.setCode(data);
      shipBarCode.setBarHeight(40f);
     

      imgShipBarCode = shipBarCode.createImageWithBarcode(cb, BaseColor.BLACK, BaseColor.BLUE);
        
        return imgShipBarCode;
    }
    
}
