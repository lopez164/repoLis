/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeCuentasSecundarias implements Runnable {
   
    Inicio ini;
    List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias = null;
    String nit=null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param listaDeCuentasSecundarias
     * @param nit
     */
    public HiloListadoDeCuentasSecundarias(Inicio ini, List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias,String nit) {

        this.ini = ini;
        this.listaDeCuentasSecundarias = listaDeCuentasSecundarias;
        this.nit=nit;

    }

     public HiloListadoDeCuentasSecundarias(Inicio ini) {

        this.ini = ini;

    }
    
    @Override
    public void run() {

        ResultSet rst = null;
        Statement st;

        Connection con;
        try {
            listaDeCuentasSecundarias = new ArrayList<>();
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            
            String sql = null;
            if(nit==null){
                sql = "SELECT "
                        + "cp.idCuentaPrincipal, "
                        + "cp.nombreCuentaPrincipal, "
                        + "cs.idCuentaSecundaria, "
                        + "cs.nombreCuentaSecundaria, "
                        + "cs.codigoCuentaSecundaria, "
                        + "cs.activo "
                        + "FROM cuentassecundariaslogistica cs "
                        + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                        + "where cs.activo=1 "
                        + "order by cs.idCuentaPrincipal,cs.nombreCuentaSecundaria;"; 
            }
            else{
                 sql = "SELECT "
                        + "cp.idCuentaPrincipal, "
                        + "cp.nombreCuentaPrincipal, "
                        + "cs.idCuentaSecundaria, "
                        + "cs.nombreCuentaSecundaria, "
                        + "cs.codigoCuentaSecundaria, "
                        + "cs.activo "
                        + "FROM cuentassecundariaslogistica cs "
                        + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                        + "where cs.activo=1 "
                        + "order by cs.idCuentaPrincipal,cs.nombreCuentaSecundaria;";
            }
            

            if (con != null) {
                st = con.createStatement();
                 
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CCuentaSecundariaLogistica cuenta = new CCuentaSecundariaLogistica();
                   
                    cuenta.setIdCuentaPrincipal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPrincipal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setIdCuentaSecundaria(rst.getInt("idCuentaSecundaria"));
                    cuenta.setNombreCuentaSecundaria(rst.getString("nombreCuentaSecundaria"));
                    cuenta.setCodigoCuentaSecundaria(rst.getString("codigoCuentaSecundaria"));
                    cuenta.setActivo(rst.getInt("activo"));
                    

                    this.listaDeCuentasSecundarias.add(cuenta);

                }
                ini.setListadeCuentasSecundarias(listaDeCuentasSecundarias);
                rst.close();
                st.close();
                con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
                     
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
