/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
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
 * @author lelopez
 */
public class CcontrolClass {

    ArrayList<CSvcIncidencias> listaDeIncidencias;
    Date fecha;
    Inicio ini;

    public CcontrolClass(Inicio ini, Date fecha) {
        this.ini = ini;
        this.fecha = fecha;
    }

    public ArrayList<CSvcIncidencias> getListaDeIncidencias() {
        return listaDeIncidencias;
    }

    public ArrayList<CSvcIncidencias> getListaDeIncidencias(Date fecha) {
        listaDeIncidencias = new ArrayList<>();

        ResultSet rst;
        Statement st;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        String sql = "select * from vst_incidenciasCostumerService where consecutivo in("
                + "select consecutivo from incidenciasPorFecha where  fechaGestion=CURRENT_DATE());";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                rst.afterLast();
                int valor = rst.getRow();
                System.out.println("tamaño de la consulta " + valor);
                rst.beforeFirst();
                while (rst.next()) {
                    // System.out.println(new Date() + "Cargando  ->  bitacora factura N° " + numeroDeFactura);

                    CSvcIncidencias incidencia = new CSvcIncidencias();
                    incidencia.ini = this.ini;
                   
                    
                    incidencia.cargarListadoDegestiones(incidencia.getConsecutivo());

                    listaDeIncidencias.add(incidencia);
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return listaDeIncidencias;
    }

    public void setListaDeIncidencias(ArrayList<CSvcIncidencias> listaDeIncidencias) {
        this.listaDeIncidencias = listaDeIncidencias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean validarIncidenciasPendientes() {
        boolean encontrado = false;

        ResultSet rst;
        Statement st;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        String sql = "select * from incidenciasPorFecha where fechaGestion=CURRENT_DATE()";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    encontrado = true;
                }

                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return encontrado;
    }

    public boolean asignarIncidenciasPendientes() {
        boolean encontrado = false;
        ResultSet rst;
        Statement st;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        String sql = "";
        try {
            sql = "INSERT INTO incidenciasPorFecha "
                    + "(consecutivo,fechaGestion,estadoIncidencia,usuario)"
                    + " (SELECT consecutivo,"
                    + "CURRENT_DATE(),'"
                    + "1',"
                    + "'" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "' "
                    //+ "lopez164' "
                    + " FROM vst_incidenciasCostumerService where idEstado=1) "
                    + "ON DUPLICATE KEY UPDATE flag=0; ;";
            if (con != null) {
                try {
                    st = con.createStatement();
                    encontrado = st.execute(sql);

                    st.close();
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

           
        } catch (Exception ex) {
            Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
        }
         return encontrado;
    }

    public ArrayList<CSvcIncidencias> refrescarListadoDeIncidencias(String cadena) {

        ArrayList<CSvcIncidencias> lista = new ArrayList<>();
        ResultSet rst;
        Statement st;
        Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        String sql = "select * from vst_incidenciasCostumerService "
                + "where fechaGestion=CURRENT_DATE() ;";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    // System.out.println(new Date() + "Cargando  ->  bitacora factura N° " + numeroDeFactura);

                    CSvcIncidencias incidencia = new CSvcIncidencias();
                    incidencia.ini = this.ini;


                    incidencia.cargarListadoDegestiones(incidencia.getConsecutivo());

                    lista.add(incidencia);
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CcontrolClass.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return lista;
    }

}
