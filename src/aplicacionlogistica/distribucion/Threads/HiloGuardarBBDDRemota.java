/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es unhilo Thread, que perrmite guardar los registros en una BBDD
 * remota
 *
 *
 * @author Luis Eduardo López Casanova
 */
public class HiloGuardarBBDDRemota implements Runnable {

    Inicio ini;
    String sentenciaSQL = null;
    String mensaje = "";
    String rutaDelArchivo = null;

    /**
     * Método constructor sin parámetros
     *
     *
     */
    public HiloGuardarBBDDRemota() {

    }

    /**
     * Método constructor
     *
     * @param ini clase Inicio que contiene datos de la configuración del
     * sistema
     * @param sentenciaSQL corresponde a cadena con la sentencia SQl que permite
     * realizar la operacion en la BBDD
     * @param mensaje
     *
     */
    public HiloGuardarBBDDRemota(Inicio ini, String sentenciaSQL, String mensaje) {
        this.ini = ini;
        this.sentenciaSQL = sentenciaSQL;
        this.mensaje = mensaje;

    }

    /**
     * Método constructor
     *
     * @param ini clase Inicio que contiene datos de la configuración del
     * sistema
     * @param rutaDelArchivo corresponde a ruta del archivo que se va a
     * ejecutar;
     *
     *
     */
    public HiloGuardarBBDDRemota(Inicio ini, String rutaDelArchivo) {
        this.ini = ini;
        this.rutaDelArchivo = rutaDelArchivo;

    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        boolean ok = false;
        String val = null;
        
            int i = 0;
          
            try {
                if (sentenciaSQL != null) {

                    if (i < 5) {
                        ok = insertar();
                    }
                    i = 5;

                }
                if (rutaDelArchivo != null) {
                    if (i < 5) {
                        ok = insertar(ini, rutaDelArchivo);
                    }
                    i = 5;
                }
                Thread.sleep(3);
            } catch (InterruptedException | SQLException ex) {
                System.err.println(ex.getMessage());
                Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex);
                
            }
       
        // System.out.println("acá  sale del while " + mensaje);

    }

    /**
     * Método que permite guardar los registros en la base de datos remota
     *
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean insertar() {
        boolean insertar = false;
        Statement st = null;
        Connection con = null;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloGuardarBBDDRemota");

        if (con != null) {
            try {
                st = con.createStatement();
                st.execute(sentenciaSQL);
                insertar = true;
                st.close();
                con.close();
            } catch (SQLException ex) {
                try {
                    System.out.println("Error en insertar en la BBDD remota() consulta sql " + ex + "(sql=" + sentenciaSQL + ")");
                    Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex);
                    st.close();
                    con.close();
                    insertar = false;
                } catch (SQLException ex1) {
                    Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }
        return insertar;
    }

    public String insertar(String nada, int nadita) {
        String rpta = "";
        try {
            // DESCRIPCIÓN SERVICIO POR STIVENSON RINCÓN

            /*
            
             * Url del servicio: http://www.logarea.net/distribucion/public/service/insert (Solo se atiende peticiones por post)
             *
             * SE DEBE ENVIAR
             * Variable de nombre: SQL (Contiene el insert que se quiere ejecutar)
             * Variable de nombre: USERNAME
             * Variable de nombre: PASSWORD
            
             *  DEVUELVE
             *   0: Si hubo un error autenticando
             *   1: Si el insert se ejecuto con éxito
             *   2: Si huvo un error en la ejecución de la consulta
             *   3: Si hubo en error recogiendo los parametros que se reciben
             */
            //* ESTE CODIGO FUE PROBADO CON java version "1.8.0_25", compilado con javac 1.8.0_25 y funciona.
            URL url = new URL("http://www.logarea.net/distribucion/public/service/insert");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("SQL", sentenciaSQL);
            params.put("USERNAME", "distribucion");
            params.put("PASSWORD", "123456");

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);

            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0; rpta += (char) c); // Se recibe respuesta

        } catch (MalformedURLException ex) {
            Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar en la BBDD remota() consulta sql " + ex + "(sql=" + sentenciaSQL + ")");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar en la BBDD remota() consulta sql " + ex + "(sql=" + sentenciaSQL + ")");
        } catch (IOException ex) {
            Logger.getLogger(HiloGuardarBBDDRemota.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar en la BBDD remota() consulta sql " + ex + "(sql=" + sentenciaSQL + ")");
        }

        return rpta;

    }

    /**
     * Método que hace posible la insercion de datos a la BBDD remota desde un
     * archivo de texto el cual debe ser con extension .sql.
     *
     *
     * @param rutaDelArchivo ruta del archivo donde se encuentra la fuente de
     * datos.
     * @param ini corresponde a la clase Inicio en la cual está la configuración
     * del sistema
     * @return
     * @throws java.sql.SQLException
     * @see
     * http://www.coderanch.com/t/306966/JDBC/databases/Execute-sql-file-java
     */
    public boolean insertar(Inicio ini, String rutaDelArchivo) throws SQLException {
        String s;
        StringBuilder sb = new StringBuilder();
        Statement st = null;
        Connection con = null;
        boolean grabado = true;

        try {
            FileReader fr = new FileReader(new File(rutaDelArchivo));
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloGuardarBBDDRemota");

            if (con != null) {
                st = con.createStatement();
                for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                    // in order to not execute empty statements
                    if (!inst[i].trim().equals("")) {
                        try {
                            st.executeUpdate(inst[i]);
                            System.out.println(">>" + inst[i]);

                        } catch (Exception ex) {
                            System.out.println("*** Error : " + ex.toString() + inst[i]);
                            grabado = false;
                        }

                    }
                }
                st.close();
                con.close();
            }

        } catch (IOException | SQLException e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            System.out.println("################################################");
            System.out.println(sb.toString());
            
            st.close();
            con.close();
            grabado = false;
        }
        return grabado;
    }

}
