/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import mtto.vehiculos.Administracion.CTiposDeCombustibles;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeCombustibles implements Runnable {

    Inicio ini;
    IngresoAlSistema form = null;

    ArrayList<CTiposDeCombustibles> arrTiposDeCombustibles = null;

    ResultSet rst = null;

    /**
     * Constructor de clase
     */
    public HiloListadoDeCombustibles(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     */
    public HiloListadoDeCombustibles(Inicio ini, int tiempo, IngresoAlSistema form) {
        this.form = form;

        this.ini = ini;

    }

    @Override
    public void run() {

        if (form != null) {
            listaForm();
        } else {
            listaVacio();
        }

    }

    private void listaForm() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;

        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarra;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCombustibles");

            con = ini.getConnRemota();
            
            CTiposDeCombustibles combustible = new CTiposDeCombustibles(ini);
            arrTiposDeCombustibles = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(combustible.arrListadoDeCombustibles());

                rst.last();
                numeroFilas = rst.getRow();
                rst.beforeFirst();

                form.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    combustible = new CTiposDeCombustibles(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    combustible.setIdCombustible(rst.getInt("idTipoCombustible"));
                    combustible.setNombreCombustible(rst.getString("nombreTipoCombustible"));
                    combustible.setActivoCombustible(rst.getInt("activo"));;

                    System.out.println("Cargando Cargos -> " + combustible.getNombreCombustible());

                    arrTiposDeCombustibles.add(combustible);

                    form.contadorDeRegistros++;
                    contadorDeFilas++;
                    porcentajeBarra = (int) (contadorDeFilas * 100) / numeroFilas;

                    form.barraInferior.setValue(porcentajeBarra);
                    form.barraInferior.repaint();
                    Thread.sleep(10);

                    //porcentajeBarra = (int) (form.ContadorTotalNumeroDeRegistros * 100 / form.totalNumeroDeRegistros);
                    form.barraSuperior.setValue(porcentajeBarra);
                    form.barraSuperior.repaint();
                    Thread.sleep(1);

                    form.txtUsuario.setEnabled(false);
                    form.txtClave.setEnabled(false);

//                    if (form.ContadorTotalNumeroDeRegistros == form.totalNumeroDeRegistros) {
//                        form.barraSuperior.setValue(100);
//                        form.barraSuperior.repaint();
//                        Thread.sleep(1);
//                        IngresoAlSistema_original.band = true;
//                        form.txtUsuario.setEnabled(true);
//                        form.txtClave.setEnabled(true);
//                        form.txtUsuario.requestFocus();
//                    }
                    Thread.sleep(1);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
               // con.close();
                ini.setListaDeTiposDeCombustibles(arrTiposDeCombustibles);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaVacio() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCombustibles");

            con = ini.getConnRemota();
            
            CTiposDeCombustibles combustible = new CTiposDeCombustibles(ini);
            arrTiposDeCombustibles = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(combustible.arrListadoDeCombustibles());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    combustible = new CTiposDeCombustibles(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    combustible.setIdCombustible(rst.getInt("idTipoCombustible"));
                    combustible.setNombreCombustible(rst.getString("nombreTipoCombustible"));
                    combustible.setActivoCombustible(rst.getInt("activo"));;

                    System.out.println("Cargando Cargos -> " + combustible.getNombreCombustible());

                    arrTiposDeCombustibles.add(combustible);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                //con.close();
                ini.setListaDeTiposDeCombustibles(arrTiposDeCombustibles);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
