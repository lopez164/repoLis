/*
 * http://jc-mouse.blogspot.com/2011/01/crear-copia-de-seguridad-backup-de.html
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Usuario
 */
public class JcArchivoExcelClienteCamdum {//implements Runnable {

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

    public JcArchivoExcelClienteCamdum(int tiempo, FImportarArchivoExcel buBd, File file,Inicio ini) {
        this.buBd=buBd;
        this.file=file;
        this.ini=ini;
//        try {
////            FileInputStream fis = new FileInputStream(file);
////            workbook = new XSSFWorkbook(fis);
////            numHojas = workbook.getNumberOfSheets();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void ingresarDatos() {
        //Inicio ini = new Inicio(buB);
        String sql = "";
        String sql2 = "";
        String cliente = null,establecimiento = null,mensaje = null;
        String factura=null;
       
        int i = 0;
          int j = 0;
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();
           sql2="";
             System.out.println("inicio inserciones  " +new Date());
            while (rows.hasNext()) {
                try{
                    XSSFRow row = ((XSSFRow) rows.next());
         
                sql = "INSERT INTO `clientesCamdun` (`codigoInterno`, `nitCliente`, `nombreEstablecimiento`, `nombreDeCliente`,"
                            + " `direccion`, `barrio`, `ciudad`, `clasificacion`, `celularCliente`, `emailCliente`,`zona`,`regional`,`agencia`,"
                        + "`activo`, `usuario`, `flag`) "
                        + "VALUES( ";
                 int tipo=0;

               tipo=row.getCell(0).getCellType();
               cliente= ReadRow(tipo, row, 0).replace("'", "#").trim();
               sql+= "'" +  ReadRow(tipo, row, 0).replace("'", "#").trim() + "', "; // codigo interno
               
                tipo=row.getCell(1).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 1).replace("'", "#") + "', "; //nit
               tipo=row.getCell(2).getCellType();
               establecimiento=ReadRow(tipo, row, 2).replace("'", "#").trim();
               sql+= "'" +  ReadRow(tipo, row, 2).replace("'", "#").trim() + "', "; // nombre del establecimiento
                tipo=row.getCell(3).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 3).replace("'", "#") + "', "; // nombre del cliente
                tipo=row.getCell(4).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 4).replace("'", "#") + "', "; // direccion
                tipo=row.getCell(5).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 5).replace("'", "#") + "', "; // barrio
                tipo=row.getCell(6).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 6).replace("'", "#") + "', ";//   ciudad
                tipo=row.getCell(7).getCellType();
               sql+= "'" +  ReadRow(tipo, row, 7).replace("'", "#") + "', "; // clasificacion
               tipo=row.getCell(8).getCellType();
               factura= ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura 
                  sql += "'0', "//celularCliente
                        + "'no incluido', "//emailCliente
                        + ini.getUser().getZona() +","//
                        + ini.getUser().getRegional()+","//
                        + ini.getUser().getAgencia() +","//
                        + "'1','"//activo
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                        + "'1') ON DUPLICATE KEY UPDATE "
                        + "flag='-1';";     
                  
                     // System.out.println("ingresar registro cliente # "  + ";(" + i + ") " + sql);
           
                    
                        i++;    
                        j++;
                    sql2+=sql;
                  // GRABA UN REGISTRO
                    if (i == 1) {
                       // int val = Integer.parseInt(ini.insertarMetodoPost(sql2));
                        int val=1;
                         ini.insertarDatosLocalmente(sql2);
                        switch (val) {
                            case 0:
                                System.out.println("Error de autenticacion) ");
                                JOptionPane.showInternalMessageDialog(null, "Error de autenticacion", "Error", 0);
                                break;
                            case 1:
                        System.out.println("lleva " + j + "  inserciones  clientes camdun " + new Date());

                                break;
                            case 2:
                                System.out.println("2: Si huvo un error en la ejecución de la consulta");
                                JOptionPane.showInternalMessageDialog(null, "error en la ejecución de la consulta sql", "Error", 0);
                                break;
                            case 3:
                                System.out.println("3: Si hubo en error recogiendo los parametros que se reciben");
                                JOptionPane.showInternalMessageDialog(null, " error recogiendo los parametros que se reciben", "Error", 0);
                                break;
                            default:
                                JOptionPane.showInternalMessageDialog(null, " error de conexión a la red", "Error", 0);

                        }
                        i = 0;
                      
                        sql2 = "";
                    }
                  
                 
                   
                  /*
                    if (!ini.insertarDatosLocalmente(sql)) {
                        System.out.println("Error en insertat clientes Camdun (NO ENTRA AL CATCH)" + ";(" + i + ") " + sql);
                        buBd.mensaje += "Error al insertarDatosLocalmente dato Cliente " + cliente + " , establecimiento : " + establecimiento + " factuta # : " + factura + "\n";
                        buBd.txtErrores.setText(buBd.mensaje);
                    }
                  **/
                  
              
              
                }catch(Exception ex){
                     buBd. mensaje+="Error al insertar dato Cliente " + cliente +  " , establecimiento : " + establecimiento +" factuta # : " + factura + "\n" ;
                     buBd.txtErrores.setText( buBd.mensaje);
                      Logger.getLogger(JcArchivoExcelClienteCamdum.class.getName()).log(Level.SEVERE, null, ex);
                      
                       System.out.println("Error en insertar cliente Camdun" + ex + ";(" + i + ") " + sql);
                  sql="";
               
                }
            
            }
              
            // GRABA TODOS LOS REGISTROS
           ///ini.insertarMetodoPost(sql2);
            ini.insertarDatosLocalmente(sql2);
             System.out.println("lleva " + j + "  inserciones  clientes camdun " + new Date());
           System.out.println("Termina  inserciones  " +new Date());
            //workbook.C();
             fis.close();
            System.out.println("Success import excel to mysql table clientes Camdun ");
            //band=true;
        } catch (Exception ex) {
              buBd. mensaje+="Error al insertar dato Cliente " + cliente +  " , establecimiento : " + establecimiento +" factuta # : " + factura + "\n" ;
              buBd.txtErrores.setText( buBd.mensaje);
            Logger.getLogger(JcArchivoExcelClienteCamdum.class.getName()).log(Level.SEVERE, null, ex + ";(" + i + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + i + ") " + sql);
            
            // band=true;
        }

    }

//    @Override
//    public void run() {
//        ingresarDatos();
//    }

    public String ReadRow(int tipo, XSSFRow row, int indice) {
       String valor = null;
              
                switch (tipo) {
                    case 0: { // tipo numerico
                        int val=(int)row.getCell(indice).getNumericCellValue();
                        valor=  String.valueOf(val);

                    }
                    break;
                    case 1: { // tipo string
                        //tipo string, obvio! devuelve un String
//                        lista.add(cell.getStringCellValue());
//                        tip.add("Label");
                        valor=row.getCell(indice).getStringCellValue().trim();
                    }
                    break;
                    case 2: { // tipo formula
                        //tipo formula, devuelve un String
//                        lista.add(cell.getCellFormula());
//                        tip.add("Formula");
                         valor=row.getCell(indice).getCellFormula().toString();
                    }
                    break; 
                    case 3: { // vacio
                        //tipo BLANK blanco o vacio
//                        lista.add("");
//                        tip.add("Vacio");
                         valor=row.getCell(indice).getStringCellValue()+"".trim();
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
                    default:
                        valor="".trim();
                   
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
