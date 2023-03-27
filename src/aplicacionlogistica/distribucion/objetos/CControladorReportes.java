/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class CControladorReportes {
    ArrayList<Vst_FacturasDescargadas> listaDeFacturasDescargadas;
    Inicio ini;

    public CControladorReportes() {
    }

    public CControladorReportes(Inicio ini) {
       this.ini=ini; 
    }
     /**
     * Método que devuelve el listado de  las facturas descargadas entre un rango
     * de fechas definicas por el usuariodel manifiesto
     * 
     * @return un array con la lista de las facturas descargadas
     */
    public ArrayList<Vst_FacturasDescargadas> getListaDeFacturasDescargadas() {
        return listaDeFacturasDescargadas;
    }

     /**
     * Método que asigna el listado de  las facturas descargadas 
     * 
     * @param listaDeFacturasDescargadas lista de facturas descargadas
     */
    public void setListaDeFacturasDescargadas(ArrayList<Vst_FacturasDescargadas> listaDeFacturasDescargadas) {
        this.listaDeFacturasDescargadas = listaDeFacturasDescargadas;
    }
    
     /**
     * Método que crea y devuelve el listado de  las facturas descargadas entre un rango
     * de fechas definicas por el usuariodel manifiesto
     * 
     * @param fechaIncial fecha incial del reporte
     * @param fechaFinal fecha final del reporte
     * @param orderBy 1 por cliente, 
     * 2 por vendedor , 
     * 3 conductor ,
     * 4 por causal de rechazo
     * 
     * @return un array con la lista de las facturas descargadas
     */
    public ArrayList<Vst_FacturasDescargadas> getListaDeFacturasDescargadas(JDateChooser fechaIncial, JDateChooser fechaFinal, int orderBy) {
        
        String sql;
        Connection con;
        Statement st;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT * "
                + "FROM vst_movilizacionfacturasdescargadas "
                + "WHERE "
                + "fechaDistribucion >='" + Inicio.getFechaSql(fechaIncial) + "' "
                + "AND fechaDistribucion <='" + Inicio.getFechaSql(fechaFinal) + "' "
                + "AND idTipoDeMovimiento='3' ";
                switch(orderBy){
                    case 1:
                        sql += "order by nombreDeCliente,nombreCausalDeRechazo asc ;";
                        break;
                    case 2:
                        sql += "order by vendedor,nombreCausalDeRechazo asc ;";
                        break;
                    case 3:
                        sql += "order by nombreConductor,nombreCausalDeRechazo asc ;";
                        break;
                    case 4:
                        sql += "order by nombreCausalDeRechazo,vendedor,nombreConductor asc ;";
                        break;
                    case 5:
                        sql += "order by fechaDistribucion ,vendedor,nombreConductor,nombreCausalDeRechazo asc ;";
                        break;
                    
                }

        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                listaDeFacturasDescargadas = new ArrayList<>();
                
                while (rst.next()) {
                   Vst_FacturasDescargadas facsdes = new Vst_FacturasDescargadas();
                    facsdes.setIdCanal(rst.getInt("idCanal"));
                    facsdes.setNombreCanal(rst.getString("nombreCanal"));
                    facsdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facsdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facsdes.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                    facsdes.setVehiculo(rst.getString("vehiculo"));
                    facsdes.setNombreConductor(rst.getString("nombreConductor"));
                    facsdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facsdes.setDireccion(rst.getString("direccion"));
                    facsdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facsdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facsdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facsdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facsdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facsdes.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
                    facsdes.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facsdes.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
                    facsdes.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
                    facsdes.setVendedor(rst.getString("vendedor"));

                
                    listaDeFacturasDescargadas.add(facsdes);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return listaDeFacturasDescargadas;
    }
    
     /**
     * Método que crea y devuelve el listado de  las facturas descargadas entre un rango
     * de fechas definicas por el usuariodel manifiesto
     * 
     * @param fechaIncial fecha incial del reporte
     * @param fechaFinal fecha final del reporte
     * @param orderBy 1 por cliente, 
     * 2 por vendedor , 
     * 3 conductor ,
     * 4 por causal de rechazo
     * @param movimiento movimiento de la factura
     * 
     * @return un array con la lista de las facturas descargadas
     */
    public ArrayList<Vst_FacturasDescargadas> getListaDeFacturasDescargadas(JDateChooser fechaIncial, JDateChooser fechaFinal, int orderBy, int movimiento) {
        
        String sql;
        Connection con;
        Statement st;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT * "
                + "FROM vst_movilizacionfacturasdescargadas "
                + "WHERE "
                + "fechaDistribucion >='" + Inicio.getFechaSql(fechaIncial) + "' "
                + "AND fechaDistribucion <='" + Inicio.getFechaSql(fechaFinal) + "' "
                + "AND idTipoDeMovimiento='" + movimiento + "' ";
                switch(orderBy){
                    case 1:
                        sql += "order by nombreDeCliente,nombreCausalDeRechazo asc ;";
                        break;
                    case 2:
                        sql += "order by vendedor,nombreCausalDeRechazo asc ;";
                        break;
                    case 3:
                        sql += "order by nombreConductor,nombreCausalDeRechazo asc ;";
                        break;
                    case 4:
                        sql += "order by nombreCausalDeRechazo,vendedor,nombreConductor asc ;";
                        break;
                    case 5:
                        sql += "order by fechaDistribucion ,vendedor,nombreConductor,nombreCausalDeRechazo asc ;";
                        break;
                    
                }

        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                listaDeFacturasDescargadas = new ArrayList<>();
                
                while (rst.next()) {
                   Vst_FacturasDescargadas facsdes = new Vst_FacturasDescargadas();
                    facsdes.setIdCanal(rst.getInt("idCanal"));
                    facsdes.setNombreCanal(rst.getString("nombreCanal"));
                    facsdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facsdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facsdes.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                    facsdes.setVehiculo(rst.getString("vehiculo"));
                    facsdes.setNombreConductor(rst.getString("nombreConductor"));
                    facsdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facsdes.setDireccion(rst.getString("direccion"));
                    facsdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facsdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facsdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facsdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facsdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facsdes.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
                    facsdes.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facsdes.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
                    facsdes.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
                    facsdes.setVendedor(rst.getString("vendedor"));

                
                    listaDeFacturasDescargadas.add(facsdes);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return listaDeFacturasDescargadas;
    }
    
    
      /**
     * Método que crea y devuelve el listado de  las facturas descargadas entre un rango
     * de fechas definicas por el usuariodel manifiesto
     * 
     * @param fechaIncial fecha incial del reporte
     * @param fechaFinal fecha final del reporte
     * @param codigoCliente código del cliente
     * @param nombreCliente nombre ó parte del nombre del cliente
     * 
     * @return un array con la lista de las facturas descargadas
     */
    public ArrayList<Vst_FacturasDescargadas> getListaDeFacturasDescargadas(JDateChooser fechaIncial, JDateChooser fechaFinal, String codigoCliente,String nombreCliente, int tipoMovimiento) {
        
        String sql;
        Connection con;
        Statement st;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT * "
                + "FROM vst_movilizacionfacturasdescargadas "
                + "WHERE "
                + "fechaDistribucion >='" + Inicio.getFechaSql(fechaIncial) + "' "
                + "AND fechaDistribucion <='" + Inicio.getFechaSql(fechaFinal) + "' "
                + "AND idTipoDeMovimiento='" + tipoMovimiento +"'  ";
 
        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                listaDeFacturasDescargadas = new ArrayList<>();
                
                while (rst.next()) {
                   Vst_FacturasDescargadas facsdes = new Vst_FacturasDescargadas();
                    facsdes.setIdCanal(rst.getInt("idCanal"));
                    facsdes.setNombreCanal(rst.getString("nombreCanal"));
                    facsdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facsdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facsdes.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                    facsdes.setVehiculo(rst.getString("vehiculo"));
                    facsdes.setNombreConductor(rst.getString("nombreConductor"));
                    facsdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facsdes.setDireccion(rst.getString("direccion"));
                    facsdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facsdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facsdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facsdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facsdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facsdes.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
                    facsdes.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facsdes.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
                    facsdes.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
                    facsdes.setVendedor(rst.getString("vendedor"));

                
                    listaDeFacturasDescargadas.add(facsdes);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return listaDeFacturasDescargadas;
    }
    
    
     /**
     * Método que crea y devuelve el listado de  las facturas descargadas entre un rango
     * de fechas definicas por el usuariodel manifiesto
     * 
     * @param fechaIncial fecha incial del reporte
     * @param fechaFinal fecha final del reporte
     * @param vendedor nombre del vendedor
     * @param orderBy 1 por cliente, 
     * 2 por vendedor , 
     * 3 conductor ,
     * 4 por causal de rechazo
     * 
     * @return un array con la lista de las facturas descargadas
     */
    public ArrayList<Vst_FacturasDescargadas> getListaDeFacturasRechazadas(Date fechaIncial, Date fechaFinal, String vendedor, int orderBy) {
        
        String sql;
        Connection con;
        Statement st;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT * "
                + "FROM vst_movilizacionfacturasdescargadas;"
                + "WHERE "
                + "fechaDistribucion >='" + fechaIncial + "'"
                + "AND fechaDistribucion <='" + fechaFinal + "'"
                 +"AND vendedor ='" + vendedor + "'"
                + "AND MovimientoFactura='3' ";
                
               

        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                listaDeFacturasDescargadas = new ArrayList<>();
                
                while (rst.next()) {
                   Vst_FacturasDescargadas facsdes = new Vst_FacturasDescargadas();
                  facsdes.setIdCanal(rst.getInt("idCanal"));
                    facsdes.setNombreCanal(rst.getString("nombreCanal"));
                    facsdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facsdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facsdes.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                    facsdes.setVehiculo(rst.getString("vehiculo"));
                    facsdes.setNombreConductor(rst.getString("nombreConductor"));
                    facsdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facsdes.setDireccion(rst.getString("direccion"));
                    facsdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facsdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facsdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facsdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facsdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facsdes.setIdTipoDeMovimiento(rst.getInt("idTipoDeMovimiento"));
                    facsdes.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facsdes.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
                    facsdes.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
                    facsdes.setVendedor(rst.getString("vendedor"));

                    listaDeFacturasDescargadas.add(facsdes);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return listaDeFacturasDescargadas;
    }
}
