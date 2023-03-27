/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es unhilo Thread, que perrmite guardar los registros en una BBDD
 * remota
 *
 *
 * @author Luis Eduardo López Casanova
 */
public class HiloGuardarSenteciasSql implements Runnable {

    Inicio ini;
    List<String> listaDeSentenciasSQL = null;
     String cadena = null;
     boolean local=false;

    /**
     * Método constructor sin parámetros
     *
     *
     */
    public HiloGuardarSenteciasSql() {

    }

    /**
     * Método constructor
     *
     * @param ini clase Inicio que contiene datos de la configuración del
     * sistema
     * @param mensaje
     *
     */
    public HiloGuardarSenteciasSql(Inicio ini, List<String> listaDeSentenciasSQL) {
        this.ini = ini;
        this.listaDeSentenciasSQL = listaDeSentenciasSQL;

    }
    
    public HiloGuardarSenteciasSql(Inicio ini, List<String> listaDeSentenciasSQL,boolean local) {
        this.ini = ini;
        this.listaDeSentenciasSQL = listaDeSentenciasSQL;
        this.local=local;

    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        Connection con;//= null;
        Statement st;//= null;
       
        if(local){
           con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal()); 
        }else{
          con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloGuardarSenteciasSql");  
        }

        
       
        grabarDatos(con);

    }

    public void grabarDatos(Connection con) {
        Statement st = null;
        try {
            if (con != null) {

                con.setAutoCommit(false);

                int contador = 0;
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {

                    if (obj.length() > 0) {
                        cadena=""+ obj;
                        System.out.println("dato para grabar Remoto :  " + obj);
                        st.execute(obj);
                        contador++;

                        System.out.println("dato insertado Remoto :  " + obj);
                    }
                    if (contador == 100) {
                        contador = 0;
                        Thread.sleep(100);
                    }
                    Thread.sleep(10);
                }

                con.commit();
                st.close();
                con.close();

            }
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");

            try {
                con.rollback();
                 st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(HiloGuardarSenteciasSql.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
