/*
 * 
 */
package mtto.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.ingresoDeRegistros.FImportarConsumoCombustible;
import mtto.documentos.objetos.GastosPorVehiculo;
import mtto.ingresoDeRegistros.objetos.GastosFlota;
import mtto.vehiculos.CCarros;
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
public class HiloImportarConsumosCombustibeDesdeArchivo implements Runnable {

    File file;
    FImportarConsumoCombustible fImportarConsumoCombustible;

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

    List<GastosPorVehiculo> listaDeGastosPorVehiculo = null;
    List<String> sqlInsercionRemota = null;

    String comando;//= "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_clientes.sql";
    String ruta;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    String mensaje;

    /**
     * Método constructor de la clase
     *
     * @param form corrsponde al formulario desde donde fue instanciada la
     * clase, con el fin de poder controlar las barras de progreso
     */
    public HiloImportarConsumosCombustibeDesdeArchivo(FImportarConsumoCombustible form, String comando) {
        this.fImportarConsumoCombustible = form;
        this.file = form.file;
        this.ini = form.ini;
        this.comando = comando; //
        ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + this.file.getName().substring(0, this.file.getName().indexOf(".")) + ".sql";

    } // 3146846841

    public void ingresarDatosPacheco() {
        try {

            listaDeGastosPorVehiculo = new ArrayList();

            if (!fImportarConsumoCombustible.cancelar) {
                // fImportarArchivoExcelPacheco.lblBarraDeProgreso.setVisible(true);
                importarConsumosCombustible();
                // this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                Thread.sleep(1);
                return;
            }

            /* el 50% del proceso restante
            totalTodasLasFilas = 0;

            totalTodasLasFilas += sqlInsercionRemota.size();

            fConsumoCombustible.sqlInsercionRemota = this.sqlInsercionRemota;

             cc String cadena = "";

            if (listaDeFacturas.size() > 0) {

                for (CFacturas factura : listaDeFacturas) {
                    cadena += "'" + factura.getNumeroDeFactura() + "',";

                }

                cadena = cadena.substring(0, cadena.length() - 1);

                /* SI el check rbtTrasmLocal esta seleccionado hace trasmision local
                if (fConsumoCombustible.rbtTrasmLocal.isSelected()) {

                    new Thread(new HiloGuardarTrasmisionAlServidor(ini, sqlInsercionRemota, this.fConsumoCombustible, false, cadena)).start();

                }

                /* SI el check rbtTrasmRemota esta seleccionado hace trasmision Remota
                if (fConsumoCombustible.rbtTrasmRemota.isSelected()) {

                    new Thread(new HiloGuardarTrasmisionAlServidor(ini, sqlInsercionRemota, this.fConsumoCombustible, true, cadena)).start();

                }

            } else {
                JOptionPane.showMessageDialog(this.fConsumoCombustible, "No Hay facturas en la lista ", "Sin facturas", JOptionPane.INFORMATION_MESSAGE);
            }

            fConsumoCombustible.lblBarraDeProgreso.setVisible(true);

            //JOptionPane.showMessageDialog(this.fConsumoCombustible, "Proceso de importacion de datos desde el archivo " +this.file.getName()   +"  finalizado", "Final del proceso",  JOptionPane.INFORMATION_MESSAGE, null);
            //JOptionPane.showMessageDialog(this.fConsumoCombustible, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloImportarConsumosCombustibeDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
             */
        } catch (Exception ex) {

        }
    }

    public boolean grabar() {
        boolean grabado = false;

        mensaje = "";

        for (GastosFlota obj : this.fImportarConsumoCombustible.listaDeGastosFlota) {

            String msg = "";
            msg = obj.guardar(1);
            if (!msg.equals("")) {
                mensaje += msg;
                this.fImportarConsumoCombustible.txtObservaciones.setText(mensaje);
            }
            grabado = true;
        }

        //grabado = ini.insertarDatosRemotamente(sqlInsercionRemota, "HiloImportarConsumosCombustibeDesdeArchivo");
        return grabado;
    }

    @SuppressWarnings("SleepWhileInLoop")

