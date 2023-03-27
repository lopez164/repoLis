/*
 * http://jc-mouse.blogspot.com/2011/01/crear-copia-de-seguridad-backup-de.html
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class JcArchivoExcelProductosPorFacturaCamdun implements Runnable {

    File file;
    FImportarArchivoExcel buBd;
    Inicio ini = null;
    public static boolean band = false;
    private int tiempo = 5;
    public int numHojas;
    public int numrows;
    public int numcolumns;
    public Object[] tipos = null;
    private XSSFWorkbook workbook;
    private Iterator rows;

    public JcArchivoExcelProductosPorFacturaCamdun(int tiempo, FImportarArchivoExcel buBd, File file, Inicio ini) {
        this.buBd = buBd;
        this.file = file;
        this.ini = ini;
//        try {
////            FileInputStream fis = new FileInputStream(file);
////            workbook = new XSSFWorkbook(fis);
////            numHojas = workbook.getNumberOfSheets();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public boolean ingresarDatos_original() {
        boolean grabado = false;
        //Inicio ini = new Inicio("sggbcUwXck6KiLTwXzPlkA");
        String sql = null, sql2 = "";
        String cliente = null;
        String factura = null;
        int i = 0, j = 0;
        int z = 1;
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            int x = (int) numrows / 100;
            int y = 0;
            rows = sheet.rowIterator();
            buBd.barraInferior.setStringPainted(true);
            while (rows.hasNext()) {
                try {
                    grabado = false;
                    XSSFRow row = ((XSSFRow) rows.next());

                    sql = "INSERT INTO `productosPorFactura`(`factura`,`codigoProducto`,`cantidad`,`valorUnitarioConIva`,`valorTotalConIva`,"
                            + "`activo`,`usuario`,`flag`) VALUES(";
                    int tipo;
                    tipo = row.getCell(0).getCellType();
                    cliente = ReadRow(tipo, row, 0).replace("'", "#").trim();
                    tipo = row.getCell(8).getCellType();
                    factura = ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura 
                    int val = (int) row.getCell(8).getNumericCellValue();
                    String valor = String.valueOf(val);
                    factura = valor;
                    sql += "'" + valor + "',";
                    val = (int) row.getCell(10).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";
                    val = (int) row.getCell(16).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";

                    sql += "'" + row.getCell(17).getNumericCellValue() + "',";

                    sql += "'" + row.getCell(18).getNumericCellValue() + "',";

                    sql += "'1','"//activo
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            + "'1') ON DUPLICATE KEY UPDATE "
                            + "flag='-1';";

                    grabado = ini.insertarDatosLocalmente(sql);

                    if (!grabado) {
                        System.out.println("Error en insertat productos por factura Camdun (NO ENTRA AL CATCH)" + ";(" + i + ") " + sql);
                        buBd.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
                        buBd.txtErrores.setText(buBd.mensaje);
                    };

                } catch (Exception ex) {
                    System.out.println("Error en insertar productos por factura Camdun" + ex + ";(" + i + ") " + sql);
                    buBd.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
                    buBd.txtErrores.setText(buBd.mensaje);
                }
                i++;
                j++;
                y++;

                if (y > x) {
                    y = 0;
                    buBd.barraInferior.setValue(z);
                    buBd.barraInferior.repaint();
                    z++;
                }

                Thread.sleep(1);
            }
//             ini.insertarMetodoPost(sql2);
//               System.out.println("lleva " + j + "  inserciones prodcutos por factura " + new Date());
            buBd.barraInferior.setValue(100);
            buBd.barraInferior.repaint();
            band = true;
            fis.close();
            System.out.println("Success import excel to mysql table productos por factura");

        } catch (Exception ex) {
            buBd.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
            buBd.txtErrores.setText(buBd.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun.class.getName()).log(Level.SEVERE, null, ex + ";(" + i + ") " + sql);
            System.out.println("Error en insertar producto en la factura " + ex + ";(" + i + ") " + sql);

            band = true;
        }
        return grabado;

    }

    public boolean ingresarDatos_copia() {
        boolean grabado = false;
        String sql = null, sql2 = "";
        String cliente = null;
        String factura = null;
        int i = 0;
        int j = 0;
        // PUNTOS EN EL PROGRESS BAR
        int puntosProgressBar = 1;
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            //  SE TOMA EL # DE FILAS DEL ARCHIVO Y SE DIVIDE EN LOS 100 PUNTOS DEL PROGRESS BAR
            int porcentajeProgressBar = (int) numrows / 100;
            // 
            int filasPorPorcentaje = 0;
            rows = sheet.rowIterator();
            buBd.barraInferior.setStringPainted(true);
            // INICIA EL WHILE DE FILAS EN EL ARCHIVO
            while (rows.hasNext()) {
                try {
                    grabado = false;
                    XSSFRow row = ((XSSFRow) rows.next());

                    sql = "INSERT INTO `productosPorFactura`(`factura`,`codigoProducto`,`cantidad`,`valorUnitarioConIva`,`valorTotalConIva`,"
                            + "`activo`,`usuario`,`flag`) VALUES(";
                    int tipo;
                    tipo = row.getCell(0).getCellType();
                    cliente = ReadRow(tipo, row, 0).replace("'", "#").trim();
                    tipo = row.getCell(8).getCellType();
                    factura = ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura 
                    int val = (int) row.getCell(8).getNumericCellValue();
                    String valor = String.valueOf(val);
                    factura = valor;
                    sql += "'" + valor + "',";
                    val = (int) row.getCell(10).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";
                    val = (int) row.getCell(16).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";

                    sql += "'" + row.getCell(17).getNumericCellValue() + "',";

                    sql += "'" + row.getCell(18).getNumericCellValue() + "',";

                    sql += "'1','"//activo
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            + "'1') ON DUPLICATE KEY UPDATE "
                            + "flag='-1';";
                    sql2 += sql;
                    i++;  j++;  filasPorPorcentaje++;

                } catch (Exception ex) {
                    System.out.println("Error en insertar productos por factura Camdun" + ex + ";(" + i + ") " + sql);
                    buBd.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
                    buBd.txtErrores.setText(buBd.mensaje);
                }
                // VALIDA SI HAY 100 REGISTROS PARA  PODER INSERTAR EN LA BASE DE DATOS
                if (i == 1) {
                    //ini.insertarMetodoPost(sql2);
                      ini.insertarDatosLocalmente(sql2);
                    Thread.sleep(1);
                    System.out.println("lleva " + j + "  inserciones prodcutos por factura " + new Date());
                    i = 0;
                    grabado = true;
                    sql2 = "";
                }
                // INCREMENTA EL VALOR DE PROGRESS BAR
              
                if (filasPorPorcentaje > porcentajeProgressBar) {
                    filasPorPorcentaje = 0;
                    buBd.barraInferior.setValue(puntosProgressBar);
                    buBd.barraInferior.repaint();
                    puntosProgressBar++;
                }

                Thread.sleep(1);
            } // FINAL DEL WHILE

            //ini.insertarMetodoPost(sql2);
              ini.insertarDatosLocalmente(sql2);
            System.out.println("lleva " + j + "  inserciones prodcutos por factura " + new Date());
            buBd.barraInferior.setValue(100);
            buBd.barraInferior.repaint();
            band = true;
            fis.close();
            System.out.println("Success import excel to mysql table productos por factura");

        } catch (Exception ex) {
            buBd.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
            buBd.txtErrores.setText(buBd.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun.class.getName()).log(Level.SEVERE, null, ex + ";(" + i + ") " + sql);
            System.out.println("Error en insertar producto en la factura " + ex + ";(" + i + ") " + sql);

            band = true;
        }
        return grabado;

    }

    @Override
    public void run() {
        ingresarDatos_copia();
    }

    public String ReadRow(int tipo, XSSFRow row, int indice) {
        String valor = null;

        switch (tipo) {
            case 0: {
                int val = (int) row.getCell(indice).getNumericCellValue();
                valor = String.valueOf(val);

            }
            break;
            case 1: {
                //tipo string, obvio! devuelve un String
//                        lista.add(cell.getStringCellValue());
//                        tip.add("Label");
                valor = row.getCell(indice).getStringCellValue();
            }
            break;
            case 2: {
                //tipo formula, devuelve un String
//                        lista.add(cell.getCellFormula());
//                        tip.add("Formula");
                //valor=row.getCell(indice).getCellFormula().toString();
                valor = row.getCell(indice).getStringCellValue();
            }
            break;
            case 3: {
                //tipo BLANK blanco o vacio
//                        lista.add("");
//                        tip.add("Vacio");
                valor = row.getCell(indice).getStringCellValue() + "";
            }
            break;
            case 4: {
                //tipo boolean, devuelve un boolean
//                        lista.add(cell.getBooleanCellValue());
//                        tip.add("Boolean");
                valor = row.getCell(indice).getBooleanCellValue() + "";
            }
            break;
            case 5: {
                //tipo boolean, devuelve un boolean
//                        lista.add(cell.getErrorCellString());
//                        tip.add("Error");
                valor = row.getCell(indice).getStringCellValue() + "";
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
