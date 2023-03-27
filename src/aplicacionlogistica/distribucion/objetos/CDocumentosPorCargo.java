/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CDocumentosPorCargo {

    ArrayList<CDocumentos> documentos;
    CCargos cargo;
    Inicio ini;

    public CDocumentosPorCargo(Inicio ini,CCargos cargo) {
       this.ini=ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        this.cargo = cargo;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        sql = "Select idTipoDocumento from `documentosPorCargo`  where idCargo=" + cargo.getIdCargo();
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                ArrayList<CDocumentos> arrDocumentos = new ArrayList<>();

                while (rst.next()) {
                    CDocumentos doc = new CDocumentos(ini, rst.getInt(1));
                    arrDocumentos.add(doc);
                }
                this.documentos = arrDocumentos;
            } catch (SQLException ex) {
                 System.out.println("Error en CDocumentosPorCargo 1 sql " + ex.getMessage().toString());
                Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.out.println("Error en CDocumentosPorCargo 2 sql " + ex.getMessage().toString());
                Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    public void setDocumento(ArrayList<CDocumentos> documento) {
        this.documentos = documento;
    }

    public CCargos getCargo() {
        return cargo;
    }

    public void setCargo(CCargos cargo) {
        this.cargo = cargo;
    }

    public ArrayList<CDocumentos> getDocumentos(CCargos cargo) {
        Connection con;
        ArrayList<CDocumentos> arrDocumentos = null;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        sql = "Select idTipoDocumento from `documentosPorCargo`  where idCargo=" + cargo.getIdCargo();
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                 arrDocumentos = new ArrayList<>();

                while (rst.next()) {
                    CDocumentos doc = new CDocumentos(ini, rst.getInt(1));
                    arrDocumentos.add(doc);
                }
                this.documentos=arrDocumentos;
            } catch (SQLException ex) {
                  System.out.println("Error en getDocumentos 1 sql " + ex.getMessage().toString());
                Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                  System.out.println("Error en getDocumentos 2 sql " + ex.getMessage().toString());
                Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
     return arrDocumentos;
    }

     public ArrayList<CDocumentos> getDocumentos(CCargos cargo, CEmpleados empleado, boolean var) {
        String sql = null;
        String desicion;
        Connection con;
        Statement st;
        ResultSet rst;
         ArrayList<CDocumentos> arrDocumentos = null;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

        if (var) {
            desicion = "";
        } else {
            desicion = "not";
        }
        
        sql = "SELECT tiposDocumentos.idtiposDocumentos "
                + "FROM tiposDocumentos,documentosPorCargo "
                + "WHERE "
                + "documentosPorCargo.idtipodocumento=tiposDocumentos.idtiposDocumentos "
                + "and tiposDocumentos.idtiposDocumentos " + desicion + " in (SELECT tiposDocumentos.idtiposDocumentos "
                + "FROM tiposDocumentos,documentosPorEmpleado "
                + "WHERE "
                + "documentosPorEmpleado.idtipodocumento=tiposDocumentos.idtiposDocumentos and "
                + "documentosPorEmpleado.cedula='" + empleado.getCedula() + "' and "
                + "documentosPorEmpleado.idcargo=" + cargo.getIdCargo() + " and documentosPorEmpleado.activo=1 ) "
                + "and documentosPorCargo.idCargo=" + cargo.getIdCargo() ;
        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                arrDocumentos = new ArrayList<>();
                while (rst.next()) {
                   CDocumentos doc = new CDocumentos(ini, rst.getInt(1));
                   arrDocumentos.add(doc); 
                }
                
            }catch (SQLException ex) {
                  System.out.println("Error en getDocumentos 1 sql " + ex.getMessage().toString());
            Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
              System.out.println("Error en getDocumentos 2 sql " + ex.getMessage().toString());
            Logger.getLogger(CDocumentosPorCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
       

        return arrDocumentos;
    }
}