    private boolean importarConsumosCombustible() {
        String sql = "";
        int numeroFilas;
        int contadorDeFilas = 0;

        boolean confirmado = false;

        GastosPorVehiculo gastosPorVehiculo = null;
        GastosFlota gastosFlota = null;
        try {
            FileInputStream fis;
            fis = new FileInputStream(this.file);
            workbook = new XSSFWorkbook(fis);
            numHojas = workbook.getNumberOfSheets();
            XSSFSheet sheet = workbook.getSheetAt(0);

            //this.fImportarArchivoExcelPacheco.jLabel1.setText("Proceso generalal de sincronizacion");
            numeroFilas = sheet.getLastRowNum();
            totalTodasLasFilas = numeroFilas * 4;
            contadorDeTodasLasFilas = 0;

            //numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();

            System.out.println("inicio inserciones  " + new Date());
            //*  sqlInsercionRemota = new ArrayList<>();

            this.fImportarConsumoCombustible.listaDeGastosFlota = new ArrayList<>();
            this.fImportarConsumoCombustible.listaDeGastosPorVehiculo = new ArrayList<>();

            sqlInsercionRemota = new ArrayList();

            // archivo.borrarArchivo();
            while (rows.hasNext()) {

                /*  INCREMENTA LOS CONTADORES  */
                controladorDeCiclos++;
                contadorDeFilas++;
                contadorDeTodasLasFilas++;
                XSSFRow row = ((XSSFRow) rows.next());

                if (this.fImportarConsumoCombustible.cancelar) {
                    JOptionPane.showMessageDialog(this.fImportarConsumoCombustible, "Operacion Cancelada", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }

                if (contadorDeFilas > 1) {

                    gastosPorVehiculo = new GastosPorVehiculo(ini);
                    gastosFlota = new GastosFlota(ini);
                    /*
                    
                    sucursalProveedor, 
                            **/

                    gastosFlota.setIdProveedor(fImportarConsumoCombustible.proveedor.getCedula());

                    gastosPorVehiculo.setIdProveedor(fImportarConsumoCombustible.proveedor.getCedula());
                    gastosPorVehiculo.setNombreProveedor(fImportarConsumoCombustible.proveedor.getNombres() + " "
                            + fImportarConsumoCombustible.proveedor.getApellidos());

                    gastosFlota.setSucursalProveedor("" + fImportarConsumoCombustible.sucursalProveedor.getIdSucursal());
                    gastosFlota.setNumeroDeOrden("0");

                    gastosFlota.setNumeroFactura(row.getCell(0).toString());
                    gastosPorVehiculo.setNumeroRecibo(row.getCell(0).toString());

                    gastosPorVehiculo.setIdLineaFactura("1");

                    Date fecha = row.getCell(1).getDateCellValue();
                    long d = fecha.getTime();
                    java.sql.Date fecha2 = new java.sql.Date(d);
                    String cadena = "" + fecha2;

                    gastosPorVehiculo.setFechaRecibo("" + fecha2);//row.getCell(1).toString());
                    gastosFlota.setFechaFactura("" + fecha2);

                    gastosPorVehiculo.setNombreSucursal(row.getCell(2).toString());

                    gastosPorVehiculo.setCiudad(row.getCell(3).toString());
                    gastosPorVehiculo.setPlaca(row.getCell(5).toString());
                    gastosFlota.setVehiculo(row.getCell(5).toString());

                    cadena = row.getCell(9).toString();
                    gastosPorVehiculo.setDescripcionProductoServicio(row.getCell(9).toString());
                    if (cadena.contains("Extra")) {
                        gastosPorVehiculo.setCodigoSubcuenta(3);

                    } else if (cadena.contains("Corriente")) {
                        gastosPorVehiculo.setCodigoSubcuenta(2);

                    } else if (cadena.contains("ACPM")) {
                        gastosPorVehiculo.setCodigoSubcuenta(1);

                    } else if (cadena.contains("Diesel")) {
                        gastosPorVehiculo.setCodigoSubcuenta(1);
                    }
                    gastosPorVehiculo.setCantidad(row.getCell(10).toString());
                    gastosPorVehiculo.setValorUnitario(row.getCell(12).toString());
                    gastosPorVehiculo.setValorTotal(row.getCell(14).toString());
                    gastosFlota.setValorfactura(row.getCell(14).toString());

                    gastosPorVehiculo.setKilometraje(row.getCell(15).toString());
                    gastosFlota.setKilometraje(row.getCell(15).toString());

                    gastosPorVehiculo.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
                    gastosFlota.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
                    gastosPorVehiculo.setActivo("1");
                    gastosFlota.setActivo("1");
                    gastosPorVehiculo.setFlag(1);
                    gastosFlota.setFlag("1");

                    /* Datos provisionales */
                    gastosPorVehiculo.setAgencia(0);
                    gastosFlota.setAgencia("0");
                    gastosFlota.setRegional("0");
                    gastosFlota.setZona("0");
                    gastosFlota.setConductor("0");
                    gastosPorVehiculo.setConductor("0");

                    if (ini.getListaDeVehiculos() != null) {
                        for (CCarros car : ini.getListaDeVehiculos()) {
                            if (car.getPlaca().equals(gastosPorVehiculo.getPlaca())) {
                                for (CAgencias ag : ini.getListaDeAgencias()) {
                                    if (ag.getIdAgencia() == car.getAgencia()) {
                                        gastosPorVehiculo.setAgencia(ag.getIdAgencia());
                                        gastosFlota.setAgencia("" + ag.getIdAgencia());
                                        gastosFlota.setRegional("" + ag.getIdRegional());
                                        gastosFlota.setZona("" + ag.getIdZona());
                                        gastosFlota.setConductor(car.getConductor());
                                        gastosPorVehiculo.setConductor(car.getConductor());

                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        gastosPorVehiculo.setAgencia(1);
                    }
                    gastosFlota.setFormatoFotografia(".jpg");
                    listaDeGastosPorVehiculo = new ArrayList<>();
                    listaDeGastosPorVehiculo.add(gastosPorVehiculo);
                    gastosFlota.setListaGastosPorVehiculo(listaDeGastosPorVehiculo);

                    this.fImportarConsumoCombustible.listaDeGastosFlota.add(gastosFlota);
                    this.fImportarConsumoCombustible.listaDeGastosPorVehiculo.add(gastosPorVehiculo);

                    /* EL SISTEMA INFORMA  LA CANTIDAD DE REGISTROS INSERTADOS A LA BBDD */
                    //this.fImportarConsumoCombustible.listaDeGastosFlota.add(gastosFlota);
                    //sqlInsercionRemota.add(gastosPorVehiculo.getSentenciaInsertSQL());
                    System.out.println("lleva " + contadorDeFilas + "  filas para grabar consumos de combustible " + new Date());

                    /* ACTUALIZA LAS BARRAS DE PROGRESO */
                    porcentajeBarraSuperior = (int) (contadorDeFilas * 100) / numeroFilas;
                    //porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;
                    //porcentajeBarraSuperior = (int) (contadorDeTodasLasFilas * 100) / totalTodasLasFilas;

//                        this.fImportarArchivoExcelPacheco.jLabel2.setText("Sincronizando clientes");
//
                    this.fImportarConsumoCombustible.barraSuperior.setValue(porcentajeBarraSuperior);
                    this.fImportarConsumoCombustible.barraSuperior.repaint();
//
//                        this.fImportarArchivoExcelPacheco.barraSuperior.setValue(porcentajeBarraSuperior);
//                        this.fImportarArchivoExcelPacheco.barraSuperior.repaint();
                    Thread.sleep(1);

                }
                Thread.sleep(1);

            }

//  FIN DEL WHILE
            confirmado = true;
            fis.close();
            llenarTabla();
            this.fImportarConsumoCombustible.jbtnGrabarConsumos.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("Error en agregar  Registro " + ex + ";(" + contadorDeFilas + ") ");
            Logger.getLogger(HiloImportarConsumosCombustibeDesdeArchivo.class.getName()).log(Level.SEVERE, null, ex);

        }

        return confirmado;
    }

    @Override
    public void run() {
        switch (this.comando) {
            case "importarConsumosCombustible":
                if (importarConsumosCombustible()) {
                    JOptionPane.showMessageDialog(fImportarConsumoCombustible, "Datos importados correctamente desde el archivo", "OK", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(fImportarConsumoCombustible, "Error al llamar los datos del Archivo", "Error", JOptionPane.ERROR_MESSAGE);

                }
                break;
            case "grabar":
                if (grabar()) {
                    JOptionPane.showMessageDialog(fImportarConsumoCombustible, "Registros Guardados correctamente", "Ok", JOptionPane.INFORMATION_MESSAGE);
                    fImportarConsumoCombustible.jbtnGrabarConsumos.setEnabled(false);
                    fImportarConsumoCombustible.txtCarpetas.setText("");
                    fImportarConsumoCombustible.txtCarpetas.setEnabled(true);
                    fImportarConsumoCombustible.btnEscogerFolder.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(fImportarConsumoCombustible, "Error al guardar los registros", "Error", JOptionPane.ERROR_MESSAGE);
                    fImportarConsumoCombustible.jbtnGrabarConsumos.setEnabled(false);
                    fImportarConsumoCombustible.txtCarpetas.setEnabled(true);
                    fImportarConsumoCombustible.btnEscogerFolder.setEnabled(true);

                }
                break;

        }

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

    public void llenarTabla() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + fImportarConsumoCombustible.listaDeGastosFlota.size());

        DefaultTableModel modelo = (DefaultTableModel) fImportarConsumoCombustible.tblConsumos.getModel();

        for (GastosPorVehiculo obj : this.fImportarConsumoCombustible.listaDeGastosPorVehiculo) {
            int filaTabla2 = fImportarConsumoCombustible.tblConsumos.getRowCount();

            modelo.addRow(new Object[fImportarConsumoCombustible.tblConsumos.getRowCount()]);

            fImportarConsumoCombustible.tblConsumos.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getNumeroRecibo(), filaTabla2, 1);  // item
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getFechaRecibo(), filaTabla2, 2); // numero de manifiesto
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getNombreSucursal(), filaTabla2, 3); // numero de manifiesto
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getCiudad(), filaTabla2, 4); // fecha de distribucion
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getPlaca(), filaTabla2, 5); // placa del vehiculo
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getDescripcionProductoServicio(), filaTabla2, 6); // nombre del conductor
            fImportarConsumoCombustible.tblConsumos.setValueAt(obj.getCantidad(), filaTabla2, 7); // nombre del conductor
            fImportarConsumoCombustible.tblConsumos.setValueAt(nf.format(Double.parseDouble(obj.getValorUnitario())), filaTabla2, 8); // nombre del conductor
            fImportarConsumoCombustible.tblConsumos.setValueAt(nf.format(Double.parseDouble(obj.getValorTotal())), filaTabla2, 9); // nombre del conductor

        }

    }
}
