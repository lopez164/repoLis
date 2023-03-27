/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import mtto.vehiculos.Administracion.CTiposDeDocumentos;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeTiposDeDocumentos implements Runnable {

    List<CTiposDeDocumentos> listaDeTiposDeDocumentos = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListadoDeTiposDeDocumentos(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    public HiloListadoDeTiposDeDocumentos(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
        if (this.barraInf != null) {
            llenarListaCompleta();
            return;
        }else{
            llenarlistaCompleta2();
        }
//
//        if (flistaEmpleados != null) {
//            llenarListaporApellidos(apellido);
//            return;
//        }

//        if (flistaEmpleados != null) {
//            //llenarListaporApellidos(apellido);
//            return;
//        }
//        if (ini != null) {
//            llenarlistaCompleta2();
//        }

    }

    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de
     * empleados en la clase Ini
     *
     * @param ini
     * @param form
     */
    private void llenarlistaCompleta2() throws HeadlessException {
        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            String sql = "select * from tiposdocumentos  order by idtiposDocumentos;";
            CTiposDeDocumentos tiposDeDocumentos = new CTiposDeDocumentos(ini);
            listaDeTiposDeDocumentos = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    tiposDeDocumentos = new CTiposDeDocumentos(ini);
                    
                    tiposDeDocumentos.setIdtiposDocumentos(rst.getInt("idtiposDocumentos"));
                    tiposDeDocumentos.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                    tiposDeDocumentos.setFormato(rst.getString("formato"));
                    tiposDeDocumentos.setTieneVencimiento(rst.getInt("tieneVencimiento"));
                    tiposDeDocumentos.setFechaIng(rst.getDate("fechaIng"));
                    tiposDeDocumentos.setUsuario(rst.getString("usuario"));
                    tiposDeDocumentos.setActivo(rst.getInt("activo"));
                    tiposDeDocumentos.setFlag(rst.getInt("flag"));

                    listaDeTiposDeDocumentos.add(tiposDeDocumentos);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeTiposDeDocumentos(listaDeTiposDeDocumentos);
                //ini.setListaDeEmpleados(arrEmpleados);

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            //band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de
     * empleados en la clase Ini y en la vista de ingreso al sistema para ver
     * las barras de porentaje
     *
     */
    private void llenarListaCompleta() throws HeadlessException {

        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            String sql = "select * from tiposdocumentos where activo=1;";
            listaDeTiposDeDocumentos = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {
                        CTiposDeDocumentos tiposDeDocumentos = new CTiposDeDocumentos(ini);
                     
                    tiposDeDocumentos.setIdtiposDocumentos(rst.getInt("idtiposDocumentos"));
                    tiposDeDocumentos.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                    tiposDeDocumentos.setFormato(rst.getString("formato"));
                    tiposDeDocumentos.setTieneVencimiento(rst.getInt("tieneVencimiento"));
                    tiposDeDocumentos.setFechaIng(rst.getDate("fechaIng"));
                    tiposDeDocumentos.setUsuario(rst.getString("usuario"));
                    tiposDeDocumentos.setActivo(rst.getInt("activo"));
                    tiposDeDocumentos.setFlag(rst.getInt("flag"));

                    listaDeTiposDeDocumentos.add(tiposDeDocumentos);
                    k++;
                    System.out.println("Cargando usuarios -> " + k);

                    /*Le asigna valor a la barra inferior*/
                    y = (int) (k * 100) / numeroFilas;
                    this.barraInf.setValue(y);
                    this.barraInf.repaint();
                    Thread.sleep(1);
                }

                /*Asigna el valor a la Barra de progreso superior*/
                this.contadorDeRegistros += numeroFilas;
                if(totalFilasDeConsultas != 0){
                y = (int) (this.contadorDeRegistros * 100) / totalFilasDeConsultas;
                
                this.barraSup.setValue((int) y);//100
                this.barraSup.repaint();
            }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeTiposDeDocumentos(listaDeTiposDeDocumentos);
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
