/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Reportes;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CFacturasAutorizadasDescuentos;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import com.opencsv.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lelopez
 */
public class HiloReporteFacturasAutorizadasConDescuentos implements Runnable {

    FReporteClientesConDescuento FReporteClientesConDescuento;
    public ArrayList<Vst_FacturasDescargadas> listaDeRegistros;
    //boolean xxx = true;
    
    Inicio ini;

    public HiloReporteFacturasAutorizadasConDescuentos(FReporteClientesConDescuento form) {
        this.FReporteClientesConDescuento = form;
        this.ini = form.ini;

    }

    @Override
    public void run() {

        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        FReporteClientesConDescuento.listaDeFacturasAutorizadasConDescuento = new ArrayList();

        /*Sentncia sql para crear los objetos */
        String sql = "select * "
                + "from view_FacturasConDescuentosAutorizados "
                + "where fechaDeVenta = CURRENT_DATE "
                + "AND valorFacturaSinIva>=condicion "
                + "AND idCliente IN(select codigoInterno from clientesConDescuentosAutorizados); ";

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                int totalRegistros = 0;
                int i = 0;
                int y;
                
                rst.last();
                totalRegistros = (rst.getRow());
                this.FReporteClientesConDescuento.barra.setValue(0);
                rst.beforeFirst();
                
                /*Valida que haya registros de la consulta  sino hay, sale de la funcion */
                if (totalRegistros == 0) {
                    y = 100;
                    this.FReporteClientesConDescuento.barra.setValue(y);
                    this.FReporteClientesConDescuento.barra.repaint();
                    Thread.sleep(10);
                    JOptionPane.showMessageDialog(FReporteClientesConDescuento, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);

                    return;
                }

                /*Se genera un nombre aleatorio para el archivo*/
                String clave = UUID.randomUUID().toString().substring(0, 8);
                String ruta = "tmp/" + clave + "_facturasConDescuentosAutorizados.csv";

                try (CSVWriter writer = new CSVWriter(new FileWriter(ruta), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);
                    // Desktop.getDesktop().open(new File("yourfile.csv"));

                }

                FReporteClientesConDescuento.setFile(new File(ruta));

                
                /*Empieza a armar la lista con los registros encontrados*/
                while (rst.next()) {
                    CFacturasAutorizadasDescuentos fcd = new CFacturasAutorizadasDescuentos();
                    fcd.setNumeroFactura(rst.getString("numeroFactura"));
                    fcd.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fcd.setIdCliente(rst.getString("idCliente"));
                    fcd.setNitCliente(rst.getString("nitCliente"));
                    fcd.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fcd.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fcd.setBarrio(rst.getString("barrio"));
                    fcd.setFormaDePago(rst.getInt("formaDePago"));
                    fcd.setCanal(rst.getInt("canal"));
                    fcd.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fcd.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fcd.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fcd.setValorRechazo(rst.getDouble("valorRechazo"));
                    fcd.setValorDescuento(rst.getDouble("valorDescuento"));
                    fcd.setValorTotalRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fcd.setIsFree(rst.getInt("isFree"));
                    fcd.setEstadoFactura(rst.getInt("estadoFactura"));
                    fcd.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fcd.setPorcentajeDesuento(rst.getDouble("porcentajeDesuento"));
                    fcd.setCondicion(rst.getString("condicion"));
                    FReporteClientesConDescuento.listaDeFacturasAutorizadasConDescuento.add(fcd);

                    i++;
                    y = (int) (i * 100) / totalRegistros;
                    this.FReporteClientesConDescuento.barra.setValue(y);
                    this.FReporteClientesConDescuento.barra.repaint();
                    Thread.sleep(10);

                }

                rst.close();
                st.close();
                con.close();

                //  Desktop.getDesktop().open(new File("tmp/" + clave + "_facturasConDescuento.csv""));
            }
            FReporteClientesConDescuento.llenarTablaFacturasConDescuentos();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(HiloReporteFacturasAutorizadasConDescuentos.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(FReporteClientesConDescuento, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteFacturasAutorizadasConDescuentos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(FReporteClientesConDescuento, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteFacturasAutorizadasConDescuentos.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

  
}
