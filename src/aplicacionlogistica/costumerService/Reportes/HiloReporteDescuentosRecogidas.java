/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Reportes;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CDescuentosPorFactura;
import aplicacionlogistica.costumerService.objetos.CRecogidasPorFactura;
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
public class HiloReporteDescuentosRecogidas implements Runnable {

    FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas;
    public ArrayList<Vst_FacturasDescargadas> listaDeRegistros;
    boolean xxx = true;
    String clave;
    Inicio ini;

    public HiloReporteDescuentosRecogidas(FReporteDescuentos_Recogidas form) {
        this.fReporteDescuentos_Recogidas = form;
        this.ini = form.ini;

    }

    @Override
    public void run() {

        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        this.fReporteDescuentos_Recogidas.lblCirculoDeProgreso.setVisible(true);
        this.fReporteDescuentos_Recogidas.listaDeDescuentos = new ArrayList();
        this.fReporteDescuentos_Recogidas.listaDeRecogidas = new ArrayList();


        /*Sentncia sql para crear los objetos */
        String sql = "SELECT movi,idMov,factura,fechaVenta, documento,nombreCliente,direccion, valor "
                + "FROM ( "
                + "SELECT 'Descuento' as movi,df.iddescuentoporfactura as idMov,df.numeroFactura as factura,f.fechaDeVenta as fechaVenta,"
                + "c.nombreDeCliente as nombreCliente,f.direccion as direccion, df.numeroDocumento as documento,df.valorDescuento as valor "
                + "FROM descuentoporfactura df "
                + "join facturascamdun f on f.numeroFactura=df.numeroFactura "
                + "join clientescamdun c on c.codigoInterno=f.cliente "
                + "WHERE  f.fechaDeVenta>='" + fReporteDescuentos_Recogidas.fechaIncial + "'and "
                + "f.fechaDeVenta<='" + fReporteDescuentos_Recogidas.fechaFinal + "' "
                + "UNION ALL "
                + "SELECT 'Recogida' as movi,rf.idrecogidaporfactura as idMov,rf.numeroFactura as factura,f.fechaDeVenta as fechaVenta,"
                + "c.nombreDeCliente as nombreCliente,f.direccion as direccion, rf.numeroDocumento as documento,rf.valorrecogida as valor "
                + "FROM recogidaporfactura rf "
                + "join facturascamdun f on f.numeroFactura=rf.numeroFactura "
                + "join clientescamdun c on c.codigoInterno=f.cliente "
                + "WHERE  f.fechaDeVenta>='" + fReporteDescuentos_Recogidas.fechaIncial + "'and "
                + "f.fechaDeVenta<='" + fReporteDescuentos_Recogidas.fechaFinal + "' "
                + ") movimientos;";

        long startTime = fReporteDescuentos_Recogidas.fechaIncial.getTime();
        long endTime = fReporteDescuentos_Recogidas.fechaFinal.getTime();
        long diffTime = (endTime - startTime) + 1;

        int diffDays = (int) (diffTime / (1000 * 60 * 60 * 24));

        if (diffDays <= 0) {
            diffDays = 1;
        }

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                /*Se genera un nombre aleatorio para el archivo*/
                clave = UUID.randomUUID().toString().substring(0, 8);

                try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/" + clave + ".csv"), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);
                    // Desktop.getDesktop().open(new File("yourfile.csv"));

                }

                fReporteDescuentos_Recogidas.setFile(new File("tmp/" + clave + ".csv"));

                rst.last();
                int totalRegistros = (rst.getRow() / diffDays);
                int valor = 0;
                this.fReporteDescuentos_Recogidas.barra.setValue(0);
                
                rst.beforeFirst();
                valor = totalRegistros / 100;
                int i = 0;
                int avance = 0;
                int x = 0;
                
