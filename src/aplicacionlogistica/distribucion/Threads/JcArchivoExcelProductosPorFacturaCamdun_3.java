/*
 * http://jc-mouse.blogspot.com/2011/01/crear-copia-de-seguridad-backup-de.html
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexionenBurbuja;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
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
public class JcArchivoExcelProductosPorFacturaCamdun_3
implements Runnable {

    File file;
    FImportarArchivoExcel form;
    Inicio ini = null;
    public static boolean band = false;
    private int tiempo = 5;
    public int numHojas;
    public int numrows;
    public int numcolumns;
    public Object[] tipos = null;
    private XSSFWorkbook workbook;
    private Iterator rows;
    
    int controladorDeCiclos=0;
    String sql2="";
    int totalTodasLasFilas=0;
  double contadorDeTodasLasFilas=0;
  int porcentajeBarraSuperior;

    public JcArchivoExcelProductosPorFacturaCamdun_3(int tiempo, FImportarArchivoExcel form, File file, Inicio ini) {
        this.form = form;
        this.file = file;
        this.ini = ini;

    }

    public void ingresarDatos() {
        try {
            ingresarClientesCamdun();
            this.form.barraSuperior.setValue(25);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);
            
            insertarProductosCamdun();
              this.form.barraSuperior.setValue(50);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);
            
            ingresarFacturasCamdun();
              this.form.barraSuperior.setValue(75);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);
            
            ingresarProductosPorFactura();
              this.form.barraSuperior.setValue(100);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_3.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
      private void ingresarClientesCamdun() {
        String sql = "";
        String idCliente = null, establecimiento = null, mensaje = null;
        String factura = null;
        
          int numeroFilas;
         double contadorDeFilas=0;
        int porcentajeBarraInferior;
        
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            
          numeroFilas=sheet.getLastRowNum();
            totalTodasLasFilas=numeroFilas*4;
            contadorDeTodasLasFilas=0;
            
            
            numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();
           
            System.out.println("inicio inserciones  " + new Date());
            while (rows.hasNext()) {
                try {
                    XSSFRow row = ((XSSFRow) rows.next());
                    CClientes cliente = new CClientes(ini);

                    sql = "INSERT INTO `clientescamdun` (`codigoInterno`, `nitCliente`, `nombreEstablecimiento`, `nombreDeCliente`,"
                            + " `direccion`, `barrio`, `ciudad`, `clasificacion`, `celularCliente`, `emailCliente`,`zona`,`regional`,`agencia`,"
                            + "`activo`, `usuario`, `flag`) "
                            + "VALUES( ";
                    int tipo = 0;
                    
                    // CODIGO DEL CLIENTE
                    tipo = row.getCell(0).getCellType();
                    idCliente = ReadRow(tipo, row, 0).replace("'", "#").trim();
                    sql += "'" + ReadRow(tipo, row, 0).replace("'", "#").trim() + "', "; // codigo interno
                    
                    //CODIGO INTERNO DEL CLIENTE
                    tipo = row.getCell(1).getCellType();
                    sql += "'" + ReadRow(tipo, row, 1).replace("'", "#") + "', "; //nit
                    
                    // NIT DEL CLIENTE
                    tipo = row.getCell(2).getCellType();
                    establecimiento = ReadRow(tipo, row, 2).replace("'", "#").trim();
                    sql += "'" + ReadRow(tipo, row, 2).replace("'", "#").trim() + "', "; // nombre del establecimiento
                    
                    // NOMBRE DEL ESTABLECIMIENTO
                    tipo = row.getCell(3).getCellType();
                    sql += "'" + ReadRow(tipo, row, 3).replace("'", "#") + "', "; // nombre del cliente
                    
                    //DIRECCION DEL CLIENTE
                    tipo = row.getCell(4).getCellType();  
                    sql += "'" + ReadRow(tipo, row, 4).replace("'", "#") + "', "; // direccion
                    
                    // BARRIO
                    tipo = row.getCell(5).getCellType();
                    sql += "'" + ReadRow(tipo, row, 5).replace("'", "#") + "', "; // barrio
                    
                    // CIUDAD
                    tipo = row.getCell(6).getCellType();
                    sql += "'" + ReadRow(tipo, row, 6).replace("'", "#") + "', ";//   ciudad
                    
                    //  CLASIFICACION DEL NEGOCIO
                    tipo = row.getCell(7).getCellType();
                    sql += "'" + ReadRow(tipo, row, 7).replace("'", "#") + "', "; // clasificacion
                    
                    // NUMERO DE LA FACTURA
                    tipo = row.getCell(8).getCellType();
                    factura = ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura 
                    
                    // CELULAR DEL CLIENTE
                    sql += "'0', "//celularCliente
                            
                    // EMAIL DEL CLIENTE
                            + "'no incluido', "//emailCliente
                            
                    // ZONA, REGIONAL Y AGENCIA
                            + ini.getUser().getZona() + ","//
                            + ini.getUser().getRegional() + ","//
                            + ini.getUser().getAgencia() + ","//
                            
                   // ACTIVO
                            + "'1','"//activo
                            
                   // USUARIO
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            
                   // FLAG
                            + "'1') ON DUPLICATE KEY UPDATE "
                            + "flag='-1';";
                    
                    // System.out.println("ingresar registro cliente # "  + ";(" + i + ") " + sql);
                   
                  
                    // GRABA UN REGISTRO
                    Statement st = null;
                    Connection con;
                    // CONEXION ESTATICA L SERVIDOR LOCAL
                    con = CConexionenBurbuja.GetConnection();
                    if (con != null) {
                        st = con.createStatement();
                        st.execute(sql);
                    }
                    st.close();
                    con.close();
                    
                    // INCREMENTA LOS CONTADORES
                    controladorDeCiclos++;
                      contadorDeFilas++;
                      contadorDeTodasLasFilas++;
                      
                      // ACTUALIZA LAS BARRAS DE PROGRESO
                     porcentajeBarraInferior=(int) (contadorDeFilas*100)/numeroFilas;
                     porcentajeBarraSuperior=(int) (contadorDeTodasLasFilas*100)/totalTodasLasFilas;
                                   
                        this.form.barraInferior.setValue(porcentajeBarraInferior);
                        this.form.barraInferior.repaint();
                        
                         this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.form.barraSuperior.repaint();
                        
                       Thread.sleep(1);
                       
                       sql2+=sql;
                       
                       if(controladorDeCiclos>=100){
                           controladorDeCiclos=0;
                            if(ini.insertarDatosLocalmente(sql2)){
                                sql2="";
                          }
                         
                       }
                       
                        System.out.println("lleva " +contadorDeFilas + "  inserciones  Clientes camdun " + new Date());
                  
                } catch (Exception ex) {
                    form.mensaje += "Error al insertar dato Cliente " + idCliente + " , establecimiento : " + establecimiento + " factuta # : " + factura + "\n";
                    form.txtErrores.setText(form.mensaje);
                    Logger.getLogger(JcArchivoExcelClienteCamdum_2.class.getName()).log(Level.SEVERE, null, ex);

                    System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") " + sql);
                     contadorDeFilas++;
                     contadorDeTodasLasFilas++;
                }

            } //  FIN DEL WHILE

            controladorDeCiclos = 0;
            if (ini.insertarMetodoPost(sql2)) {
                sql2 = "";
            }
          
            System.out.println("lleva " + contadorDeFilas  + "  inserciones  clientes camdun " + new Date());
            System.out.println("Termina  inserciones  " + new Date());
           
            fis.close();
            System.out.println("Success import excel to mysql table clientes Camdun ");
          
        } catch (Exception ex) {
            form.mensaje += "Error al insertar dato Cliente " + idCliente + " , establecimiento : " + establecimiento + " factuta # : " + factura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelClienteCamdum_2.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") " + sql);

            // band=true;
        }
    } // FIN DEL METODO ingresarClientesCamdun()
           
       private void insertarProductosCamdun() {
        String sql = null;
        String producto = null;
        String cliente = null;
        String factura = null;
         
         int numeroFilas;
         double contadorDeFilas=0;
        int porcentajeBarraInferior;
        
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            
            numeroFilas=sheet.getLastRowNum();
            
            rows = sheet.rowIterator();

            while (rows.hasNext()) {
                try {
                    XSSFRow row = ((XSSFRow) rows.next());
                    sql = "INSERT INTO `productosCamdun`(`codigoProducto`,`descripcionProducto`,`linea`,`valorUnitario`,"
                            + "`activo`,`usuario`,`flag`) VALUES(";
                    int tipo;
                    
                    // CODIGO DEL CLIENTE
                    tipo = row.getCell(0).getCellType();
                    cliente = ReadRow(tipo, row, 0).replace("'", "#").trim();
                    
                    // NUMERO DE LA FACTURA
                    tipo = row.getCell(8).getCellType();
                    factura = ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura 
                    
                    // CODIGO DEL PRODUCTO
                    tipo = row.getCell(10).getCellType(); // codigoProducto
                    producto = "" + ReadRow(tipo, row, 10);
                    sql += "'" + ReadRow(tipo, row, 10) + "',";
                    tipo = row.getCell(11).getCellType();
                    
                    //  DESCRIPCION DEL PRODUCTO
                    sql += "'" + ReadRow(tipo, row, 11) + "',";//descripcionProducto
                    tipo = row.getCell(12).getCellType();
                    
                    //  LINEA DEL PRODUCTO
                    sql += "'" + ReadRow(tipo, row, 12) + "',"; //linea
                    tipo = row.getCell(17).getCellType();
                    
                    // VALOR UNITARIO CON IVA DEL PRODUCTO
                    sql += "'" + ReadRow(tipo, row, 17) + "',"; //valorUnitario
                    
                    // ACTIVO
                    sql += "'1','"//activo
                            
                            // USUARIO
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            
                            // FLAG
                            + "'1') ON DUPLICATE KEY UPDATE "
                            + "`valorUnitario`='" + ReadRow(tipo, row, 17) + "',"
                            + "flag='-1';";
                    //System.out.println("datos a ingresar" +  ";(" + i + ") " + sql);
                  
                   
                    // GRABA UN REGISTRO
                    Statement st = null;
                    Connection con;
                    // CONEXION ESTATICA L SERVIDOR LOCAL
                    con = CConexionenBurbuja.GetConnection();
                    if (con != null) {
                        st = con.createStatement();
                        st.execute(sql);
                    }
                    st.close();
                    con.close();
                      
                      controladorDeCiclos++; 
                      contadorDeFilas++;
                      contadorDeTodasLasFilas++;
                      
                     porcentajeBarraInferior=(int) (contadorDeFilas*100)/numeroFilas;
                     porcentajeBarraSuperior=(int) (contadorDeTodasLasFilas*100)/totalTodasLasFilas;
                                   
                        this.form.barraInferior.setValue(porcentajeBarraInferior);
                        this.form.barraInferior.repaint();
                        
                         this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.form.barraSuperior.repaint();
                        
                       Thread.sleep(1);
                       
                         
                       sql2+=sql;
                       
                       if(controladorDeCiclos>=100){
                           controladorDeCiclos=0;
                            if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
                       }
                       
                        System.out.println("lleva " +contadorDeFilas + "  inserciones  Productos camdun " + new Date());
                    
                } catch (Exception ex) {
                    System.out.println("Error en insertar producto Camdun" + ex + ";(" +contadorDeFilas + ") " + sql);
                    form.mensaje += "Error al insertar dato Producto #  " + producto + "cliente :" + cliente + "  factura : " + factura + "\n";
                    form.txtErrores.setText(form.mensaje);
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }

            }// FIN DEL WHILE
            
             controladorDeCiclos = 0;
              if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
            sql2 = "";
            
            
            System.out.println("lleva " + contadorDeFilas + "  inserciones  productos Camdun " + new Date());
            fis.close();
            System.out.println("Success import excel to mysql table productos camdun");
           
        } catch (Exception ex) {
            form.mensaje += "Error al insertar dato Producto #  " + producto + "cliente :" + cliente + "  factura : " + factura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosCamdun_2.class.getName()).log(Level.SEVERE, null, ex + ";(" +contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar codigo de producto Camdun" + ex + ";(" +contadorDeFilas + ") " + sql);

        }
   } // FIN DEL METODO insertarProductosCamdun() 
       
       private void ingresarFacturasCamdun() {
        String sql = "";
        String factura = null;
        String cliente = null;
        
        int numeroFilas;
         double contadorDeFilas=0;
        int porcentajeBarraInferior;
        
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            
            numeroFilas=sheet.getLastRowNum();
            
            rows = sheet.rowIterator();

            while (rows.hasNext()) {
                try{
                    XSSFRow row = ((XSSFRow) rows.next());
                    int tipo;
                    
                    sql=  "INSERT INTO `facturasCamdun`(`numeroFactura`,`fechaDeVenta`,`cliente`,`vendedor`,"
                            + "`valorFacturaSinIva`,`valorIvaFactura`,`valorTotalFactura`,`zona`,`regional`,`agencia`,"
                            + "`activo`,`usuario`,`flag`,`canal`)VALUES (";
                    
                   
                    
                    // NUMERO DE LA FACTURA
                    int val=(int)row.getCell(8).getNumericCellValue();// numeroFactura
                   String valor=  String.valueOf(val).trim();
                    factura=valor;
                    sql+= "'" + valor + "',";
                    
                    
                    //FECHA DE VENTA
                    tipo=row.getCell(9).getCellType(); //fechaDeVenta
                    sql+= "'" + ReadRow(tipo, row, 9).trim()+ "',";
                    
                     // CODIGO DEL CLIENTE
                    tipo=row.getCell(0).getCellType();
                    cliente= ReadRow(tipo, row, 0).replace("'", "#").trim();
                     val=(int)row.getCell(0).getNumericCellValue(); //cliente
                    valor=  String.valueOf(val).trim();
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
                    
                   
                   // GRABA UN REGISTRO
                    Statement st = null;
                    Connection con;
                    // CONEXION ESTATICA L SERVIDOR LOCAL
                    con = CConexionenBurbuja.GetConnection();
                    if (con != null) {
                        st = con.createStatement();
                        st.execute(sql);
                    }
                    st.close();
                    con.close();
                    
                    
                    // INCREMENTA LOS CONTADORES
                     controladorDeCiclos++;  
                     contadorDeFilas++;
                      contadorDeTodasLasFilas++;
                      
                      // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                     porcentajeBarraInferior=(int) (contadorDeFilas*100)/numeroFilas;
                     porcentajeBarraSuperior=(int) (contadorDeTodasLasFilas*100)/totalTodasLasFilas;
                                   
                        this.form.barraInferior.setValue(porcentajeBarraInferior);
                        this.form.barraInferior.repaint();
                        
                         this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.form.barraSuperior.repaint();
                        
                       Thread.sleep(1);
                       
                        sql2+=sql;
                       
                       if(controladorDeCiclos>=100){
                           controladorDeCiclos=0;
                           if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
                       }
                       
                        System.out.println("lleva " +contadorDeFilas + "  inserciones  facturas camdun " + new Date());
                                
                }catch(Exception ex){
                    form. mensaje+="Error al insertar dato Factura # " + factura + " cliente : " + cliente + "\n" ;
                    form.txtErrores.setText(form.mensaje);
                    Logger.getLogger(JcArchivoExcelFacturasCamdun_2.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas+ ") " + sql);
                    System.out.println("Error en insertat facturas Camdun" + ex + ";(" +contadorDeFilas+ ") " + sql);
                  contadorDeFilas++;
                  contadorDeTodasLasFilas++;
                     
                }
              
            } //FIN DEL WHILE
 
            fis.close();
            
              controladorDeCiclos = 0;
             if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
            
            System.out.println("lleva " +contadorDeFilas + "  inserciones  facturas camdun " + new Date());
            System.out.println("Success import excel  to mysql table facturas Camdun");
           
        } catch (Exception ex) {
            form. mensaje+="Error al insertar dato Factura # " + factura + " cliente : " + cliente + "\n" ;
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelFacturasCamdun_2.class.getName()).log(Level.SEVERE, null, ex + ";(" +contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas+ ") " + sql);
            
        }
    }

       private void ingresarProductosPorFactura() {
      
        String sql = null;
        String cliente = null;
        String factura = null;
        
        
      int numeroFilas;
         double contadorDeFilas=0;
        int porcentajeBarraInferior;
        
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            
            numeroFilas=sheet.getLastRowNum();
            
             rows = sheet.rowIterator();
            
            while (rows.hasNext()) {
                try {
                   
                    XSSFRow row = ((XSSFRow) rows.next());

                    sql = "INSERT INTO `productosPorFactura`(`factura`,`codigoProducto`,`cantidad`,`valorUnitarioConIva`,`valorTotalConIva`,"
                            + "`activo`,`usuario`,`flag`) VALUES(";
                    int tipo;
                    //  ID DEL CLIENTE
                    tipo = row.getCell(0).getCellType();
                    cliente = ReadRow(tipo, row, 0).replace("'", "#").trim();
                    
                    // NUMERO DE LA FACTURA
                    tipo = row.getCell(8).getCellType();
                    factura = ReadRow(tipo, row, 8).replace("'", "#"); // # de la factura                 
                    int val = (int) row.getCell(8).getNumericCellValue();
                    String valor = String.valueOf(val);
                    factura = valor;
                    sql += "'" + valor + "',";
                    
                    // CODIGO DEL PRODUCTO
                    val = (int) row.getCell(10).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";
                    
                    // CANTIDAD DE PRODUCTO
                    val = (int) row.getCell(16).getNumericCellValue();
                    valor = String.valueOf(val);
                    sql += "'" + valor + "',";
                    
                    // VALOR UNITARIO CON IVA
                    sql += "'" + row.getCell(17).getNumericCellValue() + "',";
                    
                    // VALOR TOTAL CON  IVA
                    sql += "'" + row.getCell(18).getNumericCellValue() + "',";
                    
                    // ACTIVO
                    sql += "'1','"//activo
                            
                            // USUARIO
                            + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "'," //"'" + this.get + "', "//usuario
                            
                            // FLAG
                            + "'1') ON DUPLICATE KEY UPDATE "
                            + "flag='-1';";
                                    
                        
                      
                   // GRABA UN REGISTRO
                    Statement st = null;
                    Connection con;
                    // CONEXION ESTATICA L SERVIDOR LOCAL
                    con = CConexionenBurbuja.GetConnection();
                    if (con != null) {
                        st = con.createStatement();
                        st.execute(sql);
                    }
                    st.close();
                    con.close();
                    
                    // INCREMENTA LOS CONTDORES
                    controladorDeCiclos++;
                     contadorDeFilas++;
                      contadorDeTodasLasFilas++;
                      
                      
                      // ACTUALIZA LAS BARRAS DE PROGRESO
                     porcentajeBarraInferior=(int) (contadorDeFilas*100)/numeroFilas;
                     porcentajeBarraSuperior=(int) (contadorDeTodasLasFilas*100)/totalTodasLasFilas;
                                   
                        this.form.barraInferior.setValue(porcentajeBarraInferior);
                        this.form.barraInferior.repaint();
                        
                         this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.form.barraSuperior.repaint();
                        
                    Thread.sleep(1);
                    
                      sql2+=sql;
                       
                       if(controladorDeCiclos>=100){
                           controladorDeCiclos=0;
                            if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
                       }
                    
                       System.out.println("lleva " +contadorDeFilas+ "  inserciones prodcutos por factura " + new Date());

                } catch (Exception ex) {
                    System.out.println("Error en insertar productos por factura Camdun" + ex + ";(" + contadorDeFilas+ ") " + sql);
                    form.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
                    form.txtErrores.setText(form.mensaje);
                   contadorDeFilas++;
                   contadorDeTodasLasFilas++;
                }
               
            } //  FIN DEL WHILE
            
            System.out.println("lleva " +contadorDeFilas+ "  inserciones prodcutos por factura " + new Date());
           
            fis.close();
            
             controladorDeCiclos = 0;
              if(ini.insertarMetodoPost(sql2)){
                                sql2="";
                          }
            
            System.out.println("Success import excel to mysql table productos por Factura");

        } catch (Exception ex) {
            form.mensaje += "Error al insertar dato producto en la factura # " + factura + "cliente " + cliente + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_3.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" +contadorDeFilas+ ") " + sql);

            band = true;
        }
        
    }

    @Override
    public void run() {
        ingresarDatos();
    }

    public String ReadRow(int tipo, XSSFRow row, int indice) {
        String valor = null;
        
       //  codigo tomado de http://recetasdeprogramacion.blogspot.com/2013/05/generar-ficheros-ms-excel.html  para campos de fecha
        switch (tipo) {
            // TIPO DE DATO NUMERICO
            case 0: { 
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
                // TIPO DE DATO CADENA
            case 1: {
              
                valor = row.getCell(indice).getStringCellValue();
            }
            break;
                // TIPO DE CELDA ES FORMULA
            case 2: {
                
                //valor=row.getCell(indice).getCellFormula().toString();
                valor = row.getCell(indice).getStringCellValue();
            }
            break;
                // CELDA VACIA O EN BLANCO
            case 3: {
  
                valor = row.getCell(indice).getStringCellValue() + "";
            }
            break;
                // TIPO DE CELDA BOOLEANO
            case 4: {
              
                valor = row.getCell(indice).getBooleanCellValue() + "";
            }
            break;
                // TIPO DE CELDA BOOLEANO
            case 5: {
              
                valor = row.getCell(indice).getErrorCellString() + "";
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
