/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CBitacoraSms;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloEnviarSMSClientes implements Runnable {

    public static boolean band = false;
    Inicio ini;
    CManifiestosDeDistribucion manifiesto;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    ArrayList<String> listado = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param manifiesto
     */
    public HiloEnviarSMSClientes(Inicio ini, CManifiestosDeDistribucion manifiesto) {
        this.ini = ini;
        this.manifiesto = manifiesto;

    }

    @Override
    public void run() {

        ResultSet rst = null;
        Statement st;
        Connection con;
        Hashtable<String, String> map = new Hashtable<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloEnviarSMSClientes");

        String sql = "select c.codigoInterno,c.nombreDeCliente,c.celularCliente "
                + "group_concat(fm.numerofactura) as facturas,"
                + "sum(fm.valorARecaudarFactura) as valor "
                + "from"
                + "facturaspormanifiesto fm ,facturascamdun f,clientescamdun c "
                + "where  "
                + " fm.numerofactura=f.numerofactura and  "
                + "f.cliente=c.codigoInterno and "
                + "fm.numeroManifiesto='" + this.manifiesto.getNumeroManifiesto() + "' "
                + "group by c.codigoInterno;";

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    String sms = "";
                    if (rst.getString("celularCliente").length() == 10) {

                        /*Se configura el mensaje, asignando el nobre del cliente,
                        las facturas y el valor a recaudar*/
                        sms = ini.getMensajeSMS().replaceFirst("XXXX", rst.getString("nombreDeCliente"));
                        sms = ini.getMensajeSMS().replaceFirst("YYYY", rst.getString("facturas"));
                        sms = ini.getMensajeSMS().replaceFirst("ZZZZ", nf.format(rst.getString("valor")));
                        // para efectos del mesaje, se remplaza cada espacio por un signo +
                        sms = ini.getMensajeSMS().replace(" ", "+");

                        /*guardamos los valores en un hashMap, el celular destino
                        y el mensaje*/
                        map.put("57" + rst.getString("celularCliente"), sms);

                    }

                }
                rst.close();
                st.close();
                con.close();

                for (Map.Entry<String, String> obj : map.entrySet()) {
                    try {
                        String destinatario = obj.getKey();
                        String mensaje = obj.getValue();
                        System.out.println("clave = " + destinatario + " ->  " + mensaje);

                        /*Se asigna el destinatario y se envia el mensaje*/
                        if(enviarSms(destinatario, mensaje)){
                            CBitacoraSms bitacoraSms = new CBitacoraSms(this.ini,"Camdun",destinatario,mensaje,ini.getUser().getNombreUsuario());
                        }

                    } catch (Exception ex) {
                        System.out.println("Error al enviar mensaje " + ex.getMessage().toString());
                        Logger.getLogger(HiloEnviarSMSClientes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(HiloEnviarSMSClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean enviarSms(String destinatario, String texto) {
       String estado = null;
        try {

            // String usuario="distrilog";
            //String clave="Distri22";
            //String destinatarios = "573164671160";
            //String texto = "mimor+este+es+mi+primer+mensaje+en+la+web,+dedicado+con+todo+mi+amor...";
            // String origen="Distrilog B2B";
            //String urlLink="https://gateway.plusmms.net/send.php?"
            String urlLink = ini.getUrlLinkSMS()
                    + "username=" + ini.getUSuarioSMS() + "&"
                    + "password=" + ini.getClaveSMS() + "&"
                    + "to=" + destinatario + "&"
                    + "text=" + texto + "&"
                    + "from=" + ini.enviaSMS() + "&"
                    + "coding=0&"
                    + "dlr-mask=8";
            //karely@nrsgateway.com
            URL url = new URL(urlLink);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String mensaje;
            while ((mensaje = br.readLine()) != null) {
                sb.append(mensaje);
            }
             estado = sb.toString().substring(0, 1);

            System.out.println(sb.toString());

        } catch (MalformedURLException ex) {
            Logger.getLogger(HiloEnviarSMSClientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloEnviarSMSClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(estado.equals("0")){
            return true;
        }else{
            return false;
        }
        
    }
}