                while(rst.next()){
                    
                    if (x > valor) {
                        avance++;
                        this.fReporteDescuentos_Recogidas.barra.setValue((int) avance);
                        x = 0;
                    } else {
                        x++;
                    }
                    
                   if("Descuento".equals(rst.getString("movi"))){
                       CDescuentosPorFactura df = new CDescuentosPorFactura();
                       df.setIddescuentoporfactura(rst.getInt("idMov"));
                       df.setNumeroDocumento(rst.getString("documento"));
                       df.setNumeroFactura(rst.getString("factura"));
                       df.setFechaDeVenta(rst.getDate("fechaVenta"));
                       df.setNombreCliente(rst.getString("nombreCliente"));
                       df.setDireccionCliente(rst.getString("direccion"));
                       df.setValorDescuento(rst.getDouble("valor"));
                       fReporteDescuentos_Recogidas.listaDeDescuentos.add(df);
            
                       
                   }else{
                       CRecogidasPorFactura rf = new CRecogidasPorFactura();
                       rf.setIdRecogidaPorFactura(rst.getInt("idMov"));
                       rf.setNumeroDocumento(rst.getString("documento"));
                       rf.setNumeroFactura(rst.getString("factura"));
                       rf.setFechaDeVenta(rst.getDate("fechaVenta"));
                       rf.setNombreCliente(rst.getString("nombreCliente"));
                       rf.setDireccionCliente(rst.getString("direccion"));
                       rf.setValorRecogida(rst.getDouble("valor"));
                       fReporteDescuentos_Recogidas.listaDeRecogidas.add(rf);
                   }
                    Thread.sleep(10);
                }
                
                 this.fReporteDescuentos_Recogidas.barra.setValue(100);
                rst.close();
                st.close();
                con.close();

                //  Desktop.getDesktop().open(new File("tmp/" + clave +".csv"));

            }
            fReporteDescuentos_Recogidas.llenarTablaDescuentos();
            fReporteDescuentos_Recogidas.llenarTablaRecogidas();
            this.fReporteDescuentos_Recogidas.lblCirculoDeProgreso.setVisible(false);
//                if (this.form.listaDeRegistros.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
//                }

        } catch (SQLException ex) {
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(HiloReporteDescuentosRecogidas.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(fReporteDescuentos_Recogidas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteDescuentosRecogidas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(fReporteDescuentos_Recogidas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteDescuentosRecogidas.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private Vst_FacturasDescargadas llenarRegistro(ResultSet rst) {
        Vst_FacturasDescargadas registro = new Vst_FacturasDescargadas();
        try {

            registro.setIdCanal(rst.getInt("idCanal"));
            registro.setNombreCanal(rst.getString("nombreCanal"));
            registro.setNumeroManifiesto(rst.getString("numeroManifiesto"));
            registro.setAdherencia(rst.getInt("adherencia"));
            registro.setNumeroFactura(rst.getString("numeroFactura"));
            registro.setFechaDistribucion(rst.getDate("fechaDistribucion"));
            registro.setFechaDeVenta(rst.getDate("fechaDeVenta"));
            registro.setVehiculo(rst.getString("vehiculo"));
            registro.setTipoContrato(rst.getString("tipoContrato"));
            registro.setConductor(rst.getString("conductor"));
            registro.setNombreConductor(rst.getString("nombreConductor"));
            registro.setNombreDespachador(rst.getString("nombreDespachador"));
            registro.setNombreDeRuta(rst.getString("nombreDeRuta"));
            registro.setTipoRuta(rst.getString("tipoRuta"));
            registro.setNombreDeCliente(rst.getString("nombreDeCliente"));
            registro.setDireccion(rst.getString("direccion"));
            registro.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
            registro.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
            registro.setValorRechazo(rst.getDouble("valorRechazo"));
            registro.setValorDescuento(rst.getDouble("valorDescuento"));
            registro.setValorRecaudado(rst.getDouble("valorRecaudado"));
            registro.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
            registro.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
            registro.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
            registro.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
            registro.setCompetencia(rst.getString("competencia"));
            registro.setVendedor(rst.getString("vendedor"));
            registro.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
            registro.setFechaReal(rst.getDate("fechaReal"));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(fReporteDescuentos_Recogidas, "NO hay registros para mostrar", "No hay Datos", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloReporteDescuentosRecogidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registro;
    }

}
