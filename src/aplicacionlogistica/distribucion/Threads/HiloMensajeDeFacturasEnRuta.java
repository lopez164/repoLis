/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloMensajeDeFacturasEnRuta implements Runnable {
   
    String numeroManifiesto;
    Inicio ini;

   

    /**
     * Constructor de clase
     *
     * @param ini
     * @param mensaje
   
     */
    public  HiloMensajeDeFacturasEnRuta(Inicio ini, String numeroManifiesto) {
       
        this.ini = ini;
        this.numeroManifiesto=numeroManifiesto;

    }

    @Override
    public void run() {
        enviarMensajes(numeroManifiesto);
         

    }

    private ArrayList<String[]>  enviarMensajes(String numeroManifiesto) {
        ArrayList<String[]> listaDeMensajes = new ArrayList();
        String[] cadena = new String[2];
        String telefono;
        Connection con;
        Statement st;
        String sql = "";
        ResultSet rst;
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloMensajeDeFacturasEnRuta");

        try {
            sql = "select c.nombreDeCliente as cliente,c.celularCliente,format(sum(f.valorTotalFactura),2) as valor"
                    + "from facturaspormanifiesto fm "
                    + "join manifiestosdedistribucion m "
                    + "join clientescamdun c "
                    + "join facturascamdun f "
                    + "where "
                    + "m.consecutivo=fm.numeroManifiesto and "
                    + "fm.numeroFactura=f.numeroFactura and "
                    + "f.cliente=c.codigoInterno and "
                    + "c.celularCliente <> '0' and "
                    + "m.consecutivo='" + numeroManifiesto + "' "
                    + "group by c.codigoInterno;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    cadena[0] = "Estimado(a) Cliente(a) " + rst.getString("nombreDeCliente") + ", con la presentes le estamos informando "
                            + "que en el dia de hoy le estara llegando su pedido por valor de $ " + rst.getDouble("valor") + "";
                   
                    cadena[1] = rst.getString("celularCliente");
                   
                    listaDeMensajes.add(cadena);

                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(HiloMensajeDeFacturasEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return  listaDeMensajes;
    }
}
