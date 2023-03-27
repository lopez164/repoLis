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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * este objeto permite tomar desde un archivo de excel, toda la información
 * requerida para poder realizar la operación de distribución, ya que la
 * información que contiene permite crear y actualizar los siguientes objetos:
 * 1. Clientes 2. Productos 3. Facturas 4. Los productos asigados a casa factura
 *
 *
 * @author Luis Eduardo López Casanova
 */
public class HiloImportarDesdeExcel implements Runnable {

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
    ArrayList<String> sqlInsercionRegistros = null;
    ArrayList<CFacturas> facturasAnuladas = null;

    String ruta;//= "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_clientes.sql";

    ArrayList<CClientes> listaDeClientes;
    ArrayList<CProductos> listaDeProductos;
    ArrayList<CFacturas> listaDeFacturas;
    ArrayList<CProductosPorFactura> listaDeProductosorFactura;

    /**
     * Método constructor de la clase
     *
     * @param form corrsponde al formulario desde donde fue instanciada la
     * clase, con el fin de poder controlar las barras de progreso
     */
    public HiloImportarDesdeExcel(FImportarArchivoExcel form) {
        this.form = form;
        this.file = form.file;
        this.ini = form.ini;
        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0, this.file.getName().indexOf(".")) + ".sql";

    }

    /**
     *
     * Método que nos permite llamar otros métodos para la inserción de los
     * datos
     *
     *
     */
    public void ingresarDatos() {
        try {

            facturasAnuladas = new ArrayList();

            form.lblBarraDeProgreso.setVisible(true);
            ingresarClientesCamdun();
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            insertarProductosCamdun();
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            ingresarFacturasCamdun();
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            ingresarProductosPorFactura();
            this.form.barraSuperior.repaint();
            Thread.sleep(1);

            /* el 50% del proceso restante*/
            totalTodasLasFilas = 0;

            totalTodasLasFilas += sqlInsercionRegistros.size();

            form.sqlInsercionRemota = this.sqlInsercionRegistros;

            String cadena = "";
            for (CFacturas factura : listaDeFacturas) {
                cadena += "'" + factura.getNumeroDeFactura() + "',";

            }

            /* SI el check rbtTrasmLocal esta seleccionado hace trasmision local*/
            if (form.rbtTrasmLocal.isSelected()) {

                new Thread(new HiloGuardarTrasmisionAlServidor(ini, sqlInsercionRegistros, this.form, false, cadena)).start();

            }

            /* SI el check rbtTrasmRemota esta seleccionado hace trasmision Remota*/
            if (form.rbtTrasmRemota.isSelected()) {

                new Thread(new HiloGuardarTrasmisionAlServidor(ini, sqlInsercionRegistros, this.form, true, cadena)).start();

            }

            //grababardatos(false);
            //grababardatos(true);
            //insertarBBDDRemota(sqlInsercionRegistros);
            form.lblBarraDeProgreso.setVisible(true);

            //JOptionPane.showMessageDialog(this.form, "Proceso de importacion de datos desde el archivo " +this.file.getName()   +"  finalizado", "Final del proceso",  JOptionPane.INFORMATION_MESSAGE, null);
            //JOptionPane.showMessageDialog(this.form, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("SleepWhileInLoop")
    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de clientes camdun, los cuales
     * provienen de un archivo en excel tomado previamente en los parámetros del
     * método constructor."
     *
     */
    private void ingresarClientesCamdun() {
        String sql = "";
        String numeroFactura = null;
        CFacturas factura = null;

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
            //*  sqlInsercionRegistros = new ArrayList<>();

            listaDeClientes = new ArrayList<>();
            ArrayList<CClientes> auxListaDeClientes = new ArrayList<>();

            ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);

            // archivo.borrarArchivo();
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;

                try {
                    XSSFRow row = ((XSSFRow) rows.next());

                    cliente = new CClientes(ini);
                    factura = new CFacturas(ini);

                    /* trae el numero de la factura */
                    String cadena = row.getCell(19).toString();
                    factura.setNumeroDeFactura(cadena.substring(0, cadena.length() - 2));

                    /*trae el codigo del cliente */
                    cadena = row.getCell(7).toString() + "_" + row.getCell(9).toString();


                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        cliente.setCodigoInterno(cadena);

                        if (!cliente.getCodigoInterno().startsWith("ANULA")) {

                            cliente.setCodigoInterno(cadena);

                            cliente.setNitCliente(row.getCell(7).toString());

                            cliente.setNombreEstablecimiento(row.getCell(8).toString());

                            cliente.setNombreDeCliente(row.getCell(8).toString());

                            cliente.setDireccion(row.getCell(10).toString());

                            cliente.setBarrio("");

                            cliente.setCiudad(row.getCell(11).toString());

                            cadena = row.getCell(12).toString();

                            if (cadena.length() <= 11) {
                                cliente.setCelularCliente(cadena);
                            } else {
                                cliente.setCelularCliente(row.getCell(12).toString().substring(0, 10));
                            }

                            cliente.setClasificacion(row.getCell(13).toString());

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
                            System.out.println("lleva " + contadorDeFilas + "  filas para grabar  Clientes camdun " + new Date());

                        }

                    }

                    /* ACTUALIZA LAS BARRAS DE PROGRESO */
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    //porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                    Thread.sleep(1);

                } catch (Exception ex) {
                    boolean existe = false;
                    if (facturasAnuladas.isEmpty()) {
                        facturasAnuladas.add(factura);
                        form.mensaje += "Factura anulada ... # " + factura.getNumeroDeFactura() + "\n";
                    } else {

                        for (CFacturas obj : facturasAnuladas) {
                            if (obj.getNumeroDeFactura().equals(factura.getNumeroDeFactura())) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            facturasAnuladas.add(factura);
                            form.mensaje += "Factura anulada ... # " + factura.getNumeroDeFactura() + "\n";
                            existe = false;
                        }

                    }

                    form.txtErrores.setText(form.mensaje);

                    System.out.println("Error en agregar  Cliente " + ex + ";(" + contadorDeFilas + ") ");

                    Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex);

                    // contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                Thread.sleep(1);

            } //  FIN DEL WHILE
            fis.close();
            /**
             * *************************************************************************************
             */


            /* una vez se hace el recorrido del archivo empezamos a depurar el archivo 
             para que al ejecutar la sentencia sql no se repitan los registros y sea 
             már rapido la ejecución 
             */
            contadorDeFilas = 0;
            numeroFilas = auxListaDeClientes.size();
            porcentajeBarraInferior = (int) ((contadorDeFilas * 100) / numeroFilas);

            /* Se agregan las sentencias uppdate  que actualizan las tablas clientes, productos, facturas y productos por
           factura con el fin de identificar los registros que ya estan guardados en la BBDD
             */
            sqlInsercionRegistros = new ArrayList();

            for (CClientes obj : auxListaDeClientes) {
                boolean aparece = false;
                if (listaDeClientes.isEmpty()) {
                    listaDeClientes.add(obj);
                    /*Guarda la sentencia sql en un arraay*/
                    sqlInsercionRegistros.add(obj.getSentenciaInsertSQLImpExcel());

                    /*Guarda la sentencia sql en el archivo .sql*/
                    Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                } else {

                    aparece = false;
                    for (CClientes obj2 : listaDeClientes) {
                        if (obj.getCodigoInterno().equals(obj2.getCodigoInterno())) {
                            aparece = true;
                            break;

                        }
                    }
                    if (!aparece) {
                        listaDeClientes.add(obj);
                        sqlInsercionRegistros.add(obj.getSentenciaInsertSQLImpExcel());
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                        // obj.grabarClientes();
                    }
                }
                System.out.println("acá van  " + contadorDeFilas + "  filas contadas ");
                contadorDeFilas++;
                porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                this.form.barraInferior.setValue(porcentajeBarraInferior);
                this.form.barraInferior.repaint();
            }

            /* Se guardan los datos localmente */
            //  ini.insertarDatosLocalmente(sqlInsercionRegistros);
            // sqlInsercionRegistros = null;
            controladorDeCiclos = 0;

            /* EL SISTEMA DA INFORMACION DE LA CANTIDAD DE REGISTROS PROCESADOS */
            System.out.println("lleva " + contadorDeFilas + "  registros listos  clientes camdun " + new Date());
            System.out.println("Termina  inserciones  de Clientes " + new Date());

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Cliente " + cliente.getNombreDeCliente() + " , establecimiento : " + cliente.getNombreEstablecimiento() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") " + sql);
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") " + sql);

        }
    }

    /*  FIN DEL METODO ingresarClientesCamdun() */
    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de productoscamdun, los cuales
     * provienen de un archivo en excel tomado previamente en los parámetros del
     * método constructor."
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

            listaDeProductos = new ArrayList<>();
            ArrayList<CProductos> auxListaDeProductos = new ArrayList<>();

            numeroFilas = sheet.getLastRowNum();

            rows = sheet.rowIterator();

            //*  sqlInsercionRegistros = new ArrayList<>();
            //String ruta;
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;

                try {
                    XSSFRow row = ((XSSFRow) rows.next());

                    producto = new CProductos(ini);
                    factura = new CFacturas(ini);

                    String cadena = row.getCell(19).toString();
                    factura.setNumeroDeFactura(cadena.substring(0, cadena.length() - 2));

                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        cadena = row.getCell(20).toString();
                        producto.setCodigoProducto(cadena);

                        producto.setDescripcionProducto(row.getCell(21).toString());

                        producto.setLinea(row.getCell(14).toString());

                        double val1, val2, val3;
                        /*Valor neto unitario sin iva*/
                        val1 = Double.parseDouble(row.getCell(28).toString());
                        /*Cantidad de unidades*/
                        val2 = Double.parseDouble(row.getCell(24).toString());
                        /*Valor unitario sin iva*/
                        val3 = val1 / val2;

                        producto.setValorUnitarioSinIva(val3);

                        /*Valor neto unitario con iva*/
                        val1 = Double.parseDouble(row.getCell(29).toString());
                        /*Cantidad de unidades*/
                        val2 = Double.parseDouble(row.getCell(24).toString());
                        /*Valor unitario con iva*/
                        val3 = val1 / val2;

                        producto.setValorUnitarioConIva(val3);

                        producto.setIsFree(1);

                        producto.setPesoProducto(0.0);
                        producto.setLargoProducto(0.0);
                        producto.setAnchoProducto(0.0);
                        producto.setAltoProducto(0.0);
                        producto.setActivo(1);

                        auxListaDeProductos.add(producto);
                        System.out.println("lleva " + contadorDeFilas + "  filas para grabar  Productos camdun " + new Date());

                    }

                    /* Se actualizan las barras de progreso del formulario */
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                    Thread.sleep(1);

                } catch (Exception ex) {
                    System.out.println("Error en insertar producto Camdun" + ex + ";(" + contadorDeFilas + ") ");
                    // form.mensaje += "Error al insertar dato Producto #  " + producto.getDescripcionProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
                    //form.txtErrores.setText(form.mensaje);
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                Thread.sleep(10);

            }// FIN DEL WHILE

            fileImputStream.close();

            /* una vez se hace el recorrido del archivo empezamos a depurar el archivo  para que al ejecutar
            * la sentencia sql no se repitan los registros y sea már rapido la ejecución */
            contadorDeFilas = 0;
            numeroFilas = auxListaDeProductos.size();
            porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
            for (CProductos obj : auxListaDeProductos) {
                boolean aparece;
                if (listaDeProductos.isEmpty()) {
                    listaDeProductos.add(obj);
                    sqlInsercionRegistros.add(obj.getSentenciaInsertSQL());
                    Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), this.ruta);
                } else {

                    aparece = false;
                    for (CProductos obj2 : listaDeProductos) {
                        if (obj.getCodigoProducto().equals(obj2.getCodigoProducto())) {
                            aparece = true;
                            break;
                        }
                    }
                    if (!aparece) {
                        listaDeProductos.add(obj);
                        /*Guarda la consulta sql en un array*/
                        sqlInsercionRegistros.add(obj.getSentenciaInsertSQL());

                        /*Guarda la consulta sql en el archivo .sql*/
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), this.ruta);
                        obj.grabarProductos();
                    }
                }
                contadorDeFilas++;
                porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                this.form.barraInferior.setValue(porcentajeBarraInferior);
                this.form.barraInferior.repaint();
            }

            //*  ini.insertarDatosLocalmente(sqlInsercionRegistros);
            controladorDeCiclos = 0;
            //sqlInsercionRemota = null;

            System.out.println("lleva " + contadorDeFilas + "  inserciones  productos Camdun " + new Date());
            System.out.println("Success import excel to mysql table productos camdun");

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Producto #  " + producto.getCodigoProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar codigo de producto Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }
    } // FIN DEL METODO insertarProductosCamdun() 

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de facturascamdun, los cuales
     * provienen de un archivo en excel tomado previamente en los parámetros del
     * método constructor."
     *
     */
    private void ingresarFacturasCamdun() {

        CFacturas factura = null;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
        String numeroFactura;

        try {

            listaDeFacturas = new ArrayList<>();
            ArrayList<CFacturas> auxListaDeFacturas = new ArrayList<>();

            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;

            rows = sheet.rowIterator();

            //* sqlInsercionRegistros = new ArrayList<>();
            //int tipo;
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());
                try {

                    factura = new CFacturas(ini);

                    String cadena = row.getCell(19).toString();
                    factura.setNumeroDeFactura(cadena.substring(0, cadena.length() - 2));

                    factura.setCodigoDeCliente(row.getCell(7).toString() + "_" + cadena);

                    /* Se valida que la fila leida tenga registros de numero de factura */
                    if (!factura.getNumeroDeFactura().isEmpty()) {

                        Date fech;
                        fech = new Date();
                        cadena = "" + row.getCell(2).toString()
                                + "/" + row.getCell(1).toString()
                                + "/" + row.getCell(0).toString();
                        fech = Inicio.getFechaSql(cadena);

                        factura.setFechaDeVenta("" + fech);

                        if (factura.getFechaDeVenta() != null) {

                            /*Direccion de la Factura*/
                            factura.setDireccion(row.getCell(10).toString());

                            /*Barrio*/
                            factura.setBarrio("");

                            /*ciudad*/
                            factura.setCiudad(row.getCell(11).toString());


                            /*Telefono*/
                            cadena = row.getCell(11).toString();
                            if (cadena.length() <= 11) {
                                factura.setTelefono(cadena);
                            } else {
                                factura.setTelefono(row.getCell(11).toString().substring(0, 10));
                            }

                            //tipo = row.getCell(5).getCellType();
                            factura.setVendedor(row.getCell(4).toString());

                            factura.setFormaDePago("CONTADO");
                            factura.setCanal(1);
                            factura.setValorFacturaSinIva(0.0);
                            factura.setValorIvaFactura(0.0);

                            //tipo = row.getCell(26).getCellType();
                            factura.setValorTotalFactura(0.0);

                            factura.setValorRechazo(0.0);
                            factura.setValorDescuento(0.0);
                            factura.setValorTotalRecaudado(0.0);
                            //factura.setimagenFactura();
                            //factura.setFormato();
                            factura.setZona(ini.getUser().getZona());
                            factura.setRegional(ini.getUser().getRegional());
                            factura.setAgencia(ini.getUser().getAgencia());
                            factura.setIsFree(1);
                            factura.setEstadoFactura(1);
                            factura.setPesofactura(0.0);
                            // factura.setRuta(1);
                            factura.setTrasmitido(1);
                            factura.setNumeroDescuento("" + 0);
                            factura.setNumeroRecogida("" + 0);

                            factura.setActivoFactura(1);

                            auxListaDeFacturas.add(factura);

                            System.out.println("lleva " + contadorDeFilas + "  facturas lista para insertar a la BBDD " + new Date());

                        }

                    }

                    // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                } catch (Exception ex) {

                    System.out.println(ex);
                    Date fech;
                    fech = new Date();
                    String cadena = "" + row.getCell(2).toString()
                            + "-" + row.getCell(1).toString()
                            + "-" + row.getCell(0).toString();

                    fech = Inicio.getFechaSql(cadena);

                    //fech = Inicio.getFechaSql(ReadRow(tipo, row, 9).trim());
                    factura.setFechaDeVenta("" + fech);

                    cadena = row.getCell(19).toString();
                    factura.setNumeroDeFactura(cadena.substring(0, cadena.length() - 2));

                    //tipo = row.getCell(0).getCellType();
                    factura.setCodigoDeCliente("" + 0);

                    /*Direccion de la Factura*/
                    factura.setDireccion("ANULADA");

                    /*Barrio*/
                    factura.setBarrio("ANULADA");

                    /*ciudad*/
                    factura.setCiudad("ANULADA");


                    /*Telefono*/
                    factura.setTelefono("0");

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

                    // form.mensaje += "Factura  Anulada # " + factura.getNumeroFactura() + "\n";
                    // form.txtErrores.setText(form.mensaje);
                    Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
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
            contadorDeFilas = 0;
            numeroFilas = auxListaDeFacturas.size();
            porcentajeBarraInferior = (int) ((contadorDeFilas * 100) / numeroFilas);
            for (CFacturas obj : auxListaDeFacturas) {
                boolean aparece = false;
                if (listaDeFacturas.isEmpty()) {
                    listaDeFacturas.add(obj);
                    sqlInsercionRegistros.add(obj.getSentenciaInsertSQL());
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
                        /*Guarda la consulta sql en un array*/
                        sqlInsercionRegistros.add(obj.getSentenciaInsertSQL());

                        /*Guarda la consulta sql en el archivo*/
                        Inicio.GuardaConsultaEnFichero(obj.getSentenciaInsertSQL(), ruta);
                    }
                }
                contadorDeFilas++;
                porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                this.form.barraInferior.setValue(porcentajeBarraInferior);
                this.form.barraInferior.repaint();
            }

            //*  ini.insertarDatosLocalmente(sqlInsercionRegistros);
            //*  sqlInsercionRegistros = null;
            controladorDeCiclos = 0;

            System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas camdun " + new Date());
            System.out.println("Success import excel  to mysql table facturas Camdun");

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato Factura # " + factura.getNumeroDeFactura() + " cliente : " + factura.getCodigoDeCliente() + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de productospor factura, los
     * cuales provienen de un archivo en excel tomado previamente en los
     * parámetros del método constructor."
     *
     */
    private void ingresarProductosPorFactura() {

        String numeroFactura = null;
        List<CFacturas> listaDefacturas = null;

        CProductosPorFactura productoPorFactura = null;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarraInferior;
        listaDeProductosorFactura = new ArrayList<>();
        ArrayList<CProductosPorFactura> auxListaDeProductosorFactura = new ArrayList<>();
        FileInputStream fis;
        //String ruta;
        ArchivosDeTexto archivo;
        // int tipo;

        try {

            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            numeroFilas = sheet.getLastRowNum();
            rows = sheet.rowIterator();
            // * sqlInsercionRegistros = new ArrayList<>();

            CFacturas factura = null;
            while (rows.hasNext()) {

                XSSFRow row;
                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;

                try {

                    String cadena = null;
                    row = ((XSSFRow) rows.next());

                    if (contadorDeFilas == 1) {
                        factura = new CFacturas(ini);

                        cadena = row.getCell(19).toString(); //.substring(0, cadena.length() - 2);
                        cadena = cadena.substring(0, cadena.length() - 2);
                        factura.setNumeroDeFactura(cadena);

                        numeroFactura = factura.getNumeroDeFactura();

                    } else {

                        cadena = row.getCell(19).toString(); //.substring(0, cadena.length() - 2);
                        cadena = cadena.substring(0, cadena.length() - 2);
                    }

                    if (numeroFactura.equals(cadena)) {

                        productoPorFactura = new CProductosPorFactura(ini);

                        asignarPorductoPorFactura(productoPorFactura, numeroFactura, row, auxListaDeProductosorFactura);

                    } else {
                        if(auxListaDeProductosorFactura.size() > 0){
                       for(CProductosPorFactura pxf : auxListaDeProductosorFactura){
                           
                       }
                       
                        for (CProductosPorFactura pxf : auxListaDeProductosorFactura) {
                            if (pxf.getCantidad() > 0) {
                                listaDeProductosorFactura.add(pxf);
                                /*Guardda la consulta SQL en un array*/
                                sqlInsercionRegistros.add(pxf.getSentenciaInsertSQL());
                                Inicio.GuardaConsultaEnFichero(pxf.getSentenciaInsertSQL(), this.ruta);

                            }
                        }
                        auxListaDeProductosorFactura = new ArrayList<>();
                        factura = new CFacturas(ini);
                        cadena = row.getCell(19).toString();
                        factura.setNumeroDeFactura(cadena.substring(0, cadena.length() - 2));
                        numeroFactura = factura.getNumeroDeFactura();

//                        productoPorFactura = new CProductosPorFactura(ini);
//
//                        asignarPorductoPorFactura(productoPorFactura, numeroFactura, row, auxListaDeProductosorFactura);
                    }

                    }

                    /* con el código del cliente podemos validar la factura
                    cadena = row.getCell(9).toString();
                    factura.setCodigoDeCliente(row.getCell(7).toString() + "_" + cadena.substring(0, cadena.length())); */
                    //listaDeProductosorFactura.add(productoPorFactura);

                    /*Guardda la consulta SQL en un array*/
                    // sqlInsercionRegistros.add(productoPorFactura.getSentenciaInsertSQL());

                    /*Guardda la consulta SQL en el archivo*/
                    // Inicio.GuardaConsultaEnFichero(productoPorFactura.getSentenciaInsertSQL(), this.ruta);
                    System.out.println("lleva  " + contadorDeFilas + "  inserciones productos por factura " + new Date());

                    // ACTUALIZA LAS BARRAS DE PROGRESO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.form.barraInferior.setValue(porcentajeBarraInferior);
                    this.form.barraInferior.repaint();

                    this.form.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.form.barraSuperior.repaint();

                } catch (Exception ex) {
                    System.out.println("Error en insertar productos por factura Camdun" + ex + ";(" + contadorDeFilas + ") ");
                    // form.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getFactura() + "producto " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
                    // form.txtErrores.setText(form.mensaje);
                    contadorDeFilas++;
                    contadorDeTodasLasFilas++;
                }
                Thread.sleep(1);

            } //  FIN DEL WHILE

            fis.close();

            System.out.println("lleva " + contadorDeFilas + "  inserciones prodcutos por factura " + new Date());

            this.form.barraSuperior.setValue(0);
            this.form.barraSuperior.repaint();

            this.form.barraInferior.setValue(0);
            this.form.barraInferior.repaint();

            // * ini.insertarDatosLocalmente(sqlInsercionRegistros);
            // insertarBBDDRemota(sqlInsercionRegistros);
            // this.form.barraSuperior.setValue(100);
            this.form.barraSuperior.repaint();

            // sqlInsercionRegistros = null;
            controladorDeCiclos = 0;

            System.out.println("Success import excel to mysql table productos por Factura");

        } catch (IOException | InterruptedException ex) {
            form.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getNumeroFactura() + "producto  " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
            Logger.getLogger(HiloImportarDesdeExcel.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente Camdun" + ex + ";(" + contadorDeFilas + ") ");

        }

    }

    public void asignarPorductoPorFactura(CProductosPorFactura productoPorFactura, String numeroFactura, XSSFRow row, ArrayList<CProductosPorFactura> auxListaDeProductosorFactura) throws NumberFormatException {
        productoPorFactura.setNumeroFactura(numeroFactura);

        productoPorFactura.setCodigoProducto(row.getCell(20).toString());
        productoPorFactura.setCantidad(Double.parseDouble(row.getCell(24).toString()));
        double val1, val2, val3;
        val1 = Double.parseDouble(row.getCell(28).toString());
        val2 = Double.parseDouble(row.getCell(24).toString());
        val3 = val1 / val2;
        productoPorFactura.setValorUnitarioSinIva(val3);

        productoPorFactura.setValorProductoXCantidad(Double.parseDouble(row.getCell(29).toString()));

        val1 = Double.parseDouble(row.getCell(29).toString());
        val2 = Double.parseDouble(row.getCell(24).toString());
        val3 = val1 / val2;
        productoPorFactura.setValorUnitarioConIva(val3);
        productoPorFactura.setPesoProducto(0.0);
        productoPorFactura.setActivo(1);
        auxListaDeProductosorFactura.add(productoPorFactura);
    }

    @Override
    public void run() {
        ingresarDatos();
    }

    /**
     * Método que lee las filas del archivo en excel donde viene toda la
     * información
     *
     * @param tipo corresponde al tipo de celda que fue leida, cadena, entero,
     * fecha etc
     * @param row corresponde a la fila que se está leyendo
     * @param indice corresponde a la columna
     *
     * @return devuelve una cadena con el valor contenido en la celda que se ha
     * leido
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
            default: {

                valor = "";
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

    public boolean insertarBBDDRemota(ArrayList<String> listaDeSentenciasSQL) {
        boolean insertar = false;
        Connection con = null;
        Statement st = null;
        String cadena = null;
        double contadorDeFilas = 0;
        contadorDeTodasLasFilas = 0;
        int porcentajeBarraInferior;
        int numeroFilas;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloImportarFacturasDesdeArchivo");

        if (con != null) {
            try {
                st = con.createStatement();
                contadorDeFilas = 0;
                numeroFilas = listaDeSentenciasSQL.size();
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
                    contadorDeTodasLasFilas++;
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

    private boolean grababardatos(boolean remoto) {
        int filas = 0;
        String servidor;

        Connection con;//= null;
        Statement st;//= null;
        String cadena = null;

        if (remoto) {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloImportarFacturasDesdeArchivo");
            servidor = "servidor remoto";
        } else {
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            servidor = "servidor local";
        }

        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                for (String obj : this.sqlInsercionRegistros) {

                    try {
                        System.out.println(obj);

                        if (obj.length() > 0) {
                            st.execute(obj);
                            filas++;
                            if (remoto) {
                                this.form.barraInferior.setValue((int) (filas * 100) / sqlInsercionRegistros.size());
                                this.form.barraInferior.repaint();
                                Thread.sleep(2);
                            } else {
                                this.form.barraSuperior.setValue((int) (filas * 100) / sqlInsercionRegistros.size());
                                this.form.barraSuperior.repaint();
                                Thread.sleep(2);
                            }

                            System.out.println("dato insertado en el  :  " + servidor + "  " + obj);
                        }
                    } catch (SQLException ex) {
                        con.rollback();
                        Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                con.commit();
                st.close();
                con.close();
                this.form.barraSuperior.setValue(100);
                this.form.barraSuperior.repaint();

            }
        } catch (SQLException ex) {

            Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");

        }

        return true;
    }
}
