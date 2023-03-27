/*
 * http://jc-mouse.blogspot.com/2011/01/crear-copia-de-seguridad-backup-de.html
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class JcArchivoExcelFacturasCamdun_2  {//{implements Runnable {

    File file;
    FImportarArchivoExcel buBd;
    Inicio ini=null;
    public static boolean band = false;
    private int tiempo = 5;
    public int numHojas;
    public int numrows;
    public int numcolumns;
    public Object[] tipos = null;
    private XSSFWorkbook workbook;
    private Iterator rows;

    public JcArchivoExcelFacturasCamdun_2(int tiempo, FImportarArchivoExcel buBd, File file,Inicio ini) {
        this.buBd=buBd;
        this.file=file;
        this.ini=ini;


    }

    public void ingresarDatos() {
     
        ingresarFacturasCamdun();

    }

    private void ingresarFacturasCamdun() {
        String sql = null,sql2 = "";
        String factura = null;
        String cliente = null;
        int i = 0 ,j= 0;
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();

            while (rows.hasNext()) {
                try{
                    XSSFRow row = ((XSSFRow) rows.next());
                    int tipo;
                    
                    sql=  "INSERT INTO `facturasCamdun`(`numeroFactura`,`fechaDeVenta`,`cliente`,`vendedor`,"
                            + "`valorFacturaSinIva`,`valorIvaFactura`,`valorTotalFactura`,`zona`,`regional`,`agencia`,"
                            + "`activo`,`usuario`,`flag`,`canal`)VALUES (";
                    
                    // CODIGO DEL CLIENTE
                    tipo=row.getCell(0).getCellType();
                    cliente= ReadRow(tipo, row, 0).replace("'", "#").trim();
                    int val=(int)row.getCell(0).getNumericCellValue(); //cliente
                    String valor=  String.valueOf(val).trim();
                    
                    // NUMERO DE LA FACTURA
                    val=(int)row.getCell(8).getNumericCellValue();// numeroFactura
                    valor=  String.valueOf(val).trim();
                    factura=valor;
                    sql+= "'" + valor + "',";
                    
                    
                    //FECHA DE VENTA
                    tipo=row.getCell(9).getCellType(); //fechaDeVenta
                    sql+= "'" + ReadRow(tipo, row, 9).trim()+ "',";
                    sql+= "'" + valor + "',";
                    
                    //  NOMBRE DEL VENDEDOR
                    tipo=row.getCell(19).getCellType();
                    sql+= "'" + ReadRow(tipo, row, 19).trim() + "',";  // vendedor
                    
                    // VALOR DE LA FACTURA SIN IVA
                    double suma=0.0;
                    sql+= "'" + row.getCell(13).getNumericCellValue() + "',"; // valorFacturaSinIva
                    
                    // VALOR DEL IVA DE LA FACTURA
                    sql+= "'" + row.getCell(14).getNumericCellValue() + "',"; // valorIvaFactura
                    
                    // VALOR TOTAL DE LA FACTURA
                    suma=row.getCell(13).getNumericCellValue() + row.getCell(14).getNumericCellValue();
                    sql+= "'" +suma+ "',";// valorTotalFactura
                    
                    // ZONA, REGIONAL, AGENCIA DE LA FACTURA
                    sql+= ini.getUser().getZona() +","//
                            + ini.getUser().getRegional()+","//
                            + ini.getUser().getAgencia() +","//
                            
                            // ACTIVO
                            + "'1','"//activo
                            
                            // USUARIO
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            
                            // FLAG
                            + "'1','1') ON DUPLICATE KEY UPDATE "
                            + "flag='-1';";
                    // System.out.println("factura a ingresar" +  ";(" + i + ") " + sql);
                    
                    i++;
                    j++;
                    
                    
                    ini.insertarDatosLocalmente(sql);
                    
                }catch(Exception ex){
                    buBd. mensaje+="Error al insertar dato Factura # " + factura + " cliente : " + cliente + "\n" ;
                    buBd.txtErrores.setText(buBd.mensaje);
                    Logger.getLogger(JcArchivoExcelFacturasCamdun_2.class.getName()).log(Level.SEVERE, null, ex + ";(" + i + ") " + sql);
                    System.out.println("Error en insertat facturas Camdun" + ex + ";(" + i + ") " + sql);
                    i=0;
                     
                }
              
            }
 
            fis.close();
            
            System.out.println("lleva " + j + "  inserciones  facturas camdun " + new Date());
            System.out.println("Success import excel  to mysql table facturas Camdun");
            //band=true;
        } catch (Exception ex) {
            buBd. mensaje+="Error al insertar dato Factura # " + factura + " cliente : " + cliente + "\n" ;
            buBd.txtErrores.setText(buBd.mensaje);
            Logger.getLogger(JcArchivoExcelFacturasCamdun_2.class.getName()).log(Level.SEVERE, null, ex + ";(" + i + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + i + ") " + sql);
            
        }
    }

//    @Override
//    public void run() {
//        ingresarDatos();
//    }

    public String ReadRow(int tipo, XSSFRow row, int indice) {
       String valor = null;
              
                switch (tipo) {
                    case 0: {  //  codigo tomado de http://recetasdeprogramacion.blogspot.com/2013/05/generar-ficheros-ms-excel.html  para campos de fecha
                        if (HSSFDateUtil.isCellDateFormatted(row.getCell(indice))) {
                             SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                             valor= ft.format(row.getCell(indice).getDateCellValue()) ;
                            //valor=String.valueOf(row.getCell(indice).getDateCellValue());
                            
                        } else {
                        int val = (int) row.getCell(indice).getNumericCellValue();
                        valor = String.valueOf(val);
                        }
                       

                    }
                    break;
                    case 1: {
                        //tipo string, obvio! devuelve un String
//                        lista.add(cell.getStringCellValue());
//                        tip.add("Label");
                        valor=row.getCell(indice).getStringCellValue();
                    }
                    break;
                    case 2: {
                        //tipo formula, devuelve un String
//                        lista.add(cell.getCellFormula());
//                        tip.add("Formula");
                         //valor=row.getCell(indice).getCellFormula().toString();
                         valor=row.getCell(indice).getStringCellValue();
                    }
                    break;
                    case 3: {
                        //tipo BLANK blanco o vacio
//                        lista.add("");
//                        tip.add("Vacio");
                         valor=row.getCell(indice).getStringCellValue()+"";
                    }
                    break;
                    case 4: {
                        //tipo boolean, devuelve un boolean
//                        lista.add(cell.getBooleanCellValue());
//                        tip.add("Boolean");
                         valor=row.getCell(indice).getBooleanCellValue() + "";
                    }
                    break;
                    case 5: {
                        //tipo boolean, devuelve un boolean
//                        lista.add(cell.getErrorCellString());
//                        tip.add("Error");
                         valor=row.getCell(indice).getStringCellValue()+ "";
                    }
                    break;

                }
          
            return valor;
    }

    public String[] ReadSheet() {
        String[] hojas = null;

        hojas = new String[numHojas];
        for (int i = 0; i < numHojas; i++) {
            hojas[i] = workbook.getSheetAt(i).getSheetName();
        }
        return hojas;
    }

    public Object[] ReadHead(String hj) {
        XSSFSheet sheet = workbook.getSheet(hj);
        numrows = sheet.getLastRowNum();
        rows = sheet.rowIterator();
        return copia_ReadRow();
    }
    public Object[] copia_ReadRow() {
        numcolumns = 0;
        Object[] fila = null;
        ArrayList lista = new ArrayList();
        ArrayList tip = new ArrayList();

        if (rows.hasNext()) {
            XSSFRow row = ((XSSFRow) rows.next());
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {

                numcolumns++;
                XSSFCell cell = (XSSFCell) cells.next();
                int tipo = cell.getCellType();
                switch (tipo) {
                    case 0: {
                        //tipo numeric, devuelve un double

                        lista.add(cell.getNumericCellValue());
                        tip.add("Numeric");

                    }
                    break;
                    case 1: {
                        //tipo string, obvio! devuelve un String
                        lista.add(cell.getStringCellValue());
                        tip.add("Label");
                    }
                    break;
                    case 2: {
                        //tipo formula, devuelve un String
                        lista.add(cell.getCellFormula());
                        tip.add("Formula");
                    }
                    break;
                    case 3: {
                        //tipo BLANK blanco o vacio
                        lista.add("");
                        tip.add("Vacio");
                    }
                    break;
                    case 4: {
                        //tipo boolean, devuelve un boolean
                        lista.add(cell.getBooleanCellValue());
                        tip.add("Boolean");
                    }
                    break;
                    case 5: {
                        //tipo boolean, devuelve un boolean
                        lista.add(cell.getErrorCellString());
                        tip.add("Error");
                    }
                    break;

                }
            }
            tipos = new Object[numcolumns];
            fila = new Object[numcolumns];
            for (int i = 0; i < numcolumns; i++) {
                fila[i] = lista.get(i).toString();
                tipos[i] = tip.get(i).toString();

            }
        }
        return fila;
    }
    
}
