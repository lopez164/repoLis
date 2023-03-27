/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.picking;

import aplicacionlogistica.distribucion.formularios.reportes.ReporteFacturasEnDistribucion.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanalHilo;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasEnDistribucion;
import com.opencsv.CSVWriter;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloReporteFacturasPendientesenPicking implements Runnable {

    Inicio ini;
    FReporteFacturasPendientesEnPicking form;
    List<List<String>> listaDeFacturas = null;
    String sql;
    String rutaArchivo;
    java.sql.Date fecha;
    int caso;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param form
     * @param caso
     */
    public HiloReporteFacturasPendientesenPicking(FReporteFacturasPendientesEnPicking form, int caso) {
        this.form = form;
        this.ini = form.ini;
       
        this.caso = caso;

    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        fecha = (java.sql.Date) ini.getFechaSql(this.form.jFechaInicial);
        rutaArchivo = form.ini.getRutaDeApp() + "reportes/reporteFacturasPendientesAlistamiento_" + fecha + ".csv";
        switch (caso) {
            case 0:
                nuevo();
                break;
            case 1:
        {
            try {
                exportarCSV();
                
            } catch (IOException ex) {
                Logger.getLogger(HiloReporteFacturasPendientesenPicking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                break;
            case 2:
                break;
        }

    }

    private void nuevo() throws HeadlessException {
        ResultSet rst = null;
        Statement st;
        Connection con;
        int i = 0;
        try {

            listaDeFacturas = new ArrayList();
            List<String> factura = null;

            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            

            factura = new ArrayList();
            sentenciaSQL();

            if (con != null) {
                st = con.createStatement();

                rst = st.executeQuery(sql);

                int rstSize = rst.getFetchSize() / 100;

                while (rst.next()) {
                    if (i >= rstSize) {
                        form.barra.setValue(form.barra.getValue() + i);
                        i = 0;
                    }
                    System.out.println("Cargando  -> " + new Date());
                    factura = new ArrayList();
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    //factura.add("" + rst.getString("item"));
                    factura.add("" + rst.getDate("fecha"));
                    factura.add("" + rst.getString("numeroFactura"));
                    factura.add("" + rst.getInt("idManifiestopicking"));
                    factura.add("" + rst.getString("destino"));
                    factura.add("" + rst.getString("nombreDeCliente"));
                    factura.add("" + rst.getString("direccion"));
                    factura.add("" + rst.getDouble("valorTotalFactura"));

                    System.out.println("Cargando facturas # -> " + rst.getString("numeroFactura"));

                    listaDeFacturas.add(factura);

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(10);

                    i++;
                }
                form.barra.setValue(100);
                i = 0;
                rst.close();
                st.close();
                con.close();
                //ini.setListaDeFacturasEnDistribucion(listaDeFacturas);
                form.listaDeFacturas = listaDeFacturas;
                Thread.sleep(1);
                form.llenarTablabitacora();
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (SQLException ex) {
            Logger.getLogger(HiloReporteFacturasPendientesenPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportarCSV() throws IOException {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
                
        sentenciaSQL();

        // con = CConexiones.GetConnection(this.form.ini.getCadenaRemota(), this.form.ini.getUsuarioBDRemota(), this.form.ini.getClaveBDRemota());
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(this.sql);

                /*Se exporta el resultado a un archivo plano .CSV */
                try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);
                    // Desktop.getDesktop().open(new File("yourfile.csv"));

                   
                   
                    
                } catch (Exception ex) {

                }
                
            } catch (SQLException ex) {
                Logger.getLogger(FReporteFacturacionPorCanalHilo.class.getName()).log(Level.SEVERE, null, ex);

            }
             Desktop.getDesktop().open(new File(rutaArchivo));
        }

    }

    private void sentenciaSQL() {
        sql = "SELECT DATE_FORMAT(fp.fechaIng,\"%Y-%m-%d\") as fecha,fp.numeroFactura,"
                + "fp.idManifiestopicking,m.destino,c.nombreDeCliente,"
                + "f.direccion, f.valorTotalFactura "
                + "FROM documentosEnFacturacion df "
                + "join facturasenpicking fp on fp.numeroFactura=df.numeroFactura "
                + "join manifiestosdepicking m on fp.idManifiestopicking=m.idmanifiestosdepicking "
                + "left outer join facturascamdun f on f.numeroFactura=fp.numeroFactura "
                + "left outer join clientescamdun  c on c.codigoInterno=f.cliente "
                + "where df.listo=0 and "
                + "fp.fechaIng>='" + fecha + " 00:00:00' and "
                + "fp.fechaIng<='" + fecha + " 23:59:59'; "
                + ";";

    }
}
