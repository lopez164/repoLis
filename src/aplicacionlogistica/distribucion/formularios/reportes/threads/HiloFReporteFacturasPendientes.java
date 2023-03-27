/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.threads;

import aplicacionlogistica.distribucion.Threads.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import com.opencsv.CSVWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author Usuario
 */
public class HiloFReporteFacturasPendientes implements Runnable {

    public static boolean band = false;

    Inicio ini;
    ArrayList<Vst_Factura> listaDeFacturasSinMovimiento = null;
    boolean xxx;

    FReporteFacturasPendientes form;

    public HiloFReporteFacturasPendientes(FReporteFacturasPendientes form) {
        this.ini = form.ini;
        this.form = form;

    }

    public HiloFReporteFacturasPendientes(FReporteFacturasPendientes form, boolean xxx) {
        this.form = form;
        this.xxx = xxx;
        this.ini = form.ini;
    }

    @Override
    public void run() {

        this.form.lblCirculoDeProgreso.setVisible(true);
        if (xxx) {
            ResultSet rst = null;
            Statement st = null;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasSinMovimiento");

            String sql = null;
            this.form.fechaIncial = ini.getFechaSql(this.form.jFechaInicial);
            this.form.fechaFinal = ini.getFechaSql(this.form.jFechaFinal);

            /*
        sql = "SELECT * FROM vst_fcturas where fechaDeVenta>='" + this.form.fechaIncial + "' "
                + " and fechaDeVenta<='" + this.form.fechaFinal + "' "
                + " and direccionDeCliente<>'ANULADA' "
                + " and salidasDistribucion< 1 or "
                + "(estadoFactura=6 or estadoFactura=7);"; */
////            sql = "SELECT  numeroFactura,fechaDeVenta,idCliente, nitCliente,"
////                    + "nombreDeCliente,direccionDeCliente,barrio,ciudad,telefonoCliente,emailCliente,"
////                    + "latitud,longitud,nombreEstablecimiento,vendedor,telefonoVendedor,formaDePago,"
////                    + "pago,canal,nombreCanalDeVenta,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
////                    + "valorRechazo, valorDescuento,valorTotalRecaudado,zona,regional,agencia,isFree,"
////                    + "estadoFactura,if(estadoFactura=1,'SIN  MOVIMIENTO',nombreEstadoFactura) AS nombreEstadoFactura,"
////                    + "activo,salidasDistribucion,trasmitido,"
////                    + "numeroDescuento,numeroRecogida, pesofactura "
////                    + "FROM vst_fcturas "
////                    
////                    + "where fechaDeVenta>='" + this.form.fechaIncial + "' "
////                    + "and fechaDeVenta<='" + this.form.fechaFinal + "' "
////                    + "and direccionDeCliente<>'ANULADA' "
////                    + "and salidasDistribucion < 1 or (fechaDeVenta>='" + this.form.fechaIncial + "' "
////                    + "and fechaDeVenta<='" + this.form.fechaFinal + "' "
////                    + "and salidasDistribucion > 0 and "
////                    + "( estadoFactura=6 or estadoFactura=7)) "
////                    + "or((estadoFactura = 1 and isFree = 0) and fechaDeVenta>='" + this.form.fechaIncial + "' and fechaDeVenta<='" + this.form.fechaFinal + "') "
////                    + "order by isFree;";
            sql = "SELECT "
                    + " f.numeroFactura AS numeroFactura,"
                    + " f.fechaDeVenta AS fechaDeVenta,"
                    + " f.cliente AS idCliente,"
                    + "  c.nitCliente AS nitCliente,"
                    + "  c.nombreDeCliente AS nombreDeCliente,"
                    + "  f.direccion AS direccionDeCliente,"
                    + "  f.barrio AS barrio,"
                    + "  c.ciudad AS ciudad,"
                    + "  c.celularCliente AS telefonoCliente,"
                    + "  c.emailCliente AS emailCliente,"
                    + "  c.latitud AS latitud,"
                    + " c.longitud AS longitud,"
                    + " c.nombreEstablecimiento AS nombreEstablecimiento,"
                    + "  f.vendedor AS vendedor,"
                    + "  f.telefonoVendedor AS telefonoVendedor,"
                    + "  f.formaDePago AS formaDePago,"
                    + "  IF((f.formaDePago = 0),"
                    + "     'NO DEFINIDO',"
                    + "    IF((f.formaDePago = 1),"
                    + "        'CONTADO',"
                    + "        'CREDITO')) AS pago,"
                    + "  f.canal AS canal,"
                    + " cv.nombreCanalDeVenta AS nombreCanalDeVenta,"
                    + "  f.valorFacturaSinIva AS valorFacturaSinIva,"
                    + "  f.valorIvaFactura AS valorIvaFactura,"
                    + "  f.valorTotalFactura AS valorTotalFactura,"
                    + "  f.valorRechazo AS valorRechazo,"
                    + "  f.valorDescuento AS valorDescuento,"
                    + " f.valorTotalRecaudado AS valorTotalRecaudado,"
                    + "  f.zona AS zona,"
                    + "  f.regional AS regional,"
                    + "  f.agencia AS agencia,"
                    + "  f.isFree AS isFree,"
                    + "  f.estadoFactura AS estadoFactura,"
                    + "  tmf.nombreTipoDeMovimiento AS nombreEstadoFactura,"
                    + "  f.activo AS activo,"
                    + "  f.flag AS salidasDistribucion,"
                    + "  f.trasmitido AS trasmitido,"
                    + "  f.numeroDescuento AS numeroDescuento,"
                    + " f.numeroRecogida AS numeroRecogida,"
                    + "  f.pesofactura AS pesofactura,"
                    + " f.plazoDias AS plazoDias,"
                    + "  f.prefijo AS prefijo,"
                    + "  f.numero AS numero,"
                    + "  f.fpContado AS fpContado,"
                    + "  f.observaciones AS observaciones,"
                    + "  fm.numeroManifiesto as numeroManifiesto,"
                    + "  m.conductor as condcutor,"
                    + "  concat(p.nombres,' ',p.apellidos) as nombreConductor "
                    + "  FROM "
                    + "  facturas f "
                    + "  Left outer Join facturaspormanifiesto fm on fm.numeroFactura= f.numeroFactura "
                    + "  left outer join manifiestosdedistribucion m on m.consecutivo= fm.numeroManifiesto "
                    + "  left outer join personas p on p.cedula=m.conductor "
                    + "  JOIN clientes c ON f.cliente = c.codigoInterno "
                    + "  JOIN tiposcanaldeventas cv ON f.canal = cv.idCanalDeVenta "
                    + "  JOIN tiposdemovimientosmanifiestosfacturas tmf ON tmf.idtipoDeMovimiento = f.estadoFactura "
                    + "  where ";
            if ((ini.getPropiedades().getProperty("idOperador").equals("2"))) {
                if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                    // fechaInicial = " select concat(date_sub(current_date(), interval 1 day),' 05:00:00')";
                    // fechaFinal = " select concat(current_date(),' 04:59:59')";
                    sql += "f.fechaIng >=(select concat(date_sub(current_date(), interval 1 day),' 05:00:00')) AND "
                            + "f.fechaIng <= (select concat(current_date(),' 04:59:59'))  ";

                } else {
                    sql += "f.fechaIng >=(select concat(current_date(),' 05:00:00')) AND "
                            + "f.fechaIng <= (select concat(date_add(current_date(), interval 1 day),' 04:59:59'))  ";

                }

            } else {
                sql += "fechaDeVenta= curdate() ";

            }
//                    + "and salidasDistribucion < 1 or "
//                    + "(estadoFactura=6 or estadoFactura=7) "
            sql += "order by  f.isFree, f.estadoFactura desc ;";

            try {
                if (con != null) {
                    this.form.listaDeFacturasSinMovimiento = new ArrayList<>();
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    rst.last();
                    int valor = rst.getRow();
                    this.form.barra.setValue(0);
                    rst.first();

                    valor = valor / 100;
                    int i = 0;
                    int avance = 0;
                    Vst_FacturasPorManifiesto registro = new Vst_FacturasPorManifiesto();
                    rst.beforeFirst();

                    /*Se genera un nombre aleatorio para el archivo*/
                    String clave = UUID.randomUUID().toString().substring(0, 8);

                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/" + clave + ".csv"), '\t')) {
                        //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));

                    }

                    this.form.reporteFinal = new File("tmp/" + clave + ".csv");

                    // Desktop.getDesktop().open(new File("tmp/" + clave +".csv"));
                    /**
                     * ***********************************************************
                     */
                    rst.beforeFirst();

                    while (rst.next()) {
                        if (i > valor) {
                            avance++;
                            this.form.barra.setValue((int) avance);
                            i = 0;
                        }
                        registro = llenarRegistro(rst);

                        // this.form.listaDeFacturasSinMovimiento.add(registro);
                        i++;
                        Thread.sleep(10);

                    }
                    this.form.barra.setValue(100);
                    this.form.jBtnExcel1.setEnabled(true);
                    this.form.llenarTablaListaDeFacturasSinMovimiento();
                    this.form.btnExportarExcel.setEnabled(true);
                    this.form.btnAceptar.setEnabled(true);
                    this.form.lblCirculoDeProgreso.setVisible(false);
                }
                rst.close();
                st.close();
                con.close();
                if (this.form.listaDeFacturasSinMovimiento.isEmpty()) {
                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
                }

            } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                band = true;
            } catch (Exception ex) {
                Logger.getLogger(HiloFReporteFacturasPendientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                exportarExcel();
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFReporteFacturasPendientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private Vst_FacturasPorManifiesto llenarRegistro(ResultSet rst) throws InterruptedException {
        Vst_FacturasPorManifiesto registro = new Vst_FacturasPorManifiesto();
        try {
            System.out.println("Cargando  -> " + new Date());
            Vst_FacturasPorManifiesto factura = new Vst_FacturasPorManifiesto(ini);

            factura.setNumeroFactura(rst.getString("numeroFactura"));
            factura.setFechaDeVenta(rst.getDate("fechaDeVenta"));
            factura.setCliente(rst.getString("idCliente"));
            factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
            factura.setDireccionCliente(rst.getString("direccionDeCliente"));
            factura.setBarrio(rst.getString("barrio"));
            factura.setCiudad(rst.getString("ciudad"));
            factura.setTelefonoCliente(rst.getString("telefonoCliente"));
            factura.setVendedor(rst.getString("vendedor"));
            factura.setFormaDePago(rst.getString("formaDePago"));
            // factura.setPago(rst.getString("pago"));
            factura.setCanal(rst.getInt("canal"));
            //factura.setNombreCanalDeVenta(rst.getString("nombreCanalDeVenta"));
            factura.setIsFree(rst.getInt("isFree"));
            factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
            factura.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
            factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
            factura.setValorRechazo(rst.getDouble("valorRechazo"));
            factura.setValorDescuento(rst.getDouble("valorDescuento"));
            factura.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
            factura.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
            factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
            factura.setNombreConductor(rst.getString("nombreConductor"));

            factura.setEstadoFactura(rst.getInt("estadoFactura"));

            factura.setObservaciones(rst.getString("observaciones"));
            this.form.listaDeFacturasSinMovimiento.add(factura);

            System.out.println("Cargando facturaPor Manifiesto número -> " + factura.getNumeroFactura());

            //arrFacturasPorManifiesto.add(fxm);
            System.out.println("tiempo 2 " + new Date());
            Thread.sleep(10);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloFReporteFacturasPendientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registro;
    }

    public void exportarExcel() throws InterruptedException {
        String nombreArchivo = "Inventario.xls";
        String rutaArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "manifiestos/" + nombreArchivo;
        String hoja = "Hoja1";
        this.form.barra.setValue(0);
        this.form.jLabel1.setVisible(true);
        this.form.lblCirculoDeProgreso.setVisible(true);

        new Thread(new HiloAux(form.barra, form.jLabel1)).start();

        //cabecera de la hoja de excel
        String[] header = new String[]{"numeroFactura", "fechaDeVenta", "nombreCanalDeVenta", "cliente",
            "nombreDeCliente", "direccionDeCliente", "barrio", "ciudad", "telefonoCliente",
            "vendedor", "valorFacturaSinIva", "valorTotalFactura"};

        //contenido de la hoja de excel
        //String[][] document = new String[form.listaDeRegistros.size()][header.length];
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja1 = libro.createSheet(hoja);

        int valor = form.listaDeFacturasSinMovimiento.size() / 100;
        int k = 0;
        int avance = 0;

        //poner negrita a la cabecera
        int i = 0;
        CellStyle style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        HSSFRow row = hoja1.createRow(i);//se crea las filas
        for (int j = 0; j < header.length; j++) {
            //para la cabecera
            HSSFCell cell = row.createCell(j);//se crea las celdas para la cabecera, junto con la posición
            cell.setCellStyle(style); // se añade el style crea anteriormente 
            cell.setCellValue(header[j]);//se añade el contenido
            hoja1.autoSizeColumn(j);
            Thread.sleep(10);
        }
        i++;
        /*RESTO DE INFORME*/

        for (Vst_FacturasPorManifiesto obj : form.listaDeFacturasSinMovimiento) {
            if (k > valor) {
                avance++;
                this.form.barra.setValue((int) avance);
                k = 0;
                Thread.sleep(10);
            }
            row = hoja1.createRow(i);//se crea las filas
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(obj.getNumeroFactura());  // item
            // hoja1.autoSizeColumn(0);
            cell = row.createCell(1);
            cell.setCellValue("" + obj.getFechaDeVenta()); // numero de manifiesto

            cell = row.createCell(2);
            cell.setCellValue("" + obj.getNombreCanal()); // numero de manifiesto
            //hoja1.autoSizeColumn(1);
            cell = row.createCell(3);
            cell.setCellValue(obj.getCliente()); // fecha de distribucion
            //hoja1.autoSizeColumn(2);
            cell = row.createCell(4);
            cell.setCellValue(obj.getNombreDeCliente()); // placa del vehiculo
            //hoja1.autoSizeColumn(3);
            cell = row.createCell(5);
            cell.setCellValue(obj.getDireccionCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(4);
            cell = row.createCell(6);
            cell.setCellValue("" + obj.getBarrio()); // nombre del conductor
            //hoja1.autoSizeColumn(5);
            cell = row.createCell(7);
            cell.setCellValue("" + obj.getCiudad()); // nombre del conductor
            //hoja1.autoSizeColumn(6);
            cell = row.createCell(8);
            cell.setCellValue(obj.getTelefonoCliente()); // nombre del conductor
            //hoja1.autoSizeColumn(7);
            cell = row.createCell(9);
            cell.setCellValue(obj.getVendedor()); // nombre del conductor
            //hoja1.autoSizeColumn(8);
            cell = row.createCell(10);
            cell.setCellValue(obj.getValorFacturaSinIva()); // nombre del conductor
            //hoja1.autoSizeColumn(9);
            cell = row.createCell(11);
            cell.setCellValue(obj.getValorTotalFactura()); // nombre del conductor
            //hoja1.autoSizeColumn(10);

            //hoja1.autoSizeColumn(25);
            i++;
            k++;
            Thread.sleep(10);
        }

        File file;
        file = new File(rutaArchivo);

        try {

            if (file.exists()) {// si el archivo existe se elimina
                file.delete();
                System.out.println("Archivo eliminado");

            }
            FileOutputStream fileOuS = new FileOutputStream(new File(rutaArchivo));
            libro.write(fileOuS);
            this.form.barra.setValue(100);
            this.form.lblCirculoDeProgreso.setVisible(false);
            this.form.jLabel1.setText("Archivo exportado satisfactoriamente");
            this.form.jLabel1.setText("Archivo exportado satisfactoriamente");
            fileOuS.flush();
            fileOuS.close();

            System.out.println("Archivo Creado");

            Desktop.getDesktop().open(new File(rutaArchivo));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
