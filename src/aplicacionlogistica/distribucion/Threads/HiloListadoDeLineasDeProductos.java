/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeLineasDeProductos implements Runnable {

    private int tiempo = 5;
    Inicio ini;
    //IngresarManifiestoDeDistribucion form;
    List<String> listaDeLineasDeProductos = null;
    IngresoAlSistema form = null;

    ResultSet rst = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param tiempo
     * @param form
     */
    public HiloListadoDeLineasDeProductos(Inicio ini, int tiempo, IngresoAlSistema form) {
        this.form = form;
        this.tiempo = tiempo;
        this.ini = ini;

    }

    public HiloListadoDeLineasDeProductos(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
        if (form != null) {

            ResultSet rst = null;
            Statement st;

            int numeroFilas;
            double contadorDeFilas = 0;
            int porcentajeBarra;

            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDePlacas");

            con = ini.getConnRemota();
            
            String linea;
            listaDeLineasDeProductos = new ArrayList();

            if (con != null) {
                try {
                    st = con.createStatement();
                    String sql="select distinct linea from productosCamdun";
                    
                    rst = st.executeQuery(sql);

                    
                    rst.last();
                    numeroFilas = rst.getRow();
                    rst.beforeFirst();

                  //  form.totalNumeroDeRegistros += numeroFilas;

                    while (rst.next()) {

                        linea = asignarPropiedades(rst);

                        System.out.println("Cargando vehiculo de placa  -> " + linea);

                        listaDeLineasDeProductos.add(linea);

                       // form.ContadorTotalNumeroDeRegistros++;
                        contadorDeFilas++;
                        porcentajeBarra = (int) (contadorDeFilas * 100) / numeroFilas;

                        form.barraInferior.setValue(porcentajeBarra);
                        form.barraInferior.repaint();
                        Thread.sleep(10);

                       // porcentajeBarra = (int) (form.ContadorTotalNumeroDeRegistros * 100 / form.totalNumeroDeRegistros);
                        form.barraSuperior.setValue(porcentajeBarra);
                        form.barraSuperior.repaint();
                        Thread.sleep(1);

                        form.txtUsuario.setEnabled(false);
                        form.txtClave.setEnabled(false);

                       
                        Thread.sleep(1);

                        System.out.println("tiempo 2 " + new Date());

                        Thread.sleep(1);
                    }
                    rst.close();
                    st.close();
                   // con.close();
                    ini.setListaDeLineasDeProductos(listaDeLineasDeProductos);
                } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
                catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(HiloListadoDeLineasDeProductos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            ResultSet rst = null;
            Statement st;
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDePlacas");

            con = ini.getConnRemota();
            
            String linea;
            listaDeLineasDeProductos = new ArrayList();
                String sql="select distinct linea from productosCamdun";

            if (con != null) {
                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);

                    while (rst.next()) {

                        linea = asignarPropiedades(rst);

                        System.out.println("Cargando vehiculo de placa  -> " + linea);

                        listaDeLineasDeProductos.add(linea);
                        Thread.sleep(10);
                    }
                    rst.close();
                    st.close();
                    //con.close();
                    ini.setListaDeVendedores(listaDeLineasDeProductos);
                } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
                catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(HiloListadoDeLineasDeProductos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    private String asignarPropiedades(ResultSet rst1) throws SQLException {
       String linea;
       
        linea=(rst1.getString("linea"));
        
        return linea;
    }
}
