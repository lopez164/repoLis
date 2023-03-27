/*
 * 
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcelPacheco;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import com.spire.xls.ExcelVersion;
import com.spire.xls.OrderBy;
import com.spire.xls.SortComparsionType;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
public class HiloImportarFacturasDesdeArchivo implements Runnable {

    File file;
    //FImportarArchivoExcel form;
    FImportarArchivoExcelPacheco fImportarArchivoExcelPacheco;
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
    List<String> sqlInsercionRemota = null;
    List<String> sqlInsercionRemotaManifiestos = null;
    List<CFacturas> facturasAnuladas = null;
    public List<String> sqlGeneral = null;

    String ruta;//= "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_clientes.sql";

    List<CClientes> listaDeClientes;
    List<CProductos> listaDeProductos;
    String listaDeFacturas;
    String listaDeManifiestos;
    List<CProductosPorFactura> listaDeProductosorFactura;

    String clienteIrregular;
    File fileSq;

    /**
     * Método constructor de la clase
     */
    public HiloImportarFacturasDesdeArchivo(FImportarArchivoExcelPacheco form) {
        this.fImportarArchivoExcelPacheco = form;
        this.file = form.file;
        //this.file = new File("/home/lelopez/Documentos/ALD-PLUS/clientes/Pacheco/trasmision/archivo ejemplo.xlsx");

        this.ini = form.ini;
        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0, this.file.getName().indexOf(".")) + ".sql";

    } // 3146846841

    /**
     *
     * Método que nos permite llamar otros métodos para la inserción de los
     * datos
     *
     *
     */
    public void ingresarDatosPacheco() {
        try {
            //  /home/lelopez/Documentos/ALD-PLUS/clientes/Pacheco/trasmision/archivo ejemplo.xlsx

            facturasAnuladas = new ArrayList();

            if (!fImportarArchivoExcelPacheco.cancelar) {
                fImportarArchivoExcelPacheco.lblBarraDeProgreso.setVisible(true);
                ingresarClientes();
                this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                Thread.sleep(1);

            }

            if (!fImportarArchivoExcelPacheco.cancelar) {
                insertarProductos();
                this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                Thread.sleep(1);

            }

            if (!fImportarArchivoExcelPacheco.cancelar) {
                ingresarFacturas();
                this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                Thread.sleep(1);
            }

            if (!fImportarArchivoExcelPacheco.cancelar) {
                ingresarProductosPorFactura();
                this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                Thread.sleep(1);

            }

            /*Valida que haya una trasmision remota */
            if (fImportarArchivoExcelPacheco.rbtTrasmRemota.isShowing()) {
                if (!fImportarArchivoExcelPacheco.cancelar) {
                    ingresarManifiestos();
                    this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                    Thread.sleep(1);

                }

                if (!fImportarArchivoExcelPacheco.cancelar) {
                    ingresarFacturasPorManifiesto();
                    this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                    Thread.sleep(1);

                }

                if (fImportarArchivoExcelPacheco.cancelar) {
                    return;
                }
            }

            /* el 50% del proceso restante*/
            totalTodasLasFilas = 0;

            totalTodasLasFilas += sqlInsercionRemota.size();
            totalTodasLasFilas += sqlInsercionRemotaManifiestos.size();

            fImportarArchivoExcelPacheco.sqlInsercionRemota = this.sqlInsercionRemota;
            fImportarArchivoExcelPacheco.sqlInsercionRemotaManifiestos = this.sqlInsercionRemotaManifiestos;

            if (sqlInsercionRemota.size() > 0) {

                /* SI el check rbtTrasmLocal esta seleccionado hace trasmision local*/
                if (fImportarArchivoExcelPacheco.rbtTrasmLocal.isSelected()) {

                    new Thread(new HiloGuardarTrasmisionAlServidorPacheco(ini, sqlInsercionRemota, this.fImportarArchivoExcelPacheco, false, listaDeFacturas)).start();

                }

                /* SI el check rbtTrasmRemota esta seleccionado hace trasmision Remota*/
                if (fImportarArchivoExcelPacheco.rbtTrasmRemota.isSelected()) {

                    new Thread(new HiloGuardarTrasmisionAlServidorPacheco(ini, this.fImportarArchivoExcelPacheco, true, listaDeFacturas, listaDeManifiestos)).start();

                }

            } else {
                JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "No Hay facturas en la lista ", "Sin facturas", JOptionPane.INFORMATION_MESSAGE);
            }

            fImportarArchivoExcelPacheco.lblBarraDeProgreso.setVisible(true);

            //JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Proceso de importacion de datos desde el archivo " +this.file.getName()   +"  finalizado", "Final del proceso",  JOptionPane.INFORMATION_MESSAGE, null);
            //JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("SleepWhileInLoop")

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de clientes , los cuales
     * provienen de un archivo en excel tomado previamente en los parámetros del
     * método constructor."
     *
     */
    private void ingresarClientes() {
        String sql = "";
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        CClientes cliente = null;
        String numeroPrevio = "0";
        int cantidad = 0;

        try {
            FileInputStream fis;
            /*Ordena el archivo fuente por codigo de clientes*/
            sortData(1, "Clientes");

            /*Ordenado el archivo se abre para reccorrerlo*/
            fis = new FileInputStream("tmp/SortDataClientes.xlsx");

            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);

            this.fImportarArchivoExcelPacheco.lblLocal.setText("Proceso general de sincronizacion");

            numeroFilas = sheet.getLastRowNum();

            totalTodasLasFilas = numeroFilas * 6;
            contadorDeTodasLasFilas = 0;

            //numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();

            System.out.println("inicio inserciones  ");
            //*  sqlInsercionRemota = new ArrayList<>();

            listaDeClientes = new ArrayList<>();

            sqlInsercionRemota = new ArrayList();

            System.out.println("Se Inicia proceso de clientes ");

            // archivo.borrarArchivo();
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {

                    try {

                        String cadena = "";

                        /*Nit del cliente*/
                        cadena = (String) row.getCell(2).toString();
                        if (cadena.contains("#N/A")) {

                            clienteIrregular = "codigo : " + row.getCell(1).toString() + " \n"
                                    + "Nombre : " + row.getCell(3).toString() + " \n"
                                    + "direccion : " + row.getCell(5).toString() + "\n\n";
                            fImportarArchivoExcelPacheco.mensaje += "Cliente no tiene NIt : " + clienteIrregular + "\n";
                            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);

                        }

                        /* Codigo del codigo del cliente*/
                        cadena = row.getCell(1).toString();

                        if (!numeroPrevio.equals(cadena)) {

                            numeroPrevio = cadena;
                            cantidad++;

                            cliente = new CClientes(ini);
                            cliente.setCodigoInterno(cadena);

                            /*Se trae el nit del cliente */
                            cadena = (String) row.getCell(2).toString();
                            try {
                                /*Si la celda trae un valor numerico*/
                                cadena = row.getCell(2).toString();
                                double cad = row.getCell(2).getNumericCellValue();
                                cadena = String.format("%.0f", cad);
                                cliente.setNitCliente(cadena);

                            } catch (Exception e) {
                                /*Si se genera una excepcion, trae el valor que contiene la celd a*/
                                try {
                                    cliente.setNitCliente(row.getCell(2).toString());
                                } catch (Exception ex) {
                                    /*Si nuevamente gener excepcion toma el nit con un valor */
                                    cliente.setNitCliente("0000");
                                }

                            }

                            cliente.setNombreDeCliente(row.getCell(3).toString());

                            /*Nombre del establecimiento*/
                            try {
                                cliente.setNombreEstablecimiento(row.getCell(4).toString());
                            } catch (Exception e) {
                                cliente.setNombreEstablecimiento("");
                            }

                            /*Direccion del cliente */
                            cliente.setDireccion(row.getCell(5).toString());

                            /*Barrio del Cliente*/
                            cliente.setBarrio(row.getCell(6).toString());

                            /*ciudad*/
                            cliente.setCiudad(row.getCell(7).toString());

                            /*Telefono*/
                            try {
                                cadena = row.getCell(8).toString();
                                double cad = row.getCell(8).getNumericCellValue();
                                cadena = String.format("%.0f", cad);
                                cliente.setCelularCliente(cadena);

                            } catch (Exception e) {
                                cliente.setNitCliente(row.getCell(8).toString());

                            }

                            /*Email del cliente */
                            try {
                                if ((row.getCell(9).toString()).isEmpty()) {
                                    cliente.setEmailCliente("SinEmail@hotmail.com");
                                } else {
                                    cliente.setEmailCliente(row.getCell(9).toString());
                                }

                            } catch (Exception e) {
                                cliente.setEmailCliente("SinEmail@hotmail.com");
                            }


                            /*latitud y longitud */
                            try {
                                Double latitud = Double.parseDouble(row.getCell(10).toString());
                                Double longitud = Double.parseDouble(row.getCell(11).toString());
                                cliente.setLatitud(row.getCell(10).toString());
                                cliente.setLongitud(row.getCell(11).toString());
                            } catch (Exception e) {
                                cliente.setLongitud("0");
                                cliente.setLatitud("0");
                            }

                            cliente.setFechaDeIngresoCliente(ini.getFechaActualServidor());
                            cliente.setActivoCliente(1);

                            /*Clasificacion Cliente */
                            try {
                                cliente.setClasificacion(row.getCell(49).toString());
                            } catch (Exception e) {
                                cliente.setClasificacion("NO ESPECIFICADO");
                            }

                            /*Canal del cliente */
                            try {
                                cadena = row.getCell(50).toString();
                                String[] cad = cadena.split("-");
                                cliente.setCanalDeVenta(Integer.parseInt(cad[0]));

                            } catch (Exception e) {
                                cliente.setCanalDeVenta(1);
                            }

                            cliente.setRuta(1);
                            cliente.setFrecuencia(1);
                            cliente.setZona(ini.getIdZona());
                            cliente.setRegional(ini.getIdRegional());
                            cliente.setAgencia(ini.getIdAgencia());
                            cliente.setPorcentajeDescuento(0.0);
                            cliente.setActivoCliente(1);

                            /*se agrega cadena para insertar cliente */
                            sqlInsercionRemota.add(cliente.getSentenciaInsertSQLImpExcel());

                            // System.out.println("lleva " + sqlInsercionRemota.size() + " filas para grabar  Clientes  ,");
                        }


                        /* ACTUALIZA LAS BARRAS DE PROGRESO */
                        porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                        //porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                        porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                        this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando clientes");

                        this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                        this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                        this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                        Thread.sleep(1);

                    } catch (Exception ex) {
                        System.out.println("Error en Da to Cliente " + ex + ";(" + contadorDeFilas + ") ");

                        Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);

                        // contadorDeFilas++;
                        contadorDeTodasLasFilas++;
                    }

                    Thread.sleep(1);

                }
            }

            fis.close();

            System.out.println("lleva " + cantidad + " Clientes  ,");

            // ini.insertarBBDDRemota(sqlInsercionRemota, "clientes");
            // ini.insertarDatosRemotamente(sqlInsercionRemota, "");
            System.out.println("Se guardaron los clientes");

        } catch (IOException | InterruptedException ex) {

            /* form.mensaje += "Error al insertar dato Cliente " + cliente.getNombreDeCliente() + " , establecimiento : " + cliente.getNombreEstablecimiento() + " factura # : " + numeroFactura + "\n";
            form.txtErrores.setText(form.mensaje);
             */
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") " + sql);
            System.out.println("Error en Dato del cliente " + ex + ";(" + contadorDeFilas + ") " + sql);

        }
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de productos, los cuales
     * provienen de un archivo en excel tomado previamente en los parámetros del
     * método constructor."
     *
     */
    private void insertarProductos() {
        String numeroFactura = null;
        CProductos producto = null;
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        CFacturas factura;
        String numeroPrevio = "0";
        int cantidad = 0;

        try {
            sortData(12, "Productos");
            FileInputStream fileImputStream;
            // fileImputStream = new FileInputStream(this.file);
            fileImputStream = new FileInputStream("tmp/SortDataProductos.xlsx");
            workbook = new XSSFWorkbook(fileImputStream);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            listaDeProductos = new ArrayList<>();
            ArrayList<CProductos> auxListaDeProductos = new ArrayList<>();

            numeroFilas = sheet.getLastRowNum();

            rows = sheet.rowIterator();

            //*  sqlInsercionRemota = new ArrayList<>();
            //String ruta;
            while (rows.hasNext()) {


                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {
                    try {

                        producto = new CProductos(ini);

                        /*LLama la columna con el codigo del producto */
                        String cadena = row.getCell(12).toString();

                        if (!cadena.equals(numeroPrevio)) {

                            numeroPrevio = cadena;
                            cantidad++;

                            /*Codigo del producto*/
                            producto.setCodigoProducto(cadena);

                            /*Descripcion del producto*/
                            producto.setDescripcionProducto(row.getCell(13).toString());

                            /* Linea de producto */
                            producto.setLinea(row.getCell(14).toString());

                            /*Valor unitario con iva*/
                            producto.setValorUnitarioConIva(Double.parseDouble(row.getCell(19).toString()));

                            /*Valor unitario sin iva*/
                            producto.setValorUnitarioSinIva(Double.parseDouble(row.getCell(20).toString()));

                            producto.setIsFree(1);

                            producto.setPesoProducto(((Double.parseDouble(row.getCell(21).toString())) / (Double.parseDouble(row.getCell(15).toString()))) * 1000);

                            producto.setLargoProducto(0.0);
                            producto.setAnchoProducto(0.0);
                            producto.setAltoProducto(0.0);
                            producto.setActivo(1);

                            //auxListaDeProductos.add(producto);
                            sqlInsercionRemota.add(producto.getSentenciaInsertSQL());

                            // System.out.println("lleva " + sqlInsercionRemota.size() + "  filas para grabar  Productos  ");
                        }

                        /* Se actualizan las barras de progreso del formulario */
                        porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                        // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                        porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                        this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando productos ");
                        this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                        this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                        this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                        Thread.sleep(1);

                    } catch (Exception ex) {
                        System.out.println("Erroren dato del producto " + ex + ";(" + contadorDeFilas + ") ");
                        // form.mensaje += "Error al insertar dato Producto #  " + producto.getDescripcionProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
                        //form.txtErrores.setText(form.mensaje);
                        contadorDeFilas++;
                        contadorDeTodasLasFilas++;
                    }
                }
                Thread.sleep(10);

            }// FIN DEL WHILE

            fileImputStream.close();
            System.out.println("lleva " + cantidad + " Productos  ,");

            controladorDeCiclos = 0;
            //sqlInsercion

            System.out.println("lleva " + contadorDeFilas + "  inserciones  productos  ");
            System.out.println("Success import excel to mysql table productos ");

        } catch (IOException | InterruptedException ex) {
            fImportarArchivoExcelPacheco.mensaje += "Error al insertar dato Producto #  " + producto.getCodigoProducto() + "nombre Producto :" + producto.getDescripcionProducto() + " factura # : " + numeroFactura + "\n";
            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar codigo de producto " + ex + ";(" + contadorDeFilas + ") ");

        }
    } // FIN DEL METODO insertarProductos() 

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de facturas, los cuales provienen
     * de un archivo en excel tomado previamente en los parámetros del método
     * constructor."
     *
     */
    private void ingresarFacturas() {

        CFacturas factura = null;
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        String numeroFactura;
        String numeroPrevio = "0";
        int cantidad = 0;

        try {

            listaDeFacturas = "'";
            sortData(0, "Facturas");
            FileInputStream fis;
            //fis = new FileInputStream(this.file);
            fis = new FileInputStream("tmp/SortDataFacturas.xlsx");
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;

            rows = sheet.rowIterator();

            //* sqlInsercionRemota = new ArrayList<>();
            //int tipo;
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {
                    String cadena = "";

                    /*Nit del cliente*/
                    cadena = (String) row.getCell(2).toString();

                    factura = new CFacturas(ini);

                    cadena = row.getCell(0).toString();

                    if (!cadena.equals(numeroPrevio)) {
                        numeroPrevio = cadena;

                        cantidad++;
                        /*Numero de la factura*/
                        factura.setNumeroDeFactura(cadena);

                        listaDeFacturas += cadena + "','";

                        /*Se toma el codico del cliente*/
                        factura.setCodigoDeCliente(row.getCell(1).toString());

                        /*Fecha de venta */
                        cadena = row.getCell(47).toString();
                        factura.setFechaDeVenta(cadena);

                        /*Direccion de la Factura*/
                        factura.setDireccion(row.getCell(5).toString());

                        /* Ruta*/
                        factura.setRuta("NA");

                        /*Barrio*/
                        factura.setBarrio(row.getCell(6).toString());

                        /*ciudad*/
                        factura.setCiudad(row.getCell(7).toString());

                        /*Telefono*/
                        factura.setTelefono(row.getCell(8).toString());

                        factura.setVendedor(row.getCell(23).toString());

                        factura.setFormaDePago("CONTADO");

                        /*Canal del cliente*/
                        try {
                            cadena = row.getCell(50).toString();
                            String[] cad = cadena.split("-");
                            factura.setCanal(Integer.parseInt(cad[0]));

                        } catch (Exception e) {
                            factura.setCanal(1);
                        }

                        factura.setValorFacturaSinIva(0.0);
                        factura.setValorIvaFactura(0.0);

                        //factura.setValorTotalFactura(Double.parseDouble(row.getCell(30).toString()));
                        factura.setValorTotalFactura(0.0);

                        factura.setValorRechazo(0.0);
                        factura.setValorDescuento(0.0);
                        factura.setValorTotalRecaudado(0.0);
                        factura.setZona(ini.getIdZona());
                        factura.setRegional(ini.getIdRegional());
                        factura.setAgencia(ini.getIdAgencia());
                        factura.setIsFree(1);
                        factura.setEstadoFactura(1);
                        factura.setPesofactura(0.0);
                        factura.setTrasmitido(0);
                        factura.setNumeroDescuento("" + 0);
                        factura.setNumeroRecogida("" + 0);

                        factura.setActivoFactura(1);

                        factura.setTelefonoVendedor("");
                        factura.setPlazoDias(1);
                        factura.setPrefijo("");
                        factura.setNumero("");
                        factura.setFpContado(0.0);

                        try {
                            factura.setObservaciones(row.getCell(52).toString());
                        } catch (Exception e) {
                            factura.setObservaciones("");
                        }

                        sqlInsercionRemota.add(factura.getSentenciaInsertSQL());

                        //System.out.println("lleva " + sqlInsercionRemota.size() + "  facturas lista para insertar a la BBDD ");
                    }

                    // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando Facturas");
                    this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                    this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                    this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                }
                Thread.sleep(1);

            } //FIN DEL WHILE

            fis.close();
            System.out.println("lleva " + cantidad + " facturas  ,");
            listaDeFacturas = listaDeFacturas.substring(0, listaDeFacturas.length() - 2);

            //*  ini.insertarDatosLocalmente(sqlInsercionRemota);
            //*  sqlInsercionRemota = null;
            controladorDeCiclos = 0;

            //System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas  ");
            System.out.println("Success import excel  to mysql table facturas ");

        } catch (IOException | InterruptedException ex) {
            fImportarArchivoExcelPacheco.mensaje += "Error al insertar dato Factura # " + factura.getNumeroDeFactura() + " cliente : " + factura.getCodigoDeCliente() + "\n";
            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente " + ex + ";(" + contadorDeFilas + ") ");

        } catch (SQLException ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
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

        CProductosPorFactura productoPorFactura = null;
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        listaDeProductosorFactura = new ArrayList<>();
        ArrayList<CProductosPorFactura> auxListaDeProductosorFactura = new ArrayList<>();
        FileInputStream fis;
        //String ruta;
        ArchivosDeTexto archivo;
        int cantidad = 0;
        // int tipo;

        try {

            fis = new FileInputStream(this.file);

            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            numeroFilas = sheet.getLastRowNum();
            rows = sheet.rowIterator();
            // * sqlInsercionRemota = new ArrayList<>();

            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {

                    String cadena = "";

                    /*Nit del cliente*/
                    cadena = (String) row.getCell(2).toString();

                    try {

                        cantidad++;
                        productoPorFactura = new CProductosPorFactura(ini);
                        /*numero de la factura*/
                        productoPorFactura.setNumeroFactura(row.getCell(0).toString());

                        productoPorFactura.setCodigoProducto(row.getCell(12).toString());

                        productoPorFactura.setDescripcionProducto(row.getCell(13).toString());
                        productoPorFactura.setCantidad(Double.parseDouble(row.getCell(15).toString()));

                        productoPorFactura.setValorUnitarioSinIva(Double.parseDouble(row.getCell(20).toString()));

                        /*Valor del producto sin iva*/
                        Double val1 = Double.parseDouble(row.getCell(37).toString());
                        
                        /*Valor IVA */
                        Double val2 = Double.parseDouble(row.getCell(43).toString());
                        /*Valor total del producto con iva */
                        Double val3 = val1 + val2;
                        /*Valor del producto con iva * la cantidad de producto */
                        productoPorFactura.setValorProductoXCantidad(val3);

                        val1=Double.parseDouble(row.getCell(19).toString());
                        val2=Double.parseDouble(row.getCell(22).toString());
                        val3=val1*(1+(val2/100));
                        productoPorFactura.setValorUnitarioConIva(val3);

                        /*peso del producot en gramos */
                        productoPorFactura.setPesoProducto((Double.parseDouble(row.getCell(21).toString())) * 1000);

                        productoPorFactura.setActivo(1);
                        // productoPorFactura.grabarProductosPorFactura();
                        auxListaDeProductosorFactura.add(productoPorFactura);

                        /*Guardda la consulta SQL en un array*/
                        sqlInsercionRemota.add(productoPorFactura.getSentenciaInsertSQL());

                        /*Guardda la consulta SQL en el archivo
                                Inicio.GuardaConsultaEnFichero(productoPorFactura.getSentenciaInsertSQL(), this.ruta);*/
                        // System.out.println("lleva  " + sqlInsercionRemota.size() + "  inserciones productos por factura ");
                        // ACTUALIZA LAS BARRAS DE PROGRESO
                        porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                        // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                        porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                        this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando detalle factura");
                        this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                        this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                        this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                        this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                    } catch (Exception ex) {
                        System.out.println("Error en insertar productos por factura " + ex + ";(" + contadorDeFilas + ") ");
                        // form.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getFactura() + "producto " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
                        // form.txtErrores.setText(form.mensaje);
                        contadorDeFilas++;
                        contadorDeTodasLasFilas++;
                    }
                }
            }
            Thread.sleep(1);

            sqlGeneral = new ArrayList<>();
            for (String sql : sqlInsercionRemota) {
                sqlGeneral.add(sql);
            }

            fis.close();

            System.out.println("lleva " + cantidad + " detalle factura  ,");

            System.out.println("lleva " + contadorDeFilas + "  inserciones prodcutos por factura ");

            this.fImportarArchivoExcelPacheco.barraSuperior.setValue(0);
            this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

            this.fImportarArchivoExcelPacheco.barraInferior.setValue(0);
            this.fImportarArchivoExcelPacheco.barraInferior.repaint();

            // * ini.insertarDatosLocalmente(sqlInsercionRemota);
            // insertarBBDDRemota(sqlInsercionRemota);
            // this.fImportarArchivoExcelPacheco.barraSuperior.setValue(100);
            this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

            // sqlInsercionRemota = null;
            controladorDeCiclos = 0;

            System.out.println("Success import excel to mysql table productos por Factura");

        } catch (IOException | InterruptedException ex) {
            fImportarArchivoExcelPacheco.mensaje += "Error al insertar dato producto en la factura # " + productoPorFactura.getNumeroFactura() + "producto  " + productoPorFactura.getCodigoProducto() + " factura # : " + numeroFactura + "\n";
            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente " + ex + ";(" + contadorDeFilas + ") ");

        }

    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de facturas, los cuales provienen
     * de un archivo en excel tomado previamente en los parámetros del método
     * constructor."
     *
     */
    private void ingresarManifiestos() {

        CManifiestosDeDistribucion manifiesto = null;
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        String numeroFactura;
        String numeroPrevio = "0";
        int cantidad = 0;

        try {

            sqlInsercionRemotaManifiestos = new ArrayList<>();
            listaDeManifiestos = "'";

            sortData(30, "Manifiestos");
            FileInputStream fis;
            //fis = new FileInputStream(this.file);
            fis = new FileInputStream("tmp/SortDataManifiestos.xlsx");
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;

            rows = sheet.rowIterator();

            //* sqlInsercionRemota = new ArrayList<>();
            //int tipo;
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {
                    String cadena = "";
                    /*Nit del cliente*/
                    cadena = (String) row.getCell(2).toString();

                    manifiesto = new CManifiestosDeDistribucion(ini);

                    cadena = row.getCell(30).toString();
                    if (!cadena.equals("0") && (!cadena.equals(""))) {

                        if (!cadena.equals(numeroPrevio)) {
                            numeroPrevio = cadena;
                            listaDeManifiestos += cadena + "','";
                            cantidad++;

                            manifiesto.setNumeroManifiesto(row.getCell(30).toString());
                            manifiesto.setFechaDistribucion(row.getCell(31).toString());
                            manifiesto.setVehiculo("AAA000");
                            manifiesto.setConductor("0");
                            manifiesto.setDespachador("0");

                            try {
                                cadena = row.getCell(50).toString();
                                String[] cad = cadena.split("-");
                                manifiesto.setIdCanal(Integer.parseInt(cad[0]));
                            } catch (Exception e) {
                                manifiesto.setIdCanal(1);

                            }

                            manifiesto.setIdRuta(1);
                            manifiesto.setEstadoManifiesto(3);
                            manifiesto.setKmSalida(0);
                            manifiesto.setKmEntrada(0);
                            manifiesto.setKmRecorrido(0);
                            manifiesto.setZona(ini.getIdZona());
                            manifiesto.setRegional(ini.getIdRegional());
                            manifiesto.setAgencia(ini.getIdAgencia());
                            manifiesto.setIsFree(1);
                            manifiesto.setValorTotalManifiesto(0.0);
                            manifiesto.setValorRecaudado(0.0);
                            manifiesto.setHoraDeDespacho("2023-03-01 00:00:00");
                            manifiesto.setHoraDeLiquidacion("2022-01-01 00:00:00");
                            manifiesto.setPesoKgManifiesto(0.0);
                            manifiesto.setCantidadPedidos(0);
                            manifiesto.setActivo(1);
                            //manifiesto.setFechaIng("");	
                            manifiesto.setUsuarioManifiesto(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
                            //manifiesto.setFlag(-1);
                            manifiesto.setObservaciones("");
                            manifiesto.setCantDeSalidas(0);
                            manifiesto.setFlag(1);

                            sqlInsercionRemotaManifiestos.add(manifiesto.getSentenciaInsertSQLSinActualizacion());

                            // System.out.println("lleva " + sqlInsercionRemota.size() + "  mnifiestos para insertar a la BBDD ");
                        }
                    }
                    // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando Manifiestos");
                    this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                    this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                    this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                }
                Thread.sleep(1);

            } //FIN DEL WHILE

            fis.close();
            listaDeManifiestos = listaDeManifiestos.substring(0, listaDeManifiestos.length() - 2);
            System.out.println("lleva " + cantidad + " manifiestos");

            //*  ini.insertarDatosLocalmente(sqlInsercionRemota);
            //*  sqlInsercionRemota = null;
            controladorDeCiclos = 0;

            //  System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas  ");
            // System.out.println("Success import excel  to mysql table facturas ");
        } catch (IOException | InterruptedException ex) {
            fImportarArchivoExcelPacheco.mensaje += "Error al insertar dato manifiesto # \n" + ex;
            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente " + ex + ";(" + contadorDeFilas + ") ");

        } catch (SQLException ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local y en la
     * BBDD remota correspondientes a la tabla de facturas, los cuales provienen
     * de un archivo en excel tomado previamente en los parámetros del método
     * constructor."
     *
     */
    private void ingresarFacturasPorManifiesto() {

        CFacturasPorManifiesto facturaxManifiesto = null;
        int numeroFilas;
        int contadorDeFilas = 0;
        int porcentajeBarraInferior;
        String numeroPrevio = "0";
        int cantidad = 0;
        int adherencia = 1;
        String manifiestoprevio = "0";

        try {

            FileInputStream fis;
            //fis = new FileInputStream(this.file);
            fis = new FileInputStream("tmp/SortDataFacturas.xlsx");
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);
            numrows = sheet.getLastRowNum();

            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;

            rows = sheet.rowIterator();

            //* sqlInsercionRemota = new ArrayList<>();
            //int tipo;
            int i = 0;
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarArchivoExcelPacheco.cancelar == true) {
                    JOptionPane.showMessageDialog(this.fImportarArchivoExcelPacheco, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (contadorDeFilas > 1) {

                    String cadena = "";
                    /*Nit del cliente*/
                    cadena = (String) row.getCell(2).toString();

                    facturaxManifiesto = new CFacturasPorManifiesto(ini);

                    cadena = row.getCell(30).toString();
                    if (!cadena.equals("0") && (!cadena.equals(""))) {
                        cadena = row.getCell(0).toString();
                        if (!cadena.equals(numeroPrevio)) {
                            facturaxManifiesto.setNumeroManifiesto(row.getCell(30).toString());

                            if (!manifiestoprevio.equals(facturaxManifiesto.getNumeroManifiesto())) {
                                manifiestoprevio = row.getCell(30).toString();
                                adherencia = 1;
                            }
                            numeroPrevio = cadena;

                            cantidad++;
                            facturaxManifiesto.setNumeroManifiesto(row.getCell(30).toString());
                            facturaxManifiesto.setNumeroFactura(row.getCell(0).toString());
                            facturaxManifiesto.setValorARecaudarFactura(0.0);
                            facturaxManifiesto.setPesoFactura(0.0);
                            facturaxManifiesto.setAdherencia(adherencia++);
                            facturaxManifiesto.setIdZona(ini.getIdZona());
                            facturaxManifiesto.setIdRegional(ini.getIdRegional());
                            facturaxManifiesto.setIdAgencia(ini.getIdAgencia());
                            facturaxManifiesto.setActivo(1);
                            //facturaxManifiesto.setFechaIng("");
                            facturaxManifiesto.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
                            facturaxManifiesto.setFlag(1);
                            facturaxManifiesto.setDespachado(1);

                            facturaxManifiesto.setFechaDespachado("" + ini.getFechaActualServidor());
                            facturaxManifiesto.setUsuariodespachador("");
                            facturaxManifiesto.setVehiculoAsignado("");
                            facturaxManifiesto.setFpContado(0.0);

                            sqlInsercionRemotaManifiestos.add(facturaxManifiesto.getSentenciaInsertSQL());

                            // System.out.println("lleva " + sqlInsercionRemota.size() + "  facturas lista para insertar a la BBDD ");
                        }
                    }
                    // ACTUALIZA LA BARRA DE DESPLAZAMIENTO
                    porcentajeBarraInferior = (int) (contadorDeFilas * 100) / numeroFilas;
                    // porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

                    this.fImportarArchivoExcelPacheco.lblRemoto.setText("Sincronizando Facturas");
                    this.fImportarArchivoExcelPacheco.barraInferior.setValue(porcentajeBarraInferior);
                    this.fImportarArchivoExcelPacheco.barraInferior.repaint();

                    this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.fImportarArchivoExcelPacheco.barraSuperior.repaint();

                }

                Thread.sleep(1);

            } //FIN DEL WHILE

            for (String sql : sqlInsercionRemotaManifiestos) {
                sqlGeneral.add(sql);
            }

           
            fis.close();
            System.out.println("lleva " + cantidad + " fcturas por manifiesto  ,");

            //*  ini.insertarDatosLocalmente(sqlInsercionRemota);
            //*  sqlInsercionRemota = null;
            controladorDeCiclos = 0;

            //System.out.println("lleva " + contadorDeFilas + "  inserciones  facturas  ");
            System.out.println("Success import excel  to mysql table facturas ");

        } catch (IOException | InterruptedException ex) {
            fImportarArchivoExcelPacheco.mensaje += "Error al insertar dato Factura # " + facturaxManifiesto.getNumeroFactura() + "\n";
            fImportarArchivoExcelPacheco.txtErrores.setText(fImportarArchivoExcelPacheco.mensaje);
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex + ";(" + contadorDeFilas + ") ");
            System.out.println("Error en insertar cliente " + ex + ";(" + contadorDeFilas + ") ");

        } catch (SQLException ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloImportarFacturasDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        //ingresarDatos();
        ingresarDatosPacheco();
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

    private void sortData(int columna, String tabla) {
        //Create a Workbook instance

        Workbook workbook = new Workbook();

        //Load the sample Excel document
        // workbook.loadFromFile("sample.xlsx");
        workbook.loadFromFile(this.file.getAbsolutePath());

        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);

        //Specify the column that need to be sorted and the sort mode (ascending or descending)
        workbook.getDataSorter().getSortColumns().add(columna, SortComparsionType.Values, OrderBy.Ascending);

        //Sort data in the specified cell range
        // workbook.getDataSorter().sort(sheet.getCellRange("A1:BI2349"));
        workbook.getDataSorter().sort(sheet.getAllocatedRange());

        //System.out.print("" + sheet.getAllocatedRange());
        //Save the document to file
        workbook.saveToFile("tmp/SortData" + tabla + ".xlsx", ExcelVersion.Version2007);

    }
}
