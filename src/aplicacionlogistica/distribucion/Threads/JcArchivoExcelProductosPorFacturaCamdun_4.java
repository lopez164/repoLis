/*
 * 
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * este objeto permite tomar desde un archivo de excel, toda la información requerida para
 * poder realizar la operación de distribución, ya que la información que contiene permite 
 * crear y actualizar los siguientes objetos:
 * 1. Clientes
 * 2. Productos
 * 3. Facturas
 * 4. Los productos asigados a casa factura
 * 
 *
 * @author Luis Eduardo López Casanova
 */
public class JcArchivoExcelProductosPorFacturaCamdun_4 implements Runnable {

    File file;
    FImportarArchivoExcel form;
    Inicio ini = null;
  
    public int numHojas;
    public int numrows;
    public int numcolumns;
    public Object[] tipos = null;
    private XSSFWorkbook workbook;
    private Iterator rows;

    int controladorDeCiclos = 0;
    String sql2 = "";
    int totalTodasLasFilas = 0;
    double contadorDeTodasLasFilas = 0;
    int porcentajeBarraSuperior;
    ArrayList<String> sqlInsercionRemota= null;
     
   String  ruta ;//= "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_clientes.sql";
             
    
    ArrayList<CClientes> listaDeClientes;
    ArrayList<CProductos> listaDeProductos;
    ArrayList<CFacturas> listaDeFacturas;
    ArrayList<CProductosPorFactura> listaDeProductosorFactura;
    
/**
     * Método constructor de la clase
     * 
     *  
     * @param tiempo  no hace absolutamente nada
     * @param form corrsponde al formulario desde donde fue instanciada la clase, con el fin de poder 
     * controlar las barras de progreso
     * @param file corresponde a un archivo en excel, en el cual viene toda la información de las ventas
     * más recientes para  poder actualizar el sistema
     * @param ini corresponde a la clase Inicio en la cual está la configuración del sistema
     */ 
    public JcArchivoExcelProductosPorFacturaCamdun_4(int tiempo, FImportarArchivoExcel form, File file, Inicio ini) {
        this.form = form;
        this.file = file;
        this.ini = ini;
        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0,this.file.getName().indexOf(".")) + ".sql";
            
        

    }
    
    
    /**
     
     * Método que nos permite llamar otros métodos para la inserción de los
     * datos
     *
     *
     */
    public void ingresarDatos() {
        try {
            
            form.lblBarraDeProgreso.setVisible(true);
            ingresarClientesCamdun();
            //this.form.barraSuperior.setValue(25);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            insertarProductosCamdun();
           // this.form.barraSuperior.setValue(50);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            ingresarFacturasCamdun();
            //this.form.barraSuperior.setValue(75);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            ingresarProductosPorFactura();
           // this.form.barraSuperior.setValue(100);
            this.form.barraSuperior.repaint();
            Thread.sleep(1);
           
            /* el 50% del proceso restante*/
            totalTodasLasFilas=0;
            
           totalTodasLasFilas+=listaDeClientes.size();
           totalTodasLasFilas+=listaDeProductos.size();
           totalTodasLasFilas+=listaDeFacturas.size();
           totalTodasLasFilas+=listaDeProductosorFactura.size();
           
           sqlInsercionRemota=new ArrayList<>();
           
           for(CClientes obj : listaDeClientes){
               sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
           }
           
            insertarBBDDRemota(sqlInsercionRemota);
            sqlInsercionRemota=new ArrayList<>();
           
           for(CProductos obj : listaDeProductos){
                sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
           }
           
           
            insertarBBDDRemota(sqlInsercionRemota);
            sqlInsercionRemota=new ArrayList<>();
           
           for(CFacturas obj : listaDeFacturas){
                sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
           }
           
           
            insertarBBDDRemota(sqlInsercionRemota); // 3114538155
            sqlInsercionRemota=new ArrayList<>();
           
           for(CProductosPorFactura obj : listaDeProductosorFactura){
                sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
           }

           
            insertarBBDDRemota(sqlInsercionRemota);
            sqlInsercionRemota=new ArrayList<>();
           
           
        //insertarBBDDRemota(sqlInsercionRemota);
         form.lblBarraDeProgreso.setVisible(false);
        
           JOptionPane.showMessageDialog(this.form, "Proceso de importacion de datos desde el archivo " +this.file.getName()   +"  finalizado", "Final del proceso",  JOptionPane.INFORMATION_MESSAGE, null);
           //JOptionPane.showMessageDialog(this.form, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
           
          
        
        } catch (InterruptedException ex) {
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("SleepWhileInLoop")
     /**
     * Método que realiza la inserción de los datos en la BBDD local y en la BBDD remota correspondientes
     * a la tabla de clientes camdun, los cuales provienen de un archivo en excel tomado previamente
     * en los parámetros del método constructor."
     * 
      */
    private void ingresarClientesCamdun() {
        String sql = "";
          String numeroFactura = null;
          CFacturas factura;
       
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
        CClientes cliente = null;

        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);

            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;
            contadorDeTodasLasFilas = 0;

            //numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();

            System.out.println("inicio inserciones  " + new Date());
            sqlInsercionRemota = new ArrayList<>();
            
             listaDeClientes  = new ArrayList<>();
             ArrayList<CClientes> auxListaDeClientes  = new ArrayList<>();
           
             
             ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
             archivo.borrarArchivo();
   

            while (rows.hasNext()) {
                
                /*  INCREMENTA LOS CONTADORES  */
                    controladorDeCiclos++;
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                    
                try {
                    XSSFRow row = ((XSSFRow) rows.next());
                    
                    cliente = new CClientes(ini);
                    factura= new CFacturas(ini);
                    
                    int tipo;
                    tipo = row.getCell(8).getCellType();
                   // numeroFactura = "" + (int) row.getCell(8).getNumericCellValue();
                    
                    
                    factura.setNumeroDeFactura(ReadRow(tipo, row, 8).replace("'", "#")); 
                    
                   
                    
                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        tipo = row.getCell(0).getCellType();
                        cliente.setCodigoInterno(ReadRow(tipo, row, 0).replace("'", "#"));

                        if (!cliente.getCodigoInterno().startsWith("ANULA")) {
                            tipo = row.getCell(0).getCellType();
                            cliente.setCodigoInterno(ReadRow(tipo, row, 0).replace("'", "#").trim());

                            tipo = row.getCell(1).getCellType();
                            cliente.setNitCliente(ReadRow(tipo, row, 1).replace("'", "#"));

                            tipo = row.getCell(2).getCellType();
                            cliente.setNombreEstablecimiento(ReadRow(tipo, row, 2).replace("'", "#").trim());

                            tipo = row.getCell(3).getCellType();
                            cliente.setNombreDeCliente(ReadRow(tipo, row, 3).replace("'", "#"));

                            tipo = row.getCell(4).getCellType();
                            cliente.setDireccion(ReadRow(tipo, row, 4).replace("'", "#"));

                            tipo = row.getCell(5).getCellType();
                            cliente.setBarrio(ReadRow(tipo, row, 5).replace("'", "#"));

                            tipo = row.getCell(6).getCellType();
                            cliente.setCiudad(ReadRow(tipo, row, 6).replace("'", "#"));

                            tipo = row.getCell(7).getCellType();
                            cliente.setClasificacion(ReadRow(tipo, row, 7).replace("'", "#"));

                            cliente.setCelularCliente("0");
                            cliente.setEmailCliente("NO INCLUIDO");
                            cliente.setFechaDeIngresoCliente(ini.getFechaActualServidor());
                            cliente.setActivoCliente(1);
                            cliente.setLatitud("0");
                            cliente.setLongitud("0");
                            cliente.setCanalDeVenta(1);
                            cliente.setRuta(1);
                            cliente.setFrecuencia(1);
                            cliente.setZona(ini.getUser().getZona());
                            cliente.setRegional(ini.getUser().getRegional());
                            cliente.setAgencia(ini.getUser().getAgencia());
                            cliente.setPorcentajeDescuento(0.0);
                            cliente.setActivoCliente(1);
                           
                       
                             /* EL SISTEMA INFORMA  LA CANTIDAD DE REGISTROS INSERTADOS A LA BBDD */
                            auxListaDeClientes.add(cliente);
                            System.out.println("lleva " + contadorDeFilas + "  inserciones  Clientes camdun " + new Date());

                        }

                        
                    }

                  

                    /* ACTUALIZA LAS BARRAS DE PROGRESO */
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    //porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                     porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 50) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                    Thread.sleep(1);
                                   

                } catch (Exception ex) {
                    form.mensaje += "Error al insertar dato Cliente " + cliente.getNombreDeCliente() + " , establecimiento : " + cliente.getNombreEstablecimiento() + " factuta # : " + numeroFactura + "\n";
                    form.txtErrores.setText(form.mensaje);
                    System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") " + sql);
                    Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex);

                  
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                     Thread.sleep(1);

            } //  FIN DEL WHILE
             fis.close();
        
            /* una vez se hace el recorrido del archivo empezamos a depurar el archivo 
             para que al ejecutar la sentencia sql no se repitan los registros y sea 
             már rapido la ejecución 
             */
           contadorDeFilas=0;
           numeroFilas=auxListaDeClientes.size();
           porcentajeBarraInferior = (int) ((contadorDeFilas * 100) / numeroFilas);
           
           /* Se actualizan las tablas clientes, roductos, facturas y productos por
           factura
           */

           sqlInsercionRemota.add("update clientescamdun set flag='-1'");
           sqlInsercionRemota.add("update productoscamdun set flag='-1'");
           sqlInsercionRemota.add("update facturascamdun set flag='-1'");
           sqlInsercionRemota.add("update productosporfactura set flag='-1'");
           
           Inicio.GuardaConsultaEnFichero("update clientescamdun set flag='-1'", ruta);
           Inicio.GuardaConsultaEnFichero("update productoscamdun set flag='-1'", ruta);
           Inicio.GuardaConsultaEnFichero("update facturascamdun set flag='-1'", ruta);
           Inicio.GuardaConsultaEnFichero("update productosporfactura set flag='-1'", ruta);
           
             for (CClientes obj : auxListaDeClientes) {
                boolean aparece = false;
                if (listaDeClientes.isEmpty()) {
                    listaDeClientes.add(obj);
                     sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                     Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                } else {
                    
                    aparece = false;
                    for (CClientes obj2 : listaDeClientes) {
                        if (obj.getCodigoInterno().equals( obj2.getCodigoInterno())) {
                            aparece = true;
                            break;
                           
                        }
                    }
                    if (!aparece) {
                        listaDeClientes.add(obj);
                        sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                        // obj.grabarClientes();
                    }
                }
                contadorDeFilas++;
                 porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                 this.form.barraInferior.setValue(porcentajeBarraInferior);
                 this.form.barraInferior.repaint();
            }
           
           /* Se guardan los datos localmente */
           ini.insertarDatosLocalmente(sqlInsercionRemota);
           
            sqlInsercionRemota=null;
           controladorDeCiclos = 0;
        
            
            /* EL SISTEMA DA INFORMACION DE LA CANTIDAD DE REGISTROS PROCESADOS */
            System.out.println("lleva " + contadorDeFilas + "  inserciones  clientes camdun " + new Date());
            System.out.println("Termina  inserciones  de Clientes " + new Date());           
          
        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Cliente " + cliente.getNombreDeCliente() + " , establecimiento : " + cliente.getNombreEstablecimiento() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") " + sql);

          
        }
    } /*  FIN DEL METODO ingresarClientesCamdun() */
    
    

      /**
     * Método que realiza la inserción de los datos en la BBDD local y en la BBDD remota correspondientes
     * a la tabla de productoscamdun, los cuales provienen de un archivo en excel tomado previamente
     * en los parámetros del método constructor."
     * 
      */
    private void insertarProductosCamdun() {

        
        String numeroFactura = null;
        CProductos producto = null;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
        CFacturas factura;

        try {
            FileInputStream fileImputStream;
            fileImputStream = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fileImputStream);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            
              listaDeProductos  = new ArrayList<>();
              ArrayList<CProductos> auxListaDeProductos  = new ArrayList<>();
    

            numeroFilas = sheet.getLastRowNum();

            rows = sheet.rowIterator();
          
            sqlInsercionRemota= new ArrayList<>();
            
             String ruta;
             //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_productos.sql";
             // ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0,this.file.getName().indexOf(".")) + ".sql";
             //ArchivosDeTexto archivo= new ArchivosDeTexto(ruta);
           // archivo.borrarArchivo();
            
            while (rows.hasNext()) {
                
                /*  INCREMENTA LOS CONTADORES  */
                    controladorDeCiclos++;
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                    
                
                try {
                    XSSFRow row = ((XSSFRow) rows.next());

                    producto = new CProductos(ini);
                    factura= new CFacturas(ini);
                    
                      int tipo;
                    tipo = row.getCell(8).getCellType();
                    numeroFactura = "" + (int) row.getCell(8).getNumericCellValue();
                    
                    
                    factura.setNumeroDeFactura(ReadRow(tipo, row, 8).replace("'", "#"));
                    
                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        tipo = row.getCell(0).getCellType();
                        factura.setCodigoDeCliente(ReadRow(tipo, row, 0).replace("'", "#"));

                        if (!factura.getCodigoDeCliente().startsWith("ANULA")) {
                            
                            tipo = row.getCell(10).getCellType();
                            producto.setCodigoProducto(ReadRow(tipo, row, 10));

                            tipo = row.getCell(11).getCellType();
                            producto.setDescripcionProducto(ReadRow(tipo, row, 11));

                            tipo = row.getCell(12).getCellType();
                            producto.setLinea(ReadRow(tipo, row, 12));

                            producto.setValorUnitarioSinIva(0.0);

                            tipo = row.getCell(17).getCellType();
                            producto.setValorUnitarioConIva(Double.parseDouble(ReadRow(tipo, row, 17)));

                            producto.setIsFree(1);
                            producto.setActivo(1);

                            auxListaDeProductos.add(producto);
                             System.out.println("lleva " + contadorDeFilas + "  inserciones  Productos camdun " + new Date());

                        }

 
                    }

                    /* Se actualizan las barras de progreso del formulario */
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                   // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                     porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 50) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                    Thread.sleep(1);

                } catch (Exception ex) {
                    System.out.println("Error en insertar producto Camdun" + ex + ";(" + contadorDeFilas + ") ");
                    form.mensaje += "Error al insertar dato Producto #  " + producto.getDescripcionProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
                    form.txtErrores.setText(form.mensaje);
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                     Thread.sleep(1);

            }// FIN DEL WHILE
            
            fileImputStream.close();
            
           /* una vez se hace el recorrido del archivo empezamos a depurar el archivo  para que al ejecutar
            * la sentencia sql no se repitan los registros y sea már rapido la ejecución */
            contadorDeFilas=0;
           numeroFilas=auxListaDeProductos.size();
            porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
            for (CProductos obj : auxListaDeProductos) { 
                boolean aparece;
                if (listaDeProductos.isEmpty()) {
                    listaDeProductos.add(obj);
                     sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                     Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), this.ruta);
                } else {
                    
                    aparece = false;
                    for (CProductos obj2 : listaDeProductos) {
                        if (obj.getCodigoProducto().equals( obj2.getCodigoProducto())) {
                            aparece = true;
                            break;
                        }
                    }
                    if (!aparece) {
                        listaDeProductos.add(obj);
                        sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), this.ruta);
                        obj.grabarProductos();
                    }
                }
                 contadorDeFilas++;
                 porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                 this.form.barraInferior.setValue(porcentajeBarraInferior);
                 this.form.barraInferior.repaint();
            }
            
             ini.insertarDatosLocalmente(sqlInsercionRemota);
           
             controladorDeCiclos = 0;
             sqlInsercionRemota=null;

            System.out.println("lleva " + contadorDeFilas + "  inserciones  productos Camdun " + new Date());
            System.out.println("Success import excel to mysql table productos camdun");

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Producto #  " + producto.getCodigoProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar codigo de producto Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }
    } // FIN DEL METODO insertarProductosCamdun() 
    
    
      /**
     * Método que realiza la inserción de los datos en la BBDD local y en la BBDD remota correspondientes
     * a la tabla de facturascamdun, los cuales provienen de un archivo en excel tomado previamente
     * en los parámetros del método constructor."
     * 
      */
    private void ingresarFacturasCamdun() {
        
       CFacturas factura = null;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
        String numeroFactura;

        try {
            
             listaDeFacturas  = new ArrayList<>();
            ArrayList<CFacturas> auxListaDeFacturas  = new ArrayList<>();
 
            
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            numeroFilas = sheet.getLastRowNum();

            rows = sheet.rowIterator();
           
              sqlInsercionRemota= new ArrayList<>();
            
             //String ruta;// = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_facturas.sql";
            //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0,this.file.getName().indexOf(".")) + ".sql";
            // ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
           // archivo.borrarArchivo();
            
             int tipo;
            while (rows.hasNext()) {
                
                /*  INCREMENTA LOS CONTADORES  */
                    controladorDeCiclos++;
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                
                try {
                    XSSFRow row = ((XSSFRow) rows.next());
                    factura= new CFacturas(ini);
                    
                    tipo = row.getCell(8).getCellType();
                    numeroFactura = "" + (int)row.getCell(8).getNumericCellValue();
                    
                    
                    factura.setNumeroDeFactura(ReadRow(tipo, row, 8).replace("'", "#"));
                    
                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        //tipo = row.getCell(0).getCellType();
                        
                        factura.setCodigoDeCliente("" + (int) row.getCell(0).getNumericCellValue());
                        
                        if (!factura.getCodigoDeCliente().startsWith("ANULA")) {
                            
                            tipo = row.getCell(9).getCellType();
                            Date fech ;
                            fech = new Date();
                            fech = Inicio.getFechaSql(ReadRow(tipo, row, 9).trim());
                            factura.setFechaDeVenta("" + fech);

                            if (factura.getFechaDeVenta() != null) {

                                //tipo = row.getCell(8).getCellType();
                                
                                factura.setNumeroDeFactura("" + (int) row.getCell(8).getNumericCellValue());

                                tipo = row.getCell(0).getCellType();
                                factura.setCodigoDeCliente(ReadRow(tipo, row, 0).replace("'", "#").trim());

                                /*Direccion de la Factura*/
                                tipo = row.getCell(4).getCellType();
                                factura.setDireccion(ReadRow(tipo, row, 4).replace("'", "#"));

                                /*Barrio*/
                                tipo = row.getCell(5).getCellType();
                                factura.setBarrio(ReadRow(tipo, row, 5).replace("'", "#").trim());
                                
                                /*ciudad*/
                                tipo = row.getCell(6).getCellType();
                                factura.setCiudad(ReadRow(tipo, row, 6).replace("'", "#").trim());


                                /*Telefono*/
                                factura.setTelefono("0");

                                tipo = row.getCell(19).getCellType();
                                factura.setVendedor(ReadRow(tipo, row, 19).trim());

                                factura.setFormaDePago("CONTADO");
                                factura.setCanal(1);
                                factura.setValorFacturaSinIva(row.getCell(13).getNumericCellValue());
                                factura.setValorIvaFactura(row.getCell(14).getNumericCellValue());

                                double suma;
                                suma = row.getCell(13).getNumericCellValue() + row.getCell(14).getNumericCellValue();
                                factura.setValorTotalFactura(suma);

                                factura.setValorRechazo(0.0);
                                factura.setValorDescuento(0.0);
                                factura.setValorTotalRecaudado(0.0);
                                //factura.setimagenFactura();
                                //factura.setFormato();
                                factura.setZona(ini.getUser().getZona());
                                factura.setRegional(ini.getUser().getRegional());
                                factura.setAgencia(ini.getUser().getAgencia());
                                factura.setIsFree(1);
                                factura.setActivoFactura(1);
                                

                               
                                auxListaDeFacturas.add(factura);

                                System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas camdun " + new Date());

                            }
                        }
                        /* SI LA FACTURA ESTA ANULADA*/
                        else{
                            tipo = row.getCell(9).getCellType();
                            Date fech = new Date();
                            fech = Inicio.getFechaSql(ReadRow(tipo, row, 9).trim());
                            factura.setFechaDeVenta("" + fech);


                                //tipo = row.getCell(8).getCellType();
                                
                                factura.setNumeroDeFactura("" + (int) row.getCell(8).getNumericCellValue());

                                //tipo = row.getCell(0).getCellType();
                                factura.setCodigoDeCliente(""+ 0);

                                //tipo = row.getCell(19).getCellType();
                                factura.setVendedor("FACTURA ANULADA");

                                factura.setFormaDePago("CONTADO");
                                factura.setCanal(1);
                                factura.setValorFacturaSinIva(0.0);
                                factura.setValorIvaFactura(0.0);

                                factura.setValorTotalFactura(0.0);

                                factura.setValorRechazo(0.0);
                                factura.setValorDescuento(0.0);
                                factura.setValorTotalRecaudado(0.0);
                               
                                factura.setZona(ini.getUser().getZona());
                                factura.setRegional(ini.getUser().getRegional());
                                factura.setAgencia(ini.getUser().getAgencia());
                                factura.setIsFree(1);
                                factura.setActivoFactura(0);
                                

                               
                                auxListaDeFacturas.add(factura);

                                System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas camdun (ANULADA) " + new Date());
                            
                        }

                    }
  
                    // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                   // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                     porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 50) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();
 
                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                  

                } catch (Exception ex) {
                    form.mensaje += "Error al insertar dato Factura # " + factura.getNumeroDeFactura() + " cliente : " + factura.getCodigoDeCliente() + "\n";
                    form.txtErrores.setText(form.mensaje);
                    Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
                    System.out.println("Error en insertar facturas Camdun " + ex + ";(" + contadorDeFilas + ") ");
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;

                }
                     Thread.sleep(1);

            } //FIN DEL WHILE

            fis.close();
            
            /* una vez se hace el recorrido del archivo empezamos a depurar el archivo  para que al ejecutar
             * la sentencia sql no se repitan los registros y sea már rapido la ejecución
            */
            listaDeFacturas = new ArrayList<>();
            contadorDeFilas=0;
           numeroFilas=auxListaDeFacturas.size();
            porcentajeBarraInferior = (int) ((contadorDeFilas * 100) / numeroFilas);
            for (CFacturas obj : auxListaDeFacturas) {
                boolean aparece = false;
                if (listaDeFacturas.isEmpty()) {
                    listaDeFacturas.add(obj);
                     sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                     Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                } else {

                    aparece = false;
                    for (CFacturas obj2 : listaDeFacturas) {
                        if (obj.getNumeroDeFactura().equals(obj2.getNumeroDeFactura())) {
                            aparece = true;
                            break;
                        }
                    }
                    if (!aparece) {
                        listaDeFacturas.add(obj);
                        sqlInsercionRemota.add(obj.getSentenciaInsertSQL());
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                                            }
                }
                 contadorDeFilas++;
                 porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                 this.form.barraInferior.setValue(porcentajeBarraInferior);
                 this.form.barraInferior.repaint();
            }
            
            
            ini.insertarDatosLocalmente(sqlInsercionRemota);
            sqlInsercionRemota = null;
            controladorDeCiclos = 0;
          

            System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas camdun " + new Date());
            System.out.println("Success import excel  to mysql table facturas Camdun");

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Factura # " + factura.getNumeroDeFactura() + " cliente : " + factura.getCodigoDeCliente() + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }
    }
    
    
      /**
     * Método que realiza la inserción de los datos en la BBDD local y en la BBDD remota correspondientes
     * a la tabla de productospor factura, los cuales provienen de un archivo en excel tomado previamente
     * en los parámetros del método constructor."
     * 
      */
    private void ingresarProductosPorFactura() {

       
        String numeroFactura = null;

        CProductosPorFactura productoPorFactura = null;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
         listaDeProductosorFactura = new ArrayList<>();
         ArrayList<CProductosPorFactura> auxListaDeProductosorFactura = new ArrayList<>();
        FileInputStream fis;
         String ruta;
          ArchivosDeTexto archivo;
          int tipo;

        try {
            
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            numeroFilas = sheet.getLastRowNum();
            rows = sheet.rowIterator();
            sqlInsercionRemota = new ArrayList<>();

            //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_productosPorFactura.sql";
             //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0,this.file.getName().indexOf(".")) + ".sql";
            //archivo = new ArchivosDeTexto(ruta);
            //archivo.borrarArchivo();
            
            while (rows.hasNext()) {
                
                /*  INCREMENTA LOS CONTADORES  */
                    controladorDeCiclos++;
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                
                try {

                    XSSFRow row = ((XSSFRow) rows.next());
                    CFacturas factura = new CFacturas(ini);
                    
                    productoPorFactura = new CProductosPorFactura(ini);
                    tipo = row.getCell(8).getCellType();
                    numeroFactura = "" + (int) row.getCell(8).getNumericCellValue();
                                        
                    factura.setNumeroDeFactura(ReadRow(tipo, row, 8).replace("'", "#"));
                
                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        tipo = row.getCell(0).getCellType();
                        factura.setCodigoDeCliente(ReadRow(tipo, row, 0).replace("'", "#"));

                        if (!factura.getCodigoDeCliente().startsWith("ANULA")) {

                            tipo = row.getCell(8).getCellType();
                            productoPorFactura.setNumeroFactura(ReadRow(tipo, row, 8).replace("'", "#"));

                            double val =  row.getCell(10).getNumericCellValue();
                            String valor = String.valueOf(val);
                            productoPorFactura.setCodigoProducto(valor);

                            val = (int) row.getCell(16).getNumericCellValue();
                            productoPorFactura.setCantidad(val);

                            productoPorFactura.setValorUnitarioSinIva(0.0);
                            productoPorFactura.setValorUnitarioConIva(0.0);
                            productoPorFactura.setValorUnitarioConIva(row.getCell(17).getNumericCellValue());
                           // productoPorFactura.setValorUnitarioTotalConIva(row.getCell(18).getNumericCellValue());
                            productoPorFactura.setActivo(1);
                           

                            productoPorFactura.grabarProductosPorFactura();
                            auxListaDeProductosorFactura.add(productoPorFactura);
                            sqlInsercionRemota.add(productoPorFactura.getSentenciaInsertSQL());
                            Inicio.GuardaConsultaEnFichero(productoPorFactura.getSentenciaInsertSQL(), this.ruta);
                            listaDeProductosorFactura.add(productoPorFactura);
                            
                             System.out.println("lleva  " + contadorDeFilas + "  inserciones prodcutos por factura " + new Date());
                           
                            
                        }else{
                             form.mensaje += "El registo  de la fila  " + contadorDeFilas + ", se encuentra ANULADA, # factura  " + factura.getNumeroDeFactura() + "\n";
                             form.txtErrores.setText(form.mensaje);
                           System.out.println("El registo  de la fila  " + contadorDeFilas + ", se encuentra ANULADA, # factura  " + factura.getNumeroDeFactura()  + new Date());
                        }

                    } else {
                        form.mensaje += "El registo  de la fila  " + contadorDeFilas + ", se encuentra vacia, no tiene numero de  factura  " + new Date() + "\n";
                        form.txtErrores.setText(form.mensaje);
                        System.out.println("El registo  de la fila  " + contadorDeFilas + ", se encuentra vacia, no tiene numero de  factura  " + new Date());
                    }

                    // ACTUALIZA LAS BARRAS DE PROGRESO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                   // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                     porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 50) / totalTodasLasFilas;
   
                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                  
                   

                } catch (Exception ex) {
                    System.out.println("Error en insertar productos por factura Camdun" + ex + ";(" + contadorDeFilas + ") ");
                    form.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getNumeroFactura() + "producto " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
                    form.txtErrores.setText(form.mensaje);
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                  Thread.sleep(1);

            } //  FIN DEL WHILE
            
            fis.close();

            System.out.println("lleva " + contadorDeFilas + "  inserciones prodcutos por factura " + new Date());

              
                      
             this.form.barraSuperior.setValue(50);
             this.form.barraSuperior.repaint();
             
             
            
               
             this.form.barraInferior.setValue(0);
               this.form.barraInferior.repaint();
               
            ini.insertarDatosLocalmente(sqlInsercionRemota);
          // insertarBBDDRemota(sqlInsercionRemota);
           
           // this.form.barraSuperior.setValue(100);
            this.form.barraSuperior.repaint();
          
            sqlInsercionRemota=null;
            controladorDeCiclos = 0;
           

           
          
            
               System.out.println("Success import excel to mysql table productos por Factura");
              
        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getNumeroFactura() + "producto  " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(JcArchivoExcelProductosPorFacturaCamdun_4.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }

    }

    @Override
    public void run() {
        ingresarDatos();
    }

    
      /**
     * Método que lee las filas del archivo en excel donde viene toda la información
     * 
     * @param tipo corresponde al tipo de celda que fue leida, cadena, entero, fecha etc
     * @param row corresponde a la fila que se está leyendo
     * @param indice corresponde a la columna
     * 
     * @return devuelve una cadena con el valor contenido en la celda que se ha leido
      */
    public String ReadRow(int tipo, XSSFRow row, int indice) {
        String valor = null;

        //  codigo tomado de http://recetasdeprogramacion.blogspot.com/2013/05/generar-ficheros-ms-excel.html  para campos de fecha
        
        switch (tipo) {
            // TIPO DE DATO NUMERICO
            case 0: {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(indice))) {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
                    valor = ft.format(row.getCell(indice).getDateCellValue());
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
                if (valor == null) {
                    valor = "NA";
                }
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
            // TIPO DE CELDA ERRO
            case 5: {

                valor = row.getCell(indice).getErrorCellString() + "";
            }
            break;
            default : {

                valor =  "";
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
    
 public boolean insertarBBDDRemota(ArrayList<String> listaDeSentenciasSQL)  {
        boolean insertar = false;
        Connection con = null;
        Statement st = null;
        String cadena = null;
         double contadorDeFilas = 0;
         contadorDeTodasLasFilas=0;
        int porcentajeBarraInferior;
          int numeroFilas;


            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"JcArchivoExcelProductosPorFacturaCamdun_4");

            if (con != null) {
            try {
                st = con.createStatement();
                 contadorDeFilas=0;
                 numeroFilas=listaDeSentenciasSQL.size();
                 porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                
                for (String obj : listaDeSentenciasSQL) {
                    try {
                        st.execute(obj);
                        cadena = obj;
                        System.out.println("dato insertado servidor remoto -->" + obj);
                    } catch (Exception ex) {
                        System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                    }
                 contadorDeFilas++;
                 contadorDeTodasLasFilas ++;
                 porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                 this.form.barraInferior.setValue(porcentajeBarraInferior);
                 this.form.barraInferior.repaint();
                 
                 porcentajeBarraSuperior = (int) ((contadorDeTodasLasFilas * 50) / totalTodasLasFilas) + 50;
                 this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                 this.form.barraSuperior.repaint();
                                  
                }
                insertar = true;
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
            }
 
            }
 
        return insertar;
    }
    

}
